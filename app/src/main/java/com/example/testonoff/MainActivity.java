package com.example.testonoff;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.hardware.ConsumerIrManager;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.Toast;

import com.example.testonoff.models.ACDetail;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    Button btOn, btOff;
    ConsumerIrManager ir;
    RecyclerView rvBrand;
    BrandAdapter adapterDB;
    ArrayList<String> listName;
    public static ArrayList<ACDetail> listAC;

    // Tên của CSDL
    String DB_NAME = "ACLOCAL.db";
    // Tên thư mục database, lưu trữ ứng dụng trong thư mục cài đặt gốc
    private String DB_PATH = "/databases/";
    // SQLDatabase để truy vấn và tương tác với dữ liệu
    SQLiteDatabase database = null;

    private static int[] raw = {};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();

        File dbFile = getDatabasePath(DB_NAME);
        // Nếu dbFile chưa tồn tại thì chép vào
        // Nếu đã tồn tại thì xóa đi làm lại
        if (dbFile.exists()) {
            dbFile.delete();
        }
        // sao chép dữ liệu từ database vào
        copyDataBase();
        showListBrandData();
    }


    private void initView() {
        btOn = findViewById(R.id.bt_on);
        btOff = findViewById(R.id.bt_off);
        ir = (ConsumerIrManager) getSystemService(CONSUMER_IR_SERVICE);
        rvBrand = findViewById(R.id.lv_name);
        listName = new ArrayList<>();
        listAC = new ArrayList<>();
        adapterDB = new BrandAdapter(new IItemOnClickOpenBrand() {
            @Override
            public void onClickOpenBrand(String brand) {
                showListDeviceBrand(brand);
                Intent toPower = new Intent(MainActivity.this, PowerActivity.class);
                startActivity(toPower);
            }
        });
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(
                this, RecyclerView.VERTICAL, false
        );
        rvBrand.setLayoutManager(linearLayoutManager);
        rvBrand.setAdapter(adapterDB);

        adapterDB.setData(listName);
    }

    @SuppressLint("NotifyDataSetChanged")
    private void showListBrandData() {
//         Mở CSDL (nếu chưa có thì tạo mới)
        database = openOrCreateDatabase(DB_NAME, MODE_PRIVATE, null);
        Cursor cursor = database.rawQuery("SELECT DISTINCT brand FROM remote", null);

        listName.clear();

        while (cursor.moveToNext()) {
            String name = cursor.getString(0);
            listName.add(name);
        }
        Toast.makeText(this, listName.size()+"", Toast.LENGTH_SHORT).show();
        cursor.close();
        adapterDB.notifyDataSetChanged();
    }

    private void showListDeviceBrand(String brand) {
        database = openOrCreateDatabase(DB_NAME, MODE_PRIVATE, null);
        Cursor cursor = database.rawQuery("SELECT * FROM remote WHERE brand = ?",
                new String[] {brand});
        listAC.clear();
        while (cursor.moveToNext()) {
            String fragment = cursor.getString(1);
            String button_fragment = cursor.getString(2);
            String brandD = cursor.getString(4);
            String frequency = cursor.getString(5);
            String main_frame = cursor.getString(6);
            listAC.add(new ACDetail(fragment, button_fragment, brandD, frequency, main_frame));
        }
        cursor.close();
    }

    public ArrayList<ACDetail> getAC(String brand, String fragment) {
        ArrayList<ACDetail> listAdd;
        listAdd = new ArrayList<>();
        database = openOrCreateDatabase(DB_NAME, MODE_PRIVATE, null);
        Cursor cursor = database.rawQuery("SELECT * FROM remote WHERE brand = ? & fragment = ?",
                new String[] {brand, fragment});
        while (cursor.moveToNext()) {
            String fragmentt = cursor.getString(1);
            String button_fragment = cursor.getString(2);
            String brandD = cursor.getString(4);
            String frequency = cursor.getString(5);
            String main_frame = cursor.getString(6);
            listAdd.add(new ACDetail(fragmentt, button_fragment, brandD, frequency, main_frame));
        }
        cursor.close();
        return listAdd;
    }

    private void copyDataBase() {
        try {
            // Lấy database và đưa vào myInput
            InputStream myInput = getAssets().open(DB_NAME);
            // Lấy đường dẫn lưu trữ để đưa myInput vào
            String outFileName = getApplicationInfo().dataDir + DB_PATH + DB_NAME;
            File f = new File(getApplicationInfo().dataDir + DB_PATH);

            if (!f.exists()) {
                f.mkdir();
            }
            // Mở 1 CSDL rỗng là InputStream
            // myInput -> myOutPut là nơi mà ta sẽ tương tác với CSDL
            OutputStream myOutPut = new FileOutputStream(outFileName);
            // Chép dữ liệu từ myInput vào myOutPut
            byte[] buffer = new byte[1024];
            int len;
            // Lần đọc từng buffer
            while ((len = myInput.read(buffer)) > 0) {
                // Ghi vào nơi lưu trữ là myOutput
                myOutPut.write(buffer, 0, len);
            }
            //Làm rỗng file myOutput
            myOutPut.flush();
            myInput.close();
            myOutPut.close();

        } catch (Exception e) {
            e.printStackTrace();
            Log.e("lỗi sao chép", e.toString());
        }
    }
}
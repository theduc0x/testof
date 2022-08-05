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
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.testonoff.Util.Utils;
import com.example.testonoff.adapter.BrandAdapter;
import com.example.testonoff.models.ACDetail;
import com.example.testonoff.my_interface.IItemOnClickOpenBrand;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    EditText etSearch;
    ConsumerIrManager ir;
    RecyclerView rvBrand;
    BrandAdapter adapterDB;
    ArrayList<String> listName;
    public static ArrayList<ACDetail> listAC;
    public static ArrayList<String> listNameDevice;
    public static ACDetail acDetail;
    public static ArrayList<ACDetail> listRemoteAC;

    // Tên của CSDL
    String DB_NAME = "ACLOCAL.db";
    // Tên thư mục database, lưu trữ ứng dụng trong thư mục cài đặt gốc
    private String DB_PATH = "/databases/";
    // SQLDatabase để truy vấn và tương tác với dữ liệu
    public static SQLiteDatabase database = null;

    private static int[] raw = {};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        database = openOrCreateDatabase(DB_NAME, MODE_PRIVATE, null);
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

        etSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                adapterDB.getFilter().filter(etSearch.getText());
            }

            @Override
            public void afterTextChanged(Editable s) {
                adapterDB.getFilter().filter(etSearch.getText());
            }
        });

        getListModeData();
    }


    private void initView() {
        etSearch = findViewById(R.id.et_search_brand);
        ir = (ConsumerIrManager) getSystemService(CONSUMER_IR_SERVICE);
        rvBrand = findViewById(R.id.lv_name);
        listName = new ArrayList<>();
        listAC = new ArrayList<>();
        listNameDevice = new ArrayList<>();
        listRemoteAC = new ArrayList<>();
        adapterDB = new BrandAdapter(new IItemOnClickOpenBrand() {
            @Override
            public void onClickOpenBrand(String brand) {
                showListDeviceBrand(brand);
                showListDevice(brand);
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
        // Add các giá trị speed
        Utils.getSpeedData();
    }

    @SuppressLint("NotifyDataSetChanged")
    private void showListBrandData() {
        Cursor cursor;
//         Mở CSDL (nếu chưa có thì tạo mới)
//        if (!brand.equals("")) {
//            cursor = database.rawQuery("SELECT DISTINCT brand " +
//                    "FROM remote WHERE brand like ?",
//                    new String[] {brand + "%"});
//        } else {
            cursor = database.rawQuery("SELECT DISTINCT brand FROM remote", null);
//        }
        listName.clear();
        while (cursor.moveToNext()) {
            String name = cursor.getString(0);
            listName.add(name);
        }
        Toast.makeText(this, listName.size() + "", Toast.LENGTH_SHORT).show();
        cursor.close();
        adapterDB.notifyDataSetChanged();
    }

    public void showListDeviceBrand(String brand) {
        database = openOrCreateDatabase(DB_NAME, MODE_PRIVATE, null);
        Cursor cursor =
                database.rawQuery("SELECT DISTINCT fragment, button_fragment, " +
                                "frequency, main_frame FROM remote WHERE brand = ?",
                        new String[]{brand});
        listAC.clear();
        while (cursor.moveToNext()) {
            String fragment = cursor.getString(0);
            String button_fragment = cursor.getString(1);
            String frequency = cursor.getString(2);
            String main_frame = cursor.getString(3);
            listAC.add(new ACDetail(fragment, button_fragment, brand, frequency, main_frame));
        }
        cursor.close();
    }

    public static void showACDetail(String fragment, String button_fragment, String button_fragment2) {
        Cursor cursor =
                database.rawQuery("SELECT DISTINCT fragment, button_fragment, frequency, " +
                                "main_frame FROM remote WHERE fragment = ? AND " +
                                "(button_fragment = ? OR button_fragment = ?)",
                        new String[]{fragment, button_fragment, button_fragment2});
        acDetail = null;
        while (cursor.moveToNext()) {
            String fragmentt = cursor.getString(0);
            String button_fragmentt = cursor.getString(1);
            String frequency = cursor.getString(2);
            String main_frame = cursor.getString(3);
            acDetail = new ACDetail(fragmentt, button_fragmentt, null, frequency, main_frame);
        }
        cursor.close();
    }

    public void showListDevice(String brand) {
        Log.d("addd", brand);
        brand = brand.trim();
        database = openOrCreateDatabase(DB_NAME, MODE_PRIVATE, null);
        Cursor cursor = database.rawQuery(" SELECT DISTINCT fragment FROM remote WHERE brand = ?",
                new String[]{brand});
        listNameDevice.clear();
        while (cursor.moveToNext()) {
            String fragment = cursor.getString(0);
            listNameDevice.add(fragment);
            listNameDevice.add(fragment);
        }
        cursor.close();
    }
    // cho tất cả các sự kiện của một loại device

    /**
     *
     * @param fragment là tên loại thiết bị để show lên danh sách các button của thiết bị đó
     */
    public static void showListRemoteDetail(String fragment) {
//        Cursor cursor = database.rawQuery("SELECT DISTINCT fragment, button_fragment," +
//                        " frequency, main_frame FROM remote WHERE fragment = ?",
//                new String[]{fragment});
        Cursor cursor = database.rawQuery("SELECT DISTINCT fragment, button_fragment, " +
                        "frequency, main_frame FROM remote WHERE fragment = ?" +
                        "AND ((button_fragment like \"%F%\" OR button_fragment = \"Power\" " +
                        "OR button_fragment = \"PowerOn\" OR button_fragment = \"Delay\" " +
                        "OR button_fragment like \"MODE%\") OR button_fragment like \"T%\")",
                new String[]{fragment});
        listRemoteAC.clear();
        while (cursor.moveToNext()) {
            String fragmentt = cursor.getString(0);
            String button_fragmentt = cursor.getString(1);
            String frequency = cursor.getString(2);
            String main_frame = cursor.getString(3);

            listRemoteAC
                    .add(new ACDetail(fragmentt, button_fragmentt, null, frequency, main_frame));
        }
        cursor.close();
    }

    /**
     * Trả về giá trị list MODE có trong database
     */
    public void getListModeData() {
        Cursor cursor = database.rawQuery("SELECT DISTINCT button_fragment " +
                        "FROM remote WHERE button_fragment like \"Mode%\"",
                null);
        Utils.listModeData.clear();
        while (cursor.moveToNext()) {
            String button_fragmentt = cursor.getString(0);
            Utils.listModeData.add(button_fragmentt);
        }
        cursor.close();
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
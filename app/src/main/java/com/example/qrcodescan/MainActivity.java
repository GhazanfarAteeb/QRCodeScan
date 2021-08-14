package com.example.qrcodescan;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;
import com.squareup.picasso.Picasso;

import java.util.Date;

public class MainActivity extends AppCompatActivity {
    SQLiteOpenHelper openHelper;
    SQLiteDatabase db;
    Cursor cursor;
    Button resetButton;
    TextView id;
    TextView name;
    TextView salary;
    ImageView img;
    TextView timestamp;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        openHelper = new DatabaseHelper(this);
        ActivityCompat.requestPermissions(this, new String[] {Manifest.permission.CAMERA}, PackageManager.PERMISSION_GRANTED);
        ActivityCompat.requestPermissions(this, new String[] {Manifest.permission.INTERNET}, PackageManager.PERMISSION_GRANTED);
        resetButton = findViewById(R.id.reset);
        resetButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                id.setText(null);
                name.setText(null);
                salary.setText(null);
                img.setImageResource(0);
                timestamp.setText(null);
            }
        });
    }

    public void scanButton(View view) {
        IntentIntegrator intentIntegrator =new IntentIntegrator(this);
        intentIntegrator.initiateScan();
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        IntentResult intentResult = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if (intentResult !=null) {
            if (intentResult.getContents() ==null) {

            }
            else {
                String code = intentResult.getContents();

                cursor = openHelper.getReadableDatabase().rawQuery("SELECT * FROM " + DatabaseHelper.TABLE_NAME+" WHERE "+ DatabaseHelper.COL_1+"="+code, null);
                if (cursor.getCount() != 0) {
                    while(cursor.moveToNext()) {
                        id = findViewById(R.id.id);
                        id.setText(""+cursor.getInt(0));

                        name = findViewById(R.id.name);
                        name.setText(""+cursor.getString(1));

                        salary = findViewById(R.id.salary);
                        salary.setText(""+cursor.getDouble(2));

                        String URL = cursor.getString(3);
                        img = findViewById(R.id.ImageView);
                        Picasso.with(this).load(URL).into(img);

                        timestamp = findViewById(R.id.timestamp);
                        Date date = new Date();
                        timestamp.setText(date.toString());
                    }
                }
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }
}
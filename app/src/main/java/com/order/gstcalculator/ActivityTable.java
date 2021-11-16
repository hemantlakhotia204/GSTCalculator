package com.order.gstcalculator;


import androidx.annotation.NonNull;

import androidx.appcompat.app.AppCompatActivity;

import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.animation.ObjectAnimator;
import android.app.AlertDialog;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;


import java.text.DecimalFormat;
import java.util.ArrayList;


public class ActivityTable extends AppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener, TableAMTAdapter.ItemListener {

    TextView tvTotalAMT;
    TextView tvTax18, tvTax12, tvTax5, tvGST18, tvGST12, tvGST5;
    RecyclerView rvList;
    TableAMTAdapter mAdapter;
    ArrayList<ConstructorAmount> constructorAmounts;
    ArrayList<String> taxableValue, gst, taxRate;
    float taxValue18, taxValue12, taxValue5, GST18, GST12, GST5;
    View adjustGST;
    int bool = 1;
    LinearLayout layoutGST, layoutArrow;

    Cursor cursor;
    SQLTotalAMT sqlTotalAMT;
    BottomNavigationView bottomNavigationView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_table);

        tvTotalAMT = findViewById(R.id.tvTotalAMT);
        bottomNavigationView = findViewById(R.id.bottom_nav);
        adjustGST = findViewById(R.id.adjustGST);
        layoutGST = findViewById(R.id.layoutGST);
        layoutArrow = findViewById(R.id.layoutArrow);


        bottomNavigationView.setOnNavigationItemSelectedListener(this);
        bottomNavigationView.setSelectedItemId(R.id.menu_table);


        setRV();
        setGST();

        adjustGST.setOnClickListener(v -> {
            float xValue = layoutGST.getWidth();

            Log.d("gst.204","Bool: " + bool);
            if(bool == 0) {
                bool = 1;
                adjustGST.setBackground(ContextCompat.getDrawable(this, R.drawable.ic_baseline_arrow_left_24));
                layoutGST.animate().setDuration(1000).x(0).start();
                layoutArrow.animate().setDuration(1000).x(xValue).start();
//                cardGST.animate().setDuration((1000)).x(0).start();

            } else if (bool == 1) {
                bool = 0;
                adjustGST.setBackground(ContextCompat.getDrawable(this, R.drawable.ic_baseline_arrow_right_24));
                layoutGST.animate().setDuration(1000).x(-xValue).start();
                layoutArrow.animate().setDuration(1000).x(0).start();
//                cardGST.animate().setDuration(1000).x(-xValue/2).start();

            }
        });





    }

    private void setRV() {
        rvList = findViewById(R.id.rvList);

        rvList.setHasFixedSize(true);
        rvList.setLayoutManager(new LinearLayoutManager(this));

        getBill();
    }

    private void getBill() {
        sqlTotalAMT = new SQLTotalAMT(this);
        cursor = sqlTotalAMT.getCursor();
        constructorAmounts = new ArrayList<>();

        if (cursor != null) {
            for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()) {
                constructorAmounts.add(new ConstructorAmount(
                        cursor.getInt(0),
                        cursor.getString(1),
                        cursor.getString(2),
                        cursor.getString(3),
                        cursor.getString(4),
                        cursor.getString(5),
                        cursor.getString(6),
                        cursor.getString(7),
                        cursor.getString(8)
                ));
            }
        }

        float total = 0;
        for (int i = 0; i < constructorAmounts.size(); i++) {

            total += Float.parseFloat(constructorAmounts.get(i).getTotalAmount());

        }
        tvTotalAMT.setText(String.valueOf(total));
        mAdapter = new TableAMTAdapter(this, constructorAmounts);
        rvList.setAdapter(mAdapter);
    }

    public void setGST() {
        tvTax18 = findViewById(R.id.tvTax18);
        tvTax12 = findViewById(R.id.tvTax12);
        tvTax5 = findViewById(R.id.tvTax5);
        tvGST18 = findViewById(R.id.tvGST18);
        tvGST12 = findViewById(R.id.tvGST12);
        tvGST5 = findViewById(R.id.tvGST5);

        sqlTotalAMT = new SQLTotalAMT(this);
        cursor = sqlTotalAMT.getCursor();

        taxableValue = new ArrayList<>();
        gst = new ArrayList<>();
        taxRate = new ArrayList<>();

        for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()) {
            taxableValue.add(cursor.getString(5));
            taxRate.add(cursor.getString(6));
            gst.add(cursor.getString(7));
        }

        for (int i=0; i<taxRate.size(); i++) {
            if (taxRate.get(i).contains("0.18")) {
                taxValue18 += Float.parseFloat(taxableValue.get(i));
                GST18 += Float.parseFloat(gst.get(i));
            } else if(taxRate.get(i).contains("0.12")) {
                taxValue12 += Float.parseFloat(taxableValue.get(i));
                GST12 += Float.parseFloat(gst.get(i));
            } else if (taxRate.get(i).contains("0.05")) {
                taxValue5 += Float.parseFloat(taxableValue.get(i));
                GST5+= Float.parseFloat(gst.get(i));
            }
        }


        tvTax18.setText(String.valueOf(roundTwoDecimal(taxValue18)));
        tvTax12.setText(String.valueOf(roundTwoDecimal(taxValue12)));
        tvTax5.setText(String.valueOf(roundTwoDecimal(taxValue5)));

        tvGST18.setText(String.valueOf(roundTwoDecimal(GST18)));
        tvGST12.setText(String.valueOf(roundTwoDecimal(GST12)));
        tvGST5.setText(String.valueOf(roundTwoDecimal(GST5)));
    }

    private float roundTwoDecimal(float x) {
        DecimalFormat decimalFormat = new DecimalFormat("#.##");
        return Float.parseFloat(decimalFormat.format(x));
    }



    private void alertDialogRefresh() {
        new androidx.appcompat.app.AlertDialog.Builder(this)
                .setMessage("Confirm Refresh" +
                        "It will delete the table created")
                .setPositiveButton("Yes", (dialog, which) -> {
                    tvTotalAMT.setText("0.0");

                    sqlTotalAMT.deleteDB(this);
                    bottomNavigationView.setSelectedItemId(R.id.menu_home);
                })
                .setNegativeButton("NO", (dialog, which) -> bottomNavigationView.setSelectedItemId(R.id.menu_home))
                .show();

    }


    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.menu_home) {
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
            ActivityTable.this.finish();
        } else if (id == R.id.menu_refresh){
            alertDialogRefresh();
            ObjectAnimator animator = ObjectAnimator.ofFloat(tvTotalAMT, "alpha", 100f, 0f);
            animator.setDuration(1000);
            animator.start();
        }
        return true;
}


    @Override
    public void onLongItemCLick(final int index) {

        String message = "Delete Row With ID = " + index;

        new AlertDialog.Builder(this)
                .setTitle("Confirm Delete")
                .setMessage(message)
                .setPositiveButton("Yes", (dialog, which) -> {
                    int result = sqlTotalAMT.deleteRow(index);
                    Log.d("gst.204", result + "");
                    getBill();
                    mAdapter = new TableAMTAdapter(ActivityTable.this, constructorAmounts);
                    rvList.setAdapter(mAdapter);


                })
                .setNegativeButton("No", (dialog, which) -> {

                })
                .show();
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        int action = event.getAction();
        Toast.makeText(this, String.valueOf(action), Toast.LENGTH_SHORT).show();

        return super.onTouchEvent(event);
    }

    @Override
    protected void onResume() {
        bottomNavigationView.setSelectedItemId(R.id.menu_table);
        super.onResume();
    }
}

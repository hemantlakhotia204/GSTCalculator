package com.order.gstcalculator;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;


import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.text.DecimalFormat;

public class MainActivity extends AppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener {
    EditText etSellPrice, etPercentage, etQuantity, etDiscount;
    TextView tvListPrice, tvTaxValue, tvGST, tvTotal;
    Button btnGet;
    float sellPrice;
    float percentage;
    float quantity;
    float discount;
    float listPrice;
    float taxValue;
    float gst;
    float total;
    float price;


    BottomNavigationView bottomNavigationView;
    SQLTotalAMT sqlTotalAMT;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        etSellPrice = findViewById(R.id.etSellPrice);
        etPercentage = findViewById(R.id.etPercentage);
        etQuantity = findViewById(R.id.etQuantity);
        etDiscount = findViewById(R.id.etDiscount);
        tvListPrice = findViewById(R.id.tvListPrice);
        tvTaxValue = findViewById(R.id.tvTaxValue);
        tvGST = findViewById(R.id.tvGST);
        tvTotal = findViewById(R.id.tvTotal);
        btnGet = findViewById(R.id.btnGet);

        bottomNavigationView = findViewById(R.id.bottom_nav);

        bottomNavigationView.setOnNavigationItemSelectedListener(this);
        bottomNavigationView.setSelectedItemId(R.id.menu_home);
        sqlTotalAMT = new SQLTotalAMT(this);

        btnGet.setOnClickListener(v -> calculate());


    }

    private float roundTwoDecimal(float x) {
        DecimalFormat decimalFormat = new DecimalFormat("#.##");
        return Float.parseFloat(decimalFormat.format(x));
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.menu_table) {
            Intent intent = new Intent(this, ActivityTable.class);
            startActivity(intent);
        } else if (id == R.id.menu_refresh) {
            alertDialogRefresh();
        }

        return true;
    }

    @Override
    public void onBackPressed() {
        alertDialogBack();
    }

    @Override
    protected void onResume() {
        bottomNavigationView.setSelectedItemId(R.id.menu_home);
//            Toast.makeText(this, "Resumed", Toast.LENGTH_SHORT).show();
        super.onResume();
    }


    private void alertDialogRefresh() {
        new AlertDialog.Builder(this)
                .setMessage("Confirm Refresh" +
                        "It will delete the table created")
                .setPositiveButton("Yes", (dialog, which) -> {
                    tvListPrice.setText(R.string.list_price);
                    tvTotal.setText(R.string.total_amount);
                    tvTaxValue.setText(R.string.taxable_value);
                    tvGST.setText(R.string.cgst_sgst);
                    sqlTotalAMT.deleteDB(MainActivity.this);
                    bottomNavigationView.setSelectedItemId(R.id.menu_home);
                })
                .setNegativeButton("NO", (dialog, which) -> bottomNavigationView.setSelectedItemId(R.id.menu_home))
                .show();

    }

    private void alertDialogBack() {
        new AlertDialog.Builder(this)
                .setMessage("Confirm Exit")
                .setPositiveButton("Yes", (dialog, which) -> MainActivity.super.onBackPressed())
                .setNegativeButton("NO", null)
                .show();
    }

    private void calculate() {
        if (etQuantity.getText().toString().isEmpty() ||
                etPercentage.getText().toString().isEmpty() ||
                etSellPrice.getText().toString().isEmpty()) {
            Toast.makeText(MainActivity.this, "Please Fill All Entries!", Toast.LENGTH_SHORT).show();
        } else {
            sellPrice = Float.parseFloat(etSellPrice.getText().toString());
            percentage = Float.parseFloat(etPercentage.getText().toString()) / 100;
            quantity = Float.parseFloat(etQuantity.getText().toString());
            Toast.makeText(MainActivity.this, "Submitted", Toast.LENGTH_SHORT).show();
            total = sellPrice * quantity;
            taxValue = total / (1 + percentage);
            listPrice = roundTwoDecimal(taxValue) / quantity;

            listPrice = roundTwoDecimal(listPrice);
            taxValue = listPrice * quantity;
            gst = (taxValue * percentage) / 2;
            total = gst * 2 + taxValue;

            if (!etDiscount.getText().toString().isEmpty()) {
                discount = Float.parseFloat(etDiscount.getText().toString()) / 100;
                listPrice = listPrice - (listPrice * discount);
                taxValue = roundTwoDecimal(listPrice) * quantity;
                gst = (taxValue * percentage) / 2;
                total = taxValue + (2 * roundTwoDecimal(gst));
            }

            listPrice = roundTwoDecimal(listPrice);
            taxValue = roundTwoDecimal(taxValue);
            gst = roundTwoDecimal(gst);
            total = roundTwoDecimal(total);



            String textList = "List Price: " + "\n" + listPrice;
            String textTax = "Taxable Value: " + "\n" + taxValue;
            String textGST = "SGST/CGST: " + "\n" + gst;
            String textTotal = "Total Amount: " + "\n" + total;
            tvListPrice.setText(textList);
            tvTaxValue.setText(textTax);
            tvGST.setText(textGST);
            tvTotal.setText(textTotal);
            etPercentage.setText("");
            etQuantity.setText("");
            etSellPrice.setText("");
            etDiscount.setText("");

            price = listPrice;

            sqlTotalAMT.setBill(quantity, listPrice, discount, price, taxValue, percentage, gst, roundTwoDecimal(total));
        }
    }

}

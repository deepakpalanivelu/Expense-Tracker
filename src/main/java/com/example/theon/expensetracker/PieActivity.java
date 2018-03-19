package com.example.theon.expensetracker;

import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;


import android.graphics.Color;

import android.util.Log;

import com.example.theon.expensetracker.Database.DBHelper;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;


import java.util.ArrayList;
import java.util.List;

public class PieActivity extends AppCompatActivity {

    private static String TAG = "PieActivity";


    PieChart pieChart;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.content_pie);
        Log.d(TAG, "onCreate: starting to create chart");

        pieChart = (PieChart) findViewById(R.id.idPieChart);
        DBHelper db = new DBHelper(this);
        Cursor cur = db.getExpenseByCategory();


        while(cur.moveToNext())
        {
            StringBuffer sb=new StringBuffer();
            sb.append(cur.getString(0)+"\n");
            sb.append(cur.getString(1));
            Log.d("value here is ",sb.toString());
//            ONLY 0 value is being shown for all categories, sum is not happening, once
        }

        List<Entry> entries = new ArrayList<Entry>();
        entries.add(new Entry(4f, 0));
        entries.add(new Entry(1f, 1));
        entries.add(new Entry(3f, 2));
        entries.add(new Entry(2f, 3));

        pieChart.setDescription("Expense by category");
        pieChart.setRotationEnabled(true);
        ArrayList<Integer> colours = new ArrayList<>();
        colours.add(Color.parseColor("#2B4A6F"));
        colours.add(Color.parseColor("#26715B"));

        colours.add(Color.parseColor("#A23645"));
        colours.add(Color.parseColor("#93A63A"));

        pieChart.setUsePercentValues(true);
        pieChart.setHoleColor(Color.BLACK);
        pieChart.setCenterTextColor(Color.WHITE);
        pieChart.setHoleRadius(25f);
        pieChart.setTransparentCircleAlpha(0);
        pieChart.setCenterText("Category");
        pieChart.setCenterTextSize(10);

        PieDataSet dataset = new PieDataSet(entries,"category");

        dataset.setSliceSpace(2);
        dataset.setValueTextSize(12);
        dataset.setColors(colours);


        ArrayList<String> labels = new ArrayList<String>();
                labels.add("Dining");
                labels.add("Entertainment");
                labels.add("Other");
                labels.add("Gas");
        Legend legend = pieChart.getLegend();
        legend.setForm(Legend.LegendForm.CIRCLE);
        legend.setPosition(Legend.LegendPosition.LEFT_OF_CHART);
        PieData data = new PieData(labels, dataset);
        pieChart.setData(data);
        pieChart.animateY(5000);

    }


}
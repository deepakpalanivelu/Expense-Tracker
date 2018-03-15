package com.example.theon.expensetracker;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;


import android.graphics.Color;

import android.util.Log;

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


        List<Entry> entries = new ArrayList<Entry>();
        entries.add(new Entry(4f, 0));
        entries.add(new Entry(8f, 1));
        entries.add(new Entry(6f, 2));
        entries.add(new Entry(12f, 3));


        pieChart.setDescription("Expense by category");
        pieChart.setRotationEnabled(true);
        ArrayList<Integer> colours = new ArrayList<>();
        colours.add(Color.GRAY);
        colours.add(Color.BLUE);
        colours.add(Color.RED);
        colours.add(Color.GREEN);

        pieChart.setUsePercentValues(true);
        pieChart.setHoleColor(Color.BLUE);
        pieChart.setCenterTextColor(Color.BLACK);
        pieChart.setHoleRadius(25f);
        pieChart.setTransparentCircleAlpha(0);
        pieChart.setCenterText("Super Cool Chart");
        pieChart.setCenterTextSize(10);

        PieDataSet dataset = new PieDataSet(entries,"category");

        dataset.setSliceSpace(2);
        dataset.setValueTextSize(12);

        //add colors to dataset


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

    }


}
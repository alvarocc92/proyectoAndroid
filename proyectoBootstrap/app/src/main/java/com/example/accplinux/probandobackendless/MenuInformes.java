package com.example.accplinux.probandobackendless;

import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.RelativeLayout;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.util.ArrayList;
import java.util.List;

public class MenuInformes extends AppCompatActivity {

    BarChart chart ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu_informes);

        chart = (BarChart) findViewById(R.id.chart);

        float[] group1 ={1.0f,2.0f,3.0f,4.0f,3.0f,8.0f,5.0f};
        float[] group2 = {5.0f,3.0f,0.5f,4.0f,7.0f,10.0f,7.0f};

        List<BarEntry> entriesGroup1 = new ArrayList<>();
        List<BarEntry> entriesGroup2 = new ArrayList<>();

        for(int i = 0; i < group1.length; i++) {
            entriesGroup1.add(new BarEntry(i, group1[i]));
            entriesGroup2.add(new BarEntry(i, group2[i]));
        }

        BarDataSet set1 = new BarDataSet(entriesGroup1, "Group 1");
        BarDataSet set2 = new BarDataSet(entriesGroup2, "Group 2");

        final String[] quarters = new String[] { "Enero" , "Febrero" , "Marzo" , "Abril" , "Mayo" , "Junio" , "Julio" };

        IAxisValueFormatter formatter = new IAxisValueFormatter() {

            @Override
            public String getFormattedValue(float value, AxisBase axis) {
                return quarters[(int) value];
            }
        };

        XAxis xAxis = chart.getXAxis();
        xAxis.setGranularity(1f);
        xAxis.setValueFormatter(formatter);

        float groupSpace = 0.06f;
        float barSpace = 0.02f; // x2 dataset
        float barWidth = 0.45f;

        BarData data = new BarData(set1, set2);
        data.setBarWidth(barWidth); // set the width of each bar
        chart.setData(data);
        chart.groupBars(-0.5f, groupSpace, barSpace); // perform the "explicit" grouping

        chart.invalidate();

    }
}


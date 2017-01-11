package com.example.accplinux.probandobackendless;

import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MotionEvent;
import android.widget.RelativeLayout;
import android.widget.Toast;

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
import com.github.mikephil.charting.listener.ChartTouchListener;
import com.github.mikephil.charting.listener.OnChartGestureListener;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.util.ArrayList;
import java.util.List;

public class MenuInformes extends AppCompatActivity implements OnChartGestureListener {

    BarChart chart ;
    Proyecto proyecto;
    List<Empleado> listEmpleados = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu_informes);

        chart = (BarChart) findViewById(R.id.chart);

        chart.setTouchEnabled(true);



        proyecto = (Proyecto) getIntent().getSerializableExtra("proyecto");

        listEmpleados.addAll(proyecto.getEmpleadoAsignados());

        float presupuesto = proyecto.getPresupuesto();
        float gastos = 0f;

        for(int i = 0; i<listEmpleados.size(); i++){
            if(listEmpleados.get(i).getSalario()!=null){
                float salario = 0;
                salario = (listEmpleados.get(i).getSalario().floatValue()/12);
                gastos+=salario;
            }
        }

        List<BarEntry> entriesGroup1 = new ArrayList<>();
        List<BarEntry> entriesGroup2 = new ArrayList<>();

        entriesGroup1.add(new BarEntry(0, presupuesto));
        entriesGroup2.add(new BarEntry(1, gastos));

        BarDataSet set1 = new BarDataSet(entriesGroup1, "Presupuesto");
        set1.setColors(ColorTemplate.COLORFUL_COLORS);
        BarDataSet set2 = new BarDataSet(entriesGroup2, "Gastos");
        set2.setColors(ColorTemplate.MATERIAL_COLORS);


        final String[] quarters = new String[] { proyecto.getNombre()," " };

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
        data.setBarWidth(barWidth);// set the width of each bar
        chart.setOnChartGestureListener(this);
        chart.setData(data);
        chart.groupBars(-0.5f, groupSpace, barSpace); // perform the "explicit" grouping

        chart.invalidate();

    }

    @Override
    public void onChartGestureStart(MotionEvent me, ChartTouchListener.ChartGesture lastPerformedGesture) {

    }

    @Override
    public void onChartGestureEnd(MotionEvent me, ChartTouchListener.ChartGesture lastPerformedGesture) {

    }

    @Override
    public void onChartLongPressed(MotionEvent me) {

        Intent menuInformeDetalles = new Intent(MenuInformes.this, MenuInformesDetalles.class);
        menuInformeDetalles.putExtra("proyecto",proyecto);
        Toast.makeText(this , "Detalle de: "+proyecto.getNombre(), Toast.LENGTH_LONG).show();
        startActivity(menuInformeDetalles);
    }

    @Override
    public void onChartDoubleTapped(MotionEvent me) {

    }

    @Override
    public void onChartSingleTapped(MotionEvent me) {

    }

    @Override
    public void onChartFling(MotionEvent me1, MotionEvent me2, float velocityX, float velocityY) {

    }

    @Override
    public void onChartScale(MotionEvent me, float scaleX, float scaleY) {

    }

    @Override
    public void onChartTranslate(MotionEvent me, float dX, float dY) {

    }
}


package com.example.accplinux.probandobackendless;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MotionEvent;
import android.widget.Toast;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;
import com.github.mikephil.charting.listener.ChartTouchListener;
import com.github.mikephil.charting.listener.OnChartGestureListener;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.util.ArrayList;
import java.util.List;

public class MenuInformesDetalles extends AppCompatActivity {

    PieChart chart ;
    Proyecto proyecto;
    List<Empleado> listEmpleados = new ArrayList<>();
    List<PieEntry> entries = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu_informes_detalles);
        chart = (PieChart) findViewById(R.id.chartDetalles);

        proyecto = (Proyecto) getIntent().getSerializableExtra("proyecto");

        listEmpleados.addAll(proyecto.getEmpleadoAsignados());

        for(int i = 0; i<listEmpleados.size(); i++){
            if(listEmpleados.get(i).getSalario()!=null){
                entries.add(new PieEntry(listEmpleados.get(i).getSalario().floatValue()/12, listEmpleados.get(i).getNombre()));
            }
        }


        PieDataSet set = new PieDataSet(entries, "Detalle Informe");
        set.setColors(ColorTemplate.COLORFUL_COLORS);
        PieData data = new PieData(set);

        chart.setData(data);
        chart.invalidate();

       /* final String[] quarters = new String[] { proyecto.getNombre()," " };

        IAxisValueFormatter formatter = new IAxisValueFormatter() {

            @Override
            public String getFormattedValue(float value, AxisBase axis) {
                return quarters[(int) value];
            }
        };

        XAxis xAxis = chart.getXAxis();
        xAxis.setGranularity(1f);
        xAxis.setValueFormatter(formatter);*/

    }

}

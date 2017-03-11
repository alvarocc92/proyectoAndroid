package com.example.accplinux.probandobackendless;

import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MotionEvent;
import android.widget.Toast;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.Description;
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
import java.util.Date;
import java.util.List;

public class MenuInformesDetalles extends AppCompatActivity {

    PieChart chart ;
    Proyecto proyecto;
    List<Empleado> listEmpleados;
    List<Gastos> listGastos;
    List<PieEntry> entries = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu_informes_detalles);
        chart = (PieChart) findViewById(R.id.chartDetalles);

        chart.setTransparentCircleColor(Color.BLACK);
        chart.setTransparentCircleAlpha(125);
        chart.setUsePercentValues(true);
        chart.setCenterText("Detalle");
        chart.animateX(1000);
        chart.animateY(1000);
        proyecto = (Proyecto) getIntent().getSerializableExtra("proyecto");

        Description descripcion = new Description();
        descripcion.setText(proyecto.getNombre());
        chart.setDescription(descripcion);

        listEmpleados = new ArrayList<>();
        listEmpleados.addAll(proyecto.getEmpleadoAsignados());

        listGastos = new ArrayList<>();
        listGastos.addAll(proyecto.getListGastos());

        Date fechaIni = proyecto.getFechaInicio();
        Date fechaFin = proyecto.getFechaFin();

        long diferencia = fechaFin.getTime()-fechaIni.getTime();
        long dias = diferencia / (1000*60*60*24);

        long meses = dias / 28;

        for(int i = 0; i<listEmpleados.size(); i++){
            if(listEmpleados.get(i).getSalario()!=null){
                entries.add(new PieEntry((listEmpleados.get(i).getSalario().floatValue()/12)*meses, listEmpleados.get(i).getNombre()));
            }
        }

        for(int i = 0; i<listGastos.size(); i++){
            entries.add(new PieEntry(listGastos.get(i).getCantidad(), listGastos.get(i).getDescripcion()));
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

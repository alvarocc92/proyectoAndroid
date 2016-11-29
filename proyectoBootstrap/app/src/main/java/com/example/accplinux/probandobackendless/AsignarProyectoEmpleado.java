package com.example.accplinux.probandobackendless;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

public class AsignarProyectoEmpleado extends AppCompatActivity {

    //ArrayList<String> listEmpleados = new ArrayList<>();
    ArrayList<String> idEmpleados = new ArrayList<>();
    Proyecto proyecto;
    List<Empleado> listEmpleados = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_asignar_proyecto_empleado);

        //idEmpleados = getIntent().getExtras().getStringArrayList("idEmpleados");
        //listEmpleados = getIntent().getExtras().getStringArrayList("nombreEmpleados");

        proyecto = (Proyecto) getIntent().getExtras().getSerializable("proyecto");
        listEmpleados = (List<Empleado>) getIntent().getExtras().getSerializable("listEmpleados");


        CustomAdapterAsignarEmpleado adapter = new CustomAdapterAsignarEmpleado(proyecto,listEmpleados,this);

        ListView lView = (ListView)findViewById(R.id.activity_asignar_proyecto_empleado);
        lView.setAdapter(adapter);

    }
}

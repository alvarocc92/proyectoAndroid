package com.example.accplinux.probandobackendless;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.ListView;

public class ListarEmpleados extends AppCompatActivity {
    ListView tablaEmpleados;
    ArrayAdapter<String> adaptador;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_listar_empleados);
        tablaEmpleados = (ListView) findViewById(R.id.tablaEmpleados);
        adaptador = new ArrayAdapter(this,android.R.layout.simple_list_item_1,getIntent().getExtras().getStringArrayList("listado"));
        tablaEmpleados.setAdapter(adaptador);
    }
}

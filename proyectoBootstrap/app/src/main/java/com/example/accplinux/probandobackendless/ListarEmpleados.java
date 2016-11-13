package com.example.accplinux.probandobackendless;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

public class ListarEmpleados extends AppCompatActivity {
    ListView tablaEmpleados;
    ArrayAdapter<String> adaptador;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ArrayList<String> list = new ArrayList<String>();
        setContentView(R.layout.activity_listar_empleados);

        list = getIntent().getExtras().getStringArrayList("listado");

        CustomAdapter adapter = new CustomAdapter(list, this);

        ListView lView = (ListView)findViewById(R.id.tablaEmpleados);
        lView.setAdapter(adapter);
        //tablaEmpleados = (ListView) findViewById(R.id.tablaEmpleados);
        //adaptador = new ArrayAdapter(this,android.R.layout.simple_list_item_1,getIntent().getExtras().getStringArrayList("listado"));
        //tablaEmpleados.setAdapter(adaptador);
    }
}

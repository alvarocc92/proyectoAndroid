package com.example.accplinux.probandobackendless;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

public class ListarProyectos extends AppCompatActivity {


    //ArrayList<String> listProyectos = new ArrayList<>();
    //ArrayList<String> idEmpleados = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_listar_proyectos);

        final List<Proyecto> listProyectos = (List<Proyecto>) getIntent().getSerializableExtra("listProyectos");

        //idEmpleados = getIntent().getExtras().getStringArrayList("idEmpleados");

        CustomAdapterProyectos customProyectos = new CustomAdapterProyectos(listProyectos, this);

        ListView lView = (ListView)findViewById(R.id.activity_listar_proyectos);
        lView.setAdapter(customProyectos);
    }
}

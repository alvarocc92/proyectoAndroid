package com.example.accplinux.probandobackendless;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ListView;
import java.util.ArrayList;

public class ListarEmpleados extends AppCompatActivity {

    ArrayList<String> listEmpleados = new ArrayList<>();
    ArrayList<String> idEmpleados = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_listar_empleados);

        listEmpleados = getIntent().getExtras().getStringArrayList("listado");
        idEmpleados = getIntent().getExtras().getStringArrayList("idEmpleados");

        CustomAdapter adapter = new CustomAdapter(listEmpleados,idEmpleados, this);

        ListView lView = (ListView)findViewById(R.id.tablaEmpleados);
        lView.setAdapter(adapter);
    }
}

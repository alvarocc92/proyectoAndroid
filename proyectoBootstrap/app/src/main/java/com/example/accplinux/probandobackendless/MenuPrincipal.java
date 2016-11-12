package com.example.accplinux.probandobackendless;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.beardedhen.androidbootstrap.BootstrapCircleThumbnail;

public class MenuPrincipal extends AppCompatActivity {

    BootstrapCircleThumbnail empleados;
    BootstrapCircleThumbnail proyectos;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu_principal);

        empleados = (BootstrapCircleThumbnail) findViewById(R.id.empleados);
        proyectos = (BootstrapCircleThumbnail) findViewById(R.id.proyectos);

        empleados.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent menuEmpleados = new Intent(MenuPrincipal.this,MenuEmpleados.class);
                startActivity(menuEmpleados);

            }
        });

        proyectos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent menuProyectos = new Intent(MenuPrincipal.this,MenuProyectos.class);
                startActivity(menuProyectos);

            }
        });

    }
}

package com.example.accplinux.probandobackendless;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.backendless.Backendless;
import com.backendless.async.callback.BackendlessCallback;
import com.backendless.exceptions.BackendlessFault;

import java.text.ParseException;
import java.text.SimpleDateFormat;

public class NuevoProyecto extends AppCompatActivity {

    EditText nombreProyecto,jefeProyecto,presupuesto,cliente,fechaInicio,fechaFin;
    Button guardarProyecto;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nuevo_proyecto);

        nombreProyecto = (EditText) findViewById(R.id.nombreProyecto);
        jefeProyecto = (EditText) findViewById(R.id.jefeProyecto);
        presupuesto = (EditText) findViewById(R.id.presupuesto);
        cliente = (EditText) findViewById(R.id.cliente);
        fechaInicio = (EditText) findViewById(R.id.fechaInicio);
        fechaFin = (EditText) findViewById(R.id.fechaFin);

        guardarProyecto = (Button) findViewById(R.id.guardarProyecto);

        guardarProyecto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Proyecto proyecto= new Proyecto();
                SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");

                proyecto.setNombre(nombreProyecto.getText().toString());
                proyecto.setJefeProyecto(jefeProyecto.getText().toString());
                proyecto.setPresupuesto(Long.parseLong(presupuesto.getText().toString()));
                proyecto.setCliente(cliente.getText().toString());
                try {
                    proyecto.setFechaInicio(format.parse(fechaInicio.getText().toString()));
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                try {
                    proyecto.setFechaFin(format.parse(fechaFin.getText().toString()));
                } catch (ParseException e) {
                    e.printStackTrace();
                }

                guardarProyecto(proyecto);
            }
        });
    }
    public void guardarProyecto(Proyecto proyecto) {

        Backendless.Persistence.save(new Proyecto(proyecto.getNombre(), proyecto.getJefeProyecto(), proyecto.getPresupuesto(), proyecto.getCliente(), proyecto.getFechaInicio(),proyecto.getFechaFin(),false), new BackendlessCallback<Proyecto>() {
            @Override
            public void handleResponse(Proyecto proyecto) {
                Log.i("Empleado", "Nuevo proyecto registrado" + proyecto.getNombre());
                Log.i("Empleado", "id del proyecto: " + proyecto.getObjectId());

                Toast.makeText(getApplicationContext(), "Proyecto registrado.", Toast.LENGTH_LONG).show();
                Intent menuProyectos = new Intent(NuevoProyecto.this, MenuProyectos.class);
                startActivity(menuProyectos);
            }
            @Override
            public void handleFault(BackendlessFault backendlessFault) {
                Toast.makeText(getApplicationContext(), backendlessFault.getMessage(), Toast.LENGTH_LONG).show();
                Toast.makeText(getApplicationContext(), backendlessFault.getCode(), Toast.LENGTH_LONG).show();
            }
        });
    }
}

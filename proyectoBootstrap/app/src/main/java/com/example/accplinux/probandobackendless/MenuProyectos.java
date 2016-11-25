package com.example.accplinux.probandobackendless;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.backendless.Backendless;
import com.backendless.BackendlessCollection;
import com.backendless.async.callback.AsyncCallback;
import com.backendless.exceptions.BackendlessFault;
import com.beardedhen.androidbootstrap.BootstrapButton;
import java.io.Serializable;


import java.util.ArrayList;
import java.util.List;

public class MenuProyectos extends AppCompatActivity {

    BootstrapButton listarProyectos,newProyecto,antiguosProyectos;
    //ArrayList<String> mostrarProyectos =new ArrayList<>();
    //ArrayList<String> idProyectos = new ArrayList<>();
    List<Proyecto> listProyectos = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu_proyectos);

        listarProyectos = (BootstrapButton) findViewById(R.id.listarProyectos);
        newProyecto = (BootstrapButton) findViewById(R.id.newProyecto);
        antiguosProyectos = (BootstrapButton) findViewById(R.id.proyectosFinalizados);

        cargarProyectos();

        newProyecto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                crearNuevoProyecto();
            }
        });

        listarProyectos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               /* while(bar.getProgress()<100){
                    bar.setProgress(randomProgress(bar.getProgress(), 100));
                }*/
                Intent activityListarProyectos = new Intent(MenuProyectos.this,ListarProyectos.class);
                activityListarProyectos.putExtra("listProyectos", (Serializable) listProyectos);
                startActivity(activityListarProyectos);
            }
        });
    }

    public void crearNuevoProyecto(){
        Intent nuevoProyecto = new Intent(MenuProyectos.this,NuevoProyecto.class);
        startActivity(nuevoProyecto);
    }

    public void cargarProyectos(){

        Backendless.Persistence.of(Proyecto.class).find(new AsyncCallback<BackendlessCollection<Proyecto>>(){
            @Override
            public void handleResponse( BackendlessCollection<Proyecto> foundContacts )
            {
                for(int i =0 ; i<foundContacts.getTotalObjects();i++){
                    listProyectos.add(foundContacts.getData().get(i));
                     // String nombreCompleto = foundContacts.getData().get(i).getNombre()+" "+foundContacts.getData().get(i);
                    //  mostrarProyectos.add(nombreCompleto);
                   //  idProyectos.add(nombreCompleto);
                }
            }
            @Override
            public void handleFault( BackendlessFault fault )
            {
                Toast.makeText(getApplicationContext(), fault.getMessage(), Toast.LENGTH_LONG).show();
                Toast.makeText(getApplicationContext(), fault.getCode(), Toast.LENGTH_LONG).show();
            }
        });
    }

}

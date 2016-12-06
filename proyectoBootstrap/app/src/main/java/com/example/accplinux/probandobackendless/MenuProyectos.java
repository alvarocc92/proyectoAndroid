package com.example.accplinux.probandobackendless;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
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
    List<Proyecto> listProyectos = new ArrayList<>();
    ProgressDialog pDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu_proyectos);

        listarProyectos = (BootstrapButton) findViewById(R.id.listarProyectos);
        newProyecto = (BootstrapButton) findViewById(R.id.newProyecto);
        antiguosProyectos = (BootstrapButton) findViewById(R.id.proyectosFinalizados);

        newProyecto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                crearNuevoProyecto();
            }
        });

        listarProyectos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                pDialog = new ProgressDialog(MenuProyectos.this);
                pDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
                pDialog.setMessage("Cargando proyectos...");
                pDialog.setCancelable(true);
                pDialog.setMax(100);

                CargarProyectos cargarProyectos = new CargarProyectos();
                cargarProyectos.execute();
            }
        });

    }

    public void crearNuevoProyecto(){
        Intent nuevoProyecto = new Intent(MenuProyectos.this,NuevoProyecto.class);
        startActivity(nuevoProyecto);
    }

    private class CargarProyectos extends AsyncTask<Void, Integer, Boolean> {

        @Override
        protected Boolean doInBackground(Void... params) {


            final boolean[] acabado = {false};

            Backendless.Persistence.of(Proyecto.class).find(new AsyncCallback<BackendlessCollection<Proyecto>>(){
                @Override
                public void handleResponse( BackendlessCollection<Proyecto> foundContacts )
                {
                    for(int i =0 ; i<foundContacts.getTotalObjects();i++){
                        listProyectos.add(foundContacts.getData().get(i));
                    }
                    acabado[0] = true;
                    onPostExecute(acabado[0]);
                }
                @Override
                public void handleFault( BackendlessFault fault )
                {
                    Toast.makeText(MenuProyectos.this, fault.getMessage(), Toast.LENGTH_LONG).show();
                    Toast.makeText(MenuProyectos.this, fault.getCode(), Toast.LENGTH_LONG).show();
                }
            });
            return acabado[0];
        }

        @Override
        protected void onProgressUpdate(Integer... values) {

            int progreso = values[0].intValue();
            pDialog.setProgress(progreso);

        }

        @Override
        protected void onPreExecute() {

            pDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
                @Override
                public void onCancel(DialogInterface dialog) {
                    MenuProyectos.CargarProyectos.this.cancel(true);
                }
            });

            pDialog.setProgress(0);
            pDialog.show(); //sirve para mostrar el dialogo
        }

        @Override
        protected void onPostExecute(Boolean result) {
            if(result)
            {
                pDialog.dismiss();

                Toast.makeText(MenuProyectos.this, "Proyectos cargados." ,Toast.LENGTH_SHORT).show();

                Intent activityListarProyectos = new Intent(MenuProyectos.this,ListarProyectos.class);
                activityListarProyectos.putExtra("listProyectos", (Serializable) listProyectos);
                startActivity(activityListarProyectos);

            }
        }

        @Override
        protected void onCancelled() {
            Toast.makeText(MenuProyectos.this, "Tarea cancelada!", Toast.LENGTH_SHORT).show();
        }
    }
}

/*

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

 */
package com.example.accplinux.probandobackendless;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.backendless.Backendless;
import com.backendless.BackendlessCollection;
import com.backendless.async.callback.AsyncCallback;
import com.backendless.exceptions.BackendlessFault;
import com.backendless.persistence.BackendlessDataQuery;
import com.beardedhen.androidbootstrap.BootstrapButton;
import java.io.Serializable;


import java.util.ArrayList;
import java.util.List;

public class MenuProyectos extends AppCompatActivity implements SearchView.OnQueryTextListener, MenuItemCompat.OnActionExpandListener {

    BootstrapButton listarProyectos,newProyecto,antiguosProyectos;
    List<Proyecto> listProyectos = new ArrayList<>();
    ProgressDialog pDialog;
    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu_proyectos);

        listarProyectos = (BootstrapButton) findViewById(R.id.listarProyectos);
        newProyecto = (BootstrapButton) findViewById(R.id.newProyecto);
        antiguosProyectos = (BootstrapButton) findViewById(R.id.proyectosFinalizados);
        toolbar = (Toolbar) findViewById(R.id.tool_bar);


        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

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

        antiguosProyectos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                pDialog = new ProgressDialog(MenuProyectos.this);
                pDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
                pDialog.setMessage("Cargando proyectos...");
                pDialog.setCancelable(true);
                pDialog.setMax(100);

                CargarAntiguosProyectos cargarAntiguosProyectos = new CargarAntiguosProyectos();
                cargarAntiguosProyectos.execute();
            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_actions, menu);

        MenuItem searchItem = menu.findItem(R.id.buscar);

        SearchView searchView = (SearchView) MenuItemCompat.getActionView(searchItem);

        searchView.setOnQueryTextListener(this);
        MenuItemCompat.setOnActionExpandListener(searchItem, this);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.home:
                Intent menuPrincipal = new Intent (MenuProyectos.this,MenuPrincipal.class);
                startActivity(menuPrincipal);
            case R.id.buscar:
                return true;
            case R.id.miCuenta:
                Intent mi_cuenta = new Intent(MenuProyectos.this, MiCuenta.class);
                startActivity(mi_cuenta);
                return true;
            case R.id.logout:
                Backendless.UserService.logout( new AsyncCallback<Void>()
                {
                    public void handleResponse( Void response )
                    {
                        Toast.makeText(getApplicationContext(), "Sesión finalizada.", Toast.LENGTH_SHORT).show();
                        Intent login = new Intent(MenuProyectos.this,MainActivity.class);
                        startActivity(login);
                    }
                    public void handleFault( BackendlessFault fault )
                    {
                        Toast.makeText(getApplicationContext(), fault.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public boolean onQueryTextChange(String arg0) {
        return false;
    }

    @Override
    public boolean onQueryTextSubmit(String arg0) {

        String whereClause = "nombre LIKE '%"+arg0+"%' OR  cliente LIKE '%"+arg0+"%'";
        BackendlessDataQuery dataQuery = new BackendlessDataQuery();
        dataQuery.setWhereClause( whereClause );

        Backendless.Persistence.of( Proyecto.class ).find( dataQuery,
                new AsyncCallback<BackendlessCollection<Proyecto>>(){
                    @Override
                    public void handleResponse( BackendlessCollection<Proyecto> foundContacts )
                    {
                        if(foundContacts.getTotalObjects()>0){

                            for(int i = 0; i<foundContacts.getTotalObjects(); i++){
                                listProyectos.add(foundContacts.getData().get(i));
                                //listBusqueadaEmpleados.add(foundContacts.getData().get(i));
                            }
                            Intent busquedaProyectos = new Intent(MenuProyectos.this,ListarProyectos.class);
                            busquedaProyectos.putExtra("listProyectos", (Serializable) listProyectos);
                            startActivity(busquedaProyectos);
                        }else{
                            Toast.makeText(MenuProyectos.this, "No hay proyectos con esa búsqueda.", Toast.LENGTH_LONG).show();
                        }
                    }
                    @Override
                    public void handleFault( BackendlessFault fault )
                    {
                        Toast.makeText(MenuProyectos.this, fault.getMessage(), Toast.LENGTH_LONG).show();
                        Toast.makeText(MenuProyectos.this, fault.getCode(), Toast.LENGTH_LONG).show();
                    }
                });
        return false;
    }

    public boolean onMenuItemActionCollapse(MenuItem arg0) {
        return true;
    }
    public boolean onMenuItemActionExpand(MenuItem arg0) {
        return true;
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
                        if(foundContacts.getData().get(i).getFinalizado().equals(false)){
                            listProyectos.add(foundContacts.getData().get(i));
                        }
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

    private class CargarAntiguosProyectos extends AsyncTask<Void, Integer, Boolean> {

        @Override
        protected Boolean doInBackground(Void... params) {


            final boolean[] acabado = {false};

            Backendless.Persistence.of(Proyecto.class).find(new AsyncCallback<BackendlessCollection<Proyecto>>(){
                @Override
                public void handleResponse( BackendlessCollection<Proyecto> foundContacts )
                {
                    for(int i =0 ; i<foundContacts.getTotalObjects();i++){
                        if(foundContacts.getData().get(i).getFinalizado().equals(true)){
                            listProyectos.add(foundContacts.getData().get(i));
                        }
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
                    MenuProyectos.CargarAntiguosProyectos.this.cancel(true);
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

                Intent activityListarProyectos = new Intent(MenuProyectos.this,ListarAntiguosProyectos.class);
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
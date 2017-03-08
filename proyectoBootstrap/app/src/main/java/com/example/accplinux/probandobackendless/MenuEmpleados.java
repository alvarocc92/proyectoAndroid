package com.example.accplinux.probandobackendless;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.os.AsyncTask;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.Toast;

import com.backendless.Backendless;
import com.backendless.BackendlessCollection;
import com.backendless.BackendlessUser;
import com.backendless.Persistence;
import com.backendless.async.callback.AsyncCallback;
import com.backendless.async.callback.BackendlessCallback;
import com.backendless.exceptions.BackendlessFault;
import com.backendless.persistence.BackendlessDataQuery;
import com.backendless.persistence.QueryOptions;
import com.backendless.property.ObjectProperty;
import com.beardedhen.androidbootstrap.BootstrapButton;
import com.beardedhen.androidbootstrap.BootstrapProgressBar;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Random;

public class MenuEmpleados extends AppCompatActivity implements SearchView.OnQueryTextListener, MenuItemCompat.OnActionExpandListener {

    Toolbar toolbar;
    BootstrapButton newEmpleado,listarEmpleados,listarAntiguosEmpleados;
    ProgressDialog pDialog;

    ImageView gif;
    AnimationDrawable frame;
    View actividad;

    ArrayList<String> mostrarEmpleados = new ArrayList<>();
    ArrayList<String> idEmpleados = new ArrayList<>();

    //List<Empleado> listBusqueadaEmpleados = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu_empleados);

        newEmpleado = (BootstrapButton) findViewById(R.id.newEmpleado);
        listarEmpleados = (BootstrapButton) findViewById(R.id.listarEmpleados);
        listarAntiguosEmpleados = (BootstrapButton) findViewById(R.id.listarAntiguosEmpleados);

        actividad = (View) findViewById(R.id.activity_menu_empleados);

        gif = (ImageView) findViewById(R.id.gif);
        gif.setBackgroundResource(R.drawable.gif_empleado);
        frame = (AnimationDrawable) gif.getBackground();
        frame.start();

        toolbar = (Toolbar) findViewById(R.id.tool_bar);

        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

        newEmpleado.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                registrarEmpleado();
            }
        });

        listarEmpleados.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                pDialog = new ProgressDialog(MenuEmpleados.this);
                pDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
                pDialog.setMessage("Cargando empleados...");
                pDialog.setCancelable(true);
                pDialog.setMax(100);

                CargarEmpleados cargarEmpleados = new CargarEmpleados();
                cargarEmpleados.execute();

            }
        });

        listarAntiguosEmpleados.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                pDialog = new ProgressDialog(MenuEmpleados.this);
                pDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
                pDialog.setMessage("Cargando empleados...");
                pDialog.setCancelable(true);
                pDialog.setMax(100);

                CargarAntiguosEmpleados cargarAntiguosEmpleados = new CargarAntiguosEmpleados();
                cargarAntiguosEmpleados.execute();

            }
        });
    }

    public void registrarEmpleado(){
        Intent crearEmpleado = new Intent(MenuEmpleados.this, CrearEmpleado.class);
        startActivity(crearEmpleado);
    }

    @Override
    protected void onDestroy() {
        frame.stop();
        super.onDestroy();

        unbindDrawables(actividad);

    }

    private void unbindDrawables(View view) {
        if (view.getBackground() != null) {
            view.getBackground().setCallback(null);
        }
        if (view instanceof ViewGroup) {
            for (int i = 0; i < ((ViewGroup) view).getChildCount(); i++) {
                unbindDrawables(((ViewGroup) view).getChildAt(i));
            }
            ((ViewGroup) view).removeAllViews();
        }
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
                Intent menuPrincipal = new Intent (MenuEmpleados.this,MenuPrincipal.class);
                startActivity(menuPrincipal);
                return true;
            case R.id.buscar:
                return true;
            case R.id.miCuenta:
                Intent mi_cuenta = new Intent(MenuEmpleados.this, MiCuenta.class);
                startActivity(mi_cuenta);
                return true;
            case R.id.logout:
                Backendless.UserService.logout( new AsyncCallback<Void>()
                {
                    public void handleResponse( Void response )
                    {
                        Toast.makeText(getApplicationContext(), "Sesión finalizada.", Toast.LENGTH_SHORT).show();
                        Intent login = new Intent(MenuEmpleados.this,MainActivity.class);
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

        String whereClause = "nombre LIKE '%"+arg0+"%' OR  apellidos LIKE '%"+arg0+"%'";
        BackendlessDataQuery dataQuery = new BackendlessDataQuery();
        dataQuery.setWhereClause( whereClause );

        Backendless.Persistence.of( Empleado.class ).find( dataQuery,
                new AsyncCallback<BackendlessCollection<Empleado>>(){
                    @Override
                    public void handleResponse( BackendlessCollection<Empleado> foundContacts )
                    {
                        if(foundContacts.getTotalObjects()>0){

                            for(int i = 0; i<foundContacts.getTotalObjects(); i++){
                                if(foundContacts.getData().get(i).getAntiguo().equals(false)){
                                    String nombreCompleto = foundContacts.getData().get(i).getNombre()+" "+foundContacts.getData().get(i).getApellidos();
                                    mostrarEmpleados.add(nombreCompleto);
                                    idEmpleados.add(foundContacts.getData().get(i).getObjectId());
                                }
                            }
                            Intent listEmpleados = new Intent(MenuEmpleados.this,ListarEmpleados.class);
                            listEmpleados.putExtra("listado",mostrarEmpleados);
                            listEmpleados.putExtra("idEmpleados",idEmpleados);
                            startActivity(listEmpleados);
                        }else{
                            Toast.makeText(MenuEmpleados.this, "No hay empleados con esa búsqueda.", Toast.LENGTH_LONG).show();
                        }
                    }
                    @Override
                    public void handleFault( BackendlessFault fault )
                    {
                        Toast.makeText(MenuEmpleados.this, fault.getMessage(), Toast.LENGTH_LONG).show();
                        Toast.makeText(MenuEmpleados.this, fault.getCode(), Toast.LENGTH_LONG).show();
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

    public class CargarEmpleados extends AsyncTask<Void, Integer, Boolean> {

        @Override
        protected Boolean doInBackground(Void... params) {

            final boolean[] acabado = {false};

            BackendlessDataQuery dataQuery = new BackendlessDataQuery();
            dataQuery.setPageSize(40);

            Backendless.Persistence.of(Empleado.class).find( dataQuery, new AsyncCallback<BackendlessCollection<Empleado>>(){
                @Override
                public void handleResponse( BackendlessCollection<Empleado> foundContacts )
                {
                    for(int i =0 ; i<foundContacts.getTotalObjects();i++){
                            if(foundContacts.getData().get(i).getAntiguo().equals(false)){
                                String nombreCompleto = foundContacts.getData().get(i).getNombre()+" "+foundContacts.getData().get(i).getApellidos();
                                mostrarEmpleados.add(nombreCompleto);
                                idEmpleados.add(foundContacts.getData().get(i).getObjectId());
                            }
                    }
                    acabado[0] = true;
                    onPostExecute(acabado[0]);
                }
                @Override
                public void handleFault( BackendlessFault fault )
                {
                    Toast.makeText(MenuEmpleados.this, fault.getMessage(), Toast.LENGTH_LONG).show();
                    Toast.makeText(MenuEmpleados.this, fault.getCode(), Toast.LENGTH_LONG).show();
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
                    CargarEmpleados.this.cancel(true);
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

                Toast.makeText(MenuEmpleados.this, "Empleados cargados." ,Toast.LENGTH_SHORT).show();

                Intent listEmpleados = new Intent(MenuEmpleados.this,ListarEmpleados.class);
                listEmpleados.putExtra("listado",mostrarEmpleados);
                listEmpleados.putExtra("idEmpleados",idEmpleados);
                startActivity(listEmpleados);

            }
        }

        @Override
        protected void onCancelled() {
            Toast.makeText(MenuEmpleados.this, "Tarea cancelada!", Toast.LENGTH_SHORT).show();
        }
    }

    private class CargarAntiguosEmpleados extends AsyncTask<Void, Integer, Boolean> {

        @Override
        protected Boolean doInBackground(Void... params) {


            final boolean[] acabado = {false};

            BackendlessDataQuery dataQuery = new BackendlessDataQuery();
            dataQuery.setPageSize(40);

            Backendless.Persistence.of(Empleado.class).find( dataQuery, new AsyncCallback<BackendlessCollection<Empleado>>(){
                @Override
                public void handleResponse( BackendlessCollection<Empleado> foundContacts )
                {
                    for(int i =0 ; i<foundContacts.getTotalObjects();i++){
                        if(foundContacts.getData().get(i).getAntiguo().equals(true)){
                            String nombreCompleto = foundContacts.getData().get(i).getNombre()+" "+foundContacts.getData().get(i).getApellidos();
                            mostrarEmpleados.add(nombreCompleto);
                            idEmpleados.add(foundContacts.getData().get(i).getObjectId());
                        }
                    }

                    acabado[0] = true;
                    onPostExecute(acabado[0]);
                }
                @Override
                public void handleFault( BackendlessFault fault )
                {
                    Toast.makeText(MenuEmpleados.this, fault.getMessage(), Toast.LENGTH_LONG).show();
                    Toast.makeText(MenuEmpleados.this, fault.getCode(), Toast.LENGTH_LONG).show();
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
                    CargarAntiguosEmpleados.this.cancel(true);
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

                Toast.makeText(MenuEmpleados.this, "Antiguos empleados cargados." ,Toast.LENGTH_SHORT).show();

                Intent listEmpleados = new Intent(MenuEmpleados.this,ListarAntiguosEmpleados.class);
                listEmpleados.putExtra("listado",mostrarEmpleados);
                listEmpleados.putExtra("idEmpleados",idEmpleados);
                startActivity(listEmpleados);

            }
        }

        @Override
        protected void onCancelled() {
            Toast.makeText(MenuEmpleados.this, "Tarea cancelada!", Toast.LENGTH_SHORT).show();
        }
    }
}
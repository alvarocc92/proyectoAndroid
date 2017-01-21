package com.example.accplinux.probandobackendless;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;
import android.widget.Toast;

import com.backendless.Backendless;
import com.backendless.BackendlessCollection;
import com.backendless.async.callback.AsyncCallback;
import com.backendless.exceptions.BackendlessFault;
import com.backendless.persistence.BackendlessDataQuery;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class AsignarProyectoEmpleado extends AppCompatActivity implements MenuItemCompat.OnActionExpandListener{

    //ArrayList<String> listEmpleados = new ArrayList<>();
    ArrayList<String> idEmpleados = new ArrayList<>();
    Proyecto proyecto;
    ProgressDialog pDialog;
    List<Proyecto> listProyectos;
    List<Empleado> listEmpleados;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_asignar_proyecto_empleado);

        Toolbar toolbar = (Toolbar) findViewById(R.id.tool_bar);
        setSupportActionBar(toolbar);

        listEmpleados = new ArrayList<>();
        listProyectos = new ArrayList<>();

        proyecto = (Proyecto) getIntent().getExtras().getSerializable("proyecto");
        listEmpleados = (List<Empleado>) getIntent().getExtras().getSerializable("listEmpleados");
        listProyectos = (List<Proyecto>) getIntent().getExtras().getSerializable("listProyectos");

        CustomAdapterAsignarEmpleado adapter = new CustomAdapterAsignarEmpleado(proyecto,listEmpleados,this);

        ListView lView = (ListView)findViewById(R.id.activity_asignar_proyecto_empleado);
        lView.setAdapter(adapter);

    }

    @Override
    public void onBackPressed() {

        pDialog = new ProgressDialog(AsignarProyectoEmpleado.this);
        pDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        pDialog.setMessage("Cargando proyectos...");
        pDialog.setCancelable(true);
        pDialog.setMax(100);

        listProyectos= new ArrayList<>();

        AsignarProyectoEmpleado.CargarProyectos cargarProyectos = new AsignarProyectoEmpleado.CargarProyectos();
        cargarProyectos.execute();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_principal, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.home:
                Intent menuPrincipal = new Intent (AsignarProyectoEmpleado.this,MenuPrincipal.class);
                startActivity(menuPrincipal);
                return true;
            case R.id.miCuenta:
                Intent mi_cuenta = new Intent(AsignarProyectoEmpleado.this, MiCuenta.class);
                startActivity(mi_cuenta);
                return true;
            case R.id.logout:
                Backendless.UserService.logout(new AsyncCallback<Void>()
                {
                    public void handleResponse( Void response )
                    {
                        Toast.makeText(getApplicationContext(), "Sesión finalizada.", Toast.LENGTH_SHORT).show();
                        Intent login = new Intent(AsignarProyectoEmpleado.this,MainActivity.class);
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
    public boolean onMenuItemActionExpand(MenuItem item) {
        return false;
    }

    @Override
    public boolean onMenuItemActionCollapse(MenuItem item) {
        return false;
    }

    private class CargarProyectos extends AsyncTask<Void, Integer, Boolean> {

        @Override
        protected Boolean doInBackground(Void... params) {


            final boolean[] acabado = {false};

            BackendlessDataQuery dataQuery = new BackendlessDataQuery();
            dataQuery.setPageSize(20);

            Backendless.Persistence.of(Proyecto.class).find(dataQuery, new AsyncCallback<BackendlessCollection<Proyecto>>(){
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
                    Toast.makeText(AsignarProyectoEmpleado.this, fault.getMessage(), Toast.LENGTH_LONG).show();
                    Toast.makeText(AsignarProyectoEmpleado.this, fault.getCode(), Toast.LENGTH_LONG).show();
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
                    AsignarProyectoEmpleado.CargarProyectos.this.cancel(true);
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

                Toast.makeText(AsignarProyectoEmpleado.this, "Proyectos cargados." ,Toast.LENGTH_SHORT).show();

                Intent activityListarProyectos = new Intent(AsignarProyectoEmpleado.this,ListarProyectos.class);
                activityListarProyectos.putExtra("listProyectos", (Serializable) listProyectos);
                startActivity(activityListarProyectos);

            }
        }

        @Override
        protected void onCancelled() {
            Toast.makeText(AsignarProyectoEmpleado.this, "Tarea cancelada!", Toast.LENGTH_SHORT).show();
        }
    }
}

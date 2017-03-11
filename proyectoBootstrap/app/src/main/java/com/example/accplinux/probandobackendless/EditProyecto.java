package com.example.accplinux.probandobackendless;

import android.content.Intent;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import com.backendless.Backendless;
import com.backendless.async.callback.AsyncCallback;
import com.backendless.async.callback.BackendlessCallback;
import com.backendless.exceptions.BackendlessFault;
import com.beardedhen.androidbootstrap.BootstrapButton;

import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class EditProyecto extends AppCompatActivity implements MenuItemCompat.OnActionExpandListener{


    EditText nombre,jefeProyecto,presupuesto,cliente,fechaInicio,fechaFin;
    //CheckBox proyectoAcabado;
    BootstrapButton actualizarProyecto;
    boolean antiguo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_proyecto);

        SimpleDateFormat input = new SimpleDateFormat("EEE MMM d HH:mm:ss zzz yyyy");
        SimpleDateFormat output = new SimpleDateFormat("dd/MM/yyyy");

        nombre = (EditText) findViewById(R.id.nombre);
        jefeProyecto = (EditText) findViewById(R.id.jefeProyecto);
        presupuesto = (EditText) findViewById(R.id.presupuesto);
        cliente = (EditText) findViewById(R.id.cliente);
        fechaInicio = (EditText) findViewById(R.id.fechaInicio);
        fechaFin = (EditText) findViewById(R.id.fechaFin);
        //proyectoAcabado = (CheckBox) findViewById(R.id.proyectoAcabado);
        actualizarProyecto = (BootstrapButton) findViewById(R.id.actualizarProyecto);

        Toolbar toolbar = (Toolbar) findViewById(R.id.tool_bar);
        setSupportActionBar(toolbar);

        final Proyecto proyecto = (Proyecto) getIntent().getSerializableExtra("proyecto");
        final List<Proyecto> listProyectos = (List<Proyecto>) getIntent().getSerializableExtra("listProyectos");
        final int position = (int) getIntent().getSerializableExtra("position");
        antiguo = (boolean) getIntent().getSerializableExtra("antiguo");


        nombre.setText(proyecto.getNombre());
        jefeProyecto.setText(proyecto.getJefeProyecto());
        presupuesto.setText(proyecto.getPresupuesto().toString()+" €");
        cliente.setText(proyecto.getCliente());

        try {
            Date fechInicio = input.parse(proyecto.getFechaInicio().toString());
            Date fechFin = input.parse(proyecto.getFechaFin().toString());
            String fechIni = output.format(fechInicio);
            String fechF = output.format(fechFin);
            fechaInicio.setText(fechIni.toString());
            fechaFin.setText(fechF.toString());

        } catch (ParseException e) {
            e.printStackTrace();
        }

        Log.i("Proyecto", "id del proyecto: " + proyecto.getObjectId());

        actualizarProyecto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                actualizarProyecto(proyecto, listProyectos, position);
            }
        });
    }

    public void actualizarProyecto(Proyecto proyecto, final List<Proyecto> listProyectos, final int position){

        SimpleDateFormat format= new SimpleDateFormat("dd/MM/yyyy");

        proyecto.setNombre(nombre.getText().toString());
        proyecto.setJefeProyecto(jefeProyecto.getText().toString());
        String presupuestoString = presupuesto.getText().toString();
        String array[] = presupuestoString.split(" ", 2);
        proyecto.setPresupuesto(Long.parseLong(array[0]));
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

        //proyecto.setFinalizado(Boolean.valueOf(proyectoAcabado.getText().toString()));
        listProyectos.remove(position);
        listProyectos.add(position, proyecto);

        Backendless.Persistence.save(proyecto, new BackendlessCallback<Proyecto>() {
            @Override
            public void handleResponse(Proyecto proyecto) {
                Log.i("Proyecto", "proyecto actualizado" + proyecto.getNombre());
                Log.i("Proyecto", "id del proyecto: " + proyecto.getObjectId());

                Toast.makeText(getApplicationContext(), "Proyecto actualizado.", Toast.LENGTH_LONG).show();

                if(!antiguo) {
                    Intent listarProyectos = new Intent(EditProyecto.this, ListarProyectos.class);
                    listarProyectos.putExtra("listProyectos", (Serializable) listProyectos);

                    startActivity(listarProyectos);
                }else{
                    Intent listarProyectos = new Intent(EditProyecto.this, ListarAntiguosProyectos.class);
                    listarProyectos.putExtra("listProyectos", (Serializable) listProyectos);

                    startActivity(listarProyectos);
                }
            }
            @Override
            public void handleFault(BackendlessFault backendlessFault) {
                Toast.makeText(getApplicationContext(), backendlessFault.getMessage(), Toast.LENGTH_LONG).show();
                Toast.makeText(getApplicationContext(), backendlessFault.getCode(), Toast.LENGTH_LONG).show();
            }
        });
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
                Intent menuPrincipal = new Intent (EditProyecto.this,MenuPrincipal.class);
                startActivity(menuPrincipal);
                return true;
            case R.id.miCuenta:
                Intent mi_cuenta = new Intent(EditProyecto.this, MiCuenta.class);
                startActivity(mi_cuenta);
                return true;
            case R.id.logout:
                Backendless.UserService.logout(new AsyncCallback<Void>()
                {
                    public void handleResponse( Void response )
                    {
                        Toast.makeText(getApplicationContext(), "Sesión finalizada.", Toast.LENGTH_SHORT).show();
                        Intent login = new Intent(EditProyecto.this,MainActivity.class);
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
}

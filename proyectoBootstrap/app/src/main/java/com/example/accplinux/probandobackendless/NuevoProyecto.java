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
import android.widget.EditText;
import android.widget.Toast;

import com.backendless.Backendless;
import com.backendless.async.callback.AsyncCallback;
import com.backendless.async.callback.BackendlessCallback;
import com.backendless.exceptions.BackendlessFault;
import com.beardedhen.androidbootstrap.BootstrapButton;

import java.text.ParseException;
import java.text.SimpleDateFormat;

public class NuevoProyecto extends AppCompatActivity implements MenuItemCompat.OnActionExpandListener{

    EditText nombreProyecto,jefeProyecto,presupuesto,cliente,fechaInicio,fechaFin;
    BootstrapButton guardarProyecto;

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

        Toolbar toolbar = (Toolbar) findViewById(R.id.tool_bar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

        guardarProyecto = (BootstrapButton) findViewById(R.id.guardarProyecto);

        guardarProyecto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Proyecto proyecto= new Proyecto();
                SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");

                boolean valid = true;

                if(nombreProyecto.getText().toString().length()>0 && nombreProyecto.getText()!=null){
                    proyecto.setNombre(nombreProyecto.getText().toString());
                }else{
                    Toast.makeText(getApplicationContext(), "Nombre no puede estar vacío", Toast.LENGTH_SHORT).show();
                    valid = false;
                }

                if(jefeProyecto.getText().toString().length()>0 && jefeProyecto.getText()!=null){
                    proyecto.setJefeProyecto(jefeProyecto.getText().toString());
                }else{
                    Toast.makeText(getApplicationContext(), "Jefe de proyecto no puede estar vacío", Toast.LENGTH_SHORT).show();
                    valid = false;
                }

                if(presupuesto.getText().toString().length()>0 && presupuesto.getText()!=null){
                    proyecto.setPresupuesto(Long.parseLong(presupuesto.getText().toString()));
                }else{
                    Toast.makeText(getApplicationContext(), "Presupuesto no puede estar vacío", Toast.LENGTH_SHORT).show();
                    valid = false;
                }
                if(cliente.getText().toString().length()>0 && cliente.getText()!=null){
                    proyecto.setCliente(cliente.getText().toString());
                }else{
                    Toast.makeText(getApplicationContext(), "Cliente no puede estar vacío", Toast.LENGTH_SHORT).show();
                    valid = false;
                }

                if(fechaFin.getText().toString().length()>0 && fechaInicio.getText().toString().length()>0){
                    try {
                        proyecto.setFechaFin(format.parse(fechaFin.getText().toString()));
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                    try {
                        proyecto.setFechaInicio(format.parse(fechaInicio.getText().toString()));
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                    if(proyecto.getFechaFin().before(proyecto.getFechaInicio())){
                        Toast.makeText(getApplicationContext(), "Fecha inicio es superior a fecha fin", Toast.LENGTH_SHORT).show();
                        valid = false;
                    }
                }else{
                    Toast.makeText(getApplicationContext(), "Fecha inicio y fecha fin no pueden estar vacios", Toast.LENGTH_SHORT).show();
                }

                if(valid){
                    guardarProyecto(proyecto);
                }
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
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_principal, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.home:
                Intent menuPrincipal = new Intent (NuevoProyecto.this,MenuPrincipal.class);
                startActivity(menuPrincipal);
                return true;
            case R.id.miCuenta:
                Intent mi_cuenta = new Intent(NuevoProyecto.this, MiCuenta.class);
                startActivity(mi_cuenta);
                return true;
            case R.id.logout:
                Backendless.UserService.logout(new AsyncCallback<Void>()
                {
                    public void handleResponse( Void response )
                    {
                        Toast.makeText(getApplicationContext(), "Sesión finalizada.", Toast.LENGTH_SHORT).show();
                        Intent login = new Intent(NuevoProyecto.this,MainActivity.class);
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

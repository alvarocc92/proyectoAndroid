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

import java.math.BigDecimal;


public class CrearEmpleado extends AppCompatActivity implements MenuItemCompat.OnActionExpandListener{

    EditText nombre,apellidos,email,direccion,movil,salario;
    BootstrapButton guardarEmpleado;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_crear_empleado);

        nombre = (EditText) findViewById(R.id.nombre);
        apellidos = (EditText) findViewById(R.id.apellidos);
        email = (EditText) findViewById(R.id.empleado_email);
        direccion = (EditText) findViewById(R.id.empleado_direccion);
        movil = (EditText) findViewById(R.id.empleado_movil);
        salario = (EditText) findViewById(R.id.salario);
        guardarEmpleado = (BootstrapButton) findViewById(R.id.guardarEmpleado);

        Toolbar toolbar = (Toolbar) findViewById(R.id.tool_bar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

        guardarEmpleado.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Empleado empleado = new Empleado();

                empleado.setNombre(nombre.getText().toString());
                empleado.setApellidos(apellidos.getText().toString());
                empleado.setMovil(movil.getText().toString());
                empleado.setDireccion(direccion.getText().toString());
                empleado.setEmail(email.getText().toString());
                empleado.setSalario(BigDecimal.valueOf(Long.parseLong(salario.getText().toString())));

                guardarEmpleado(empleado);
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
                Intent menuPrincipal = new Intent (CrearEmpleado.this,MenuPrincipal.class);
                startActivity(menuPrincipal);
                return true;
            case R.id.miCuenta:
                Intent mi_cuenta = new Intent(CrearEmpleado.this, MiCuenta.class);
                startActivity(mi_cuenta);
                return true;
            case R.id.logout:
                Backendless.UserService.logout(new AsyncCallback<Void>()
                {
                    public void handleResponse( Void response )
                    {
                        Toast.makeText(getApplicationContext(), "Sesión finalizada.", Toast.LENGTH_SHORT).show();
                        Intent login = new Intent(CrearEmpleado.this,MainActivity.class);
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
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    public boolean onMenuItemActionExpand(MenuItem item) {
        return false;
    }

    @Override
    public boolean onMenuItemActionCollapse(MenuItem item) {
        return false;
    }

    public void guardarEmpleado(Empleado empleado) {

        Backendless.Persistence.save(new Empleado(empleado.getNombre(), empleado.getApellidos(), empleado.getEmail(),
                empleado.getMovil(), empleado.getDireccion(),empleado.getObjectId(), empleado.getSalario(), false,false), new BackendlessCallback<Empleado>() {
            @Override
            public void handleResponse(Empleado empleado) {
                Log.i("Empleado", "Nuevo empleado registrado" + empleado.getEmail());
                Log.i("Empleado", "id del empleado: " + empleado.getObjectId());

                Toast.makeText(getApplicationContext(), "Empleado registrado.", Toast.LENGTH_LONG).show();
                Intent menuEmpleados = new Intent(CrearEmpleado.this, MenuEmpleados.class);
                startActivity(menuEmpleados);
            }
            @Override
            public void handleFault(BackendlessFault backendlessFault) {
                Toast.makeText(getApplicationContext(), backendlessFault.getMessage(), Toast.LENGTH_LONG).show();
                Toast.makeText(getApplicationContext(), backendlessFault.getCode(), Toast.LENGTH_LONG).show();
            }
        });
    }
}

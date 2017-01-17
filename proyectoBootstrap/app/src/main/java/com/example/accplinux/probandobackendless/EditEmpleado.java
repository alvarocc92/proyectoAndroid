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

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

public class EditEmpleado extends AppCompatActivity implements MenuItemCompat.OnActionExpandListener{

    EditText nombre,apellidos,email,direccion,movil,salario;
    BootstrapButton actualizarEmpleado;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_empleado);

        nombre = (EditText) findViewById(R.id.nombre);
        apellidos = (EditText) findViewById(R.id.apellidos);
        email = (EditText) findViewById(R.id.empleado_email);
        direccion = (EditText) findViewById(R.id.empleado_direccion);
        movil = (EditText) findViewById(R.id.empleado_movil);
        salario = (EditText) findViewById(R.id.salario);
        actualizarEmpleado = (BootstrapButton) findViewById(R.id.actualizarEmpleado);

        final Empleado empleado = (Empleado) getIntent().getSerializableExtra("empleado");
        final List<String> list = (List<String>) getIntent().getSerializableExtra("listado");
        final List<String> idEmpleados = (List<String>) getIntent().getSerializableExtra("idEmpleados");
        final int position = (int) getIntent().getSerializableExtra("position");


        Toolbar toolbar = (Toolbar) findViewById(R.id.tool_bar);
        setSupportActionBar(toolbar);

        nombre.setText(empleado.getNombre());
        apellidos.setText(empleado.getApellidos());
        email.setText(empleado.getEmail());
        direccion.setText(empleado.getMovil());
        movil.setText(empleado.getDireccion());
        salario.setText(empleado.getSalario().toString());


        Log.i("Empleado", "id del empleado: " + empleado.getObjectId());

        actualizarEmpleado.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                actualizarEmpleado(empleado, list, idEmpleados, position);
            }
        });
    }

    public void actualizarEmpleado(Empleado empleado,final List<String> list,final List<String> idEmpleados,final int position){

        empleado.setNombre(nombre.getText().toString());
        empleado.setApellidos(apellidos.getText().toString());
        empleado.setEmail(email.getText().toString());
        empleado.setDireccion(direccion.getText().toString());
        empleado.setMovil(movil.getText().toString());
        empleado.setSalario(BigDecimal.valueOf(Long.parseLong(salario.getText().toString())));


        Backendless.Persistence.save(empleado, new BackendlessCallback<Empleado>() {
            @Override
            public void handleResponse(Empleado empleado) {
                Log.i("Empleado", "empleado actualizado" + empleado.getApellidos());
                Log.i("Empleado", "id del empleado: " + empleado.getObjectId());

                list.remove(position);
                list.add(position,empleado.getNombre()+" "+empleado.getApellidos());
                Intent listEmpleados = new Intent(EditEmpleado.this,ListarEmpleados.class);
                listEmpleados.putExtra("listado", (Serializable) list);
                listEmpleados.putExtra("idEmpleados", (Serializable) idEmpleados);
                Toast.makeText(getApplicationContext(), "Empleado actualizado.", Toast.LENGTH_LONG).show();
                startActivity(listEmpleados);

                /*Intent menuEmpleados = new Intent(EditEmpleado.this, MenuEmpleados.class);
                startActivity(menuEmpleados);*/
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
                Intent menuPrincipal = new Intent (EditEmpleado.this,MenuPrincipal.class);
                startActivity(menuPrincipal);
                return true;
            case R.id.miCuenta:
                Intent mi_cuenta = new Intent(EditEmpleado.this, MiCuenta.class);
                startActivity(mi_cuenta);
                return true;
            case R.id.logout:
                Backendless.UserService.logout(new AsyncCallback<Void>()
                {
                    public void handleResponse( Void response )
                    {
                        Toast.makeText(getApplicationContext(), "Sesión finalizada.", Toast.LENGTH_SHORT).show();
                        Intent login = new Intent(EditEmpleado.this,MainActivity.class);
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

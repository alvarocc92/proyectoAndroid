package com.example.accplinux.probandobackendless;

import android.content.Intent;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.backendless.Backendless;
import com.backendless.async.callback.AsyncCallback;
import com.backendless.async.callback.BackendlessCallback;
import com.backendless.exceptions.BackendlessFault;
import com.beardedhen.androidbootstrap.BootstrapButton;

import java.text.ParseException;
import java.text.SimpleDateFormat;

import static com.example.accplinux.probandobackendless.R.id.actualizarProyecto;
import static com.example.accplinux.probandobackendless.R.id.jefeProyecto;
import static com.example.accplinux.probandobackendless.R.id.nombreProyecto;

public class GastoProyecto extends AppCompatActivity implements MenuItemCompat.OnActionExpandListener {

    EditText descripcion, cantidad;
    BootstrapButton guardarGasto;
    Proyecto proyecto;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gasto_proyecto);

        descripcion = (EditText) findViewById(R.id.descripcion);
        cantidad = (EditText) findViewById(R.id.cantidad);
        guardarGasto = (BootstrapButton) findViewById(R.id.guardarGasto);

        proyecto = (Proyecto) getIntent().getSerializableExtra("proyecto");

        Toolbar toolbar = (Toolbar) findViewById(R.id.tool_bar);
        setSupportActionBar(toolbar);

        guardarGasto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Gastos gasto = new Gastos();

                gasto.setDescripcion(descripcion.getText().toString());
                gasto.setCantidad(Float.parseFloat(cantidad.getText().toString()));
                gasto.setProyecto(proyecto);
                guardarGastoProyecto(gasto);

            }
        });
    }

    public void guardarGastoProyecto(final Gastos gasto) {


        Backendless.Persistence.save(new Gastos(gasto.getDescripcion(), gasto.getCantidad(), gasto.getProyecto()), new BackendlessCallback<Gastos>() {
            @Override
            public void handleResponse(Gastos gastos) {
                Log.i("Empleado", "Nuevo gasto registrado");
                proyecto.getListGastos().add(gastos);
                updateProyecto();
            }

            @Override
            public void handleFault(BackendlessFault backendlessFault) {
                Toast.makeText(getApplicationContext(), backendlessFault.getMessage(), Toast.LENGTH_LONG).show();
                Toast.makeText(getApplicationContext(), backendlessFault.getCode(), Toast.LENGTH_LONG).show();
            }
        });

    }

    public void updateProyecto(){

        Backendless.Persistence.save(proyecto, new BackendlessCallback<Proyecto>() {
            @Override
            public void handleResponse(Proyecto empleado) {

                Log.i("proyecto", "Gasto guardado en proyecto" );
                Toast.makeText(getApplicationContext(), "Gasto guardado.", Toast.LENGTH_LONG).show();

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
                Intent menuPrincipal = new Intent (GastoProyecto.this,MenuPrincipal.class);
                startActivity(menuPrincipal);
                return true;
            case R.id.miCuenta:
                Intent mi_cuenta = new Intent(GastoProyecto.this, MiCuenta.class);
                startActivity(mi_cuenta);
                return true;
            case R.id.logout:
                Backendless.UserService.logout(new AsyncCallback<Void>()
                {
                    public void handleResponse( Void response )
                    {
                        Toast.makeText(getApplicationContext(), "Sesión finalizada.", Toast.LENGTH_SHORT).show();
                        Intent login = new Intent(GastoProyecto.this,MainActivity.class);
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

package com.example.accplinux.probandobackendless;

import android.content.Intent;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;
import com.backendless.Backendless;
import com.backendless.async.callback.AsyncCallback;
import com.backendless.exceptions.BackendlessFault;
import com.beardedhen.androidbootstrap.BootstrapCircleThumbnail;

public class MenuPrincipal extends AppCompatActivity implements MenuItemCompat.OnActionExpandListener{

    BootstrapCircleThumbnail empleados;
    BootstrapCircleThumbnail proyectos;
    BootstrapCircleThumbnail informes;
    Toolbar toolbar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu_principal);

        empleados = (BootstrapCircleThumbnail) findViewById(R.id.empleados);
        proyectos = (BootstrapCircleThumbnail) findViewById(R.id.proyectos);
        informes = (BootstrapCircleThumbnail) findViewById(R.id.informes);
        toolbar = (Toolbar) findViewById(R.id.tool_bar);

        setSupportActionBar(toolbar);

        empleados.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent menuEmpleados = new Intent(MenuPrincipal.this,MenuEmpleados.class);
                startActivity(menuEmpleados);
            }
        });

        proyectos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent menuProyectos = new Intent(MenuPrincipal.this,MenuProyectos.class);
                startActivity(menuProyectos);
            }
        });

        informes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent menuInformes = new Intent(MenuPrincipal.this,MenuInformes.class);
                startActivity(menuInformes);
            }
        });
    }

    //impedir la tecla de Back del telefono para no volver a la activity login
    @Override
    public void onBackPressed() {

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
                Intent menuPrincipal = new Intent (MenuPrincipal.this,MenuPrincipal.class);
                startActivity(menuPrincipal);
                return true;
            case R.id.miCuenta:
                Intent mi_cuenta = new Intent(MenuPrincipal.this, MiCuenta.class);
                startActivity(mi_cuenta);
                return true;
            case R.id.logout:
                Backendless.UserService.logout(new AsyncCallback<Void>()
                {
                    public void handleResponse( Void response )
                    {
                        Toast.makeText(getApplicationContext(), "Sesión finalizada.", Toast.LENGTH_SHORT).show();
                        Intent login = new Intent(MenuPrincipal.this,MainActivity.class);
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

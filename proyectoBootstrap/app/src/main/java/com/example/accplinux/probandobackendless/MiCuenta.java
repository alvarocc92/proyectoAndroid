package com.example.accplinux.probandobackendless;

import android.content.Intent;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.backendless.Backendless;
import com.backendless.BackendlessUser;
import com.backendless.async.callback.AsyncCallback;
import com.backendless.exceptions.BackendlessFault;
import com.backendless.persistence.local.UserTokenStorageFactory;
import com.beardedhen.androidbootstrap.BootstrapButton;

public class MiCuenta extends AppCompatActivity implements MenuItemCompat.OnActionExpandListener{

    EditText nuevaContraseña, confirmacion;
    BootstrapButton cambiarContraseña;
    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mi_cuenta);

        //email = (EditText) findViewById(R.id.email);
        nuevaContraseña = (EditText) findViewById(R.id.nuevaContraseña);
        confirmacion = (EditText) findViewById(R.id.confirmacion);
        cambiarContraseña = (BootstrapButton) findViewById(R.id.cambiarContraseña);

        toolbar = (Toolbar) findViewById(R.id.tool_bar);
        setSupportActionBar(toolbar);

        cambiarContraseña.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //BackendlessUser user = new BackendlessUser();

                //user.setEmail(email.getText().toString());
                //user.setPassword(contraseñaActual.getText().toString());

                //modificarContraseña(user, nuevaContraseña.getText().toString(), confirmacion.getText().toString());
                cambiarPw();
            }
        });
    }

    public void cambiarPw() {

        Backendless.UserService.isValidLogin(new AsyncCallback<Boolean>() {
            @Override
            public void handleResponse(Boolean isValidLogin) {
                if (isValidLogin && Backendless.UserService.CurrentUser() != null) {
                    BackendlessUser user = Backendless.UserService.CurrentUser();
                    Backendless.UserService.findById(user.getObjectId(), new AsyncCallback<BackendlessUser>() {
                        @Override
                        public void handleResponse(BackendlessUser currentUser) {
                            if (nuevaContraseña.getText().toString().equals(confirmacion.getText().toString())) {
                                currentUser.setPassword(confirmacion.getText().toString());
                                Backendless.UserService.update( currentUser, new AsyncCallback<BackendlessUser>()
                                {
                                    public void handleResponse( BackendlessUser user )
                                    {
                                        Toast.makeText(getApplicationContext(), "Usuario actualizado.", Toast.LENGTH_LONG).show();
                                    }

                                    public void handleFault( BackendlessFault fault )
                                    {
                                        Toast.makeText(getApplicationContext(), fault.getMessage(), Toast.LENGTH_LONG).show();
                                    }
                                });
                            } else {
                                Toast.makeText(getApplicationContext(), "Las contraseñas no coinciden", Toast.LENGTH_LONG).show();
                            }
                        }
                        @Override
                        public void handleFault(BackendlessFault fault) {
                            Toast.makeText(getApplicationContext(), "Los datos introducidos no son correctos.", Toast.LENGTH_LONG).show();
                            Toast.makeText(getApplicationContext(), fault.getMessage() + " " + fault.getCode(), Toast.LENGTH_LONG).show();
                        }
                    });
                } else {
                    Toast.makeText(getApplicationContext(), "currentUser es null", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void handleFault(BackendlessFault fault) {
                Toast.makeText(getApplicationContext(), "Los datos introducidos no son correctos.", Toast.LENGTH_LONG).show();
                Toast.makeText(getApplicationContext(), fault.getMessage() + " " + fault.getCode(), Toast.LENGTH_LONG).show();
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
                Intent menuPrincipal = new Intent (MiCuenta.this,MenuPrincipal.class);
                startActivity(menuPrincipal);
                return true;
            case R.id.miCuenta:
                Intent mi_cuenta = new Intent(MiCuenta.this, MiCuenta.class);
                startActivity(mi_cuenta);
                return true;
            case R.id.logout:
                Backendless.UserService.logout(new AsyncCallback<Void>()
                {
                    public void handleResponse( Void response )
                    {
                        Toast.makeText(getApplicationContext(), "Sesión finalizada.", Toast.LENGTH_SHORT).show();
                        Intent login = new Intent(MiCuenta.this,MainActivity.class);
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


   /* public void modificarContraseña(BackendlessUser user, final String nuevaContraseña, final String confirmacion){

        Backendless.UserService.login( user.getEmail(), user.getPassword(), new AsyncCallback<BackendlessUser>()
        {
            public void handleResponse( BackendlessUser user )
            {
                if(nuevaContraseña.equals(confirmacion)){
                    user.setPassword(nuevaContraseña);
                    Backendless.UserService.update( user, new AsyncCallback<BackendlessUser>()
                    {
                        public void handleResponse( BackendlessUser user )
                        {
                            Toast.makeText(getApplicationContext(), "Usuario actualizado.", Toast.LENGTH_LONG).show();
                        }

                        public void handleFault( BackendlessFault fault )
                        {
                            Toast.makeText(getApplicationContext(), fault.getMessage(), Toast.LENGTH_LONG).show();
                        }
                    });
                }else{
                    Toast.makeText(getApplicationContext(),"La contraseña deben coincidir con la confirmación.", Toast.LENGTH_LONG).show();
                }
            }

            public void handleFault( BackendlessFault fault )
            {
                Toast.makeText(getApplicationContext(), "Los datos introducidos no son correctos.", Toast.LENGTH_LONG).show();
            }
        });
    }*/

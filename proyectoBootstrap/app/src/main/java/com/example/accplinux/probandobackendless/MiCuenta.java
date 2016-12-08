package com.example.accplinux.probandobackendless;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.backendless.Backendless;
import com.backendless.BackendlessUser;
import com.backendless.async.callback.AsyncCallback;
import com.backendless.exceptions.BackendlessFault;

public class MiCuenta extends AppCompatActivity {

    EditText email,contraseñaActual,nuevaContraseña,confirmacion;
    Button cambiarContraseña;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mi_cuenta);

        email = (EditText) findViewById(R.id.email);
        contraseñaActual = (EditText) findViewById(R.id.contraseñaActual);
        nuevaContraseña = (EditText) findViewById(R.id.nuevaContraseña);
        confirmacion = (EditText) findViewById(R.id.confirmacion);

        cambiarContraseña = (Button) findViewById(R.id.cambiarContraseña);

        cambiarContraseña.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BackendlessUser user = new BackendlessUser();
                user.setEmail(email.getText().toString());
                user.setPassword(contraseñaActual.getText().toString());

                modificarContraseña(user, nuevaContraseña.getText().toString(), confirmacion.getText().toString());
            }
        });
    }
    public void modificarContraseña(BackendlessUser user, final String nuevaContraseña, final String confirmacion){

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
    }
}

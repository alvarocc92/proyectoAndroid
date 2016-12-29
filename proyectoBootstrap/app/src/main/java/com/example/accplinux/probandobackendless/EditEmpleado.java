package com.example.accplinux.probandobackendless;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.backendless.Backendless;
import com.backendless.async.callback.BackendlessCallback;
import com.backendless.exceptions.BackendlessFault;
import com.beardedhen.androidbootstrap.BootstrapButton;

import java.math.BigDecimal;

public class EditEmpleado extends AppCompatActivity {

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
                actualizarEmpleado(empleado);
            }
        });
    }

    public void actualizarEmpleado(Empleado empleado){

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

                Toast.makeText(getApplicationContext(), "Empleado actualizado.", Toast.LENGTH_LONG).show();
                Intent menuEmpleados = new Intent(EditEmpleado.this, MenuEmpleados.class);
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

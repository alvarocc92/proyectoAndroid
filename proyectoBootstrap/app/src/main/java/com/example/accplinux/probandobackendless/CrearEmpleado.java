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

import java.math.BigDecimal;


public class CrearEmpleado extends AppCompatActivity {

    EditText nombre,apellidos,email,direccion,movil,salario;
    Button guardarEmpleado;

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
        guardarEmpleado = (Button) findViewById(R.id.guardarEmpleado);

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
    public void guardarEmpleado(Empleado empleado) {

        Backendless.Persistence.save(new Empleado(empleado.getNombre(), empleado.getApellidos(), empleado.getEmail(),
                empleado.getMovil(), empleado.getDireccion(),empleado.getObjectId(), empleado.getSalario()), new BackendlessCallback<Empleado>() {
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

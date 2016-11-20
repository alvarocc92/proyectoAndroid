package com.example.accplinux.probandobackendless;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ListAdapter;
import android.widget.Toast;

import com.backendless.Backendless;
import com.backendless.BackendlessCollection;
import com.backendless.BackendlessUser;
import com.backendless.Persistence;
import com.backendless.async.callback.AsyncCallback;
import com.backendless.async.callback.BackendlessCallback;
import com.backendless.exceptions.BackendlessFault;
import com.backendless.persistence.BackendlessDataQuery;
import com.backendless.persistence.QueryOptions;
import com.backendless.property.ObjectProperty;
import com.beardedhen.androidbootstrap.BootstrapButton;
import com.beardedhen.androidbootstrap.BootstrapProgressBar;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Random;

public class MenuEmpleados extends AppCompatActivity {

    BootstrapButton newEmpleado,listarEmpleados;
    ArrayList<String> mostrarEmpleados =new ArrayList<>();
    ArrayList<String> idEmpleados = new ArrayList<>();
    BootstrapProgressBar bar;
    private Random random;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu_empleados);

        newEmpleado = (BootstrapButton) findViewById(R.id.newEmpleado);
        listarEmpleados = (BootstrapButton) findViewById(R.id.listarEmpleados);
        bar = (BootstrapProgressBar) findViewById(R.id.barraInfo);

        bar.setProgress(0);
        cargarEmpleados();


        newEmpleado.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                registrarEmpleado();
            }
        });

        listarEmpleados.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                while(bar.getProgress()<100){
                        bar.setProgress(randomProgress(bar.getProgress(), 100));
                    }
                Intent listEmpleados = new Intent(MenuEmpleados.this,ListarEmpleados.class);
                listEmpleados.putExtra("listado",mostrarEmpleados);
                listEmpleados.putExtra("idEmpleados",idEmpleados);
                startActivity(listEmpleados);
                }
        });

    }
    public void registrarEmpleado(){
        Intent crearEmpleado = new Intent(MenuEmpleados.this, CrearEmpleado.class);
        startActivity(crearEmpleado);
    }

    public void cargarEmpleados() {
        Backendless.Persistence.of(Empleado.class).find( new AsyncCallback<BackendlessCollection<Empleado>>(){
            @Override
            public void handleResponse( BackendlessCollection<Empleado> foundContacts )
            {
                for(int i =0 ; i<foundContacts.getTotalObjects();i++){
                    String nombreCompleto = foundContacts.getData().get(i).getNombre()+" "+foundContacts.getData().get(i).getApellidos();
                    mostrarEmpleados.add(nombreCompleto);
                    idEmpleados.add(foundContacts.getData().get(i).getObjectId());
                }
            }
            @Override
            public void handleFault( BackendlessFault fault )
            {
                Toast.makeText(getApplicationContext(), fault.getMessage(), Toast.LENGTH_LONG).show();
                Toast.makeText(getApplicationContext(), fault.getCode(), Toast.LENGTH_LONG).show();
            }
        });

    }
    private int randomProgress(int currentProgress, int maxProgress) {

        if (random == null) {
            random = new Random();
        }

        int prog = currentProgress + random.nextInt(5);

        if (prog > maxProgress) {
            prog -= maxProgress;
        }

        return prog;
    }
}

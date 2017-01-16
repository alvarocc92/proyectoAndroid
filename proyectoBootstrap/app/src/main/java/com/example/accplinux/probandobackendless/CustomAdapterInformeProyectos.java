package com.example.accplinux.probandobackendless;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.backendless.Backendless;
import com.backendless.BackendlessCollection;
import com.backendless.async.callback.AsyncCallback;
import com.backendless.exceptions.BackendlessFault;
import com.backendless.persistence.BackendlessDataQuery;
import com.beardedhen.androidbootstrap.BootstrapButton;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;


/**
 * Created by accplinux on 23/11/16.
 */

public class CustomAdapterInformeProyectos extends BaseAdapter implements ListAdapter {

    private Context context;
    private List<Proyecto> listProyectos = new ArrayList<>();
    private List<Empleado> listEmpleados = new ArrayList<>();
    ProgressDialog pDialog;
    private Proyecto proyecto;


    public CustomAdapterInformeProyectos(List<Proyecto> listProyectos, Context context) {
        this.listProyectos = listProyectos;
        this.context = context;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        View view = convertView;
        if (view == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.precargar_informe_proyectos, null);
        }
        final TextView listItemText = (TextView)view.findViewById(R.id.list_item_informe_proyecto);
        listItemText.setText(listProyectos.get(position).getNombre());

        BootstrapButton gastoProyecto = (BootstrapButton)view.findViewById(R.id.gastoProyecto);
        BootstrapButton verInforme = (BootstrapButton)view.findViewById(R.id.verInforme);

        verInforme.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v)  {
                Log.i("Proyecto", "id proyecto: " + listProyectos.get(position).getObjectId());
                informeProyecto(listProyectos.get(position));
            }
        });

        gastoProyecto.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v)  {

                Intent gastoProyecto = new Intent(context.getApplicationContext(),GastoProyecto.class);
                gastoProyecto.putExtra("proyecto",listProyectos.get(position));
                context.startActivity(gastoProyecto);

            }
        });
        return view;
    }

    public void informeProyecto(Proyecto proyecto){
        Intent listarProyectos = new Intent(context.getApplicationContext(),MenuInformes.class);
        listarProyectos.putExtra("proyecto",proyecto);
        context.startActivity(listarProyectos);
    }

    @Override
    public int getCount() {
        return listProyectos.size();
    }

    @Override
    public Object getItem(int pos) {
        return listProyectos.get(pos);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

}
/*
    public void cargarEmpleados() {
        Backendless.Persistence.of(Empleado.class).find( new AsyncCallback<BackendlessCollection<Empleado>>(){
            @Override
            public void handleResponse( BackendlessCollection<Empleado> foundContacts )
            {
                for(int i =0 ; i<foundContacts.getTotalObjects();i++){
                    listEmpleados.add(foundContacts.getData().get(i));
                }
            }
            @Override
            public void handleFault( BackendlessFault fault )
            {
                Toast.makeText(context.getApplicationContext(), fault.getMessage(), Toast.LENGTH_LONG).show();
                Toast.makeText(context.getApplicationContext(), fault.getCode(), Toast.LENGTH_LONG).show();
            }
        });

    }
 */


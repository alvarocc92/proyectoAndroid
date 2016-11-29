package com.example.accplinux.probandobackendless;

import android.content.Context;
import android.content.Intent;
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
import com.beardedhen.androidbootstrap.BootstrapButton;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;


/**
 * Created by accplinux on 23/11/16.
 */

public class CustomAdapterProyectos extends BaseAdapter implements ListAdapter {

    private Context context;
    private List<Proyecto> listProyectos = new ArrayList<>();
    private List<Empleado> listEmpleados = new ArrayList<>();

    private ArrayList<String> mostrarEmpleados =new ArrayList<>();
    private ArrayList<String> idEmpleados = new ArrayList<>();

    public CustomAdapterProyectos(List<Proyecto> listProyectos, Context context) {
        this.listProyectos = listProyectos;
        this.context = context;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        View view = convertView;
        if (view == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.precargar_proyectos, null);
        }
        final TextView listItemText = (TextView)view.findViewById(R.id.list_item_proyecto);
        listItemText.setText(listProyectos.get(position).getNombre());

        BootstrapButton editProyecto = (BootstrapButton)view.findViewById(R.id.editProyecto);
        BootstrapButton asignarProyectoEmpleado = (BootstrapButton)view.findViewById(R.id.asignarProyectoEmpleado);

        editProyecto.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v)  {
                Log.i("Proyecto", "id proyecto: " + listProyectos.get(position).getObjectId());
                editProyecto(listProyectos.get(position));
            }
        });

        asignarProyectoEmpleado.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v)  {
                Log.i("Proyecto", "id proyecto: " + listProyectos.get(position).getObjectId());
                asignarProyectoEmpleado(listProyectos.get(position));
            }
        });

        return view;
    }

    public void editProyecto(Proyecto proyecto){
        Intent listarProyectos = new Intent(context.getApplicationContext(),EditProyecto.class);
        listarProyectos.putExtra("proyecto",proyecto);
        context.startActivity(listarProyectos);
    }

    public void asignarProyectoEmpleado(Proyecto proyecto){

        cargarEmpleados();

        Intent asignarProyecto = new Intent(context.getApplicationContext(),AsignarProyectoEmpleado.class);

        asignarProyecto.putExtra("proyecto",proyecto);
        asignarProyecto.putExtra("listEmpleados",(Serializable) listEmpleados);

        //asignarProyecto.putExtra("nombreEmpleados",mostrarEmpleados);
        //asignarProyecto.putExtra("idEmpleados",idEmpleados);
        context.startActivity(asignarProyecto);
    }

    public void cargarEmpleados() {
        Backendless.Persistence.of(Empleado.class).find( new AsyncCallback<BackendlessCollection<Empleado>>(){
            @Override
            public void handleResponse( BackendlessCollection<Empleado> foundContacts )
            {
                for(int i =0 ; i<foundContacts.getTotalObjects();i++){
                    listEmpleados.add(foundContacts.getData().get(i));
                   /* String nombreCompleto = foundContacts.getData().get(i).getNombre()+" "+foundContacts.getData().get(i).getApellidos();
                    mostrarEmpleados.add(nombreCompleto);
                    idEmpleados.add(foundContacts.getData().get(i).getObjectId());*/
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

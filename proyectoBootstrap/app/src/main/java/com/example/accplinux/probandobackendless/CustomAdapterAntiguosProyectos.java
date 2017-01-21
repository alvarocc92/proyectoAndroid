package com.example.accplinux.probandobackendless;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AlertDialog;
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
 * Created by accplinux on 2/01/17.
 */

public class CustomAdapterAntiguosProyectos extends BaseAdapter implements ListAdapter {

    private Context context;
    private List<Proyecto> listProyectos = new ArrayList<>();
    private List<Empleado> listEmpleados = new ArrayList<>();
    ProgressDialog pDialog;
    private Proyecto proyecto;

    public CustomAdapterAntiguosProyectos(List<Proyecto> listProyectos, Context context) {
        this.listProyectos = listProyectos;
        this.context = context;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        View view = convertView;
        if (view == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.precarga_antiguos_proyectos, null);
        }
        final TextView listItemText = (TextView)view.findViewById(R.id.list_item_antiguo_proyecto);
        listItemText.setText(listProyectos.get(position).getNombre());

        BootstrapButton verProyecto = (BootstrapButton)view.findViewById(R.id.verProyecto);
        BootstrapButton deleteProyecto = (BootstrapButton)view.findViewById(R.id.deleteProyecto);

        verProyecto.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v)  {
                Log.i("Proyecto", "id proyecto: " + listProyectos.get(position).getObjectId());
                editProyecto(listProyectos.get(position),position);
            }
        });

        deleteProyecto.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {

                proyecto = listProyectos.get(position);

                AlertDialog.Builder builder1 = new AlertDialog.Builder(context);
                builder1.setMessage("¿Deseas eliminar permanentemente?");
                builder1.setCancelable(true);

                builder1.setPositiveButton(
                        "Si",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                deleteProyecto(proyecto);
                            }
                        });

                builder1.setNegativeButton(
                        "No",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                            }
                        });

                AlertDialog alert11 = builder1.create();
                alert11.show();

            }
        });
        return view;
    }

    public void editProyecto(Proyecto proyecto, final int position){
        Intent listarProyectos = new Intent(context.getApplicationContext(),EditProyecto.class);
        listarProyectos.putExtra("proyecto",proyecto);
        listarProyectos.putExtra("proyecto",proyecto);
        listarProyectos.putExtra("antiguo",false);
        listarProyectos.putExtra("position",position);
        listarProyectos.putExtra("listProyectos", (Serializable) listProyectos);
        context.startActivity(listarProyectos);
    }

    public void deleteProyecto(Proyecto proyecto){

                Backendless.Persistence.of( Proyecto.class ).remove( proyecto,new AsyncCallback<Long>(){
                    public void handleResponse( Long response )
                    {
                        Toast.makeText(context.getApplicationContext(), "Proyecto borrado.", Toast.LENGTH_LONG).show();

                        Intent menuProyectos = new Intent(context.getApplicationContext(),MenuProyectos.class);
                        context.startActivity(menuProyectos);
                        //notifyDataSetChanged();
                    }
                    public void handleFault( BackendlessFault fault )
                    {
                        Toast.makeText(context.getApplicationContext(), fault.getCode(), Toast.LENGTH_LONG).show();
                        Toast.makeText(context.getApplicationContext(), fault.getMessage(), Toast.LENGTH_LONG).show();
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

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
    ProgressDialog pDialog;
    private Proyecto proyecto;


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

                pDialog = new ProgressDialog(context);
                pDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
                pDialog.setMessage("Cargando empleados...");
                pDialog.setCancelable(true);
                pDialog.setMax(100);

                Log.i("Proyecto", "id proyecto: " + listProyectos.get(position).getObjectId());

                CargarEmpleados cargarEmpleados = new CargarEmpleados();
                cargarEmpleados.execute();
            }
        });
        return view;
    }

    public void editProyecto(Proyecto proyecto){
        Intent listarProyectos = new Intent(context.getApplicationContext(),EditProyecto.class);
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

    private class CargarEmpleados extends AsyncTask<Void, Integer, Boolean> {

        @Override
        protected Boolean doInBackground(Void... params) {

            final boolean[] acabado = {false};

            Backendless.Persistence.of(Empleado.class).find( new AsyncCallback<BackendlessCollection<Empleado>>(){
                @Override
                public void handleResponse( BackendlessCollection<Empleado> foundContacts )
                {
                    for(int i =0 ; i<foundContacts.getTotalObjects();i++){
                        listEmpleados.add(foundContacts.getData().get(i));
                    }
                    acabado[0]=true;
                    onPostExecute(acabado[0]);
                }
                @Override
                public void handleFault( BackendlessFault fault )
                {
                    Toast.makeText(context.getApplicationContext(), fault.getMessage(), Toast.LENGTH_LONG).show();
                    Toast.makeText(context.getApplicationContext(), fault.getCode(), Toast.LENGTH_LONG).show();
                }
            });
            return acabado[0];
        }

        @Override
        protected void onProgressUpdate(Integer... values) {

            int progreso = values[0].intValue();
            pDialog.setProgress(progreso);

        }

        @Override
        protected void onPreExecute() {

            pDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
                @Override
                public void onCancel(DialogInterface dialog) {
                    CustomAdapterProyectos.CargarEmpleados.this.cancel(true);
                }
            });

            pDialog.setProgress(0);
            pDialog.show(); //sirve para mostrar el dialogo
        }

        @Override
        protected void onPostExecute(Boolean result) {
            if(result)
            {
                pDialog.dismiss();
                Toast.makeText(context.getApplicationContext(), "Empleados cargados." ,Toast.LENGTH_SHORT).show();

                Intent asignarProyecto = new Intent(context.getApplicationContext(),AsignarProyectoEmpleado.class);
                asignarProyecto.putExtra("proyecto",proyecto);
                asignarProyecto.putExtra("listEmpleados",(Serializable) listEmpleados);
                context.startActivity(asignarProyecto);
            }
        }

        @Override
        protected void onCancelled() {
            Toast.makeText(context.getApplicationContext(), "Tarea cancelada!", Toast.LENGTH_SHORT).show();
        }
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

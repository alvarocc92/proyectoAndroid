package com.example.accplinux.probandobackendless;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
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
import com.backendless.async.callback.AsyncCallback;
import com.backendless.async.callback.BackendlessCallback;
import com.backendless.exceptions.BackendlessFault;
import com.beardedhen.androidbootstrap.BootstrapButton;

import java.util.ArrayList;

/**
 * Created by accplinux on 26/12/16.
 */

public class CustomAdapterAntiguosEmpleados extends BaseAdapter implements ListAdapter {

    private ArrayList<String> list = new ArrayList<>();
    private ArrayList<String> idEmpleados = new ArrayList<>();
    private Context context;

    public CustomAdapterAntiguosEmpleados(ArrayList<String> list,ArrayList<String> idEmpleados, Context context) {
        this.list = list;
        this.context = context;
        this.idEmpleados=idEmpleados;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        View view = convertView;
        if (view == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.precarga_empleados, null);
        }
        final TextView listItemText = (TextView)view.findViewById(R.id.list_item_empleado);
        listItemText.setText(list.get(position));

        BootstrapButton editEmpleado = (BootstrapButton)view.findViewById(R.id.editEmpleado);
        BootstrapButton deleteEmpleado = (BootstrapButton)view.findViewById(R.id.deleteEmpleado);

        deleteEmpleado.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Log.i("Empleado", "id empleado: " + idEmpleados.get(position));

                final String idEmpleado = idEmpleados.get(position);

                AlertDialog.Builder builder1 = new AlertDialog.Builder(context);
                builder1.setMessage("¿Deseas eliminar permanentemente?");
                builder1.setCancelable(true);

                builder1.setPositiveButton(
                        "Si",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                deleteEmpleado(idEmpleado);                            }
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

        editEmpleado.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v)  {
                Log.i("Empleado", "id empleado: " + idEmpleados.get(position));

                String idEmpleado = idEmpleados.get(position);
                editEmpleado(idEmpleado);
            }
        });
        return view;
    }
    public void deleteEmpleado(String idEmpleado){

        Backendless.Persistence.of( Empleado.class ).findById(idEmpleado, new AsyncCallback<Empleado>() {
            @Override
            public void handleResponse(final Empleado empleado )
            {
                Backendless.Persistence.of( Empleado.class ).remove( empleado,new AsyncCallback<Long>(){
                    public void handleResponse( Long response )
                    {
                        Toast.makeText(context.getApplicationContext(), "Empleado borrado.", Toast.LENGTH_LONG).show();

                        Intent menuEmpleados = new Intent(context.getApplicationContext(),MenuEmpleados.class);
                        context.startActivity(menuEmpleados);
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
            public void handleFault( BackendlessFault fault )
            {
                Toast.makeText(context.getApplicationContext(), fault.getCode(), Toast.LENGTH_LONG).show();
                Toast.makeText(context.getApplicationContext(), fault.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }
    public void editEmpleado(String idEmpleado){

        Backendless.Persistence.of( Empleado.class ).findById(idEmpleado, new AsyncCallback<Empleado>() {
            @Override
            public void handleResponse(final Empleado empleado )
            {
                ArrayList<String> editEmpleado = new ArrayList<>();
                editEmpleado.add(empleado.getNombre());
                editEmpleado.add(empleado.getApellidos());
                editEmpleado.add(empleado.getEmail());
                editEmpleado.add(empleado.getMovil());
                editEmpleado.add(empleado.getDireccion());

                Intent listEmpleados = new Intent(context.getApplicationContext(),EditEmpleado.class);
                listEmpleados.putExtra("empleado",empleado);
                context.startActivity(listEmpleados);
            }

            @Override
            public void handleFault( BackendlessFault fault )
            {
                Toast.makeText(context.getApplicationContext(), fault.getCode(), Toast.LENGTH_LONG).show();
                Toast.makeText(context.getApplicationContext(), fault.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }


    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int pos) {
        return list.get(pos);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }
}

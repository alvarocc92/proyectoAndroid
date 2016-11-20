package com.example.accplinux.probandobackendless;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
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

public class CustomAdapter extends BaseAdapter implements ListAdapter  {

    private ArrayList<String> list = new ArrayList<>();
    private ArrayList<String> idEmpleados = new ArrayList<>();

    private Context context;




    public CustomAdapter(ArrayList<String> list,ArrayList<String> idEmpleados, Context context) {
        this.list = list;
        this.context = context;
        this.idEmpleados=idEmpleados;
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


    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        View view = convertView;
        if (view == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.precarga_empleados, null);
        }

        //Handle TextView and display string from your list
        final TextView listItemText = (TextView)view.findViewById(R.id.list_item_empleado);
        listItemText.setText(list.get(position));

        //Handle buttons and add onClickListeners
        BootstrapButton editEmpleado = (BootstrapButton)view.findViewById(R.id.editEmpleado);
        BootstrapButton deleteEmpleado = (BootstrapButton)view.findViewById(R.id.deleteEmpleado);

        deleteEmpleado.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                //String nombre = listItemText.getText().toString();
                //String query = "nombreCompleto= "+ "'"+nombre+"'";
                //String whereClause = query;
                //BackendlessDataQuery dataQuery = new BackendlessDataQuery();
               // dataQuery.setWhereClause(whereClause);
                String idEmpleado = idEmpleados.get(position);
                Log.i("Empleado", "id empleado: " + idEmpleados.get(position));
                Backendless.Persistence.of( Empleado.class ).findById(idEmpleado, new AsyncCallback<Empleado>() {
                    @Override
                    public void handleResponse(final Empleado empleado )
                    {
                        Backendless.Persistence.of( Empleado.class ).remove( empleado,new AsyncCallback<Long>(){
                            public void handleResponse( Long response )
                            {
                                // Contact has been deleted. The response is the
                                // time in milliseconds when the object was deleted
                                Toast.makeText(context.getApplicationContext(), "Empleado borrado."+ empleado.getNombreCompleto(), Toast.LENGTH_LONG).show();
                                list.remove(position);
                                notifyDataSetChanged();

                            }
                            public void handleFault( BackendlessFault fault )
                            {
                                // an error has occurred, the error code can be
                                // retrieved with fault.getCode()
                                Toast.makeText(context.getApplicationContext(), fault.getCode(), Toast.LENGTH_LONG).show();
                            }
                        });
                    }
                    @Override
                    public void handleFault( BackendlessFault fault )
                    {
                        // an error has occurred, the error code can be retrieved with fault.getCode()
                        Toast.makeText(context.getApplicationContext(), fault.getCode(), Toast.LENGTH_LONG).show();
                    }
                });//find
            }//onclick
        });//onclicklistener
        editEmpleado.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v)  {

                final String idEmpleado = idEmpleados.get(position);
                Log.i("Empleado", "id empleado: " + idEmpleados.get(position));

                Backendless.Persistence.of( Empleado.class ).findById(idEmpleado, new AsyncCallback<Empleado>() {
                    @Override
                    public void handleResponse(final Empleado empleado )
                    {
                        Log.i("Empleado", "id empleado: " + idEmpleados.get(position));
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

                        // an error has occurred, the error code can be retrieved with fault.getCode()
                        Toast.makeText(context.getApplicationContext(), fault.getCode(), Toast.LENGTH_LONG).show();
                    }
                });//find
            }
        });
        return view;
    }
}

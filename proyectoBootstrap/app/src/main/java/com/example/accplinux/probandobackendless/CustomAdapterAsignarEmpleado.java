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
import com.backendless.async.callback.BackendlessCallback;
import com.backendless.exceptions.BackendlessFault;
import com.beardedhen.androidbootstrap.BootstrapButton;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class CustomAdapterAsignarEmpleado extends BaseAdapter implements ListAdapter  {

    //private ArrayList<String> list = new ArrayList<>();
   // private ArrayList<String> idEmpleados = new ArrayList<>();
    private Context context;
    private Proyecto proyecto;
    private List<Empleado> listEmpleados = new ArrayList<>();
    private List<Empleado> empleadosAsignados = new ArrayList<>();

    public CustomAdapterAsignarEmpleado(Proyecto proyecto, List<Empleado> listEmpleados, Context context) {

        this.listEmpleados = listEmpleados;
        this.context = context;
        this.proyecto = proyecto;
        this.empleadosAsignados = proyecto.getEmpleadoAsignados();

    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        View view = convertView;

        if (view == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.precarga_asignar_empleados, null);
        }

        if(listEmpleados.get(position).getDesasignado().equals(true)){

            final TextView listItemText = (TextView)view.findViewById(R.id.list_item_empleado);
            listItemText.setText(listEmpleados.get(position).getNombre()+" "+listEmpleados.get(position).getApellidos());
            BootstrapButton asignarEmpleado = (BootstrapButton) view.findViewById(R.id.asignar_empleado);
            BootstrapButton desAsignar = (BootstrapButton) view.findViewById(R.id.desAsignar);

            asignarEmpleado.setEnabled(false);
            asignarEmpleado.setVisibility(View.GONE);

            desAsignar.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View v)  {
                    Log.i("AsignarProyecto", "id empleado: " + listEmpleados.get(position).getObjectId()+" .Nombre del empleado : "+ listEmpleados.get(position).getNombre());
                    desAsignarProyectoEmpleado(listEmpleados.get(position),proyecto, position);
                }
            });

        }else{
            final TextView listItemText = (TextView)view.findViewById(R.id.list_item_empleado);
            listItemText.setText(listEmpleados.get(position).getNombre()+" "+listEmpleados.get(position).getApellidos());

            BootstrapButton asignarEmpleado = (BootstrapButton) view.findViewById(R.id.asignar_empleado);
            BootstrapButton desAsignar = (BootstrapButton) view.findViewById(R.id.desAsignar);

            desAsignar.setEnabled(false);
            desAsignar.setVisibility(View.GONE);

            asignarEmpleado.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View v)  {
                    Log.i("AsignarProyecto", "id empleado: " + listEmpleados.get(position).getObjectId()+" .Nombre del empleado : "+ listEmpleados.get(position).getNombre());
                    asignarProyectoEmpleado(listEmpleados.get(position), proyecto, position);
                }
            });
        }
        return view;
    }

    public void asignarProyectoEmpleado(final Empleado empleado, final Proyecto proyecto, final int position){

        proyecto.getEmpleadoAsignados().add(empleado);
        empleado.setProyecto(proyecto);
        empleado.setDesasignado(true);

        Backendless.Persistence.save(empleado, new BackendlessCallback<Empleado>() {

            @Override
            public void handleResponse(Empleado empleadoActualizado) {

                Log.i("Empleado", "empleado asignado" + empleado.getNombre());
                Log.i("Empleado", "id empleado asignado" + empleado.getObjectId());

                listEmpleados.remove(position);
                listEmpleados.add(position, empleadoActualizado);

                Intent asignarProyectoEmpleado = new Intent(context.getApplicationContext(), AsignarProyectoEmpleado.class);

                asignarProyectoEmpleado.putExtra("listEmpleados", (Serializable) listEmpleados);
                asignarProyectoEmpleado.putExtra("proyecto", proyecto);

                context.startActivity(asignarProyectoEmpleado);


                Toast.makeText(context.getApplicationContext(), "Empleado asignado.", Toast.LENGTH_LONG).show();

            }
            @Override
            public void handleFault(BackendlessFault backendlessFault) {
                Toast.makeText(context.getApplicationContext(), backendlessFault.getMessage(), Toast.LENGTH_LONG).show();
                Toast.makeText(context.getApplicationContext(), backendlessFault.getCode(), Toast.LENGTH_LONG).show();
            }
        });

        Backendless.Persistence.save(proyecto, new BackendlessCallback<Proyecto>() {
            @Override
            public void handleResponse(Proyecto proyecto) {

                Log.i("Empleado", "empleado asignado" + empleado.getNombre());
                Log.i("Empleado", "id empleado asignado" + empleado.getObjectId());

            }
            @Override
            public void handleFault(BackendlessFault backendlessFault) {
                Toast.makeText(context.getApplicationContext(), backendlessFault.getMessage(), Toast.LENGTH_LONG).show();
                Toast.makeText(context.getApplicationContext(), backendlessFault.getCode(), Toast.LENGTH_LONG).show();
            }
        });
    }

    public void desAsignarProyectoEmpleado(final Empleado empleado, Proyecto proyecto, final int position) {


        proyecto.getEmpleadoAsignados().remove(empleado);

        //para update la actividad

        Backendless.Persistence.save(proyecto, new BackendlessCallback<Proyecto>() {
            @Override
            public void handleResponse(final Proyecto proyecto) {

                empleado.setDesasignado(false);
                empleado.setProyecto(null);

                Backendless.Persistence.save(empleado, new BackendlessCallback<Empleado>() {
                    @Override
                    public void handleResponse(Empleado empleado) {

                        listEmpleados.remove(position);
                        listEmpleados.add(position, empleado);

                        Log.i("Empleado", "empleado asignado" + empleado.getNombre());
                        Log.i("Empleado", "id empleado asignado" + empleado.getObjectId());

                        Toast.makeText(context.getApplicationContext(), "Empleado desasignado.", Toast.LENGTH_LONG).show();

                        Intent asignarProyectoEmpleado = new Intent(context, AsignarProyectoEmpleado.class);

                        asignarProyectoEmpleado.putExtra("listEmpleados", (Serializable) listEmpleados);
                        asignarProyectoEmpleado.putExtra("proyecto",proyecto);

                        context.startActivity(asignarProyectoEmpleado);

                    }
                    @Override
                    public void handleFault(BackendlessFault backendlessFault) {
                        Toast.makeText(context.getApplicationContext(), backendlessFault.getMessage(), Toast.LENGTH_LONG).show();
                        Toast.makeText(context.getApplicationContext(), backendlessFault.getCode(), Toast.LENGTH_LONG).show();
                    }
                });

                Log.i("Empleado", "empleado asignado" + empleado.getNombre());
                Log.i("Empleado", "id empleado asignado" + empleado.getObjectId());

            }
            @Override
            public void handleFault(BackendlessFault backendlessFault) {
                Toast.makeText(context.getApplicationContext(), backendlessFault.getMessage(), Toast.LENGTH_LONG).show();
                Toast.makeText(context.getApplicationContext(), backendlessFault.getCode(), Toast.LENGTH_LONG).show();
            }
        });
    }

    @Override
    public int getCount() {
        return listEmpleados.size();
    }

    @Override
    public Object getItem(int pos) {
        return listEmpleados.get(pos);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

}

     /* empleado.setDesasignado(false);
        empleado.setProyecto(null);

        Backendless.Persistence.save(empleado, new BackendlessCallback<Empleado>() {
            @Override
            public void handleResponse(Empleado empleado) {

                Log.i("Empleado", "empleado asignado" + empleado.getNombre());
                Log.i("Empleado", "id empleado asignado" + empleado.getObjectId());

                Toast.makeText(context.getApplicationContext(), "Empleado desasignado.", Toast.LENGTH_LONG).show();

            }
            @Override
            public void handleFault(BackendlessFault backendlessFault) {
                Toast.makeText(context.getApplicationContext(), backendlessFault.getMessage(), Toast.LENGTH_LONG).show();
                Toast.makeText(context.getApplicationContext(), backendlessFault.getCode(), Toast.LENGTH_LONG).show();
            }
        });*/
    /*public void deleteEmpleado(String idEmpleado){

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
    }*/
   /*final TextView listItemText = (TextView)view.findViewById(R.id.list_item_empleado);
        listItemText.setText(listEmpleados.get(position).getNombre()+" "+listEmpleados.get(position).getApellidos());

        BootstrapButton asignarEmpleado = (BootstrapButton) view.findViewById(R.id.asignar_empleado);

        asignarEmpleado.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v)  {
                Log.i("AsignarProyecto", "id empleado: " + listEmpleados.get(position).getObjectId()+" .Nombre del empleado : "+ listEmpleados.get(position).getNombre());
                asignarProyectoEmpleado(listEmpleados.get(position),proyecto);
            }
        });*/
      /*boolean asignado = false;
        for(int i = 0; i<empleadosAsignados.size();i++){
            if(listEmpleados.get(position).getObjectId().equals(empleadosAsignados.get(i).getObjectId())){
                asignado=true;
                break;
            }else{
                asignado=false;
            }
        }*/
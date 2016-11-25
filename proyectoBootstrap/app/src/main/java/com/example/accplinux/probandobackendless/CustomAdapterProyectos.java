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
import com.backendless.async.callback.AsyncCallback;
import com.backendless.exceptions.BackendlessFault;
import com.beardedhen.androidbootstrap.BootstrapButton;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by accplinux on 23/11/16.
 */

public class CustomAdapterProyectos extends BaseAdapter implements ListAdapter {

    private Context context;
    private List<Proyecto> listProyectos ;

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

        editProyecto.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v)  {
                Log.i("Proyecto", "id proyecto: " + listProyectos.get(position).getObjectId());
                editProyecto(listProyectos.get(position));
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
}

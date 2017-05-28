package com.example.accplinux.probandobackendless;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.backendless.Backendless;
import com.backendless.BackendlessCollection;
import com.backendless.BackendlessUser;
import com.backendless.async.callback.AsyncCallback;
import com.backendless.async.callback.BackendlessCallback;
import com.backendless.exceptions.BackendlessFault;
import com.backendless.persistence.BackendlessDataQuery;
import com.github.tibolte.agendacalendarview.AgendaCalendarView;
import com.github.tibolte.agendacalendarview.CalendarPickerController;
import com.github.tibolte.agendacalendarview.models.BaseCalendarEvent;
import com.github.tibolte.agendacalendarview.models.CalendarEvent;
import com.github.tibolte.agendacalendarview.models.DayItem;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

public class MenuAgenda extends AppCompatActivity implements  CalendarPickerController{

    AgendaCalendarView agenda;
    Dialog openDialog;
    ProgressDialog pDialog;
    Calendar minDate = Calendar.getInstance();
    Calendar maxDate = Calendar.getInstance();
    List<CalendarEvent> eventList = new ArrayList<>();
    BackendlessUser currentUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu_agenda);
        agenda = (AgendaCalendarView) findViewById(R.id.agenda_calendar_view);

        minDate.add(Calendar.MONTH, -6);
        minDate.set(Calendar.DAY_OF_MONTH, 1);
        maxDate.add(Calendar.YEAR, 1);

        currentUser = Backendless.UserService.CurrentUser();

        pDialog = new ProgressDialog(MenuAgenda.this);
        pDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        pDialog.setMessage("Cargando eventos...");
        pDialog.setCancelable(true);
        pDialog.setMax(100);


        CargarEventos cargareventos = new CargarEventos();
        cargareventos.execute();

    }

    @Override
    public void onDaySelected(final DayItem dayItem) {

        openDialog = new Dialog(MenuAgenda.this);
        openDialog.setContentView(R.layout.dialog_crear_evento);
        openDialog.setCanceledOnTouchOutside(false);

        Button crearEvento = (Button) openDialog.findViewById(R.id.btn_yes);
        Button cancelar = (Button) openDialog.findViewById(R.id.btn_no);

        final EditText titulo = (EditText) openDialog.findViewById(R.id.titulo);
        final EditText description = (EditText) openDialog.findViewById(R.id.description);
        final EditText localizacion = (EditText) openDialog.findViewById(R.id.localizacion);

        crearEvento.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Agenda event = new Agenda();

                if(titulo.getText()!= null && !titulo.getText().toString().isEmpty()){
                    event.setTitulo(titulo.getText().toString());
                    event.setDescription(description.getText().toString());
                    event.setLocalizacion(localizacion.getText().toString());
                    event.setInicio(dayItem.getDate());
                    guardarEvento(event);
                }else{
                    Toast.makeText(getApplicationContext(), "El título no puede estar vacío.", Toast.LENGTH_LONG).show();
                }

            }
        });

        cancelar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openDialog.dismiss();
            }
        });
        openDialog.show();
    }

    @Override
    public void onEventSelected(final CalendarEvent event) {

        if(!event.getTitle().equals("No events") && event.getTitle().length()>0){

            AlertDialog.Builder builder1 = new AlertDialog.Builder(this);
            builder1.setMessage("¿Deseas eliminar este evento?");
            builder1.setCancelable(true);

            builder1.setPositiveButton(
                    "Si",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            deleteEvento(event);
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
        }else{
            Toast.makeText(getApplicationContext(), "No hay eventos ese día.", Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void onScrollToDate(Calendar calendar) {

    }

    private void deleteEvento(CalendarEvent event){

        String busqueda = "titulo >= '"+event.getTitle()+"'";

        BackendlessDataQuery dataQuery = new BackendlessDataQuery();
        dataQuery.setWhereClause( busqueda );

        Backendless.Persistence.of( Agenda.class ).find( dataQuery,
                new AsyncCallback<BackendlessCollection<Agenda>>(){
                    @Override
                    public void handleResponse( BackendlessCollection<Agenda> foundContacts )
                    {
                        Backendless.Persistence.of( Agenda.class ).remove( foundContacts.getData().get(0),new AsyncCallback<Long>(){
                            public void handleResponse( Long response )
                            {
                                Toast.makeText(MenuAgenda.this, "Evento borrado.", Toast.LENGTH_LONG).show();

                                pDialog = new ProgressDialog(MenuAgenda.this);
                                pDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
                                pDialog.setMessage("Cargando eventos...");
                                pDialog.setCancelable(true);
                                pDialog.setMax(100);

                                eventList = new ArrayList<>();

                                CargarEventos cargareventos = new CargarEventos();
                                cargareventos.execute();

                            }
                            public void handleFault( BackendlessFault fault )
                            {
                                Toast.makeText(MenuAgenda.this, fault.getCode(), Toast.LENGTH_LONG).show();
                                Toast.makeText(MenuAgenda.this, fault.getMessage(), Toast.LENGTH_LONG).show();
                            }
                        });
                    }
                    @Override
                    public void handleFault( BackendlessFault fault )
                    {
                        Toast.makeText(MenuAgenda.this, fault.getMessage(), Toast.LENGTH_LONG).show();
                        Toast.makeText(MenuAgenda.this, fault.getCode(), Toast.LENGTH_LONG).show();
                    }
                });

    }

    private void guardarEvento(Agenda event){

        Backendless.Persistence.save(new Agenda(event.getTitulo(), event.getDescription(), event.getLocalizacion(),event.getInicio(),currentUser), new BackendlessCallback<Agenda>() {
            @Override
            public void handleResponse(Agenda empleado) {

                Toast.makeText(getApplicationContext(), "Evento creado.", Toast.LENGTH_LONG).show();
                openDialog.dismiss();

                eventList = new ArrayList<>();

                CargarEventos cargareventos = new CargarEventos();
                cargareventos.execute();

            }
            @Override
            public void handleFault(BackendlessFault backendlessFault) {
                Toast.makeText(getApplicationContext(), backendlessFault.getMessage(), Toast.LENGTH_LONG).show();
                Toast.makeText(getApplicationContext(), backendlessFault.getCode(), Toast.LENGTH_LONG).show();
                openDialog.dismiss();
            }
        });

    }

    private class CargarEventos extends AsyncTask<Void, Integer, Boolean> {
        @Override
        protected Boolean doInBackground(Void... params) {
            final boolean[] acabado = {false};
            String busqueda = "inicio >= '"+minDate.getTime()+"'";
            BackendlessDataQuery dataQuery = new BackendlessDataQuery();
            dataQuery.setWhereClause( busqueda );
            Backendless.Persistence.of( Agenda.class ).find( dataQuery,
                    new AsyncCallback<BackendlessCollection<Agenda>>(){
                        @Override
                        public void handleResponse( BackendlessCollection<Agenda> foundContacts )
                        {
                            if(foundContacts.getTotalObjects()>0){

                                for(int i = 0; i<foundContacts.getTotalObjects(); i++){

                                    Agenda evento = foundContacts.getData().get(i);

                                    if(evento.getFin()==null && currentUser.getObjectId().equals(foundContacts.getData().get(i).getUsuario().getObjectId())){
                                        evento.setFin(evento.getInicio());
                                        Calendar inicio = Calendar.getInstance();
                                        Calendar fin = Calendar.getInstance();
                                        inicio.setTime(evento.getInicio());
                                        fin.setTime(evento.getFin());
                                        BaseCalendarEvent event = new BaseCalendarEvent(evento.getTitulo(), evento.getDescription(), evento.getLocalizacion(),
                                                ContextCompat.getColor(getApplicationContext(), R.color.bootstrap_brand_success), inicio, fin, true);
                                        eventList.add(event);
                                    }
                                    acabado[0] = true;
                                    onPostExecute(acabado[0]);
                                }
                            }else{
                                Toast.makeText(MenuAgenda.this, "No hay eventos registrados.", Toast.LENGTH_LONG).show();
                            }
                        }
                        @Override
                        public void handleFault( BackendlessFault fault )
                        {
                            Toast.makeText(MenuAgenda.this, fault.getMessage(), Toast.LENGTH_LONG).show();
                            Toast.makeText(MenuAgenda.this, fault.getCode(), Toast.LENGTH_LONG).show();
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
                    CargarEventos.this.cancel(true);
                }
            });

            pDialog.setProgress(0);
            pDialog.show();
        }

        @Override
        protected void onPostExecute(Boolean result) {
            if(result)
            {
                pDialog.dismiss();
                Toast.makeText(MenuAgenda.this, "Eventos cargados." ,Toast.LENGTH_SHORT).show();
                agenda.init(eventList, minDate, maxDate, Locale.getDefault(), MenuAgenda.this);
            }
        }

        @Override
        protected void onCancelled() {
            Toast.makeText(MenuAgenda.this, "Tarea cancelada!", Toast.LENGTH_SHORT).show();
        }
    }

}

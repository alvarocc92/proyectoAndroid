package com.example.accplinux.probandobackendless;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.backendless.Backendless;
import com.backendless.BackendlessUser;
import com.backendless.async.callback.AsyncCallback;
import com.backendless.async.callback.BackendlessCallback;
import com.backendless.exceptions.BackendlessFault;
import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.appindexing.Thing;
import com.google.android.gms.common.api.GoogleApiClient;

public class MainActivity extends AppCompatActivity {

    Button bR, bL;
    EditText ed1, ed2;
    TextView tx1;
    int counter = 3;
    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    private GoogleApiClient client;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Backendless.initApp(this, BackendSettings.APP_ID, BackendSettings.SECRET_KEY, BackendSettings.VERSION);

        bL = (Button) findViewById(R.id.buttonLogin);
        bR = (Button) findViewById(R.id.buttonRegister);
        ed1 = (EditText) findViewById(R.id.usuario);
        ed2 = (EditText) findViewById(R.id.password);

        //  tx1.setVisibility(View.GONE);

        bR.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                registerUserAsync();
            }
        });
        bL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loginUserAsync();
            }
        });


        //registerUserAsync();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();
    }


    private void registerUserAsync() {

        final BackendlessUser user = new BackendlessUser();

        user.setEmail(ed1.getText().toString());
        user.setPassword(ed2.getText().toString());

        AsyncCallback<BackendlessUser> callback = new AsyncCallback<BackendlessUser>() {
            @Override
            public void handleResponse(BackendlessUser registeredUser) {
                System.out.println("User has been registered - " + registeredUser.getObjectId());

                Backendless.Persistence.save(new Comment("Se ha producido un nuevo registro : " + user.getEmail() + ".", user.getEmail()), new BackendlessCallback<Comment>() {
                    @Override
                    public void handleResponse(Comment comment) {
                        Log.i("Comments", "Got new comment from " + user.getEmail());
                        Toast.makeText(getApplicationContext(), "Registrado con éxito, pulsa 'login'.", Toast.LENGTH_LONG).show();

                    }
                });

            }

            @Override
            public void handleFault(BackendlessFault backendlessFault) {
                System.out.println("Server reported an error - " + backendlessFault.getMessage());
                Toast.makeText(getApplicationContext(), backendlessFault.getMessage(), Toast.LENGTH_LONG).show();
            }
        };

        //user.setProperty( "phoneNumber", "666-666-666" );

        Backendless.UserService.register(user, callback);


    }

    public void loginUserAsync() {
        final BackendlessUser user = new BackendlessUser();

        user.setEmail(ed1.getText().toString());
        user.setPassword(ed2.getText().toString());

        AsyncCallback<BackendlessUser> callback = new AsyncCallback<BackendlessUser>() {
            @Override
            public void handleResponse(BackendlessUser loggedUser) {
                System.out.println("User logged - " + loggedUser.getObjectId());
                Toast.makeText(getApplicationContext(), "Bienvenido.", Toast.LENGTH_LONG).show();

                Backendless.Persistence.save(new Comment("Se ha producido un nuevo login en la aplicación de : " + user.getEmail() + ".",
                        user.getEmail()), new BackendlessCallback<Comment>() {
                    @Override
                    public void handleResponse(Comment comment) {
                        Log.i("Comments", "Got new login from " + user.getEmail());
                    }
                });
                Intent menuPrincipal = new Intent(MainActivity.this, MenuPrincipal.class);
                startActivity(menuPrincipal);
            }

            @Override
            public void handleFault(BackendlessFault backendlessFault) {
                System.out.println("Server reported an error - " + backendlessFault.getMessage());
                Toast.makeText(getApplicationContext(), backendlessFault.getMessage(), Toast.LENGTH_LONG).show();
            }
        };
        Backendless.UserService.login(user.getEmail(), user.getPassword(), callback);

    }

    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    public Action getIndexApiAction() {
        Thing object = new Thing.Builder()
                .setName("Main Page") // TODO: Define a title for the content shown.
                // TODO: Make sure this auto-generated URL is correct.
                .setUrl(Uri.parse("http://[ENTER-YOUR-URL-HERE]"))
                .build();
        return new Action.Builder(Action.TYPE_VIEW)
                .setObject(object)
                .setActionStatus(Action.STATUS_TYPE_COMPLETED)
                .build();
    }

    @Override
    public void onStart() {
        super.onStart();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client.connect();
        AppIndex.AppIndexApi.start(client, getIndexApiAction());
    }

    @Override
    public void onStop() {
        super.onStop();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        AppIndex.AppIndexApi.end(client, getIndexApiAction());
        client.disconnect();
    }
}

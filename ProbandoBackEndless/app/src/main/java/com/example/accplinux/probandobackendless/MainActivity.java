package com.example.accplinux.probandobackendless;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.backendless.Backendless;
import com.backendless.BackendlessUser;
import com.backendless.async.callback.AsyncCallback;
import com.backendless.async.callback.BackendlessCallback;
import com.backendless.exceptions.BackendlessFault;

public class MainActivity extends AppCompatActivity {

    public static String APP_ID = "1BF00D70-57EA-B78B-FFEA-0329E7E68200";
    public static String SECRET_KEY = "F6A09F64-D438-1AE7-FF11-11F48E039A00";
    public static String VERSION = "v1";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Backendless.initApp(this, APP_ID, SECRET_KEY, VERSION );

        registerUserAsync();

    }


    private static void registerUserAsync()
        {
            AsyncCallback<BackendlessUser> callback = new AsyncCallback<BackendlessUser>()
            {
                @Override
                public void handleResponse( BackendlessUser registeredUser )
                {
                    System.out.println( "User has been registered - " + registeredUser.getObjectId() );
                }

                @Override
                public void handleFault( BackendlessFault backendlessFault )
                {
                    System.out.println( "Server reported an error - " + backendlessFault.getMessage() );
                }
            };

            final BackendlessUser user = new BackendlessUser();
            user.setEmail( "alvarocc.ac@gmail.com" );
            user.setPassword( "a1a2a3a4" );
            user.setProperty( "phoneNumber", "666-666-666" );

            Backendless.UserService.register( user, callback );

            Backendless.Persistence.save( new Comment( "Funciona!!!!", user.getEmail() ), new BackendlessCallback<Comment>()
            {
                @Override
                public void handleResponse( Comment comment )
                {
                    Log.i( "Comments", "Got new comment from " + user.getEmail() );
                }
            } );
        }
}

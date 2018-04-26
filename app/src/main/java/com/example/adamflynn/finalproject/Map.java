package com.example.adamflynn.finalproject;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.LocationManager;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;

public class Map extends AppCompatActivity {

    private static final int ERROR_DIALOG_REQUEST = 9001;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);

        if(isServicesOK()){
                init();
                statusCheck();
            }
        }
        private void init(){
            Button btnMap = (Button)findViewById(R.id.btnMap);
            btnMap.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(Map.this, MapActivity.class);
                    startActivity(intent);
                }
            });
    }

    public boolean isServicesOK(){
        Toast.makeText(this, "checking google services version", Toast.LENGTH_LONG).show();

        int available = GoogleApiAvailability.getInstance().isGooglePlayServicesAvailable(Map.this);
        if(available == ConnectionResult.SUCCESS) {
            //everything is fine and the user can make map requests
            Toast.makeText(this, "Google Play Services is working", Toast.LENGTH_LONG).show();
            return true;
        }else if(GoogleApiAvailability.getInstance().isUserResolvableError(available)){
            //an error occured but we can resolve it
            Toast.makeText(this, "an error occured but we can fix it", Toast.LENGTH_LONG).show();
            Dialog dialog = GoogleApiAvailability.getInstance().getErrorDialog(Map.this, available, ERROR_DIALOG_REQUEST);
            dialog.show();
        }else{
            Toast.makeText(this, "You can't make requests", Toast.LENGTH_LONG).show();
        }
        return false;
    }

    public void statusCheck(){
        final LocationManager manager = (LocationManager)getSystemService(Context.LOCATION_SERVICE);
        assert manager != null;
        if (!manager.isProviderEnabled(LocationManager.GPS_PROVIDER)) buildAlertMessageNoGps();
    }

    private void buildAlertMessageNoGps() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Your GPS seems to be disabled, do you want to enable it?")
                .setCancelable(false)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(final DialogInterface dialog,final int which) {
                        startActivity(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS));
                    }
                });
        final AlertDialog alert = builder.create();
        alert.show();
    }

}

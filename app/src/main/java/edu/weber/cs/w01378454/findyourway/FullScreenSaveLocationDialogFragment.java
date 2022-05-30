package edu.weber.cs.w01378454.findyourway;

import android.Manifest;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.location.Location;

import android.os.Bundle;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;

import androidx.fragment.app.DialogFragment;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.textfield.TextInputLayout;

import android.location.LocationManager;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Executor;

import android.Manifest;


public class FullScreenSaveLocationDialogFragment extends DialogFragment {

    private MenuItem SaveCurrentLocation;
    private View root;

    private TextInputLayout LocationName;
    private TextInputLayout GPSCoordinates;
    private TextInputLayout LocationComments;

    double Longitude;
    double Latitude;

    long LatAndLong;

    private Context context;
    Activity activity;

    private FusedLocationProviderClient fusedLocationClient;
    private LocationCallback locationCallback;
    private LocationRequest locationRequest;



    public FullScreenSaveLocationDialogFragment() {
        // Required empty public constructor
    }

    public FullScreenSaveLocationDialogFragment(Context context, double Longitude, double Latitude) {
        this.context = context;
        this.Longitude = Longitude;
        this.Latitude = Latitude;
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(FullScreenSaveLocationDialogFragment.STYLE_NORMAL, R.style.AppTheme_Dialog_FullScreen);

        activity = getActivity();

        locationRequest = LocationRequest.create();
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        locationRequest.setInterval(20 * 1000);

        locationCallback = new LocationCallback(){
            @Override
            public void onLocationResult(LocationResult locationResult) {
                super.onLocationResult(locationResult);

                if (locationResult == null) {
                    return;
                }
                for (Location location : locationResult.getLocations()) {
                    // Update UI with location data
                    // ...
                }
            }
        };

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(activity);

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return root = inflater.inflate(R.layout.fragment_full_screen_save_location_dialog, container, false);
    }

    @Override
    public void onResume() {
        super.onResume();

       /* if (requestingLocationUpdates) { //requestingLocationUpdate
            startLocationUpdates();
        }*/

        LocationName = root.findViewById(R.id.LocationName);
        GPSCoordinates = root.findViewById(R.id.GPSLocation);
        LocationComments = root.findViewById(R.id.LocationComments);

        lastlocation();

        GPSCoordinates.getEditText().setText(LatAndLong + "");

    }

    @Override
    public void onPause() {
        super.onPause();

        stopLocationUpdates();
    }

    @Override
    public void onStart() {
        super.onStart();
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        requireDialog().getWindow().setWindowAnimations(R.style.AppTheme_Dialog_FullScreen);

        Toolbar toolbar = view.findViewById(R.id.toolbar);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });


        toolbar.inflateMenu(R.menu.fullscreensavelocationdialog);
        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.Save:

                        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child("Locations").push();
                        Map<String, Object> map = new HashMap<>();
                        map.put("Id", databaseReference.getKey());
                        map.put("LocationName", LocationName.getEditText().getText().toString());
                        map.put("GPSCoordinates", GPSCoordinates.getEditText().getText().toString());
                        map.put("LocationComments", LocationComments.getEditText().getText().toString());

                        databaseReference.setValue(map);

                        Toast toast = Toast.makeText(activity, "Location Saved", Toast.LENGTH_SHORT);
                        toast.show();

                        dismiss();

                        return true;
                    default:
                        return false;
                }
            }
        });
    }


    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        Dialog dialog = super.onCreateDialog(savedInstanceState);
        dialog.getWindow().setWindowAnimations(R.style.AppTheme_Dialog_FullScreen);

        return dialog;
    }

    public void lastlocation() {
        if (ActivityCompat.checkSelfPermission(activity, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(activity, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        fusedLocationClient.getLastLocation().addOnSuccessListener(activity, new OnSuccessListener<Location>() {
                    @Override
                    public void onSuccess(Location location) {
                        // Got last known location. In some rare situations this can be null.
                        if (location != null) {

                            Latitude = location.getLatitude();
                            Longitude = location.getLongitude();

                            String Lat = Double.toString(Latitude);
                            String Lon = Double.toString(Longitude);
                         

                            GPSCoordinates.getEditText().setText(Lat + "/" + Lon);

                        }
                    }
                });
    }

    private void startLocationUpdates() {
        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.


            return;
        }


        fusedLocationClient.requestLocationUpdates(locationRequest,locationCallback, Looper.getMainLooper());


    }

    private void stopLocationUpdates() {
        fusedLocationClient.removeLocationUpdates(locationCallback);
    }



}


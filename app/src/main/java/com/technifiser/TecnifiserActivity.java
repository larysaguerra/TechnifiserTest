package com.technifiser;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.technifiser.utils.CustomProgressDialog;

/**
 * Created by guerra on 10/08/17.
 * Util base activity.
 */

public class TecnifiserActivity extends AppCompatActivity {

    private CustomProgressDialog progressDialog;
    private boolean pause = false;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        progressDialog = new CustomProgressDialog(this);
        progressDialog.setCancelable(false);
    }

    @Override
    protected void onPause() {
        super.onPause();
        pause = true;
    }

    @Override
    protected void onResume() {
        super.onResume();
        pause = false;
    }

    /**
     * Util show toast.
     */
    public void showToast(String message) {
        Toast toast = Toast.makeText(getActivity(), message, Toast.LENGTH_LONG);
        toast.getView().setBackgroundColor(ContextCompat.getColor(getActivity(), R.color.colorPrimaryDark));
        toast.show();
    }

    /**
     * Util show progress dialog.
     */
    public void showProgressDialog(String message) {
        progressDialog.setMessage(message);
        if (!pause) {
            progressDialog.show();
        }
    }

    /**
     * Util hide progress dialog.
     */
    public void dismissProgressDialog() {
        progressDialog.dismiss();
    }

    /**
     * Return activity.
     */
    public Activity getActivity() {
        return this;
    }

    /**
     * Util show alert dialog.
     */
    public void showAlertDialog(final String message) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getActivity());
                alertDialogBuilder
                        .setMessage(message)
                        .setCancelable(false)
                        .setIcon(R.drawable.icon_sushi_logo)
                        .setPositiveButton(R.string.accept, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                            }
                        });
                AlertDialog alertDialog = alertDialogBuilder.create();
                alertDialog.show();
            }
        });
    }

    /**
     * Util checl permission.
     */
    public void askForPermissions(String permission, int requestCode) {
        ActivityCompat.requestPermissions(this, new String[]{permission}, requestCode);
    }

    /**
     * Return check permission granted.
     */
    public boolean isPermissionGranted(String permission) {
        return ContextCompat.checkSelfPermission(TecnifiserApplication.getInstance(), permission) == PackageManager.PERMISSION_GRANTED;
    }

    /**
     * Move camera on fragmentMap
     */
    public void moveCamera(GoogleMap map, double lat, double lng, float zoom) {
        LatLng venue = new LatLng(lat, lng);
        map.moveCamera(CameraUpdateFactory.newLatLngZoom(venue, zoom));
    }

    /**
     * Return custom marker.
     */
    public Marker createMarket(GoogleMap map, LatLng latLng, String name) {
        return map.addMarker(new MarkerOptions()
                .position(latLng)
                .title(name)
                .icon(BitmapDescriptorFactory.fromResource(R.drawable.icon_marker)));
    }

    /**
     * Util open map with route.
     */
    public void openMapRoute(Location latLngFrom, LatLng latLngTo) {
        if (latLngFrom != null) {
            try {
                String urlMaps = "https://www.google.com.co/maps/dir/" +
                        String.valueOf(latLngFrom.getLatitude()) + ",+" +
                        String.valueOf(latLngFrom.getLongitude()) + "/" +
                        String.valueOf(latLngTo.latitude) + ",+" +
                        String.valueOf(latLngTo.longitude) + "/";
                Intent i = new Intent(Intent.ACTION_VIEW);
                i.setData(Uri.parse(urlMaps));
                startActivity(i);
            } catch (Exception e) {
                showToast(getString(R.string.error_open_map));
            }
        } else {
            showToast(getString(R.string.error_open_map));
        }
    }

    /**
     * Util open profile.
     */
    public void openWebView(int resource) {
        Intent i = new Intent(Intent.ACTION_VIEW);
        i.setData(Uri.parse(getString(resource)));
        startActivity(i);
    }

}

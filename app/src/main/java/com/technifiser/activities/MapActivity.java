package com.technifiser.activities;

import android.content.pm.PackageManager;
import android.location.Location;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.CardView;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.technifiser.R;
import com.technifiser.TecnifiserActivity;
import com.technifiser.api.ApiTecnifiser;
import com.technifiser.api.SearchByQueryCallback;
import com.technifiser.api.result.Group;
import com.technifiser.api.result.Item;
import com.technifiser.api.result.Response;

import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by guerra on 10/08/17.
 * Map with information on Sushi restaurants around the user's location.
 */

public class MapActivity extends TecnifiserActivity implements
        OnMapReadyCallback,
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener,
        GoogleMap.OnInfoWindowClickListener,
        GoogleMap.OnMarkerClickListener,
        SearchByQueryCallback {

    @BindView(R.id.card_title)
    CardView cardTitle;

    @BindView(R.id.image_result)
    ImageView imageResult;
    @BindView(R.id.text_result_cant)
    TextView textResultCant;
    @BindView(R.id.text_result)
    TextView textResult;
    @BindView(R.id.image_marker)
    ImageView imageMarker;

    @BindView(R.id.linear_description)
    LinearLayout linearDescription;
    @BindView(R.id.text_name)
    TextView textName;
    @BindView(R.id.text_address)
    TextView textAddress;
    @BindView(R.id.text_distance)
    TextView textDistance;

    @BindView(R.id.linear_map)
    LinearLayout linearMap;

    private GoogleApiClient mGoogleApiClient;
    private GoogleMap mMap;

    private float DEFAULT_ZOOM = 14;
    private Location lastLocation = null;

    private ApiTecnifiser apiTecnifiser;

    private Marker markerSelected;
    private HashMap<Marker, Item> eventMarkerMap = new HashMap<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);
        ButterKnife.bind(this);
        initToolbar();
        initMap();
        initGoogleApiClient();
        initApiSearch();
        showProgressDialog(getString(R.string.wait_search));
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.setOnInfoWindowClickListener(this);
        mMap.setOnMarkerClickListener(this);
        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            mMap.setMyLocationEnabled(true);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        mGoogleApiClient.connect();
    }

    @Override
    protected void onPause() {
        super.onPause();
        mGoogleApiClient.disconnect();
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            lastLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
            if (lastLocation != null) {
                String userLL = lastLocation.getLatitude() + "," + lastLocation.getLongitude();
                double userLLAcc = lastLocation.getAccuracy();
                moveCamera(mMap, lastLocation.getLatitude(), lastLocation.getLongitude(), DEFAULT_ZOOM);
                String QUERY_SEARCH = "sushi";
                apiTecnifiser.search(QUERY_SEARCH, userLL, userLLAcc, this);
            } else {
                showAlertDialog(getString(R.string.error_location));
            }
        }
    }

    @Override
    public void onConnectionSuspended(int i) {
        showToast(getString(R.string.error_connection));
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        showToast(getString(R.string.error_connection));
    }

    @Override
    public void searchOnSuccess(Response response) {
        dismissProgressDialog();
        if (resultFound(response.getTotalResults())) {
            List<Group> group = response.getGroups();
            if (!group.isEmpty()) {
                List<Item> items = group.get(0).getItems();
                boolean move = true;
                for (Item item : items) {
                    Marker marker = createMarket(mMap, new LatLng(item.getVenue().getLocation().getLat(),
                            item.getVenue().getLocation().getLng()), item.getVenue().getName());
                    marker.showInfoWindow();
                    eventMarkerMap.put(marker, item);
                    if (move) {
                        moveCamera(mMap, item.getVenue().getLocation().getLat(),
                                item.getVenue().getLocation().getLng(), DEFAULT_ZOOM);
                        move = false;
                    }
                }
            }
        }
    }

    @Override
    public void searchError(int code, String mjs) {
        dismissProgressDialog();
        showToast(getString(R.string.error_connection));
    }

    @Override
    public boolean onMarkerClick(Marker marker) {
        updateUi(marker);
        return false;
    }

    @Override
    public void onInfoWindowClick(Marker marker) {
        updateUi(marker);
    }

    @OnClick(R.id.button_go)
    void openMap() {
        openMapRoute(lastLocation, markerSelected.getPosition());
    }

    private void initToolbar() {
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
    }

    private void initMap() {
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    private void initGoogleApiClient() {
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this, this)
                .addConnectionCallbacks(this)
                .addApi(LocationServices.API)
                .build();
        mGoogleApiClient.connect();
    }

    private void initApiSearch() {
        apiTecnifiser = new ApiTecnifiser();
    }

    private boolean resultFound(int cant) {
        boolean hasResult = cant != 0;
        imageResult.setVisibility(View.VISIBLE);
        if (hasResult) {
            imageResult.setImageResource(R.drawable.icon_sushi_02);
            imageMarker.setVisibility(View.VISIBLE);
            textResultCant.setText("" + cant + " ");
            textResult.setText(getString(R.string.result_restaurants_found));
        } else {
            imageMarker.setVisibility(View.INVISIBLE);
            imageResult.setImageResource(R.drawable.icon_sushi_03);
            textResult.setText(getString(R.string.result_not_found));
        }
        return hasResult;
    }

    private void updateUi(Marker marker) {
        markerSelected = marker;
        linearDescription.setVisibility(View.VISIBLE);
        Item item = eventMarkerMap.get(marker);
        textName.setText(" " + item.getVenue().getName() + " ");
        textAddress.setText(" " + item.getVenue().getLocation().getAddress() + "");
        textDistance.setText(" " + item.getVenue().getLocation().getDistance() + " Mtrs.");
    }

}
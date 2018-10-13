package com.ssit.realm.activity;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.ssit.realm.R;
import com.ssit.realm.model.Users;
import com.ssit.realm.realm.RealmController;

import io.realm.Realm;

public class UserDetails extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private long userID = 0;
    LatLng latlang;
    private String MarkerAddress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.userdetails);

        Bundle extras = getIntent().getExtras();
        userID = extras.getLong("position");

        Realm realm = RealmController.getInstance().getRealm();

        Users users = realm.where(Users.class).equalTo("id", userID).findFirst();

        TextView name = (TextView) findViewById(R.id.name);
        TextView username = (TextView) findViewById(R.id.username);
        TextView company = (TextView) findViewById(R.id.company);
        TextView email = (TextView) findViewById(R.id.email);
        TextView phoneno = (TextView) findViewById(R.id.phoneno);
        TextView website = (TextView) findViewById(R.id.website);
        TextView address = (TextView) findViewById(R.id.address);

        name.setText(users.getName());
        email.setText(users.getEmail());
        username.setText(users.getUsername());
        company.setText(users.getCompany());
        phoneno.setText(users.getPhone());
        website.setText(users.getWebsite());
        address.setText(users.getAddress());
        latlang = new LatLng(users.getLat(), users.getLang());
        MarkerAddress = users.getAddress();

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }


    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        // Add a marker in Sydney and move the camera
        mMap.addMarker(new MarkerOptions().position(latlang).title(MarkerAddress));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(latlang));
        mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        mMap.getUiSettings().setZoomControlsEnabled(true);
        mMap.getUiSettings().setZoomGesturesEnabled(true);
        mMap.getUiSettings().setCompassEnabled(true);
        mMap.getUiSettings().setIndoorLevelPickerEnabled(true);
        mMap.getUiSettings().setMapToolbarEnabled(true);
        mMap.getUiSettings().setMyLocationButtonEnabled(true);
        mMap.getUiSettings().setScrollGesturesEnabled(true);
        mMap.getUiSettings().setTiltGesturesEnabled(true);
        mMap.getUiSettings().setRotateGesturesEnabled(true);

    }
}

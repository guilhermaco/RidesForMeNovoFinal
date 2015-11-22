package com.ridesforme.ridesforme.fragments;

import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.InflateException;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.GoogleApiClient.ConnectionCallbacks;
import com.google.android.gms.common.api.GoogleApiClient.OnConnectionFailedListener;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMap.OnMarkerDragListener;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.ridesforme.ridesforme.HomeActivity;
import com.ridesforme.ridesforme.MainActivity;
import com.ridesforme.ridesforme.PesquisarCaronaActivity;
import com.ridesforme.ridesforme.R;
import com.ridesforme.ridesforme.UserSessionManager;
import com.ridesforme.ridesforme.util.EnderecoCompletoUtil;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;


public class MapHomeFragment extends Fragment implements OnMapReadyCallback, ConnectionCallbacks, OnConnectionFailedListener,OnMarkerDragListener {
    UserSessionManager session;
    private static View view;
    private GoogleMap map;
    private static final String TAG = MainActivity.class.getSimpleName();
    private final static int PLAY_SERVICES_RESOLUTION_REQUEST = 1000;
    private Location mLastLocation;
    private GoogleApiClient mGoogleApiClient;
    private TextView endereco;
    public EnderecoCompletoUtil enderecoUtil;
    private Bundle bundleAll;

    private ContatoFragment.OnFragmentInteractionListener mListener;

    public MapHomeFragment() {

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        if (view != null) {
            ViewGroup parent = (ViewGroup) view.getParent();
            if (parent != null)
                parent.removeView(view);
        }
        try {
            view = inflater.inflate(R.layout.fragment_map_home, container, false);
        } catch (InflateException e) {
        /* map is already there, just return view as it is */
        }

        if (checkPlayServices()) {
            // Building the GoogleApi client
            buildGoogleApiClient();
        }
        enderecoUtil = new EnderecoCompletoUtil();
        bundleAll = new Bundle();

        endereco = (TextView) view.findViewById(R.id.endereco);
        session = new UserSessionManager(getActivity());
        if (session.checkLogin());
        HashMap<String, String> user = session.getUserDetails();
        String name = user.get(UserSessionManager.KEY_NAME);
        /*TextView txtLogin = (TextView)v.findViewById(R.id.lbllogin);
        txtLogin.setText(Html.fromHtml("Name: <b>" + name + "</b>"));*/

        //GOOGLE MAPS
        SupportMapFragment m = ((SupportMapFragment) getChildFragmentManager()
                .findFragmentById(R.id.map));
        m.getMapAsync(this);



        Button btnCarona = (Button)view.findViewById(R.id.btnCarona);
        btnCarona.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getContext(),PesquisarCaronaActivity.class);
                startActivity(i);
            }
        });

        return view;
    }

    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        public void onFragmentInteraction(Uri uri);
    }

    protected synchronized void buildGoogleApiClient() {
        mGoogleApiClient = new GoogleApiClient.Builder(getActivity())
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API).build();
    }

    private boolean checkPlayServices() {
        int resultCode = GooglePlayServicesUtil
                .isGooglePlayServicesAvailable(getActivity());
        if (resultCode != ConnectionResult.SUCCESS) {
            if (GooglePlayServicesUtil.isUserRecoverableError(resultCode)) {
                GooglePlayServicesUtil.getErrorDialog(resultCode, getActivity(),
                        PLAY_SERVICES_RESOLUTION_REQUEST).show();
            } else {
                Toast.makeText(getActivity(),
                        "This device is not supported.", Toast.LENGTH_LONG)
                        .show();
            }
            return false;
        }
        return true;
    }

    @Override
    public void onStart() {
        super.onStart();
        if (mGoogleApiClient != null) {
            mGoogleApiClient.connect();
        }
    }

    @Override
    public void onResume() {
        super.onResume();

        checkPlayServices();
    }

    /**
     * Google api callback methods
     */
    @Override
    public void onConnectionFailed(ConnectionResult result) {
        Log.i(TAG, "Connection failed: ConnectionResult.getErrorCode() = "
                + result.getErrorCode());
    }

    @Override
    public void onConnected(Bundle arg0) {

        map.getUiSettings().setZoomControlsEnabled(true);
        map.getUiSettings().setCompassEnabled(true);
        map.setMyLocationEnabled(true);
        map.setOnMarkerDragListener(this);

        LatLng myLocation;
        HashMap<String, String> location = session.getLastLocation();

        mLastLocation = LocationServices.FusedLocationApi.getLastLocation(
                mGoogleApiClient);

        if (mLastLocation != null && location.get("lat") == null) {
            myLocation = new LatLng(mLastLocation.getLatitude(), mLastLocation.getLongitude());
            map.moveCamera(CameraUpdateFactory.newLatLngZoom(myLocation, 17));
            Marker perth = map.addMarker(new MarkerOptions()
                    .position(myLocation)
                    .draggable(true)
                    .icon(BitmapDescriptorFactory.fromResource(R.drawable.marker)));

            session.createLastLocation(String.valueOf(mLastLocation.getLatitude()), String.valueOf(mLastLocation.getLongitude()));
            Log.i("1", "LOCALIZACAO ENCONTRADA / SHARED PREFERENCES NULL");

            try {
                geocoding(mLastLocation.getLatitude(),mLastLocation.getLongitude());
            } catch (IOException e) {
                e.printStackTrace();
            }

        } else if (mLastLocation != null && location.get("lat") != null) {
            myLocation = new LatLng(mLastLocation.getLatitude(), mLastLocation.getLongitude());
            map.moveCamera(CameraUpdateFactory.newLatLngZoom(myLocation, 17));
            Marker perth = map.addMarker(new MarkerOptions()
                    .position(myLocation)
                    .draggable(true)
                    .icon(BitmapDescriptorFactory.fromResource(R.drawable.marker)));

            session.createLastLocation(String.valueOf(mLastLocation.getLatitude()), String.valueOf(mLastLocation.getLongitude()));
            Log.i("1", "LOCALIZACAO ENCONTRADA / SHARED PREFERENCES PREENCHIDO");

            try {
                geocoding(mLastLocation.getLatitude(),mLastLocation.getLongitude());
            } catch (IOException e) {
                e.printStackTrace();
            }

        } else if (mLastLocation == null && location.get("lat") != null) {
            myLocation = new LatLng(Double.parseDouble(location.get("lat")), Double.parseDouble(location.get("lng")));
            map.moveCamera(CameraUpdateFactory.newLatLngZoom(myLocation, 17));
            Marker perth = map.addMarker(new MarkerOptions()
                    .position(myLocation)
                    .draggable(true)
                    .icon(BitmapDescriptorFactory.fromResource(R.drawable.marker)));

            Log.i("1", "LOCALIZACAO NÃO ENCONTRADA / SHARED PREFERENCES PREENCHIDO");

            try {
                geocoding(Double.parseDouble(location.get("lat")), Double.parseDouble(location.get("lng")));
            } catch (IOException e) {
                e.printStackTrace();
            }

        } else {
            myLocation = new LatLng(-14.2392976, -53.1805017);
            Toast.makeText(getActivity(), "Localização não encontrada", Toast.LENGTH_LONG).show();
            map.moveCamera(CameraUpdateFactory.newLatLngZoom(myLocation, 2));
            Marker perth = map.addMarker(new MarkerOptions()
                    .position(myLocation)
                    .draggable(true)
                    .icon(BitmapDescriptorFactory.fromResource(R.drawable.marker)));

            Log.i("1", "LOCALIZACAO NÃO ENCONTRADA / SHARED PREFERENCES NULL");
        }
    }

    public void geocoding (Double lat, Double lng) throws IOException {
        Geocoder geocoder = new Geocoder(getActivity(), Locale.getDefault());
        List<Address> addresses = geocoder.getFromLocation(lat, lng, 1);
        endereco.setText(addresses.get(0).getThoroughfare().toString() + "," + addresses.get(0).getFeatureName());
        ((HomeActivity) getActivity()).pEndereco = addresses.get(0).getThoroughfare();
        ((HomeActivity) getActivity()).pNumero =  addresses.get(0).getFeatureName();
        ((HomeActivity) getActivity()).pCidade =  addresses.get(0).getLocality();
        ((HomeActivity) getActivity()).pBairro = addresses.get(0).getSubLocality();
        bundleAll.putString("endereco",((HomeActivity) getActivity()).pEndereco);
        bundleAll.putString("cidade",((HomeActivity) getActivity()).pCidade);
        bundleAll.putString("numero", ((HomeActivity) getActivity()).pNumero);
        bundleAll.putString("bairro", ((HomeActivity) getActivity()).pBairro);

    }



    @Override
    public void onConnectionSuspended(int arg0) {
        mGoogleApiClient.connect();
    }


    @Override
    public void onMapReady(GoogleMap map2) {
        this.map = map2;

    }



    @Override
    public void onMarkerDragStart(Marker marker) {
    }

    @Override
    public void onMarkerDrag(Marker marker) {

    }


    @Override
    public void onMarkerDragEnd(Marker marker) {
        try {
            Geocoder geocoder = new Geocoder(getActivity(), Locale.getDefault());
            List<Address> addresses = geocoder.getFromLocation(marker.getPosition().latitude, marker.getPosition().longitude, 1);
            endereco.setText(addresses.get(0).getAddressLine(0).toString());
        } catch (Exception e) {
            try {
                mLastLocation = LocationServices.FusedLocationApi.getLastLocation(
                        mGoogleApiClient);
                LatLng myLocation = new LatLng(mLastLocation.getLatitude(), mLastLocation.getLongitude());
                map.moveCamera(CameraUpdateFactory.newLatLngZoom(myLocation, 17));
                marker.setPosition(myLocation);
                Toast.makeText(getActivity(), "movido", Toast.LENGTH_SHORT).show();
            } catch (Exception ee) {
                LatLng myLocation = new LatLng(-14.2392976, -53.1805017);
                Toast.makeText(getActivity(), "Localização não encontrada", Toast.LENGTH_LONG).show();
                map.moveCamera(CameraUpdateFactory.newLatLngZoom(myLocation, 2));
                marker.setPosition(myLocation);
            }


            e.printStackTrace();
        }
    }
}

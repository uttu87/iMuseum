package com.android.iseasoft.imuseum.view;


import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.android.iseasoft.imuseum.BuildConfig;
import com.android.iseasoft.imuseum.R;
import com.android.iseasoft.imuseum.adapters.InfoWindowListener;
import com.android.iseasoft.imuseum.adapters.MarkerInfoWindowAdapter;
import com.android.iseasoft.imuseum.model.museum.Museum;
import com.android.iseasoft.imuseum.model.museum.MuseumData;
import com.android.iseasoft.imuseum.present.MuseumAPI;
import com.android.iseasoft.imuseum.utils.Define;
import com.android.iseasoft.imuseum.utils.DownloadPolyline;
import com.android.iseasoft.imuseum.utils.LogUtil;
import com.android.iseasoft.imuseum.utils.ParsePolyline;
import com.appolica.interactiveinfowindow.InfoWindow;
import com.appolica.interactiveinfowindow.InfoWindowManager;
import com.appolica.interactiveinfowindow.fragment.MapInfoWindowFragment;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.content.Context.LOCATION_SERVICE;

/**
 * A simple {@link Fragment} subclass.
 */
public class MuseumMapFragment extends Fragment implements GoogleMap.OnMyLocationButtonClickListener,
        GoogleMap.OnMyLocationClickListener, OnMapReadyCallback, InfoWindowManager.WindowShowListener {

    private GoogleMap mMap;
    private MuseumData mMuseumData;
    private Polyline mPolyline;

    private InfoWindow formWindow;
    private InfoWindowManager infoWindowManager;

    private float LOCATION_REFRESH_DISTANCE = 1;
    private long LOCATION_REFRESH_TIME = 100;
    private LatLng mCurrentLatLng;
    private FusedLocationProviderClient mFusedLocationClient;
    private static final int REQUEST_PERMISSIONS_REQUEST_CODE = 34;

    private static MuseumMapFragment mInstance = null;

    public static synchronized MuseumMapFragment getInstance(){
        if(mInstance == null){
            mInstance = new MuseumMapFragment();
        }
        return  mInstance;
    }


    @SuppressLint("ValidFragment")
    private MuseumMapFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_museum_map, container, false);

        //SupportMapFragment mapFragment = (SupportMapFragment) this.getChildFragmentManager()
        //        .findFragmentById(R.id.museummap);
        final MapInfoWindowFragment mapInfoWindowFragment = (MapInfoWindowFragment) this.getChildFragmentManager()
                .findFragmentById(R.id.museummap);
        mapInfoWindowFragment.getMapAsync(this);
        infoWindowManager = mapInfoWindowFragment.infoWindowManager();
        infoWindowManager.setHideOnFling(true);
        infoWindowManager.setWindowShowListener(this);

        if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return view;
        }

        mCurrentLatLng = new LatLng(16.0585469,108.2397508);//set template
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(getActivity());

        // Inflate the layout for this fragment
        return view;
    }

    @Override
    public void onStart() {
        super.onStart();

        if (!checkPermissions()) {
            requestPermissions();
        } else {
            getLastLocation();
        }

    }

    private void getMuseumData(){
        MuseumAPI.getMuseumData(getActivity(), Define.MUSEUM_DATA_QUERY, new Callback<MuseumData>() {
            @Override
            public void onResponse(Call<MuseumData> call, final Response<MuseumData> response) {

                if(getActivity() == null)
                    return;

                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {

                        if(response.isSuccessful()) {
                            LogUtil.d("MainActivity", "Museum Data loaded from API");
                            mMuseumData = response.body();

                            for(Museum museum: mMuseumData.getData().getMuseums()){

                                //Check if Latitude or Longitude is null
                                if(museum.getLocation().getLat() == null || museum.getLocation().getLng() == null){
                                    continue;
                                }
                                LatLng location = new LatLng(museum.getLocation().getLat(), museum.getLocation().getLng());
                                MarkerOptions markerOptions = new MarkerOptions();
                                markerOptions.position(location);
                                markerOptions.icon(BitmapDescriptorFactory.fromResource(R.drawable.housepoint));
                                Marker marker = mMap.addMarker(markerOptions);
                                //marker.setTag(museum);

                                final int offsetX = (int) getResources().getDimension(R.dimen.marker_offset_x);
                                final int offsetY = (int) getResources().getDimension(R.dimen.marker_offset_y);

                                final InfoWindow.MarkerSpecification markerSpec =
                                        new InfoWindow.MarkerSpecification(offsetX, offsetY);
                                FormFragment formFragment  = new FormFragment(museum, new InfoWindowListener() {
                                    @Override
                                    public void onClickListener(Object object) {
                                        final Museum museum1 = (Museum)object;
                                        LatLng latLng = new LatLng(museum1.getLocation().getLat(), museum1.getLocation().getLng());
                                        directionToMuseumPlace(latLng);
                                    }
                                });

                                InfoWindow infoWindow = new InfoWindow(marker, markerSpec, formFragment);
                                marker.setTag(infoWindow);

                                // Setting a custom info window adapter for the google map
                                //MarkerInfoWindowAdapter markerInfoWindowAdapter = new MarkerInfoWindowAdapter(getContext());
                                //mMap.setInfoWindowAdapter(markerInfoWindowAdapter);


                                mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
                                    @Override
                                    public boolean onMarkerClick(Marker marker) {
                                        //if(marker.isInfoWindowShown()){
                                        //    marker.hideInfoWindow();
                                        //} else {
                                        //    marker.showInfoWindow();
                                        //}
                                        InfoWindow form = (InfoWindow)marker.getTag();
                                        infoWindowManager.toggle(form, true);

                                        return false;
                                    }
                                });

                                mMap.setOnInfoWindowClickListener(new GoogleMap.OnInfoWindowClickListener() {
                                    @Override
                                    public void onInfoWindowClick(Marker marker) {
                                        //if(marker.isInfoWindowShown()){
                                        //    marker.hideInfoWindow();
                                        //} else {
                                        //    marker.showInfoWindow();
                                        //}

                                        //Museum museum1 = (Museum) marker.getTag();
                                        //LatLng latLng = new LatLng(museum1.getLocation().getLat(), museum1.getLocation().getLng());
                                        //directionToMuseumPlace(latLng);

                                        InfoWindow form = (InfoWindow)marker.getTag();
                                        infoWindowManager.toggle(form, true);
                                    }
                                });

                            }


                        }else {
                            int statusCode  = response.code();
                            // handle request errors depending on status code

                            LogUtil.d("MainActivity", "Museum Data loaded from API error code: " + statusCode);
                        }

                    }
                });

            }

            @Override
            public void onFailure(Call<MuseumData> call, Throwable t) {
                if(getActivity() == null)
                    return;

                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(getContext(), "Kết nối mạng có vấn đề, vui lòng kiểm tra lại!!!", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });

    }

    private void directionToMuseumPlace(LatLng desLatLng){
        if(mCurrentLatLng == null){
            LogUtil.d("@hai.phamvan", "Can not direction, mCurrentLatLng is null");
            return;
        }

        String url = Define.GOOGLE_MAP_API_DIRECTION_URL;
        url = url.replace("SOURCE_LATITUDE", Double.toString(mCurrentLatLng.latitude));
        url = url.replace("SOURCE_LONGITUDE", Double.toString(mCurrentLatLng.longitude));
        url = url.replace("DES_LATITUDE", Double.toString(desLatLng.latitude));
        url = url.replace("DES_LONGITUDE", Double.toString(desLatLng.longitude));
        url = url.replace("GOOGLE_MAP_API_KEY", Define.GOOGLE_MAP_API_DIRECTION_KEY);

        LogUtil.d("@hai.phamvan", url);

        try {

            DownloadPolyline downloadPolyline = new DownloadPolyline();
            downloadPolyline.execute(url);
            String dataJSON = downloadPolyline.get();

            List<LatLng> latLngs = ParsePolyline.getLatLngListFromJson(dataJSON);

            PolylineOptions polylineOptions = new PolylineOptions();
            for (LatLng value: latLngs){
                LogUtil.d("@hai.phamvan", value.toString());
                polylineOptions.add(value);
            }

            if(mPolyline != null){
                mPolyline.remove();
            }
            mPolyline = mMap.addPolyline(polylineOptions);
            mPolyline.setColor(getActivity().getResources().getColor(R.color.colorAccent));


        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
    }

    @Override
    public boolean onMyLocationButtonClick() {
        return false;
    }

    @Override
    public void onMyLocationClick(@NonNull Location location) {

    }

    @SuppressLint("MissingPermission")
    @Override
    public void onMapReady(GoogleMap googleMap) {

        mMap = googleMap;
        if(checkPermissions()){
            mMap.setMyLocationEnabled(true);
            mMap.setOnMyLocationButtonClickListener(this);
            mMap.setOnMyLocationClickListener(this);
        }


        mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        mMap.setIndoorEnabled(true);

        getMuseumData();
    }

    private final LocationListener mLocationListener = new LocationListener() {
        @Override
        public void onLocationChanged(android.location.Location location) {

            LogUtil.d("MainActivity", "onLocationChanged" + location.toString());
            mCurrentLatLng = new LatLng(location.getLatitude(), location.getLongitude());
            CameraUpdate moveAndZoomCamera = CameraUpdateFactory.newLatLngZoom(mCurrentLatLng, Define.ZOOM_VALUE);
            mMap.animateCamera(moveAndZoomCamera, Define.TIME_TO_MOVE_CAMERA, null);
            MarkerOptions markerOptions = new MarkerOptions();
            markerOptions.position(mCurrentLatLng);
            markerOptions.icon(BitmapDescriptorFactory.fromResource(R.drawable.placeholder));
            mMap.addMarker(markerOptions);
        }

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {

        }

        @Override
        public void onProviderEnabled(String provider) {

        }

        @Override
        public void onProviderDisabled(String provider) {

        }
    };

    @SuppressWarnings("MissingPermission")
    private void getLastLocation() {
        mFusedLocationClient.getLastLocation()
                .addOnCompleteListener(getActivity(), new OnCompleteListener<Location>() {
                    @Override
                    public void onComplete(@NonNull Task<Location> task) {
                        if (task.isSuccessful() && task.getResult() != null) {
                            Location location = task.getResult();

                            LogUtil.d("@hai.phamvan", "getLastLocation" + location.toString());
                            mCurrentLatLng = new LatLng(location.getLatitude(), location.getLongitude());
                            CameraUpdate moveAndZoomCamera = CameraUpdateFactory.newLatLngZoom(mCurrentLatLng, Define.ZOOM_VALUE);
                            mMap.animateCamera(moveAndZoomCamera, Define.TIME_TO_MOVE_CAMERA, null);
                            MarkerOptions markerOptions = new MarkerOptions();
                            markerOptions.position(mCurrentLatLng);
                            markerOptions.icon(BitmapDescriptorFactory.fromResource(R.drawable.placeholder));
                            mMap.addMarker(markerOptions);


                        } else {
                            LogUtil.d("@hai.phamvan", "getLastLocation:exception" + task.getException());
                            showSnackbar(getString(R.string.no_location_detected));
                        }
                    }
                });
    }

    /**
     * Shows a {@link Snackbar} using {@code text}.
     *
     * @param text The Snackbar text.
     */
    private void showSnackbar(final String text) {
        View container = getActivity().findViewById(R.id.activity_container);
        if (container != null) {
            Snackbar.make(container, text, Snackbar.LENGTH_LONG).show();
        }
    }

    /**
     * Shows a {@link Snackbar}.
     *
     * @param mainTextStringId The id for the string resource for the Snackbar text.
     * @param actionStringId   The text of the action item.
     * @param listener         The listener associated with the Snackbar action.
     */
    private void showSnackbar(final int mainTextStringId, final int actionStringId,
                              View.OnClickListener listener) {
        Snackbar.make(getActivity().findViewById(android.R.id.content),
                getString(mainTextStringId),
                Snackbar.LENGTH_INDEFINITE)
                .setAction(getString(actionStringId), listener).show();
    }

    /**
     * Return the current state of the permissions needed.
     */
    private boolean checkPermissions() {
        int permissionState = ActivityCompat.checkSelfPermission(getActivity(),
                Manifest.permission.ACCESS_COARSE_LOCATION);
        return permissionState == PackageManager.PERMISSION_GRANTED;
    }

    private void startLocationPermissionRequest() {
        ActivityCompat.requestPermissions(getActivity(),
                new String[]{Manifest.permission.ACCESS_COARSE_LOCATION},
                REQUEST_PERMISSIONS_REQUEST_CODE);
    }

    private void requestPermissions() {
        boolean shouldProvideRationale =
                ActivityCompat.shouldShowRequestPermissionRationale(getActivity(),
                        Manifest.permission.ACCESS_COARSE_LOCATION);

        // Provide an additional rationale to the user. This would happen if the user denied the
        // request previously, but didn't check the "Don't ask again" checkbox.
        if (shouldProvideRationale) {
            LogUtil.d("@hai.phamvan", "Displaying permission rationale to provide additional context.");

            showSnackbar(R.string.permission_rationale, android.R.string.ok,
                    new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            // Request permission
                            startLocationPermissionRequest();
                        }
                    });

        } else {
            LogUtil.d("@hai.phamvan", "Requesting permission");
            // Request permission. It's possible this can be auto answered if device policy
            // sets the permission in a given state or the user denied the permission
            // previously and checked "Never ask again".
            startLocationPermissionRequest();
        }
    }

    /**
     * Callback received when a permissions request has been completed.
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        LogUtil.d("@hai.phamvan", "onRequestPermissionResult");
        if (requestCode == REQUEST_PERMISSIONS_REQUEST_CODE) {
            if (grantResults.length <= 0) {
                // If user interaction was interrupted, the permission request is cancelled and you
                // receive empty arrays.
                LogUtil.d("@hai.phamvan", "User interaction was cancelled.");
            } else if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permission granted.
                getLastLocation();
            } else {
                // Permission denied.

                // Notify the user via a SnackBar that they have rejected a core permission for the
                // app, which makes the Activity useless. In a real app, core permissions would
                // typically be best requested during a welcome-screen flow.

                // Additionally, it is important to remember that a permission might have been
                // rejected without asking the user for permission (device policy or "Never ask
                // again" prompts). Therefore, a user interface affordance is typically implemented
                // when permissions are denied. Otherwise, your app could appear unresponsive to
                // touches or interactions which have required permissions.
                showSnackbar(R.string.permission_denied_explanation, R.string.settings,
                        new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                // Build intent that displays the App settings screen.
                                Intent intent = new Intent();
                                intent.setAction(
                                        Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                                Uri uri = Uri.fromParts("package",
                                        BuildConfig.APPLICATION_ID, null);
                                intent.setData(uri);
                                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                startActivity(intent);
                            }
                        });
            }
        }
    }

    @Override
    public void onWindowShowStarted(@NonNull InfoWindow infoWindow) {

    }

    @Override
    public void onWindowShown(@NonNull InfoWindow infoWindow) {

    }

    @Override
    public void onWindowHideStarted(@NonNull InfoWindow infoWindow) {

    }

    @Override
    public void onWindowHidden(@NonNull InfoWindow infoWindow) {

    }
}

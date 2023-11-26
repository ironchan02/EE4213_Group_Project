package com.iron.ee4213.Group.Controller;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.DrawableRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.res.ResourcesCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.location.GpsStatus;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.util.Pair;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.SearchView;

import org.json.JSONArray;
import org.json.JSONObject;
import org.osmdroid.api.IMapController;
import org.osmdroid.config.Configuration;
import org.osmdroid.events.MapListener;
import org.osmdroid.events.ScrollEvent;
import org.osmdroid.events.ZoomEvent;
import org.osmdroid.tileprovider.tilesource.TileSourceFactory;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.Marker;
import org.osmdroid.views.overlay.Overlay;
import org.osmdroid.views.overlay.Polyline;
import org.osmdroid.views.overlay.compass.CompassOverlay;
import org.osmdroid.views.overlay.gestures.RotationGestureOverlay;
import org.osmdroid.views.overlay.mylocation.GpsMyLocationProvider;
import org.osmdroid.views.overlay.mylocation.MyLocationNewOverlay;
import static android.Manifest.permission.*;

import com.iron.ee4213.Group.Adapter.BinMarkerEntityAdapter;
import com.iron.ee4213.Group.Entity.BinMarkerEntity;
import com.iron.ee4213.Group.R;

import java.io.BufferedInputStream;
import java.io.InputStream;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

import javax.net.ssl.HttpsURLConnection;


public class MapFragment extends Fragment implements MapListener, GpsStatus.Listener {


    private MapView map;
    private IMapController controller;
    private CompassOverlay compassOverlay;
    private MyLocationNewOverlay myLocationNewOverlay;
    private RotationGestureOverlay rotationGestureOverlay;
    private Polyline route;
    private SearchView searchView;

    private RecyclerView searchResult;

    private List<BinMarkerEntity> binMarkerEntityList;


    public MapFragment(List<BinMarkerEntity> binMarkerEntityList) {
        super(R.layout.fragment_map);
        this.binMarkerEntityList = binMarkerEntityList;
    }


    @SuppressLint("MissingPermission")
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        View view = inflater.inflate(R.layout.fragment_map, container, false);
        ActivityResultLauncher<String[]> locationPermissionRequest =
                registerForActivityResult(new ActivityResultContracts
                                .RequestMultiplePermissions(), result -> {
                            Boolean fineLocationGranted = result.getOrDefault(
                                    ACCESS_FINE_LOCATION, false);
                            Boolean coarseLocationGranted = result.getOrDefault(
                                   ACCESS_COARSE_LOCATION,false);
                            if (fineLocationGranted != null && fineLocationGranted) {
                                // Precise location access granted.
                            } else if (coarseLocationGranted != null && coarseLocationGranted) {
                                // Only approximate location access granted.
                            } else {
                                // No location access granted.
                            }
                        }
                );
        locationPermissionRequest.launch(new String[] {
                ACCESS_FINE_LOCATION,
                ACCESS_COARSE_LOCATION
        });

        Configuration.getInstance().load(
                this.getContext(),
                this.getContext().getSharedPreferences(
                        getString(R.string.app_name),
                        Context.MODE_PRIVATE
                )
        );

        map = view.findViewById(R.id.osmmap);
        map.setTileSource(TileSourceFactory.MAPNIK);
        map.setUseDataConnection(true);
        map.getMapCenter();
        map.setMultiTouchControls(true);
        map.getLocalVisibleRect(new Rect());

        controller = map.getController();

        myLocationNewOverlay = new MyLocationNewOverlay(new GpsMyLocationProvider(this.getContext()), map);
        myLocationNewOverlay.enableMyLocation();
        myLocationNewOverlay.enableFollowLocation();
        myLocationNewOverlay.setDrawAccuracyEnabled( true );

        LocationListener locationListener = location -> {
            if (myLocationNewOverlay.isFollowLocationEnabled())
                myLocationNewOverlay.disableFollowLocation();
        };

        LocationManager locationManager = (LocationManager) requireActivity().getSystemService(Context.LOCATION_SERVICE);

        if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListener);
        }
        myLocationNewOverlay.runOnFirstFix(() -> requireActivity().runOnUiThread(() -> {
            controller.setCenter(myLocationNewOverlay.getMyLocation());
            controller.setCenter(myLocationNewOverlay.getMyLocation());
        }));

        compassOverlay = new CompassOverlay(this.getContext(), map);
        compassOverlay.enableCompass();

        rotationGestureOverlay = new RotationGestureOverlay(this.getContext(), map);
        rotationGestureOverlay.setEnabled(true);
        map.setMultiTouchControls(true);

        controller.setZoom( 18.0 );
        initMarkerList();
        binMarkerEntityList.forEach(entity -> map.getOverlays().add(entity.getMarker()));

        searchView = view.findViewById(R.id.searchBar);
        searchView.setFocusable(true);
        searchView.setFocusableInTouchMode(true);

        map.setOnTouchListener((view1, motionEvent) -> {
            Log.e("Iron","detected");
            searchResult.setVisibility(View.INVISIBLE);
            return false;
        });

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                BinMarkerEntityAdapter binMarkerEntityAdapter = new BinMarkerEntityAdapter(
                        binMarkerEntityList,
                        myLocationNewOverlay.getMyLocation(),
                        binMarkerEntity -> map.getController().animateTo(binMarkerEntity.getMarker().getPosition())
                );
                searchResult.setAdapter(binMarkerEntityAdapter);
                searchResult.setVisibility(View.VISIBLE);
                return true;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                return true;
            }
        });

        searchResult = view.findViewById(R.id.searchResult);
        searchResult.setLayoutManager(new LinearLayoutManager(this.getContext()));

        List<Overlay> list = map.getOverlays();
        list.add(myLocationNewOverlay);
        list.add(compassOverlay);
        list.add(rotationGestureOverlay);
        map.addMapListener(this);
        return view;
    }

    private void initMarkerList() {
        binMarkerEntityList.add(new BinMarkerEntity(markerGenerator(new GeoPoint(22.336581921887568, 114.17251346222527), R.drawable.recycle), "Bin 1", "Container", true, false, true, false, false));
        binMarkerEntityList.add(new BinMarkerEntity(markerGenerator(new GeoPoint(22.33659006927164, 114.17291609498238), R.drawable.paper), "Bin 2", "Container", false, true, false, true, false));
        binMarkerEntityList.add(new BinMarkerEntity(markerGenerator(new GeoPoint(22.33650751165865, 114.1733020729623), R.drawable.recycle), "Bin 3", "Container", false, false, true, true, false));
        binMarkerEntityList.add(new BinMarkerEntity(markerGenerator(new GeoPoint(22.33615526024503, 114.17309218402544), R.drawable.paper), "Bin 4", "Container", true, true, false, true, false));
        binMarkerEntityList.add(new BinMarkerEntity(markerGenerator(new GeoPoint(22.336026250105228, 114.17312437053373), R.drawable.recycle), "Bin 5", "Container", true, true, true, true, true));
        binMarkerEntityList.add(new BinMarkerEntity(markerGenerator(new GeoPoint(22.335789333055335, 114.17371484798878), R.drawable.paper), "Bin 6", "Container", false, false, false, false, true));
    }

    
    public Marker markerGenerator(GeoPoint geoPoint, @DrawableRes int id) {
        Marker marker = new Marker(map);
        marker.setPosition( geoPoint );
        marker.setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_CENTER);
        marker.setTitle("LoL");
        Drawable d = ResourcesCompat.getDrawable(getResources(), id, null);
        Bitmap bm = ((BitmapDrawable) d).getBitmap();
        Drawable dr = new BitmapDrawable(getResources(), Bitmap.createScaledBitmap(bm, (int) (15.0f * getResources().getDisplayMetrics().density), (int) (15.0f * getResources().getDisplayMetrics().density), true));
        marker.setIcon(dr);
        marker.setOnMarkerClickListener((marker1, mapView) -> {
            mapView.getController().animateTo(marker1.getPosition());
            if ( marker1.isInfoWindowShown() ) {
                if (myLocationNewOverlay.getMyLocation() == null) return true;
                getDrawRouteThread(myLocationNewOverlay.getMyLocation(), marker1.getPosition()).start();
            } else {
                marker1.showInfoWindow();
            }
            return true;
        });
        return marker;
    }

    private Thread getDrawRouteThread(GeoPoint start, GeoPoint end) {
        return new Thread(() -> {
            try {
                URL url = new URL(
                        "https://api.openrouteservice.org/v2/directions/foot-walking?api_key=5b3ce3597851110001cf6248f2c638411977411cb6f561bedb6c9213&" +
                                "start=" + start.getLongitude() + "," + start.getLatitude() +
                                "&end=" + end.getLongitude() + "," + end.getLatitude()
                );
                HttpsURLConnection urlConnections = (HttpsURLConnection) url.openConnection();

                if ( urlConnections.getResponseCode() != 200 ) return;
                InputStream is = new BufferedInputStream(urlConnections.getInputStream());
                byte[] byteArr = is.readAllBytes();
                String jsonString = new String(byteArr, StandardCharsets.UTF_8);
                JSONObject json = new JSONObject(jsonString);
                JSONArray points = json.getJSONArray("features")
                        .getJSONObject(0)
                        .getJSONObject("geometry")
                        .getJSONArray("coordinates");
                map.getOverlays().remove(route);
                route = new Polyline(map);
                for ( int i = 0; i < points.length(); ++i ) {
                    JSONArray point = points.getJSONArray(i);
                    route.addPoint(new GeoPoint(point.getDouble(1), point.getDouble(0)));
                }
                map.getOverlays().add(route);
            } catch ( Exception e ) {
                e.printStackTrace();
            }
        });
    }

    @Override
    public void onGpsStatusChanged(int i) {
        //bruh
    }

    @Override
    public boolean onScroll(ScrollEvent event) {
        return true;
    }

    @Override
    public boolean onZoom(ZoomEvent event) {
        return true;
    }

    @Override
    public void onResume() {
        super.onResume();
        if ( map != null )
            map.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        if ( map != null )
            map.onPause();
    }


}
package com.iron.ee4213.Group.Controller;

import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import androidx.viewpager2.widget.ViewPager2;

import com.iron.ee4213.Group.Adapter.BinMarkerEntityAdapter;
import com.iron.ee4213.Group.Entity.BinMarkerEntity;
import com.iron.ee4213.Group.R;

import org.osmdroid.util.GeoPoint;

import java.util.List;

public class MapListFragment extends Fragment {

    private RecyclerView recycler;
    private List<BinMarkerEntity> binMarkerEntityList;

    private final ViewPager2 viewPager2;

    public MapListFragment(List<BinMarkerEntity> binMarkerEntityList, ViewPager2 viewPager2) {
        super(R.layout.fragment_map_list);
        this.binMarkerEntityList = binMarkerEntityList;
        this.viewPager2 = viewPager2;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_map_list, container, false);
        recycler = view.findViewById(R.id.recyclerList);
        recycler.setLayoutManager( new LinearLayoutManager(this.getContext()) );
        LocationManager locationManager = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);

        LocationListener locationListener = (LocationListener) location -> {
            double latitude = location.getLatitude();
            double longitude = location.getLongitude();
            BinMarkerEntityAdapter binMarkerEntityAdapter = new BinMarkerEntityAdapter(binMarkerEntityList, new GeoPoint(latitude, longitude), binMarkerEntity -> {
                MapFragment mapFragment = (MapFragment) ((FragmentStateAdapter)viewPager2.getAdapter()).createFragment(0);
                mapFragment.setNav(binMarkerEntity.getMarker().getPosition());
                viewPager2.setCurrentItem(0, true);

            });
            recycler.setAdapter(binMarkerEntityAdapter);

        };

        if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListener);
            locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, locationListener);
        }
        return view;
    }
}

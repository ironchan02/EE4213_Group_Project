package com.iron.ee4213.Group.Controller;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.widget.ViewPager2;

import com.google.android.material.tabs.TabLayout;
import com.iron.ee4213.Group.Adapter.MapFragmentAdapter;
import com.iron.ee4213.Group.Entity.BinMarkerEntity;
import com.iron.ee4213.Group.R;

import java.util.ArrayList;
import java.util.List;

public class MapContainerFragment extends Fragment {

    private ViewPager2 mapViewPager;
    private TabLayout mapTab;

    private final List<BinMarkerEntity> binMarkerEntityList = new ArrayList<>();

    public MapContainerFragment() {
        super(R.layout.fragment_map_container);
    }

    public ViewPager2 getMapViewPager() {
        return mapViewPager;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_map_container, container, false);
        mapTab = view.findViewById(R.id.mapTab);
        mapViewPager = view.findViewById(R.id.mapViewPager);
        MapFragmentAdapter mapFragmentAdapter = new MapFragmentAdapter(requireActivity().getSupportFragmentManager(), requireActivity().getLifecycle(), binMarkerEntityList, mapViewPager);
        mapViewPager.setAdapter(mapFragmentAdapter);
        mapViewPager.setUserInputEnabled(false);
        mapViewPager.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                mapTab.selectTab(mapTab.getTabAt(position));
            }
        });
        mapViewPager.setOffscreenPageLimit(mapViewPager.getChildCount());

        mapTab.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                mapViewPager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

        mapTab.addTab(mapTab.newTab().setText("Map"));
        mapTab.addTab(mapTab.newTab().setText("List"));

        return view;
    }
}

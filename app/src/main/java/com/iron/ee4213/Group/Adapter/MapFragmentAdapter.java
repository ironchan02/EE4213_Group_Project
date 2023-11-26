package com.iron.ee4213.Group.Adapter;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Lifecycle;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.iron.ee4213.Group.Controller.HomeFragment;
import com.iron.ee4213.Group.Controller.MapFragment;
import com.iron.ee4213.Group.Controller.MapListFragment;
import com.iron.ee4213.Group.Entity.BinMarkerEntity;

import java.util.List;

public class MapFragmentAdapter extends FragmentStateAdapter {

    private List<BinMarkerEntity> binMarkerEntityList;

    public MapFragmentAdapter(@NonNull FragmentManager fragmentManager, @NonNull Lifecycle lifecycle, List<BinMarkerEntity> binMarkerEntityList) {
        super(fragmentManager, lifecycle);
        this.binMarkerEntityList = binMarkerEntityList;
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        Log.e("Iron", "Calling createFragment");
        return switch (position) {
            case 0 -> new MapFragment(binMarkerEntityList);
            case 1 -> new MapListFragment(binMarkerEntityList);
            default -> new MapFragment(binMarkerEntityList);
        };
    }

    @Override
    public int getItemCount() {
        return 2;
    }
}

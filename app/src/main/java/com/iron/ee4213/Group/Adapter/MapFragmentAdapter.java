package com.iron.ee4213.Group.Adapter;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Lifecycle;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import androidx.viewpager2.widget.ViewPager2;

import com.iron.ee4213.Group.Controller.HomeFragment;
import com.iron.ee4213.Group.Controller.MapFragment;
import com.iron.ee4213.Group.Controller.MapListFragment;
import com.iron.ee4213.Group.Entity.BinMarkerEntity;

import java.util.List;

public class MapFragmentAdapter extends FragmentStateAdapter {

    private List<BinMarkerEntity> binMarkerEntityList;

    private ViewPager2 viewPager2;

    private Fragment[] fragments;

    public MapFragmentAdapter(@NonNull FragmentManager fragmentManager, @NonNull Lifecycle lifecycle, List<BinMarkerEntity> binMarkerEntityList, ViewPager2 viewPager2) {
        super(fragmentManager, lifecycle);
        this.binMarkerEntityList = binMarkerEntityList;
        Log.e("Iron", String.valueOf(this.binMarkerEntityList == null));
        this.viewPager2 = viewPager2;
        fragments = new Fragment[2];
        fragments[0] = new MapFragment(binMarkerEntityList);
        fragments[1] = new MapListFragment(binMarkerEntityList, viewPager2);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        return fragments[position];
    }

    @Override
    public int getItemCount() {
        return 2;
    }
}

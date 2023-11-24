package com.iron.ee4213.Group.Adapter;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Lifecycle;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.iron.ee4213.Group.Controller.HomeFragment;
import com.iron.ee4213.Group.Controller.MapFragment;

public class FragmentAdapter extends FragmentStateAdapter {


    public FragmentAdapter(@NonNull FragmentManager fragmentManager, @NonNull Lifecycle lifecycle) {
        super(fragmentManager, lifecycle);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        return switch (position) {
            case 2 -> new MapFragment();
            default -> new HomeFragment();
        };
    }

    @Override
    public int getItemCount() {
        return 5;
    }
}

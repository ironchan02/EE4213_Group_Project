package com.iron.ee4213.Group.Controller;

import android.os.Bundle;
import android.util.TypedValue;
import android.view.Gravity;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.viewpager2.widget.ViewPager2;

import com.google.android.material.tabs.TabLayout;
import com.iron.ee4213.Group.Adapter.FragmentAdapter;
import com.iron.ee4213.Group.R;

import java.util.ArrayList;
import java.util.List;


public class MainActivity extends AppCompatActivity {

    private final List<String> tabTitles = new ArrayList<>() {{
        add("Home");
        add("Recycle");
        add("Map");
        add("Guide");
        add("More");
    }};

    private ViewPager2 viewPager;
    private TabLayout tabLayout;
    private Toolbar toolbar;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        tabLayout = findViewById(R.id.tabBar);
        viewPager = findViewById(R.id.viewPager);
        toolbar = findViewById(R.id.toolbar);

        FragmentAdapter fragmentAdapter = new FragmentAdapter(getSupportFragmentManager(), getLifecycle());
        viewPager.setAdapter(fragmentAdapter);
        viewPager.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                tabLayout.selectTab( tabLayout.getTabAt( position ) );
                viewPager.setUserInputEnabled( position != 2 );
                toolbar.setTitle(tabTitles.get(position));
            }
        });
        viewPager.setUserInputEnabled(false);
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem( tab.getPosition() );
                viewPager.setUserInputEnabled( tab.getPosition() != 2 );
                toolbar.setTitle(tabTitles.get(tab.getPosition()));
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
        initTabLayoutItem();
    }

    private void initTabLayoutItem() {
        tabTitles.forEach(title -> {
            TabLayout.Tab tab = tabLayout.newTab();
            TextView textView = new TextView(this);
            textView.setText(title);
            textView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 10);
            textView.setGravity(Gravity.CENTER);
            tab.setCustomView(textView);
            tabLayout.addTab(tab);
        });
    }

}

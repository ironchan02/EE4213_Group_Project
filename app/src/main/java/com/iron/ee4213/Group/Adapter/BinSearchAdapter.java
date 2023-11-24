package com.iron.ee4213.Group.Adapter;

import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.iron.ee4213.Group.Entity.BinMarkerEntity;
import com.iron.ee4213.Group.R;

import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.overlay.Marker;
import org.osmdroid.views.overlay.mylocation.MyLocationNewOverlay;
import org.w3c.dom.Text;

import java.util.List;

public class BinSearchAdapter extends RecyclerView.Adapter<BinSearchAdapter.ViewHolder> {
    private final List<BinMarkerEntity> binMarkerEntityList;
    private final MyLocationNewOverlay myLocationNewOverlay;
    public BinSearchAdapter(List<BinMarkerEntity> binMarkerEntityList, MyLocationNewOverlay myLocationNewOverlay) {
        this.binMarkerEntityList = binMarkerEntityList;
        this.myLocationNewOverlay = myLocationNewOverlay;
    }


    @NonNull
    @Override
    public BinSearchAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_search_result, parent, false);
        return new ViewHolder(view, myLocationNewOverlay.getMyLocation());
    }

    @Override
    public void onBindViewHolder(@NonNull BinSearchAdapter.ViewHolder holder, int position) {
        BinMarkerEntity binMarkerEntity = binMarkerEntityList.get(position);
        holder.bind(binMarkerEntity);
    }

    @Override
    public int getItemCount() {
        return binMarkerEntityList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private TextView binName;
        private TextView binDescription;
        private TextView binDistance;
        private GeoPoint currentGeo;

        public ViewHolder(@NonNull View view, GeoPoint currentGeo) {
            super(view);
            binName = view.findViewById(R.id.textLocationName);
            binDescription = view.findViewById(R.id.textLocationString);
            binDistance = view.findViewById(R.id.textDistance);
            this.currentGeo = currentGeo;
        }

        public void bind(BinMarkerEntity binMarkerEntity) {
            Marker marker = binMarkerEntity.getMarker();
            binName.setText(marker.getTitle());
            binDescription.setText(marker.getTitle());
            //double distance = currentGeo.distanceToAsDouble( marker.getPosition() );
            //binDistance.setText(String.valueOf(  Math.round( distance ) ));
            binDistance.setText("0m");
        }
    }
}

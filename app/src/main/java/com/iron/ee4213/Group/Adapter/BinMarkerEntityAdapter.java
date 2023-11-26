package com.iron.ee4213.Group.Adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.iron.ee4213.Group.Entity.BinMarkerEntity;
import com.iron.ee4213.Group.R;

import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.Marker;
import org.osmdroid.views.overlay.mylocation.MyLocationNewOverlay;

import java.util.List;

public class BinMarkerEntityAdapter extends RecyclerView.Adapter<BinMarkerEntityAdapter.ViewHolder> {
    private final List<BinMarkerEntity> binMarkerEntityList;
    private final GeoPoint currentLocation;
    private final OnItemClickListener clickListener;

    public BinMarkerEntityAdapter(List<BinMarkerEntity> binMarkerEntityList, GeoPoint currentLocation, OnItemClickListener clickListener) {
        this.binMarkerEntityList = binMarkerEntityList;
        this.currentLocation = currentLocation;
        this.clickListener = clickListener;
    }


    @NonNull
    @Override
    public BinMarkerEntityAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_search_result, parent, false);
        return new ViewHolder(view, currentLocation);
    }

    @Override
    public void onBindViewHolder(@NonNull BinMarkerEntityAdapter.ViewHolder holder, int position) {
        BinMarkerEntity binMarkerEntity = binMarkerEntityList.get(position);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                clickListener.onClick(binMarkerEntity);
            }
        });

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
        private ImageView paper, plastic, metal, largeLitter, litter;

        public ViewHolder(@NonNull View view, GeoPoint currentGeo) {
            super(view);
            binName = view.findViewById(R.id.textLocationName);
            binDescription = view.findViewById(R.id.textLocationString);
            binDistance = view.findViewById(R.id.textDistance);
            paper = view.findViewById(R.id.paperIcon);
            plastic = view.findViewById(R.id.plasticIcon);
            metal = view.findViewById(R.id.metalIcon);
            largeLitter = view.findViewById(R.id.largeLitterIcon);
            litter = view.findViewById(R.id.litterIcon);
            this.currentGeo = currentGeo;
        }

        public void bind(BinMarkerEntity binMarkerEntity) {
            Marker marker = binMarkerEntity.getMarker();
            binName.setText(binMarkerEntity.getBinName());
            binDescription.setText(binMarkerEntity.getBinDescription());
            double distance = currentGeo.distanceToAsDouble( marker.getPosition() );
            long m = Math.round( distance );
            binDistance.setText( m > 500 ? (m/1000) + "km" : m + "km" );
            paper.setVisibility( binMarkerEntity.isAcceptPaper() ? View.VISIBLE : View.GONE );
            plastic.setVisibility( binMarkerEntity.isAcceptPlastic() ? View.VISIBLE : View.GONE );
            metal.setVisibility( binMarkerEntity.isAcceptMetal() ? View.VISIBLE : View.GONE );
            largeLitter.setVisibility( binMarkerEntity.isAcceptLarge() ? View.VISIBLE : View.GONE );
            litter.setVisibility( binMarkerEntity.isAcceptLitter() ? View.VISIBLE : View.GONE );
        }
    }

    public interface OnItemClickListener {
        void onClick(BinMarkerEntity binMarkerEntity);
    }
}

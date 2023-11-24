package com.iron.ee4213.Group.Entity;

import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.overlay.Marker;

public class BinMarkerEntity {

    private Marker marker;
    private boolean isAcceptPaper;
    private boolean isAcceptPlastic;
    private boolean isAcceptMetal;
    private boolean isAcceptLitter;
    private boolean isAcceptLarge;

    public BinMarkerEntity(Marker marker, boolean isAcceptPaper, boolean isAcceptPlastic, boolean isAcceptMetal, boolean isAcceptLitter, boolean isAcceptLarge) {
        this.marker = marker;
        this.isAcceptPaper = isAcceptPaper;
        this.isAcceptPlastic = isAcceptPlastic;
        this.isAcceptMetal = isAcceptMetal;
        this.isAcceptLitter = isAcceptLitter;
        this.isAcceptLarge = isAcceptLarge;
    }

    public BinMarkerEntity(Marker marker) {
        this(marker, false, false, false, false, false);
    }

    public Marker getMarker() {
        return marker;
    }

    public void setMarker(Marker marker) {
        this.marker = marker;
    }

    public boolean isAcceptPaper() {
        return isAcceptPaper;
    }

    public void setAcceptPaper(boolean acceptPaper) {
        isAcceptPaper = acceptPaper;
    }

    public boolean isAcceptPlastic() {
        return isAcceptPlastic;
    }

    public void setAcceptPlastic(boolean acceptPlastic) {
        isAcceptPlastic = acceptPlastic;
    }

    public boolean isAcceptMetal() {
        return isAcceptMetal;
    }

    public void setAcceptMetal(boolean acceptMetal) {
        isAcceptMetal = acceptMetal;
    }

    public boolean isAcceptLitter() {
        return isAcceptLitter;
    }

    public void setAcceptLitter(boolean acceptLitter) {
        isAcceptLitter = acceptLitter;
    }

    public boolean isAcceptLarge() {
        return isAcceptLarge;
    }

    public void setAcceptLarge(boolean acceptLarge) {
        isAcceptLarge = acceptLarge;
    }
}

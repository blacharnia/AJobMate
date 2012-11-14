package rafpio.ajobmate.activities;

import java.util.List;

import rafpio.ajobmate.R;
import rafpio.ajobmate.core.LocationItemizedOverlay;
import android.os.Bundle;

import com.google.android.maps.GeoPoint;
import com.google.android.maps.MapActivity;
import com.google.android.maps.MapController;
import com.google.android.maps.MapView;
import com.google.android.maps.Overlay;
import com.google.android.maps.OverlayItem;

public class LocationMapActivity extends MapActivity {

    MapView mapView;
    MapController mc;
    GeoPoint point;
    double latitude;
    double longitude;
    String title;
    String message;

    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        init();

        setContentView(R.layout.location_mapview);

        mapView = (MapView) findViewById(R.id.mapView);
        mapView.setBuiltInZoomControls(true);

        mc = mapView.getController();

        point = new GeoPoint((int) (latitude * 1E6), (int) (longitude * 1E6));
        mc.setZoom(20);
        mc.setCenter(point);
        mc.animateTo(point);

        List<Overlay> mapOverlays = mapView.getOverlays();
        LocationItemizedOverlay itemizedoverlay = new LocationItemizedOverlay(
                getResources().getDrawable(R.drawable.androidmarker), this);

        OverlayItem overlayitem = new OverlayItem(point, title, message);
        itemizedoverlay.addOverlay(overlayitem);
        mapOverlays.add(itemizedoverlay);
    }

    @Override
    protected boolean isRouteDisplayed() {
        return false;
    }

    private void init() {
        Bundle extras = getIntent().getExtras();
        if (null != extras) {
            latitude = extras.getDouble(getString(R.string.latitude));
            longitude = extras.getDouble(getString(R.string.longitude));
            if (0L == latitude || 0 == longitude) {
                finish();
            }
            title = extras.getString("title");
            message = extras.getString("message");
        } else {
            finish();
        }
    }
}

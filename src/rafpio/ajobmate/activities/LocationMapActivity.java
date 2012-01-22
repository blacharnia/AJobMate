package rafpio.ajobmate.activities;

import rafpio.ajobmate.R;
import android.os.Bundle;

import com.google.android.maps.GeoPoint;
import com.google.android.maps.MapActivity;
import com.google.android.maps.MapController;
import com.google.android.maps.MapView;

public class LocationMapActivity extends MapActivity {

    MapView mapView;
    MapController mc;
    GeoPoint p;

    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.location_mapview);

        mapView = (MapView) findViewById(R.id.mapView);

        mc = mapView.getController();
        String coordinates[] = { "1.352566007", "103.78921587" };
        double lat = Double.parseDouble(coordinates[0]);
        double lng = Double.parseDouble(coordinates[1]);

        p = new GeoPoint((int) (lat * 1E6), (int) (lng * 1E6));

        mc.animateTo(p);
        mapView.invalidate();
    }

    @Override
    protected boolean isRouteDisplayed() {
        // TODO Auto-generated method stub
        return false;
    }

}

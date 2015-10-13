package com.test.manhnn.bandodulich;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.net.ParseException;
import android.os.AsyncTask;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private static String url_cate="http://where.beliat.com/service/category";
    private static String url_place="http://where.beliat.com/service/place";
    private static final String TAG_DATA = "data";
    private static final String TAG_ID = "id";
    private static final String TAG_IMAGE_ID = "image_id";
    private static final String TAG_NAME = "name";
    private static final String TAG_DATA_PLACE = "data";
    private static final String TAG_ID_PLACE = "id";
    private static final String TAG_NAME_PLACE = "name";
    private static final String TAG_ADD = "address";
    private static final String TAG_LAT = "lat";
    private static final String TAG_LONG = "long";
    private static final String TAG_SHORT_DES = "short_description";
    private static final String TAG_DES = "description";
    private static final String TAG_CATE_ID = "category_id";
    private static final String TAG_IMAGE_ID_PLACE = "image_id";
    JSONArray data = null;
    JSONArray data_place = null;
    TextView tv;
    Data dt;
    Category category;
    LatLng maker;
    ArrayList<Data> list= null;
    ArrayList<Category> list1= null;
    Context context;
    SupportMapFragment mapFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.context =context;
        new JSONAsyncTask().execute(url_place);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        // Getting LocationManager object from System Service LOCATION_SERVICE


    }


    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {
                marker.showInfoWindow();
                return true;
            }
        });


    }
    class JSONAsyncTask extends AsyncTask<String, Void, Boolean> {

        ProgressDialog dialog;


        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            dialog = new ProgressDialog(MapsActivity.this);
            dialog.setMessage("Loading, please wait");
            dialog.setTitle("Connecting server");
            dialog.show();
            dialog.setCancelable(false);
        }

        @Override
        protected Boolean doInBackground(String... urls) {
            list = new ArrayList<Data>();
            list1 = new ArrayList<Category>();
            try {


                HttpGet httppost = new HttpGet(urls[0]);
                HttpClient httpclient = new DefaultHttpClient();
                HttpResponse response = httpclient.execute(httppost);

                // StatusLine stat = response.getStatusLine();
                int status = response.getStatusLine().getStatusCode();

                if (status == 200) {
                    HttpEntity entity = response.getEntity();
                    String data = EntityUtils.toString(entity);


                    JSONObject jsono = new JSONObject(data);
                    JSONArray jarray = jsono.getJSONArray(TAG_DATA_PLACE);

                    for (int i = 0; i < jarray.length(); i++) {
                        JSONObject object = jarray.getJSONObject(i);

                        dt = new Data();
                        dt.setId(object.getInt(TAG_ID_PLACE));
                        dt.setLat(object.getDouble(TAG_LAT));
                        dt.setLongtat(object.getDouble(TAG_LONG));
                        dt.setName(object.getString(TAG_NAME_PLACE));
                        list.add(dt);

                    }
                    JSONArray jarraycate = jsono.getJSONArray(TAG_DATA);

                    for (int i = 0; i < jarraycate.length(); i++) {
                        JSONObject object = jarraycate.getJSONObject(i);

                        category = new Category();

                        category.setId(object.getInt(TAG_CATE_ID));
                        category.setName(object.getString(TAG_NAME));
                        category.setImage_id(object.getInt(TAG_IMAGE_ID));
                        list1.add(category);

                    }
                    return true;
                }
            } catch (ParseException e1) {
                e1.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return false;
        }

        protected void onPostExecute(Boolean result) {
            dialog.cancel();

            for (int i=0;i<list.size();i++) {

                LatLng sydney = new LatLng(list.get(i).getLat(),list.get(i).getLongtat());

                    mMap.addMarker(new MarkerOptions().position(sydney));
                   mMap.setInfoWindowAdapter(new MarkerInfoWindowAdapter());


            }
            if(result == false)
                Toast.makeText(getApplicationContext(), "Unable to fetch data from server", Toast.LENGTH_LONG).show();

        }
        public class MarkerInfoWindowAdapter implements GoogleMap.InfoWindowAdapter {
            public MarkerInfoWindowAdapter() {
            }

            @Override
            public View getInfoWindow(Marker marker) {
                return null;
            }

            @Override
            public View getInfoContents(Marker marker) {
                View v = getLayoutInflater().inflate(R.layout.infowindow_layout, null);


                ImageView markerIcon = (ImageView) v.findViewById(R.id.marker_icon);

                TextView markerLabel = (TextView) v.findViewById(R.id.marker_label);

                TextView anotherLabel = (TextView) v.findViewById(R.id.another_label);

                //markerIcon.setImageResource(manageMarkerIcon(myMarker.getmIcon()));

                    anotherLabel.setText(dt.getName());

                //markerLabel.setText(myMarker.getmLabel());

                return v;
            }
        }
    }
}


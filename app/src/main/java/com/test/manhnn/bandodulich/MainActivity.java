package com.test.manhnn.bandodulich;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;

import android.net.ParseException;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.model.LatLng;

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

public class MainActivity extends AppCompatActivity {
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
    ArrayList<Data> list;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        list = new ArrayList<Data>();
        new JSONAsyncTask().execute("http://where.beliat.com/service/place");
        Log.i("vltn",""+list.size());

    }


    class JSONAsyncTask extends AsyncTask<String, Void, Boolean> {

        ProgressDialog dialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            dialog = new ProgressDialog(MainActivity.this);
            dialog.setMessage("Loading, please wait");
            dialog.setTitle("Connecting server");
            dialog.show();
            dialog.setCancelable(false);
        }

        @Override
        protected Boolean doInBackground(String... urls) {
            try {

                //------------------>>
                HttpGet httppost = new HttpGet(urls[0]);
                HttpClient httpclient = new DefaultHttpClient();
                HttpResponse response = httpclient.execute(httppost);

                // StatusLine stat = response.getStatusLine();
                int status = response.getStatusLine().getStatusCode();

                if (status == 200) {
                    HttpEntity entity = response.getEntity();
                    String data = EntityUtils.toString(entity);


                    JSONObject jsono = new JSONObject(data);
                    JSONArray jarray = jsono.getJSONArray("data");

                    for (int i = 0; i < jarray.length(); i++) {
                        JSONObject object = jarray.getJSONObject(i);

                        Data data1 = new Data();


                        //Log.i("vl",""+list.get(i).getLongtat().toString());
                    }
                    return true;
                }

                //------------------>>

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

            for(int i=0;i<=list.size();i++){
                tv = (TextView) findViewById(R.id.tv);
            //tv.setText(list.get(6).getLat());
            Log.i("vt", "" + list.size());}
            if(result == false)
                Toast.makeText(getApplicationContext(), "Unable to fetch data from server", Toast.LENGTH_LONG).show();

        }
    }






}

package com.example.moneyjeju.MAP;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import com.example.moneyjeju.JejuApp;
import com.example.moneyjeju.R;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.util.ArrayList;

public class ScheduleTotalMap extends Fragment {

    final static private String URL = "http://192.168.0.8/locationget.php";
    private static final String TAG_JSON="webnautes";
    private static final String TAG_LATITUDE = "latitude";
    private static final String TAG_LONGITUDE = "longitude";
    GoogleMap map;
    String userId,planNo,selectDate,JsonString;
    ArrayList<Location> list=new ArrayList<>();
    JejuApp jejuApp;



    private OnMapReadyCallback callback = new OnMapReadyCallback() {

        /**
         * Manipulates the map once available.
         * This callback is triggered when the map is ready to be used.
         * This is where we can add markers or lines, add listeners or move the camera.
         * In this case, we just add a marker near Sydney, Australia.
         * If Google Play services is not installed on the device, the user will be prompted to
         * install it inside the SupportMapFragment. This method will only be triggered once the
         * user has installed Google Play services and returned to the app.
         */
        @Override
        public void onMapReady(GoogleMap googleMap) {

            jejuApp=(JejuApp) getActivity().getApplicationContext();
            userId=jejuApp.userId;

            planNo=jejuApp.planNo;

            map=googleMap;

           jejuApp.map=map;




            LatLng Jeju = new LatLng(33.3809145, 126.53578739783740);



            map.moveCamera(CameraUpdateFactory.newLatLngZoom(Jeju,10));

        }
    };

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {


        View view=inflater.inflate(R.layout.fragment_schedule_total_map, container, false);


        return view;
    }






    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        SupportMapFragment mapFragment =
                (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.Totalmap);



        if (mapFragment != null) {

            mapFragment.getMapAsync(callback);
        }
    }

    public void SelectDate(String selectDate){
        this.selectDate=selectDate;
        userId=jejuApp.userId;
        planNo=jejuApp.planNo;


        if(selectDate!=null){
            LocationGet locationGet=new LocationGet();
            locationGet.execute(URL,userId,planNo,selectDate);
        }
    }



    private class LocationGet extends AsyncTask<String,Void,String>{

        @Override
        protected String doInBackground(String... strings) {
            String userId=strings[1];
            String planNo=strings[2];
            String selectDate=strings[3];


            String URL=strings[0];
            String errorString=null;

            String postParameters = "userId=" + userId + "&planNo=" + planNo + "&selectDate=" + selectDate;

            try{
                java.net.URL url=new java.net.URL(URL);
                HttpURLConnection httpURLConnection=(HttpURLConnection)url.openConnection();

                httpURLConnection.setReadTimeout(5000);
                httpURLConnection.setConnectTimeout(5000);
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.setDoInput(true);
                httpURLConnection.connect();

                OutputStream outputStream=httpURLConnection.getOutputStream();
                outputStream.write(postParameters.getBytes("UTF-8"));
                outputStream.flush();
                outputStream.close();

                InputStream inputStream;

                int responseStatusCode = httpURLConnection.getResponseCode();
                Log.d("LocationGet", "response code - " + responseStatusCode);

                if(responseStatusCode == HttpURLConnection.HTTP_OK) {
                    inputStream = httpURLConnection.getInputStream();
                }
                else{
                    inputStream = httpURLConnection.getErrorStream();
                }

                InputStreamReader inputStreamReader = new InputStreamReader(inputStream, "UTF-8");
                BufferedReader bufferedReader = new BufferedReader(inputStreamReader);

                StringBuilder sb = new StringBuilder();
                String line;

                while((line = bufferedReader.readLine()) != null){
                    sb.append(line);
                }


                bufferedReader.close();



                return sb.toString().trim();

            }
            catch (Exception e){

                Log.d("LocationGet", "InsertData: Error ", e);
                errorString = e.toString();
                return null;}

        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);

            Log.d("LocationGet", "response - " + s);

            if (s == null){

            }
            else {
                JsonString=s;
                showResult();
            }
        }
    }

    private void showResult() {
        try {
            JSONObject jsonObject = new JSONObject(JsonString);
            JSONArray jsonArray = jsonObject.getJSONArray(TAG_JSON);

            for (int i = 0; i < jsonArray.length(); i++) {

                JSONObject item = jsonArray.getJSONObject(i);

                String latitude = item.getString(TAG_LATITUDE);
                String longitude = item.getString(TAG_LONGITUDE);

                Location location = new Location();

                location.setLatitude(latitude);
                location.setLongitude(longitude);

                list.add(location);



            }

        } catch (JSONException e) {

            Log.d("LocationGet", "showResult : ", e);
        }



        jejuApp.map.clear();

        int count=list.size();




        PolylineOptions polylineOptions=new PolylineOptions();
        polylineOptions.clickable(true);


        for(int i=0;i<count;i++){
            LatLng latLng=new LatLng(Double.parseDouble(list.get(i).getLatitude()),Double.parseDouble(list.get(i).getLongitude()));
            Marker marker=jejuApp.map.addMarker(new MarkerOptions().position(latLng)
                                                                    .title(""+(i+1)));
            polylineOptions.add(latLng);
        }



       Polyline polyline=jejuApp.map.addPolyline(polylineOptions);
        polyline.setColor(Color.RED);



    }

}
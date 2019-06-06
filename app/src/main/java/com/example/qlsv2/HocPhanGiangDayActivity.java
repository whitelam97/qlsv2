package com.example.qlsv2;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Spinner;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.qlsv2.Adapter.HocPhanAdapter;
import com.example.qlsv2.Class.hocphan;
import com.example.qlsv2.Class.url;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;

public class HocPhanGiangDayActivity extends AppCompatActivity {
    Spinner spnhocky;
    com.example.qlsv2.Class.url url= new url();
    String URLhocky=url.getUrl()+"diemdanh/select_hocky.php";
    ArrayList<String> hockyArrayList;

    ArrayList<hocphan> hocphanArrayList;
    ListView listView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hoc_phan_giang_day);

        listView = findViewById(R.id.lvlophoc);
        hocphanArrayList = new ArrayList<hocphan>();

        //spnhocky
        hockyArrayList=new ArrayList<>();
        spnhocky=(Spinner)findViewById(R.id.spnhocky1);
        loadSpinnerData(URLhocky);

        spnhocky.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                hocphanArrayList = new ArrayList<hocphan>();
                selechocphan();
            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                // DO Nothing here
            }
        });

        //nut back
        if(getSupportActionBar()!=null){
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }
    }

    public void selechocphan() {
        String country=   spnhocky.getItemAtPosition(spnhocky.getSelectedItemPosition()).toString();
        String[] output = country.split("[\\, ]");
        String hk =output[2];
        String namhoc=output[6];
        SharedPreferences shared= getSharedPreferences("canbo", Context.MODE_PRIVATE);
        String idCB = shared.getString("idCB", "");
        final String  URLhp= url.getUrl()+"diemdanh/se_hocphangiangday.php?idCB="+idCB+"&hocky="+hk+"&namhoc="+namhoc+"";
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                new docJson().execute(URLhp);
            }
        });
    }
    //spnhocky
    private void loadSpinnerData(String url) {
        RequestQueue requestQueue= Volley.newRequestQueue(getApplicationContext());
        StringRequest stringRequest=new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try{
                        JSONArray jsonArray=new JSONArray(response);
                        for(int i=0;i<jsonArray.length();i++){
                            JSONObject jsonObject1=jsonArray.getJSONObject(i);
                            String hocky1=jsonObject1.getString("hocky");
                            String namhoc=jsonObject1.getString("namhoc");
                            hockyArrayList.add("Học kỳ "+hocky1+", năm học "+namhoc);
                        }
                    spnhocky.setAdapter(new ArrayAdapter<String>(HocPhanGiangDayActivity.this, android.R.layout.simple_spinner_dropdown_item, hockyArrayList));
                }catch (JSONException e){e.printStackTrace();}
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
            }
        });
        int socketTimeout = 30000;
        RetryPolicy policy = new DefaultRetryPolicy(socketTimeout, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        stringRequest.setRetryPolicy(policy);
        requestQueue.add(stringRequest);
    }
    //nut back
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id=item.getItemId();
        if (id==android.R.id.home){
            this.finish();
        }
        return super.onOptionsItemSelected(item);
    }
    //select học phần
    class docJson extends AsyncTask<String,Integer,String> {

        //doinbackgroufd dung doc du lieu tren mang
        @Override
        protected String doInBackground(String... strings) {
            return docnoidungtuURL(strings[0]);
        }

        @Override
        protected void onPostExecute(String s) {
            try {
                JSONArray mangjson = new JSONArray(s);
                for(int i=0;i<mangjson.length();i++){
                    JSONObject sp= mangjson.getJSONObject(i);
                    hocphanArrayList.add(new hocphan(
                            sp.getString("mslopHP"),
                            sp.getString("tenlopHP"),
                            sp.getString("loailopHP"),
                            sp.getString("soSV"),
                            sp.getString("tuanhoc"),
                            sp.getString("bacDT"),
                            sp.getString("hocky"),
                            sp.getString("namhoc"),
                            sp.getString("tenHP"),
                            sp.getString("msHP"),
                            sp.getString("soTC"),
                            sp.getString("msCB"),
                            sp.getString("hotenCB")
                            ));
                }
//                Toast.makeText(getApplicationContext(),""+sanPhamArrayList.size(), Toast.LENGTH_LONG).show();
                HocPhanAdapter listadapter=new HocPhanAdapter(getApplicationContext(),
                        R.layout.row_hocphan, hocphanArrayList);
                listView.setAdapter(listadapter);

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }
    private static String docnoidungtuURL(String theUrl) {
        StringBuilder stringBuilder = new StringBuilder();
        try {
            //tạo một biến url
            URL url = new URL(theUrl);
            //tạo một biến ket noi ủrl
            URLConnection urlConnection = url.openConnection();

            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
            String line;
            //Doc urlconnet tư buttfetreader
            while ((line=bufferedReader.readLine())!=null){
                stringBuilder.append(line+"\n");
            }
            bufferedReader.close();
        }catch (Exception e){
            e.printStackTrace();
        }
        return stringBuilder.toString();

    }



}

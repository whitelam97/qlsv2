package com.example.qlsv2.Activity;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import com.example.qlsv2.Adapter.tkb_tuan_Adapter;
import com.example.qlsv2.Class.tkb_tuan;
import com.example.qlsv2.Class.url;
import com.example.qlsv2.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;

public class LichBieuActivity extends AppCompatActivity {
    Spinner spnhocky1,spntuan;
    com.example.qlsv2.Class.url url= new url();
    String URLhocky=url.getUrl()+"diemdanh/HocKy.php";
    String urltuan;

    ArrayList<String> hockyArrayList;
    ArrayList<String> tuanArrayList;
    TextView txttuan,txthknh;

    ListView listView;
    ArrayList<tkb_tuan> tkb_tuanArrayList;

    Button tim;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lich_bieu);

        listView = findViewById(R.id.lvlophoc);
        spntuan =findViewById(R.id.spntuan);
        spnhocky1=findViewById(R.id.spnhocky1);
        txthknh=findViewById(R.id.txthknh);
        txttuan=findViewById(R.id.txttuan);
        //lấy hk, nh hien tai
        SharedPreferences shared= getSharedPreferences("hocky", Context.MODE_PRIVATE);
        final String hk = shared.getString("hocky", "");
        final String nh = shared.getString("namhoc", "");
        final String tgbd = shared.getString("thoigianBD", "");
        final String idhk = shared.getString("idHK", "");
        txthknh.setText("học kỳ "+hk+", năm học "+nh);
        //lấy tuần hiện tại
        SharedPreferences shared1= getSharedPreferences("tuanht", Context.MODE_PRIVATE);
        final String tuan = shared1.getString("tuanht", "");
        final String tuanstt = shared1.getString("sttTuan", "");

        SharedPreferences shared4= getSharedPreferences("mondaytosundaynow", Context.MODE_PRIVATE);
        final String monday = shared4.getString("monday", "");
        final String sunday = shared4.getString("sunday", "");
        final String dayht = shared4.getString("ngayht", "");
        txttuan.setText("Tuần "+tuan+" (từ "+monday+" đến "+sunday+" )");

        //lấy idCB
        SharedPreferences shared3= getSharedPreferences("canbo", Context.MODE_PRIVATE);
        final String idcb = shared3.getString("idCB", "");


        //spnhocky
        hockyArrayList=new ArrayList<>();
        loadSpinnerHk(URLhocky);
        spnhocky1.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                String s= hockyArrayList.get(i);
                String[] outpu = s.split("[\\, ]");
                String hki =outpu[2];
                String namhoc=outpu[6];
                tuanArrayList= new ArrayList<>();
                urltuan =url.getUrl()+"diemdanh/TuanBDTuanKT.php?hk="+hki+"&nh="+namhoc+"";
                loadSpinnertuan(urltuan);
                spntuan.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                        tkb_tuanArrayList = new ArrayList<tkb_tuan>();
                        tkb_tuan_Adapter tuanAdapter= new tkb_tuan_Adapter(
                                getApplicationContext(),
                                R.layout.row_tkb_tuan,
                                tkb_tuanArrayList);
                        listView.setAdapter(tuanAdapter);
                        String coun=   spnhocky1.getItemAtPosition(spnhocky1.getSelectedItemPosition()).toString();
                        String[] outpu = coun.split("[\\, ]");
                        String hk =outpu[2];
                        String namhoc=outpu[6];
                        SharedPreferences shared= getSharedPreferences("canbo", Context.MODE_PRIVATE);
                        String idCB = shared.getString("idCB", "");
                        String tuan=  spntuan.getSelectedItem().toString();
                        String[] sotuan = tuan.split(" ");
                        SharedPreferences shared6= getSharedPreferences("tuandbkt", Context.MODE_PRIVATE);
                        final String tuabd = shared6.getString("tuanBD", "");
                        int bd= Integer.parseInt(tuabd);
                        int st =Integer.parseInt(sotuan[1])-bd+1;
//                      Toast.makeText(LichBieuActivity.this, st+"", Toast.LENGTH_SHORT).show();
                        final String  URLtkbtuan1= url.getUrl()+"diemdanh/LichBieuTuan.php?idCB="+idCB+"&sttTuan="+st+"&hocky="+hk+"&namhoc="+namhoc+"";

                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                new docJson().execute(URLtkbtuan1);
                            }
                        });

                    }
                    @Override
                    public void onNothingSelected(AdapterView<?> adapterView) {
                        // DO Nothing here
                        // Đổ dữ liệu ra lisview lich bieu hien tai
                        tkb_tuanArrayList = new ArrayList<tkb_tuan>();
                        final String  URLtkbtuan= url.getUrl()+"diemdanh/LichBieuTuanHienTai.php?idCB="+idcb+"&sttTuan="+tuanstt+"&idHK="+idhk+"";
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                new docJson().execute(URLtkbtuan);
                            }
                        });
                    }
                });
            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                // DO Nothing here
                String hk ="7";
                String namhoc="2018-2019";
                tuanArrayList= new ArrayList<>();
                urltuan =url.getUrl()+"diemdanh/TuanBDTuanKT.php?hk="+hk+"&nh="+namhoc+"";
                loadSpinnertuan(urltuan);
            }
        });


        //nut back
        if(getSupportActionBar()!=null){
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }
    }

    public void loadSpinnertuan(String url) {
        RequestQueue requestQueue= Volley.newRequestQueue(getApplicationContext());
        StringRequest stringRequest=new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try{
                    JSONArray jsonArray=new JSONArray(response);
                    JSONObject jsonObject1=jsonArray.getJSONObject(0);
                    String tuanbd=jsonObject1.getString("tuanBD");
                    String tuankt=jsonObject1.getString("tuanKT");

                    SharedPreferences shared = getSharedPreferences("tuandbkt", Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = shared.edit();
                    editor.putString("tuanBD",tuanbd);
                    editor.putString("tuanKT",tuankt);
                    editor.commit();
                        int bd=Integer.parseInt(tuanbd);
                        int kt=Integer.parseInt(tuankt);
                        for (int k=bd;k<=kt;k++){
                            tuanArrayList.add(k-bd,"Tuần "+k);
                        }
                        if (tuanArrayList.size()==0){
                            tuanArrayList.add(0,"Tuần 22");

                        }
                    spntuan.setAdapter(new ArrayAdapter<String>(LichBieuActivity.this,
                            android.R.layout.simple_spinner_dropdown_item, tuanArrayList));

                }catch (JSONException e){e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
                Toast.makeText(LichBieuActivity.this, error.toString(), Toast.LENGTH_LONG).show();
            }
        });
        int socketTimeout = 30000;
        RetryPolicy policy = new DefaultRetryPolicy(socketTimeout, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        stringRequest.setRetryPolicy(policy);
        requestQueue.add(stringRequest);
    }

    public void loadSpinnerHk(String url) {
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
                    spnhocky1.setAdapter(new ArrayAdapter<String>(LichBieuActivity.this,
                            android.R.layout.simple_spinner_dropdown_item, hockyArrayList));
                }catch (JSONException e){e.printStackTrace();}
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(LichBieuActivity.this, "KHông tim thay", Toast.LENGTH_SHORT).show();
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
    //select thoikhoabieu asyntask(đuongan, so, chuoi tra ve) đọc json đổ và arrayadapter
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
                    JSONObject lh= mangjson.getJSONObject(i);
                    tkb_tuanArrayList.add(new tkb_tuan(
                            lh.getString("tenlopHP"),
                            lh.getString("tenPhong"),
                            lh.getString("thu"),
                            lh.getString("tietBD"),
                            lh.getString("sotiet"),
                            lh.getString("ht")

                    ));
                }
                tkb_tuan_Adapter tuanAdapter= new tkb_tuan_Adapter(
                        getApplicationContext(),
                        R.layout.row_tkb_tuan,
                        tkb_tuanArrayList);
                listView.setAdapter(tuanAdapter);

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

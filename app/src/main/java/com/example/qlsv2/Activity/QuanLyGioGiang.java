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

public class QuanLyGioGiang extends AppCompatActivity {
    com.example.qlsv2.Class.url url= new url();
    Button btnok;
    TextView txttuanht, txtnhhk;
    Spinner spndonvi, spncanbo,spnhk,spntuan;
    ArrayList<String> DviArrayList;
    ArrayList<String> CBArrayList;
    ArrayList<String> hkArrayList;
    ArrayList<String> tuanArrayList;
    String urlDvi=url.getUrl()+"diemdanh/DonVi.php";
    String URLhocky=url.getUrl()+"diemdanh/HocKy.php";
    String urlCB;
    String urltuan;
    ListView listViewQLGG;
    ArrayList<tkb_tuan> tkbQlggTuanArrayList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quan_ly_gio_giang);

        //anh xa
        spnhk=findViewById(R.id.spnhk);
        spntuan=findViewById(R.id.spntuan);
        spncanbo= findViewById(R.id.spngiaovien);
        spndonvi= findViewById(R.id.spndonvi);
        txtnhhk=findViewById(R.id.txthknh);
        txttuanht= findViewById(R.id.txttuanht);
        listViewQLGG= findViewById(R.id.lvqlgiogiang);

        //nut back
        if(getSupportActionBar()!=null){
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }

        //xét textview txtnhhk, txttuanht
        SharedPreferences shared= getSharedPreferences("hocky", Context.MODE_PRIVATE);
        final String hk = shared.getString("hocky", "");
        final String nh = shared.getString("namhoc", "");
        final String tgbd = shared.getString("thoigianBD", "");
        final String idhk = shared.getString("idHK", "");
        SharedPreferences shared2= getSharedPreferences("tuanht", Context.MODE_PRIVATE);
        final String tuan = shared2.getString("sttTuan", "");
        final String tuanhtai = shared2.getString("tuanht", "");
        SharedPreferences shared3= getSharedPreferences("mondaytosundaynow", Context.MODE_PRIVATE);
        final String monday = shared3.getString("monday", "");
        final String sunday = shared3.getString("sunday", "");
        final String dayht = shared3.getString("ngayht", "");
        txttuanht.setText("Tuần "+tuanhtai+" (từ "+monday+" đến "+sunday+" )");
        txtnhhk.setText("Học kỳ "+hk+" năm học "+nh);

        //Load spinner don vi
        DviArrayList=new ArrayList<>();
        loadSpinnerDvi(urlDvi);
        //Click spiner đơn vị load spiner can bo
        spndonvi.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                String s= DviArrayList.get(i);
                String[] out = s.split(" ");
                String idDvi =out[0];
                CBArrayList= new ArrayList<>();
                urlCB=url.getUrl()+"diemdanh/TenCBTheoDVi.php?idDvi="+idDvi+"";
                loadSpinnerCB(urlCB);
                spncanbo.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> adapterView, View view, int ii, long l) {
                        //spnhocky
                        hkArrayList=new ArrayList<>();
                        loadSpinnerHk(URLhocky);
                        spnhk.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                            @Override
                            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                                String s= hkArrayList.get(i);
                                String[] outpu = s.split("[\\, ]");
                                String hki =outpu[2];
                                String namhoc=outpu[6];
                                tuanArrayList= new ArrayList<>();
                                urltuan =url.getUrl()+"diemdanh/TuanBDTuanKT.php?hk="+hki+"&nh="+namhoc+"";
                                loadSpinnertuan(urltuan);
                                spntuan.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                                    @Override
                                    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                                       LoadlistviewQLGG();
                                    }
                                    @Override
                                    public void onNothingSelected(AdapterView<?> adapterView) {
                                        // Đổ dữ liệu ra lisview lich bieu hien tai
                                        //lấy idCB
                                        SharedPreferences shared9= getSharedPreferences("canbo", Context.MODE_PRIVATE);
                                        final String idcb = shared9.getString("idCB", "");
                                        tkbQlggTuanArrayList = new ArrayList<tkb_tuan>();
                                        final String  URLtkbtuan= url.getUrl()+"diemdanh/LichBieuTuanHienTai.php?idCB="+idcb+"&sttTuan="+tuan+"&idHK="+idhk+"";
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
                    }
                    @Override
                    public void onNothingSelected(AdapterView<?> adapterView) {
                        // DO Nothing here
                        //spnhocky
                        hkArrayList=new ArrayList<>();
                        loadSpinnerHk(URLhocky);
                    }
                });
            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                // DO Nothing here
                String idDvi ="1";
                CBArrayList= new ArrayList<>();
                urlCB=url.getUrl()+"diemdanh/TenCBTheoDVi.php?idDvi="+idDvi+"";
                loadSpinnerCB(urlCB);
            }
        });



    }

    public void LoadlistviewQLGG(){
        tkbQlggTuanArrayList = new ArrayList<tkb_tuan>();
        tkb_tuan_Adapter tuanAdapter= new tkb_tuan_Adapter(
                getApplicationContext(),
                R.layout.row_tkb_tuan,
                tkbQlggTuanArrayList);
        listViewQLGG.setAdapter(tuanAdapter);
        String coun=   spnhk.getItemAtPosition(spnhk.getSelectedItemPosition()).toString();
        String[] outpu = coun.split("[\\, ]");
        String hk =outpu[2];
        String namhoc=outpu[6];
        String hotencb=   spncanbo.getItemAtPosition(spncanbo.getSelectedItemPosition()).toString();
        String tuan=  spntuan.getSelectedItem().toString();
        String[] sotuan = tuan.split(" ");
        SharedPreferences shared6= getSharedPreferences("tuandbkt", Context.MODE_PRIVATE);
        final String tuabd = shared6.getString("tuanBD", "");
        int bd= Integer.parseInt(tuabd);
        int st =Integer.parseInt(sotuan[1])-bd+1;
        Toast.makeText(QuanLyGioGiang.this, hotencb+st+hk+namhoc, Toast.LENGTH_SHORT).show();
        final String  urlQlggTuan= url.getUrl()+"diemdanh/LichBieuTuanTheoTenCB.php?hotenCB="+hotencb+"&sttTuan="+st+"&hocky="+hk+"&namhoc="+namhoc+"";
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                new docJson().execute(urlQlggTuan);
            }
        });
    }

    private void loadSpinnerDvi(String url) {
        RequestQueue requestQueue= Volley.newRequestQueue(getApplicationContext());
        StringRequest stringRequest=new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try{
                    JSONArray jsonArray=new JSONArray(response);
                    for(int i=0;i<jsonArray.length();i++){
                        JSONObject jsonObject1=jsonArray.getJSONObject(i);
                        String idDvi=jsonObject1.getString("idDvi");
                        String msDvi=jsonObject1.getString("msDvi");
                        String tenDvi=jsonObject1.getString("tenDvi");
                        DviArrayList.add(idDvi+" "+msDvi+" - "+tenDvi);
                    }
                    spndonvi.setAdapter(new ArrayAdapter<String>(QuanLyGioGiang.this,
                            android.R.layout.simple_spinner_dropdown_item, DviArrayList));
                }catch (JSONException e){e.printStackTrace();}
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(QuanLyGioGiang.this, "KHông tim thay", Toast.LENGTH_SHORT).show();
                error.printStackTrace();
            }
        });
        int socketTimeout = 30000;
        RetryPolicy policy = new DefaultRetryPolicy(socketTimeout, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        stringRequest.setRetryPolicy(policy);
        requestQueue.add(stringRequest);
    }

    private void loadSpinnerCB(String url) {
        RequestQueue requestQueue= Volley.newRequestQueue(getApplicationContext());
        StringRequest stringRequest=new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try{
                    JSONArray jsonArray=new JSONArray(response);
                    for(int i=1;i<jsonArray.length();i++){
                        JSONObject jsonObject1=jsonArray.getJSONObject(i);
                        String hotenCB=jsonObject1.getString("hotenCB");
                        CBArrayList.add(hotenCB);
                    }
                    spncanbo.setAdapter(new ArrayAdapter<String>(QuanLyGioGiang.this,
                            android.R.layout.simple_spinner_dropdown_item, CBArrayList));
                }catch (JSONException e){e.printStackTrace();}
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(QuanLyGioGiang.this, "KHông tim thay", Toast.LENGTH_SHORT).show();
                error.printStackTrace();
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
                        hkArrayList.add("Học kỳ "+hocky1+", năm học "+namhoc);
                    }
                    spnhk.setAdapter(new ArrayAdapter<String>(QuanLyGioGiang.this,
                            android.R.layout.simple_spinner_dropdown_item, hkArrayList));
                }catch (JSONException e){e.printStackTrace();}
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(QuanLyGioGiang.this, "KHông tim thay", Toast.LENGTH_SHORT).show();
                error.printStackTrace();
            }
        });
        int socketTimeout = 30000;
        RetryPolicy policy = new DefaultRetryPolicy(socketTimeout, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        stringRequest.setRetryPolicy(policy);
        requestQueue.add(stringRequest);
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
                    spntuan.setAdapter(new ArrayAdapter<String>(QuanLyGioGiang.this,
                            android.R.layout.simple_spinner_dropdown_item, tuanArrayList));

                }catch (JSONException e){e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
                Toast.makeText(QuanLyGioGiang.this, error.toString(), Toast.LENGTH_LONG).show();
            }
        });
        int socketTimeout = 30000;
        RetryPolicy policy = new DefaultRetryPolicy(socketTimeout, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        stringRequest.setRetryPolicy(policy);
        requestQueue.add(stringRequest);
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
                    tkbQlggTuanArrayList.add(new tkb_tuan(
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
                        tkbQlggTuanArrayList);
                listViewQLGG.setAdapter(tuanAdapter);

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
    //nut back
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id=item.getItemId();
        if (id==android.R.id.home){
            this.finish();
        }
        return super.onOptionsItemSelected(item);
    }
}

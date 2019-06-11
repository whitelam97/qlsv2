package com.example.qlsv2;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ListView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.qlsv2.Adapter.SinhVienAdapter;
import com.example.qlsv2.Class.sinhvien;
import com.example.qlsv2.Class.url;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class DiemDanhActivity extends AppCompatActivity {

    ListView lvsv;
    ArrayList<sinhvien> sinhvienArrayList;
    SinhVienAdapter sinhVienAdapter;
    com.example.qlsv2.Class.url url= new url();
    String idlopHP,idTKB,sttTuan;

    ArrayList<String> svcheckArrayList;

    Button btnhoantat, btndong;
    CheckBox checkall;
    String urlthemsv=url.getUrl()+"diemdanh/DiemDanh.php";
    String urlxoasv=url.getUrl()+"diemdanh/XoaDiemDanhSV.php";
    String urlupdatedd=url.getUrl()+"diemdanh/SetTinhTrangTGTKB.php";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_diem_danh);

        //anh xa
        btnhoantat = findViewById(R.id.btnhoattat);
        btndong = findViewById(R.id.btndong);
        checkall = findViewById(R.id.ckball);

        //lấy id tkb
        SharedPreferences shared = getSharedPreferences("tkbclick", Context.MODE_PRIVATE);
        idlopHP = shared.getString("idlophp", "");
        idTKB = shared.getString("idtkb", "");
        sttTuan = shared.getString("stttuan", "");


        //nut back
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }

        //load sv đã được điểm danh
        svcheckArrayList = new ArrayList<String>();
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                new docJsonCheckSV().execute(url.getUrl()+"diemdanh/SinhVienDaDiemdanh.php?idTKB="+idTKB+"");
            }
        });

        //load lisview sinh viên từ csdl lên
        lvsv = findViewById(R.id.lvsv);
        sinhvienArrayList = new ArrayList<sinhvien>();
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                new docJson().execute(url.getUrl() + "diemdanh/DanhSachSVThuocLopHP.php?idlopHP="+idlopHP+"");
            }
        });


        //click chọn tat ca sv
        checkall.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    for (int i = lvsv.getCount() - 1; i >= 0; i--) {
                        sinhvienArrayList.get(i).setCheck(true);
                    }
                    //Sau khi xóa xong thì gọi update giao diện
                    sinhVienAdapter.notifyDataSetChanged();
                }
                if (!isChecked) {
                    for (int i = lvsv.getCount() - 1; i >= 0; i--) {
                        sinhvienArrayList.get(i).setCheck(false);
                    }
                    //Sau khi xóa xong thì gọi update giao diện
                    sinhVienAdapter.notifyDataSetChanged();
                }
            }
        });



        //click button đóng
        btndong.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v) {
               finish();
            }
        });

        //click button hoàn tất điểm danh_ insert or xóa sv in hoc
        btnhoantat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                for (int i=0; i<sinhvienArrayList.size(); i++){
                    if (sinhvienArrayList.get(i).isChecked()){
                        String ii= sinhvienArrayList.get(i).getIdSV();
                         themsv(urlthemsv,ii,idTKB);
                    }
                    else {
                        String ii= sinhvienArrayList.get(i).getIdSV();
                        themsv(urlxoasv,ii,idTKB);
                    }
                }
                //upate tinh trang thoi gin diem danh
                UpdateTinhtrangTGDiemDanh(urlupdatedd);
                Toast.makeText(DiemDanhActivity.this, "Cập nhật thành công!", Toast.LENGTH_SHORT).show();
                finish();
            }
        });
    }

    //select danh sach sinh vien
    class docJson extends AsyncTask<String,Integer,String> {
        //doinbackgroufd dung doc du lieu tren mang
        @Override
        protected String doInBackground(String... strings) {
            return docnoidungtuURL(strings[0]);
        }
        @Override
        protected void onPostExecute(String s) {
            try {
                JSONArray mangjonsv = new JSONArray(s);
                for(int i=0;i<mangjonsv.length();i++){
                    int k=0;
                    JSONObject lh= mangjonsv.getJSONObject(i);
                    for (int j=0;j<svcheckArrayList.size();j++)
                    {
                        if (svcheckArrayList.get(j).equals(lh.getString("idSV"))){
                           k=1;
                        }
                    }
                    if (k==1){
                        sinhvienArrayList.add(new sinhvien(
                                lh.getString("idSV"),
                                lh.getString("mssv"),
                                lh.getString("hotenSV"),
                                lh.getString("msLopCN"),
                                lh.getString("cohoc"),
                                lh.getString("tongbuoi"),
                                true
                        ));
                    }
                    else {
                        sinhvienArrayList.add(new sinhvien(
                                lh.getString("idSV"),
                                lh.getString("mssv"),
                                lh.getString("hotenSV"),
                                lh.getString("msLopCN"),
                                lh.getString("cohoc"),
                                lh.getString("tongbuoi"),
                                false
                        ));
                    }

                }
                sinhVienAdapter = new SinhVienAdapter(
                        getApplicationContext(),
                        R.layout.row_diemdanhsinhvien,
                        sinhvienArrayList);
                lvsv.setAdapter(sinhVienAdapter);

            } catch (JSONException e) {
//                Toast.makeText(DiemDanhActivity.this, "Lỗi "+e.toString(), Toast.LENGTH_SHORT).show();
                e.printStackTrace();
            }
        }
    }

    //select danh sach sv đã điểm danh
    class docJsonCheckSV extends AsyncTask<String,Integer,String> {
        //doinbackgroufd dung doc du lieu tren mang
        @Override
        protected String doInBackground(String... strings) {
            return docnoidungtuURL(strings[0]);
        }
        @Override
        protected void onPostExecute(String s) {
            try {
                JSONArray mang = new JSONArray(s);
                for(int i=0;i<mang.length();i++){
                    JSONObject lh= mang.getJSONObject(i);
                    svcheckArrayList.add(i,lh.getString("idSV"));
                }
//                Toast.makeText(DiemDanhActivity.this, svcheckArrayList.size()+"", Toast.LENGTH_SHORT).show();

            } catch (JSONException e) {
                Toast.makeText(DiemDanhActivity.this, "Lỗi check SV", Toast.LENGTH_SHORT).show();
                e.printStackTrace();
            }
        }
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
    //them  sv(điểm danh)
    private void themsv(String urladdsv, final String getidsv, final String getidtkb){
        RequestQueue requestQueue =Volley.newRequestQueue(this);
        StringRequest stringRequest= new StringRequest(Request.Method.POST,urladdsv,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        if(response.equals("success")){
//                            Toast.makeText(DiemDanhActivity.this, "Cập nhật thành công!", Toast.LENGTH_SHORT).show();
                        }
//                        else
//                            Toast.makeText(DiemDanhActivity.this, "Cập nhật thành công!", Toast.LENGTH_SHORT).show();
                    }
                },
                new Response.ErrorListener(){
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(DiemDanhActivity.this, "Lỗi sever"+error.toString(), Toast.LENGTH_SHORT).show();
                    }
                }
        ){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {

                Map<String,String> param = new HashMap<>();
                param.put("idSV",getidsv);
                param.put("idTKB",getidtkb);
                return param;
            }
        };
        requestQueue.add(stringRequest);
    }

    private void UpdateTinhtrangTGDiemDanh(String urlUpdateTTTG){
        RequestQueue requestQueue =Volley.newRequestQueue(this);
        StringRequest stringRequest= new StringRequest(Request.Method.POST,urlUpdateTTTG,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        if(response.equals("success")){
//                            Toast.makeText(DiemDanhActivity.this, "Điểm danh thành công!", Toast.LENGTH_LONG).show();
                        }
//                        else
//                            Toast.makeText(DiemDanhActivity.this, "Điểm danh lỗi!", Toast.LENGTH_SHORT).show();
                    }
                },
                new Response.ErrorListener(){
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(DiemDanhActivity.this, "Lỗi sever"+error.toString(), Toast.LENGTH_SHORT).show();
                    }
                }
        ){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> param = new HashMap<>();
                String pattern = "dd-MM-yyyy";
                SimpleDateFormat sdf = new SimpleDateFormat(pattern);
                Date now = new Date();
                String t= sdf.format(now);
                param.put("tinhtrang","1");
                param.put("thoigiandiemdanh",t);
                param.put("idTKB",idTKB);
                return param;
            }
        };
        requestQueue.add(stringRequest);
    }


















}

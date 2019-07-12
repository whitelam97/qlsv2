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
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.qlsv2.Adapter.SinhVienAdapter;
import com.example.qlsv2.Class.ngayhoc;
import com.example.qlsv2.Class.sinhvien;
import com.example.qlsv2.Class.url;
import com.example.qlsv2.R;

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

public class ThongKeGiangDay extends AppCompatActivity {
    Button btnok,btnthoat;
    CheckBox ckball;
    Spinner spnhknh, spnlophp,spnngayhoc;
    ListView listViewTKGD;
    com.example.qlsv2.Class.url url= new url();
    String URLhknh=url.getUrl()+"diemdanh/HocKy.php";
    String urlLopHP;
    String urlngayhoc;
    String idCB;
    ArrayList<String> hknkArrayList;
    ArrayList<String> LopHPArrayList;
    ArrayList<String> ngayhocArrayList;
    ArrayList<ngayhoc> ngayhocsclasArray;
    ArrayList<String> svcheckArrayList;
    ArrayList<sinhvien> sinhvienArrayList;
    SinhVienAdapter sinhVienAdapter;
    String urlthemsv=url.getUrl()+"diemdanh/DiemDanh.php";
    String urlxoasv=url.getUrl()+"diemdanh/XoaDiemDanhSV.php";
    String urlupdatedd=url.getUrl()+"diemdanh/SetTinhTrangTGTKB.php";

    String idTKB,idlopHP;
    String hk,nh;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_thong_ke_giang_day);

        btnok=findViewById(R.id.btnhoattat);
        btnthoat=findViewById(R.id.btndongtkgd);
        ckball=findViewById(R.id.ckballtkgd);
        spnhknh=findViewById(R.id.spnhknh);
        spnlophp=findViewById(R.id.spnlophptkgd);
        spnngayhoc=findViewById(R.id.spnngay);
        listViewTKGD=findViewById(R.id.lvsvTKGD);

        SharedPreferences shared= getSharedPreferences("canbo", Context.MODE_PRIVATE);
         idCB = shared.getString("idCB", "");

        //spnhocky
        hknkArrayList=new ArrayList<>();
        loadSpinnerhknh(URLhknh);
        spnhknh.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                String s= hknkArrayList.get(i);
                String[] outpu = s.split("[\\, ]");
                hk =outpu[2];
                nh=outpu[6];
                LopHPArrayList= new ArrayList<>();
//                Toast.makeText(ThongKeGiangDay.this, ""+idCB+"-"+hk+"-"+nh, Toast.LENGTH_SHORT).show();
                urlLopHP =url.getUrl()+"diemdanh/TenLopHPTheoHK.php?idCB="+idCB+"&hocky="+hk+"&namhoc="+nh+"";
                loadSpinnerlophp(urlLopHP);
                spnlophp.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> adapterView, View viiew, int po, long l) {
                        String tenlop= LopHPArrayList.get(po);
                        ngayhocArrayList= new ArrayList<>();
                        ngayhocsclasArray = new ArrayList<ngayhoc>();
                        urlngayhoc =url.getUrl()+"diemdanh/NgayHocTheoLopHP.php?tenlophp="+tenlop+"";
                        loadSpinnerNgayHoc(urlngayhoc);
                        spnngayhoc.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                            @Override
                            public void onItemSelected(AdapterView<?> adapterView, View viiiew, int posi, long l) {
                                 idTKB= ngayhocsclasArray.get(posi).getIdTKB();
                                 idlopHP= ngayhocsclasArray.get(posi).getIdlopHP();
                                //load sv đã được điểm danh
                                svcheckArrayList = new ArrayList<String>();
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        new docJsonCheckSV().execute(url.getUrl() + "diemdanh/SinhVienDaDiemdanh.php?idTKB="+idTKB+"");
                                    }
                                });

                                //load lisview sinh viên từ csdl lên
                                sinhvienArrayList = new ArrayList<sinhvien>();
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        new docJson().execute(url.getUrl() + "diemdanh/DanhSachSVThuocLopHP.php?idlopHP="+idlopHP+"");
                                    }
                                });

                                //click chọn tat ca sv
                                ckball.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                                    @Override
                                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                                        if (isChecked) {
                                            for (int i = listViewTKGD.getCount() - 1; i >= 0; i--) {
                                                sinhvienArrayList.get(i).setCheck(true);
                                            }
                                            //Sau khi xóa xong thì gọi update giao diện
                                            sinhVienAdapter.notifyDataSetChanged();
                                        }
                                        if (!isChecked) {
                                            for (int i = listViewTKGD.getCount() - 1; i >= 0; i--) {
                                                sinhvienArrayList.get(i).setCheck(false);
                                            }
                                            //Sau khi xóa xong thì gọi update giao diện
                                            sinhVienAdapter.notifyDataSetChanged();
                                        }
                                    }
                                });

                                //click button đóng
                                btnthoat.setOnClickListener(new View.OnClickListener()
                                {
                                    @Override
                                    public void onClick(View v) {
                                        finish();
                                    }
                                });

                                //click button hoàn tất điểm danh_ insert or xóa sv in hoc
                                btnok.setOnClickListener(new View.OnClickListener() {
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
                                    }
                                });


                            }
                            @Override
                            public void onNothingSelected(AdapterView<?> adapterView) {
                                //load sv đã được điểm danh
                                svcheckArrayList = new ArrayList<String>();
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        new docJsonCheckSV().execute(url.getUrl() + "diemdanh/SinhVienDaDiemdanh.php?idTKB=39323");
                                    }
                                });

                                //load lisview sinh viên từ csdl lên
                                sinhvienArrayList = new ArrayList<sinhvien>();
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        new docJson().execute(url.getUrl() + "diemdanh/DanhSachSVThuocLopHP.php?idlopHP=3297");
                                    }
                                });
                            }
                        });
                    }
                    @Override
                    public void onNothingSelected(AdapterView<?> adapterView) {
                        String tenlop= "";
                        ngayhocArrayList= new ArrayList<>();
                        urlngayhoc =url.getUrl()+"diemdanh/NgayHocTheoLopHP.php?tenlophp="+tenlop+"";
                        loadSpinnerNgayHoc(urlngayhoc);
                    }
                });
            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                // DO Nothing here
                String hk ="7";
                String namhoc="2018-2019";

                LopHPArrayList= new ArrayList<>();
                urlLopHP =url.getUrl()+"diemdanh/TenLopHPTheoHK.php?idCB="+idCB+"&hocky="+hk+"&namhoc="+namhoc+"";
                loadSpinnerlophp(urlLopHP);
            }
        });

        //nut back
        if(getSupportActionBar()!=null){
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }
    }
    //spnhocky
    private void loadSpinnerhknh(String url) {
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
                        hknkArrayList.add("Học kỳ "+hocky1+", năm học "+namhoc);
                    }
                    spnhknh.setAdapter(new ArrayAdapter<String>(ThongKeGiangDay.this, android.R.layout.simple_spinner_dropdown_item, hknkArrayList));
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
    private void loadSpinnerlophp(String url) {
        RequestQueue requestQueue= Volley.newRequestQueue(getApplicationContext());
        StringRequest stringRequest=new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try{
                    JSONArray jsonArray=new JSONArray(response);
                    for(int i=0;i<jsonArray.length();i++){
                        JSONObject jsonObject1=jsonArray.getJSONObject(i);
                        String tenlopHP=jsonObject1.getString("tenlopHP");
                        LopHPArrayList.add(tenlopHP);
                    }
                    spnlophp.setAdapter(new ArrayAdapter<String>(ThongKeGiangDay.this,
                            android.R.layout.simple_spinner_dropdown_item, LopHPArrayList));
                }catch (JSONException e){e.printStackTrace();}
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(ThongKeGiangDay.this, "KHông tim thay", Toast.LENGTH_SHORT).show();
                error.printStackTrace();
            }
        });
        int socketTimeout = 30000;
        RetryPolicy policy = new DefaultRetryPolicy(socketTimeout, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        stringRequest.setRetryPolicy(policy);
        requestQueue.add(stringRequest);
    }
    private void loadSpinnerNgayHoc(String url) {
        RequestQueue requestQueue= Volley.newRequestQueue(getApplicationContext());
        StringRequest stringRequest=new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try{
                    JSONArray jsonArray=new JSONArray(response);
                    for(int i=0;i<jsonArray.length();i++){
                        JSONObject jsonObject1=jsonArray.getJSONObject(i);
                        String idlopHP=jsonObject1.getString("idlopHP");
                        String idTKB=jsonObject1.getString("idTKB");
                        String ngayhoc=jsonObject1.getString("ngayhoc");
                        ngayhocsclasArray.add(new ngayhoc(idlopHP,idTKB,ngayhoc));
//                      Kiem tra phải nhỏ hon ngay hien tai mới add vao spinner
                        ngayhocArrayList.add(ngayhoc);
                    }
                    spnngayhoc.setAdapter(new ArrayAdapter<String>(ThongKeGiangDay.this,
                            android.R.layout.simple_spinner_dropdown_item, ngayhocArrayList));
                }catch (JSONException e){e.printStackTrace();}
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(ThongKeGiangDay.this, "KHông tim thay", Toast.LENGTH_SHORT).show();
                error.printStackTrace();
            }
        });
        int socketTimeout = 30000;
        RetryPolicy policy = new DefaultRetryPolicy(socketTimeout, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        stringRequest.setRetryPolicy(policy);
        requestQueue.add(stringRequest);
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
                JSONArray man = new JSONArray(s);
                for(int i=0;i<man.length();i++){
                    int k=0;
                    JSONObject lh= man.getJSONObject(i);
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
                listViewTKGD.setAdapter(sinhVienAdapter);
            } catch (JSONException e) {
                Toast.makeText(ThongKeGiangDay.this, "Lỗi", Toast.LENGTH_SHORT).show();
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
            } catch (JSONException e) {
//                Toast.makeText(DiemDanhActivity.this, "Lỗi", Toast.LENGTH_SHORT).show();
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
    //them xóa sv(điểm danh)
    private void themsv(String urladdsv, final String getidsv, final String getidtkb){
        RequestQueue requestQueue =Volley.newRequestQueue(this);
        StringRequest stringRequest= new StringRequest(Request.Method.POST,urladdsv,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        if(response.equals("success")){
                            Toast.makeText(ThongKeGiangDay.this, "Cập nhật thành công!", Toast.LENGTH_SHORT).show();
                        }
                        else
                            Toast.makeText(ThongKeGiangDay.this, "Cập nhật thành công!", Toast.LENGTH_SHORT).show();
                    }
                },
                new Response.ErrorListener(){
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(ThongKeGiangDay.this, "Lỗi sever"+error.toString(), Toast.LENGTH_SHORT).show();
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
    //them xóa sv(điểm danh)
    private void UpdateTinhtrangTGDiemDanh(String urlUpdateTTTG){
        RequestQueue requestQueue =Volley.newRequestQueue(this);
        StringRequest stringRequest= new StringRequest(Request.Method.POST,urlUpdateTTTG,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        if(response.equals("success")){
                            Toast.makeText(ThongKeGiangDay.this, "Điểm danh thành công!", Toast.LENGTH_LONG).show();
                        }
                        else
                            Toast.makeText(ThongKeGiangDay.this, "Điểm danh thành công!", Toast.LENGTH_SHORT).show();
                    }
                },
                new Response.ErrorListener(){
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(ThongKeGiangDay.this, "Lỗi sever"+error.toString(), Toast.LENGTH_SHORT).show();
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

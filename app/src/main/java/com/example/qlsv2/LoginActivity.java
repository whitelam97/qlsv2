package com.example.qlsv2;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
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
import com.example.qlsv2.Class.url;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import es.dmoral.toasty.Toasty;

public class LoginActivity extends AppCompatActivity {

    EditText mscb, matkhau;
    TextView signup,login,txtimage;
    String pass,username;
    String urllogin,urlhocky,urltuan,urlmosu;
    com.example.qlsv2.Class.url url= new url();
    private long backPressedTime;
    //spinner quyền truy cập
    Spinner spnquyen;
    String urlquyen;
    ArrayList<String> quyenArrayList;
    String quyen;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        txtimage=findViewById(R.id.txtimage);
        txtimage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mscb.setText("cangpa");
                matkhau.setText("30e25409a9c409f0bddd0ccbf2700145");
            }
        });
        signup = (TextView) findViewById(R.id.signup);
        mscb = (EditText) findViewById(R.id.user);
        matkhau = (EditText) findViewById(R.id.pass);
        login = (TextView) findViewById(R.id.login);

        //spnner quyen
       urlquyen =url.getUrl()+"diemdanh/Nhomnguoidung.php";
        quyenArrayList=new ArrayList<>();
        spnquyen=findViewById(R.id.spnquyen);
        loadSpinnerData(urlquyen);

        mscb.setText("ngantm");
        matkhau.setText("3e5d638f3350fd2dd6912f30d31df86b");

        login.setOnClickListener(new  View.OnClickListener() {
            @Override
            public void onClick(View v) {
                username = mscb.getText().toString();
                pass = matkhau.getText().toString();
                if (username.isEmpty() || pass.isEmpty()) {
                    Toast.makeText(LoginActivity.this, "fill details", Toast.LENGTH_SHORT).show();
                } else {
                    login(username, pass);
                    selectHocKy();
                    selecttuan();
                    seMondaytoSundayofWeek();
                }
            }
        });

    }
    //Load spniner quyen truy cap
    private void loadSpinnerData(String url) {
        RequestQueue requestQueue= Volley.newRequestQueue(getApplicationContext());
        StringRequest stringRequest=new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try{
                    JSONArray jsonArray=new JSONArray(response);
                    for(int i=0;i<jsonArray.length();i++){
                        JSONObject jsonObject1=jsonArray.getJSONObject(i);
                        String nhomnguoidung=jsonObject1.getString("nhomnguoidung");
                        quyenArrayList.add(nhomnguoidung);
                    }
                    ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>(
                            LoginActivity.this,R.layout.item_spinner,quyenArrayList);
                    spinnerArrayAdapter.setDropDownViewResource(R.layout.item_spinner);
                    spnquyen.setAdapter(spinnerArrayAdapter);
//                    spnquyen.setAdapter(new ArrayAdapter<String>(LoginActivity.this,
//                            android.R.layout.simple_spinner_dropdown_item, quyenArrayList));
                }catch (JSONException e){e.printStackTrace();}
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(LoginActivity.this, "KHông tim thay", Toast.LENGTH_LONG).show();
                error.printStackTrace();
            }
        });
        int socketTimeout = 30000;
        RetryPolicy policy = new DefaultRetryPolicy(socketTimeout, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        stringRequest.setRetryPolicy(policy);
        requestQueue.add(stringRequest);
    }
    //đăng nhập
    public void login(final String user, final String pass){

        quyen=spnquyen.getSelectedItem().toString();
        urllogin = url.getUrl()+"diemdanh/login_canbo.php?msCB="+user+"&matkhau="+pass+"&nhomnguoidung="+quyen+"";

        Log.i("Hiteshurl",""+urllogin);
        RequestQueue requestQueue = Volley.newRequestQueue(LoginActivity.this);
        StringRequest stringRequest = new StringRequest(Request.Method.GET, urllogin, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    JSONArray jsonArray = jsonObject.getJSONArray("canbo");
                    JSONObject jsonObject1 = jsonArray.getJSONObject(0);
                    String idCB = jsonObject1.getString("idCB");
                    String msCB = jsonObject1.getString("msCB");
                    String hotenCB = jsonObject1.getString("hotenCB");
                    String ngaysinh = jsonObject1.getString("ngaysinh");
                    String gioitinh = jsonObject1.getString("gioitinh");
                    String chucvu = jsonObject1.getString("chucvu");
                    String sdt = jsonObject1.getString("sdt");
                    String email = jsonObject1.getString("email");
                    String matkhau = jsonObject1.getString("matkhau");
                    String idDvi = jsonObject1.getString("idDvi");
                    String msDvi = jsonObject1.getString("msDvi");
                    String tenDvi = jsonObject1.getString("tenDvi");
                    String idQuyen = jsonObject1.getString("idQuyen");
                    String msQuyen = jsonObject1.getString("msQuyen");


                    SharedPreferences shared = getSharedPreferences("canbo", Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = shared.edit();
                    editor.putString("idCB",idCB);
                    editor.putString("msCB",msCB);
                    editor.putString("hotenCB",hotenCB);
                    editor.putString("ngaysinh",ngaysinh);
                    editor.putString("gioitinh",gioitinh);
                    editor.putString("chucvu",chucvu);
                    editor.putString("sdt",sdt);
                    editor.putString("email",email);
                    editor.putString("matkhau",matkhau);
                    editor.putString("idDvi",idDvi);
                    editor.putString("msDvi",msDvi);
                    editor.putString("tenDvi",tenDvi);
                    editor.putString("idQuyen",idQuyen);
                    editor.putString("msQuyen",msQuyen);
                    editor.commit();

                    // kiểm tra id xem là giáo viên hay thanh tra để chuyển activity
                    int ii=Integer.parseInt(idQuyen);
                    switch (ii){
                        case 1:  {
                            Intent intent = new Intent(LoginActivity.this,MainActivity.class);
                            startActivity(intent);
                            break;
                        }
                        case 2:  {
                            Intent intent1 = new Intent(LoginActivity.this,Main2Activity.class);
                            startActivity(intent1);
                            break;
                        }
                    }

                } catch (JSONException e) {
                    mscb.setText(e.toString());
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.i("HiteshURLerror",""+error);
            }
        });

        requestQueue.add(stringRequest);

    }
    //select học kỳ hiện tại
    public void selectHocKy(){
        urlhocky = url.getUrl()+"diemdanh/select_hocky_now.php";
        Log.i("Hiteshurl",""+urlhocky);
        RequestQueue requestQueue = Volley.newRequestQueue(LoginActivity.this);
        StringRequest stringRequest = new StringRequest(Request.Method.GET, urlhocky, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    JSONArray jsonArray = jsonObject.getJSONArray("hocky");
                    JSONObject jsonObject1 = jsonArray.getJSONObject(0);
                    String idHK = jsonObject1.getString("idHK");
                    String msHK = jsonObject1.getString("msHK");
                    String hocky = jsonObject1.getString("hocky");
                    String namhoc = jsonObject1.getString("namhoc");
                    String thoigianBD = jsonObject1.getString("thoigianBD");
                    String thoigianKT = jsonObject1.getString("thoigianKT");


                    SharedPreferences shared = getSharedPreferences("hocky", Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = shared.edit();
                    editor.putString("idHK",idHK);
                    editor.putString("msHK",msHK);
                    editor.putString("hocky",hocky);
                    editor.putString("namhoc",namhoc);
                    editor.putString("thoigianBD",thoigianBD);
                    editor.putString("thoigianKT",thoigianKT);
                    editor.commit();
                } catch (JSONException e) {
                    mscb.setText(e.toString());
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.i("HiteshURLerror",""+error);
            }
        });

        requestQueue.add(stringRequest);
    }
    //select tuần hiện tại
    public void selecttuan(){
        SharedPreferences shared= getSharedPreferences("hocky", Context.MODE_PRIVATE);
        String tgbd = shared.getString("thoigianBD", "");

        urltuan = url.getUrl()+"diemdanh/select_tuanhientai.php?thoigianBD="+tgbd+"";
        Log.i("Hiteshurl",""+urltuan);
        RequestQueue requestQueue = Volley.newRequestQueue(LoginActivity.this);
        StringRequest stringRequest = new StringRequest(Request.Method.GET, urltuan, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    JSONArray jsonArray = jsonObject.getJSONArray("tuanht");
                    JSONObject jsonObject1 = jsonArray.getJSONObject(0);
                    String sttTuan = jsonObject1.getString("sttTuan");
                    String tuanht = jsonObject1.getString("tuanht");


                    SharedPreferences shared = getSharedPreferences("tuanht", Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = shared.edit();
                    editor.putString("sttTuan",sttTuan);
                    editor.putString("tuanht",tuanht);
                    editor.commit();

                } catch (JSONException e) {
                    mscb.setText(e.toString());
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.i("HiteshURLerror",""+error);
            }
        });

        requestQueue.add(stringRequest);
    }
    //select cac ngay trong tuan hien tai tu thu 2- cn
    public void seMondaytoSundayofWeek(){
        SharedPreferences shared= getSharedPreferences("hocky", Context.MODE_PRIVATE);
        String idhk = shared.getString("idHK", "");
        SharedPreferences sharedtuan= getSharedPreferences("tuanht", Context.MODE_PRIVATE);
        String stttuan = sharedtuan.getString("sttTuan", "");

        urlmosu = url.getUrl()+"diemdanh/Se_MonDaytoSunDayOFWeek.php?idHK="+idhk+"&sttTuan="+stttuan+"";
        Log.i("Hiteshurl",""+urlmosu);
        RequestQueue requestQueue = Volley.newRequestQueue(LoginActivity.this);
        StringRequest stringRequest = new StringRequest(Request.Method.GET, urlmosu, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    JSONArray jsonArray = jsonObject.getJSONArray("mondaytosundaynow");
                    JSONObject jsonObject1 = jsonArray.getJSONObject(0);
                    String monday = jsonObject1.getString("monday");
                    String sunday = jsonObject1.getString("sunday");
                    String ngayht = jsonObject1.getString("ngayht");

                    SharedPreferences shared = getSharedPreferences("mondaytosundaynow", Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = shared.edit();
                    editor.putString("monday",monday);
                    editor.putString("sunday",sunday);
                    editor.putString("ngayht",ngayht);
                    editor.commit();

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.i("HiteshURLerror",""+error);
            }
        });

        requestQueue.add(stringRequest);
    }
    //doubclick to exit
    public void onBackPressed() {

        //doubclick to exit
        if (backPressedTime + 2000 > System.currentTimeMillis()) {
            super.onBackPressed();
            return;
        } else {
            Toasty.warning(getBaseContext(), "Press back again to exit", Toast.LENGTH_SHORT, true).show();

        }
        backPressedTime = System.currentTimeMillis();
//    DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
//        if (drawer.isDrawerOpen(GravityCompat.START)) {
//            drawer.closeDrawer(GravityCompat.START);
//        } else {
//            super.onBackPressed();
//        }
    }



}

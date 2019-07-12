package com.example.qlsv2.Activity;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.wifi.WifiManager;
import android.os.AsyncTask;
import android.os.Bundle;
import androidx.annotation.NonNull;

import com.example.qlsv2.R;
import com.google.android.material.navigation.NavigationView;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.text.format.Formatter;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.qlsv2.Adapter.LopHocAdapter;
import com.example.qlsv2.Class.lophoc;
import com.example.qlsv2.Class.url;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import es.dmoral.toasty.Toasty;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    TextView txtuser;
    TextView txtemail,txttuanht;
    SharedPreferences shared;


    ListView listView;
    ArrayList<lophoc> lophocArrayList;
    ArrayList<lophoc> lophocArrayListcapnhattt;
    com.example.qlsv2.Class.url url= new url();
    String urlupdatetinhtrang=url.getUrl()+"diemdanh/UpdateTinhTrang.php";

    private long backPressedTime;

    private Dialog dialog;

    String dayht;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Đổ dữ liệu ra lisview thoikhoabieu
        Intent intent = getIntent();
        final String monday = intent.getStringExtra("monday");
        final String sunday = intent.getStringExtra("sunday");
         dayht = intent.getStringExtra("ngayht");


        final String tuan = intent.getStringExtra("sttTuan");
        final String tuanhtai = intent.getStringExtra("tuanht");

        final String hkht = intent.getStringExtra("idHK");


        listView = findViewById(R.id.lvlophoc);
        WifiManager wifiManager= (WifiManager) getApplicationContext().getSystemService(WIFI_SERVICE);
        String cip = Formatter.formatIpAddress(wifiManager.getConnectionInfo().getIpAddress());

        Toast.makeText(this, ""+cip, Toast.LENGTH_SHORT).show();

        Toolbar toolbar = findViewById(R.id.toolbar1);
        setSupportActionBar(toolbar);
//        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
//        fab.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
//            }
//        });
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.addDrawerListener(new DrawerLayout.DrawerListener() {
            @Override
            public void onDrawerSlide(@NonNull View view, float v) {

            }

            @Override
            public void onDrawerOpened(@NonNull View view) {
                navHeader();
            }

            @Override
            public void onDrawerClosed(@NonNull View view) {

            }

            @Override
            public void onDrawerStateChanged(int i) {

            }
        });
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);


        //set textview ngày tuan hien tai
        txttuanht=findViewById(R.id.txttuanht);
        txttuanht.setText("Ngày "+dayht+", Tuần "+tuanhtai+" ("+monday+" - "+sunday+")");

        lophocArrayListcapnhattt = new ArrayList<lophoc>();
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                new docJsonArray().execute(url.getUrl()+"diemdanh/LopHocTrongNgayTT.php?sttTuan="+tuan+"&idHK="+hkht+"");
            }
        });

        loadlistview();

        //click listview
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (lophocArrayList.get(position).getTinhtrang().equals("0")){
                    Intent intent = new Intent(MainActivity.this, MapsActivity.class);
                    intent.putExtra("idlophp",lophocArrayList.get(position).getIdlopHP());
                    intent.putExtra("idtkb",lophocArrayList.get(position).getIdTKB());
                    startActivity(intent);

                }
            }
        });

    }
    public void loadlistview(){
        SharedPreferences shared= getSharedPreferences("canbo", Context.MODE_PRIVATE);
        final String idCB = shared.getString("idCB", "");
        SharedPreferences shared1= getSharedPreferences("hocky", Context.MODE_PRIVATE);
        final String hkht = shared1.getString("idHK", "");
        SharedPreferences shared2= getSharedPreferences("tuanht", Context.MODE_PRIVATE);
        final String tuan = shared2.getString("sttTuan", "");
        lophocArrayList = new ArrayList<lophoc>();
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                new docJson().execute(url.getUrl()+"diemdanh/LophocTrongNgayCB.php?idCB="+idCB+"&sttTuan="+tuan+"&idHK="+hkht+"");
            }
        });
    }
    //đọc json lay arraylist de cap nhap lai tinh trang
    class docJsonArray extends AsyncTask<String,Integer,String> {
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
                    lophocArrayListcapnhattt.add(new lophoc(
                            lh.getString("idTKB"),
                            lh.getString("sttTuan"),
                            lh.getString("thu"),
                            lh.getString("tietBD"),
                            lh.getString("sotiet"),
                            lh.getString("daybu"),
                            lh.getString("idlopHP"),
                            lh.getString("idPhong"),
                            lh.getString("tinhtrang"),
                            lh.getString("idCB"),
                            lh.getString("thoigiandiemdanh"),
                            lh.getString("msCB"),
                            lh.getString("hotenCB"),
                            lh.getString("idHP"),
                            lh.getString("mslopHP"),
                            lh.getString("tenlopHP"),
                            lh.getString("loailopHP"),
                            lh.getString("soSV"),
                            lh.getString("tuanhoc"),
                            lh.getString("msPhong"),
                            lh.getString("tenPhong"),
                            lh.getString("nhahoc"),
                            lh.getString("sttTang"),
                            lh.getString("loaiPhong"),
                            lh.getString("idHK"),
                            lh.getString("msHK"),
                            lh.getString("hocky"),
                            lh.getString("namhoc"),
                            lh.getString("thoigianBD"),
                            lh.getString("thoigianKT"),
                            lh.getString("tgbd"),
                            lh.getString("tgkt"),
                            lh.getString("tenDvi"),
                            lh.getString("timenow")
                    ));
                }
                String pattern = "HH:mm";
                SimpleDateFormat sdf = new SimpleDateFormat(pattern);
                for (int i=0;i<lophocArrayListcapnhattt.size();i++){
                    String tt= lophocArrayListcapnhattt.get(i).getTinhtrang();
                    try {
                        Date star = sdf.parse(lophocArrayListcapnhattt.get(i).getTgbd());
                        Date end = sdf.parse(lophocArrayListcapnhattt.get(i).getTgkt());
                        Date ht =sdf.parse(lophocArrayListcapnhattt.get(i).getTimenow());
                        if (ht.after(end)&&tt.equals("0")){
                            UpdateTinhtrang(urlupdatetinhtrang,"2",lophocArrayListcapnhattt.get(i).getIdTKB());
                        }
                        if(ht.after(star)&&ht.before(end)&& tt.equals("-1")) {
                            //mo khoa & set tinh trang lai la tiet hoc dang dien ra
                            UpdateTinhtrang(urlupdatetinhtrang,"0",lophocArrayListcapnhattt.get(i).getIdTKB());
                        }
                    } catch (ParseException e){
                        e.printStackTrace();
                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
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
                    lophocArrayList.add(new lophoc(
                            lh.getString("idTKB"),
                            lh.getString("sttTuan"),
                            lh.getString("thu"),
                            lh.getString("tietBD"),
                            lh.getString("sotiet"),
                            lh.getString("daybu"),
                            lh.getString("idlopHP"),
                            lh.getString("idPhong"),
                            lh.getString("tinhtrang"),
                            lh.getString("idCB"),
                            lh.getString("thoigiandiemdanh"),
                            lh.getString("msCB"),
                            lh.getString("hotenCB"),
                            lh.getString("idHP"),
                            lh.getString("mslopHP"),
                            lh.getString("tenlopHP"),
                            lh.getString("loailopHP"),
                            lh.getString("soSV"),
                            lh.getString("tuanhoc"),
                            lh.getString("msPhong"),
                            lh.getString("tenPhong"),
                            lh.getString("nhahoc"),
                            lh.getString("sttTang"),
                            lh.getString("loaiPhong"),
                            lh.getString("idHK"),
                            lh.getString("msHK"),
                            lh.getString("hocky"),
                            lh.getString("namhoc"),
                            lh.getString("thoigianBD"),
                            lh.getString("thoigianKT"),
                            lh.getString("tgbd"),
                            lh.getString("tgkt"),
                            lh.getString("tenDvi"),
                            lh.getString("timenow")
                            ));
                }
                LopHocAdapter listadapter= new LopHocAdapter(
                        getApplicationContext(),
                        R.layout.row_lophoc,
                        lophocArrayList);
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
    public void navHeader(){
        txtuser= findViewById(R.id.txtname);
        txtemail=findViewById(R.id.txtmail);
        shared= getSharedPreferences("canbo", Context.MODE_PRIVATE);

        String username = shared.getString("hotenCB", "");
        String email = shared.getString("email", "");
        txtemail.setText(email);
        txtuser.setText(username);
    }
    //doudlick to exit
    @Override
    public void onBackPressed()
    {

        //doubclick to exit
        if (backPressedTime + 2000 > System.currentTimeMillis()) {
            super.onBackPressed();
            return;
        } else {
             Toasty.warning(getBaseContext(), "Nhấn một lần nữa để thoát!", Toast.LENGTH_SHORT, true).show();

        }
        backPressedTime = System.currentTimeMillis();
//    DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
//        if (drawer.isDrawerOpen(GravityCompat.START)) {
//            drawer.closeDrawer(GravityCompat.START);
//        } else {
//            super.onBackPressed();
//        }
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.\
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            //Đổ dữ liệu ra lisview thoikhoabieu
            SharedPreferences shared= getSharedPreferences("canbo", Context.MODE_PRIVATE);
            final String idCB = shared.getString("idCB", "");

            SharedPreferences shared1= getSharedPreferences("hocky", Context.MODE_PRIVATE);
            final String hkht = shared1.getString("idHK", "");

            SharedPreferences shared2= getSharedPreferences("tuanht", Context.MODE_PRIVATE);
            final String tuan = shared2.getString("sttTuan", "");

            listView = findViewById(R.id.lvlophoc);
            lophocArrayList = new ArrayList<lophoc>();
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    new docJson().execute(url.getUrl()+"diemdanh/LophocTrongNgayCB.php?idCB="+idCB+"&sttTuan="+tuan+"&idHK="+hkht+"");
                }
            });
        }

        return super.onOptionsItemSelected(item);
    }
    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {

        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_user) {
            // Handle the camera action
            Intent intentuser =new Intent(MainActivity.this,UserActivity.class);
            startActivity(intentuser);
        } else if (id == R.id.nav_calender) {
            Intent intentuser =new Intent(MainActivity.this,LichBieuActivity.class);
            startActivity(intentuser);
        } else if (id == R.id.nav_list) {
            Intent intentkgd =new Intent(MainActivity.this,ThongKeGiangDay.class);
            intentkgd.putExtra("ngayht",dayht);
            startActivity(intentkgd);
        }  else if (id == R.id.nav_share) {
            showDialog();
        }else if (id == R.id.hpgd) {
            Intent intenthpgd =new Intent(MainActivity.this,HocPhanGiangDayActivity.class);
            startActivity(intenthpgd);
        }else if (id == R.id.nav_mail) {
            Intent it = new Intent(Intent.ACTION_SEND);
            it.putExtra(Intent.EXTRA_EMAIL, new String[]{"whitelam97@gmail.com"});
            it.putExtra(Intent.EXTRA_SUBJECT,"GÓP Ý PHÁT TRIỂN HỆ THỐNG QUẢN LÝ LỊCH BIỂU GIẢNG DẠY TRÊN NỀN TẢNG ANDROID");
            it.putExtra(Intent.EXTRA_TEXT,"Xin chào quản trị viên hệ thống! Tôi là ");
            it.setType("message/rfc822");
            startActivity(Intent.createChooser(it,"Chọn ứng dụng Mail"));
        }
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
    private void UpdateTinhtrang(String urlUpdateTTTG, final String ttrang, final String idTKB){
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        StringRequest stringRequest= new StringRequest(Request.Method.POST,urlUpdateTTTG,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        if(response.equals("success")){
                            Toast.makeText(MainActivity.this, "Điểm danh thành công!", Toast.LENGTH_LONG).show();
                        }
                        else
                            Toast.makeText(MainActivity.this, "!", Toast.LENGTH_LONG).show();
                    }
                },
                new Response.ErrorListener(){
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(MainActivity.this, "Lỗi sever"+error.toString(), Toast.LENGTH_SHORT).show();
                    }
                }
        ){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> param = new HashMap<>();
                param.put("tinhtrang",ttrang);
                param.put("idTKB",idTKB);
                return param;
            }
        };
        requestQueue.add(stringRequest);
    }
    public void showDialog(){
        dialog = new Dialog(MainActivity.this);
        dialog.setContentView(R.layout.dialog_gioithieuapp);
        ImageButton imageButton = dialog.findViewById(R.id.ibtnback);
        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
        dialog.show();
        Window window = dialog.getWindow();
        window.setLayout(LinearLayout.LayoutParams.FILL_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
    }
}

package com.example.qlsv2;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

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

import es.dmoral.toasty.Toasty;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    TextView txtuser;
    TextView txtemail,txttuanht;
    SharedPreferences shared;


    ListView listView;
    ArrayList<lophoc> lophocArrayList;
    com.example.qlsv2.Class.url url= new url();

    private long backPressedTime;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar1);
        setSupportActionBar(toolbar);
//        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
//        fab.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
//            }
//        });
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
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
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        //Đổ dữ liệu ra lisview thoikhoabieu
        SharedPreferences shared= getSharedPreferences("canbo", Context.MODE_PRIVATE);
        final String idCB = shared.getString("idCB", "");
        SharedPreferences shared1= getSharedPreferences("hocky", Context.MODE_PRIVATE);
        final String hkht = shared1.getString("idHK", "");

        SharedPreferences shared2= getSharedPreferences("tuanht", Context.MODE_PRIVATE);
        final String tuan = shared2.getString("sttTuan", "");
        final String tuanhtai = shared2.getString("tuanht", "");
        SharedPreferences shared3= getSharedPreferences("mondaytosundaynow", Context.MODE_PRIVATE);
        final String monday = shared3.getString("monday", "");
        final String sunday = shared3.getString("sunday", "");
        final String dayht = shared3.getString("ngayht", "");

        //set textview ngày tuan hien tai
        txttuanht=findViewById(R.id.txttuanht);
        txttuanht.setText("Ngày "+dayht+", Tuần "+tuanhtai+" ("+monday+" - "+sunday+")");


        listView = findViewById(R.id.lvlophoc);
        lophocArrayList = new ArrayList<lophoc>();
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                new docJson().execute(url.getUrl()+"diemdanh/select_tkb_ngayhientai.php?idCB="+idCB+"&sttTuan="+tuan+"&idHK="+hkht+"");
            }
        });
        //click listview
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String idlophp= lophocArrayList.get(position).getIdlopHP();
                String idtkb= lophocArrayList.get(position).getIdTKB();
                String stttuan= lophocArrayList.get(position).getSttTuan();

                SharedPreferences sharedclick = getSharedPreferences("tkbclick", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedclick.edit();
                editor.putString("idlophp",idlophp);
                editor.putString("idtkb",idtkb);
                editor.putString("stttuan",stttuan);
                editor.commit();

                String pattern = "HH:mm";
                SimpleDateFormat sdf = new SimpleDateFormat(pattern);
                Date now = new Date();
                String t= sdf.format(now);
                try {
                    Date star = sdf.parse(lophocArrayList.get(position).getTgbd());
                    Date end = sdf.parse(lophocArrayList.get(position).getTgkt());
                    Date ht =sdf.parse(t);
                    if(ht.after(star)&&ht.before(end)) {
                        Intent intendiemdanh =new Intent(MainActivity.this,DiemDanhActivity.class);
                        startActivity(intendiemdanh);
                    }
                } catch (ParseException e){
                    e.printStackTrace();
                }
            }
        });

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
                            lh.getString("tgkt")
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
    public void onBackPressed() {

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
            return true;
        }
        if (id == R.id.action_view) {
            return true;
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
            startActivity(intentkgd);
        }  else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }else if (id == R.id.hpgd) {
            Intent intenthpgd =new Intent(MainActivity.this,HocPhanGiangDayActivity.class);
            startActivity(intenthpgd);
        }
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

}
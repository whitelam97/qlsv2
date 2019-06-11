package com.example.qlsv2;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
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
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.Spinner;
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

public class Main2Activity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    TextView txtuser;
    TextView txtemail,txttuanht;
    SharedPreferences shared;
    private long backPressedTime;

    Spinner spntinhtrang;
    ListView listView;
    ArrayList<lophoc> lophocArrayList;
    ArrayList<lophoc> lophocArrayListtt;

    com.example.qlsv2.Class.url url= new url();

    String arrtinhtrang[]={
            "Tất cả",
            "Chưa mở",
            "Chưa điểm danh",
            "Đã điểm danh",
            "Đã khóa"
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
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

        //Đổ dữ liệu ra listview
        //Đổ dữ liệu ra lisview thoikhoabieu
        SharedPreferences shared1= getSharedPreferences("hocky", Context.MODE_PRIVATE);
        final String hkht = shared1.getString("idHK", "");
        SharedPreferences shared2= getSharedPreferences("tuanht", Context.MODE_PRIVATE);
        final String tuan = shared2.getString("sttTuan", "");
        final String tuanhtai = shared2.getString("tuanht", "");
        SharedPreferences shared3= getSharedPreferences("mondaytosundaynow", Context.MODE_PRIVATE);
        final String monday = shared3.getString("monday", "");
        final String sunday = shared3.getString("sunday", "");
        final String dayht = shared3.getString("ngayht", "");

        //set textview tuan hien tai
        txttuanht=findViewById(R.id.txttuanht);
        txttuanht.setText("Ngày "+dayht+", tuần "+tuanhtai+" ("+monday+" - "+sunday+")");

        listView = findViewById(R.id.lvlophoc);
        lophocArrayList = new ArrayList<lophoc>();
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                new Main2Activity.docJson().execute(url.getUrl()+"diemdanh/LophocTrongNgayTT.php?sttTuan="+tuan+"&idHK="+hkht+"");
            }
        });



        //load Spinner tinh trang
         spntinhtrang= findViewById(R.id.spntinhtrang);
        ArrayAdapter<String> adapter=new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item,arrtinhtrang);
        adapter.setDropDownViewResource(android.R.layout.simple_list_item_single_choice);
        spntinhtrang.setAdapter(adapter);


        //load lại list view đã loc qua spinner tinh trang
        lophocArrayListtt = new ArrayList<lophoc>();
        spntinhtrang.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                switch (i){
                    case 0:{
                        lophocArrayListtt.clear();
                        Loctinhtrang(lophocArrayList,lophocArrayListtt,"6");
                    }break;
                    case 1:{
                        lophocArrayListtt.clear();
                        Loctinhtrang(lophocArrayList,lophocArrayListtt,"-1");
                    }break;
                    case 2:{
                        lophocArrayListtt.clear();
                        Loctinhtrang(lophocArrayList,lophocArrayListtt,"0");

                    }break;
                    case 3:{
                        lophocArrayListtt.clear();
                        Loctinhtrang(lophocArrayList,lophocArrayListtt,"1");

                    }break;
                    case 4:{
                        lophocArrayListtt.clear();
                        Loctinhtrang(lophocArrayList,lophocArrayListtt,"2");
                    }break;
                }

            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        //click item listview
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
                        Intent intendiemdanh =new Intent(Main2Activity.this,DiemDanhActivity.class);
                        startActivity(intendiemdanh);
                    }
                } catch (ParseException e){
                    e.printStackTrace();
                }
            }
        });



    }
    public void LoadListview(){
        //Đổ dữ liệu ra lisview thoikhoabieu
        SharedPreferences shared1= getSharedPreferences("hocky", Context.MODE_PRIVATE);
        final String hkht = shared1.getString("idHK", "");
        SharedPreferences shared2= getSharedPreferences("tuanht", Context.MODE_PRIVATE);
        final String tuan = shared2.getString("sttTuan", "");
        final String tuanhtai = shared2.getString("tuanht", "");
        SharedPreferences shared3= getSharedPreferences("mondaytosundaynow", Context.MODE_PRIVATE);
        final String monday = shared3.getString("monday", "");
        final String sunday = shared3.getString("sunday", "");
        final String dayht = shared3.getString("ngayht", "");

        //set textview tuan hien tai
        txttuanht=findViewById(R.id.txttuanht);
        txttuanht.setText("Ngày "+dayht+", tuần "+tuanhtai+" ("+monday+" - "+sunday+")");

        listView = findViewById(R.id.lvlophoc);
        lophocArrayList = new ArrayList<lophoc>();
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                new Main2Activity.docJson().execute(url.getUrl()+"diemdanh/LophocTrongNgayTT.php?sttTuan="+tuan+"&idHK="+hkht+"");
            }
        });

    }
    public  void Loctinhtrang(ArrayList<lophoc> Loparraylist, ArrayList<lophoc> LopttArr, String k){
        for (int j =0; j<Loparraylist.size(); j++){
            String tt= Loparraylist.get(j).getTinhtrang();
            if (tt.equals(k)){
                LopttArr.add(new lophoc(
                        Loparraylist.get(j).getIdTKB(),
                        Loparraylist.get(j).getSttTuan(),
                        Loparraylist.get(j).getThu(),
                        Loparraylist.get(j).getTietBD(),
                        Loparraylist.get(j).getSotiet(),
                        Loparraylist.get(j).getDaybu(),
                        Loparraylist.get(j).getIdlopHP(),
                        Loparraylist.get(j).getIdPhong(),
                        Loparraylist.get(j).getTinhtrang(),
                        Loparraylist.get(j).getIdCB(),
                        Loparraylist.get(j).getThoigiandiemdanh(),
                        Loparraylist.get(j).getMsCB(),
                        Loparraylist.get(j).getHotenCB(),
                        Loparraylist.get(j).getIdHP(),
                        Loparraylist.get(j).getMslopHP(),
                        Loparraylist.get(j).getTenlopHP(),
                        Loparraylist.get(j).getLoailopHP(),
                        Loparraylist.get(j).getSoSV(),
                        Loparraylist.get(j).getTuanhoc(),
                        Loparraylist.get(j).getMsPhong(),
                        Loparraylist.get(j).getTenPhong(),
                        Loparraylist.get(j).getNhahoc(),
                        Loparraylist.get(j).getSttTang(),
                        Loparraylist.get(j).getLoaiPhong(),
                        Loparraylist.get(j).getIdHK(),
                        Loparraylist.get(j).getMsHK(),
                        Loparraylist.get(j).getHocky(),
                        Loparraylist.get(j).getNamhoc(),
                        Loparraylist.get(j).getThoigianBD(),
                        Loparraylist.get(j).getThoigianKT(),
                        Loparraylist.get(j).getTgbd(),
                        Loparraylist.get(j).getTgkt(),
                        Loparraylist.get(j).getTenDvi()
                        ));
            }
            if (k.equals("6")){
                LopttArr.add(new lophoc(
                        Loparraylist.get(j).getIdTKB(),
                        Loparraylist.get(j).getSttTuan(),
                        Loparraylist.get(j).getThu(),
                        Loparraylist.get(j).getTietBD(),
                        Loparraylist.get(j).getSotiet(),
                        Loparraylist.get(j).getDaybu(),
                        Loparraylist.get(j).getIdlopHP(),
                        Loparraylist.get(j).getIdPhong(),
                        Loparraylist.get(j).getTinhtrang(),
                        Loparraylist.get(j).getIdCB(),
                        Loparraylist.get(j).getThoigiandiemdanh(),
                        Loparraylist.get(j).getMsCB(),
                        Loparraylist.get(j).getHotenCB(),
                        Loparraylist.get(j).getIdHP(),
                        Loparraylist.get(j).getMslopHP(),
                        Loparraylist.get(j).getTenlopHP(),
                        Loparraylist.get(j).getLoailopHP(),
                        Loparraylist.get(j).getSoSV(),
                        Loparraylist.get(j).getTuanhoc(),
                        Loparraylist.get(j).getMsPhong(),
                        Loparraylist.get(j).getTenPhong(),
                        Loparraylist.get(j).getNhahoc(),
                        Loparraylist.get(j).getSttTang(),
                        Loparraylist.get(j).getLoaiPhong(),
                        Loparraylist.get(j).getIdHK(),
                        Loparraylist.get(j).getMsHK(),
                        Loparraylist.get(j).getHocky(),
                        Loparraylist.get(j).getNamhoc(),
                        Loparraylist.get(j).getThoigianBD(),
                        Loparraylist.get(j).getThoigianKT(),
                        Loparraylist.get(j).getTgbd(),
                        Loparraylist.get(j).getTgkt(),
                        Loparraylist.get(j).getTenDvi()
                ));
            }
        }
        LopHocAdapter listadapternew= new LopHocAdapter(getApplicationContext(), R.layout.row_lophoc, LopttArr);
        listView.setAdapter(listadapternew);

    }
    //đọc json load du liệu lên lisview
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
                            lh.getString("tenDvi")

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
    @Override
    public void onBackPressed() {
        if (backPressedTime + 2000 > System.currentTimeMillis()) {
            super.onBackPressed();
            return;
        } else {
            Toasty.warning(getBaseContext(), "Press back again to exit", Toast.LENGTH_SHORT, true).show();

        }
        backPressedTime = System.currentTimeMillis();

//        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
//        if (drawer.isDrawerOpen(GravityCompat.START)) {
//            drawer.closeDrawer(GravityCompat.START);
//        } else {
//            super.onBackPressed();
//        }
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main2, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        //noinspection SimplifiableIfStatement
        if (id == R.id.action_find) {
            showAlertDialog();
            return true;
        }else
            if (id == R.id.action_reset) {
                    LoadListview();
                    spntinhtrang.setSelection(0);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
    public void showAlertDialog(){
        Dialog dialog = new Dialog(Main2Activity.this);
        dialog.setContentView(R.layout.dialog_thanhtra_boloc);

        RadioButton raball= findViewById(R.id.rbkall);
        RadioButton rablt= findViewById(R.id.rablt);
        RadioButton rabth= findViewById(R.id.rabth);


        Spinner spnnhahoc =findViewById(R.id.spnnhahoc);
        Spinner spntang =findViewById(R.id.spntanghoc);
        Spinner spnkhoa =findViewById(R.id.spnkhoa);
        Spinner spntietbd =findViewById(R.id.spntietbd);

        ArrayList<String> nhahocspnarraylist;

        for (int i=0;i<lophocArrayList.size();i++){
            String nha= lophocArrayList.get(i).getNhahoc();

        }





        dialog.show();
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_user1) {
            // Handle the camera action
            Intent intentuser =new Intent(Main2Activity.this,UserActivity.class);
            startActivity(intentuser);
        } else if (id == R.id.nav_calender1) {
            Intent intentdhdd =new Intent(Main2Activity.this,TheoDoiHDD.class);
            startActivity(intentdhdd);
        } else if (id == R.id.nav_list1) {
            Intent intentqlgg =new Intent(Main2Activity.this,QuanLyGioGiang.class);
            startActivity(intentqlgg);
        } else if (id == R.id.nav_test1) {
            Intent intenthtkt=new Intent(Main2Activity.this,ThanhTra_KiemTra.class);
            startActivity(intenthtkt);

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

}

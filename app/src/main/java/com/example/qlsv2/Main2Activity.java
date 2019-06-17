package com.example.qlsv2;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.constraint.Constraints;
import android.view.Gravity;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
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
import com.example.qlsv2.Adapter.tkb_tuan_Adapter;
import com.example.qlsv2.Class.lophoc;
import com.example.qlsv2.Class.tkb_tuan;
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


import es.dmoral.toasty.Toasty;

public class Main2Activity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    com.example.qlsv2.Class.url url= new url();
    TextView txtuser;
    TextView txtemail,txttuanht;
    SharedPreferences shared;
    private long backPressedTime;

    Spinner spntinhtrang;
    ListView listView;
    ArrayList<lophoc> lophocArrayList;
    ArrayList<lophoc> lophocArrayListtt;
    ArrayList<lophoc> lophocBolocArrayListtt;

    RadioGroup rdbgroup;
    ArrayList<String> nhahocspnarraylist,tanghocArrayList,khoaArrayList,tietbdArrayList;
    RadioButton raball,rablt,rabth;
    Spinner spnnhahoc,spntang,spnkhoa,spntietbd;
    Button btntim;

    LopHocAdapter listadapter;
    String check;

    String arrtinhtrang[]={
            "Tất cả",
            "Chưa mở",
            "Chưa điểm danh",
            "Đã điểm danh",
            "Đã khóa"
    };
    String urlupdatedd=url.getUrl()+"diemdanh/SetTinhTrangTGTKB.php";


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
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        //Đổ dữ liệu ra listview
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

//        lophocArrayList = new ArrayList<lophoc>();
//        runOnUiThread(new Runnable() {
//            @Override
//            public void run() {
//                new Main2Activity.docJson().execute(url.getUrl()+"diemdanh/LophocTrongNgayTT.php?sttTuan="+tuan+"&idHK="+hkht+"");
//            }
//        });
        LoadListview();

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
                        //all
                        lophocArrayListtt.clear();
                        Loctinhtrang(lophocArrayList,lophocArrayListtt,"6");
                        break;
                    }
                    case 1:{
                        //chua mo
                        lophocArrayListtt.clear();
                        Loctinhtrang(lophocArrayList,lophocArrayListtt,"-1");
                        break;
                    }
                    case 2:{
                        //chua diem danh
                        lophocArrayListtt.clear();
                        Loctinhtrang(lophocArrayList,lophocArrayListtt,"0");

                    } break;
                    case 3:{
                        // da diem danh
                        lophocArrayListtt.clear();
                        Loctinhtrang(lophocArrayList,lophocArrayListtt,"1");
                        break;
                    }
                    case 4:{
                        //da khoa
                        lophocArrayListtt.clear();
                        Loctinhtrang(lophocArrayList,lophocArrayListtt,"2");
                        break;
                    }
                }
                listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
                    @Override
                    public boolean onItemLongClick(AdapterView<?> adapterView, View view, int position, long l) {
                        String tinhtr= lophocArrayListtt.get(position).getTinhtrang();
                        String idtkb= lophocArrayListtt.get(position).getIdTKB();
                        Toast.makeText(Main2Activity.this, "Cập nhật thành công!", Toast.LENGTH_SHORT).show();
                        UpdateTinhtrangTGDiemDanh(urlupdatedd,tinhtr,idtkb);
                        lophocArrayListtt.remove(position);
                        listadapter.notifyDataSetChanged();
                        LoadListview();
                        return false;
                    }
                });
            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                LoadListview();
            }
        });

        lophocBolocArrayListtt = new ArrayList<lophoc>();
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
//                LopHocAdapter listadapter= new LopHocAdapter(getApplicationContext(), R.layout.row_lophoc, lophocArrayList);
//                listView.setAdapter(listadapter);

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
    public  void Loctinhtrang(ArrayList<lophoc> Loparraylist, ArrayList<lophoc> LopttArr,final String k){
        for (int j =0; j<Loparraylist.size(); j++){
            String tt = Loparraylist.get(j).getTinhtrang();
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
        listadapter= new LopHocAdapter(getApplicationContext(), R.layout.row_lophoc, LopttArr);
        listView.setAdapter(listadapter);
    }
    public  void Boloc(ArrayList<lophoc> Loparraylist, ArrayList<lophoc> LopttArr,final String loailop,
                       final String nha,final String tang, final String khoa,final String tietbd){
        LopttArr.clear();
        for (int j =0; j<Loparraylist.size(); j++){
            String loai = Loparraylist.get(j).getLoailopHP();
            String nh = Loparraylist.get(j).getNhahoc();
            String tag = Loparraylist.get(j).getSttTang();
            String kh = Loparraylist.get(j).getTenDvi();
            String tbd = Loparraylist.get(j).getTietBD();
            if (loai.equals(loailop)&&nh.equals(nha)&&tag.equals(tang)&&kh.equals(khoa)&&tbd.equals(tietbd)){
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
        listadapter= new LopHocAdapter(getApplicationContext(), R.layout.row_lophoc, LopttArr);
        listView.setAdapter(listadapter);
    }
    public  void Bolockoloailophp(ArrayList<lophoc> Loparraylist, ArrayList<lophoc> LopttArr,
                                  final String nha,final String tang, final String khoa,final String tietbd){
        LopttArr.clear();
        for (int j =0; j<Loparraylist.size(); j++){
            String nh = Loparraylist.get(j).getNhahoc();
            String tag = Loparraylist.get(j).getSttTang();
            String kh = Loparraylist.get(j).getTenDvi();
            String tbd = Loparraylist.get(j).getTietBD();
            if (nh.equals(nha)&&tag.equals(tang)&&kh.equals(khoa)&&tbd.equals(tietbd)){
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
        listadapter= new LopHocAdapter(getApplicationContext(), R.layout.row_lophoc, LopttArr);
        listView.setAdapter(listadapter);
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
        }
        else
            if (id == R.id.action_reset) {
//                lophocArrayList = new ArrayList<lophoc>();
                LoadListview();
//                lophocArrayListtt = new ArrayList<lophoc>();
//               Loctinhtrang(lophocArrayList,lophocArrayListtt,"6");
//                spntinhtrang.setSelection(0);

                return true;
        }

        return super.onOptionsItemSelected(item);
    }
    public void showAlertDialog(){
        Dialog dialog = new Dialog(Main2Activity.this);
        dialog.setContentView(R.layout.dialog_thanhtra_boloc);
        rdbgroup= dialog.findViewById(R.id.rabgroup);
        raball= dialog.findViewById(R.id.rbkall);
        rablt= dialog.findViewById(R.id.rablt);
        rabth= dialog.findViewById(R.id.rabth);
        spnnhahoc =dialog.findViewById(R.id.spnnhahoc);
        spntang =dialog.findViewById(R.id.spntanghoc);
        spnkhoa =dialog.findViewById(R.id.spnkhoa);
        spntietbd =dialog.findViewById(R.id.spntietbd);


        dialog.getWindow().setGravity(Gravity.BOTTOM);
        dialog.getWindow().setLayout(Constraints.LayoutParams.MATCH_PARENT, Constraints.LayoutParams.WRAP_CONTENT);
        //spinner nha hoc
        nhahocspnarraylist=new ArrayList<String>();
        try {
            for (int i = 0; i < lophocArrayListtt.size(); i++) {
                if (!nhahocspnarraylist.contains(lophocArrayListtt.get(i).getNhahoc())) {
                    String st=lophocArrayListtt.get(i).getNhahoc();
                    nhahocspnarraylist.add(st);
                }
            }
            if (nhahocspnarraylist.size()==0){
                nhahocspnarraylist.add("");
            }
            ArrayAdapter<String> adapternhahoc = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, nhahocspnarraylist);
            spnnhahoc.setAdapter(adapternhahoc);
            spnnhahoc.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> adapterView, View view, int item, long l) {
                        //Spinner tang hoc
                        tanghocArrayList = new ArrayList<String>();
                        try {
                            for (int j = 0; j < lophocArrayListtt.size(); j++) {
                                if (!tanghocArrayList.contains(lophocArrayListtt.get(j).getSttTang())&&spnnhahoc.getSelectedItem().toString().equals(lophocArrayListtt.get(j).getNhahoc())) {
                                    String st = lophocArrayListtt.get(j).getSttTang();
                                    tanghocArrayList.add(st);
                                }
                            }
                            if (tanghocArrayList.size() == 0) {
                                tanghocArrayList.add("");
                            }
                            ArrayAdapter<String> adaptertang = new ArrayAdapter<>(Main2Activity.this, android.R.layout.simple_spinner_item, tanghocArrayList);
                            spntang.setAdapter(adaptertang);
                            spntang.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                                @Override
                                public void onItemSelected(AdapterView<?> adapterView, View view, int item, long l) {
                                    //Spinner khoa
                                    khoaArrayList=new ArrayList<String>();
                                    try {
                                        for (int i = 0; i < lophocArrayListtt.size(); i++) {
                                            if (!khoaArrayList.contains(lophocArrayListtt.get(i).getTenDvi())) {
                                                String st=lophocArrayListtt.get(i).getTenDvi();
                                                khoaArrayList.add(st);
                                            }
                                        }
                                        if (khoaArrayList.size()==0){
                                            khoaArrayList.add("");
                                        }
                                        ArrayAdapter<String> adaptekhoa = new ArrayAdapter<>(Main2Activity.this, android.R.layout.simple_spinner_item, khoaArrayList);
                                        spnkhoa.setAdapter(adaptekhoa);
                                    }catch (Exception e ){
                                        txttuanht.setText(e.toString());
                                    }
                                    spnkhoa.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                                        @Override
                                        public void onItemSelected(AdapterView<?> adapterView, View view, int item, long l) {
                                            tietbdArrayList=new ArrayList<String>();
                                            try {
                                                for (int i = 0; i < lophocArrayListtt.size(); i++) {
                                                    if (!tietbdArrayList.contains(lophocArrayListtt.get(i).getTietBD())) {
                                                        String st=lophocArrayListtt.get(i).getTietBD();
                                                        tietbdArrayList.add(st);
                                                    }
                                                }
                                                if (tietbdArrayList.size()==0){
                                                    tietbdArrayList.add("");
                                                }
                                                ArrayAdapter<String> adaptetietbd = new ArrayAdapter<>(Main2Activity.this, android.R.layout.simple_spinner_item, tietbdArrayList);
                                                spntietbd.setAdapter(adaptetietbd);
                                            }catch (Exception e ){
                                                txttuanht.setText(e.toString());
                                            }
                                            spntietbd.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                                                @Override
                                                public void onItemSelected(AdapterView<?> adapterView, View view, int item, long l) {
                                                    String nhahoc= spnnhahoc.getSelectedItem().toString();
                                                    String stttang= spntang.getSelectedItem().toString();
                                                    String donvi= spnkhoa.getSelectedItem().toString();
                                                    String tietbd= spntietbd.getSelectedItem().toString();
                                                    if(raball.isChecked()){
                                                        check="all";
                                                    }else
                                                    if(rablt.isChecked()){
                                                        check="LT";
                                                    }else
                                                    if(rabth.isChecked()){
                                                        check="TH";
                                                    }
                                                    if(check.equals("all")){
                                                        Bolockoloailophp(lophocArrayListtt,lophocBolocArrayListtt,nhahoc,stttang,donvi,tietbd);
                                                    }
                                                    else{
                                                        Boloc(lophocArrayListtt,lophocBolocArrayListtt,check,nhahoc,stttang,donvi,tietbd);
                                                    }
                                                    rdbgroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
                                                        @Override
                                                        public void onCheckedChanged(RadioGroup radioGroup, int i) {
                                                            String nhahoc= spnnhahoc.getSelectedItem().toString();
                                                            String stttang= spntang.getSelectedItem().toString();
                                                            String donvi= spnkhoa.getSelectedItem().toString();
                                                            String tietbd= spntietbd.getSelectedItem().toString();
                                                            if(raball.isChecked()){
                                                                check="all";
                                                            }else
                                                            if(rablt.isChecked()){
                                                                check="LT";
                                                            }else
                                                            if(rabth.isChecked()){
                                                                check="TH";
                                                            }
                                                            if(check.equals("all")){
                                                                Bolockoloailophp(lophocArrayListtt,lophocBolocArrayListtt,nhahoc,stttang,donvi,tietbd);
                                                            }
                                                            else{
                                                                Boloc(lophocArrayListtt,lophocBolocArrayListtt,check,nhahoc,stttang,donvi,tietbd);
                                                            }
                                                        }
                                                    });


                                                    listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
                                                        @Override
                                                        public boolean onItemLongClick(AdapterView<?> adapterView, View view, int position, long l) {
                                                            String tinhtr= lophocBolocArrayListtt.get(position).getTinhtrang();
                                                            String idtkb= lophocBolocArrayListtt.get(position).getIdTKB();
                                                            Toast.makeText(Main2Activity.this, "Cập nhật thành công!", Toast.LENGTH_SHORT).show();
                                                            UpdateTinhtrangTGDiemDanh(urlupdatedd,tinhtr,idtkb);
                                                            lophocBolocArrayListtt.remove(position);
                                                            listadapter.notifyDataSetChanged();
                                                            LoadListview();
                                                            return false;
                                                        }
                                                    });

                                                }
                                                @Override
                                                public void onNothingSelected(AdapterView<?> adapterView) {
                                                    String nhahoc= spnnhahoc.getSelectedItem().toString();
                                                    String stttang= spntang.getSelectedItem().toString();
                                                    String donvi= spnkhoa.getSelectedItem().toString();
                                                    String tietbd= "1";
                                                    Boloc(lophocArrayListtt,lophocBolocArrayListtt,"all",nhahoc,stttang,donvi,tietbd);
                                                }
                                            });
                                        }
                                        @Override
                                        public void onNothingSelected(AdapterView<?> adapterView) {
                                            tietbdArrayList=new ArrayList<String>();
                                            try {
                                                for (int i = 0; i < lophocArrayListtt.size(); i++) {
                                                    if (!tietbdArrayList.contains(lophocArrayListtt.get(i).getTietBD())) {
                                                        String st=lophocArrayListtt.get(i).getTietBD();
                                                        tietbdArrayList.add(st);
                                                    }
                                                }
                                                if (tietbdArrayList.size()==0){
                                                    tietbdArrayList.add("");
                                                }
                                                ArrayAdapter<String> adaptetietbd = new ArrayAdapter<>(Main2Activity.this, android.R.layout.simple_spinner_item, tietbdArrayList);
                                                spntietbd.setAdapter(adaptetietbd);
                                            }catch (Exception e ){
                                                txttuanht.setText(e.toString());
                                            }
                                        }
                                    });

                                }
                                @Override
                                public void onNothingSelected(AdapterView<?> adapterView) {
                                    //Spinner khoa
                                    khoaArrayList=new ArrayList<String>();
                                    try {
                                        for (int i = 0; i < lophocArrayListtt.size(); i++) {
                                            if (!khoaArrayList.contains(lophocArrayListtt.get(i).getTenDvi())) {
                                                String st=lophocArrayListtt.get(i).getTenDvi();
                                                khoaArrayList.add(st);
                                            }
                                        }
                                        if (khoaArrayList.size()==0){
                                            khoaArrayList.add("");
                                        }
                                        ArrayAdapter<String> adaptekhoa = new ArrayAdapter<>(Main2Activity.this, android.R.layout.simple_spinner_item, khoaArrayList);
                                        spnkhoa.setAdapter(adaptekhoa);
                                    }catch (Exception e ){
                                        txttuanht.setText(e.toString());
                                    }
                                }
                            });
                        } catch (Exception e) {
                            txttuanht.setText(e.toString());
                        }
                    }
                @Override
                public void onNothingSelected(AdapterView<?> adapterView) {
                    spnnhahoc.setClickable(false);
                    tanghocArrayList=new ArrayList<String>();
                    ArrayAdapter<String> adaptertang = new ArrayAdapter<>(Main2Activity.this, android.R.layout.simple_spinner_item, tanghocArrayList);
                    spntang.setAdapter(adaptertang);
                }
            });
        }catch (Exception e ){
            txttuanht.setText(e.toString());
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
        }else if (id == R.id.nav_list1) {
            Intent intentqlgg =new Intent(Main2Activity.this,QuanLyGioGiang.class);
            startActivity(intentqlgg);
        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }


    private void UpdateTinhtrangTGDiemDanh(String urlUpdateTTTG,final String tinhtrang, final  String idtkb){
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        StringRequest stringRequest= new StringRequest(Request.Method.POST,urlUpdateTTTG,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        if(response.equals("success")){
                         Toast.makeText(Main2Activity.this, " thành công!", Toast.LENGTH_LONG).show();
                        }
//                       else
//                           Toast.makeText(Main2Activity.this, " lỗi!", Toast.LENGTH_SHORT).show();
                    }
                },
                new Response.ErrorListener(){
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(Main2Activity.this, "Lỗi sever"+error.toString(), Toast.LENGTH_SHORT).show();
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
                if (tinhtrang.equals("2")){
                    param.put("tinhtrang","0");
                    param.put("thoigiandiemdanh"," ");
                    param.put("idTKB",idtkb);
                }
                if (tinhtrang.equals("0")){
                    param.put("tinhtrang","1");
                    param.put("thoigiandiemdanh",t);
                    param.put("idTKB",idtkb);
                }

                return param;
            }
        };
        requestQueue.add(stringRequest);
    }

}

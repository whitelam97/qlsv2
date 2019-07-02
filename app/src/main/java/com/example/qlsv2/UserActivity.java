package com.example.qlsv2;

import android.content.Context;
import android.content.SharedPreferences;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.TextView;

public class UserActivity extends AppCompatActivity {

    TextView txtten,txtid,txtgt,txtdc,txtns,txtdt,txtemail;
    SharedPreferences shared;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user);

        anhxa();


        shared= getSharedPreferences("canbo", Context.MODE_PRIVATE);
        String idCB = shared.getString("chucvu","");
        String hotenCB = shared.getString("hotenCB", "");
        String email = shared.getString("email", "");
        String sdt = shared.getString("sdt","");
        String gioitinh = shared.getString("gioitinh","");
        String ngaysinh = shared.getString("ngaysinh","");
        String tenDvi = shared.getString("tenDvi","");


        int gt=Integer.parseInt(gioitinh);
        if(gt==1)
            txtgt.setText("Nữ");
        else txtgt.setText("Nam");

        txtns.setText(ngaysinh);
        txtid.setText(idCB);
        txtemail.setText(email);
        txtten.setText(hotenCB);
        txtdt.setText(sdt);
        txtdc.setText(tenDvi);

    if(getSupportActionBar()!=null){
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
    }


    }
    private void anhxa(){
        txtten =findViewById(R.id.txtten);
        txtid =findViewById(R.id.txtid);
        txtgt =findViewById(R.id.txtgioitinh);
        txtdc =findViewById(R.id.txtdiachi);
        txtns =findViewById(R.id.txtngaysinh);
        txtdt =findViewById(R.id.txtdienthoai);
        txtemail =findViewById(R.id.txtemail);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id=item.getItemId();
        if (id==android.R.id.home){
            this.finish();
        }
        return super.onOptionsItemSelected(item);
    }



}

package com.example.qlsv2.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.qlsv2.Class.url;
import com.example.qlsv2.R;

import java.util.HashMap;
import java.util.Map;

import es.dmoral.toasty.Toasty;

public class DoiMatKhau extends AppCompatActivity {

    EditText edtmk, edtnlmk;
    Button btnnhaplai, btndoimk;
    String matkhau, matkhaunhaplai;
    com.example.qlsv2.Class.url url= new url();
    String urlupdateMK=url.getUrl()+"diemdanh/DoiMatKhau.php";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_doi_mat_khau);

        //nut back
        if(getSupportActionBar()!=null){
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }
        edtmk= findViewById(R.id.edtmk);
        edtnlmk= findViewById(R.id.edtnlmk);
        btndoimk=findViewById(R.id.btndoimk);
        btnnhaplai= findViewById(R.id.btnnhaplai);

        SharedPreferences shared= getSharedPreferences("canbo", Context.MODE_PRIVATE);
        final String idCB = shared.getString("idCB","");

        btnnhaplai.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                edtnlmk.setText("");
                edtmk.setText("");
            }
        });

        btndoimk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //kiem tra hop le
                matkhau= edtmk.getText().toString();
                matkhaunhaplai= edtnlmk.getText().toString();

                if (!matkhau.equals("")&&!matkhaunhaplai.equals("")){
                    if (matkhaunhaplai.equals(matkhau))
                    {
                        UpdateMatKhau(urlupdateMK,idCB,matkhau);
                    }
                    else
                        Toasty.error(getBaseContext(), "Mật khẩu không giống nhau!", Toast.LENGTH_SHORT, true).show();
                }else  Toasty.error(getBaseContext(), "Mật khẩu không được để trống!", Toast.LENGTH_SHORT, true).show();

            }
        });

    }
    private void UpdateMatKhau(String urlUpdateTTTG, final String idCB, final String matkhaumoi){
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        StringRequest stringRequest= new StringRequest(Request.Method.POST,urlUpdateTTTG,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        if(response.equals("success")){
                            Toasty.info(getBaseContext(), "Cập nhật thành công!", Toast.LENGTH_SHORT, true).show();
                        }
                        else
                            Toasty.info(getBaseContext(), "Cập nhật thành công!", Toast.LENGTH_SHORT, true).show();
                    }
                },
                new Response.ErrorListener(){
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(DoiMatKhau.this, "Lỗi sever"+error.toString(), Toast.LENGTH_SHORT).show();
                    }
                }
        ){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> param = new HashMap<>();
                param.put("matkhau",matkhaumoi);
                param.put("idCB",idCB);
                return param;
            }
        };
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
}

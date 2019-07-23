package com.example.qlsv2.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
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
import com.example.qlsv2.SendMail.GMailSender;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.security.SecureRandom;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;



public class QuenMatKhau extends AppCompatActivity {
//    String GMail = "qlsvvlute2019@gmail.com"; //replace with you GMail
//    String GMailPass = "111194lam"; // replace with you GMail Password
    String GMail = "ScheduleManagementVLUTE@gmail.com"; //replace with you GMail
    String GMailPass = "fit2017@vlute"; // replace with you GMail Password
    Button btnclear, btnnhanmk;
    EditText edtmail;
    com.example.qlsv2.Class.url url= new url();
    String urlupdateMK=url.getUrl()+"diemdanh/DoiMatKhau.php";


    String str_subject="CẤP LẠI MẬT KHẨU ĐĂNG NHẬP HỆ THỒNG QUẢN LÝ LỊCH TRÌNH GIẢNG DẠY VLUTE";
    String str_message;
    String str_to;

    private static final Random RANDOM = new SecureRandom();
    private static final String ALPHABET = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz";

    // Define desired password length
    int passwordLength = 10;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quen_mat_khau);


        //nut back
        if(getSupportActionBar()!=null){
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }

        btnclear= findViewById(R.id.btnclear);
        btnnhanmk= findViewById(R.id.btnquenmk);
        edtmail= findViewById(R.id.edtmail);
        btnclear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                edtmail.setText("");
            }
        });

        btnnhanmk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                 str_to = edtmail.getText().toString();
                // Check if there are empty fields
                if (!str_to.equals("")){
                    //Check if 'To:' field is a valid email
                    if (isValidEmail(str_to)){

                        String urltuan = url.getUrl()+"diemdanh/QuenMatKhau.php?email="+str_to+"";
                        Log.i("Hiteshurl",""+urltuan);
                        RequestQueue requestQueue = Volley.newRequestQueue(QuenMatKhau.this);
                        StringRequest stringRequest = new StringRequest(Request.Method.GET, urltuan, new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                try {
                                    JSONObject jsonObject = new JSONObject(response);
                                    JSONArray jsonArray = jsonObject.getJSONArray("tuanht");
                                    JSONObject jsonObject1 = jsonArray.getJSONObject(0);
                                    String idCB = jsonObject1.getString("idCB");
                                    String msCB = jsonObject1.getString("msCB");
                                    if (!msCB.equals("Null")){
                                        edtmail.setError(null);
                                        new Handler(Looper.getMainLooper()).post(new Runnable() {
                                            @Override
                                            public void run() {
                                                Toast.makeText(QuenMatKhau.this, "Đang gửi... Vui lòng đợi", Toast.LENGTH_LONG).show();
                                            }
                                        });
                                        String password = generatePassword(passwordLength);
                                        str_message=
    "Kính chào quý Thầy/Cô, \n\nMật khẩu đăng nhập hệ thống Quản lý lịch trình giảng dạy VLUTE của quý Thầy/Cô đã được cấp lại với thông tin như sau:"+
            "\n\n\t+ Địa chỉ truy cập: http://qllb.vlute.edu.vn"+
            "\n\t+ Tên đăng nhập (MSCB): "+msCB+"\n\t+ Mật khẩu: "+password+
            "\n\nLưu ý, quý Thầy/Cô vui lòng không reply email này. Mọi chi tiết về sự cố đăng nhập xin gửi email về: "+
            "\n\n\t1. Huỳnh Minh Hiền: hienhm@vlute.edu.vn"+
            "\n\t2. Lê Thị Hoàng Yến: yenlth@vlute.edu.vn"+
            "\n\nTrân trọng kính chào,"+
            "\n\nQuản trị viên";
                                        sendEmail(str_to, str_subject, str_message);
                                        UpdateMatKhau(urlupdateMK,idCB,password);
                                    }
                                    else
                                        Toast.makeText(QuenMatKhau.this, "Tài khoản không tồn tại", Toast.LENGTH_SHORT).show();
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

                    }else{
                        new Handler(Looper.getMainLooper()).post(new Runnable() {
                            @Override
                            public void run() {
                                edtmail.setError("Địa chỉ Email không hợp lệ");
                            }
                        });
                    }
                }else{
                    Toast.makeText(QuenMatKhau.this, "There are empty fields.", Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    private void sendEmail(final String to, final String subject, final String message) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    GMailSender sender = new GMailSender(GMail, GMailPass);
                    sender.sendMail(subject,
                            message,
                            GMail,
                            to);

                    Log.w("sendEmail","Email successfully sent!");
                    new Handler(Looper.getMainLooper()).post(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(QuenMatKhau.this, "Email successfully sent!", Toast.LENGTH_LONG).show();
                            edtmail.setText("");
                        }
                    });

                } catch (final Exception e) {
                    Log.e("sendEmail", e.getMessage(), e);
                    new Handler(Looper.getMainLooper()).post(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(QuenMatKhau.this, "Email not sent. \n\n Error: " + e.getMessage(), Toast.LENGTH_LONG).show();
                        }
                    });
                }
            }

        }).start();
    }

    // Check if parameter 'emailAddress' is a valid email
    public static boolean isValidEmail(CharSequence emailAddress) {
        if (emailAddress == null) {
            return false;
        } else {
            return android.util.Patterns.EMAIL_ADDRESS.matcher(emailAddress).matches();
        }
    }
    public static String generatePassword(int length) {
        StringBuilder returnValue = new StringBuilder(length);
        for (int i = 0; i < length; i++) {
            returnValue.append(ALPHABET.charAt(RANDOM.nextInt(ALPHABET.length())));
        }
        return new String(returnValue);
    }

    private void UpdateMatKhau(String urlUpdateTTTG, final String idCB, final String matkhaumoi){
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        StringRequest stringRequest= new StringRequest(Request.Method.POST,urlUpdateTTTG,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        if(response.equals("success")){
//                            Toasty.info(getBaseContext(), "Cập nhật thành công!", Toast.LENGTH_SHORT, true).show();
                        }
//                        else
//                            Toasty.info(getBaseContext(), "Cập nhật thành công!", Toast.LENGTH_SHORT, true).show();
                    }
                },
                new Response.ErrorListener(){
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(QuenMatKhau.this, "Lỗi sever"+error.toString(), Toast.LENGTH_SHORT).show();
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
            Intent intent  = new Intent(QuenMatKhau.this, LoginActivity.class);
            startActivity(intent);
        }
        return super.onOptionsItemSelected(item);
    }

}

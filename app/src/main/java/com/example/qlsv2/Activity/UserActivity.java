package com.example.qlsv2.Activity;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.database.Cursor;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.qlsv2.R;

public class UserActivity extends AppCompatActivity {

    private static int IMG_RESULT = 1;
    String[] FILE;
    String ImageDecode;
    ImageView imageViewLoad;

    Intent intent;


    TextView txtten,txtid,txtgt,txtdc,txtns,txtdt,txtemail;
    SharedPreferences shared;
    Button btndoimk;


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


        btndoimk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(UserActivity.this,DoiMatKhau.class);
                startActivity(intent);
            }
        });


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


        imageViewLoad = (ImageView) findViewById(R.id.imageView4);

        SharedPreferences shared= getSharedPreferences("Image", Context.MODE_PRIVATE);
        final String Imagedeci = shared.getString("ImageDecode", "");
        imageViewLoad.setImageBitmap(BitmapFactory.decodeFile(Imagedeci));



    }
    private void anhxa(){
        txtten =findViewById(R.id.txtten);
        txtid =findViewById(R.id.txtid);
        txtgt =findViewById(R.id.txtgioitinh);
        txtdc =findViewById(R.id.txtdiachi);
        txtns =findViewById(R.id.txtngaysinh);
        txtdt =findViewById(R.id.txtdienthoai);
        txtemail =findViewById(R.id.txtemail);
        btndoimk = findViewById(R.id.btndoimatkhauuser);
    }
// 2 click on back
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id=item.getItemId();
        if (id==android.R.id.home){
            this.finish();
        }
        if (id == R.id.changeimage) {

            intent = new Intent(Intent.ACTION_PICK,
                    android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            try {
                ActivityCompat.requestPermissions(UserActivity.this,
                        new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                        IMG_RESULT);
                startActivityForResult(intent, IMG_RESULT);
            }catch (Exception e){
                Toast.makeText(UserActivity.this, "Bạn chưa cho phép truy cập thư mục", Toast.LENGTH_SHORT).show();
            }
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.\
        getMenuInflater().inflate(R.menu.changeimage, menu);
        return true;
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        try {
            if (requestCode == IMG_RESULT && resultCode == RESULT_OK
                    && null != data) {

                Uri URI = data.getData();
                String[] FILE = { MediaStore.Images.Media.DATA };

                Cursor cursor = getContentResolver().query(URI,
                        FILE, null, null, null);

                cursor.moveToFirst();

                int columnIndex = cursor.getColumnIndex(FILE[0]);
                ImageDecode = cursor.getString(columnIndex);
                cursor.close();

                SharedPreferences shared = getSharedPreferences("Image", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = shared.edit();
                editor.putString("ImageDecode",ImageDecode);
                editor.commit();

                imageViewLoad.setImageBitmap(BitmapFactory
                        .decodeFile(ImageDecode));

            }
        } catch (Exception e) {
            Toast.makeText(this, "Please try again", Toast.LENGTH_LONG)
                    .show();
        }

    }

}

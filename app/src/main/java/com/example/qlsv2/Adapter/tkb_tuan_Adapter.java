package com.example.qlsv2.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.qlsv2.Class.tkb_tuan;
import com.example.qlsv2.R;

import java.util.List;


public class tkb_tuan_Adapter extends ArrayAdapter<tkb_tuan> {
    public tkb_tuan_Adapter(Context context, int resource) {
        super(context, resource);
    }

    public tkb_tuan_Adapter(Context context, int resource, List<tkb_tuan> objects) {
        super(context, resource, objects);
    }
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = convertView;

        if(view==null){
            LayoutInflater inflater;
            inflater =LayoutInflater.from(getContext());
            view=inflater.inflate(R.layout.row_tkb_tuan,null);

        }
        tkb_tuan tkb_tuan =getItem(position);
        if(tkb_tuan!=null){
            //anh xa, gan gia tri
            TextView txttenHP=view.findViewById(R.id.txttenlophp);
            TextView txtphong =view.findViewById(R.id.txttenphong);
            TextView txttiet=view.findViewById(R.id.txttiettkb);
            TextView txtthu=view.findViewById(R.id.txtthu);

            ImageView imgthu= view.findViewById(R.id.imgthu);
            ImageView imgphong= view.findViewById(R.id.imgphong);
            ImageView imgtiet= view.findViewById(R.id.imgtiet);

            imgphong.setImageResource(R.drawable.ic_place_black_24dp);
            imgtiet.setImageResource(R.drawable.ic_access_time_black_24dp);
            imgthu.setImageResource(R.drawable.ic_calender);

            // xuly tiet
            int tietbd=Integer.parseInt(tkb_tuan.getTietBD());
            int sotiet=Integer.parseInt(tkb_tuan.getSotiet());
            String tiet=tkb_tuan.getTietBD();
            for(int i=1;i<sotiet;i++){
                tiet=tiet+","+(tietbd+i)+"";
            }
            txttiet.setText("Tiết "+tiet);

            int thu=Integer.parseInt(tkb_tuan.getThu());
            if (thu==8){
                txtthu.setText("Chủ nhật, tuần "+tkb_tuan.getHt());
            }else
            txtthu.setText("Thứ "+tkb_tuan.getThu()+", tuần "+tkb_tuan.getHt());

            txttenHP.setText(tkb_tuan.getTenlopHP());
            txtphong.setText(tkb_tuan.getTenPhong());
        }

        return view;
    }
}

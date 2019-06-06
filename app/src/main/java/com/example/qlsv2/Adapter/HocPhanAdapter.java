package com.example.qlsv2.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.qlsv2.Class.hocphan;
import com.example.qlsv2.R;

import java.util.List;

public class HocPhanAdapter extends ArrayAdapter<hocphan> {
    public HocPhanAdapter(Context context, int resource) {
        super(context, resource);
    }

    public HocPhanAdapter(Context context, int resource, List<hocphan> objects) {
        super(context, resource, objects);
    }
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = convertView;

        if(view==null){
            LayoutInflater inflater;
            inflater =LayoutInflater.from(getContext());
            view=inflater.inflate(R.layout.row_hocphan,null);

        }
        hocphan hocphan =getItem(position);
        if(hocphan!=null){
            //anh xa, gan gia tri
            TextView txttenHP=view.findViewById(R.id.txttenlophp);
            TextView textViewtencb =view.findViewById(R.id.txttenphong);
            TextView textViewhknh=view.findViewById(R.id.txttiettkb);
            TextView textViewbacdt =view.findViewById(R.id.txtbacdt);
            TextView textViewloaihp=view.findViewById(R.id.txtloailophp);
            TextView textViewtuanhoc=view.findViewById(R.id.txttuanoc);
            TextView textViewsosv =view.findViewById(R.id.txtsosv);


            ImageView imageViewcb= view.findViewById(R.id.imgphong);
            ImageView hknh= view.findViewById(R.id.imgtiet);
            ImageView bacdt= view.findViewById(R.id.imgbacdt);
            ImageView loaihp= view.findViewById(R.id.imgloailophp);
            ImageView tuanhoc= view.findViewById(R.id.imgtuanhoc);
            ImageView sosv= view.findViewById(R.id.imgslsv);


            imageViewcb.setImageResource(R.drawable.ic_local_offer_black_24dp);
            hknh.setImageResource(R.drawable.ic_calender);
            bacdt.setImageResource(R.drawable.ic_school_black_24dp);
            loaihp.setImageResource(R.drawable.ic_hocphangiangday_24dp);
            tuanhoc.setImageResource(R.drawable.ic_clock);
            sosv.setImageResource(R.drawable.ic_group_black_24dp);

            txttenHP.setText(hocphan.getMsHP()+" - "+ hocphan.getTenHP());
            textViewtencb.setText(hocphan.getMslopHP());
            textViewhknh.setText("Học kỳ "+hocphan.getHocky()+", năm học "+hocphan.getNamhoc());
            Integer integer =Integer.parseInt(hocphan.getBacDT());
            if (integer==1){
                textViewbacdt.setText("Đại học");
            }else textViewbacdt.setText("Cao đẳng");
            String s =hocphan.getLoailopHP();
            if (s.equals("LT"))
            textViewloaihp.setText("Lý thuyết");
            else   textViewloaihp.setText("Thực hành");

            textViewtuanhoc.setText(hocphan.getTuanhoc());
            textViewsosv.setText(hocphan.getSoSV());

        }

        return view;
    }
}

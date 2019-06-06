package com.example.qlsv2.Adapter;

import android.content.Context;

import android.support.constraint.ConstraintLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;


import com.example.qlsv2.Class.sinhvien;

import com.example.qlsv2.R;



import java.util.List;

public class SinhVienAdapter extends BaseAdapter {
    private Context context;
    private int resource;
    private List<sinhvien> arrContact;

    public SinhVienAdapter(Context context, int resource, List<sinhvien> arrContact) {
        this.context = context;
        this.resource = resource;
        this.arrContact = arrContact;
    }

    @Override
    public int getCount() {
        return arrContact.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    public boolean isChecked(int position) {
        return arrContact.get(position).isChecked();
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.row_diemdanhsinhvien, parent, false);
            viewHolder = new ViewHolder();
            viewHolder.txtstt = (TextView) convertView.findViewById(R.id.txtSTT);
            viewHolder.txttensv = (TextView) convertView.findViewById(R.id.txtTenSV);
            viewHolder.txtmssv = (TextView) convertView.findViewById(R.id.txtmssv);
            viewHolder.txtlopcn = (TextView) convertView.findViewById(R.id.txtlopCN);
            viewHolder.txtco = (TextView) convertView.findViewById(R.id.txtcohoc);
            viewHolder.txtvang = (TextView) convertView.findViewById(R.id.txtvang);
            viewHolder.phantram = (TextView) convertView.findViewById(R.id.txtphantram);

            viewHolder.imgco = (ImageView) convertView.findViewById(R.id.imgcohoc);
            viewHolder.imgvang = (ImageView) convertView.findViewById(R.id.imgvang);
            viewHolder.imgphantram = (ImageView) convertView.findViewById(R.id.imgphantram);
            viewHolder.ckbdiemdanh = (CheckBox) convertView.findViewById(R.id.cbdiemdanh);
            viewHolder.listViewsinhvien=(ListView) convertView.findViewById(R.id.lvsv);
            viewHolder.consLayout=(ConstraintLayout) convertView.findViewById(R.id.layoutdongsinhvien);

            convertView.setTag(viewHolder);

        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        sinhvien contact = arrContact.get(position);
       viewHolder.txtstt.setText(position+1+"");
        viewHolder.txttensv.setText(contact.getHotenSV());
        viewHolder.txtmssv.setText(contact.getMssv());
        viewHolder.txtlopcn.setText(contact.getMsLopCN());



            viewHolder.txtco.setText(contact.getCohoc());
            int cohoc= Integer.parseInt(contact.getCohoc());
            int tongbuoi= Integer.parseInt(contact.getTongbuoi());
            int vang = tongbuoi-cohoc;
            viewHolder.txtvang.setText(tongbuoi-cohoc+"");
            int phantram = (vang*100)/tongbuoi;
            viewHolder.phantram.setText(phantram+"%");



        viewHolder.imgco.setImageResource(R.drawable.ic_check_black_24dp);
        viewHolder.imgvang.setImageResource(R.drawable.ic_clear_black_24dp);
        viewHolder.imgphantram.setImageResource(R.drawable.ic_phantram);


        if (contact.getCheck()){
            viewHolder.ckbdiemdanh.setChecked(arrContact.get(position).isChecked());
        }
        else if (!contact.getCheck())
            viewHolder.ckbdiemdanh.setChecked(false);

        viewHolder.ckbdiemdanh.setTag(position);
        viewHolder.ckbdiemdanh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                boolean newState = !arrContact.get(position).isChecked();
                arrContact.get(position).setCheck(newState);
            }
        });

        viewHolder.ckbdiemdanh.setChecked(isChecked(position));

        return convertView;
    }


    public class ViewHolder {
        TextView txtstt,txttensv,txtmssv,txtlopcn,txtco,txtvang,phantram;
        ImageView imgco,imgvang,imgphantram;
        CheckBox ckbdiemdanh;
        ListView listViewsinhvien;
        ConstraintLayout consLayout;

    }
}

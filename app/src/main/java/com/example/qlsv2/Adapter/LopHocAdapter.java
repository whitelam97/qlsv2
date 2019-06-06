package com.example.qlsv2.Adapter;

import android.content.Context;
import android.support.constraint.ConstraintLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.example.qlsv2.R;
import com.example.qlsv2.Class.lophoc;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class LopHocAdapter extends BaseAdapter {
    private Context context;
    private int resource;
    private List<lophoc> arrContact;

    public LopHocAdapter(Context context, int resource, List<lophoc> arrContact) {
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

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.row_lophoc, parent, false);
            viewHolder = new ViewHolder();
            viewHolder.tvlop = (TextView) convertView.findViewById(R.id.txttenlophp);
            viewHolder.tvgiaovien = (TextView) convertView.findViewById(R.id.txttenphong);
            viewHolder.tvtiet = (TextView) convertView.findViewById(R.id.txtbacdt);
            viewHolder.tvphong = (TextView) convertView.findViewById(R.id.txttiettkb);
            viewHolder.tvtinhtrang = (TextView) convertView.findViewById(R.id.txtloailophp);
            viewHolder.imguser = (ImageView) convertView.findViewById(R.id.imgphong);
            viewHolder.imgplace = (ImageView) convertView.findViewById(R.id.imgtiet);
            viewHolder.imgtime = (ImageView) convertView.findViewById(R.id.imgbacdt);
            viewHolder.imgcheck = (ImageView) convertView.findViewById(R.id.imgloailophp);
            viewHolder.constraintLayout=(ConstraintLayout) convertView.findViewById(R.id.layoutdonglophoc);
            viewHolder.listView=(ListView) convertView.findViewById(R.id.lvlophoc);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        lophoc contact = arrContact.get(position);
        viewHolder.tvlop.setText(contact.getTenlopHP());
        viewHolder.tvgiaovien.setText(contact.getHotenCB());
        viewHolder.tvphong.setText(contact.getTenPhong());

        //xuly tinhtrang
        int tt=Integer.parseInt(contact.getTinhtrang());
        if (tt==-1){
            viewHolder.tvtinhtrang.setText("Chưa mở");
        }
        if (tt==1){
            viewHolder.tvtinhtrang.setText("Đã điểm danh");
        }
        if (tt==0){
            viewHolder.tvtinhtrang.setText("Chưa điểm danh");
        }
        if (tt==2){
            viewHolder.tvtinhtrang.setText("Đã khóa");
        }

        //xuly diem danh
        String pattern = "HH:mm";
        SimpleDateFormat sdf = new SimpleDateFormat(pattern);
        Date now = new Date();
        String t= sdf.format(now);
        try {
            Date star = sdf.parse(contact.getTgbd());
            Date end = sdf.parse(contact.getTgkt());
            Date ht =sdf.parse(t);

            if(ht.after(star)&&ht.before(end)) {
                viewHolder.constraintLayout.setBackgroundResource(R.drawable.bg_rowlv_red);
                if (tt!=1)  viewHolder.tvtinhtrang.setText("Chưa điểm danh");

            }
            if (star.before(ht)&&end.before(ht)) {
                viewHolder.constraintLayout.setBackgroundResource(R.drawable.bg_rowlistview_yello);
                if (tt!=1) viewHolder.tvtinhtrang.setText("Đã khóa");

            }
            if (ht.before(star)){
                viewHolder.constraintLayout.setBackgroundResource(R.drawable.bg_row_listview);
//                viewHolder.listView.getChildAt( position).setEnabled(false);
                viewHolder.tvtinhtrang.setText("Chưa mở");

            }
        } catch (ParseException e){
            e.printStackTrace();
        }


        // xuly tiet
        int tietbd=Integer.parseInt(contact.getTietBD());
        int sotiet=Integer.parseInt(contact.getSotiet());
        String tiet=contact.getTietBD();
        for(int i=1;i<sotiet;i++){
            tiet=tiet+","+(tietbd+i)+"";
        }
        viewHolder.tvtiet.setText(tiet);




//       viewHolder.tvtinhtrang.setText(tt);
        viewHolder.imguser.setImageResource(R.drawable.ic_user);
        viewHolder.imgplace.setImageResource(R.drawable.ic_place_black_24dp);
        viewHolder.imgtime.setImageResource(R.drawable.ic_access_time_black_24dp);
        viewHolder.imgcheck.setImageResource(R.drawable.ic_chat_black_24dp);


        return convertView;
    }

    public class ViewHolder {
        TextView tvlop,tvgiaovien,tvtiet,tvtinhtrang,tvphong;
        ImageView imguser,imgplace,imgtime,imgcheck;
        ConstraintLayout constraintLayout;
        ListView listView;
    }

}

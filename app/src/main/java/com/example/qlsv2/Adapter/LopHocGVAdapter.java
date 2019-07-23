package com.example.qlsv2.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import androidx.constraintlayout.widget.ConstraintLayout;

import com.example.qlsv2.Class.lophoc;
import com.example.qlsv2.R;

import java.util.List;

public class LopHocGVAdapter extends BaseAdapter {
    private Context context;
    private int resource;
    private List<lophoc> arrContact;

    public LopHocGVAdapter(Context context, int resource, List<lophoc> arrContact) {
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
            convertView = LayoutInflater.from(context).inflate(R.layout.row_lophocgv, parent, false);
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
        viewHolder.tvphong.setText(contact.getMsPhong()+" - "+contact.getTenDvi());
        String loailpHP= contact.getLoailopHP();

        //xuly tinhtrang
        int tt=Integer.parseInt(contact.getTinhtrang());
        if (tt==-1){
            if (loailpHP.equals("LT")){
                viewHolder.constraintLayout.setBackgroundResource(R.drawable.bg_row_lythuyet);
            }else if (loailpHP.equals("TH")){
                viewHolder.constraintLayout.setBackgroundResource(R.drawable.bg_row_thuchanh);
            }
            viewHolder.tvtinhtrang.setText("Chưa mở");


        }
        if (tt==1){
            viewHolder.tvtinhtrang.setText("Đã điểm danh");
            viewHolder.constraintLayout.setBackgroundResource(R.drawable.br_row_dadiemdanh);

        }
        if (tt==0){
            viewHolder.tvtinhtrang.setText("Chưa điểm danh");
            viewHolder.constraintLayout.setBackgroundResource(R.drawable.bg_row_lopdangdienra);

        }
        if (tt==2){
            viewHolder.tvtinhtrang.setText("Đã khóa");
            viewHolder.constraintLayout.setBackgroundResource(R.drawable.bg_row_dakhoa);

        }

//        //xuly diem danh
//        String pattern = "HH:mm";
//        SimpleDateFormat sdf = new SimpleDateFormat(pattern);
//        Date now = new Date();
//        String t= sdf.format(now);
//        String loailpHP= contact.getLoailopHP();
//        try {
//            Date star = sdf.parse(contact.getTgbd());
//            Date end = sdf.parse(contact.getTgkt());
//            Date ht =sdf.parse(t);
//
//            if(ht.after(star)&&ht.before(end)) {
//                //đang dien ra
//                viewHolder.constraintLayout.setBackgroundResource(R.drawable.bg_row_lopdangdienra);
//                if (tt!=1)  viewHolder.tvtinhtrang.setText("Chưa điểm danh");
//                else if(tt==1)  viewHolder.constraintLayout.setBackgroundResource(R.drawable.br_row_dadiemdanh);
//            }
//            if (star.before(ht)&&end.before(ht)) {
//                //đã khóa
//                viewHolder.constraintLayout.setBackgroundResource(R.drawable.bg_row_dakhoa);
//                if (tt!=1) viewHolder.tvtinhtrang.setText("Đã khóa");
//                else if(tt==1)  viewHolder.constraintLayout.setBackgroundResource(R.drawable.br_row_dadiemdanh);
//            }
//            if (ht.before(star)){
//                //chưa mở
//                if (loailpHP.equals("LT")){
//                    viewHolder.constraintLayout.setBackgroundResource(R.drawable.bg_row_lythuyet);
//                }else if (loailpHP.equals("TH")){
//                    viewHolder.constraintLayout.setBackgroundResource(R.drawable.bg_row_thuchanh);
//                }
////                viewHolder.listView.getChildAt( position).setEnabled(false);
//                viewHolder.tvtinhtrang.setText("Chưa mở");
//
//            }
//        } catch (ParseException e){
//            e.printStackTrace();
//        }




        // xuly tiet
        int tietbd=Integer.parseInt(contact.getTietBD());
        int sotiet=Integer.parseInt(contact.getSotiet());

        if (loailpHP.equals("LT")){
            String tiet=contact.getTietBD();
            for(int i=1;i<sotiet;i++){
                tiet=tiet+","+(tietbd+i)+"";
            }
            viewHolder.tvtiet.setText("Tiết "+tiet);
        }
        if (loailpHP.equals("TH")){
            String tiet="";
            tiet=(tietbd+1)/3+1+"";
            for(int i=1;i<sotiet/3;i++){
                tietbd =(tietbd+1)/3+1;
                tiet=tiet+", "+(tietbd+i)+"";
            }
            viewHolder.tvtiet.setText("Ca "+tiet);

        }



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

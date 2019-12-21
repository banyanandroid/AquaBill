package com.banyan.aquabill.adapter;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.amulyakhare.textdrawable.TextDrawable;
import com.banyan.aquabill.R;
import com.banyan.aquabill.activity.Activity_Manage_Customer;
import com.banyan.aquabill.activity.Activity_Products_and_Rate;
import com.banyan.aquabill.fragment.HomeFragment;

import java.util.ArrayList;
import java.util.HashMap;


public class Adapter_Dashboard_List extends BaseAdapter {
	public Activity activity;
    private Context context;
    private LinearLayout singleMessageContainer;

    private ArrayList<HashMap<String, String>> data;
    private static LayoutInflater inflater=null;

    public Adapter_Dashboard_List(Activity a, ArrayList<HashMap<String, String>> d) {
        activity = a;
        data=d;
    }

    public int getCount() {
        return data.size();
    }

    public Object getItem(int position) {
        return position;
    }

    public long getItemId(int position) {
        return position;
    }
          
        public View getView(int position, View convertView, ViewGroup parent) {
            View v=convertView;
            if(convertView==null)
                v = inflater.inflate(R.layout.list_item_dashboard, null);

            TextView name = (TextView)v.findViewById(R.id.list_dashboard_txt_pdu);
            TextView balance = (TextView)v.findViewById(R.id.list_dashboard_txt_balance);
            ImageView img = (ImageView)v.findViewById(R.id.list_dashboard_img);

            HashMap<String, String> result = new HashMap<String, String>();
            result = data.get(position);

            name.setText(result.get(HomeFragment.TAG_PDU_NAME));
            balance.setText(result.get(HomeFragment.TAG_BALANCE));

            String str_name = result.get(HomeFragment.TAG_PDU_NAME);

            TextDrawable drawable = TextDrawable.builder().buildRound(String.valueOf(str_name.charAt(0)).toUpperCase(),
                    Color.parseColor("#B3E5FC"));
            img.setImageDrawable(drawable);

            drawable = TextDrawable.builder()
                    .beginConfig()
                    .textColor(Color.BLACK)
                    .useFont(Typeface.DEFAULT)
                    .fontSize(10) /* size in px */
                    .bold()
                    .toUpperCase()
                    .endConfig()
                    .buildRound("a", Color.TRANSPARENT);


            return v;
        
    }
    
}
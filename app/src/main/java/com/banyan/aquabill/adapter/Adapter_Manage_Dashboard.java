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
import com.banyan.aquabill.fragment.HomeFragment;

import java.util.ArrayList;
import java.util.HashMap;


public class Adapter_Manage_Dashboard extends BaseAdapter {
	private Activity activity;
    private Context context;
    private LinearLayout singleMessageContainer;

    private ArrayList<HashMap<String, String>> data;
    private static LayoutInflater inflater=null;

    private String[] bgColors;

    public Adapter_Manage_Dashboard(Activity a, ArrayList<HashMap<String, String>> d) {
        activity = a;
        data=d;
        try {
            bgColors = activity.getApplicationContext().getResources().getStringArray(R.array.movie_serial_bg);
            inflater = (LayoutInflater)activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        }catch (Exception e) {

        }
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

            ImageView img = (ImageView) v.findViewById(R.id.list_dashboard_img);
            TextView name = (TextView)v.findViewById(R.id.list_dashboard_txt_pdu);
            TextView balance = (TextView)v.findViewById(R.id.list_dashboard_txt_balance);


            HashMap<String, String> result = new HashMap<String, String>();
            result = data.get(position);

            name.setText(result.get(HomeFragment.TAG_PDU_NAME));
            balance.setText(result.get(HomeFragment.TAG_BALANCE));

            String str_name = result.get(HomeFragment.TAG_PDU_NAME);

            TextDrawable drawable = TextDrawable.builder().buildRound(String.valueOf(str_name.charAt(0)).toUpperCase(),
                    Color.parseColor(new String (bgColors[position % bgColors.length])));
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
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
import com.banyan.aquabill.activity.Activity_Manage_Supplier;

import java.util.ArrayList;
import java.util.HashMap;


public class Adapter_Manage_Customer extends BaseAdapter {
	private Activity activity;
    private Context context;
    private LinearLayout singleMessageContainer;

    private ArrayList<HashMap<String, String>> data;
    private static LayoutInflater inflater=null;

    private String[] bgColors;

    public Adapter_Manage_Customer(Activity a, ArrayList<HashMap<String, String>> d) {
        activity = a;
        data=d;
        bgColors = activity.getApplicationContext().getResources().getStringArray(R.array.movie_serial_bg);
        inflater = (LayoutInflater)activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
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
                v = inflater.inflate(R.layout.list_item_manage_customer, null);

            ImageView img = (ImageView) v.findViewById(R.id.list_manage_cus_img);
            TextView name = (TextView)v.findViewById(R.id.list_manage_cus_txt_name);
            TextView contact_name = (TextView)v.findViewById(R.id.list_manage_cus_txt_contact_person);
            TextView mobile = (TextView)v.findViewById(R.id.list_manage_cus_txt_mob);
            TextView email = (TextView)v.findViewById(R.id.list_manage_cus_txt_email);

            HashMap<String, String> result = new HashMap<String, String>();
            result = data.get(position);

            name.setText(result.get(Activity_Manage_Customer.TAG_CUSTOMER_NAME));
            contact_name.setText(result.get(Activity_Manage_Customer.TAG_CONTACTPERSON));
            mobile.setText(result.get(Activity_Manage_Customer.TAG_MOBILE_NO));
            email.setText(result.get(Activity_Manage_Customer.TAG_EMAIL));

            String str_name = result.get(Activity_Manage_Customer.TAG_CUSTOMER_NAME);

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
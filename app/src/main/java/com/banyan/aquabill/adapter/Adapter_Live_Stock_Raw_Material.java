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
import com.banyan.aquabill.activity.Activity_Manage_Supplier;
import com.banyan.aquabill.activity.Activity_Raw_Mat_Live_Stock;

import java.util.ArrayList;
import java.util.HashMap;


public class Adapter_Live_Stock_Raw_Material extends BaseAdapter {
	private Activity activity;
    private Context context;
    private LinearLayout singleMessageContainer;

    private ArrayList<HashMap<String, String>> data;
    private static LayoutInflater inflater=null;

    private String[] bgColors;

    public Adapter_Live_Stock_Raw_Material(Activity a, ArrayList<HashMap<String, String>> d) {
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
                v = inflater.inflate(R.layout.list_item_raw_mat_live_stock, null);

            ImageView img = (ImageView) v.findViewById(R.id.list_live_stock_img);
            TextView name = (TextView)v.findViewById(R.id.list_live_stock_txt_mat_name);
            TextView qty = (TextView)v.findViewById(R.id.list_live_stock_txt_mat_qty);

            HashMap<String, String> result = new HashMap<String, String>();
            result = data.get(position);

            name.setText(result.get(Activity_Raw_Mat_Live_Stock.TAG_RAW_MAT_NAME));
            qty.setText(result.get(Activity_Raw_Mat_Live_Stock.TAG_STOCK_QTY));
            String str_name = result.get(Activity_Raw_Mat_Live_Stock.TAG_RAW_MAT_NAME);

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
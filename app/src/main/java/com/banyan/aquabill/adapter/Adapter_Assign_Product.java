package com.banyan.aquabill.adapter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.banyan.aquabill.R;
import com.banyan.aquabill.activity.Activity_Assign_Product;
import com.banyan.aquabill.activity.Activity_Products_and_Rate;

import java.util.ArrayList;
import java.util.HashMap;


public class Adapter_Assign_Product extends BaseAdapter {
	private Activity activity;
    private Context context;
    private LinearLayout singleMessageContainer;

    private ArrayList<HashMap<String, String>> data;
    private static LayoutInflater inflater=null;

    private String[] bgColors;

    public Adapter_Assign_Product(Activity a, ArrayList<HashMap<String, String>> d) {
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
                v = inflater.inflate(R.layout.list_item_assign_product, null);

            TextView name = (TextView)v.findViewById(R.id.list_assign_prod_txt_name);
            TextView qty = (TextView)v.findViewById(R.id.list_assign_prod_txt_qty);
            TextView packing = (TextView)v.findViewById(R.id.list_assign_prod_txt_packing);
            TextView purchaserate = (TextView)v.findViewById(R.id.list_assign_prod_txt_purchase);
            TextView sellingrate = (TextView)v.findViewById(R.id.list_assign_prod_txt_selling);

            HashMap<String, String> result = new HashMap<String, String>();
            result = data.get(position);

            name.setText(result.get(Activity_Assign_Product.TAG_PRODUCT));
            qty.setText(result.get(Activity_Assign_Product.TAG_QTY));
            packing.setText(result.get(Activity_Assign_Product.TAG_PACKING));
            purchaserate.setText(result.get(Activity_Assign_Product.TAG_PURCHASE_RATE));
            sellingrate.setText(result.get(Activity_Assign_Product.TAG_SELLING_RATE));

            return v;
        
    }
    
}
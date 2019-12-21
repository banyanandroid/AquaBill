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
import com.banyan.aquabill.activity.Activity_Report_Empty_Can;
import com.banyan.aquabill.activity.Activity_Report_Sales;

import java.util.ArrayList;
import java.util.HashMap;


public class Adapter_Report_Empty_Can extends BaseAdapter {
	private Activity activity;
    private Context context;
    private LinearLayout singleMessageContainer;

    private ArrayList<HashMap<String, String>> data;
    private static LayoutInflater inflater=null;

    private String[] bgColors;

    public Adapter_Report_Empty_Can(Activity a, ArrayList<HashMap<String, String>> d) {
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
                v = inflater.inflate(R.layout.list_item_empty_can_stock, null);

            TextView dealer = (TextView)v.findViewById(R.id.list_item_txt_empty_can_stock_dealer_name);
            TextView customer = (TextView)v.findViewById(R.id.list_item_txt_empty_can_stock_cust_name);
            TextView product = (TextView)v.findViewById(R.id.list_item_txt_empty_can_stock_prod_name);
            TextView stock = (TextView)v.findViewById(R.id.list_item_txt_empty_can_stock_stock_name);

            HashMap<String, String> result = new HashMap<String, String>();
            result = data.get(position);

            dealer.setText(result.get(Activity_Report_Empty_Can.TAG_DEALER_NAME));
            customer.setText(result.get(Activity_Report_Empty_Can.TAG_CUSTOMER_NAME));
            product.setText(result.get(Activity_Report_Empty_Can.TAG_PRODUCT_NAME));
            stock.setText(result.get(Activity_Report_Empty_Can.TAG_STOCK));

            return v;
        
    }
    
}
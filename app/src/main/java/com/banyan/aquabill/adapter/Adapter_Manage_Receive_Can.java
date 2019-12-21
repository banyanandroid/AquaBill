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
import com.banyan.aquabill.activity.Activity_Manage_Received_Can;
import com.banyan.aquabill.activity.Activity_Manage_Received_Cash;

import java.util.ArrayList;
import java.util.HashMap;


public class Adapter_Manage_Receive_Can extends BaseAdapter {
	private Activity activity;
    private Context context;
    private LinearLayout singleMessageContainer;

    private ArrayList<HashMap<String, String>> data;
    private static LayoutInflater inflater=null;

    private String[] bgColors;

    public Adapter_Manage_Receive_Can(Activity a, ArrayList<HashMap<String, String>> d) {
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
                v = inflater.inflate(R.layout.list_item_receive_can, null);

            TextView invoice_no = (TextView)v.findViewById(R.id.list_item_txt_receive_can_no);
            TextView customer = (TextView)v.findViewById(R.id.list_item_txt_receive_can_customer);
            TextView dealer = (TextView)v.findViewById(R.id.list_item_txt_receive_can_dealer_name);
            TextView can_type = (TextView)v.findViewById(R.id.list_item_txt_receive_can_payment_type);
            TextView qty = (TextView)v.findViewById(R.id.list_item_txt_receive_can_qty);
            TextView stock = (TextView)v.findViewById(R.id.list_item_txt_receive_can_stock);

            HashMap<String, String> result = new HashMap<String, String>();
            result = data.get(position);


            invoice_no.setText(result.get(Activity_Manage_Received_Can.TAG_RECEIPT_NO));
            customer.setText(result.get(Activity_Manage_Received_Can.TAG_CUSTOMER_NAME));
            dealer.setText(result.get(Activity_Manage_Received_Can.TAG_DEALER_NAME));
            can_type.setText(result.get(Activity_Manage_Received_Can.TAG_PRODUCT_NAME));
            qty.setText(result.get(Activity_Manage_Received_Can.TAG_QTY));
            stock.setText(result.get(Activity_Manage_Received_Can.TAG_STOCK));

            return v;
        
    }
    
}
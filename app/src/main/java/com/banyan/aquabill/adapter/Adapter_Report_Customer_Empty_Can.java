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
import com.banyan.aquabill.activity.Activity_Manage_Invoice;
import com.banyan.aquabill.activity.Activity_Report_Cust_Empty_Can_Report;

import java.util.ArrayList;
import java.util.HashMap;


public class Adapter_Report_Customer_Empty_Can extends BaseAdapter {
	private Activity activity;
    private Context context;
    private LinearLayout singleMessageContainer;

    private ArrayList<HashMap<String, String>> data;
    private static LayoutInflater inflater=null;

    private String[] bgColors;

    public Adapter_Report_Customer_Empty_Can(Activity a, ArrayList<HashMap<String, String>> d) {
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
                v = inflater.inflate(R.layout.list_item_cust_empty_can, null);

            TextView receipt_no = (TextView)v.findViewById(R.id.list_item_cust_empty_can_txt_receipt_no);
            TextView product = (TextView)v.findViewById(R.id.list_item_cust_empty_can_txt_product);
            TextView dealer = (TextView)v.findViewById(R.id.list_item_cust_empty_can_txt_dealer);
            TextView customer = (TextView)v.findViewById(R.id.list_item_cust_empty_can_txt_customer);
            TextView delivered = (TextView)v.findViewById(R.id.list_item_cust_empty_can_txt_deliverd);
            TextView received = (TextView)v.findViewById(R.id.list_item_cust_empty_can_txt_received);
            TextView opening = (TextView)v.findViewById(R.id.list_item_cust_empty_can_txt_opening);
            TextView closing = (TextView)v.findViewById(R.id.list_item_cust_empty_can_txt_closing);

            HashMap<String, String> result = new HashMap<String, String>();
            result = data.get(position);

            receipt_no.setText(result.get(Activity_Report_Cust_Empty_Can_Report.TAG_RECEIPT_NO));
            product.setText(result.get(Activity_Report_Cust_Empty_Can_Report.TAG_PRODUCT_NAME));
            dealer.setText(result.get(Activity_Report_Cust_Empty_Can_Report.TAG_DEALER_NAME));
            customer.setText(result.get(Activity_Report_Cust_Empty_Can_Report.TAG_CUSTOMER_NAME));
            delivered.setText(result.get(Activity_Report_Cust_Empty_Can_Report.TAG_DELIVERED_QTY));
            received.setText(result.get(Activity_Report_Cust_Empty_Can_Report.TAG_RECEIVED_QTY));
            opening.setText(result.get(Activity_Report_Cust_Empty_Can_Report.TAG_OPENING));
            closing.setText(result.get(Activity_Report_Cust_Empty_Can_Report.TAG_CLOSING));


            return v;
        
    }
    
}
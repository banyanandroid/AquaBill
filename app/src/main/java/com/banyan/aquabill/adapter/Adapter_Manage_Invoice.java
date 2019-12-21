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
import com.banyan.aquabill.activity.Activity_Purchase_Report;

import java.util.ArrayList;
import java.util.HashMap;


public class Adapter_Manage_Invoice extends BaseAdapter {
	private Activity activity;
    private Context context;
    private LinearLayout singleMessageContainer;

    private ArrayList<HashMap<String, String>> data;
    private static LayoutInflater inflater=null;

    private String[] bgColors;

    public Adapter_Manage_Invoice(Activity a, ArrayList<HashMap<String, String>> d) {
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
                v = inflater.inflate(R.layout.list_item_invoice_list, null);

            TextView date = (TextView)v.findViewById(R.id.list_item_txt_invoice_date);
            TextView time = (TextView)v.findViewById(R.id.list_item_txt_invoice_time);
            TextView invoice_no = (TextView)v.findViewById(R.id.list_item_txt_invoice_no);
            TextView brand = (TextView)v.findViewById(R.id.list_item_txt_invoice_brand);
            TextView product = (TextView)v.findViewById(R.id.list_item_txt_invoice_product);
            TextView quantity = (TextView)v.findViewById(R.id.list_item_txt_invoice_qty);
            TextView rate = (TextView)v.findViewById(R.id.list_item_txt_invoice_rate);
            TextView received = (TextView)v.findViewById(R.id.list_item_txt_invoice_received);
            TextView pending = (TextView)v.findViewById(R.id.list_item_txt_invoice_balance);

            HashMap<String, String> result = new HashMap<String, String>();
            result = data.get(position);

            date.setText(result.get(Activity_Manage_Invoice.TAG_DATE));
            time.setText(result.get(Activity_Manage_Invoice.TAG_TIME));
            invoice_no.setText(result.get(Activity_Manage_Invoice.TAG_INVOICE_NO));
            brand.setText(result.get(Activity_Manage_Invoice.TAG_CUSTOMER_NAME));
            product.setText(result.get(Activity_Manage_Invoice.TAG_PRODUCT_NAME));
            quantity.setText(result.get(Activity_Manage_Invoice.TAG_QTY));
            rate.setText(result.get(Activity_Manage_Invoice.TAG_INVOICE_AMT));
            received.setText(result.get(Activity_Manage_Invoice.TAG_RECEIVED_AMT));
            pending.setText(result.get(Activity_Manage_Invoice.TAG_BALANCE_AMT));


            return v;
        
    }
    
}
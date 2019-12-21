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
import com.banyan.aquabill.activity.Activity_Manage_Received_Cash;
import com.banyan.aquabill.activity.Activity_Report_Receipt;

import java.util.ArrayList;
import java.util.HashMap;


public class Adapter_Report_Receipt_Report extends BaseAdapter {
	private Activity activity;
    private Context context;
    private LinearLayout singleMessageContainer;

    private ArrayList<HashMap<String, String>> data;
    private static LayoutInflater inflater=null;

    private String[] bgColors;

    public Adapter_Report_Receipt_Report(Activity a, ArrayList<HashMap<String, String>> d) {
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
                v = inflater.inflate(R.layout.list_item_receipt_list, null);

            TextView date = (TextView)v.findViewById(R.id.list_item_txt_receved_amt_date);
            TextView time = (TextView)v.findViewById(R.id.list_item_txt_receved_amt_time);
            TextView invoice_no = (TextView)v.findViewById(R.id.list_item_txt_receved_amt_no);
            TextView customer = (TextView)v.findViewById(R.id.list_item_txt_receved_amt_customer);
            TextView dealer = (TextView)v.findViewById(R.id.list_item_txt_receved_amt_dealer_name);
            TextView payment_type = (TextView)v.findViewById(R.id.list_item_txt_receved_amt_payment_type);
            TextView amt = (TextView)v.findViewById(R.id.list_item_txt_receved_amt);
            TextView refno = (TextView)v.findViewById(R.id.list_item_txt_receved_amt_refno);
            TextView bank = (TextView)v.findViewById(R.id.list_item_txt_receved_amt_bank);

            HashMap<String, String> result = new HashMap<String, String>();
            result = data.get(position);

            date.setText(result.get(Activity_Report_Receipt.TAG_DATE));
            time.setText(result.get(Activity_Report_Receipt.TAG_TIME));
            invoice_no.setText(result.get(Activity_Report_Receipt.TAG_RECEIPT_NO));
            customer.setText(result.get(Activity_Report_Receipt.TAG_CUSTOMER_NAME));
            dealer.setText(result.get(Activity_Report_Receipt.TAG_DEALER_NAME));
            payment_type.setText(result.get(Activity_Report_Receipt.TAG_PAYMENT_TYPE));
            amt.setText(result.get(Activity_Report_Receipt.TAG_RECEIVED_AMT));
            refno.setText(result.get(Activity_Report_Receipt.TAG_REF_NO));
            bank.setText(result.get(Activity_Report_Receipt.TAG_BANK_NAME));

            return v;
        
    }
    
}
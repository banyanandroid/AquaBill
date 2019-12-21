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
import com.banyan.aquabill.activity.Activity_Purchase_Report;
import com.banyan.aquabill.activity.Activity_Report_Tranactions;

import java.util.ArrayList;
import java.util.HashMap;


public class Adapter_Report_Transactions extends BaseAdapter {
	private Activity activity;
    private Context context;
    private LinearLayout singleMessageContainer;

    private ArrayList<HashMap<String, String>> data;
    private static LayoutInflater inflater=null;

    private String[] bgColors;

    public Adapter_Report_Transactions(Activity a, ArrayList<HashMap<String, String>> d) {
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
                v = inflater.inflate(R.layout.list_item_report_transaction, null);

            TextView name = (TextView)v.findViewById(R.id.list_item_transaction_txt_cus_name);
            TextView type = (TextView)v.findViewById(R.id.list_item_transaction_txt_type);
            TextView receipt_no = (TextView)v.findViewById(R.id.list_item_transaction_txt_receipt_no);
            TextView amount = (TextView)v.findViewById(R.id.list_item_transaction_txt_amt);
            TextView received_amount = (TextView)v.findViewById(R.id.list_item_transaction_txt_received_amt);
            TextView balance_amount = (TextView)v.findViewById(R.id.list_item_transaction_txt_balance_amt);
            TextView outstanding = (TextView)v.findViewById(R.id.list_item_transaction_txt_outstaning_amt);

            HashMap<String, String> result = new HashMap<String, String>();
            result = data.get(position);

            name.setText(result.get(Activity_Report_Tranactions.TAG_CUSTOMER_NAME));
            type.setText(result.get(Activity_Report_Tranactions.TAG_TYPE));
            receipt_no.setText(result.get(Activity_Report_Tranactions.TAG_RECEIPT_NO));
            amount.setText(result.get(Activity_Report_Tranactions.TAG_AMT));
            received_amount.setText(result.get(Activity_Report_Tranactions.TAG_RECEIVED_AMT));
            balance_amount.setText(result.get(Activity_Report_Tranactions.TAG_BALANCE_AMT));
            outstanding.setText(result.get(Activity_Report_Tranactions.TAG_OUTSTANDING));


            return v;
        
    }
    
}
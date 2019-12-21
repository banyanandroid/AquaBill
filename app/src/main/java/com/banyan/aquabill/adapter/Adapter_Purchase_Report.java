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
import com.banyan.aquabill.activity.Activity_Purchase_Report;

import java.util.ArrayList;
import java.util.HashMap;


public class Adapter_Purchase_Report extends BaseAdapter {
	private Activity activity;
    private Context context;
    private LinearLayout singleMessageContainer;

    private ArrayList<HashMap<String, String>> data;
    private static LayoutInflater inflater=null;

    private String[] bgColors;

    public Adapter_Purchase_Report(Activity a, ArrayList<HashMap<String, String>> d) {
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
                v = inflater.inflate(R.layout.list_item_purchase_report, null);

            TextView date = (TextView)v.findViewById(R.id.list_item_txt_pur_date);
            TextView time = (TextView)v.findViewById(R.id.list_item_txt_pur_time);
            TextView invoice_no = (TextView)v.findViewById(R.id.list_item_txt_pur_invoice_no);
            TextView supplier = (TextView)v.findViewById(R.id.list_item_txt_pur_supplier);
            TextView rawmaterial = (TextView)v.findViewById(R.id.list_item_txt_pur_rawmat);
            TextView quantity = (TextView)v.findViewById(R.id.list_item_txt_pur_qty);
            TextView rate = (TextView)v.findViewById(R.id.list_item_txt_pur_rate);
            TextView value = (TextView)v.findViewById(R.id.list_item_txt_pur_value);
            TextView stock = (TextView)v.findViewById(R.id.list_item_txt_pur_stock);

            HashMap<String, String> result = new HashMap<String, String>();
            result = data.get(position);

            date.setText(result.get(Activity_Purchase_Report.TAG_PURCHASE_DATE));
            time.setText(result.get(Activity_Purchase_Report.TAG_PURCHASE_TIME));
            invoice_no.setText(result.get(Activity_Purchase_Report.TAG_PURCHASE_INVOICE_NO));
            supplier.setText(result.get(Activity_Purchase_Report.TAG_PURCHASE_SUPPLIER));
            rawmaterial.setText(result.get(Activity_Purchase_Report.TAG_PURCHASE_RAWMATERIAL));
            quantity.setText(result.get(Activity_Purchase_Report.TAG_PURCHASE_QUANTITY));
            rate.setText(result.get(Activity_Purchase_Report.TAG_PURCHASE_RATE));
            value.setText(result.get(Activity_Purchase_Report.TAG_PURCHASE_VALUE));
            stock.setText(result.get(Activity_Purchase_Report.TAG_PURCHASE_STOCK));


            return v;
        
    }
    
}
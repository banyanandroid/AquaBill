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
import com.banyan.aquabill.activity.Activity_Report_Sales;

import java.util.ArrayList;
import java.util.HashMap;


public class Adapter_Report_Sales extends BaseAdapter {
	private Activity activity;
    private Context context;
    private LinearLayout singleMessageContainer;

    private ArrayList<HashMap<String, String>> data;
    private static LayoutInflater inflater=null;

    private String[] bgColors;

    public Adapter_Report_Sales(Activity a, ArrayList<HashMap<String, String>> d) {
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
                v = inflater.inflate(R.layout.list_item_report_sales, null);

            TextView date = (TextView)v.findViewById(R.id.list_item_txt_rep_sales_date);
            TextView time = (TextView)v.findViewById(R.id.list_item_txt_rep_sales_time);
            TextView invoice_no = (TextView)v.findViewById(R.id.list_item_txt_rep_sales_no);
            TextView invoice_type = (TextView)v.findViewById(R.id.list_item_txt_rep_sales_type);
            TextView dealer_name = (TextView)v.findViewById(R.id.list_item_txt_rep_sales_dealer_name);
            TextView cust_name = (TextView)v.findViewById(R.id.list_item_txt_rep_sales_customer);
            TextView brand = (TextView)v.findViewById(R.id.list_item_txt_rep_sales_brand);
            TextView product = (TextView)v.findViewById(R.id.list_item_txt_rep_sales_product);
            TextView quantity = (TextView)v.findViewById(R.id.list_item_txt_rep_sales_qty);
            TextView rate = (TextView)v.findViewById(R.id.list_item_txt_rep_sales_rate);
            TextView tax = (TextView)v.findViewById(R.id.list_item_txt_rep_sales_tax);
            TextView taxable_value = (TextView)v.findViewById(R.id.list_item_txt_rep_sales_taxable_value);
            TextView received = (TextView)v.findViewById(R.id.list_item_txt_rep_sales_received);
            TextView pending = (TextView)v.findViewById(R.id.list_item_txt_rep_sales_balance);

            HashMap<String, String> result = new HashMap<String, String>();
            result = data.get(position);

            date.setText(result.get(Activity_Report_Sales.TAG_DATE));
            time.setText(result.get(Activity_Report_Sales.TAG_TIME));
            invoice_no.setText(result.get(Activity_Report_Sales.TAG_INVOICE_NO));
            invoice_type.setText(result.get(Activity_Report_Sales.TAG_INVOICE_TYPE));
            dealer_name.setText(result.get(Activity_Report_Sales.TAG_DEALER_NAME));
            cust_name.setText(result.get(Activity_Report_Sales.TAG_CUSTOMER_NAME));
            brand.setText(result.get(Activity_Report_Sales.TAG_BRAND_NAME));
            product.setText(result.get(Activity_Report_Sales.TAG_PRODUCT_NAME));
            quantity.setText(result.get(Activity_Report_Sales.TAG_QTY));
            rate.setText(result.get(Activity_Report_Sales.TAG_INVOICE_AMT));
            tax.setText(result.get(Activity_Report_Sales.TAG_TAX));
            taxable_value.setText(result.get(Activity_Report_Sales.TAG_TAXABLE_VALUE));
            received.setText(result.get(Activity_Report_Sales.TAG_RECEIVED_AMT));
            pending.setText(result.get(Activity_Report_Sales.TAG_BALANCE_AMT));


            return v;
        
    }
    
}
package com.banyan.aquabill.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.banyan.aquabill.R;
import com.banyan.aquabill.adapter.Adapter_Report_Empty_Can;
import com.banyan.aquabill.adapter.Adapter_Report_Transactions;
import com.banyan.aquabill.global.Appconfig;
import com.banyan.aquabill.global.SessionManager;
import com.google.android.material.snackbar.Snackbar;
import com.toptoche.searchablespinnerlibrary.SearchableSpinner;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import dmax.dialog.SpotsDialog;

public class Activity_Report_Tranactions extends AppCompatActivity {

    String TAG = "Products and Rates";
    private View parent_view;

    // Session Manager Class
    SessionManager session;
    private static long back_pressed;

    SpotsDialog dialog;
    public static RequestQueue queue;

    private Toolbar mToolbar;

    String str_dealer_id;

    private ListView list_report;

    static ArrayList<HashMap<String, String>> report_transaction_list;
    HashMap<String, String> params = new HashMap<String, String>();

    String  str_customer_id, str_customer, str_product_id, str_product;

    public static final String TAG_CUSTOMER_NAME = "customer_name";
    public static final String TAG_TYPE = "type";
    public static final String TAG_RECEIPT_NO = "receipt_no";
    public static final String TAG_AMT = "amount";
    public static final String TAG_RECEIVED_AMT = "received_amount";
    public static final String TAG_BALANCE_AMT = "balance_amount";
    public static final String TAG_OUTSTANDING = "outstanding";

    private Adapter_Report_Transactions adapter;

    String str_from_date, str_to_date;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report_transactions);

        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        parent_view = findViewById(android.R.id.content);

        setSupportActionBar(mToolbar);
        getSupportActionBar().setTitle("Transaction Report");
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        mToolbar.setNavigationIcon(getResources().getDrawable(R.drawable.ic_action_back));
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent i = new Intent(getApplicationContext(), MainActivity.class);
                i.putExtra("manage_class","reports");
                startActivity(i);
                finish();
            }
        });

        session = new SessionManager(getApplicationContext());
        HashMap<String, String> user = session.getUserDetails();
        str_dealer_id = user.get(SessionManager.KEY_DEALER_ID);

        SharedPreferences sharedPreferences = PreferenceManager
                .getDefaultSharedPreferences(Activity_Report_Tranactions.this);

        str_customer_id = sharedPreferences.getString("str_cus_id", "str_cus_id");

        list_report = (ListView)  findViewById(R.id.report_transaction_listview);

        report_transaction_list = new ArrayList<HashMap<String, String>>();

        try {
            dialog = new SpotsDialog(Activity_Report_Tranactions.this);
            dialog.show();
            queue = Volley.newRequestQueue(Activity_Report_Tranactions.this);
            Action_Get_Data();
        }catch (Exception e) {

        }
    }

    /***************************
     * GET Record
     ***************************/

    public void Action_Get_Data() {

        String tag_json_obj = "json_obj_req";

        StringRequest request = new StringRequest(Request.Method.POST,
                Appconfig.url_customertrans_reports, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.d(TAG, response.toString());
                try {
                    JSONObject obj = new JSONObject(response);
                    int success = obj.getInt("status");

                    if (success == 1) {

                        JSONArray arr;

                        arr = obj.getJSONArray("data");

                        report_transaction_list.clear();

                        for (int i = 0; arr.length() > i; i++) {
                            JSONObject obj1 = arr.getJSONObject(i);

                            String customer_name = obj1.getString(TAG_CUSTOMER_NAME);
                            String product_name = obj1.getString(TAG_TYPE);
                            String receipt_no = obj1.getString(TAG_RECEIPT_NO);
                            String amt = obj1.getString(TAG_AMT);
                            String received_Amt = obj1.getString(TAG_RECEIVED_AMT);
                            String balance_amt = obj1.getString(TAG_BALANCE_AMT);
                            String outstanding = obj1.getString(TAG_OUTSTANDING);

                            // creating new HashMap
                            HashMap<String, String> map = new HashMap<String, String>();

                            // adding each child node to HashMap key => value
                            map.put(TAG_CUSTOMER_NAME, customer_name);
                            map.put(TAG_TYPE, product_name);
                            map.put(TAG_RECEIPT_NO, receipt_no);
                            map.put(TAG_AMT, amt);
                            map.put(TAG_RECEIVED_AMT, received_Amt);
                            map.put(TAG_BALANCE_AMT, balance_amt);
                            map.put(TAG_OUTSTANDING, outstanding);

                            report_transaction_list.add(map);

                            adapter = new Adapter_Report_Transactions(Activity_Report_Tranactions.this,
                                    report_transaction_list);
                            list_report.setAdapter(adapter);

                        }

                    } else if (success == 0) {

                        dialog.dismiss();
                        Snackbar.make(parent_view, "No Records Found", Snackbar.LENGTH_SHORT).show();

                    }
                } catch (JSONException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }

                dialog.dismiss();
            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {

                // stopping swipe refresh
                dialog.dismiss();

            }
        }) {

            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();

                params.put("user_id", str_dealer_id);
                params.put("customer_id", str_customer_id);

                System.out.println("USER ID : " + str_dealer_id);
                System.out.println("str_customer_id : " + str_customer_id);

                return checkParams(params);
            }

            private Map<String, String> checkParams(Map<String, String> map) {
                Iterator<Map.Entry<String, String>> it = map.entrySet().iterator();
                while (it.hasNext()) {
                    Map.Entry<String, String> pairs = (Map.Entry<String, String>) it.next();
                    if (pairs.getValue() == null) {
                        map.put(pairs.getKey(), "");
                    }
                }
                return map;
            }
        };

        int socketTimeout = 60000;//30 seconds - change to what you want
        RetryPolicy policy = new DefaultRetryPolicy(socketTimeout, DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        request.setRetryPolicy(policy);
        // Adding request to request queue
        queue.add(request);
    }

    @Override
    public void onBackPressed() {

        if (back_pressed + 2000 > System.currentTimeMillis()) {

            this.moveTaskToBack(true);
        } else {

            Snackbar.make(parent_view, "Press once again to exit!", Snackbar.LENGTH_SHORT).show();

        }
        back_pressed = System.currentTimeMillis();
    }
}

package com.banyan.aquabill.activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.banyan.aquabill.R;
import com.banyan.aquabill.adapter.Adapter_Manage_Supplier;
import com.banyan.aquabill.adapter.Adapter_Purchase_Report;
import com.banyan.aquabill.global.Appconfig;
import com.banyan.aquabill.global.SessionManager;
import com.google.android.material.snackbar.Snackbar;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import dmax.dialog.SpotsDialog;

public class Activity_Purchase_Report extends AppCompatActivity implements SwipeRefreshLayout.OnRefreshListener{

    String TAG = "Purchase Report";
    private View parent_view;

    // Session Manager Class
    SessionManager session;
    private static long back_pressed;

    SpotsDialog dialog;
    public static RequestQueue queue;

    private Toolbar mToolbar;

    private ListView List;
    private SwipeRefreshLayout swipeRefreshLayout;

    private static final int REQUEST_CODE = 1;

    static ArrayList<HashMap<String, String>> purchase_report;
    HashMap<String, String> params = new HashMap<String, String>();

    public static final String TAG_PURCHASE_DATE = "date";
    public static final String TAG_PURCHASE_TIME = "time";
    public static final String TAG_PURCHASE_INVOICE_NO = "invoice_no";
    public static final String TAG_PURCHASE_SUPPLIER = "supplier";
    public static final String TAG_PURCHASE_RAWMATERIAL = "rawmaterial";
    public static final String TAG_PURCHASE_QUANTITY = "quantity";
    public static final String TAG_PURCHASE_RATE = "rate";
    public static final String TAG_PURCHASE_VALUE = "value";
    public static final String TAG_PURCHASE_STOCK = "stock";

    private Adapter_Purchase_Report adapter;

    String str_dealer_id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_purchase_report);

        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        parent_view = findViewById(android.R.id.content);

        setSupportActionBar(mToolbar);
        getSupportActionBar().setTitle("Purchase Report");
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

        List = (ListView) findViewById(R.id.alloted_comp_listView);
        swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.alloted_comp_swipe_refresh_layout);

        purchase_report = new ArrayList<HashMap<String, String>>();

        swipeRefreshLayout.setOnRefreshListener(this);

        swipeRefreshLayout.post(new Runnable() {
                                    @Override
                                    public void run() {
                                        swipeRefreshLayout.setRefreshing(true);

                                        try {
                                            queue = Volley.newRequestQueue(Activity_Purchase_Report.this);
                                            GetPurchaseReport();

                                        } catch (Exception e) {
                                            // TODO: handle exceptions
                                        }
                                    }
                                }
        );

        List.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {

            }

        });

    }


    /**
     * This method is called when swipe refresh is pulled down
     */
    @Override
    public void onRefresh() {
        try {
            purchase_report.clear();
            queue = Volley.newRequestQueue(Activity_Purchase_Report.this);
            GetPurchaseReport();

        } catch (Exception e) {
            // TODO: handle exception
        }
    }


    /*****************************
     * GET REQ
     ***************/

    public void GetPurchaseReport() {

        String tag_json_obj = "json_obj_req";

        StringRequest request = new StringRequest(Request.Method.POST,
                Appconfig.url_list_purchase_report, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.d(TAG, response.toString());
                try {
                    JSONObject obj = new JSONObject(response);
                    int success = obj.getInt("status");

                    if (success == 1) {

                        JSONArray arr;

                        arr = obj.getJSONArray("data");

                        for (int i = 0; arr.length() > i; i++) {
                            JSONObject obj1 = arr.getJSONObject(i);

                            String date = obj1.getString(TAG_PURCHASE_DATE);
                            String time = obj1.getString(TAG_PURCHASE_TIME);
                            String invoice_no = obj1.getString(TAG_PURCHASE_INVOICE_NO);
                            String supplier = obj1.getString(TAG_PURCHASE_SUPPLIER);
                            String rawmaterial = obj1.getString(TAG_PURCHASE_RAWMATERIAL);
                            String quantity = obj1.getString(TAG_PURCHASE_QUANTITY);
                            String rate = obj1.getString(TAG_PURCHASE_RATE);
                            String value = obj1.getString(TAG_PURCHASE_VALUE);
                            String stock = obj1.getString(TAG_PURCHASE_STOCK);


                            // creating new HashMap
                            HashMap<String, String> map = new HashMap<String, String>();

                            // adding each child node to HashMap key => value
                            map.put(TAG_PURCHASE_DATE, date);
                            map.put(TAG_PURCHASE_TIME, time);
                            map.put(TAG_PURCHASE_INVOICE_NO, invoice_no);
                            map.put(TAG_PURCHASE_SUPPLIER, supplier);
                            map.put(TAG_PURCHASE_RAWMATERIAL, rawmaterial);
                            map.put(TAG_PURCHASE_QUANTITY, quantity);
                            map.put(TAG_PURCHASE_RATE, rate);
                            map.put(TAG_PURCHASE_VALUE, value);
                            map.put(TAG_PURCHASE_STOCK, stock);

                            purchase_report.add(map);

                            adapter = new Adapter_Purchase_Report(Activity_Purchase_Report.this,
                                    purchase_report);
                            List.setAdapter(adapter);

                        }

                    } else if (success == 0) {

                        swipeRefreshLayout.setRefreshing(false);
                        Snackbar.make(parent_view, "No Records Found", Snackbar.LENGTH_SHORT).show();

                    }
                } catch (JSONException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
                // adapter.notifyDataSetChanged();
                // stopping swipe refresh
                swipeRefreshLayout.setRefreshing(false);
                //  pDialog.hide();
            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {

                // stopping swipe refresh
                swipeRefreshLayout.setRefreshing(false);

            }
        }) {

            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();

                params.put("user_id", str_dealer_id);

                System.out.println("USER ID : " + str_dealer_id);

                return params;
            }

        };

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

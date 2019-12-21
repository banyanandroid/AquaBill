package com.banyan.aquabill.activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
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
import com.banyan.aquabill.adapter.Adapter_Manage_Receive_Cash;
import com.banyan.aquabill.global.Appconfig;
import com.banyan.aquabill.global.SessionManager;
import com.github.fabtransitionactivity.SheetLayout;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import dmax.dialog.SpotsDialog;

public class Activity_Manage_Received_Cash extends AppCompatActivity implements SheetLayout.OnFabAnimationEndListener, SwipeRefreshLayout.OnRefreshListener{

    String TAG = "Products and Rates";
    private View parent_view;

    // Session Manager Class
    SessionManager session;
    private static long back_pressed;

    SpotsDialog dialog;
    public static RequestQueue queue;

    private Toolbar mToolbar;

    private SheetLayout mSheetLayout;
    private FloatingActionButton mFab;

    private ListView List;
    private SwipeRefreshLayout swipeRefreshLayout;

    private static final int REQUEST_CODE = 1;

    static ArrayList<HashMap<String, String>> received_cash_list;
    HashMap<String, String> params = new HashMap<String, String>();

    public static final String TAG_DEALER_NAME = "dealer_name";
    public static final String TAG_CUSTOMER_NAME = "customer_name";
    public static final String TAG_RECEIPT_NO = "receipt_no";
    public static final String TAG_DATE = "date";
    public static final String TAG_TIME = "time";
    public static final String TAG_RECEIVED_AMT = "received_amount";
    public static final String TAG_PAYMENT_TYPE = "payment_type";
    public static final String TAG_CHEQUE_NO = "chequeno";
    public static final String TAG_BANK_NAME = "bankname";

    private Adapter_Manage_Receive_Cash adapter;

    String str_dealer_id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_customer);

        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        parent_view = findViewById(android.R.id.content);

        setSupportActionBar(mToolbar);
        getSupportActionBar().setTitle("Manage Received Cash");
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        mToolbar.setNavigationIcon(getResources().getDrawable(R.drawable.ic_action_back));
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent i = new Intent(getApplicationContext(), MainActivity.class);
                i.putExtra("manage_class","invoice");
                startActivity(i);
                finish();
            }
        });

        session = new SessionManager(getApplicationContext());
        HashMap<String, String> user = session.getUserDetails();
        str_dealer_id = user.get(SessionManager.KEY_DEALER_ID);

        mFab = (FloatingActionButton) findViewById(R.id.fab_add_task);
        mSheetLayout = (SheetLayout) findViewById(R.id.bottom_sheet);
        List = (ListView) findViewById(R.id.alloted_comp_listView);
        swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.alloted_comp_swipe_refresh_layout);

        received_cash_list = new ArrayList<HashMap<String, String>>();

        mSheetLayout.setFab(mFab);
        mSheetLayout.setFabAnimationEndListener(this);

        mFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mSheetLayout.expandFab();
            }
        });

        swipeRefreshLayout.setOnRefreshListener(this);

        swipeRefreshLayout.post(new Runnable() {
                                    @Override
                                    public void run() {
                                        swipeRefreshLayout.setRefreshing(true);

                                        try {
                                            queue = Volley.newRequestQueue(Activity_Manage_Received_Cash.this);
                                            GetInvoiceList();

                                        } catch (Exception e) {
                                            // TODO: handle exceptions
                                        }
                                    }
                                }
        );

    }


    /**
     * This method is called when swipe refresh is pulled down
     */
    @Override
    public void onRefresh() {
        try {
            received_cash_list.clear();
            queue = Volley.newRequestQueue(Activity_Manage_Received_Cash.this);
            GetInvoiceList();

        } catch (Exception e) {
            // TODO: handle exception
        }
    }


    @Override
    public void onFabAnimationEnd() {
        Intent intent = new Intent(Activity_Manage_Received_Cash.this, Activity_Add_Received_Cash.class);
        startActivityForResult(intent, REQUEST_CODE);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE) {
            mSheetLayout.contractFab();
        }
    }

    /*****************
     * GET Customer
     ***************/

    public void GetInvoiceList() {

        String tag_json_obj = "json_obj_req";

        StringRequest request = new StringRequest(Request.Method.POST,
                Appconfig.url_receipt_list, new Response.Listener<String>() {

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

                            String dealer_name = obj1.getString(TAG_DEALER_NAME);
                            String customer_name = obj1.getString(TAG_CUSTOMER_NAME);
                            String date = obj1.getString(TAG_DATE);
                            String time = obj1.getString(TAG_TIME);
                            String receipt_no = obj1.getString(TAG_RECEIPT_NO);
                            String received_amount = obj1.getString(TAG_RECEIVED_AMT);
                            String payment_type = obj1.getString(TAG_PAYMENT_TYPE);
                            String chequeno = obj1.getString(TAG_CHEQUE_NO);
                            String bankname = obj1.getString(TAG_BANK_NAME);

                            // creating new HashMap
                            HashMap<String, String> map = new HashMap<String, String>();

                            // adding each child node to HashMap key => value
                            map.put(TAG_DEALER_NAME, dealer_name);
                            map.put(TAG_CUSTOMER_NAME, customer_name);
                            map.put(TAG_DATE, date);
                            map.put(TAG_TIME, time);
                            map.put(TAG_RECEIPT_NO, receipt_no);
                            map.put(TAG_RECEIVED_AMT, received_amount);
                            map.put(TAG_PAYMENT_TYPE, payment_type);
                            map.put(TAG_CHEQUE_NO, chequeno);
                            map.put(TAG_BANK_NAME, bankname);

                            received_cash_list.add(map);

                            adapter = new Adapter_Manage_Receive_Cash(Activity_Manage_Received_Cash.this,
                                    received_cash_list);
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

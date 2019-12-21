package com.banyan.aquabill.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
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
import com.banyan.aquabill.adapter.Adapter_Purchase_RawMaterial;
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

public class Activity_Purchase_Raw_Material extends AppCompatActivity implements SheetLayout.OnFabAnimationEndListener, SwipeRefreshLayout.OnRefreshListener{

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

    static ArrayList<HashMap<String, String>> purchase_list;
    HashMap<String, String> params = new HashMap<String, String>();

    public static final String TAG_PURCHASE_ID = "purchase_id";
    public static final String TAG_INVOICE_NO = "invoice_no";
    public static final String TAG_PURCHASE_DATE = "purchase_date";
    public static final String TAG_SUPPLIER_ID = "supplier_id";
    public static final String TAG_SUPPLIER = "supplier";
    public static final String TAG_SUPPLIER_ADDRESS = "address";
    public static final String TAG_SUPPLIER_GST = "gst";
    public static final String TAG_TOTAL_AMOUNT = "total_amount";

    private Adapter_Purchase_RawMaterial adapter;

    String str_dealer_id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_purchase_rawmaterial);

        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        parent_view = findViewById(android.R.id.content);

        setSupportActionBar(mToolbar);
        getSupportActionBar().setTitle("Manage Raw Material");
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        mToolbar.setNavigationIcon(getResources().getDrawable(R.drawable.ic_action_back));
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent i = new Intent(getApplicationContext(), MainActivity.class);
                i.putExtra("manage_class","master");
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

        purchase_list = new ArrayList<HashMap<String, String>>();

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
                                            queue = Volley.newRequestQueue(Activity_Purchase_Raw_Material.this);
                                            GetTodayPurchaseList();

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

                String str_purchase_id = purchase_list.get(position).get(TAG_PURCHASE_ID);
                String str_invoice_no = purchase_list.get(position).get(TAG_INVOICE_NO);
                String str_purchase_date = purchase_list.get(position).get(TAG_PURCHASE_DATE);
                String str_supplier_id = purchase_list.get(position).get(TAG_SUPPLIER_ID);
                String str_supplier = purchase_list.get(position).get(TAG_SUPPLIER);
                String str_supplier_address = purchase_list.get(position).get(TAG_SUPPLIER_ADDRESS);
                String str_supplier_gst = purchase_list.get(position).get(TAG_SUPPLIER_GST);
                String str_total_amt = purchase_list.get(position).get(TAG_TOTAL_AMOUNT);

                SharedPreferences sharedPreferences = PreferenceManager
                        .getDefaultSharedPreferences(Activity_Purchase_Raw_Material.this);
                SharedPreferences.Editor editor = sharedPreferences.edit();

                editor.putString("str_purchase_id", str_purchase_id);
                editor.putString("str_invoice_no", str_invoice_no);
                editor.putString("str_purchase_date", str_purchase_date);
                editor.putString("str_supplier_id", str_supplier_id);
                editor.putString("str_supplier", str_supplier);
                editor.putString("str_supplier_address", str_supplier_address);
                editor.putString("str_supplier_gst", str_supplier_gst);
                editor.putString("str_total_amt", str_total_amt);

                editor.commit();

                Intent i = new Intent(Activity_Purchase_Raw_Material.this, Activity_View_Purchase_Raw_Material.class);
                startActivity(i);
            }

        });
    }


    /**
     * This method is called when swipe refresh is pulled down
     */
    @Override
    public void onRefresh() {
        try {
            purchase_list.clear();
            queue = Volley.newRequestQueue(Activity_Purchase_Raw_Material.this);
            GetTodayPurchaseList();

        } catch (Exception e) {
            // TODO: handle exception
        }
    }


    @Override
    public void onFabAnimationEnd() {
        Intent intent = new Intent(Activity_Purchase_Raw_Material.this, Activity_Add_Purchase.class);
        startActivityForResult(intent, REQUEST_CODE);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE) {
            mSheetLayout.contractFab();
        }
    }

    /*****************************
     * GET REQ
     ***************/

    public void GetTodayPurchaseList() {

        String tag_json_obj = "json_obj_req";

        StringRequest request = new StringRequest(Request.Method.POST,
                Appconfig.url_list_purchase, new Response.Listener<String>() {

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

                            String purchase_id = obj1.getString(TAG_PURCHASE_ID);
                            String invoice_no = obj1.getString(TAG_INVOICE_NO);
                            String purchase_date = obj1.getString(TAG_PURCHASE_DATE);
                            String str_supplier_id = obj1.getString(TAG_SUPPLIER_ID);
                            String str_supplier = obj1.getString(TAG_SUPPLIER);
                            String str_supplier_address = obj1.getString(TAG_SUPPLIER_ADDRESS);
                            String str_supplier_gst = obj1.getString(TAG_SUPPLIER_GST);
                            String str_total_amount = obj1.getString(TAG_TOTAL_AMOUNT);

                            // creating new HashMap
                            HashMap<String, String> map = new HashMap<String, String>();

                            // adding each child node to HashMap key => value
                            map.put(TAG_PURCHASE_ID, purchase_id);
                            map.put(TAG_INVOICE_NO, invoice_no);
                            map.put(TAG_PURCHASE_DATE, purchase_date);
                            map.put(TAG_SUPPLIER_ID, str_supplier_id);
                            map.put(TAG_SUPPLIER, str_supplier);
                            map.put(TAG_SUPPLIER_ADDRESS, str_supplier_address);
                            map.put(TAG_SUPPLIER_GST, str_supplier_gst);
                            map.put(TAG_TOTAL_AMOUNT, str_total_amount);

                            purchase_list.add(map);

                            adapter = new Adapter_Purchase_RawMaterial(Activity_Purchase_Raw_Material.this,
                                    purchase_list);
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

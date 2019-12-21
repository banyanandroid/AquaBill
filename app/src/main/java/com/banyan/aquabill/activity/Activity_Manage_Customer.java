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
import com.banyan.aquabill.adapter.Adapter_Manage_Customer;
import com.banyan.aquabill.adapter.Adapter_Manage_Supplier;
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

public class Activity_Manage_Customer extends AppCompatActivity implements SheetLayout.OnFabAnimationEndListener, SwipeRefreshLayout.OnRefreshListener{

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

    static ArrayList<HashMap<String, String>> Customer_list;
    HashMap<String, String> params = new HashMap<String, String>();

    public static final String TAG_CUSTOMER_ID = "customer_id";
    public static final String TAG_CUSTOMER_NAME = "customer_name";
    public static final String TAG_CONTACTPERSON = "contact_person";
    public static final String TAG_MOBILE_NO = "mobileno";
    public static final String TAG_EMAIL = "email_id";

    private Adapter_Manage_Customer adapter;

    String str_dealer_id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_customer);

        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        parent_view = findViewById(android.R.id.content);

        setSupportActionBar(mToolbar);
        getSupportActionBar().setTitle("Manage Customer");
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

        Customer_list = new ArrayList<HashMap<String, String>>();

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
                                            queue = Volley.newRequestQueue(Activity_Manage_Customer.this);
                                            GetCustomerList();

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

                String str_supplier_id = Customer_list.get(position).get(TAG_CUSTOMER_ID);

                SharedPreferences sharedPreferences = PreferenceManager
                        .getDefaultSharedPreferences(Activity_Manage_Customer.this);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putString("str_customer_id", str_supplier_id);
                editor.commit();

                Intent i = new Intent(Activity_Manage_Customer.this, Activity_View_Customer.class);
                startActivity(i);
                finish();

            }

        });

    }


    /**
     * This method is called when swipe refresh is pulled down
     */
    @Override
    public void onRefresh() {
        try {
            Customer_list.clear();
            queue = Volley.newRequestQueue(Activity_Manage_Customer.this);
            GetCustomerList();

        } catch (Exception e) {
            // TODO: handle exception
        }
    }


    @Override
    public void onFabAnimationEnd() {
        Intent intent = new Intent(Activity_Manage_Customer.this, Activity_Add_Customer.class);
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

    public void GetCustomerList() {

        String tag_json_obj = "json_obj_req";

        StringRequest request = new StringRequest(Request.Method.POST,
                Appconfig.url_list_customer, new Response.Listener<String>() {

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

                            String customer_id = obj1.getString(TAG_CUSTOMER_ID);
                            String customer_name = obj1.getString(TAG_CUSTOMER_NAME);
                            String contact_person = obj1.getString(TAG_CONTACTPERSON);
                            String mobileno = obj1.getString(TAG_MOBILE_NO);
                            String email_id = obj1.getString(TAG_EMAIL);

                            // creating new HashMap
                            HashMap<String, String> map = new HashMap<String, String>();

                            // adding each child node to HashMap key => value
                            map.put(TAG_CUSTOMER_ID, customer_id);
                            map.put(TAG_CUSTOMER_NAME, customer_name);
                            map.put(TAG_CONTACTPERSON, contact_person);
                            map.put(TAG_MOBILE_NO, mobileno);
                            map.put(TAG_EMAIL, email_id);

                            Customer_list.add(map);

                            adapter = new Adapter_Manage_Customer(Activity_Manage_Customer.this,
                                    Customer_list);
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

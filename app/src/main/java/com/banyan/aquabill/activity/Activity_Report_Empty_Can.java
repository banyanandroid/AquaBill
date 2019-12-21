package com.banyan.aquabill.activity;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
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
import com.banyan.aquabill.adapter.Adapter_Report_Sales;
import com.banyan.aquabill.global.Appconfig;
import com.banyan.aquabill.global.SessionManager;
import com.google.android.material.snackbar.Snackbar;
import com.toptoche.searchablespinnerlibrary.SearchableSpinner;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import dmax.dialog.SpotsDialog;

public class Activity_Report_Empty_Can extends AppCompatActivity {

    String TAG = "Products and Rates";
    private View parent_view;

    // Session Manager Class
    SessionManager session;
    private static long back_pressed;

    SpotsDialog dialog;
    public static RequestQueue queue;

    private Toolbar mToolbar;

    String str_dealer_id;

    private SearchableSpinner spn_customer, spn_product;

    private Button btn_get_data;

    TextView t1;

    private ListView list_report;

    ArrayList<String> Arraylist_customer_id = null;
    ArrayList<String> Arraylist_customer_name = null;

    ArrayList<String> Arraylist_product_id = null;
    ArrayList<String> Arraylist_product_name = null;

    static ArrayList<HashMap<String, String>> report_empty_can_list;
    HashMap<String, String> params = new HashMap<String, String>();

    String  str_customer_id, str_customer, str_product_id, str_product;

    public static final String TAG_DEALER_NAME = "delaer_name";
    public static final String TAG_CUSTOMER_NAME = "customer_name";
    public static final String TAG_PRODUCT_NAME = "product_name";
    public static final String TAG_STOCK = "stock";

    private Adapter_Report_Empty_Can adapter;

    String str_from_date, str_to_date;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report_empty_can);

        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        parent_view = findViewById(android.R.id.content);

        setSupportActionBar(mToolbar);
        getSupportActionBar().setTitle("Empty Can Report");
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

        spn_customer = (SearchableSpinner) findViewById(R.id.report_recei_can_spn_customer);
        spn_product = (SearchableSpinner) findViewById(R.id.report_recei_can_spn_product);

        btn_get_data = (Button) findViewById(R.id.report_recei_can_btn_get_Data);

        list_report = (ListView)  findViewById(R.id.report_recei_can_listview);

        Arraylist_customer_id = new ArrayList<String>();
        Arraylist_customer_name = new ArrayList<String>();

        Arraylist_product_id = new ArrayList<String>();
        Arraylist_product_name = new ArrayList<String>();

        report_empty_can_list = new ArrayList<HashMap<String, String>>();

        spn_customer.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                System.out.println("Shop");

                t1 = (TextView) view;
                String str_state = t1.getText().toString();
                str_customer_id = Arraylist_customer_id.get(position);
                str_customer = Arraylist_customer_name.get(position);

                System.out.println("CUST ID : " + str_customer_id);
                System.out.println("CUST NAME : " + str_customer);

                try {
                    dialog = new SpotsDialog(Activity_Report_Empty_Can.this);
                    dialog.show();
                    queue = Volley.newRequestQueue(Activity_Report_Empty_Can.this);
                    Action_Get_Product();
                }catch (Exception e) {

                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        spn_product.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                System.out.println("Shop");

                t1 = (TextView) view;
                String str_state = t1.getText().toString();
                str_product_id = Arraylist_product_id.get(position);
                str_product = Arraylist_product_name.get(position);

                System.out.println("PROD ID : " + str_product_id);
                System.out.println("PROD NAME : " + str_product);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        btn_get_data.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                /*if (str_customer_id == null || str_customer_id.equals("")){
                    Snackbar.make(parent_view, "Please Select Customer", Snackbar.LENGTH_SHORT).show();
                }else if (str_product_id == null || str_product_id.equals("")){
                    Snackbar.make(parent_view, "Please Select Product", Snackbar.LENGTH_SHORT).show();
                }else {
                    try {
                        dialog = new SpotsDialog(Activity_Report_Empty_Can.this);
                        dialog.show();
                        queue = Volley.newRequestQueue(Activity_Report_Empty_Can.this);
                        Action_Get_Record();
                    }catch (Exception e) {

                    }
                }*/
                try {
                    dialog = new SpotsDialog(Activity_Report_Empty_Can.this);
                    dialog.show();
                    queue = Volley.newRequestQueue(Activity_Report_Empty_Can.this);
                    Action_Get_Record();
                }catch (Exception e) {

                }
            }
        });

        try {
            dialog = new SpotsDialog(Activity_Report_Empty_Can.this);
            dialog.show();
            queue = Volley.newRequestQueue(Activity_Report_Empty_Can.this);
            Action_Get_Record();
            Action_Get_Customers();
        }catch (Exception e) {

        }
    }

    /***************************
     * GET Customers
     ***************************/
    public void Action_Get_Customers() {

        String tag_json_obj = "json_obj_req";
        System.out.println("CAME 1");
        final StringRequest request = new StringRequest(Request.Method.POST,
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

                        Arraylist_customer_id.clear();
                        Arraylist_customer_name.clear();

                        for (int i = 0; arr.length() > i; i++) {
                            JSONObject obj1 = arr.getJSONObject(i);

                            String customer_id = obj1.getString("customer_id");
                            String customer_name = obj1.getString("customer_name");

                            Arraylist_customer_id.add(customer_id);
                            Arraylist_customer_name.add(customer_name);

                            try {
                                spn_customer
                                        .setAdapter(new ArrayAdapter<String>(Activity_Report_Empty_Can.this,
                                                android.R.layout.simple_spinner_dropdown_item,
                                                Arraylist_customer_name));

                            } catch (Exception e) {

                            }
                        }

                        dialog.dismiss();
                    } else if (success == 0) {

                        dialog.dismiss();

                        Snackbar.make(parent_view, "No Customer Data Found", Snackbar.LENGTH_SHORT).show();

                    }

                    dialog.dismiss();
                } catch (JSONException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {

                dialog.dismiss();

                Snackbar.make(parent_view, "Error : " + error, Snackbar.LENGTH_SHORT).show();
            }
        }) {

            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();

                params.put("user_id", str_dealer_id);

                return params;
            }

        };

        // Adding request to request queue
        queue.add(request);
    }

    /***************************
     * GET Product
     ***************************/
    public void Action_Get_Product() {

        String tag_json_obj = "json_obj_req";
        System.out.println("CAME 1");
        final StringRequest request = new StringRequest(Request.Method.POST,
                Appconfig.url_list_can, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.d(TAG, response.toString());
                try {
                    JSONObject obj = new JSONObject(response);
                    int success = obj.getInt("status");

                    if (success == 1) {

                        JSONArray arr;
                        arr = obj.getJSONArray("data");

                        Arraylist_product_id.clear();
                        Arraylist_product_name.clear();

                        for (int i = 0; arr.length() > i; i++) {
                            JSONObject obj1 = arr.getJSONObject(i);

                            String prod_id = obj1.getString("proId");
                            String prod_name = obj1.getString("pro_name");

                            Arraylist_product_id.add(prod_id);
                            Arraylist_product_name.add(prod_name);

                            try {
                                spn_product
                                        .setAdapter(new ArrayAdapter<String>(Activity_Report_Empty_Can.this,
                                                android.R.layout.simple_spinner_dropdown_item,
                                                Arraylist_product_name));

                            } catch (Exception e) {

                            }

                        }
                        dialog.dismiss();
                    } else if (success == 0) {

                        dialog.dismiss();

                        Snackbar.make(parent_view, "No Product Data Found", Snackbar.LENGTH_SHORT).show();

                    }

                    dialog.dismiss();
                } catch (JSONException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {

                dialog.dismiss();

                Snackbar.make(parent_view, "Error : " + error, Snackbar.LENGTH_SHORT).show();
            }
        }) {

            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();

                params.put("customer_id", str_customer_id);

                System.out.println("CUST ID : "+ str_customer_id);
                return params;
            }

        };

        // Adding request to request queue
        queue.add(request);
    }

    /***************************
     * GET Record
     ***************************/

    public void Action_Get_Record() {

        String tag_json_obj = "json_obj_req";

        StringRequest request = new StringRequest(Request.Method.POST,
                Appconfig.url_emptycan_reports, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.d(TAG, response.toString());
                try {
                    JSONObject obj = new JSONObject(response);
                    int success = obj.getInt("status");

                    if (success == 1) {

                        JSONArray arr;

                        arr = obj.getJSONArray("data");

                        report_empty_can_list.clear();

                        for (int i = 0; arr.length() > i; i++) {
                            JSONObject obj1 = arr.getJSONObject(i);

                            String dealer_name = obj1.getString(TAG_DEALER_NAME);
                            String customer_name = obj1.getString(TAG_CUSTOMER_NAME);
                            String product_name = obj1.getString(TAG_PRODUCT_NAME);
                            String stock = obj1.getString(TAG_STOCK);

                            // creating new HashMap
                            HashMap<String, String> map = new HashMap<String, String>();

                            // adding each child node to HashMap key => value
                            map.put(TAG_DEALER_NAME, dealer_name);
                            map.put(TAG_CUSTOMER_NAME, customer_name);
                            map.put(TAG_PRODUCT_NAME, product_name);
                            map.put(TAG_STOCK, stock);

                            report_empty_can_list.add(map);

                            adapter = new Adapter_Report_Empty_Can(Activity_Report_Empty_Can.this,
                                    report_empty_can_list);
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
                params.put("cus_id", str_customer_id);
                params.put("product_id", str_product_id);

                System.out.println("USER ID : " + str_dealer_id);
                System.out.println("str_customer_id : " + str_customer_id);
                System.out.println("str_product_id : " + str_product_id);

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

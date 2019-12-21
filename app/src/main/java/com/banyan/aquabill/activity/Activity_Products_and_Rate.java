package com.banyan.aquabill.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.banyan.aquabill.R;
import com.banyan.aquabill.adapter.Adapter_ProductsAndRate;
import com.banyan.aquabill.global.Appconfig;
import com.banyan.aquabill.global.SessionManager;
import com.google.android.material.snackbar.Snackbar;
import com.toptoche.searchablespinnerlibrary.SearchableSpinner;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import dmax.dialog.SpotsDialog;

public class Activity_Products_and_Rate extends AppCompatActivity {

    String TAG = "Products and Rates";
    private View parent_view;
    private Toolbar mToolbar;

    SessionManager session;
    private static long back_pressed;

    SpotsDialog dialog;
    public static RequestQueue queue;

    private SearchableSpinner spn_brand;
    private ListView recyclerView_list;
    private TextView t1;
    private String str_select_brand_id, str_select_brand;

    ArrayList<String> Arraylist_brand_id = null;
    ArrayList<String> Arraylist_brand_name = null;

    private String str_dealer_id, Str_select_pdu_id;

    public static final String TAG_PRODUCT = "product";
    public static final String TAG_QTY = "quantity";
    public static final String TAG_PACKING = "packing";
    public static final String TAG_PACKING_RATE = "purchase_rate";

    public Adapter_ProductsAndRate adapter;
    static ArrayList<HashMap<String, String>> productsandrates;

    HashMap<String, String> params = new HashMap<String, String>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_products_and_rates);

        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        parent_view = findViewById(android.R.id.content);

        setSupportActionBar(mToolbar);
        getSupportActionBar().setTitle("Products & Rates");
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        mToolbar.setNavigationIcon(getResources().getDrawable(R.drawable.ic_action_back));
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent i = new Intent(getApplicationContext(), MainActivity.class);
                i.putExtra("manage_class","managepdu");
                startActivity(i);
                finish();
            }
        });

        SharedPreferences sharedPreferences = PreferenceManager
                .getDefaultSharedPreferences(Activity_Products_and_Rate.this);
        Str_select_pdu_id = sharedPreferences.getString("selected_pdu_id", "selected_pdu_id");

        // Session Manager
        session = new SessionManager(Activity_Products_and_Rate.this);
        HashMap<String, String> user = session.getUserDetails();
        str_dealer_id = user.get(SessionManager.KEY_DEALER_ID);


        Arraylist_brand_id = new ArrayList<String>();
        Arraylist_brand_name = new ArrayList<String>();
        // Hashmap for ListView
        productsandrates = new ArrayList<HashMap<String, String>>();

        spn_brand = (SearchableSpinner) findViewById(R.id.product_rates_spn_brand);
        recyclerView_list = (ListView) findViewById(R.id.product_rates_recyler_list);

        spn_brand.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                System.out.println("Shop");

                t1 = (TextView) view;
                String str_state = t1.getText().toString();
                str_select_brand_id = Arraylist_brand_id.get(position);
                str_select_brand = Arraylist_brand_name.get(position);

                System.out.println("BRAND ID : " + str_select_brand_id);
                System.out.println("BRAND NAME : " + str_select_brand);

                try {
                    dialog = new SpotsDialog(Activity_Products_and_Rate.this);
                    dialog.show();
                    queue = Volley.newRequestQueue(Activity_Products_and_Rate.this);
                    Get_Report();
                }catch (Exception e) {

                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        try {
            dialog = new SpotsDialog(Activity_Products_and_Rate.this);
            dialog.show();
            queue = Volley.newRequestQueue(Activity_Products_and_Rate.this);
            Action_Get_Brand();
        }catch (Exception e) {

        }

    }

    /********************************
     * Get Brand
     *********************************/
    private void Action_Get_Brand() {

        StringRequest request = new StringRequest(Request.Method.POST,
                Appconfig.url_brand_list, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.d(TAG, response.toString());
                try {
                    JSONObject obj = new JSONObject(response);
                    int success = obj.getInt("status");

                    if (success == 1) {

                        JSONArray arr;

                        arr = obj.getJSONArray("data");

                        Arraylist_brand_id.clear();
                        Arraylist_brand_name.clear();

                        for (int i = 0; arr.length() > i; i++) {
                            JSONObject obj1 = arr.getJSONObject(i);

                            String brand_id = obj1.getString("pdubrand_id");
                            String brand_name = obj1.getString("brand");

                            Arraylist_brand_id.add(brand_id);
                            Arraylist_brand_name.add(brand_name);

                            try {
                                spn_brand
                                        .setAdapter(new ArrayAdapter<String>(getApplicationContext(),
                                                android.R.layout.simple_spinner_dropdown_item,
                                                Arraylist_brand_name));

                            } catch (Exception e) {

                            }

                        }

                        dialog.dismiss();
                    } else if (success == 0) {

                        dialog.dismiss();

                        Snackbar.make(parent_view, "No Brand Data Found", Snackbar.LENGTH_SHORT).show();

                    }

                    dialog.dismiss();
                } catch (JSONException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
                dialog.dismiss();
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

                params.put("pdu_id", Str_select_pdu_id);

                System.out.println("pdu_id " + Str_select_pdu_id);

                return params;
            }
        };
        // Adding request to request queue
        queue.add(request);
    }

    /********************************
     * Get Report
     *********************************/

    public void Get_Report() {

        StringRequest request = new StringRequest(Request.Method.POST,
                Appconfig.url_product_details, new Response.Listener<String>() {

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

                            String product = obj1.getString(TAG_PRODUCT);
                            String qty = obj1.getString(TAG_QTY);
                            String packing = obj1.getString(TAG_PACKING);
                            String purchase_rate = obj1.getString(TAG_PACKING_RATE);

                            // creating new HashMap
                            HashMap<String, String> map = new HashMap<String, String>();

                            map.put(TAG_PRODUCT, product);
                            map.put(TAG_QTY, qty);
                            map.put(TAG_PACKING, packing);
                            map.put(TAG_PACKING_RATE, purchase_rate);

                            productsandrates.add(map);

                            adapter = new Adapter_ProductsAndRate(Activity_Products_and_Rate.this,
                                    productsandrates);
                            recyclerView_list.setAdapter(adapter);

                        }

                    } else if (success == 0) {

                        dialog.dismiss();
                        Toast.makeText(getApplicationContext(), "No Data Found", Toast.LENGTH_SHORT).show();

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

                dialog.dismiss();

            }
        }) {

            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();

                params.put("pdu_id", Str_select_pdu_id);
                params.put("user_id", str_dealer_id);
                params.put("brand_id", str_select_brand_id);

                System.out.println("pdu_id : " + Str_select_pdu_id);
                System.out.println("user_id : " + str_dealer_id);
                System.out.println("brand_id : " + str_select_brand_id);

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

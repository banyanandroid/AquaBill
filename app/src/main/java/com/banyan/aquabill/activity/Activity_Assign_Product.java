package com.banyan.aquabill.activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.banyan.aquabill.R;
import com.banyan.aquabill.adapter.Adapter_Assign_Product;
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
import java.util.List;
import java.util.Map;

import dmax.dialog.SpotsDialog;

public class Activity_Assign_Product extends AppCompatActivity {

    String TAG = "Assign Product";
    private View parent_view;

    // Session Manager Class
    SessionManager session;
    private static long back_pressed;

    SpotsDialog dialog;
    public static RequestQueue queue;

    private Toolbar mToolbar;

    private SearchableSpinner spn_customer, spn_pdu, spn_brand;

    private EditText alert_edt_sell_price;

    private TextView alert_txt_prod, alert_txt_price;

    private TextView txt_msg;

    private ListView list_prod;

    ArrayList<String> Arraylist_customer_id = null;
    ArrayList<String> Arraylist_customer_name = null;

    ArrayList<String> Arraylist_pdu_id = null;
    ArrayList<String> Arraylist_pdu_name = null;

    ArrayList<String> Arraylist_brand_id = null;
    ArrayList<String> Arraylist_brand_name = null;

    TextView t1;

    String str_dealer_id;

    private String str_customer_id, str_customer, str_pdu_id, str_pdu, str_brand_id, str_brand;

    public Adapter_Assign_Product adapter;

    static ArrayList<HashMap<String, String>> product_details;

    public static final String TAG_PRODUCT_ID = "product_id";
    public static final String TAG_PRODUCT = "product";
    public static final String TAG_QTY = "quantity";
    public static final String TAG_PACKING = "packing";
    public static final String TAG_PURCHASE_RATE = "purchase_rate";
    public static final String TAG_SELLING_RATE = "selling_rate";

    private String str_up_sell_price;

    private String str_list_selected_price, str_list_selected_purchase_price;
    private String str_list_prod_id, str_list_prod, str_list_sell_price;

    private Double doub_purchase_price, doub_sell_price;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_assign_product);

        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        parent_view = findViewById(android.R.id.content);

        setSupportActionBar(mToolbar);
        getSupportActionBar().setTitle("Assign Product");
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

        Arraylist_customer_id = new ArrayList<String>();
        Arraylist_customer_name = new ArrayList<String>();

        Arraylist_pdu_id = new ArrayList<String>();
        Arraylist_pdu_name = new ArrayList<String>();

        Arraylist_brand_id = new ArrayList<String>();
        Arraylist_brand_name = new ArrayList<String>();

        product_details = new ArrayList<HashMap<String, String>>();

        spn_customer = (SearchableSpinner) findViewById(R.id.assign_prod_spn_customer);
        spn_pdu = (SearchableSpinner) findViewById(R.id.assign_prod_spn_pdu);
        spn_brand = (SearchableSpinner) findViewById(R.id.assign_prod_spn_brand);

        list_prod = (ListView) findViewById(R.id.assign_prod_list);

        spn_customer.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                System.out.println("Shop");

                t1 = (TextView) view;
                String str_state = t1.getText().toString();
                str_customer_id = Arraylist_customer_id.get(position);
                str_customer = Arraylist_customer_name.get(position);

                System.out.println("CUST ID : " + str_customer_id);
                System.out.println("CUST NAME : " + str_customer_id);

                try {
                    dialog = new SpotsDialog(Activity_Assign_Product.this);
                    dialog.show();
                    queue = Volley.newRequestQueue(Activity_Assign_Product.this);
                    Action_Get_PDU();
                }catch (Exception e) {

                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        spn_pdu.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                System.out.println("PDU");

                t1 = (TextView) view;
                String str_state = t1.getText().toString();
                str_pdu_id = Arraylist_pdu_id.get(position);
                str_pdu = Arraylist_pdu_name.get(position);

                System.out.println("PDU ID : " + str_pdu_id);
                System.out.println("PDU NAME : " + str_pdu);

                try {
                    dialog = new SpotsDialog(Activity_Assign_Product.this);
                    dialog.show();
                    queue = Volley.newRequestQueue(Activity_Assign_Product.this);
                    Action_Get_Brand();
                }catch (Exception e) {

                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        spn_brand.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                System.out.println("PDU");

                t1 = (TextView) view;
                String str_state = t1.getText().toString();
                str_brand_id = Arraylist_brand_id.get(position);
                str_brand = Arraylist_brand_name.get(position);

                System.out.println("BRAND ID : " + str_brand_id);
                System.out.println("BRAND NAME : " + str_brand);


                try {
                    dialog = new SpotsDialog(Activity_Assign_Product.this);
                    dialog.show();
                    queue = Volley.newRequestQueue(Activity_Assign_Product.this);
                    Action_Get_Products_Prices();
                }catch (Exception e) {

                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        list_prod.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {

                str_list_prod_id = product_details.get(position).get(TAG_PRODUCT_ID);
                str_list_prod = product_details.get(position).get(TAG_PRODUCT);
                str_list_selected_price = product_details.get(position).get(TAG_SELLING_RATE);
                str_list_selected_purchase_price = product_details.get(position).get(TAG_PURCHASE_RATE);

                System.out.println("Selected_PROD : " + str_list_prod_id);
                System.out.println("Selected_PURCHASE_PRICE : " + str_list_selected_purchase_price);

                AlertDialog(str_list_prod_id, str_list_prod, str_list_selected_price);
            }

        });


        try {
            dialog = new SpotsDialog(Activity_Assign_Product.this);
            dialog.show();
            queue = Volley.newRequestQueue(Activity_Assign_Product.this);
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
                                        .setAdapter(new ArrayAdapter<String>(Activity_Assign_Product.this,
                                                android.R.layout.simple_spinner_dropdown_item,
                                                Arraylist_customer_name));

                            } catch (Exception e) {

                            }

                        }

                        dialog.dismiss();
                    } else if (success == 0) {

                        dialog.dismiss();

                        Snackbar.make(parent_view, "No State Data Found", Snackbar.LENGTH_SHORT).show();

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
     * GET Customers
     ***************************/
    public void Action_Get_PDU() {

        String tag_json_obj = "json_obj_req";
        System.out.println("CAME 1");
        final StringRequest request = new StringRequest(Request.Method.POST,
                Appconfig.url_dealerpdu, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.d(TAG, response.toString());
                try {
                    JSONObject obj = new JSONObject(response);
                    int success = obj.getInt("status");

                    if (success == 1) {

                        JSONArray arr;

                        arr = obj.getJSONArray("data");

                        Arraylist_pdu_id.clear();
                        Arraylist_pdu_name.clear();

                        for (int i = 0; arr.length() > i; i++) {
                            JSONObject obj1 = arr.getJSONObject(i);

                            String pdu_id = obj1.getString("pdu_id");
                            String pdu_name = obj1.getString("pdu_name");

                            Arraylist_pdu_id.add(pdu_id);
                            Arraylist_pdu_name.add(pdu_name);

                            try {
                                spn_pdu
                                        .setAdapter(new ArrayAdapter<String>(Activity_Assign_Product.this,
                                                android.R.layout.simple_spinner_dropdown_item,
                                                Arraylist_pdu_name));

                            } catch (Exception e) {

                            }

                        }

                        dialog.dismiss();
                    } else if (success == 0) {

                        dialog.dismiss();

                        Snackbar.make(parent_view, "No State Data Found", Snackbar.LENGTH_SHORT).show();

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
     * GET Brand
     ***************************/
    public void Action_Get_Brand() {

        String tag_json_obj = "json_obj_req";
        System.out.println("CAME 1");
        final StringRequest request = new StringRequest(Request.Method.POST,
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

                            String state_id = obj1.getString("pdubrand_id");
                            String state_name = obj1.getString("brand");

                            Arraylist_brand_id.add(state_id);
                            Arraylist_brand_name.add(state_name);

                            try {
                                spn_brand
                                        .setAdapter(new ArrayAdapter<String>(Activity_Assign_Product.this,
                                                android.R.layout.simple_spinner_dropdown_item,
                                                Arraylist_brand_name));

                            } catch (Exception e) {

                            }

                        }

                        dialog.dismiss();
                    } else if (success == 0) {

                        dialog.dismiss();

                        Snackbar.make(parent_view, "No State Data Found", Snackbar.LENGTH_SHORT).show();

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

                params.put("pdu_id", str_pdu_id);

                return params;
            }

        };

        // Adding request to request queue
        queue.add(request);
    }

    /***************************
     * GET Products & Price
     ***************************/
    public void Action_Get_Products_Prices() {

        String tag_json_obj = "json_obj_req";
        System.out.println("CAME 1");
        final StringRequest request = new StringRequest(Request.Method.POST,
                Appconfig.url_customer_pricedetails, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.d(TAG, response.toString());
                try {
                    JSONObject obj = new JSONObject(response);
                    int success = obj.getInt("status");

                    if (success == 1) {

                        JSONArray arr;

                        arr = obj.getJSONArray("data");

                        product_details.clear();

                        for (int i = 0; arr.length() > i; i++) {
                            JSONObject obj1 = arr.getJSONObject(i);

                            String product_id = obj1.getString(TAG_PRODUCT_ID);
                            String product = obj1.getString(TAG_PRODUCT);
                            String qty = obj1.getString(TAG_QTY);
                            String packing = obj1.getString(TAG_PACKING);
                            String purchase_rate = obj1.getString(TAG_PURCHASE_RATE);
                            String selling_rate = obj1.getString(TAG_SELLING_RATE);

                            // creating new HashMap
                            HashMap<String, String> map = new HashMap<String, String>();

                            map.put(TAG_PRODUCT_ID, product_id);
                            map.put(TAG_PRODUCT, product);
                            map.put(TAG_QTY, qty);
                            map.put(TAG_PACKING, packing);
                            map.put(TAG_PURCHASE_RATE, purchase_rate);
                            map.put(TAG_SELLING_RATE, selling_rate);

                            product_details.add(map);

                            adapter = new Adapter_Assign_Product(Activity_Assign_Product.this,
                                    product_details);
                            list_prod.setAdapter(adapter);

                        }

                        dialog.dismiss();
                    } else if (success == 0) {

                        dialog.dismiss();

                        Snackbar.make(parent_view, "No State Data Found", Snackbar.LENGTH_SHORT).show();

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
                params.put("customer_id", str_customer_id);
                params.put("pdu_id", str_pdu_id);
                params.put("brand_id", str_brand_id);

                return params;
            }

        };

        // Adding request to request queue
        queue.add(request);
    }

    private void AlertDialog(String str_pro_id, String str_prod, String str_sell_price){

        LayoutInflater li = LayoutInflater.from(Activity_Assign_Product.this);
        View promptsView = li.inflate(R.layout.alert_dialog_assign_prod_sellingprice_up, null);

        AlertDialog alert_dialog = new AlertDialog.Builder(Activity_Assign_Product.this)
                .setView(promptsView)
                .setTitle("Update Selling Price")
                .setPositiveButton(android.R.string.ok, null) //Set to null. We override the onclick
                .setNegativeButton(android.R.string.cancel, null)
                .create();

        alert_txt_prod = (TextView) promptsView.findViewById(R.id.alert_as_prod_txt_prod_name);
        alert_txt_price = (TextView) promptsView.findViewById(R.id.alert_as_prod_txt_sellprice);
        alert_edt_sell_price = (EditText) promptsView.findViewById(R.id.alert_as_prod_edt_sell_price);
        txt_msg = (TextView) promptsView.findViewById(R.id.alert_asProd_txt_errormsg);

        alert_txt_prod.setText("Product : "+str_prod);
        alert_txt_price.setText("Selling Price : "+str_sell_price);

        if (str_list_selected_purchase_price.equals("Not Assigned")){
            txt_msg.setText("You Cant Update Selling Price For this product");
            alert_edt_sell_price.setFocusable(false);
        }else {

        }

        alert_edt_sell_price.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {

                String selling_price = alert_edt_sell_price.getText().toString().trim();

                try {
                    if (str_list_selected_purchase_price != null && !str_list_selected_purchase_price.isEmpty() && !str_list_selected_purchase_price.equals("null")) {  //purchase value if not null

                        if (str_list_selected_purchase_price.equals("Not Assigned")){
                            txt_msg.setText("You Cant Update Selling Price For this product");
                        }else {
                            doub_purchase_price = Double.parseDouble(str_list_selected_purchase_price);
                            doub_sell_price = Double.parseDouble(selling_price);

                            if (doub_purchase_price < doub_sell_price){
                                txt_msg.setText("");
                            }else {
                                txt_msg.setText("Selling price is Lesser Than Purchase price");
                            }

                        }

                    }else { //purchase value if null

                        alert_edt_sell_price.setText("");
                        txt_msg.setText("You Cant Update Selling Price For this product");

                    }

                }catch (Exception e){

                }
            }
        });

        alert_dialog.setOnShowListener(new DialogInterface.OnShowListener() {

            @Override
            public void onShow(final DialogInterface alert_dialog) {

                Button button = ((AlertDialog) alert_dialog).getButton(AlertDialog.BUTTON_POSITIVE);
                button.setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View view) {
                        // TODO Do something

                        str_up_sell_price = alert_edt_sell_price.getText().toString().trim();

                        if (str_up_sell_price.equals("")){
                            txt_msg.setText("Please Enter Selling Price");
                        }else {

                            alert_dialog.dismiss();

                            try {
                                dialog = new SpotsDialog(Activity_Assign_Product.this);
                                dialog.show();
                                queue = Volley.newRequestQueue(Activity_Assign_Product.this);
                                Action_Update_SellingPrice();
                            }catch (Exception e) {

                            }
                        }
                    }
                });
            }
        });

        alert_dialog.show();
    }


    /***************************
     * Update Selling Price
     ***************************/
    public void Action_Update_SellingPrice() {

        String tag_json_obj = "json_obj_req";
        System.out.println("CAME 1");
        final StringRequest request = new StringRequest(Request.Method.POST,
                Appconfig.url_customer_selling_price, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.d(TAG, response.toString());
                try {
                    JSONObject obj = new JSONObject(response);
                    int success = obj.getInt("status");

                    if (success == 1) {

                        new androidx.appcompat.app.AlertDialog.Builder(Activity_Assign_Product.this)
                                .setTitle("Aqua Bill")
                                .setMessage("Product Price Updated Successfully !")
                                .setIcon(R.mipmap.ic_launcher)
                                .setPositiveButton("Ok",
                                        new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface alert_dialog,

                                                                int which) {
                                                // TODO Auto-generated method stub
                                                alert_dialog.dismiss();

                                                try {

                                                    product_details.clear();
                                                    dialog = new SpotsDialog(Activity_Assign_Product.this);
                                                    dialog.show();
                                                    queue = Volley.newRequestQueue(Activity_Assign_Product.this);
                                                    Action_Get_Products_Prices();
                                                }catch (Exception e) {

                                                }

                                            }

                                        }).show();

                        dialog.dismiss();
                    } else if (success == 0) {

                        dialog.dismiss();

                        Snackbar.make(parent_view, "Not Updated Successfully", Snackbar.LENGTH_SHORT).show();

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
                params.put("customer_id", str_customer_id);
                params.put("pdu_id", str_pdu_id);
                params.put("brand_id", str_brand_id);
                params.put("product_id", str_list_prod_id);
                params.put("price", str_up_sell_price);

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

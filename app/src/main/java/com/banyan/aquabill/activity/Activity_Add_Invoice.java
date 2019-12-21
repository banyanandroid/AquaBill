package com.banyan.aquabill.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
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
import com.banyan.aquabill.global.Appconfig;
import com.banyan.aquabill.global.SessionManager;
import com.google.android.material.snackbar.Snackbar;
import com.toptoche.searchablespinnerlibrary.SearchableSpinner;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import dmax.dialog.SpotsDialog;

public class Activity_Add_Invoice extends AppCompatActivity {

    String TAG = "Add Invoice";
    private View parent_view;

    // Session Manager Class
    SessionManager session;
    private static long back_pressed;

    SpotsDialog dialog;
    public static RequestQueue queue;

    private Toolbar mToolbar;

    String str_dealer_id;

    private LinearLayout linear_payment;

    private EditText edt_rate, edt_qty, edt_total_amt, edt_bank_nam, edt_bank_ref_no, edt_amt_received,
            edt_20l_can_received, edt_10l_can_received;

    private TextView txt_date, txt_time, txt_invoice_no;

    private SearchableSpinner spn_customer, spn_brand, spn_product, spn_payment_type;

    private Button btn_save_invoice;

    private TextView t1;

    private String str_date, str_time, str_invoice_no, str_rate, str_qty, str_amt, str_can_received, str_customer_id, str_customer,
            str_customer_gst, str_brand_id, str_brand, str_product_id, str_product, str_product_stock, str_product_price,
            str_product_gst, str_product_act_price, str_product_tax_price, str_20l_can, str_10l_can;

    ArrayList<String> Arraylist_customer_id = null;
    ArrayList<String> Arraylist_customer_name = null;
    ArrayList<String> Arraylist_customer_gst = null;

    ArrayList<String> Arraylist_brand_id = null;
    ArrayList<String> Arraylist_brand_name = null;

    ArrayList<String> Arraylist_product_id = null;
    ArrayList<String> Arraylist_product_name = null;
    ArrayList<String> Arraylist_product_stock = null;
    ArrayList<String> Arraylist_product_price = null;
    ArrayList<String> Arraylist_product_gst = null;
    ArrayList<String> Arraylist_product_tax_price = null;

    private String str_up_date, str_up_time, str_up_invoice_no, str_up_price, str_up_qty, str_up_received_amt,
            str_up_10l_can_received, str_up_20l_can_received, str_payment_type, str_up_bank_name, str_up_ref_no;

    private Double int_received, int_total;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_invoice);

        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        parent_view = findViewById(android.R.id.content);

        setSupportActionBar(mToolbar);
        getSupportActionBar().setTitle("Add Invoice");
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        mToolbar.setNavigationIcon(getResources().getDrawable(R.drawable.ic_action_back));
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent i = new Intent(getApplicationContext(), Activity_Manage_Invoice.class);
                i.putExtra("manage_class","invoice");
                startActivity(i);
                finish();
            }
        });

        session = new SessionManager(getApplicationContext());
        HashMap<String, String> user = session.getUserDetails();
        str_dealer_id = user.get(SessionManager.KEY_DEALER_ID);

        txt_date = (TextView) findViewById(R.id.add_invoice_txt_date);
        txt_time = (TextView) findViewById(R.id.add_invoice_txt_time);
        txt_invoice_no = (TextView) findViewById(R.id.add_invoice_txt_invoie_no);

        edt_rate = (EditText) findViewById(R.id.add_invoice_edt_price);
        edt_qty = (EditText) findViewById(R.id.add_invoice_edt_qty);
        edt_total_amt = (EditText) findViewById(R.id.add_invoice_edt_total_amt);
        edt_amt_received = (EditText) findViewById(R.id.add_invoice_edt_cash_received);
        edt_20l_can_received = (EditText) findViewById(R.id.add_invoice_edt_cash_20lcanreceived);
        edt_10l_can_received = (EditText) findViewById(R.id.add_invoice_edt_cash_10lcanreceived);
        edt_bank_nam = (EditText) findViewById(R.id.add_invoice_edt_bank_name);
        edt_bank_ref_no = (EditText) findViewById(R.id.add_invoice_edt_ref_no);

        spn_customer = (SearchableSpinner)  findViewById(R.id.add_invoice_spn_customer);
        spn_brand = (SearchableSpinner)  findViewById(R.id.add_invoice_spn_brand);
        spn_product = (SearchableSpinner)  findViewById(R.id.add_invoice_spn_product);
        spn_payment_type = (SearchableSpinner)  findViewById(R.id.add_invoice_spn_payment);

        linear_payment = (LinearLayout) findViewById(R.id.add_invoice_linear_payment_type);

        btn_save_invoice = (Button) findViewById(R.id.add_invoice_btn_save);

        Arraylist_customer_id = new ArrayList<String>();
        Arraylist_customer_name = new ArrayList<String>();
        Arraylist_customer_gst  = new ArrayList<String>();

        Arraylist_brand_id = new ArrayList<String>();
        Arraylist_brand_name = new ArrayList<String>();

        Arraylist_product_id = new ArrayList<String>();
        Arraylist_product_name = new ArrayList<String>();
        Arraylist_product_stock = new ArrayList<String>();
        Arraylist_product_price = new ArrayList<String>();
        Arraylist_product_gst = new ArrayList<String>();
        Arraylist_product_tax_price = new ArrayList<String>();

        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat mdformat = new SimpleDateFormat("yyyy-MM-dd ");
        SimpleDateFormat mdformat1 = new SimpleDateFormat("HH:mm:ss");
        final String strDate = "" + mdformat.format(calendar.getTime());
        String strTime = "" + mdformat1.format(calendar.getTime());
        txt_date.setText(""+strDate);
        txt_time.setText(""+strTime);

        spn_customer.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                System.out.println("Shop");

                t1 = (TextView) view;
                String str_state = t1.getText().toString();
                str_customer_id = Arraylist_customer_id.get(position);
                str_customer = Arraylist_customer_name.get(position);
                str_customer_gst = Arraylist_customer_gst.get(position);

                System.out.println("CUST ID : " + str_customer_id);
                System.out.println("CUST NAME : " + str_customer);

                try {
                    dialog = new SpotsDialog(Activity_Add_Invoice.this);
                    dialog.show();
                    queue = Volley.newRequestQueue(Activity_Add_Invoice.this);
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

                System.out.println("Shop");

                t1 = (TextView) view;
                String str_state = t1.getText().toString();
                str_brand_id = Arraylist_brand_id.get(position);
                str_brand = Arraylist_brand_name.get(position);

                System.out.println("BRAND ID : " + str_brand_id);
                System.out.println("BRAND NAME : " + str_brand);

                try {
                    dialog = new SpotsDialog(Activity_Add_Invoice.this);
                    dialog.show();
                    queue = Volley.newRequestQueue(Activity_Add_Invoice.this);
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
                str_product_stock = Arraylist_product_stock.get(position);
                str_product_price = Arraylist_product_price.get(position);
                str_product_gst = Arraylist_product_gst.get(position);
                str_product_tax_price = Arraylist_product_tax_price.get(position);

                if (str_customer_gst.equals("1")){
                    str_product_act_price = str_product_tax_price;
                }else {
                    str_product_act_price = str_product_price;
                }

                edt_rate.setText(""+ str_product_act_price);

                System.out.println("PROD ID : " + str_product_id);
                System.out.println("PROD NAME : " + str_product);
                System.out.println("PROD STOCK : " + str_product_stock);
                System.out.println("PROD PRICE : " + str_product_price);
                System.out.println("PROD GST : " + str_product_gst);
                System.out.println("PROD TAX PRICE : " + str_product_tax_price);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        spn_payment_type.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                System.out.println("Shop");

                t1 = (TextView) view;
                String str_payment = t1.getText().toString();

                if (str_payment.equals("Cash")){
                    str_payment_type = "1";
                    linear_payment.setVisibility(View.GONE);
                }else if (str_payment.equals("Cheque")){
                    str_payment_type = "2";
                    linear_payment.setVisibility(View.VISIBLE);
                }else if (str_payment.equals("Bank Transfer")){
                    str_payment_type = "3";
                    linear_payment.setVisibility(View.VISIBLE);
                }else {
                    str_payment_type = "0";
                    linear_payment.setVisibility(View.GONE);
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        edt_qty.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {

                String str_get_qty = edt_qty.getText().toString().trim();

                System.out.println("VALUE : " + str_get_qty);
                System.out.println("PRICE : " + str_product_act_price);

                try {
                    if (str_get_qty != null && !str_get_qty.isEmpty() && !str_get_qty.equals("null")) {

                        System.out.println("str_product_stock : " + str_product_stock);

                        int int_stock = Integer.parseInt(str_product_stock);
                        int int_qty = Integer.parseInt(str_get_qty);

                        System.out.println("QTY : " + int_qty);
                        System.out.println("int_stock : " + int_stock);

                        if (int_qty <= int_stock) {

                            Double int_price = Double.parseDouble(str_product_act_price);

                            System.out.println("int_price : " + int_price);
                            if (int_qty != 0){
                                Double int_final_price = int_qty * int_price;
                                edt_total_amt.setText("" + int_final_price);
                            }else {
                                Snackbar.make(parent_view, "Quanty Can't be Zero", Snackbar.LENGTH_SHORT).show();
                            }

                        }else {
                            Snackbar.make(parent_view, "Your Stock Limit is " + int_stock + " only", Snackbar.LENGTH_SHORT).show();
                            edt_qty.setText("");
                        }

                    }else if (str_get_qty.equals("")){

                        edt_total_amt.setText("");
                    }else {
                        edt_total_amt.setText("");
                    }

                }catch (Exception e) {

                }
            }
        });

        edt_amt_received.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {

                String total_cost = edt_total_amt.getText().toString().trim();
                String received_amt = edt_amt_received.getText().toString().trim();

                try {

                    if (received_amt != null && !received_amt.isEmpty() && !received_amt.equals("null")) {

                        System.out.println("received_amt : " + received_amt);

                        int_received = Double.parseDouble(received_amt);

                        System.out.println("received_amt integer : " + received_amt);

                        if (total_cost != null && !total_cost.isEmpty() && !total_cost.equals("null")) {

                            System.out.println("total_cost : " + total_cost);

                            int_total = Double.parseDouble(total_cost);

                            System.out.println("total_cost integer: " + int_total);

                            System.out.println("received_amt integer : " + received_amt);

                            if (int_received > int_total) {
                                Snackbar.make(parent_view, "Received Amount is Greater Than Total Amount", Snackbar.LENGTH_SHORT).show();
                                edt_amt_received.setText("");
                            }else {

                            }

                        }else {
                            Snackbar.make(parent_view, "Total Cost is 0", Snackbar.LENGTH_SHORT).show();
                            edt_amt_received.setText("");
                        }
                    }else {

                    }

                }catch (Exception e){

                }

            }
        });

        edt_20l_can_received.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {

                String str_get_20l_can = edt_20l_can_received.getText().toString().trim();
                System.out.println("20 L : " + str_get_20l_can);

                try {
                    if (str_get_20l_can != null && !str_get_20l_can.isEmpty() && !str_get_20l_can.equals("null")) {
                        System.out.println("20 L TRUE : " + str_get_20l_can);

                        int int_20l_can = Integer.parseInt(str_get_20l_can);
                        int int_20l_stock = Integer.parseInt(str_20l_can);

                        if (int_20l_can <= int_20l_stock){

                        }else {
                            Snackbar.make(parent_view, "This Customer's 20 L can Limit is "+ int_20l_stock + " Only." , Snackbar.LENGTH_SHORT).show();
                            edt_20l_can_received.setText("0");
                        }

                    }else {

                    }

                }catch (Exception e) {

                }
            }
        });

        edt_10l_can_received.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {

                String str_get_10l_can = edt_10l_can_received.getText().toString().trim();

                try {
                    if (str_get_10l_can != null && !str_get_10l_can.isEmpty() && !str_get_10l_can.equals("null")) {
                        int int_10l_can = Integer.parseInt(str_get_10l_can);
                        int int_10l_stock = Integer.parseInt(str_10l_can);
                        if (int_10l_can <= int_10l_stock){

                        }else {
                            Snackbar.make(parent_view, "This Customer's 10 L can Limit is "+ int_10l_stock + " Only." , Snackbar.LENGTH_SHORT).show();
                            edt_10l_can_received.setText("0");
                        }

                    }else {

                    }

                }catch (Exception e) {

                }
            }
        });

        btn_save_invoice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                str_up_date = txt_date.getText().toString().trim();
                str_up_time = txt_time.getText().toString().trim();
                str_up_invoice_no = txt_invoice_no.getText().toString().trim();
                str_up_price = edt_total_amt.getText().toString().trim();
                str_up_qty = edt_qty.getText().toString().trim();
                str_up_received_amt = edt_amt_received.getText().toString().trim();
                str_up_20l_can_received = edt_20l_can_received.getText().toString().trim();
                str_up_10l_can_received = edt_10l_can_received.getText().toString().trim();
                str_up_bank_name = edt_bank_nam.getText().toString().trim();
                str_up_ref_no = edt_bank_ref_no.getText().toString().trim();

                if (str_customer_id == null){
                    Snackbar.make(parent_view, "Please Select Customer", Snackbar.LENGTH_SHORT).show();
                }else if (str_brand_id == null){
                    Snackbar.make(parent_view, "Please Select Brand", Snackbar.LENGTH_SHORT).show();
                }else if (str_product_id == null){
                    Snackbar.make(parent_view, "Please Select Product", Snackbar.LENGTH_SHORT).show();
                }else if (str_up_qty.equals("")){
                    Snackbar.make(parent_view, "Please Enter Quantity", Snackbar.LENGTH_SHORT).show();
                }else if (str_payment_type == null){
                    Snackbar.make(parent_view, "Please Select Payment Type", Snackbar.LENGTH_SHORT).show();
                }else {

                    if (str_payment_type.equals("2") || str_payment_type.equals("3")){
                        if (str_up_bank_name.equals("")){
                            Snackbar.make(parent_view, "Please Enter Bank Name", Snackbar.LENGTH_SHORT).show();
                        }else if (str_up_ref_no.equals("")){
                            Snackbar.make(parent_view, "Please Enter Payment Reference No.", Snackbar.LENGTH_SHORT).show();
                        }else {

                            try {
                                dialog = new SpotsDialog(Activity_Add_Invoice.this);
                                dialog.show();
                                queue = Volley.newRequestQueue(Activity_Add_Invoice.this);
                                Action_Add_Invoice();
                            }catch (Exception e) {

                            }
                        }
                    }else {

                        try {
                            dialog = new SpotsDialog(Activity_Add_Invoice.this);
                            dialog.show();
                            queue = Volley.newRequestQueue(Activity_Add_Invoice.this);
                            Action_Add_Invoice();
                        }catch (Exception e) {

                        }
                    }
                }
            }
        });

        try {
            dialog = new SpotsDialog(Activity_Add_Invoice.this);
            dialog.show();
            queue = Volley.newRequestQueue(Activity_Add_Invoice.this);
            Action_Get_InvoiceNo();
        }catch (Exception e) {

        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.main_home, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_home) {
            Intent i = new Intent(getApplicationContext(), MainActivity.class);
            startActivity(i);
            finish();

             return true;
        }

        return super.onOptionsItemSelected(item);
    }


    /***************************
     * GET Invoice Number
     ***************************/
    public void Action_Get_InvoiceNo() {

        String tag_json_obj = "json_obj_req";
        System.out.println("CAME 1");
        final StringRequest request = new StringRequest(Request.Method.POST,
                Appconfig.url_getinvoiceno, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.d(TAG, response.toString());
                try {
                    JSONObject obj = new JSONObject(response);
                    int success = obj.getInt("status");

                    if (success == 1) {

                        String str_invoice_no = obj.getString("invoice_no");

                        txt_invoice_no.setText(""+str_invoice_no);

                        dialog.dismiss();
                    } else if (success == 0) {

                        dialog.dismiss();

                        Snackbar.make(parent_view, "No Invoice Data Found", Snackbar.LENGTH_SHORT).show();

                    }

                    dialog.dismiss();

                    try {
                        dialog = new SpotsDialog(Activity_Add_Invoice.this);
                        dialog.show();
                        queue = Volley.newRequestQueue(Activity_Add_Invoice.this);
                        Action_Get_Customers();
                    }catch (Exception e) {

                    }

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

                System.out.println("USER : " + str_dealer_id);

                return params;
            }

        };

        // Adding request to request queue
        queue.add(request);
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
                        Arraylist_customer_gst.clear();

                        for (int i = 0; arr.length() > i; i++) {
                            JSONObject obj1 = arr.getJSONObject(i);

                            String customer_id = obj1.getString("customer_id");
                            String customer_name = obj1.getString("customer_name");
                            String customer_gst = obj1.getString("gst_no");

                            Arraylist_customer_id.add(customer_id);
                            Arraylist_customer_name.add(customer_name);
                            Arraylist_customer_gst.add(customer_gst);

                            try {
                                spn_customer
                                        .setAdapter(new ArrayAdapter<String>(Activity_Add_Invoice.this,
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
     * GET Brand
     ***************************/
    public void Action_Get_Brand() {

        String tag_json_obj = "json_obj_req";
        System.out.println("CAME 1");
        final StringRequest request = new StringRequest(Request.Method.POST,
                Appconfig.url_invoice_brandlist, new Response.Listener<String>() {

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

                            String customer_id = obj1.getString("brand_id");
                            String customer_name = obj1.getString("brand_name");

                            Arraylist_brand_id.add(customer_id);
                            Arraylist_brand_name.add(customer_name);

                            try {
                                spn_brand
                                        .setAdapter(new ArrayAdapter<String>(Activity_Add_Invoice.this,
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
                Appconfig.url_invoice_productlist, new Response.Listener<String>() {

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
                        Arraylist_product_stock.clear();
                        Arraylist_product_price.clear();
                        Arraylist_product_gst.clear();
                        Arraylist_product_tax_price.clear();

                        for (int i = 0; arr.length() > i; i++) {
                            JSONObject obj1 = arr.getJSONObject(i);

                            String prod_id = obj1.getString("product_id");
                            String prod_name = obj1.getString("product_name");
                            String stock = obj1.getString("stock");
                            String prod_price = obj1.getString("price");
                            String gst_percent = obj1.getString("gst_percent");
                            String tax_price = obj1.getString("tax_price");

                            Arraylist_product_id.add(prod_id);
                            Arraylist_product_name.add(prod_name);
                            Arraylist_product_stock.add(stock);
                            Arraylist_product_price.add(prod_price);
                            Arraylist_product_gst.add(gst_percent);
                            Arraylist_product_tax_price.add(tax_price);

                            try {
                                spn_product
                                        .setAdapter(new ArrayAdapter<String>(Activity_Add_Invoice.this,
                                                android.R.layout.simple_spinner_dropdown_item,
                                                Arraylist_product_name));

                            } catch (Exception e) {

                            }

                        }

                        JSONObject obj_can;

                        obj_can = obj.getJSONObject("cans");

                        str_20l_can = obj_can.getString("empty20");
                        str_10l_can = obj_can.getString("empty10");


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

                params.put("user_id", str_dealer_id);
                params.put("brand_id", str_brand_id);
                params.put("customer_id", str_customer_id);

                System.out.println("ID : " + str_dealer_id);
                System.out.println("BRAND ID : "+ str_brand_id);
                System.out.println("CUST ID : "+ str_customer_id);
                return params;
            }

        };

        // Adding request to request queue
        queue.add(request);
    }

    /********************************
     *    Action_ADD_Invoice
     *********************************/
    private void Action_Add_Invoice() {

        StringRequest request = new StringRequest(Request.Method.POST,
                Appconfig.url_add_invoice, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.d(TAG, response.toString());
                try {
                    JSONObject obj = new JSONObject(response);
                    int success = obj.getInt("status");
                    String message = obj.getString("message");

                    if (success == 1) {

                        dialog.dismiss();

                        new AlertDialog.Builder(Activity_Add_Invoice.this)
                                .setTitle("Aqua Bill")
                                .setMessage("Invoice Added Successfully !")
                                .setIcon(R.mipmap.ic_launcher)
                                .setPositiveButton("Ok",
                                        new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface alert_dialog,

                                                                int which) {
                                                // TODO Auto-generated method stub
                                                alert_dialog.dismiss();

                                                Intent i = new Intent(Activity_Add_Invoice.this, Activity_Manage_Invoice.class);
                                                startActivity(i);
                                                finish();

                                            }

                                        }).show();

                    }  else {

                        Snackbar.make(parent_view, "No Response From Server", Snackbar.LENGTH_SHORT).show();
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

                Snackbar.make(parent_view, "Error : " + error, Snackbar.LENGTH_SHORT).show();
                dialog.dismiss();
            }
        }) {

            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();

                params.put("user_id", str_dealer_id);
                params.put("invoice_no", str_up_invoice_no);
                params.put("date", str_up_date);
                params.put("time", str_up_time);
                params.put("customer_id", str_customer_id);
                params.put("brand_id", str_brand_id);
                params.put("product_id", str_product_id);
                params.put("price", str_up_price);
                params.put("quantity", str_up_qty);
                params.put("received_amount", str_up_received_amt);
                params.put("empty20", str_up_20l_can_received);
                params.put("empty10", str_up_10l_can_received);
                params.put("payment_type", str_payment_type);
                params.put("bank_name", str_up_bank_name);
                params.put("referenceno", str_up_ref_no);

                System.out.println("user_id" + str_dealer_id);
                System.out.println("invoice_no" + str_up_invoice_no);
                System.out.println("date" + str_up_date);
                System.out.println("time" + str_up_time);
                System.out.println("customer_id" + str_customer_id);
                System.out.println("brand_id" + str_brand_id);
                System.out.println("product_id" + str_product_id);
                System.out.println("price" + str_up_price);
                System.out.println("quantity" + str_up_qty);
                System.out.println("received_amount" + str_up_received_amt);
                System.out.println("empty20" + str_up_20l_can_received);
                System.out.println("empty10" + str_up_10l_can_received);
                System.out.println("payment_type" + str_payment_type);
                System.out.println("bank_name" + str_up_bank_name);
                System.out.println("referenceno" + str_up_ref_no);

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

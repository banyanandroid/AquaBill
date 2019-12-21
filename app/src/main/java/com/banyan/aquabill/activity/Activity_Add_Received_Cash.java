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

public class Activity_Add_Received_Cash extends AppCompatActivity {

    String TAG = "Add Received Cash";
    private View parent_view;

    // Session Manager Class
    SessionManager session;
    private static long back_pressed;

    SpotsDialog dialog;
    public static RequestQueue queue;

    private Toolbar mToolbar;

    String str_dealer_id;

    private LinearLayout linear_payment;

    private EditText edt_total_amt, edt_bank_nam, edt_bank_ref_no;

    private TextView txt_date, txt_time, txt_invoice_no;

    private SearchableSpinner spn_customer, spn_payment_type;

    private Button btn_save_invoice;

    private TextView t1;

    private String str_date, str_time, str_invoice_no, str_rate, str_qty, str_amt, str_can_received, str_customer_id, str_customer,
            str_customer_gst, str_customer_limit, str_brand_id, str_brand, str_product_id, str_product, str_product_stock, str_product_price,
            str_product_gst, str_product_act_price, str_product_tax_price, str_20l_can, str_10l_can;

    ArrayList<String> Arraylist_customer_id = null;
    ArrayList<String> Arraylist_customer_name = null;
    ArrayList<String> Arraylist_customer_gst = null;
    ArrayList<String> Arraylist_customer_credit_limit = null;

    int int_credit_limit;

    private String str_up_date, str_up_time, str_up_invoice_no, str_up_received_amt, str_payment_type, str_up_bank_name, str_up_ref_no;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_receive_cash);

        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        parent_view = findViewById(android.R.id.content);

        setSupportActionBar(mToolbar);
        getSupportActionBar().setTitle("Receive Amount");
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        mToolbar.setNavigationIcon(getResources().getDrawable(R.drawable.ic_action_back));
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent i = new Intent(getApplicationContext(), Activity_Manage_Received_Cash.class);
                i.putExtra("manage_class","invoice");
                startActivity(i);
                finish();
            }
        });

        session = new SessionManager(getApplicationContext());
        HashMap<String, String> user = session.getUserDetails();
        str_dealer_id = user.get(SessionManager.KEY_DEALER_ID);

        txt_date = (TextView) findViewById(R.id.add_receive_cash_txt_date);
        txt_time = (TextView) findViewById(R.id.add_receive_cash_txt_time);
        txt_invoice_no = (TextView) findViewById(R.id.add_receive_cash_txt_invoie_no);

        edt_total_amt = (EditText) findViewById(R.id.add_receive_cash_edt_amt);
        edt_bank_nam = (EditText) findViewById(R.id.add_receive_cash_bank_name);
        edt_bank_ref_no = (EditText) findViewById(R.id.add_receive_cash_ref_no);

        spn_customer = (SearchableSpinner)  findViewById(R.id.add_receive_cash_spn_customer);
        spn_payment_type = (SearchableSpinner)  findViewById(R.id.add_receive_cash_spn_payment);

        linear_payment = (LinearLayout) findViewById(R.id.add_receive_cash_linear);

        btn_save_invoice = (Button) findViewById(R.id.add_receive_cash_btn_save);

        Arraylist_customer_id = new ArrayList<String>();
        Arraylist_customer_name = new ArrayList<String>();
        Arraylist_customer_gst = new ArrayList<String>();
        Arraylist_customer_credit_limit = new ArrayList<String>();

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
                str_customer_limit = Arraylist_customer_credit_limit.get(position);

                System.out.println("CUST ID : " + str_customer_id);
                System.out.println("CUST NAME : " + str_customer);
                System.out.println("CUST LIMIT : " + str_customer_limit);

                if (str_customer_limit != null && !str_customer_limit.isEmpty() && !str_customer_limit.equals("null")){

                    int_credit_limit = Integer.parseInt(str_customer_limit);
                }else {
                    int_credit_limit = 0;
                }


            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        edt_total_amt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {

                String str_get_amt = edt_total_amt.getText().toString().trim();

                try {

                    if (str_get_amt != null && !str_get_amt.isEmpty() && !str_get_amt.equals("null")) {
                        int int_amt = Integer.parseInt(str_get_amt);

                        if (int_amt <= int_credit_limit){

                        }else {
                            Snackbar.make(parent_view, "This Customer's Credit Limit is "+ int_credit_limit + " Only." , Snackbar.LENGTH_SHORT).show();
                            edt_total_amt.setText("0");
                        }

                    }else {

                    }

                }catch (Exception e) {

                }
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


        btn_save_invoice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                str_up_date = txt_date.getText().toString().trim();
                str_up_time = txt_time.getText().toString().trim();
                str_up_invoice_no = txt_invoice_no.getText().toString().trim();
                str_up_received_amt = edt_total_amt.getText().toString().trim();
                str_up_bank_name = edt_bank_nam.getText().toString().trim();
                str_up_ref_no = edt_bank_ref_no.getText().toString().trim();

                if (str_customer_id == null){
                    Snackbar.make(parent_view, "Please Select Customer", Snackbar.LENGTH_SHORT).show();
                }else if (str_up_received_amt.equals("")){
                    Snackbar.make(parent_view, "Please Enter Payment", Snackbar.LENGTH_SHORT).show();
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
                                dialog = new SpotsDialog(Activity_Add_Received_Cash.this);
                                dialog.show();
                                queue = Volley.newRequestQueue(Activity_Add_Received_Cash.this);
                                Action_Add_Invoice();
                            }catch (Exception e) {

                            }
                        }
                    }else {

                        try {
                            dialog = new SpotsDialog(Activity_Add_Received_Cash.this);
                            dialog.show();
                            queue = Volley.newRequestQueue(Activity_Add_Received_Cash.this);
                            Action_Add_Invoice();
                        }catch (Exception e) {

                        }
                    }
                }
            }
        });

        try {
            dialog = new SpotsDialog(Activity_Add_Received_Cash.this);
            dialog.show();
            queue = Volley.newRequestQueue(Activity_Add_Received_Cash.this);
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
                Appconfig.url_getreceiptno, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.d(TAG, response.toString());
                try {
                    JSONObject obj = new JSONObject(response);
                    int success = obj.getInt("status");

                    if (success == 1) {

                        String str_invoice_no = obj.getString("receipt_no");

                        txt_invoice_no.setText(""+str_invoice_no);

                        dialog.dismiss();
                    } else if (success == 0) {

                        dialog.dismiss();

                        Snackbar.make(parent_view, "No Invoice Data Found", Snackbar.LENGTH_SHORT).show();

                    }

                    dialog.dismiss();

                    try {
                        dialog = new SpotsDialog(Activity_Add_Received_Cash.this);
                        dialog.show();
                        queue = Volley.newRequestQueue(Activity_Add_Received_Cash.this);
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
                            String credit_amount = obj1.getString("credit_amount");

                            Arraylist_customer_id.add(customer_id);
                            Arraylist_customer_name.add(customer_name);
                            Arraylist_customer_gst.add(customer_gst);
                            Arraylist_customer_credit_limit.add(credit_amount);

                            try {
                                spn_customer
                                        .setAdapter(new ArrayAdapter<String>(Activity_Add_Received_Cash.this,
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

    /********************************
     *    Action_ADD_Invoice
     *********************************/
    private void Action_Add_Invoice() {

        StringRequest request = new StringRequest(Request.Method.POST,
                Appconfig.url_add_received_cash, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.d(TAG, response.toString());
                try {
                    JSONObject obj = new JSONObject(response);
                    int success = obj.getInt("status");
                    String message = obj.getString("message");

                    if (success == 1) {

                        dialog.dismiss();

                        new AlertDialog.Builder(Activity_Add_Received_Cash.this)
                                .setTitle("Aqua Bill")
                                .setMessage("Cash Received Successfully !")
                                .setIcon(R.mipmap.ic_launcher)
                                .setPositiveButton("Ok",
                                        new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface alert_dialog,

                                                                int which) {
                                                // TODO Auto-generated method stub
                                                alert_dialog.dismiss();

                                                Intent i = new Intent(Activity_Add_Received_Cash.this, Activity_Manage_Received_Cash.class);
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
                params.put("date", str_up_date);
                params.put("time", str_up_time);
                params.put("customer_id", str_customer_id);
                params.put("receipt_no", str_up_invoice_no);
                params.put("received_amount", str_up_received_amt);
                params.put("payment_type", str_payment_type);
                params.put("bank_name", str_up_bank_name);
                params.put("referenceno", str_up_ref_no);

                System.out.println("user_id" + str_dealer_id);
                System.out.println("invoice_no" + str_up_invoice_no);
                System.out.println("date" + str_up_date);
                System.out.println("time" + str_up_time);
                System.out.println("customer_id" + str_customer_id);
                System.out.println("received_amount" + str_up_received_amt);
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

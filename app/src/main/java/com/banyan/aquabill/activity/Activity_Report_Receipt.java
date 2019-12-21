package com.banyan.aquabill.activity;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
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
import com.banyan.aquabill.adapter.Adapter_Report_Receipt_Report;
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

public class Activity_Report_Receipt extends AppCompatActivity {

    String TAG = "Products and Rates";
    private View parent_view;

    // Session Manager Class
    SessionManager session;
    private static long back_pressed;

    SpotsDialog dialog;
    public static RequestQueue queue;

    private Toolbar mToolbar;

    String str_dealer_id;

    private EditText edt_from_date, edt_to_date;

    private SearchableSpinner spn_customer, spn_payment_type;

    private Button btn_get_data;

    TextView t1;

    private ListView list_report;

    ArrayList<String> Arraylist_customer_id = null;
    ArrayList<String> Arraylist_customer_name = null;

    static ArrayList<HashMap<String, String>> report_receipt_list;
    HashMap<String, String> params = new HashMap<String, String>();

    String  str_customer_id, str_customer, str_payment_type;

    public static final String TAG_INVOICE_NO = "invoice_no";
    public static final String TAG_DEALER_NAME = "delaer_name";
    public static final String TAG_CUSTOMER_NAME = "customer_name";
    public static final String TAG_RECEIPT_NO = "receipt_no";
    public static final String TAG_DATE = "date";
    public static final String TAG_TIME = "time";
    public static final String TAG_RECEIVED_AMT = "receipt_amount";
    public static final String TAG_PAYMENT_TYPE = "payment_mode";
    public static final String TAG_REF_NO = "reference_no";
    public static final String TAG_BANK_NAME = "bank_name";

    private Adapter_Report_Receipt_Report adapter;

    String str_from_date, str_to_date;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report_receipt_report);

        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        parent_view = findViewById(android.R.id.content);

        setSupportActionBar(mToolbar);
        getSupportActionBar().setTitle("Receipt Report");
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

        edt_from_date = (EditText) findViewById(R.id.report_receipt_edt_from_Date);
        edt_to_date = (EditText) findViewById(R.id.report_receipt_edt_to_Date);

        spn_customer = (SearchableSpinner) findViewById(R.id.report_receipt_spn_customer);
        spn_payment_type = (SearchableSpinner) findViewById(R.id.report_receipt_spn_mode);

        btn_get_data = (Button) findViewById(R.id.report_receipt_btn_get_Data);

        list_report = (ListView)  findViewById(R.id.report_receipt_listview);

        Arraylist_customer_id = new ArrayList<String>();
        Arraylist_customer_name = new ArrayList<String>();

        report_receipt_list = new ArrayList<HashMap<String, String>>();

        edt_from_date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Function_Select_From_Date();
            }
        });

        edt_to_date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Function_Select_To_Date();
            }
        });

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
                }else if (str_payment.equals("Cheque")){
                    str_payment_type = "2";
                }else if (str_payment.equals("Bank Transfer")){
                    str_payment_type = "3";
                }else {
                    str_payment_type = "0";
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        btn_get_data.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                str_from_date = edt_from_date.getText().toString().trim();
                str_to_date = edt_to_date.getText().toString().trim();

                if (str_from_date.equals("")) {
                    Snackbar.make(parent_view, "Please Select From Date", Snackbar.LENGTH_SHORT).show();
                    edt_from_date.setError("Please Select From Date");
                }else if (str_to_date.equals("")){
                    Snackbar.make(parent_view, "Please Select To Date", Snackbar.LENGTH_SHORT).show();
                    edt_from_date.setError("Please Select To Date");
                }/*else if (str_customer_id == null || str_customer_id.equals("")){
                    Snackbar.make(parent_view, "Please Select Customer", Snackbar.LENGTH_SHORT).show();
                }else if (str_payment_type == null || str_payment_type.equals("")){
                    Snackbar.make(parent_view, "Please Select Payment Type", Snackbar.LENGTH_SHORT).show();
                }*/else {
                    try {
                        dialog = new SpotsDialog(Activity_Report_Receipt.this);
                        dialog.show();
                        queue = Volley.newRequestQueue(Activity_Report_Receipt.this);
                        Action_Get_Record();
                    }catch (Exception e) {

                    }
                }
            }
        });

        try {
            dialog = new SpotsDialog(Activity_Report_Receipt.this);
            dialog.show();
            queue = Volley.newRequestQueue(Activity_Report_Receipt.this);
            Action_Get_Record();
            Action_Get_Customers();
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
                                        .setAdapter(new ArrayAdapter<String>(Activity_Report_Receipt.this,
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
     * From Date
     ***************************/

    private void Function_Select_From_Date() {

        final Calendar c = Calendar.getInstance();
        int int_year = c.get(Calendar.YEAR);
        int int_month = c.get(Calendar.MONTH);
        int int_date = c.get(Calendar.DAY_OF_MONTH);


// Launch Date Picker Dialog
        DatePickerDialog dpd = new DatePickerDialog(
                Activity_Report_Receipt.this,
                new DatePickerDialog.OnDateSetListener() {

                    @Override
                    public void onDateSet(DatePicker view, int year,
                                          int monthOfYear, int dayOfMonth) {

                        int month = monthOfYear + 1;
                        String formattedMonth = "" + month;
                        String formattedDayOfMonth = "" + dayOfMonth;

                        if (month < 10) {

                            formattedMonth = "0" + month;
                        }
                        if (dayOfMonth < 10) {

                            formattedDayOfMonth = "0" + dayOfMonth;
                        }

                        String str_date = year + "-" + formattedMonth + "-" + formattedDayOfMonth;
                        edt_from_date.setText(str_date);

                    }
                }, int_year, int_month, int_date);
        dpd.setTitle("Select From Date");
        dpd.show();

    }

    /***************************
     * To Date
     ***************************/

    private void Function_Select_To_Date() {

        final Calendar c = Calendar.getInstance();
        int int_year = c.get(Calendar.YEAR);
        int int_month = c.get(Calendar.MONTH);
        int int_date = c.get(Calendar.DAY_OF_MONTH);


// Launch Date Picker Dialog
        DatePickerDialog dpd = new DatePickerDialog(
                Activity_Report_Receipt.this,
                new DatePickerDialog.OnDateSetListener() {

                    @Override
                    public void onDateSet(DatePicker view, int year,
                                          int monthOfYear, int dayOfMonth) {

                        int month = monthOfYear + 1;
                        String formattedMonth = "" + month;
                        String formattedDayOfMonth = "" + dayOfMonth;

                        if (month < 10) {

                            formattedMonth = "0" + month;
                        }
                        if (dayOfMonth < 10) {

                            formattedDayOfMonth = "0" + dayOfMonth;
                        }

                        String str_date = year + "-" + formattedMonth + "-" + formattedDayOfMonth;
                        edt_to_date.setText(str_date);

                    }
                }, int_year, int_month, int_date);
        dpd.setTitle("Select To Date");
        dpd.show();

    }

    /***************************
     * GET Record
     ***************************/

    public void Action_Get_Record() {

        String tag_json_obj = "json_obj_req";

        StringRequest request = new StringRequest(Request.Method.POST,
                Appconfig.url_receipt_report, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.d(TAG, response.toString());
                try {
                    JSONObject obj = new JSONObject(response);
                    int success = obj.getInt("status");

                    if (success == 1) {

                        JSONArray arr;

                        arr = obj.getJSONArray("data");

                        report_receipt_list.clear();

                        for (int i = 0; arr.length() > i; i++) {
                            JSONObject obj1 = arr.getJSONObject(i);

                            String invoice_no = obj1.getString(TAG_INVOICE_NO);
                            String dealer_name = obj1.getString(TAG_DEALER_NAME);
                            String customer_name = obj1.getString(TAG_CUSTOMER_NAME);
                            String date = obj1.getString(TAG_DATE);
                            String time = obj1.getString(TAG_TIME);
                            String receipt_no = obj1.getString(TAG_RECEIPT_NO);
                            String received_amount = obj1.getString(TAG_RECEIVED_AMT);
                            String payment_type = obj1.getString(TAG_PAYMENT_TYPE);
                            String chequeno = obj1.getString(TAG_REF_NO);
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
                            map.put(TAG_REF_NO, chequeno);
                            map.put(TAG_BANK_NAME, bankname);

                            report_receipt_list.add(map);

                            adapter = new Adapter_Report_Receipt_Report(Activity_Report_Receipt.this,
                                    report_receipt_list);
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
                params.put("from_date", str_from_date);
                params.put("to_date", str_to_date);
                params.put("cus_id", str_customer_id);
                params.put("mode", str_payment_type);

                System.out.println("USER ID : " + str_dealer_id);
                System.out.println("str_from_date : " + str_from_date);
                System.out.println("str_to_date : " + str_to_date);
                System.out.println("str_customer_id : " + str_customer_id);
                System.out.println("mode : " + str_payment_type);

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

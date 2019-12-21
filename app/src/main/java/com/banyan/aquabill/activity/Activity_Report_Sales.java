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
import com.banyan.aquabill.adapter.Adapter_Manage_Invoice;
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

public class Activity_Report_Sales extends AppCompatActivity {

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

    private SearchableSpinner spn_customer, spn_brand, spn_product;

    private Button btn_get_data;

    TextView t1;

    private ListView list_report;

    ArrayList<String> Arraylist_customer_id = null;
    ArrayList<String> Arraylist_customer_name = null;

    ArrayList<String> Arraylist_brand_id = null;
    ArrayList<String> Arraylist_brand_name = null;

    ArrayList<String> Arraylist_product_id = null;
    ArrayList<String> Arraylist_product_name = null;

    static ArrayList<HashMap<String, String>> report_sales_list;
    HashMap<String, String> params = new HashMap<String, String>();

    String  str_customer_id, str_customer, str_brand_id, str_brand, str_product_id, str_product;

    public static final String TAG_INVOICE_NO = "invoice_no";
    public static final String TAG_INVOICE_TYPE = "invoice_type";
    public static final String TAG_DEALER_NAME = "delaer_name";
    public static final String TAG_CUSTOMER_NAME = "customer_name";
    public static final String TAG_DATE = "date";
    public static final String TAG_TIME = "time";
    public static final String TAG_BRAND_NAME = "brand_name";
    public static final String TAG_PRODUCT_NAME = "product_name";
    public static final String TAG_QTY = "quantity";
    public static final String TAG_TAX = "tax";
    public static final String TAG_TAXABLE_VALUE = "taxable_value";
    public static final String TAG_INVOICE_AMT = "invoice_amount";
    public static final String TAG_RECEIVED_AMT = "received_amount";
    public static final String TAG_BALANCE_AMT = "balance_amount";

    private Adapter_Report_Sales adapter;

    String str_from_date, str_to_date;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report_sales);

        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        parent_view = findViewById(android.R.id.content);

        setSupportActionBar(mToolbar);
        getSupportActionBar().setTitle("Sales Report");
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

        edt_from_date = (EditText) findViewById(R.id.report_sales_edt_from_Date);
        edt_to_date = (EditText) findViewById(R.id.report_sales_edt_to_Date);

        spn_customer = (SearchableSpinner) findViewById(R.id.report_sales_spn_customer);
        spn_brand = (SearchableSpinner) findViewById(R.id.report_sales_spn_brand);
        spn_product = (SearchableSpinner) findViewById(R.id.report_sales_spn_product);

        btn_get_data = (Button) findViewById(R.id.report_sales_btn_get_Data);

        list_report = (ListView)  findViewById(R.id.report_sales_listview);

        Arraylist_customer_id = new ArrayList<String>();
        Arraylist_customer_name = new ArrayList<String>();

        Arraylist_brand_id = new ArrayList<String>();
        Arraylist_brand_name = new ArrayList<String>();

        Arraylist_product_id = new ArrayList<String>();
        Arraylist_product_name = new ArrayList<String>();

        report_sales_list = new ArrayList<HashMap<String, String>>();

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

                try {
                    dialog = new SpotsDialog(Activity_Report_Sales.this);
                    dialog.show();
                    queue = Volley.newRequestQueue(Activity_Report_Sales.this);
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
                    dialog = new SpotsDialog(Activity_Report_Sales.this);
                    dialog.show();
                    queue = Volley.newRequestQueue(Activity_Report_Sales.this);
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
                }else if (str_brand_id == null || str_brand_id.equals("")){
                    Snackbar.make(parent_view, "Please Select Brand", Snackbar.LENGTH_SHORT).show();
                }else if (str_product_id == null || str_product_id.equals("")){
                    Snackbar.make(parent_view, "Please Select Product", Snackbar.LENGTH_SHORT).show();
                }*/else {
                    try {
                        dialog = new SpotsDialog(Activity_Report_Sales.this);
                        dialog.show();
                        queue = Volley.newRequestQueue(Activity_Report_Sales.this);
                        Action_Get_Record();
                    }catch (Exception e) {

                    }
                }
            }
        });


        try {
            dialog = new SpotsDialog(Activity_Report_Sales.this);
            dialog.show();
            queue = Volley.newRequestQueue(Activity_Report_Sales.this);
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
                                        .setAdapter(new ArrayAdapter<String>(Activity_Report_Sales.this,
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
                                        .setAdapter(new ArrayAdapter<String>(Activity_Report_Sales.this,
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

                        for (int i = 0; arr.length() > i; i++) {
                            JSONObject obj1 = arr.getJSONObject(i);

                            String prod_id = obj1.getString("product_id");
                            String prod_name = obj1.getString("product_name");

                            Arraylist_product_id.add(prod_id);
                            Arraylist_product_name.add(prod_name);

                            try {
                                spn_product
                                        .setAdapter(new ArrayAdapter<String>(Activity_Report_Sales.this,
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
                Activity_Report_Sales.this,
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
                Activity_Report_Sales.this,
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
                Appconfig.url_sales_report, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.d(TAG, response.toString());
                try {
                    JSONObject obj = new JSONObject(response);
                    int success = obj.getInt("status");

                    if (success == 1) {

                        JSONArray arr;

                        arr = obj.getJSONArray("data");

                        report_sales_list.clear();

                        for (int i = 0; arr.length() > i; i++) {
                            JSONObject obj1 = arr.getJSONObject(i);

                            String invoice_no = obj1.getString(TAG_INVOICE_NO);
                            String invoice_type = obj1.getString(TAG_INVOICE_TYPE);
                            String dealer_name = obj1.getString(TAG_DEALER_NAME);
                            String customer_name = obj1.getString(TAG_CUSTOMER_NAME);
                            String date = obj1.getString(TAG_DATE);
                            String time = obj1.getString(TAG_TIME);
                            String brand_name = obj1.getString(TAG_BRAND_NAME);
                            String product_name = obj1.getString(TAG_PRODUCT_NAME);
                            String quantity = obj1.getString(TAG_QTY);
                            String tax = obj1.getString(TAG_TAX);
                            String taxable_value = obj1.getString(TAG_TAXABLE_VALUE);
                            String invoice_amount = obj1.getString(TAG_INVOICE_AMT);
                            String received_amount = obj1.getString(TAG_RECEIVED_AMT);
                            String balance_amount = obj1.getString(TAG_BALANCE_AMT);

                            // creating new HashMap
                            HashMap<String, String> map = new HashMap<String, String>();

                            // adding each child node to HashMap key => value
                            map.put(TAG_INVOICE_NO, invoice_no);
                            map.put(TAG_INVOICE_TYPE, invoice_type);
                            map.put(TAG_DEALER_NAME, dealer_name);
                            map.put(TAG_CUSTOMER_NAME, customer_name);
                            map.put(TAG_DATE, date);
                            map.put(TAG_TIME, time);
                            map.put(TAG_BRAND_NAME, brand_name);
                            map.put(TAG_PRODUCT_NAME, product_name);
                            map.put(TAG_QTY, quantity);
                            map.put(TAG_TAX, tax);
                            map.put(TAG_TAXABLE_VALUE, taxable_value);
                            map.put(TAG_INVOICE_AMT, invoice_amount);
                            map.put(TAG_RECEIVED_AMT, received_amount);
                            map.put(TAG_BALANCE_AMT, balance_amount);

                            report_sales_list.add(map);

                            adapter = new Adapter_Report_Sales(Activity_Report_Sales.this,
                                    report_sales_list);
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
                params.put("brand_id", str_brand_id);
                params.put("product_id", str_product_id);

                System.out.println("USER ID : " + str_dealer_id);
                System.out.println("str_from_date : " + str_from_date);
                System.out.println("str_to_date : " + str_to_date);
                System.out.println("str_customer_id : " + str_customer_id);
                System.out.println("str_brand_id : " + str_brand_id);
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

package com.banyan.aquabill.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import dmax.dialog.SpotsDialog;

public class Activity_Add_Customer extends AppCompatActivity {

    String TAG = "Add Customer";
    private View parent_view;

    // Session Manager Class
    SessionManager session;
    private String str_dealer_id;
    private static long back_pressed;

    SpotsDialog dialog;
    public static RequestQueue queue;

    private Toolbar mToolbar;

    private EditText edt_mobile_no, edt_otp, edt_name, edt_address, edt_state, edt_city, edt_postal,edt_contact_person,edt_email,edt_gst,
            edt_opening_bal,edt_20l_stock,edt_10l_stock;

    private SearchableSpinner spn_state, spn_city;

    private ImageView img_check;

    private LinearLayout linear_customer_info, linear_customer_otp;

    private Button btn_add_customer, btn_verify_otp;

    private String str_mobile, str_otp, str_otp_entered;

    ArrayList<String> Arraylist_state_id = null;
    ArrayList<String> Arraylist_state_name = null;

    ArrayList<String> Arraylist_city_id = null;
    ArrayList<String> Arraylist_city_name = null;

    TextView t1;

    private String str_mob, str_name, str_address, str_state, str_state_id, str_city, str_city_id,str_postal,str_contact_erson,
            str_email, str_gst, str_opening_balance, str_20l_stock, str_10l_stock;

    private int flag = 0;

    private String str_json_city, str_json_state;

    private String str_final_city, str_final_state;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_customer);

        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        parent_view = findViewById(android.R.id.content);

        setSupportActionBar(mToolbar);
        getSupportActionBar().setTitle("Add Customer");
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


        // Session Manager
        session = new SessionManager(getApplicationContext());
        HashMap<String, String> user = session.getUserDetails();
        str_dealer_id = user.get(SessionManager.KEY_DEALER_ID);

        edt_mobile_no = (EditText) findViewById(R.id.add_customer_edt_mob);
        img_check = (ImageView) findViewById(R.id.add_customer_img_check);
        linear_customer_otp = (LinearLayout) findViewById(R.id.add_customer_linear_otp);
        linear_customer_info = (LinearLayout) findViewById(R.id.add_customer_linear);

        edt_otp = (EditText) findViewById(R.id.add_customer_edt_otp);
        edt_name = (EditText) findViewById(R.id.add_customer_edt_name);
        edt_address = (EditText) findViewById(R.id.add_customer_edt_address);
        edt_city = (EditText) findViewById(R.id.add_customer_edt_city);
        edt_state = (EditText) findViewById(R.id.add_customer_edt_state);
        edt_postal = (EditText) findViewById(R.id.add_customer_edt_postalcode);
        edt_contact_person = (EditText) findViewById(R.id.add_customer_edt_contact_person);
        edt_email = (EditText) findViewById(R.id.add_customer_edt_email);
        edt_gst = (EditText) findViewById(R.id.add_customer_edt_gst);
        edt_opening_bal = (EditText) findViewById(R.id.add_customer_edt_opening_balance);
        edt_20l_stock = (EditText) findViewById(R.id.add_customer_edt_20l_stock);
        edt_10l_stock = (EditText) findViewById(R.id.add_customer_edt_10l_stock);

        spn_city = (SearchableSpinner) findViewById(R.id.add_customer_spn_city);
        spn_state = (SearchableSpinner) findViewById(R.id.add_customer_spn_state);

        btn_add_customer = (Button) findViewById(R.id.add_supplier_btn_add);
        btn_verify_otp = (Button) findViewById(R.id.add_customer_btn_verify_otp);

        Arraylist_state_id = new ArrayList<String>();
        Arraylist_state_name = new ArrayList<String>();

        Arraylist_city_id = new ArrayList<String>();
        Arraylist_city_name = new ArrayList<String>();

        spn_state.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                System.out.println("Shop");

                t1 = (TextView) view;
                String str_state = t1.getText().toString();
                str_state_id = Arraylist_state_id.get(position);
                str_state = Arraylist_state_name.get(position);

                System.out.println("STATE ID : " + str_state_id);
                System.out.println("STATE NAME : " + str_state);

                edt_state.setText("" + str_state);

                try {
                    dialog = new SpotsDialog(Activity_Add_Customer.this);
                    dialog.show();
                    queue = Volley.newRequestQueue(Activity_Add_Customer.this);
                    Action_Get_City();
                }catch (Exception e) {

                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        spn_city.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                System.out.println("Shop");

                t1 = (TextView) view;
                String str_state = t1.getText().toString();
                str_city_id = Arraylist_city_id.get(position);
                str_city = Arraylist_city_name.get(position);

                System.out.println("CITY ID : " + str_city_id);
                System.out.println("CITY NAME : " + str_city);

                edt_city.setText("" + str_city);

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        img_check.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                str_mobile = edt_mobile_no.getText().toString().trim();

                if (!str_mobile.equals("")){

                    try {
                        dialog = new SpotsDialog(Activity_Add_Customer.this);
                        dialog.show();
                        queue = Volley.newRequestQueue(Activity_Add_Customer.this);
                        Action_Check_Customer();
                    }catch (Exception e) {

                    }
                }else {

                    Snackbar.make(parent_view, "Please Enter a Mobile Number", Snackbar.LENGTH_SHORT).show();
                }
            }
        });

        btn_add_customer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                str_mob = edt_mobile_no.getText().toString().trim();
                str_name  = edt_name.getText().toString().trim();
                str_address = edt_address.getText().toString().trim();
                str_postal = edt_postal.getText().toString().trim();
                str_contact_erson = edt_contact_person.getText().toString().trim();
                str_email = edt_email.getText().toString().trim();
                str_gst = edt_gst.getText().toString().trim();
                str_opening_balance = edt_opening_bal.getText().toString().trim();
                str_20l_stock = edt_20l_stock.getText().toString().trim();
                str_10l_stock = edt_10l_stock.getText().toString().trim();

                if (flag == 1) { // For Old Customers
                    str_final_city = str_json_city;
                    str_final_state = str_json_state;
                }else if (flag == 2) { // For New Customers
                    str_final_city = str_city_id;
                    str_final_state = str_state_id;
                }

                System.out.println("STATE : " + str_final_state);
                System.out.println("CITY : " + str_final_city);

                if (str_name.equals("")){
                    Snackbar.make(parent_view, "Please Enter a Customer Name", Snackbar.LENGTH_SHORT).show();
                }else if (str_address.equals("")){
                    Snackbar.make(parent_view, "Please Enter a Address", Snackbar.LENGTH_SHORT).show();
                }else if (str_final_state.equals("")){
                    Snackbar.make(parent_view, "Please Select State", Snackbar.LENGTH_SHORT).show();
                }else if (str_final_city.equals("")){
                    Snackbar.make(parent_view, "Please Select City", Snackbar.LENGTH_SHORT).show();
                }else if (str_postal.equals("")){
                    Snackbar.make(parent_view, "Please Enter a Postal Code", Snackbar.LENGTH_SHORT).show();
                }else if (str_contact_erson.equals("")){
                    Snackbar.make(parent_view, "Please Enter Contact Person", Snackbar.LENGTH_SHORT).show();
                }else if (str_email.equals("")){
                    Snackbar.make(parent_view, "Please Enter a Email", Snackbar.LENGTH_SHORT).show();
                }else if (str_opening_balance.equals("")){
                    Snackbar.make(parent_view, "Please Enter Opening Balance", Snackbar.LENGTH_SHORT).show();
                }else if (str_20l_stock.equals("")){
                    Snackbar.make(parent_view, "Please Enter 20 L Stock", Snackbar.LENGTH_SHORT).show();
                }else if (str_10l_stock.equals("")){
                    Snackbar.make(parent_view, "Please Enter 10 L Stock", Snackbar.LENGTH_SHORT).show();
                }else {

                    try {
                        dialog = new SpotsDialog(Activity_Add_Customer.this);
                        dialog.show();
                        queue = Volley.newRequestQueue(Activity_Add_Customer.this);
                        Action_Add_Customer();
                    }catch (Exception e) {

                    }

                }

            }
        });

        btn_verify_otp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                str_otp_entered = edt_otp.getText().toString().trim();

                if (str_otp_entered.equals(str_otp)){

                    linear_customer_info.setVisibility(View.VISIBLE);
                    linear_customer_otp.setVisibility(View.GONE);
                    btn_add_customer.setVisibility(View.VISIBLE);

                    edt_opening_bal.setFocusableInTouchMode(true);
                    edt_opening_bal.setClickable(true);

                    edt_20l_stock.setFocusableInTouchMode(true);
                    edt_20l_stock.setClickable(true);

                    edt_10l_stock.setFocusableInTouchMode(true);
                    edt_10l_stock.setClickable(true);

                }else {

                    Snackbar.make(parent_view, "OTP Mismatched, Please Try Again!", Snackbar.LENGTH_SHORT).show();
                }
            }
        });

    }

    /********************************
     *    Action_Check_Customer
     *********************************/
    private void Action_Check_Customer() {

        StringRequest request = new StringRequest(Request.Method.POST,
                Appconfig.url_check_customer, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.d(TAG, response.toString());
                try {
                    JSONObject obj = new JSONObject(response);
                    int success = obj.getInt("status");
                    String message = obj.getString("message");

                    Snackbar.make(parent_view, message, Snackbar.LENGTH_SHORT).show();

                    if (success == 1) {

                        str_otp = obj.getString("opt");

                        JSONObject obj_data = obj.getJSONObject("data");

                        String customer_name = obj_data.getString("customer_name");
                        String address = obj_data.getString("address");
                        str_json_city = obj_data.getString("city");
                        String city_name  = obj_data.getString("city_name");
                        str_json_state  = obj_data.getString("state");
                        String state_name  = obj_data.getString("state_name");
                        String postal_code  = obj_data.getString("postal_code");
                        String contact_person  = obj_data.getString("contact_person");
                        String mobile_no  = obj_data.getString("mobile_no");
                        String email_id  = obj_data.getString("email_id");
                        String gst_no  = obj_data.getString("gst_no");
                        String opening_balance  = obj_data.getString("opening_balance");
                        String twentyl_stock  = obj_data.getString("twentyl_stock");
                        String tenl_stock  = obj_data.getString("tenl_stock");


                        edt_name.setText("" +customer_name);
                        edt_address.setText("" +address);
                        edt_city.setText("" +city_name + str_json_city);
                        edt_state.setText("" +state_name + str_json_state);
                        edt_postal.setText("" +postal_code);
                        edt_contact_person.setText("" +contact_person);
                        edt_mobile_no.setText("" +mobile_no);
                        edt_email.setText("" +email_id);
                        edt_gst.setText("" +gst_no);
                        edt_opening_bal.setText("" +opening_balance);
                        edt_20l_stock.setText("" +twentyl_stock);
                        edt_10l_stock.setText("" +tenl_stock);

                        OldUser();

                        dialog.dismiss();

                    } else if (success == 2) {

                        dialog.dismiss();

                        new_user();

                    } else if (success == 3) {

                        JSONObject obj_data = obj.getJSONObject("data");

                        String customer_name = obj_data.getString("customer_name");
                        String address = obj_data.getString("address");
                        String city = obj_data.getString("city");
                        String city_name  = obj_data.getString("city_name");
                        String state  = obj_data.getString("state");
                        String state_name  = obj_data.getString("state_name");
                        String postal_code  = obj_data.getString("postal_code");
                        String contact_person  = obj_data.getString("contact_person");
                        String mobile_no  = obj_data.getString("mobile_no");
                        String email_id  = obj_data.getString("email_id");
                        String gst_no  = obj_data.getString("gst_no");
                        String opening_balance  = obj_data.getString("opening_balance");
                        String twentyl_stock  = obj_data.getString("twentyl_stock");
                        String tenl_stock  = obj_data.getString("tenl_stock");

                        edt_name.setText("" +customer_name);
                        edt_address.setText("" +address);
                        edt_city.setText("" +city_name);
                        edt_state.setText("" +state_name);
                        edt_postal.setText("" +postal_code);
                        edt_contact_person.setText("" +contact_person);
                        edt_mobile_no.setText("" +mobile_no);
                        edt_email.setText("" +email_id);
                        edt_gst.setText("" +gst_no);
                        edt_opening_bal.setText("" +opening_balance);
                        edt_20l_stock.setText("" +twentyl_stock);
                        edt_10l_stock.setText("" +tenl_stock);

                        AlreadyExistingUser();

                        dialog.dismiss();

                    } else {

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
                params.put("mobile_no", str_mobile);

                System.out.println("user_id" + str_dealer_id);
                System.out.println("mobile_no" + str_mobile);

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

    /********************************
     *    User Function
     *********************************/
    private void OldUser(){

        flag = 1;

        linear_customer_otp.setVisibility(View.VISIBLE);

    }

    private void new_user() {

        flag = 2;

        try {
            dialog = new SpotsDialog(Activity_Add_Customer.this);
            dialog.show();
            queue = Volley.newRequestQueue(Activity_Add_Customer.this);
            Action_Get_State();
        }catch (Exception e) {

        }

        linear_customer_info.setVisibility(View.VISIBLE);

        spn_state.setVisibility(View.VISIBLE);
        spn_city.setVisibility(View.VISIBLE);

        btn_add_customer.setVisibility(View.VISIBLE);

        edt_name.setFocusableInTouchMode(true);
        edt_name.setClickable(true);

        edt_address.setFocusableInTouchMode(true);
        edt_address.setClickable(true);

        edt_postal.setFocusableInTouchMode(true);
        edt_postal.setClickable(true);

        edt_contact_person.setFocusableInTouchMode(true);
        edt_contact_person.setClickable(true);

        edt_email.setFocusableInTouchMode(true);
        edt_email.setClickable(true);

        edt_gst.setFocusableInTouchMode(true);
        edt_gst.setClickable(true);

        edt_opening_bal.setFocusableInTouchMode(true);
        edt_opening_bal.setClickable(true);

        edt_20l_stock.setFocusableInTouchMode(true);
        edt_20l_stock.setClickable(true);

        edt_10l_stock.setFocusableInTouchMode(true);
        edt_10l_stock.setClickable(true);

    }

    private void AlreadyExistingUser(){

        linear_customer_info.setVisibility(View.VISIBLE);
        linear_customer_otp.setVisibility(View.GONE);
        btn_add_customer.setVisibility(View.VISIBLE);

    }

    /***************************
     * GET State
     ***************************/

    public void Action_Get_State() {

        String tag_json_obj = "json_obj_req";
        System.out.println("CAME 1");
        final StringRequest request = new StringRequest(Request.Method.POST,
                Appconfig.url_state_list, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.d(TAG, response.toString());
                try {
                    JSONObject obj = new JSONObject(response);
                    int success = obj.getInt("status");

                    if (success == 1) {

                        JSONArray arr;

                        arr = obj.getJSONArray("data");

                        Arraylist_state_id.clear();
                        Arraylist_state_name.clear();

                        for (int i = 0; arr.length() > i; i++) {
                            JSONObject obj1 = arr.getJSONObject(i);

                            String state_id = obj1.getString("state_id");
                            String state_name = obj1.getString("state_name");

                            Arraylist_state_id.add(state_id);
                            Arraylist_state_name.add(state_name);

                            try {
                                spn_state
                                        .setAdapter(new ArrayAdapter<String>(Activity_Add_Customer.this,
                                                android.R.layout.simple_spinner_dropdown_item,
                                                Arraylist_state_name));

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

                return params;
            }

        };

        // Adding request to request queue
        queue.add(request);
    }

    /***************************
     * GET City
     ***************************/

    public void Action_Get_City() {

        final StringRequest request = new StringRequest(Request.Method.POST,
                Appconfig.url_city_list, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.d(TAG, response.toString());
                try {
                    JSONObject obj = new JSONObject(response);
                    int success = obj.getInt("status");

                    if (success == 1) {

                        JSONArray arr;

                        arr = obj.getJSONArray("data");

                        Arraylist_city_id.clear();
                        Arraylist_city_name.clear();

                        for (int i = 0; arr.length() > i; i++) {
                            JSONObject obj1 = arr.getJSONObject(i);

                            String state_id = obj1.getString("city_id");
                            String state_name = obj1.getString("city_name");

                            Arraylist_city_id.add(state_id);
                            Arraylist_city_name.add(state_name);

                            try {
                                spn_city
                                        .setAdapter(new ArrayAdapter<String>(Activity_Add_Customer.this,
                                                android.R.layout.simple_spinner_dropdown_item,
                                                Arraylist_city_name));

                            } catch (Exception e) {

                            }

                        }

                        dialog.dismiss();
                    } else if (success == 0) {

                        dialog.dismiss();

                        Snackbar.make(parent_view, "No City Data Found", Snackbar.LENGTH_SHORT).show();

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

                params.put("state_id", str_state_id);

                System.out.println("State ID : " + str_state_id);

                return params;
            }

        };

        // Adding request to request queue
        queue.add(request);
    }

    /********************************
     *    Action_ADD_Customer
     *********************************/
    private void Action_Add_Customer() {

        StringRequest request = new StringRequest(Request.Method.POST,
                Appconfig.url_add_customer, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.d(TAG, response.toString());
                try {
                    JSONObject obj = new JSONObject(response);
                    int success = obj.getInt("status");
                    String message = obj.getString("message");

                    if (success == 1) {

                        dialog.dismiss();

                        new AlertDialog.Builder(Activity_Add_Customer.this)
                                .setTitle("Aqua Bill")
                                .setMessage("Customer Added Successfully !")
                                .setIcon(R.mipmap.ic_launcher)
                                .setPositiveButton("Ok",
                                        new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface alert_dialog,

                                                                int which) {
                                                // TODO Auto-generated method stub
                                                alert_dialog.dismiss();

                                                Intent i = new Intent(Activity_Add_Customer.this, Activity_Manage_Customer.class);
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
                params.put("mobile_no", str_mobile);
                params.put("customer_name", str_name);
                params.put("address", str_address);
                params.put("city", str_city_id);
                params.put("state", str_state_id);
                params.put("postal_code", str_postal);
                params.put("contact_person", str_contact_erson);
                params.put("email_id", str_email);
                params.put("gst_no", str_gst);
                params.put("opening_balance", str_opening_balance);
                params.put("twentyl_stock", str_20l_stock);
                params.put("tenl_stock", str_10l_stock);


                System.out.println("user_id" + str_dealer_id);
                System.out.println("mobile_no" + str_mobile);
                System.out.println("str_name" + str_name);
                System.out.println("str_address" + str_address);
                System.out.println("str_city_id" + str_city_id);
                System.out.println("str_state_id" + str_state_id);
                System.out.println("str_postal" + str_postal);
                System.out.println("str_contact_erson" + str_contact_erson);
                System.out.println("str_email" + str_email);
                System.out.println("str_gst" + str_gst);
                System.out.println("str_opening_balance" + str_opening_balance);
                System.out.println("str_20l_stock" + str_20l_stock);
                System.out.println("str_10l_stock" + str_10l_stock);

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

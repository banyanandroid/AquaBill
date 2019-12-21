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
import android.widget.Spinner;
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

public class Activity_View_Supplier extends AppCompatActivity {

    String TAG = "Products and Rates";
    private View parent_view;

    // Session Manager Class
    SessionManager session;
    private static long back_pressed;

    SpotsDialog dialog;
    public static RequestQueue queue;

    private Toolbar mToolbar;

    private String str_selected_supplier_id;

    private EditText edt_supplier_name, edt_supplier_address, edt_supplier_state, edt_supplier_city, edt_supplier_gst,
            edt_supplier_mobile, edt_supplier_email;

    private TextView txt_edit;

    TextView t1;

    private SearchableSpinner spn_state, spn_city;

    ArrayList<String> Arraylist_state_id = null;
    ArrayList<String> Arraylist_state_name = null;

    ArrayList<String> Arraylist_city_id = null;
    ArrayList<String> Arraylist_city_name = null;

    private Button btn_update_supplier;

    private String  str_supplier_id, str_supplier_name, str_supplier_Address, str_supplier_gst, str_supplier_mobile, str_supplier_email,
            str_supplier_state, str_supplier_city, str_supplier_state_name, str_supplier_city_name;

    private String str_up_sup_id, str_up_sup_name, str_up_sup_address, str_up_sup_gst, str_up_sup_mobi, str_up_sup_email, str_up_sup_state,
            str_up_sup_state_id, str_up_sup_city, str_up_sup_city_id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_supplier);

        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        parent_view = findViewById(android.R.id.content);

        setSupportActionBar(mToolbar);
        getSupportActionBar().setTitle("Supplier");
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        mToolbar.setNavigationIcon(getResources().getDrawable(R.drawable.ic_action_back));
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent i = new Intent(getApplicationContext(), Activity_Manage_Supplier.class);
                startActivity(i);
                finish();
            }
        });

        SharedPreferences sharedPreferences = PreferenceManager
                .getDefaultSharedPreferences(Activity_View_Supplier.this);
        str_selected_supplier_id = sharedPreferences.getString("str_supplier_id", "str_supplier_id");

        edt_supplier_name = (EditText) findViewById(R.id.view_supplier_edt_sup_name);
        edt_supplier_address= (EditText) findViewById(R.id.view_supplier_edt_sup_address);
        edt_supplier_state= (EditText) findViewById(R.id.view_supplier_edt_sup_state);
        edt_supplier_city= (EditText) findViewById(R.id.view_supplier_edt_sup_city);
        edt_supplier_gst = (EditText) findViewById(R.id.view_supplier_edt_gst);
        edt_supplier_mobile = (EditText) findViewById(R.id.view_supplier_edt_mob);
        edt_supplier_email = (EditText) findViewById(R.id.view_supplier_edt_email);

        txt_edit = (TextView) findViewById(R.id.viewsup_txt_edit);

        spn_state = (SearchableSpinner) findViewById(R.id.view_supplier_spn_state);
        spn_city = (SearchableSpinner) findViewById(R.id.view_supplier_spn_city);
        btn_update_supplier = (Button) findViewById(R.id.update_supplier_btn_add);

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
                str_up_sup_state_id = Arraylist_state_id.get(position);
                str_up_sup_state = Arraylist_state_name.get(position);

                System.out.println("STATE ID : " + str_up_sup_state_id);
                System.out.println("STATE NAME : " + str_up_sup_state);

                try {
                    dialog = new SpotsDialog(Activity_View_Supplier.this);
                    dialog.show();
                    queue = Volley.newRequestQueue(Activity_View_Supplier.this);
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
                str_up_sup_city_id = Arraylist_city_id.get(position);
                str_up_sup_city = Arraylist_city_name.get(position);

                System.out.println("CITY ID : " + str_up_sup_city_id);
                System.out.println("CITY NAME : " + str_up_sup_city);

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        txt_edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                spn_state.setVisibility(View.VISIBLE);
                spn_city.setVisibility(View.VISIBLE);
                btn_update_supplier.setVisibility(View.VISIBLE);

                edt_supplier_name.setFocusableInTouchMode(true);
                edt_supplier_name.setClickable(true);

                edt_supplier_address.setFocusableInTouchMode(true);
                edt_supplier_address.setClickable(true);

                edt_supplier_gst.setFocusableInTouchMode(true);
                edt_supplier_gst.setClickable(true);

                edt_supplier_mobile.setFocusableInTouchMode(true);
                edt_supplier_mobile.setClickable(true);

                edt_supplier_email.setFocusableInTouchMode(true);
                edt_supplier_email.setClickable(true);

            }
        });

        btn_update_supplier.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                str_up_sup_name = edt_supplier_name.getText().toString().trim();
                str_up_sup_address = edt_supplier_address.getText().toString().trim();
                str_up_sup_state = edt_supplier_state.getText().toString().trim();
                str_up_sup_city = edt_supplier_city.getText().toString().trim();
                str_up_sup_gst = edt_supplier_gst.getText().toString().trim();
                str_up_sup_mobi = edt_supplier_mobile.getText().toString().trim();
                str_up_sup_email = edt_supplier_email.getText().toString().trim();

                try {
                    dialog = new SpotsDialog(Activity_View_Supplier.this);
                    dialog.show();
                    queue = Volley.newRequestQueue(Activity_View_Supplier.this);
                    Action_Update_Profile();
                }catch (Exception e) {

                }
            }
        });

        try {
            dialog = new SpotsDialog(Activity_View_Supplier.this);
            dialog.show();
            queue = Volley.newRequestQueue(Activity_View_Supplier.this);
            Action_Get_Supplier_Profile();
        }catch (Exception e) {

        }
    }

    /***************************
     * GET State
     ***************************/

    public void Action_Get_Supplier_Profile() {

        String tag_json_obj = "json_obj_req";
        System.out.println("CAME 1");
        final StringRequest request = new StringRequest(Request.Method.POST,
                Appconfig.url_supplier_details, new Response.Listener<String>() {

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

                            str_supplier_id = obj1.getString("supplier_id");
                            str_supplier_name = obj1.getString("supplier_name");
                            str_supplier_mobile = obj1.getString("mobile_no");
                            str_supplier_Address = obj1.getString("address");
                            str_up_sup_state_id = obj1.getString("state");
                            str_up_sup_city_id = obj1.getString("city");
                            str_supplier_gst = obj1.getString("gst");
                            str_supplier_email = obj1.getString("email_id");
                            str_supplier_city_name = obj1.getString("cityname");
                            str_supplier_state_name = obj1.getString("statename");

                            SetData();
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

                params.put("supplier_id", str_selected_supplier_id);

                System.out.println("supplier_id : " + str_selected_supplier_id);

                return params;
            }

        };

        // Adding request to request queue
        queue.add(request);
    }

    private void SetData() {

        edt_supplier_name.setText("" + str_supplier_name);
        edt_supplier_address.setText("" + str_supplier_Address);
        edt_supplier_state.setText("" + str_supplier_state_name);
        edt_supplier_city.setText("" + str_supplier_city_name);
        edt_supplier_gst.setText("" + str_supplier_gst);
        edt_supplier_mobile.setText("" + str_supplier_mobile);
        edt_supplier_email.setText("" + str_supplier_email);

        try {
            queue = Volley.newRequestQueue(Activity_View_Supplier.this);
            Action_Get_State();
        }catch (Exception e) {

        }
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
                                        .setAdapter(new ArrayAdapter<String>(Activity_View_Supplier.this,
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
                                        .setAdapter(new ArrayAdapter<String>(Activity_View_Supplier.this,
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

                params.put("state_id", str_up_sup_state_id);

                System.out.println("State ID : " + str_up_sup_state_id);

                return params;
            }

        };

        // Adding request to request queue
        queue.add(request);
    }

    /********************************
     * Update Supplier
     *********************************/
    private void Action_Update_Profile() {

        StringRequest request = new StringRequest(Request.Method.POST,
                Appconfig.url_update_supplier__profile, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.d(TAG, response.toString());
                try {
                    JSONObject obj = new JSONObject(response);
                    int success = obj.getInt("status");
                    String str_msg = obj.getString("message");

                    if (success == 1) {

                        dialog.dismiss();

                        new AlertDialog.Builder(Activity_View_Supplier.this)
                                .setTitle("Aqua Bill")
                                .setMessage("Supplier Profile Updated Successfully !")
                                .setIcon(R.mipmap.ic_launcher)
                                .setPositiveButton("Ok",
                                        new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface alert_dialog,

                                                                int which) {
                                                // TODO Auto-generated method stub

                                                try {
                                                   Intent i = new Intent(Activity_View_Supplier.this, Activity_Manage_Supplier.class);
                                                   startActivity(i);
                                                   finish();
                                                }catch (Exception e) {

                                                }

                                            }

                                        }).show();

                    }else if (success == 0){

                        Snackbar.make(parent_view, "Something Went Wrong Try Again!", Snackbar.LENGTH_SHORT).show();

                    }else {
                        Snackbar.make(parent_view, "" + str_msg, Snackbar.LENGTH_SHORT).show();
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

                params.put("supplier_id", str_selected_supplier_id);
                params.put("supplier_name", str_up_sup_name);
                params.put("address", str_up_sup_address);
                params.put("state", str_up_sup_state_id);
                params.put("city", str_up_sup_city_id);
                params.put("gst", str_up_sup_gst);
                params.put("mobile_no", str_up_sup_mobi);
                params.put("email_id", str_up_sup_email);

                System.out.println("supplier_id " + str_selected_supplier_id);
                System.out.println("supplier_name " + str_up_sup_name);
                System.out.println("str_up_address " + str_up_sup_address);
                System.out.println("str_up_state " + str_up_sup_state_id);
                System.out.println("str_up_city " + str_up_sup_city_id);
                System.out.println("gst " + str_up_sup_gst);
                System.out.println("str_up_mobile_no " + str_up_sup_mobi);
                System.out.println("str_up_email_id " + str_up_sup_email);

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

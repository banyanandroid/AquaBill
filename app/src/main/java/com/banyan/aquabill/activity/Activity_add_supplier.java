package com.banyan.aquabill.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
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

public class Activity_add_supplier extends AppCompatActivity {

    String TAG = "Add Supplier";
    private View parent_view;

    // Session Manager Class
    SessionManager session;
    private static long back_pressed;

    SpotsDialog dialog;
    public static RequestQueue queue;

    private Toolbar mToolbar;

    private EditText edt_supplier_name, edt_supplier_address, edt_supplier_gst, edt_supplier_mobile, edt_supplier_email;

    private SearchableSpinner spn_state, spn_city;

    private Button btn_update;

    TextView t1;

    private String str_supplier_name, str_supplier_Address, str_supplier_gst, str_supplier_mobile, str_supplier_email,
            str_supplier_state_id, str_supplier_state, str_supplier_city_id, str_supplier_city;

    private String str_dealer_id;

    ArrayList<String> Arraylist_state_id = null;
    ArrayList<String> Arraylist_state_name = null;

    ArrayList<String> Arraylist_city_id = null;
    ArrayList<String> Arraylist_city_name = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_supplier);

        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        parent_view = findViewById(android.R.id.content);

        setSupportActionBar(mToolbar);
        getSupportActionBar().setTitle("Add Supplier");
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

        // Session Manager
        session = new SessionManager(getApplicationContext());
        HashMap<String, String> user = session.getUserDetails();
        str_dealer_id = user.get(SessionManager.KEY_DEALER_ID);

        Arraylist_state_id = new ArrayList<String>();
        Arraylist_state_name = new ArrayList<String>();

        Arraylist_city_id = new ArrayList<String>();
        Arraylist_city_name = new ArrayList<String>();

        edt_supplier_name = (EditText) findViewById(R.id.add_supplier_edt_sup_name);
        edt_supplier_address= (EditText) findViewById(R.id.add_supplier_edt_sup_address);
        edt_supplier_gst = (EditText) findViewById(R.id.add_supplier_edt_gst);
        edt_supplier_mobile = (EditText) findViewById(R.id.add_supplier_edt_mob);
        edt_supplier_email = (EditText) findViewById(R.id.add_supplier_edt_email);

        spn_state = (SearchableSpinner) findViewById(R.id.add_supplier_spn_state);
        spn_city = (SearchableSpinner) findViewById(R.id.add_supplier_spn_city);

        btn_update = (Button) findViewById(R.id.add_supplier_btn_add);

        spn_state.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                System.out.println("Shop");

                t1 = (TextView) view;
                String str_state = t1.getText().toString();
                str_supplier_state_id = Arraylist_state_id.get(position);
                str_supplier_state = Arraylist_state_name.get(position);

                System.out.println("STATE ID : " + str_supplier_state_id);
                System.out.println("STATE NAME : " + str_supplier_state);

                try {
                    dialog = new SpotsDialog(Activity_add_supplier.this);
                    dialog.show();
                    queue = Volley.newRequestQueue(Activity_add_supplier.this);
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
                str_supplier_city_id = Arraylist_city_id.get(position);
                str_supplier_city = Arraylist_city_name.get(position);

                System.out.println("CITY ID : " + str_supplier_city_id);
                System.out.println("CITY NAME : " + str_supplier_city);

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        btn_update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                str_supplier_name = edt_supplier_name.getText().toString().trim();
                str_supplier_Address = edt_supplier_address.getText().toString().trim();
                str_supplier_gst = edt_supplier_gst.getText().toString().trim();
                str_supplier_mobile = edt_supplier_mobile.getText().toString().trim();
                str_supplier_email = edt_supplier_email.getText().toString().trim();

                try {
                    dialog = new SpotsDialog(Activity_add_supplier.this);
                    dialog.show();
                    queue = Volley.newRequestQueue(Activity_add_supplier.this);
                    Action_ADD_Profile();
                }catch (Exception e) {

                }

            }
        });

        try {
            dialog = new SpotsDialog(Activity_add_supplier.this);
            dialog.show();
            queue = Volley.newRequestQueue(Activity_add_supplier.this);
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
                                        .setAdapter(new ArrayAdapter<String>(Activity_add_supplier.this,
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
                                        .setAdapter(new ArrayAdapter<String>(Activity_add_supplier.this,
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

                params.put("state_id", str_supplier_state_id);

                System.out.println("State ID : " + str_supplier_state_id);

                return params;
            }

        };

        // Adding request to request queue
        queue.add(request);
    }

    /********************************
     * Add Supplier
     *********************************/
    private void Action_ADD_Profile() {

        StringRequest request = new StringRequest(Request.Method.POST,
                Appconfig.url_add_supplier, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.d(TAG, response.toString());
                try {
                    JSONObject obj = new JSONObject(response);
                    int success = obj.getInt("status");

                    if (success == 1) {

                        dialog.dismiss();

                        new AlertDialog.Builder(Activity_add_supplier.this)
                                .setTitle("Aqua Bill")
                                .setMessage("Supplier Added Successfully !")
                                .setIcon(R.mipmap.ic_launcher)
                                .setPositiveButton("Ok",
                                        new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface alert_dialog,

                                                                int which) {
                                                // TODO Auto-generated method stub

                                               Intent i = new Intent(Activity_add_supplier.this, Activity_Manage_Supplier.class);
                                               startActivity(i);
                                               finish();
                                            }

                                        }).show();

                    }else if (success == 0){

                        Snackbar.make(parent_view, "Something Went Wrong Try Again!", Snackbar.LENGTH_SHORT).show();

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

                params.put("user_id", str_dealer_id);
                params.put("supplier_name", str_supplier_name);
                params.put("address", str_supplier_Address);
                params.put("state", str_supplier_state_id);
                params.put("city", str_supplier_city_id);
                params.put("gst", str_supplier_gst);
                params.put("mobile_no", str_supplier_mobile);
                params.put("email_id", str_supplier_email);

                System.out.println("user_id " + str_dealer_id);
                System.out.println("supplier_name " + str_supplier_name);
                System.out.println("address " + str_supplier_Address);
                System.out.println("str_up_state " + str_supplier_state_id);
                System.out.println("str_up_city " + str_supplier_city_id);
                System.out.println("gst " + str_supplier_gst);
                System.out.println("str_up_mobile_no " + str_supplier_mobile);
                System.out.println("str_up_email_id " + str_supplier_email);

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

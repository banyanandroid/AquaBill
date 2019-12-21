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

public class Activity_Profile extends AppCompatActivity {

    // Session Manager Class
    SessionManager session;

    private Toolbar mToolbar;

    private String str_dealer_id;

    private String str_dealer_code, str_dealer_name, str_address, str_state , str_city, str_postal_code, str_contact_person,
            str_mobile_no, str_email_id, str_gst_no, str_fssai_no, str_prefix;

    private String str_up_dealer_code, str_up_dealer_name, str_up_address, str_up_state, str_up_state_id , str_up_city, str_up_city_id,
            str_up_postal_code, str_up_contact_person, str_up_mobile_no, str_up_email_id, str_up_gst_no, str_up_fssai_no, str_up_prefix;

    private EditText edit_dealer_code, edit_dealer_name, edit_address, edit_state, edit_city, edit_postal_code, edit_ontact_person,
            edit_mobile_no, edit_email_id, edit_gst_no, edit_fssai_no, edit_prefix;

    private Button btn_update;

    private TextView prof_txt_name, txt_edit;

    TextView t1;

    private SearchableSpinner spn_state, spn_city;

    ArrayList<String> Arraylist_state_id = null;
    ArrayList<String> Arraylist_state_name = null;

    ArrayList<String> Arraylist_city_id = null;
    ArrayList<String> Arraylist_city_name = null;

    SpotsDialog dialog;
    public static RequestQueue queue;
    String TAG = "reg";

    private View parent_view;
    private static long back_pressed;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        parent_view = findViewById(android.R.id.content);

        setSupportActionBar(mToolbar);
        getSupportActionBar().setTitle("Profile");
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        mToolbar.setNavigationIcon(getResources().getDrawable(R.drawable.ic_action_back));
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent i = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(i);
                finish();
            }
        });

        // Session Manager
        session = new SessionManager(getApplicationContext());
        HashMap<String, String> user = session.getUserDetails();
        str_dealer_id = user.get(SessionManager.KEY_DEALER_ID);

        edit_dealer_code = (EditText) findViewById(R.id.pro_edt_dealer_code);
        edit_dealer_name = (EditText) findViewById(R.id.pro_edt_dealer_name);
        edit_address  = (EditText) findViewById(R.id.pro_edt_address);
        edit_state = (EditText) findViewById(R.id.pro_edt_state);
        edit_city = (EditText) findViewById(R.id.pro_edt_city);
        edit_postal_code = (EditText) findViewById(R.id.pro_edt_postal);
        edit_ontact_person = (EditText) findViewById(R.id.pro_edt_contact_person);
        edit_mobile_no = (EditText) findViewById(R.id.pro_edt_mobileno);
        edit_email_id = (EditText) findViewById(R.id.pro_edt_email);
        edit_gst_no = (EditText) findViewById(R.id.pro_edt_gst);
        edit_fssai_no = (EditText) findViewById(R.id.pro_edt_fssai);
        edit_prefix = (EditText) findViewById(R.id.pro_edt_prefix);

        txt_edit = (TextView) findViewById(R.id.prof_txt_edit);
        prof_txt_name  = (TextView) findViewById(R.id.prof_txt_name);

        btn_update = (Button) findViewById(R.id.prof_btn_logout);

        spn_state = (SearchableSpinner) findViewById(R.id.prof_spn_state);
        spn_city = (SearchableSpinner) findViewById(R.id.prof_spn_city);

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
                str_up_state_id = Arraylist_state_id.get(position);
                str_up_state = Arraylist_state_name.get(position);

                System.out.println("STATE ID : " + str_up_state_id);
                System.out.println("STATE NAME : " + str_up_state);

                try {
                    dialog = new SpotsDialog(Activity_Profile.this);
                    dialog.show();
                    queue = Volley.newRequestQueue(Activity_Profile.this);
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
                str_up_city_id = Arraylist_city_id.get(position);
                str_up_city = Arraylist_city_name.get(position);

                System.out.println("CITY ID : " + str_up_city_id);
                System.out.println("CITY NAME : " + str_up_city);

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
                btn_update.setVisibility(View.VISIBLE);

                edit_dealer_name.setFocusableInTouchMode(true);
                edit_dealer_name.setClickable(true);

                edit_address.setFocusableInTouchMode(true);
                edit_address.setClickable(true);

                edit_state.setFocusableInTouchMode(true);
                edit_state.setClickable(true);

                edit_city.setFocusableInTouchMode(true);
                edit_city.setClickable(true);

                edit_postal_code.setFocusableInTouchMode(true);
                edit_postal_code.setClickable(true);

                edit_ontact_person.setFocusableInTouchMode(true);
                edit_ontact_person.setClickable(true);

                edit_mobile_no.setFocusableInTouchMode(true);
                edit_mobile_no.setClickable(true);

                edit_email_id.setFocusableInTouchMode(true);
                edit_email_id.setClickable(true);

                edit_fssai_no.setFocusableInTouchMode(true);
                edit_fssai_no.setClickable(true);

                edit_prefix.setFocusableInTouchMode(true);
                edit_prefix.setClickable(true);


                try {
                    dialog = new SpotsDialog(Activity_Profile.this);
                    dialog.show();
                    queue = Volley.newRequestQueue(Activity_Profile.this);
                    Action_Get_State();
                }catch (Exception e) {

                }
            }
        });

        btn_update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                str_up_dealer_code = edit_dealer_code.getText().toString().trim();
                str_up_dealer_name = edit_dealer_name.getText().toString().trim();
                str_up_address = edit_address.getText().toString().trim();
                str_up_state_id = edit_state.getText().toString().trim();
                str_up_city_id = edit_city.getText().toString().trim();
                str_up_postal_code = edit_postal_code.getText().toString().trim();
                str_up_contact_person = edit_ontact_person.getText().toString().trim();
                str_up_mobile_no = edit_mobile_no.getText().toString().trim();
                str_up_email_id = edit_email_id.getText().toString().trim();
                str_up_gst_no = edit_gst_no.getText().toString().trim();
                str_up_fssai_no = edit_fssai_no.getText().toString().trim();
                str_up_prefix = edit_prefix.getText().toString().trim();

                try {
                    dialog = new SpotsDialog(Activity_Profile.this);
                    dialog.show();
                    queue = Volley.newRequestQueue(Activity_Profile.this);
                    Action_Update_Profile();
                }catch (Exception e) {

                }
            }
        });

        try {
            dialog = new SpotsDialog(Activity_Profile.this);
            dialog.show();
            queue = Volley.newRequestQueue(Activity_Profile.this);
            Action_Get_Profile();
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
                                        .setAdapter(new ArrayAdapter<String>(Activity_Profile.this,
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
                                        .setAdapter(new ArrayAdapter<String>(Activity_Profile.this,
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

                params.put("state_id", str_up_state_id);

                System.out.println("State ID : " + str_up_state_id);

                return params;
            }

        };

        // Adding request to request queue
        queue.add(request);
    }
    /********************************
     * Get Points FUNCTION
     *********************************/
    private void Action_Get_Profile() {

        StringRequest request = new StringRequest(Request.Method.POST,
                Appconfig.url_profile, new Response.Listener<String>() {

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

                            str_dealer_code = obj1.getString("dealer_code");
                            str_dealer_name = obj1.getString("dealer_name");
                            str_address = obj1.getString("address");
                            str_state = obj1.getString("state");
                            str_city = obj1.getString("city");
                            str_postal_code = obj1.getString("postal_code");
                            str_contact_person = obj1.getString("contact_person");
                            str_mobile_no = obj1.getString("mobile_no");
                            str_email_id = obj1.getString("email_id");
                            str_gst_no = obj1.getString("gst_no");
                            str_fssai_no = obj1.getString("fssai_no");
                            str_prefix = obj1.getString("prefix");
                        }

                        SetText();

                    }else if (success == 0){

                        Snackbar.make(parent_view, "No Data Found", Snackbar.LENGTH_SHORT).show();

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

                System.out.println("user_id " + str_dealer_id);

                return params;
            }
        };
        // Adding request to request queue
        queue.add(request);
    }

    private void SetText() {
        prof_txt_name.setText("" + str_dealer_name);
        edit_dealer_code.setText("" + str_dealer_code);
        edit_dealer_name.setText("" + str_dealer_name);
        edit_address.setText("" + str_address);
        edit_state.setText("" + str_state);
        edit_city.setText("" + str_city);
        edit_postal_code.setText("" + str_postal_code);
        edit_ontact_person.setText("" + str_contact_person);
        edit_mobile_no.setText("" + str_mobile_no);
        edit_email_id.setText("" + str_email_id);
        edit_gst_no.setText("" + str_gst_no);
        edit_fssai_no.setText("" + str_fssai_no);
        edit_prefix.setText("" + str_prefix);
    }

    /********************************
     * Get Points FUNCTION
     *********************************/
    private void Action_Update_Profile() {

        StringRequest request = new StringRequest(Request.Method.POST,
                Appconfig.url_update_profile, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.d(TAG, response.toString());
                try {
                    JSONObject obj = new JSONObject(response);
                    int success = obj.getInt("status");

                    if (success == 1) {

                        dialog.dismiss();

                        new AlertDialog.Builder(Activity_Profile.this)
                                .setTitle("Aqua Bill")
                                .setMessage("Profile UpdatedSuccessfully !")
                                .setIcon(R.mipmap.ic_launcher)
                                .setPositiveButton("Ok",
                                        new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface alert_dialog,

                                                                int which) {
                                                // TODO Auto-generated method stub

                                                try {
                                                    dialog = new SpotsDialog(Activity_Profile.this);
                                                    dialog.show();
                                                    HideEdit();
                                                    queue = Volley.newRequestQueue(Activity_Profile.this);
                                                    Action_Get_Profile();
                                                }catch (Exception e) {

                                                }

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
                params.put("dealer_name", str_up_dealer_name);
                params.put("address", str_up_address);
                params.put("state", str_up_state_id);
                params.put("city", str_up_city_id);
                params.put("postal_code", str_up_postal_code);
                params.put("contact_person", str_up_contact_person);
                params.put("mobile_no", str_up_mobile_no);
                params.put("email_id", str_up_email_id);
                params.put("fssai_no", str_up_fssai_no);
                params.put("language", "");
                params.put("prefix", str_up_prefix);

                System.out.println("user_id " + str_dealer_id);
                System.out.println("str_up_dealer_name " + str_up_dealer_name);
                System.out.println("str_up_address " + str_up_address);
                System.out.println("str_up_state " + str_up_state_id);
                System.out.println("str_up_city " + str_up_city_id);
                System.out.println("str_up_postal_code " + str_up_postal_code);
                System.out.println("str_up_contact_person " + str_up_contact_person);
                System.out.println("str_up_mobile_no " + str_up_mobile_no);
                System.out.println("str_up_email_id " + str_up_email_id);
                System.out.println("str_up_fssai_no " + str_up_fssai_no);
                System.out.println("str_up_prefix " + str_up_prefix);

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

    private void HideEdit() {

        spn_state.setVisibility(View.GONE);
        spn_city.setVisibility(View.GONE);
        btn_update.setVisibility(View.GONE);

        edit_dealer_name.setFocusableInTouchMode(false);
        edit_dealer_name.setClickable(false);

        edit_address.setFocusableInTouchMode(false);
        edit_address.setClickable(false);

        edit_state.setFocusableInTouchMode(false);
        edit_state.setClickable(false);

        edit_city.setFocusableInTouchMode(false);
        edit_city.setClickable(false);

        edit_postal_code.setFocusableInTouchMode(false);
        edit_postal_code.setClickable(false);

        edit_ontact_person.setFocusableInTouchMode(false);
        edit_ontact_person.setClickable(false);

        edit_mobile_no.setFocusableInTouchMode(false);
        edit_mobile_no.setClickable(false);

        edit_email_id.setFocusableInTouchMode(false);
        edit_email_id.setClickable(false);

        edit_fssai_no.setFocusableInTouchMode(false);
        edit_fssai_no.setClickable(false);

        edit_prefix.setFocusableInTouchMode(false);
        edit_prefix.setClickable(false);
    }
    @Override
    public void onBackPressed() {

        Intent i = new Intent(getApplicationContext(), MainActivity.class);
        startActivity(i);
        finish();
    }
}

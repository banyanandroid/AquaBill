package com.banyan.aquabill.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

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

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import dmax.dialog.SpotsDialog;

public class Activity_View_Customer extends AppCompatActivity {

    String TAG = "Customer Info";
    private View parent_view;

    // Session Manager Class
    SessionManager session;
    private static long back_pressed;

    SpotsDialog dialog;
    public static RequestQueue queue;

    private Toolbar mToolbar;

    private EditText edt_mobile_no, edt_otp, edt_name, edt_address, edt_state, edt_city, edt_postal,edt_contact_person,edt_email,edt_gst,
            edt_opening_bal,edt_20l_stock,edt_10l_stock;

    private String str_mob, str_name, str_address, str_state, str_state_id, str_city, str_city_id,str_postal,str_contact_erson,
            str_email, str_gst, str_opening_balance, str_20l_stock, str_10l_stock;

    String str_dealer_id, str_customer_id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_customer);

        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        parent_view = findViewById(android.R.id.content);

        setSupportActionBar(mToolbar);
        getSupportActionBar().setTitle("Customer Info");
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        mToolbar.setNavigationIcon(getResources().getDrawable(R.drawable.ic_action_back));
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent i = new Intent(getApplicationContext(), Activity_Manage_Customer.class);
                startActivity(i);
                finish();
            }
        });

        session = new SessionManager(getApplicationContext());
        HashMap<String, String> user = session.getUserDetails();
        str_dealer_id = user.get(SessionManager.KEY_DEALER_ID);

        SharedPreferences sharedPreferences = PreferenceManager
                .getDefaultSharedPreferences(Activity_View_Customer.this);
        str_customer_id = sharedPreferences.getString("str_customer_id", "str_customer_id");

        edt_mobile_no = (EditText) findViewById(R.id.view_customer_edt_mob);
        edt_name = (EditText) findViewById(R.id.view_customer_edt_name);
        edt_address = (EditText) findViewById(R.id.view_customer_edt_address);
        edt_city = (EditText) findViewById(R.id.view_customer_edt_city);
        edt_state = (EditText) findViewById(R.id.view_customer_edt_state);
        edt_postal = (EditText) findViewById(R.id.view_customer_edt_postalcode);
        edt_contact_person = (EditText) findViewById(R.id.view_customer_edt_contact_person);
        edt_email = (EditText) findViewById(R.id.view_customer_edt_email);
        edt_gst = (EditText) findViewById(R.id.view_customer_edt_gst);
        edt_opening_bal = (EditText) findViewById(R.id.view_customer_edt_opening_balance);
        edt_20l_stock = (EditText) findViewById(R.id.view_customer_edt_20l_stock);
        edt_10l_stock = (EditText) findViewById(R.id.view_customer_edt_10l_stock);


        try {
            dialog = new SpotsDialog(Activity_View_Customer.this);
            dialog.show();
            queue = Volley.newRequestQueue(Activity_View_Customer.this);
            Action_Get_CustomerInfo();
        }catch (Exception e) {

        }
    }

    /********************************
     *    Action_Check_Customer
     *********************************/
    private void Action_Get_CustomerInfo() {

        StringRequest request = new StringRequest(Request.Method.POST,
                Appconfig.url_view_customer, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.d(TAG, response.toString());
                try {
                    JSONObject obj = new JSONObject(response);
                    int success = obj.getInt("status");
                    String message = obj.getString("data");

                    if (success == 1) {

                        JSONObject obj_data = obj.getJSONObject("data");

                        String customer_name = obj_data.getString("customer_name");
                        String address = obj_data.getString("address");
                        String str_city = obj_data.getString("city");
                        String city_name  = obj_data.getString("city_name");
                        String str_state  = obj_data.getString("state");
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
                params.put("customer_id", str_customer_id);

                System.out.println("user_id" + str_dealer_id);
                System.out.println("str_customer_id" + str_customer_id);

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

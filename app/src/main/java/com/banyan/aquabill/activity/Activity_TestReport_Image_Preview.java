package com.banyan.aquabill.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.Toast;

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
import com.bumptech.glide.Glide;
import com.google.android.material.snackbar.Snackbar;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import dmax.dialog.SpotsDialog;

public class Activity_TestReport_Image_Preview extends AppCompatActivity {

    String TAG = "Test Report";
    private View parent_view;

    // Session Manager Class
    SessionManager session;
    private static long back_pressed;

    SpotsDialog dialog;
    public static RequestQueue queue;

    private Toolbar mToolbar;
    private ImageView img_report;

    String str_pdu_id, str_month_id, str_type_id = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_testreport_image_preview);

        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        parent_view = findViewById(android.R.id.content);

        setSupportActionBar(mToolbar);
        getSupportActionBar().setTitle("Test Reports");
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        mToolbar.setNavigationIcon(getResources().getDrawable(R.drawable.ic_action_back));
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent i = new Intent(getApplicationContext(), Activity_TestReport.class);
                startActivity(i);
                finish();
            }
        });

        img_report = (ImageView) findViewById(R.id.test_report_img);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            str_type_id = extras.getString("Str_select_type");
            str_month_id = extras.getString("Str_select_month");
        } else {

        }

        SharedPreferences sharedPreferences = PreferenceManager
                .getDefaultSharedPreferences(Activity_TestReport_Image_Preview.this);
        str_pdu_id = sharedPreferences.getString("selected_pdu_id", "selected_pdu_id");

        if (str_pdu_id.equals("") || str_month_id.equals("") || str_type_id.equals("")){
            Toast.makeText(getApplicationContext(), "Internal Error Please Try Again", Toast.LENGTH_SHORT).show();
        }else {

            try {
                dialog = new SpotsDialog(Activity_TestReport_Image_Preview.this);
                dialog.show();
                queue = Volley.newRequestQueue(Activity_TestReport_Image_Preview.this);
                Action_Get_TestReport();
            }catch (Exception e) {

            }
        }

    }

    /********************************
     * Get Test Report
     *********************************/
    private void Action_Get_TestReport() {

        StringRequest request = new StringRequest(Request.Method.POST,
                Appconfig.url_test_report, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.d(TAG, response.toString());
                try {
                    JSONObject obj = new JSONObject(response);
                    int success = obj.getInt("status");

                    if (success == 1) {

                        String str_img = obj.getString("data");

                        Glide.with(getApplicationContext())
                                .load(str_img)
                                .thumbnail(0.5f)
                                .into(img_report);

                        dialog.dismiss();
                    } else if (success == 0) {

                        dialog.dismiss();

                        Snackbar.make(parent_view, "No Report Found", Snackbar.LENGTH_SHORT).show();

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

                params.put("pdu_id", str_pdu_id);
                params.put("type_id", str_type_id);
                params.put("month", str_month_id);

                System.out.println("str_pdu_id " + str_pdu_id);
                System.out.println("str_type_id " + str_type_id);
                System.out.println("str_month_id " + str_month_id);

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


}

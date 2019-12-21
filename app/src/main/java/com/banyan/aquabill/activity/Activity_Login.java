package com.banyan.aquabill.activity;

import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

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
import com.banyan.aquabill.global.MyFun;
import com.banyan.aquabill.global.SessionManager;
import com.google.android.material.snackbar.Snackbar;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import dmax.dialog.SpotsDialog;

public class Activity_Login extends AppCompatActivity {

    String TAG = "Login";
    private View parent_view;

    EditText edt_username, edt_password;
    Button btn_login;
    TextView txt_forgot_pwd;
    String str_username, str_password;
    SpotsDialog dialog;
    public static RequestQueue queue;

    // Session Manager Class
    SessionManager session;

    private static long back_pressed;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.action_login);

        parent_view = findViewById(android.R.id.content);

        edt_username = (EditText) findViewById(R.id.login_edt_username);
        edt_password = (EditText) findViewById(R.id.login_edt_password);

        txt_forgot_pwd = (TextView) findViewById(R.id.login_txt_forgot_pwd);

        btn_login = (Button) findViewById(R.id.login_btn_login);

        session = new SessionManager(getApplicationContext());

        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                str_username = edt_username.getText().toString().trim();
                str_password = edt_password.getText().toString().trim();

                if (str_username.equals("")){
                    edt_username.setError("Please Enter Username");
                    Snackbar.make(parent_view, "Please Enter Username", Snackbar.LENGTH_SHORT).show();
                }else if (str_password.equals("")){
                    edt_username.setError("Please Enter Password");
                    Snackbar.make(parent_view, "Please Enter Password", Snackbar.LENGTH_SHORT).show();
                }else {
                    dialog = new SpotsDialog(Activity_Login.this);
                    dialog.show();
                    queue = Volley.newRequestQueue(Activity_Login.this);
                    Action_Login();
                }
            }
        });
    }

    /********************************
     * LOGIN FUNCTION
     *********************************/
    private void Action_Login() {

        System.out.println("URL :: " + Appconfig.url_login);

        StringRequest request = new StringRequest(Request.Method.POST,
                Appconfig.url_login, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.d(TAG, response.toString());
                try {
                    JSONObject obj = new JSONObject(response);
                    int success = obj.getInt("status");
                    String message = obj.getString("message");

                    if (success == 1) {

                        JSONObject obj_data = obj.getJSONObject("data");

                        String dealer_id = obj_data.getString("dealer_id");
                        String usertype = obj_data.getString("usertype");
                        String dealer_code = obj_data.getString("dealer_code");
                        String dealer_name  = obj_data.getString("dealer_name");
                        String email_id  = obj_data.getString("email_id");
                        String mobile_no  = obj_data.getString("mobile_no");
                        String language  = obj_data.getString("language");

                        session.createLoginSession(dealer_id, usertype, dealer_code, dealer_name, email_id, mobile_no, language);

                        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
                        SharedPreferences.Editor editor = sharedPreferences.edit();

                        editor.putString("dealer_id", dealer_id);
                        editor.putString("usertype", usertype);
                        editor.putString("dealer_code", dealer_code);
                        editor.putString("dealer_name", dealer_name);
                        editor.putString("email_id", email_id);
                        editor.putString("mobile_no", mobile_no);
                        editor.putString("language", language);

                        editor.commit();

                        dialog.dismiss();

                        Intent i = new Intent(getApplicationContext(), MainActivity.class);
                        startActivity(i);
                        finish();


                    } else {

                        showCustomDialogFail(message);

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

                dialog.dismiss();
                showCustomDialogFail(""+error);
            }
        }) {

            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();

                params.put("username", str_username);
                params.put("password", str_password);

                System.out.println("str_username" + str_username);
                System.out.println("password" + str_password);

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

    private void  showCustomDialogFail(String str_fail_status) {

        final Dialog dialog = new Dialog(Activity_Login.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE); // before
        dialog.setContentView(R.layout.dialog_fail);
        dialog.setCancelable(true);

        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(dialog.getWindow().getAttributes());
        lp.width = WindowManager.LayoutParams.WRAP_CONTENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;

        ((TextView) dialog.findViewById(R.id.fail_content)).setText(""+str_fail_status);

        ((AppCompatButton) dialog.findViewById(R.id.bt_close)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        dialog.show();
        dialog.getWindow().setAttributes(lp);
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

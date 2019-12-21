package com.banyan.aquabill.activity;

import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
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
import java.util.Calendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import dmax.dialog.SpotsDialog;

public class Activity_View_Purchase_Raw_Material extends AppCompatActivity {

    String TAG = "Raw Material Purchase";
    private View parent_view;

    // Session Manager Class
    SessionManager session;
    private static long back_pressed;

    SpotsDialog dialog;
    public static RequestQueue queue;

    private Toolbar mToolbar;

    String str_dealer_id;

    private EditText edit_pur_date, edit_pur_invoice_no, edit_pur_address, edit_pur_gst, edit_pur_qty,
            edit_pur_rate, edit_pur_value, edit_supplier, edit_product;

    private SearchableSpinner spn_supplier, spn_raw_material;

    private TextView txt_edt;

    TextView t1;

    private String str_pur_date, str_pur_invoice_no, str_pur_supplier_id, str_pur_supplier, str_pur_supplier_addr, str_pur_supplier_gst,
            str_pur_raw_material, str_pur_raw_material_id, str_pur_qty, str_pur_ratw, str_pur_value, str_final_param;

    private Button btn_update_rawmaterial;

    ArrayList<String> Arraylist_supplier_id = null;
    ArrayList<String> Arraylist_supplier_name = null;

    ArrayList<String> Arraylist_rawmaterial_id = null;
    ArrayList<String> Arraylist_rawmaterial_name = null;

    int int_qty, int_rate, int_value;

    String str_sp_purchase_id, str_sp_supplier_id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edt_purchase_raw_material);

        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        parent_view = findViewById(android.R.id.content);

        setSupportActionBar(mToolbar);
        getSupportActionBar().setTitle("Raw Material Purchase");
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        mToolbar.setNavigationIcon(getResources().getDrawable(R.drawable.ic_action_back));
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent i = new Intent(getApplicationContext(), Activity_Purchase_Raw_Material.class);
                startActivity(i);
                finish();
            }
        });

        session = new SessionManager(getApplicationContext());
        HashMap<String, String> user = session.getUserDetails();
        str_dealer_id = user.get(SessionManager.KEY_DEALER_ID);

        txt_edt = (TextView) findViewById(R.id.edt_purchase_raw_mat_txt_edit);
        edit_supplier = (EditText) findViewById(R.id.purchase_edt_rawmat_edt_supplier);
        edit_product = (EditText) findViewById(R.id.purchase_edt_rawmat_edt_product);

        edit_pur_date = (EditText) findViewById(R.id.purchase_edt_rawmat_edt_date);
        edit_pur_invoice_no = (EditText) findViewById(R.id.purchase_edt_rawmat_edt_invoiceno);
        edit_pur_address = (EditText) findViewById(R.id.purchase_edt_rawmat_edt_sup_address);
        edit_pur_gst = (EditText) findViewById(R.id.purchase_edt_rawmat_edt_sup_gst);
        edit_pur_qty = (EditText) findViewById(R.id.purchase_qty_rawmat_edt_sup_qty);
        edit_pur_rate = (EditText) findViewById(R.id.purchase_edt_rawmat_edt_sup_rate);
        edit_pur_value = (EditText) findViewById(R.id.purchase_edt_rawmat_edt_sup_value);

        spn_supplier = (SearchableSpinner) findViewById(R.id.purchase_edt_rawmat_spn_supplier);
        spn_raw_material = (SearchableSpinner) findViewById(R.id.purchase_rawmat_spn_raw_material);

        btn_update_rawmaterial = (Button) findViewById(R.id.purchase_edt_rawmat_btn_add_purchase);


        Arraylist_supplier_id = new ArrayList<String>();
        Arraylist_supplier_name = new ArrayList<String>();

        Arraylist_rawmaterial_id = new ArrayList<String>();
        Arraylist_rawmaterial_name = new ArrayList<String>();

        SharedPreferences sharedPreferences = PreferenceManager
                .getDefaultSharedPreferences(Activity_View_Purchase_Raw_Material.this);
        str_sp_purchase_id = sharedPreferences.getString("str_purchase_id", "str_purchase_id");
        String str_invoice_no = sharedPreferences.getString("str_invoice_no", "str_invoice_no");
        String str_purchase_date = sharedPreferences.getString("str_purchase_date", "str_purchase_date");
        str_sp_supplier_id = sharedPreferences.getString("str_supplier_id", "str_supplier_id");
        String str_supplier = sharedPreferences.getString("str_supplier", "str_supplier");
        String str_supplier_address = sharedPreferences.getString("str_supplier_address", "str_supplier_address");
        String str_supplier_gst = sharedPreferences.getString("str_supplier_gst", "str_supplier_gst");
        String str_total_amt = sharedPreferences.getString("str_total_amt", "str_total_amt");

        edit_pur_date.setText("" + str_purchase_date);
        edit_pur_invoice_no.setText("" + str_invoice_no);
        edit_pur_invoice_no.setText("" + str_invoice_no);
        edit_supplier.setText("" + str_supplier);
        edit_pur_address.setText("" + str_supplier_address);
        edit_pur_gst.setText("" + str_supplier_gst);

        txt_edt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                spn_raw_material.setVisibility(View.VISIBLE);
                btn_update_rawmaterial.setVisibility(View.VISIBLE);

                edit_pur_qty.setFocusableInTouchMode(true);
                edit_pur_qty.setClickable(true);

                edit_pur_rate.setFocusableInTouchMode(true);
                edit_pur_rate.setClickable(true);

            }
        });

        spn_raw_material.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                System.out.println("Shop");

                t1 = (TextView) view;
                String str_state = t1.getText().toString();
                str_pur_raw_material_id = Arraylist_rawmaterial_id.get(position);
                str_pur_raw_material = Arraylist_rawmaterial_name.get(position);

                edit_product.setText(""+ str_pur_raw_material);

                edit_pur_qty.setText("");
                edit_pur_rate.setText("");
                edit_pur_value.setText("");

                System.out.println("RAW M ID : " + str_pur_raw_material_id);
                System.out.println("RAW M NAME : " + str_pur_raw_material);

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        edit_pur_rate.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {

                String str_qty = edit_pur_qty.getText().toString().trim();
                String str_rate = edit_pur_rate.getText().toString().trim();
                try {
                    if (str_qty.equals("")){
                        Snackbar.make(parent_view, "Please Enter a Quantity Before Entering the Rate", Snackbar.LENGTH_SHORT).show();
                    }else {

                        int_qty = Integer.parseInt(str_qty);
                    }
                    int_rate = Integer.parseInt(str_rate);

                    int_value =int_qty * int_rate ;

                    edit_pur_value.setText("" + int_value);
                }catch (Exception e) {

                }

            }
        });

        btn_update_rawmaterial.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                str_pur_date = edit_pur_date.getText().toString().trim();
                str_pur_invoice_no = edit_pur_invoice_no.getText().toString().trim();
                str_pur_supplier_addr = edit_pur_address.getText().toString().trim();
                str_pur_supplier_gst = edit_pur_gst.getText().toString().trim();
                str_pur_qty = edit_pur_qty.getText().toString().trim();
                str_pur_ratw = edit_pur_rate.getText().toString().trim();
                str_pur_value = edit_pur_value.getText().toString().trim();

                str_final_param = str_pur_raw_material_id + "-" + str_pur_qty + "-" + str_pur_ratw + "-" + str_pur_value;

                if (str_pur_date.equals("")){
                    Snackbar.make(parent_view, "Date Can't be Empty", Snackbar.LENGTH_SHORT).show();
                }else if (str_pur_invoice_no.equals("")){
                    Snackbar.make(parent_view, "Invoice No. Can't be Empty", Snackbar.LENGTH_SHORT).show();
                }else if (str_sp_supplier_id == null || str_sp_supplier_id.equals("")){
                    Snackbar.make(parent_view, "Supplier Can't be Empty", Snackbar.LENGTH_SHORT).show();
                }else if (str_pur_raw_material_id.equals("")){
                    Snackbar.make(parent_view, "Please Select Raw Material ", Snackbar.LENGTH_SHORT).show();
                }else if (str_final_param.equals("")){
                    Snackbar.make(parent_view, "All The Fields are Mantatory", Snackbar.LENGTH_SHORT).show();
                }else {

                    try {
                        dialog = new SpotsDialog(Activity_View_Purchase_Raw_Material.this);
                        dialog.show();
                        queue = Volley.newRequestQueue(Activity_View_Purchase_Raw_Material.this);
                        Action_Update_Purchase();
                    }catch (Exception e) {

                    }
                }

            }
        });

        try {
            dialog = new SpotsDialog(Activity_View_Purchase_Raw_Material.this);
            dialog.show();
            queue = Volley.newRequestQueue(Activity_View_Purchase_Raw_Material.this);
            Action_Get_Purchase_Info();
        }catch (Exception e) {

        }

    }

    /***************************
     * GET Purchase Info
     ***************************/

    public void Action_Get_Purchase_Info() {

        String tag_json_obj = "json_obj_req";
        System.out.println("CAME 1");
        final StringRequest request = new StringRequest(Request.Method.POST,
                Appconfig.url_edit_purchase, new Response.Listener<String>() {

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

                            String material_id = obj1.getString("material_id");
                            String material_name = obj1.getString("material_name");
                            String qty = obj1.getString("qty");
                            String price = obj1.getString("price");
                            String total_price = obj1.getString("total_price");

                            try {

                                edit_product.setText("" + material_name);
                                edit_pur_qty.setText("" + qty);
                                edit_pur_rate.setText("" + price);
                                edit_pur_value.setText("" + total_price);

                            } catch (Exception e) {

                            }

                        }

                        dialog.dismiss();

                        try {
                            dialog = new SpotsDialog(Activity_View_Purchase_Raw_Material.this);
                            dialog.show();
                            queue = Volley.newRequestQueue(Activity_View_Purchase_Raw_Material.this);
                            Action_Get_RawMaterial();
                        }catch (Exception e) {

                        }

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

                params.put("purchase_id", str_sp_purchase_id);

                return params;
            }

        };

        // Adding request to request queue
        queue.add(request);
    }

    /***************************
     * GET Raw Materia
     ***************************/

    public void Action_Get_RawMaterial() {

        String tag_json_obj = "json_obj_req";
        System.out.println("CAME 1");
        final StringRequest request = new StringRequest(Request.Method.POST,
                Appconfig.url_raw_material_list, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.d(TAG, response.toString());
                try {
                    JSONObject obj = new JSONObject(response);
                    int success = obj.getInt("status");

                    if (success == 1) {

                        JSONArray arr;

                        arr = obj.getJSONArray("data");

                        Arraylist_supplier_id.clear();
                        Arraylist_supplier_name.clear();

                        for (int i = 0; arr.length() > i; i++) {
                            JSONObject obj1 = arr.getJSONObject(i);

                            String raw_id = obj1.getString("rawmaterial_id");
                            String raw_name = obj1.getString("rawmaterial_name");

                            Arraylist_rawmaterial_id.add(raw_id);
                            Arraylist_rawmaterial_name.add(raw_name);

                            try {
                                spn_raw_material
                                        .setAdapter(new ArrayAdapter<String>(Activity_View_Purchase_Raw_Material.this,
                                                android.R.layout.simple_spinner_dropdown_item,
                                                Arraylist_rawmaterial_name));

                            } catch (Exception e) {

                            }

                        }

                    } else if (success == 0) {

                        Snackbar.make(parent_view, "No Raw Materials Data Found", Snackbar.LENGTH_SHORT).show();

                    }

                } catch (JSONException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {

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

    /********************************
     * Get Points FUNCTION
     *********************************/
    private void Action_Update_Purchase() {

        StringRequest request = new StringRequest(Request.Method.POST,
                Appconfig.url_update_purchase, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.d(TAG, response.toString());
                try {
                    JSONObject obj = new JSONObject(response);
                    int success = obj.getInt("status");

                    if (success == 1) {

                        dialog.dismiss();

                        new AlertDialog.Builder(Activity_View_Purchase_Raw_Material.this)
                                .setTitle("Aqua Bill")
                                .setMessage("Raw Material Purchase Updated Successfully !")
                                .setIcon(R.mipmap.ic_launcher)
                                .setPositiveButton("Ok",
                                        new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface alert_dialog,

                                                                int which) {
                                                // TODO Auto-generated method stub

                                                Intent i = new Intent(Activity_View_Purchase_Raw_Material.this, Activity_Purchase_Raw_Material.class);
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

                params.put("purchase_id", str_sp_purchase_id);
                params.put("user_id", str_dealer_id);
                params.put("purchase_date", str_pur_date);
                params.put("invoice_no", str_pur_invoice_no);
                params.put("supplier_id", str_sp_supplier_id);
                params.put("product_details", str_final_param);

                System.out.println("purchase_id " + str_sp_purchase_id);
                System.out.println("user_id " + str_dealer_id);
                System.out.println("str_pur_date " + str_pur_date);
                System.out.println("str_pur_invoice_no " + str_pur_invoice_no);
                System.out.println("str_pur_supplier_id " + str_sp_supplier_id);
                System.out.println("str_final_param " + str_final_param);

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

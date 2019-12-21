package com.banyan.aquabill.activity;

import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
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
import android.widget.Toast;

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

public class Activity_Add_Purchase extends AppCompatActivity {

    String TAG = "Products and Rates";
    private View parent_view;

    // Session Manager Class
    SessionManager session;
    private static long back_pressed;

    SpotsDialog dialog;
    public static RequestQueue queue;

    private Toolbar mToolbar;

    private String str_dealer_id;

    private EditText edit_pur_date, edit_pur_invoice_no, edit_pur_address, edit_pur_gst, edit_pur_qty,
            edit_pur_rate, edit_pur_value;
    private SearchableSpinner spn_supplier, spn_raw_material;
    TextView t1;

    private String str_pur_date, str_pur_invoice_no, str_pur_supplier_id, str_pur_supplier, str_pur_supplier_addr, str_pur_supplier_gst,
    str_pur_raw_material, str_pur_raw_material_id, str_pur_qty, str_pur_ratw, str_pur_value, str_final_param;

    private Button btn_add_rawmaterial;

    ArrayList<String> Arraylist_supplier_id = null;
    ArrayList<String> Arraylist_supplier_name = null;

    ArrayList<String> Arraylist_rawmaterial_id = null;
    ArrayList<String> Arraylist_rawmaterial_name = null;

    int int_qty, int_rate, int_value;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_raw_material_purchase);

        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        parent_view = findViewById(android.R.id.content);

        setSupportActionBar(mToolbar);
        getSupportActionBar().setTitle("Add Purchase");
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

        edit_pur_date = (EditText) findViewById(R.id.purchase_rawmat_edt_date);
        edit_pur_invoice_no = (EditText) findViewById(R.id.purchase_rawmat_edt_invoiceno);
        edit_pur_address = (EditText) findViewById(R.id.purchase_rawmat_edt_sup_address);
        edit_pur_gst = (EditText) findViewById(R.id.purchase_rawmat_edt_sup_gst);
        edit_pur_qty = (EditText) findViewById(R.id.purchase_rawmat_edt_sup_qty);
        edit_pur_rate = (EditText) findViewById(R.id.purchase_rawmat_edt_sup_rate);
        edit_pur_value = (EditText) findViewById(R.id.purchase_rawmat_edt_sup_value);

        spn_supplier = (SearchableSpinner) findViewById(R.id.purchase_rawmat_spn_supplier);
        spn_raw_material = (SearchableSpinner) findViewById(R.id.purchase_rawmat_spn_raw_material);

        btn_add_rawmaterial = (Button) findViewById(R.id.purchase_rawmat_btn_add_purchase);


        Arraylist_supplier_id = new ArrayList<String>();
        Arraylist_supplier_name = new ArrayList<String>();

        Arraylist_rawmaterial_id = new ArrayList<String>();
        Arraylist_rawmaterial_name = new ArrayList<String>();

        spn_supplier.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                System.out.println("Shop");

                t1 = (TextView) view;
                String str_state = t1.getText().toString();
                str_pur_supplier_id = Arraylist_supplier_id.get(position);
                str_pur_supplier = Arraylist_supplier_name.get(position);

                System.out.println("STATE ID : " + str_pur_supplier_id);
                System.out.println("STATE NAME : " + str_pur_supplier);

                try {
                    dialog = new SpotsDialog(Activity_Add_Purchase.this);
                    dialog.show();
                    queue = Volley.newRequestQueue(Activity_Add_Purchase.this);
                    Action_Supplier_Info();
                }catch (Exception e) {

                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

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

                System.out.println("RAW M ID : " + str_pur_raw_material_id);
                System.out.println("RAW M NAME : " + str_pur_raw_material);

                edit_pur_qty.setText("");
                edit_pur_rate.setText("");
                edit_pur_value.setText("");

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        edit_pur_date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Function_Select_Date();
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

        btn_add_rawmaterial.setOnClickListener(new View.OnClickListener() {
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
                    Snackbar.make(parent_view, "Please Select Date", Snackbar.LENGTH_SHORT).show();
                }else if (str_pur_invoice_no.equals("")){
                    Snackbar.make(parent_view, "Please Enter Invoice No", Snackbar.LENGTH_SHORT).show();
                }else if (str_pur_supplier_id == null || str_pur_supplier_id.equals("")){
                    Snackbar.make(parent_view, "Please select Supplier ", Snackbar.LENGTH_SHORT).show();
                }else if (str_pur_raw_material_id.equals("")){
                    Snackbar.make(parent_view, "Please Select Raw Material ", Snackbar.LENGTH_SHORT).show();
                }else if (str_final_param.equals("")){
                    Snackbar.make(parent_view, "All The Fields are Mantatory", Snackbar.LENGTH_SHORT).show();
                }else {

                    try {
                        dialog = new SpotsDialog(Activity_Add_Purchase.this);
                        dialog.show();
                        queue = Volley.newRequestQueue(Activity_Add_Purchase.this);
                        Action_Add_Purchase();
                    }catch (Exception e) {

                    }
                }
            }
        });

        try {
            dialog = new SpotsDialog(Activity_Add_Purchase.this);
            dialog.show();
            queue = Volley.newRequestQueue(Activity_Add_Purchase.this);
            Action_Get_Supplier();
        }catch (Exception e) {

        }

    }

    /***************************
     * GET Supplier
     ***************************/

    public void Action_Get_Supplier() {

        String tag_json_obj = "json_obj_req";
        System.out.println("CAME 1");
        final StringRequest request = new StringRequest(Request.Method.POST,
                Appconfig.url_supplier_list, new Response.Listener<String>() {

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

                            String supplier_id = obj1.getString("supplier_id");
                            String supplier_name = obj1.getString("supplier_name");

                            Arraylist_supplier_id.add(supplier_id);
                            Arraylist_supplier_name.add(supplier_name);

                            try {
                                spn_supplier
                                        .setAdapter(new ArrayAdapter<String>(Activity_Add_Purchase.this,
                                                android.R.layout.simple_spinner_dropdown_item,
                                                Arraylist_supplier_name));

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

                params.put("user_id", str_dealer_id);

                return params;
            }

        };

        // Adding request to request queue
        queue.add(request);
    }

    /***************************
     * GET Supplier Info
     ***************************/

    public void Action_Supplier_Info() {

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

                            String supplier_id = obj1.getString("supplier_id");
                            String supplier_name = obj1.getString("supplier_name");
                            String supplier_address = obj1.getString("address");
                            String supplier_gst = obj1.getString("gst");

                            edit_pur_address.setText("" + supplier_address);
                            edit_pur_gst.setText("" + supplier_gst);

                            dialog.dismiss();
                        }

                        dialog.dismiss();
                        try {
                            dialog = new SpotsDialog(Activity_Add_Purchase.this);
                            dialog.show();
                            queue = Volley.newRequestQueue(Activity_Add_Purchase.this);
                            Action_Get_RawMaterial();
                        }catch (Exception e) {

                        }
                    } else if (success == 0) {

                        dialog.dismiss();

                        Snackbar.make(parent_view, "No Data Found", Snackbar.LENGTH_SHORT).show();

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

                params.put("supplier_id", str_pur_supplier_id);

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
                                        .setAdapter(new ArrayAdapter<String>(Activity_Add_Purchase.this,
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
    private void Action_Add_Purchase() {

        StringRequest request = new StringRequest(Request.Method.POST,
                Appconfig.url_add_purchase, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.d(TAG, response.toString());
                try {
                    JSONObject obj = new JSONObject(response);
                    int success = obj.getInt("status");

                    if (success == 1) {

                        dialog.dismiss();

                        new AlertDialog.Builder(Activity_Add_Purchase.this)
                                .setTitle("Aqua Bill")
                                .setMessage("Raw Material Purchase Updated Successfully !")
                                .setIcon(R.mipmap.ic_launcher)
                                .setPositiveButton("Ok",
                                        new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface alert_dialog,

                                                                int which) {
                                                // TODO Auto-generated method stub

                                                Intent i = new Intent(Activity_Add_Purchase.this, Activity_Purchase_Raw_Material.class);
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
                params.put("purchase_date", str_pur_date);
                params.put("invoice_no", str_pur_invoice_no);
                params.put("supplier_id", str_pur_supplier_id);
                params.put("product_details", str_final_param);

                System.out.println("user_id " + str_dealer_id);
                System.out.println("str_pur_date " + str_pur_date);
                System.out.println("str_pur_invoice_no " + str_pur_invoice_no);
                System.out.println("str_pur_supplier_id " + str_pur_supplier_id);
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

    private void Function_Select_Date() {

        final Calendar c = Calendar.getInstance();
        int int_year = c.get(Calendar.YEAR);
        int int_month = c.get(Calendar.MONTH);
        int int_date = c.get(Calendar.DAY_OF_MONTH);


// Launch Date Picker Dialog
        DatePickerDialog dpd = new DatePickerDialog(
                Activity_Add_Purchase.this,
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
                        edit_pur_date.setText(str_date);

                    }
                }, int_year, int_month, int_date);
        dpd.setTitle("Select Call Date");
        dpd.show();

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

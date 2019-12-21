package com.banyan.aquabill.fragment;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.banyan.aquabill.R;
import com.banyan.aquabill.activity.Activity_Add_Customer;
import com.banyan.aquabill.activity.Activity_Assign_Product;
import com.banyan.aquabill.activity.Activity_Manage_Customer;
import com.banyan.aquabill.activity.Activity_Manage_Supplier;
import com.banyan.aquabill.activity.Activity_Purchase_Raw_Material;
import com.banyan.aquabill.activity.Activity_Purchase_Report;
import com.banyan.aquabill.activity.Activity_Raw_Mat_Live_Stock;
import com.banyan.aquabill.global.Appconfig;
import com.banyan.aquabill.global.SessionManager;
import com.toptoche.searchablespinnerlibrary.SearchableSpinner;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import dmax.dialog.SpotsDialog;

public class MasterFragment extends Fragment {

    // Session Manager Class
    SessionManager session;

    private SearchableSpinner spn_raw_material, spn_raw_material_damaged;
    private EditText edt_raw_material_qty, edt_raw_material_qty_damaged;
    private TextView txt_alert_raw_txt_errormsg, txt_alert_raw_txt_errormsg_damaged;

    // Manage Customers
    private LinearLayout linear_manage_customer, linear_assign_product;
    // Manage Materials
    private LinearLayout linear_manage_rawmaterial_opn_stock, linear_purchase_raw_material, linear_demaged_raw_material,
            linear_raw_mat_live_stock, linear_purchase_report;
    // Manage Suppliers
    private LinearLayout linear_manage_supplier;

    SpotsDialog dialog;
    public static RequestQueue queue;
    String TAG = "reg";

    TextView t1;

    ArrayList<String> Arraylist_raw_material_id = null;
    ArrayList<String> Arraylist_raw_material_name = null;
    ArrayList<String> Arraylist_raw_stock_qty = null;

    private String str_dealer_id;

    private String str_selected_raw_material_id, str_selected_raw_material_name, str_selected_raw_material_qty,
            str_selected_demaged_raw_material_id, str_selected_demaged_raw_material_name,
            str_selected_demaged_raw_material_qty, str_selected_raw_material_stock;

    private String str_alert_msg;

    private int flag = 0;

    private int int_rawmat_live_stock, int_rawmat_qty, int_rawmat_qty_damaged;

    public MasterFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_master, container, false);

        //Manage Customer
        linear_manage_customer = (LinearLayout) rootView.findViewById(R.id.frag_master_linear_customer_manage);
        linear_assign_product = (LinearLayout) rootView.findViewById(R.id.frag_master_linear_customer_assignproduct);
        //Manage Raw Material
        linear_manage_rawmaterial_opn_stock = (LinearLayout) rootView.findViewById(R.id.frag_master_linear_material_openingstock);
        linear_purchase_raw_material = (LinearLayout) rootView.findViewById(R.id.frag_master_linear_material_purchase_rawmaterial);
        linear_demaged_raw_material = (LinearLayout) rootView.findViewById(R.id.frag_master_linear_material_add_demage_raw);

        //Manage Supplier
        linear_manage_supplier = (LinearLayout) rootView.findViewById(R.id.frag_master_linear_supplier_manage);

        // Session Manager
        session = new SessionManager(getActivity());
        HashMap<String, String> user = session.getUserDetails();
        str_dealer_id = user.get(SessionManager.KEY_DEALER_ID);

        Arraylist_raw_material_id = new ArrayList<String>();
        Arraylist_raw_material_name = new ArrayList<String>();
        Arraylist_raw_stock_qty = new ArrayList<String>();


        /***************************
         * Organise Customer
         * ***************************/

        linear_manage_customer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent i = new Intent(getActivity(), Activity_Manage_Customer.class);
                startActivity(i);

            }
        });

        linear_assign_product.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent i = new Intent(getActivity(), Activity_Assign_Product.class);
                startActivity(i);

            }
        });

        /***************************
         * Organise Materials
         * ***************************/

        linear_manage_rawmaterial_opn_stock.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                str_alert_msg = "Add Raw Material Opening Stock";
                FunAlertRawMaterial(str_alert_msg);
            }
        });

        linear_purchase_raw_material.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent i = new Intent(getActivity(), Activity_Purchase_Raw_Material.class);
                startActivity(i);
            }
        });

        linear_demaged_raw_material.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                try {
                    dialog = new SpotsDialog(getActivity());
                    dialog.show();
                    queue = Volley.newRequestQueue(getActivity());
                    Action_Get_RawMaterial_Live_Stock();
                } catch (Exception e) {

                }
            }
        });

        /*linear_raw_mat_live_stock.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent i = new Intent(getActivity(), Activity_Raw_Mat_Live_Stock.class);
                startActivity(i);
            }
        });

        linear_purchase_report.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent i = new Intent(getActivity(), Activity_Purchase_Report.class);
                startActivity(i);
            }
        });*/

        /***************************
         * Organise Suppliers
         * ***************************/

        linear_manage_supplier.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent i = new Intent(getActivity(), Activity_Manage_Supplier.class);
                startActivity(i);

            }
        });


        try {
            dialog = new SpotsDialog(getActivity());
            dialog.show();
            queue = Volley.newRequestQueue(getActivity());
            Action_Get_RawMaterial();
        } catch (Exception e) {

        }

        // Inflate the layout for this fragment
        return rootView;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    /***************************
     * GET Raw Material
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

                        Arraylist_raw_material_id.clear();
                        Arraylist_raw_material_name.clear();

                        for (int i = 0; arr.length() > i; i++) {
                            JSONObject obj1 = arr.getJSONObject(i);

                            String state_id = obj1.getString("rawmaterial_id");
                            String state_name = obj1.getString("rawmaterial_name");

                            Arraylist_raw_material_id.add(state_id);
                            Arraylist_raw_material_name.add(state_name);

                            try {
                                spn_raw_material
                                        .setAdapter(new ArrayAdapter<String>(getActivity(),
                                                android.R.layout.simple_spinner_dropdown_item,
                                                Arraylist_raw_material_name));

                            } catch (Exception e) {

                            }

                        }

                        dialog.dismiss();
                    } else if (success == 0) {

                        dialog.dismiss();

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


    /**********************************
     * GET Raw Material Live Stock
     ***********************************/

    public void Action_Get_RawMaterial_Live_Stock() {

        String tag_json_obj = "json_obj_req";
        System.out.println("CAME 1");
        final StringRequest request = new StringRequest(Request.Method.POST,
                Appconfig.url_raw_mat_live_stock, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.d(TAG, response.toString());
                try {
                    JSONObject obj = new JSONObject(response);
                    int success = obj.getInt("status");

                    if (success == 1) {

                        JSONArray arr;

                        arr = obj.getJSONArray("data");

                        Arraylist_raw_material_id.clear();
                        Arraylist_raw_material_name.clear();

                        for (int i = 0; arr.length() > i; i++) {
                            JSONObject obj1 = arr.getJSONObject(i);

                            String state_id = obj1.getString("rawmaterial_id");
                            String state_name = obj1.getString("rawmaterial_name");
                            String stock_qty = obj1.getString("stock_qty");

                            Arraylist_raw_material_id.add(state_id);
                            Arraylist_raw_material_name.add(state_name);
                            Arraylist_raw_stock_qty.add(stock_qty);

                            try {
                                spn_raw_material_damaged
                                        .setAdapter(new ArrayAdapter<String>(getActivity(),
                                                android.R.layout.simple_spinner_dropdown_item,
                                                Arraylist_raw_material_name));

                            } catch (Exception e) {

                            }

                        }
                        str_alert_msg = "Add Demaged Raw Material";
                        FunAlertRawMaterial_Demaged(str_alert_msg);
                        dialog.dismiss();
                    } else if (success == 0) {

                        dialog.dismiss();

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

    /**********************************
     * Function Alert Add Opening Stock
     ***********************************/
    private void FunAlertRawMaterial(String str_msg) {

        LayoutInflater li = LayoutInflater.from(getActivity());
        View promptsView = li.inflate(R.layout.alert_dialog_rawmaterial_open_stock, null);

        AlertDialog alert_dialog = new AlertDialog.Builder(getActivity())
                .setView(promptsView)
                .setTitle("Add Raw Material")
                .setPositiveButton(android.R.string.ok, null) //Set to null. We override the onclick
                .setNegativeButton(android.R.string.cancel, null)
                .create();

        spn_raw_material = (SearchableSpinner) promptsView.findViewById(R.id.alert_supplier_spn_rawmaterial);
        edt_raw_material_qty = (EditText) promptsView.findViewById(R.id.alert_raw_edt_qty);
        txt_alert_raw_txt_errormsg = (TextView) promptsView.findViewById(R.id.alert_raw_txt_errormsg);

        try {
            spn_raw_material
                    .setAdapter(new ArrayAdapter<String>(getActivity(),
                            android.R.layout.simple_spinner_dropdown_item,
                            Arraylist_raw_material_name));

            spn_raw_material.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                    System.out.println("Countryyyyyyyyyyyy");

                    t1 = (TextView) view;
                    str_selected_raw_material_name = Arraylist_raw_material_name.get(position);
                    str_selected_raw_material_id = Arraylist_raw_material_id.get(position);
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });

            // spinner_currency.setSelection(2);
        } catch (Exception e) {

        }

        alert_dialog.setOnShowListener(new DialogInterface.OnShowListener() {

            @Override
            public void onShow(final DialogInterface alert_dialog) {

                Button button = ((AlertDialog) alert_dialog).getButton(AlertDialog.BUTTON_POSITIVE);
                button.setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View view) {
                        // TODO Do something

                        str_selected_raw_material_qty = edt_raw_material_qty.getText().toString().trim();

                        if (str_selected_raw_material_id == null) {
                            txt_alert_raw_txt_errormsg.setText("" + "Please Select a Raw Material");
                        } else if (str_selected_raw_material_qty.equals("")) {
                            txt_alert_raw_txt_errormsg.setText("" + "Please Select a Quantity");
                        } else {
                            try {
                                dialog = new SpotsDialog(getActivity());
                                dialog.show();
                                queue = Volley.newRequestQueue(getActivity());
                                Action_Add_RawMat_OpeningStock();
                                alert_dialog.dismiss();
                            } catch (Exception e) {

                            }
                        }

                    }
                });
            }
        });

        alert_dialog.show();
    }

    /********************************
     * Add Raw Material Opening Stock
     *********************************/
    private void Action_Add_RawMat_OpeningStock() {

        StringRequest request = new StringRequest(Request.Method.POST,
                Appconfig.url_add_opening_stock, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.d(TAG, response.toString());
                try {
                    JSONObject obj = new JSONObject(response);
                    int success = obj.getInt("status");

                    if (success == 1) {

                        dialog.dismiss();

                        new androidx.appcompat.app.AlertDialog.Builder(getActivity())
                                .setTitle("Aqua Bill")
                                .setMessage("Raw Material Opening Stock Updated Successfully !")
                                .setIcon(R.mipmap.ic_launcher)
                                .setPositiveButton("Ok",
                                        new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface alert_dialog,

                                                                int which) {
                                                // TODO Auto-generated method stub

                                                alert_dialog.dismiss();
                                            }

                                        }).show();

                    } else if (success == 0) {

                        Toast.makeText(getActivity(), "Opening Stock Can't Update", Toast.LENGTH_SHORT).show();

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
                Toast.makeText(getActivity(), "Error : " + error, Toast.LENGTH_SHORT).show();
            }
        }) {

            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();

                params.put("user_id", str_dealer_id);
                params.put("rawmaterial_id", str_selected_raw_material_id);
                params.put("qty", str_selected_raw_material_qty);

                System.out.println("user_id " + str_dealer_id);
                System.out.println("rawmaterial_id " + str_selected_raw_material_id);
                System.out.println("qty " + str_selected_raw_material_qty);

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

    /**********************************
     * Function Alert Add Demaged Stock
     ***********************************/
    private void FunAlertRawMaterial_Demaged(String str_msg) {

        LayoutInflater li = LayoutInflater.from(getActivity());
        View promptsView = li.inflate(R.layout.alert_dialog_rawmaterial_damaged, null);

        AlertDialog alert_dialog = new AlertDialog.Builder(getActivity())
                .setView(promptsView)
                .setTitle("Update Demaged Raw Material")
                .setPositiveButton(android.R.string.ok, null) //Set to null. We override the onclick
                .setNegativeButton(android.R.string.cancel, null)
                .create();

        spn_raw_material_damaged = (SearchableSpinner) promptsView.findViewById(R.id.alert_supplier_damaged_spn_rawmaterial);
        edt_raw_material_qty_damaged = (EditText) promptsView.findViewById(R.id.alert_raw_damaged_edt_qty);
        txt_alert_raw_txt_errormsg_damaged = (TextView) promptsView.findViewById(R.id.alert_raw_damaged_txt_errormsg);

        try {
            spn_raw_material_damaged
                    .setAdapter(new ArrayAdapter<String>(getActivity(),
                            android.R.layout.simple_spinner_dropdown_item,
                            Arraylist_raw_material_name));

            spn_raw_material_damaged.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {


                    t1 = (TextView) view;
                    str_selected_demaged_raw_material_name = Arraylist_raw_material_name.get(position);
                    str_selected_demaged_raw_material_id = Arraylist_raw_material_id.get(position);
                    str_selected_raw_material_stock = Arraylist_raw_stock_qty.get(position);

                    System.out.println("STOCK : " + str_selected_raw_material_stock);
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });

            // spinner_currency.setSelection(2);
        } catch (Exception e) {

        }

        alert_dialog.setOnShowListener(new DialogInterface.OnShowListener() {

            @Override
            public void onShow(final DialogInterface alert_dialog) {

                Button button = ((AlertDialog) alert_dialog).getButton(AlertDialog.BUTTON_POSITIVE);
                button.setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View view) {
                        // TODO Do something

                        str_selected_demaged_raw_material_qty = edt_raw_material_qty_damaged.getText().toString().trim();


                        if (str_selected_demaged_raw_material_id == null) {
                            txt_alert_raw_txt_errormsg_damaged.setText("" + "Please Select a Raw Material");
                        }else if (str_selected_demaged_raw_material_qty.equals("")) {
                            txt_alert_raw_txt_errormsg_damaged.setText("" + "Please Entee Raw Material Quantity");
                        }else {
                            if (!str_selected_raw_material_stock.equals("")){
                                int_rawmat_live_stock = Integer.parseInt(str_selected_raw_material_stock);
                                int_rawmat_qty_damaged = Integer.parseInt(str_selected_demaged_raw_material_qty);
                                System.out.println("STOCK: " +int_rawmat_live_stock +" QTY : " +  int_rawmat_qty_damaged);
                                if (int_rawmat_qty_damaged <= int_rawmat_live_stock){

                                    try {
                                        dialog = new SpotsDialog(getActivity());
                                        dialog.show();
                                        queue = Volley.newRequestQueue(getActivity());
                                        Action_Add_Demaged_RawMat();
                                        alert_dialog.dismiss();

                                    } catch (Exception e) {

                                    }
                                }else {
                                    txt_alert_raw_txt_errormsg_damaged.setText("Please Enter Valid Quantity");
                                }

                            }else {
                                txt_alert_raw_txt_errormsg_damaged.setText("" + "Empty Stock! You Can't Add Damaged Raw Material");
                            }
                        }

                    }
                });
            }
        });

        alert_dialog.show();
    }

    /********************************
     * Add Demaged Raw MAterial
     *********************************/
    private void Action_Add_Demaged_RawMat() {

        StringRequest request = new StringRequest(Request.Method.POST,
                Appconfig.url_add_demaged_stock, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.d(TAG, response.toString());
                try {
                    JSONObject obj = new JSONObject(response);
                    int success = obj.getInt("status");

                    if (success == 1) {

                        dialog.dismiss();

                        new androidx.appcompat.app.AlertDialog.Builder(getActivity())
                                .setTitle("Aqua Bill")
                                .setMessage("Demaged Raw Material Updated Successfully !")
                                .setIcon(R.mipmap.ic_launcher)
                                .setPositiveButton("Ok",
                                        new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface alert_dialog,

                                                                int which) {
                                                // TODO Auto-generated method stub

                                                alert_dialog.dismiss();
                                            }

                                        }).show();

                    } else if (success == 0) {

                        Toast.makeText(getActivity(), "Demagd Stock Can't Update", Toast.LENGTH_SHORT).show();

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
                Toast.makeText(getActivity(), "Error : " + error, Toast.LENGTH_SHORT).show();
            }
        }) {

            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();

                params.put("user_id", str_dealer_id);
                params.put("rawmaterial_id", str_selected_demaged_raw_material_id);
                params.put("qty", str_selected_demaged_raw_material_qty);

                System.out.println("user_id " + str_dealer_id);
                System.out.println("rawmaterial_id " + str_selected_demaged_raw_material_id);
                System.out.println("qty " + str_selected_demaged_raw_material_qty);

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

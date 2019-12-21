package com.banyan.aquabill.fragment;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.aromajoin.actionsheet.ActionSheet;
import com.aromajoin.actionsheet.OnActionListener;
import com.banyan.aquabill.R;
import com.banyan.aquabill.activity.Activity_Add_Invoice;
import com.banyan.aquabill.activity.Activity_Add_Received_Can;
import com.banyan.aquabill.activity.Activity_Add_Received_Cash;
import com.banyan.aquabill.activity.Activity_Products_and_Rate;
import com.banyan.aquabill.activity.Activity_Raw_Mat_Live_Stock;
import com.banyan.aquabill.activity.Activity_Report_Customer_Outstanding;
import com.banyan.aquabill.activity.Activity_Report_Receipt;
import com.banyan.aquabill.activity.Activity_Report_Sales;
import com.banyan.aquabill.activity.MainActivity;
import com.banyan.aquabill.adapter.Adapter_Dashboard_List;
import com.banyan.aquabill.adapter.Adapter_Manage_Dashboard;
import com.banyan.aquabill.adapter.Adapter_ProductsAndRate;
import com.banyan.aquabill.global.Appconfig;
import com.banyan.aquabill.global.SessionManager;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.toptoche.searchablespinnerlibrary.SearchableSpinner;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import dmax.dialog.SpotsDialog;

public class HomeFragment extends Fragment {

    // Session Manager Class
    SessionManager session;
    private View parent_view;
    private Context context;

    private String str_dealer_id;
    private SearchableSpinner spn_customer;
    private TextView t1;
    private String str_select_customer_id, str_select_customer;
    private CardView card_sales, card_cash_receipt, card_differece, card_outstanding, card_create_invoice, card_receive_cash,
            card_receive_can, card_locate_customer, card_rm_live_Stock;

    private TextView txt_sales, txt_cash_receipt, txt_differece, txt_out_standing;
    private ListView recyclerView_list;

    SpotsDialog dialog;
    public static RequestQueue queue;
    String TAG = "Dashboard";

    ArrayList<String> Arraylist_customer_id = null;
    ArrayList<String> Arraylist_customer_name = null;

    public Adapter_Manage_Dashboard adapter;
    static ArrayList<HashMap<String, String>> dashboard_list;

    public static final String TAG_PDU_ID = "pdu_id";
    public static final String TAG_PDU_NAME = "pdu_name";
    public static final String TAG_BALANCE = "balance";

    private String str_cus_id;

    public HomeFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_home, container, false);

        parent_view = rootView.findViewById(android.R.id.content);

        // Session Manager
        session = new SessionManager(getActivity());
        HashMap<String, String> user = session.getUserDetails();
        str_dealer_id = user.get(SessionManager.KEY_DEALER_ID);

        dashboard_list = new ArrayList<HashMap<String, String>>();

        Arraylist_customer_id = new ArrayList<String>();
        Arraylist_customer_name = new ArrayList<String>();

        spn_customer = (SearchableSpinner) rootView.findViewById(R.id.dashboard_spn_customer);

        txt_sales = (TextView) rootView.findViewById(R.id.dashboard_txt_sales);
        txt_cash_receipt = (TextView) rootView.findViewById(R.id.dashboard_txt_cash_receipt);
        txt_differece = (TextView) rootView.findViewById(R.id.dashboard_txt_differece);
        txt_out_standing = (TextView) rootView.findViewById(R.id.dashboard_txt_outstanding);

        card_sales = (CardView) rootView.findViewById(R.id.home_card_view_today_sales);
        card_cash_receipt = (CardView) rootView.findViewById(R.id.home_card_view_today_cash_received);
        card_differece = (CardView) rootView.findViewById(R.id.home_card_view_difference);
        card_outstanding = (CardView) rootView.findViewById(R.id.home_card_view_outstanding);

        card_create_invoice = (CardView) rootView.findViewById(R.id.home_card_view_create_invoice);
        card_receive_cash = (CardView) rootView.findViewById(R.id.home_card_view_receive_cash);
        card_receive_can = (CardView) rootView.findViewById(R.id.home_card_view_receive_can);
        card_locate_customer = (CardView) rootView.findViewById(R.id.home_card_view_locate_customer);
        card_rm_live_Stock = (CardView) rootView.findViewById(R.id.home_card_view_rm_live_stock);

        recyclerView_list = (ListView) rootView.findViewById(R.id.dashboard_list);

        card_sales.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent i = new Intent(getActivity(), Activity_Report_Sales.class);
                startActivity(i);
            }
        });

        card_cash_receipt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent i = new Intent(getActivity(), Activity_Report_Receipt.class);
                startActivity(i);
            }
        });


        card_outstanding.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent i = new Intent(getActivity(), Activity_Report_Customer_Outstanding.class);
                startActivity(i);

            }
        });

        card_create_invoice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent i = new Intent(getActivity(), Activity_Add_Invoice.class);
                startActivity(i);

            }
        });

        card_receive_cash.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent i = new Intent(getActivity(), Activity_Add_Received_Cash.class);
                startActivity(i);

            }
        });

        card_receive_can.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent i = new Intent(getActivity(), Activity_Add_Received_Can.class);
                startActivity(i);

            }
        });

        card_locate_customer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


            }
        });

        card_rm_live_Stock.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent i = new Intent(getActivity(), Activity_Raw_Mat_Live_Stock.class);
                startActivity(i);

            }
        });


        spn_customer.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                System.out.println("Shop");

                t1 = (TextView) view;
                String str_state = t1.getText().toString();
                str_select_customer_id = Arraylist_customer_id.get(position);
                str_select_customer = Arraylist_customer_name.get(position);

                System.out.println("CUST ID : " + str_select_customer_id);
                System.out.println("CUST NAME : " + str_select_customer);

                try {
                    dialog = new SpotsDialog(getActivity());
                    dialog.show();
                    queue = Volley.newRequestQueue(getActivity());
                    Action_Get_Dashboard_Info();
                } catch (Exception e) {

                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        System.out.println("IDDDDDDDDDD : " + str_cus_id);

        if (str_cus_id != null && !str_cus_id.isEmpty() && !str_cus_id.equals("null")) {
            str_cus_id = str_select_customer_id;
        } else {
            str_cus_id = "0";
        }

        try {
            dialog = new SpotsDialog(getActivity());
            dialog.show();
            queue = Volley.newRequestQueue(getActivity());
            Action_Get_Customers();
            Action_Get_Dashboard_Info();
            Get_Report();

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
     * GET Customers
     ***************************/
    public void Action_Get_Customers() {

        String tag_json_obj = "json_obj_req";
        System.out.println("CAME 1");
        final StringRequest request = new StringRequest(Request.Method.POST,
                Appconfig.url_list_customer, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.d(TAG, response.toString());
                try {
                    JSONObject obj = new JSONObject(response);
                    int success = obj.getInt("status");

                    if (success == 1) {

                        JSONArray arr;

                        arr = obj.getJSONArray("data");

                        Arraylist_customer_id.clear();
                        Arraylist_customer_name.clear();

                        for (int i = 0; arr.length() > i; i++) {
                            JSONObject obj1 = arr.getJSONObject(i);

                            String customer_id = obj1.getString("customer_id");
                            String customer_name = obj1.getString("customer_name");

                            Arraylist_customer_id.add(customer_id);
                            Arraylist_customer_name.add(customer_name);

                            try {
                                spn_customer
                                        .setAdapter(new ArrayAdapter<String>(getActivity(),
                                                android.R.layout.simple_spinner_dropdown_item,
                                                Arraylist_customer_name));

                            } catch (Exception e) {

                            }
                        }
                        dialog.dismiss();

                    } else if (success == 0) {

                        dialog.dismiss();

                        Toast.makeText(getActivity(), "No Customer Data Found", Toast.LENGTH_SHORT).show();

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

                // Toast.makeText(getActivity(), "ERROR : " + error, Toast.LENGTH_SHORT).show();
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
     * GET Dashboard Info
     ***************************/
    public void Action_Get_Dashboard_Info() {

        String tag_json_obj = "json_obj_req";
        System.out.println("CAME 1");
        final StringRequest request = new StringRequest(Request.Method.POST,
                Appconfig.url_dashboard, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.d(TAG, response.toString());
                try {
                    JSONObject obj = new JSONObject(response);
                    int success = obj.getInt("status");

                    if (success == 1) {

                        JSONObject obj1;
                        obj1 = obj.getJSONObject("data");

                        String str_sales = obj1.getString("sale_amount");
                        String str_receipt_amount = obj1.getString("receipt_amount");
                        String str_difference = obj1.getString("difference");
                        String str_outstanding_amount = obj1.getString("outstanding_amount");

                        txt_sales.setText("" + str_sales);
                        txt_cash_receipt.setText("" + str_receipt_amount);
                        txt_differece.setText("" + str_difference);
                        txt_out_standing.setText("" + str_outstanding_amount);

                        dialog.dismiss();
                    } else if (success == 0) {

                        dialog.dismiss();

                        Snackbar.make(parent_view, "No Customer Data Found", Snackbar.LENGTH_SHORT).show();

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

                Toast.makeText(getActivity(), "Error : " + error, Toast.LENGTH_SHORT).show();
            }
        }) {

            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();

                params.put("user_id", str_dealer_id);
                params.put("customer_id", str_select_customer_id);

                System.out.println("USER : " + str_dealer_id);
                System.out.println("CUSTOMER : " + str_select_customer_id);

                return checkParams(params);
            }

            private Map<String, String> checkParams(Map<String, String> map) {
                Iterator<Map.Entry<String, String>> it = map.entrySet().iterator();
                while (it.hasNext()) {
                    Map.Entry<String, String> pairs = (Map.Entry<String, String>) it.next();
                    if (pairs.getValue() == null) {
                        map.put(pairs.getKey(), "0");
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
     * Get Report
     *********************************/

    public void Get_Report() {

        StringRequest request = new StringRequest(Request.Method.POST,
                Appconfig.url_dashboard_dealer_pendingpdu, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.d(TAG, response.toString());
                try {
                    JSONObject obj = new JSONObject(response);
                    int success = obj.getInt("status");

                    if (success == 1) {

                        JSONArray arr;

                        arr = obj.getJSONArray("data");

                        dashboard_list.clear();

                        for (int i = 0; arr.length() > i; i++) {
                            JSONObject obj1 = arr.getJSONObject(i);

                            String pdu_id = obj1.getString(TAG_PDU_ID);
                            String pdu_name = obj1.getString(TAG_PDU_NAME);
                            String balance = obj1.getString(TAG_BALANCE);

                            // creating new HashMap
                            HashMap<String, String> map = new HashMap<String, String>();

                            map.put(TAG_PDU_ID, pdu_id);
                            map.put(TAG_PDU_NAME, pdu_name);
                            map.put(TAG_BALANCE, balance);

                            dashboard_list.add(map);

                            adapter = new Adapter_Manage_Dashboard(getActivity(),
                                    dashboard_list);
                            recyclerView_list.setAdapter(adapter);

                        }

                    } else if (success == 0) {

                        dialog.dismiss();
                        Toast.makeText(getActivity(), "No Data Found", Toast.LENGTH_SHORT).show();

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

            }
        }) {

            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();

                params.put("user_id", str_dealer_id);

                System.out.println("user_id : " + str_dealer_id);

                return params;
            }

        };

        // Adding request to request queue
        queue.add(request);
    }


}

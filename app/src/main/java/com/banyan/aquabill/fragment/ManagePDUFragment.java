package com.banyan.aquabill.fragment;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.banyan.aquabill.R;
import com.banyan.aquabill.activity.Activity_Products_and_Rate;
import com.banyan.aquabill.activity.Activity_Profile;
import com.banyan.aquabill.activity.Activity_TestReport;
import com.banyan.aquabill.global.Appconfig;
import com.banyan.aquabill.global.SessionManager;
import com.google.android.material.snackbar.Snackbar;
import com.toptoche.searchablespinnerlibrary.SearchableSpinner;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import dmax.dialog.SpotsDialog;

public class ManagePDUFragment extends Fragment {

    // Session Manager Class
    SessionManager session;
    private View parent_view;

    private String str_dealer_id;
    private SearchableSpinner spn_pdu;
    private TextView t1;
    private String str_select_pdu_id, str_select_pdu;
    private String str_pdu_name, str_bis_regno, str_fssai_no, str_gst_no, str_ssi_no, str_address, str_contact_person,
            str_contact_no, str_email, str_brand;

    private TextView txt_pdu_name, txt_bis_regno, txt_fssai_no, txt_gst_no, txt_ssi_no, txt_address, txt_contact_person,
            txt_contact_no, txt_email, txt_brand, txt_productandrate, txt_test_report;

    SpotsDialog dialog;
    public static RequestQueue queue;
    String TAG = "Manage PDU";


    ArrayList<String> Arraylist_pdu_id = null;
    ArrayList<String> Arraylist_pdu_name = null;

    public ManagePDUFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_manage_pdu, container, false);

        parent_view = rootView.findViewById(android.R.id.content);

        // Session Manager
        session = new SessionManager(getActivity());
        HashMap<String, String> user = session.getUserDetails();
        str_dealer_id = user.get(SessionManager.KEY_DEALER_ID);

        spn_pdu = (SearchableSpinner) rootView.findViewById(R.id.manage_pdu_spn_pdu);

        txt_pdu_name = (TextView) rootView.findViewById(R.id.manage_pdu_txt_pdu_name);
        txt_bis_regno = (TextView) rootView.findViewById(R.id.manage_pdu_txt_bis_regno);
        txt_fssai_no = (TextView) rootView.findViewById(R.id.manage_pdu_txt_fssai_no);
        txt_gst_no = (TextView) rootView.findViewById(R.id.manage_pdu_txt_gst_no);
        txt_ssi_no = (TextView) rootView.findViewById(R.id.manage_pdu_txt_ssi_no);
        txt_address = (TextView) rootView.findViewById(R.id.manage_pdu_txt_address);
        txt_contact_person = (TextView) rootView.findViewById(R.id.manage_pdu_txt_contact_person);
        txt_contact_no = (TextView) rootView.findViewById(R.id.manage_pdu_txt_contact_no);
        txt_email = (TextView) rootView.findViewById(R.id.manage_pdu_txt_email);
        txt_brand = (TextView) rootView.findViewById(R.id.manage_pdu_txt_brand);
        txt_productandrate = (TextView) rootView.findViewById(R.id.manage_pdu_txt_product_rate);
        txt_test_report = (TextView) rootView.findViewById(R.id.manage_pdu_txt_testreport);

        Arraylist_pdu_id = new ArrayList<String>();
        Arraylist_pdu_name = new ArrayList<String>();

        spn_pdu.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                System.out.println("Shop");

                t1 = (TextView) view;
                String str_state = t1.getText().toString();
                str_select_pdu_id = Arraylist_pdu_id.get(position);
                str_select_pdu = Arraylist_pdu_name.get(position);

                System.out.println("PDU ID : " + str_select_pdu_id);
                System.out.println("PDU NAME : " + str_select_pdu);

                try {
                    dialog = new SpotsDialog(getActivity());
                    dialog.show();
                    queue = Volley.newRequestQueue(getActivity());
                    Action_Get_PDU_Profile();
                }catch (Exception e) {

                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        txt_productandrate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (str_select_pdu_id == null || str_select_pdu_id.equals("")) {
                    Toast.makeText(getActivity(), "Please select a PDU", Toast.LENGTH_SHORT).show();
                }else {

                    SharedPreferences sharedPreferences = PreferenceManager
                            .getDefaultSharedPreferences(getActivity());
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putString("selected_pdu_id", str_select_pdu_id);
                    editor.commit();

                    Intent i = new Intent(getActivity(), Activity_Products_and_Rate.class);
                    startActivity(i);
                }
            }
        });

        txt_test_report.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (str_select_pdu_id == null || str_select_pdu_id.equals("")) {
                    Toast.makeText(getActivity(), "Please select a PDU", Toast.LENGTH_SHORT).show();
                }else {

                    SharedPreferences sharedPreferences = PreferenceManager
                            .getDefaultSharedPreferences(getActivity());
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putString("selected_pdu_id", str_select_pdu_id);
                    editor.commit();

                    Intent i = new Intent(getActivity(), Activity_TestReport.class);
                    startActivity(i);
                }
            }
        });

        try {
            dialog = new SpotsDialog(getActivity());
            dialog.show();
            queue = Volley.newRequestQueue(getActivity());
            Action_Get_PDU();
        }catch (Exception e) {

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

    /********************************
     * Get PDU
     *********************************/
    private void Action_Get_PDU() {

        StringRequest request = new StringRequest(Request.Method.POST,
                Appconfig.url_dealerpdu, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.d(TAG, response.toString());
                try {
                    JSONObject obj = new JSONObject(response);
                    int success = obj.getInt("status");

                    if (success == 1) {

                        JSONArray arr;

                        arr = obj.getJSONArray("data");

                        Arraylist_pdu_id.clear();
                        Arraylist_pdu_name.clear();

                        for (int i = 0; arr.length() > i; i++) {
                            JSONObject obj1 = arr.getJSONObject(i);

                            String pdu_id = obj1.getString("pdu_id");
                            String pdu_name = obj1.getString("pdu_name");

                            Arraylist_pdu_id.add(pdu_id);
                            Arraylist_pdu_name.add(pdu_name);

                            try {
                                spn_pdu
                                        .setAdapter(new ArrayAdapter<String>(getActivity(),
                                                android.R.layout.simple_spinner_dropdown_item,
                                                Arraylist_pdu_name));

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

    /********************************
     * Get PDU INFO
     *********************************/
    private void Action_Get_PDU_Profile() {

        StringRequest request = new StringRequest(Request.Method.POST,
                Appconfig.url_pdudetails, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.d(TAG, response.toString());
                try {
                    JSONObject obj = new JSONObject(response);
                    int success = obj.getInt("status");

                    if (success == 1) {

                        JSONObject arr;

                        arr = obj.getJSONObject("data");

                        str_pdu_name = arr.getString("pdu_name");
                        str_bis_regno = arr.getString("bis_regno");
                        str_fssai_no = arr.getString("fssai_no");
                        str_gst_no = arr.getString("gst_no");
                        str_ssi_no = arr.getString("ssi_no");
                        str_address = arr.getString("address");
                        str_contact_person = arr.getString("contact_person");
                        str_contact_no = arr.getString("contact_no");
                        str_email = arr.getString("email");
                        str_brand = arr.getString("brand");

                        SetText();

                    }else if (success == 0){

                        Toast.makeText(getActivity(), "No Data Found", Toast.LENGTH_SHORT).show();

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

                params.put("pdu_id", str_select_pdu_id);

                System.out.println("pdu_id " + str_select_pdu_id);

                return params;
            }
        };
        // Adding request to request queue
        queue.add(request);
    }

    private void SetText() {

        txt_productandrate.setVisibility(View.VISIBLE);
        txt_test_report.setVisibility(View.VISIBLE);
        txt_productandrate.setText("Click to View");
        txt_test_report.setText("Click to View");

        txt_pdu_name.setText("" + str_pdu_name);
        txt_bis_regno.setText("" + str_bis_regno);
        txt_fssai_no.setText("" + str_fssai_no);
        txt_gst_no.setText("" + str_gst_no);
        txt_ssi_no.setText("" + str_ssi_no);
        txt_address.setText("" + str_address);
        txt_contact_person.setText("" + str_contact_person);
        txt_contact_no.setText("" + str_contact_no);
        txt_email.setText("" + str_email);
        txt_brand.setText("" + str_brand);
    }
}

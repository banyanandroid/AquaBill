package com.banyan.aquabill.fragment;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
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
import com.banyan.aquabill.activity.Activity_Assign_Product;
import com.banyan.aquabill.activity.Activity_Manage_Customer;
import com.banyan.aquabill.activity.Activity_Manage_Supplier;
import com.banyan.aquabill.activity.Activity_Purchase_Raw_Material;
import com.banyan.aquabill.activity.Activity_Purchase_Report;
import com.banyan.aquabill.activity.Activity_Raw_Mat_Live_Stock;
import com.banyan.aquabill.activity.Activity_Report_Cust_Empty_Can_Report;
import com.banyan.aquabill.activity.Activity_Report_Customer_Outstanding;
import com.banyan.aquabill.activity.Activity_Report_Empty_Can;
import com.banyan.aquabill.activity.Activity_Report_Receipt;
import com.banyan.aquabill.activity.Activity_Report_Sales;
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

public class ReportsFragment extends Fragment {

    // Session Manager Class
    SessionManager session;

    private LinearLayout linear_sales_report, linear_receipt_report, linear_empty_stock_report,
            linear_outstanding, linear_cust_empty_can, linear_rm_report, linear_rm_live_stock;

    private String str_dealer_id;


    public ReportsFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_reports, container, false);

        //Manage Customer
        linear_sales_report = (LinearLayout) rootView.findViewById(R.id.frag_report_linear_sales_report);
        linear_receipt_report = (LinearLayout) rootView.findViewById(R.id.frag_report_linear_receipt_report);
        linear_empty_stock_report = (LinearLayout) rootView.findViewById(R.id.frag_report_linear_empty_Can_Stock);
        linear_outstanding = (LinearLayout) rootView.findViewById(R.id.frag_report_linear_customer_outstanding);
        linear_cust_empty_can = (LinearLayout) rootView.findViewById(R.id.frag_report_linear_cust_empty_can);
        linear_rm_report = (LinearLayout) rootView.findViewById(R.id.frag_report_linear_rm_report);
        linear_rm_live_stock = (LinearLayout) rootView.findViewById(R.id.frag_report_linear_rm_live_stock);

        // Session Manager
        session = new SessionManager(getActivity());
        HashMap<String, String> user = session.getUserDetails();
        str_dealer_id = user.get(SessionManager.KEY_DEALER_ID);


        /***************************
         * Organise Customer
         * ***************************/

        linear_sales_report.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent i = new Intent(getActivity(), Activity_Report_Sales.class);
                startActivity(i);

            }
        });

        linear_receipt_report.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent i = new Intent(getActivity(), Activity_Report_Receipt.class);
                startActivity(i);
            }
        });

        linear_empty_stock_report.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent i = new Intent(getActivity(), Activity_Report_Empty_Can.class);
                startActivity(i);
            }
        });

        linear_outstanding.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent i = new Intent(getActivity(), Activity_Report_Customer_Outstanding.class);
                startActivity(i);
            }
        });

        linear_cust_empty_can.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent i = new Intent(getActivity(), Activity_Report_Cust_Empty_Can_Report.class);
                startActivity(i);
            }
        });

        linear_rm_report.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent i = new Intent(getActivity(), Activity_Purchase_Report.class);
                startActivity(i);
            }
        });

        linear_rm_live_stock.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent i = new Intent(getActivity(), Activity_Raw_Mat_Live_Stock.class);
                startActivity(i);
            }
        });


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

}

package com.banyan.aquabill.fragment;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import androidx.fragment.app.Fragment;

import com.banyan.aquabill.R;
import com.banyan.aquabill.activity.Activity_Manage_Customer;
import com.banyan.aquabill.activity.Activity_Manage_Invoice;
import com.banyan.aquabill.activity.Activity_Manage_Raw_MaterialInvoice;
import com.banyan.aquabill.activity.Activity_Manage_Received_Can;
import com.banyan.aquabill.activity.Activity_Manage_Received_Cash;

public class GenerateInvoiceFragment extends Fragment {

    private LinearLayout linear_manage_invoice, linear_raw_mat_invoice, linear_receive_cash, linear_receive_can;

    public GenerateInvoiceFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_generate_invoice, container, false);

        linear_manage_invoice = (LinearLayout) rootView.findViewById(R.id.frag_invoice_linear_normal);
        linear_raw_mat_invoice = (LinearLayout) rootView.findViewById(R.id.frag_invoice_linear_raw);
        linear_receive_cash = (LinearLayout) rootView.findViewById(R.id.frag_invoice_linear_receive_amount);
        linear_receive_can = (LinearLayout) rootView.findViewById(R.id.frag_invoice_linear_receive_can);

        linear_manage_invoice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent i = new Intent(getActivity(), Activity_Manage_Invoice.class);
                startActivity(i);

            }
        });

        linear_raw_mat_invoice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent i = new Intent(getActivity(), Activity_Manage_Raw_MaterialInvoice.class);
                startActivity(i);

            }
        });

        linear_receive_cash.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent i = new Intent(getActivity(), Activity_Manage_Received_Cash.class);
                startActivity(i);

            }
        });

        linear_receive_can.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent i = new Intent(getActivity(), Activity_Manage_Received_Can.class);
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

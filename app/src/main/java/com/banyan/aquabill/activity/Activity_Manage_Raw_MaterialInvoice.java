package com.banyan.aquabill.activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.banyan.aquabill.R;
import com.banyan.aquabill.adapter.Adapter_Manage_Invoice;
import com.banyan.aquabill.global.Appconfig;
import com.banyan.aquabill.global.SessionManager;
import com.github.fabtransitionactivity.SheetLayout;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import dmax.dialog.SpotsDialog;

public class Activity_Manage_Raw_MaterialInvoice extends AppCompatActivity implements SheetLayout.OnFabAnimationEndListener, SwipeRefreshLayout.OnRefreshListener{

    String TAG = "Products and Rates";
    private View parent_view;

    // Session Manager Class
    SessionManager session;
    private static long back_pressed;

    SpotsDialog dialog;
    public static RequestQueue queue;

    private Toolbar mToolbar;

    private SheetLayout mSheetLayout;
    private FloatingActionButton mFab;

    private ListView List;
    private SwipeRefreshLayout swipeRefreshLayout;

    private static final int REQUEST_CODE = 1;

    static ArrayList<HashMap<String, String>> Invoice_list;
    HashMap<String, String> params = new HashMap<String, String>();

    public static final String TAG_INVOICE_NO = "invoice_no";
    public static final String TAG_DEALER_NAME = "dealer_name";
    public static final String TAG_CUSTOMER_NAME = "customer_name";
    public static final String TAG_DATE = "date";
    public static final String TAG_TIME = "time";
    public static final String TAG_BRAND_NAME = "brand_name";
    public static final String TAG_PRODUCT_NAME = "product_name";
    public static final String TAG_QTY = "quantity";
    public static final String TAG_TAX = "tax";
    public static final String TAG_TAXABLE_VALUE = "taxable_value";
    public static final String TAG_INVOICE_AMT = "invoice_amount";
    public static final String TAG_RECEIVED_AMT = "received_amount";
    public static final String TAG_BALANCE_AMT = "balance_amount";
    public static final String TAG_CHEQUE_NO = "chequeno";
    public static final String TAG_BANK_NO = "bankname";

    private Adapter_Manage_Invoice adapter;

    String str_dealer_id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_customer);

        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        parent_view = findViewById(android.R.id.content);

        setSupportActionBar(mToolbar);
        getSupportActionBar().setTitle("Manage Invoice");
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        mToolbar.setNavigationIcon(getResources().getDrawable(R.drawable.ic_action_back));
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent i = new Intent(getApplicationContext(), MainActivity.class);
                i.putExtra("manage_class","invoice");
                startActivity(i);
                finish();
            }
        });

        session = new SessionManager(getApplicationContext());
        HashMap<String, String> user = session.getUserDetails();
        str_dealer_id = user.get(SessionManager.KEY_DEALER_ID);

        mFab = (FloatingActionButton) findViewById(R.id.fab_add_task);
        mSheetLayout = (SheetLayout) findViewById(R.id.bottom_sheet);
        List = (ListView) findViewById(R.id.alloted_comp_listView);
        swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.alloted_comp_swipe_refresh_layout);

        Invoice_list = new ArrayList<HashMap<String, String>>();

        mSheetLayout.setFab(mFab);
        mSheetLayout.setFabAnimationEndListener(this);

        mFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mSheetLayout.expandFab();
            }
        });

        swipeRefreshLayout.setOnRefreshListener(this);

        swipeRefreshLayout.post(new Runnable() {
                                    @Override
                                    public void run() {
                                        swipeRefreshLayout.setRefreshing(true);

                                        try {
                                            queue = Volley.newRequestQueue(Activity_Manage_Raw_MaterialInvoice.this);
                                            GetInvoiceList();

                                        } catch (Exception e) {
                                            // TODO: handle exceptions
                                        }
                                    }
                                }
        );

        List.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {

                Func_Alert_dialog(position);

            }

        });

    }


    /**
     * This method is called when swipe refresh is pulled down
     */
    @Override
    public void onRefresh() {
        try {
            Invoice_list.clear();
            queue = Volley.newRequestQueue(Activity_Manage_Raw_MaterialInvoice.this);
            GetInvoiceList();

        } catch (Exception e) {
            // TODO: handle exception
        }
    }


    @Override
    public void onFabAnimationEnd() {
        Intent intent = new Intent(Activity_Manage_Raw_MaterialInvoice.this, Activity_Add_Raw_Mat_Invoice.class);
        startActivityForResult(intent, REQUEST_CODE);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE) {
            mSheetLayout.contractFab();
        }
    }

    /*****************
     * GET Customer
     ***************/

    public void GetInvoiceList() {

        String tag_json_obj = "json_obj_req";

        StringRequest request = new StringRequest(Request.Method.POST,
                Appconfig.url_raw_mat_invoice_list, new Response.Listener<String>() {

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

                            String invoice_no = obj1.getString(TAG_INVOICE_NO);
                            String dealer_name = obj1.getString(TAG_DEALER_NAME);
                            String customer_name = obj1.getString(TAG_CUSTOMER_NAME);
                            String date = obj1.getString(TAG_DATE);
                            String time = obj1.getString(TAG_TIME);
                            String product_name = obj1.getString(TAG_PRODUCT_NAME);
                            String quantity = obj1.getString(TAG_QTY);
                            String tax = obj1.getString(TAG_TAX);
                            String taxable_value = obj1.getString(TAG_TAXABLE_VALUE);
                            String invoice_amount = obj1.getString(TAG_INVOICE_AMT);
                            String received_amount = obj1.getString(TAG_RECEIVED_AMT);
                            String balance_amount = obj1.getString(TAG_BALANCE_AMT);
                            String chequeno = obj1.getString(TAG_CHEQUE_NO);
                            String bankname = obj1.getString(TAG_BANK_NO);

                            // creating new HashMap
                            HashMap<String, String> map = new HashMap<String, String>();

                            // adding each child node to HashMap key => value
                            map.put(TAG_INVOICE_NO, invoice_no);
                            map.put(TAG_DEALER_NAME, dealer_name);
                            map.put(TAG_CUSTOMER_NAME, customer_name);
                            map.put(TAG_DATE, date);
                            map.put(TAG_TIME, time);
                            map.put(TAG_BRAND_NAME, "");
                            map.put(TAG_PRODUCT_NAME, product_name);
                            map.put(TAG_QTY, quantity);
                            map.put(TAG_TAX, tax);
                            map.put(TAG_TAXABLE_VALUE, taxable_value);
                            map.put(TAG_INVOICE_AMT, invoice_amount);
                            map.put(TAG_RECEIVED_AMT, received_amount);
                            map.put(TAG_BALANCE_AMT, balance_amount);
                            map.put(TAG_CHEQUE_NO, chequeno);
                            map.put(TAG_BANK_NO, bankname);

                            Invoice_list.add(map);

                            adapter = new Adapter_Manage_Invoice(Activity_Manage_Raw_MaterialInvoice.this,
                                    Invoice_list);
                            List.setAdapter(adapter);

                        }

                    } else if (success == 0) {

                        swipeRefreshLayout.setRefreshing(false);
                        Snackbar.make(parent_view, "No Records Found", Snackbar.LENGTH_SHORT).show();

                    }
                } catch (JSONException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
                // adapter.notifyDataSetChanged();
                // stopping swipe refresh
                swipeRefreshLayout.setRefreshing(false);
                //  pDialog.hide();
            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {

                // stopping swipe refresh
                swipeRefreshLayout.setRefreshing(false);

            }
        }) {

            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();

                params.put("user_id", str_dealer_id);

                System.out.println("USER ID : " + str_dealer_id);

                return params;
            }

        };

        // Adding request to request queue
        queue.add(request);
    }

    /***********************
     *  Fun Alert Dialog
     * ************************/

    private void Func_Alert_dialog(int position) {

        String invoice_no = Invoice_list.get(position).get(TAG_INVOICE_NO);
        String dealer_name = Invoice_list.get(position).get(TAG_DEALER_NAME);
        String customer_name = Invoice_list.get(position).get(TAG_CUSTOMER_NAME);
        String date = Invoice_list.get(position).get(TAG_DATE);
        String time = Invoice_list.get(position).get(TAG_TIME);
        String brand_name = Invoice_list.get(position).get(TAG_BRAND_NAME);
        String product_name = Invoice_list.get(position).get(TAG_PRODUCT_NAME);
        String quantity = Invoice_list.get(position).get(TAG_QTY);
        String tax = Invoice_list.get(position).get(TAG_TAX);
        String taxable_value = Invoice_list.get(position).get(TAG_TAXABLE_VALUE);
        String invoice_amount = Invoice_list.get(position).get(TAG_INVOICE_AMT);
        String received_amount = Invoice_list.get(position).get(TAG_RECEIVED_AMT);
        String balance_amount = Invoice_list.get(position).get(TAG_BALANCE_AMT);
        String chequeno = Invoice_list.get(position).get(TAG_CHEQUE_NO);
        String bankname = Invoice_list.get(position).get(TAG_BANK_NO);

        LayoutInflater li = LayoutInflater.from(Activity_Manage_Raw_MaterialInvoice.this);
        View promptsView = li.inflate(R.layout.alert_invoice_preview, null);

        AlertDialog alert_dialog = new AlertDialog.Builder(Activity_Manage_Raw_MaterialInvoice.this)
                .setView(promptsView)
                .setTitle("Aqua Bill")
                .setNegativeButton(android.R.string.cancel, null)
                .create();

        TextView txt_invoice_no = (TextView) promptsView.findViewById(R.id.alert_invoice_invoice_no);
        TextView txt_dealer_name = (TextView) promptsView.findViewById(R.id.alert_invoice_dealer_name);
        TextView txt_customer_name = (TextView) promptsView.findViewById(R.id.alert_invoice_customer);
        TextView txt_date = (TextView) promptsView.findViewById(R.id.alert_invoice_date);
        TextView txt_time = (TextView) promptsView.findViewById(R.id.alert_invoice_time);
        TextView txt_brand_name = (TextView) promptsView.findViewById(R.id.alert_invoice_brand_name);
        TextView txt_product_name = (TextView) promptsView.findViewById(R.id.alert_invoice_prod_name);
        TextView txt_quantity = (TextView) promptsView.findViewById(R.id.alert_invoice_qty);
        TextView txt_tax = (TextView) promptsView.findViewById(R.id.alert_invoice_tax);
        TextView txt_taxable_value = (TextView) promptsView.findViewById(R.id.alert_invoice_taxable_value);
        TextView txt_invoice_amount = (TextView) promptsView.findViewById(R.id.alert_invoice_invoive_amt);
        TextView txt_received_amount = (TextView) promptsView.findViewById(R.id.alert_invoice_invoive_amt);
        TextView txt_balance_amount = (TextView) promptsView.findViewById(R.id.alert_invoice_received_amt);
        TextView txt_chequeno = (TextView) promptsView.findViewById(R.id.alert_invoice_ref_no);
        TextView txt_bankname = (TextView) promptsView.findViewById(R.id.alert_invoice_bank_name);

        txt_invoice_no.setText("" + invoice_no);
        txt_dealer_name.setText("" + dealer_name);
        txt_customer_name.setText("" + customer_name);
        txt_date.setText("" + date);
        txt_time.setText("" + time);
        txt_brand_name.setText("" + brand_name);
        txt_product_name.setText("" +product_name);
        txt_quantity.setText("" + quantity);
        txt_tax.setText("" + tax);
        txt_taxable_value.setText("" + taxable_value);
        txt_invoice_amount.setText("" + invoice_amount);
        txt_received_amount.setText("" + received_amount);
        txt_balance_amount.setText("" + balance_amount);
        txt_chequeno.setText("" + chequeno);
        txt_bankname.setText("" + bankname);

        alert_dialog.setOnShowListener(new DialogInterface.OnShowListener() {

            @Override
            public void onShow(final DialogInterface alert_dialog) {

                Button button = ((AlertDialog) alert_dialog).getButton(AlertDialog.BUTTON_NEGATIVE);
                button.setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View view) {
                        // TODO Do something

                        alert_dialog.dismiss();
                    }
                });
            }
        });

        alert_dialog.show();

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

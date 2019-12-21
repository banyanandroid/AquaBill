package com.banyan.aquabill.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.android.volley.RequestQueue;
import com.banyan.aquabill.R;
import com.banyan.aquabill.global.SessionManager;
import com.google.android.material.snackbar.Snackbar;

import java.util.Calendar;

import dmax.dialog.SpotsDialog;

public class Activity_TestReport extends AppCompatActivity {

    String TAG = "Products and Rates";
    private View parent_view;

    // Session Manager Class
    SessionManager session;
    private static long back_pressed;

    SpotsDialog dialog;
    public static RequestQueue queue;

    private Toolbar mToolbar;
    private TextView txt_month1, txt_month2, txt_month3;
    private LinearLayout linear_monthly, linear_six_month, linear_year, linear_bromate, linear_five_year;


    String Str_select_pdu_id, Str_select_month , Str_select_month_id, Str_select_type_id = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_report);

        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        parent_view = findViewById(android.R.id.content);

        setSupportActionBar(mToolbar);
        getSupportActionBar().setTitle("Test Reports");
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        mToolbar.setNavigationIcon(getResources().getDrawable(R.drawable.ic_action_back));
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent i = new Intent(getApplicationContext(), MainActivity.class);
                i.putExtra("manage_class","managepdu");
                startActivity(i);
                finish();
            }
        });

        SharedPreferences sharedPreferences = PreferenceManager
                .getDefaultSharedPreferences(Activity_TestReport.this);
        Str_select_pdu_id = sharedPreferences.getString("selected_pdu_id", "selected_pdu_id");

        txt_month1 = (TextView) findViewById(R.id.test_report_txt_month1);
        txt_month2 = (TextView) findViewById(R.id.test_report_txt_month2);
        txt_month3 = (TextView) findViewById(R.id.test_report_txt_month3);

        linear_monthly = (LinearLayout) findViewById(R.id.test_report_linear_monthly);
        linear_six_month = (LinearLayout) findViewById(R.id.test_report_linear_sixmonth);
        linear_year = (LinearLayout) findViewById(R.id.test_report_linear_yearly);
        linear_bromate = (LinearLayout) findViewById(R.id.test_report_linear_bromate);
        linear_five_year  = (LinearLayout) findViewById(R.id.test_report_linear_five_year);

        txt_month1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Str_select_month = txt_month1.getText().toString().trim();

                txt_month1.setBackgroundColor(Color.YELLOW);
                txt_month2.setBackgroundResource(R.drawable.bg_rounded_corner);
                txt_month3.setBackgroundResource(R.drawable.bg_rounded_corner);

                GetMonthId();

                System.out.println("Month ID : " + Str_select_month_id );
            }
        });
        txt_month2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Str_select_month = txt_month2.getText().toString().trim();

                txt_month1.setBackgroundResource(R.drawable.bg_rounded_corner);
                txt_month2.setBackgroundColor(Color.YELLOW);
                txt_month3.setBackgroundResource(R.drawable.bg_rounded_corner);

                GetMonthId();

                System.out.println("Month ID : " + Str_select_month_id );
            }
        });
        txt_month3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Str_select_month = txt_month3.getText().toString().trim();

                txt_month1.setBackgroundResource(R.drawable.bg_rounded_corner);
                txt_month2.setBackgroundResource(R.drawable.bg_rounded_corner);
                txt_month3.setBackgroundColor(Color.YELLOW);

                GetMonthId();

                System.out.println("Month ID : " + Str_select_month_id );
            }
        });

        MonthPicker();

        linear_monthly.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Str_select_type_id = "1";

                System.out.println("PARAMS : " + "PDU :" + Str_select_pdu_id + "MONTH " +
                        Str_select_month_id + "TYPE " + Str_select_type_id);

                if (Str_select_month_id == null || Str_select_month_id.equals("")){

                    Snackbar.make(parent_view, "Please Select Month", Snackbar.LENGTH_SHORT).show();
                }else {

                    Intent i = new Intent(getApplicationContext(), Activity_TestReport_Image_Preview.class);
                    i.putExtra("Str_select_pdu_id",Str_select_pdu_id);
                    i.putExtra("Str_select_month",Str_select_month_id);
                    i.putExtra("Str_select_type",Str_select_type_id);
                    startActivity(i);
                }

            }
        });

        linear_six_month.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Str_select_type_id = "2";

                System.out.println("PARAMS : " + "PDU :" + Str_select_pdu_id + "MONTH " +
                        Str_select_month_id + "TYPE " + Str_select_type_id);

                if (Str_select_month_id == null || Str_select_month_id.equals("")){

                    Snackbar.make(parent_view, "Please Select Month", Snackbar.LENGTH_SHORT).show();
                }else {

                    Intent i = new Intent(getApplicationContext(), Activity_TestReport_Image_Preview.class);
                    i.putExtra("Str_select_pdu_id",Str_select_pdu_id);
                    i.putExtra("Str_select_month",Str_select_month_id);
                    i.putExtra("Str_select_type",Str_select_type_id);
                    startActivity(i);
                }
            }
        });

        linear_year.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Str_select_type_id = "3";

                System.out.println("PARAMS : " + "PDU :" + Str_select_pdu_id + "MONTH " +
                        Str_select_month_id + "TYPE " + Str_select_type_id);

                if (Str_select_month_id == null || Str_select_month_id.equals("")){

                    Snackbar.make(parent_view, "Please Select Month", Snackbar.LENGTH_SHORT).show();
                }else {

                    Intent i = new Intent(getApplicationContext(), Activity_TestReport_Image_Preview.class);
                    i.putExtra("Str_select_pdu_id",Str_select_pdu_id);
                    i.putExtra("Str_select_month",Str_select_month_id);
                    i.putExtra("Str_select_type",Str_select_type_id);
                    startActivity(i);
                }
            }
        });

        linear_bromate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Str_select_type_id = "4";

                System.out.println("PARAMS : " + "PDU :" + Str_select_pdu_id + "MONTH " +
                        Str_select_month_id + "TYPE " + Str_select_type_id);

                if (Str_select_month_id == null || Str_select_month_id.equals("")){

                    Snackbar.make(parent_view, "Please Select Month", Snackbar.LENGTH_SHORT).show();
                }else {

                    Intent i = new Intent(getApplicationContext(), Activity_TestReport_Image_Preview.class);
                    i.putExtra("Str_select_pdu_id",Str_select_pdu_id);
                    i.putExtra("Str_select_month",Str_select_month_id);
                    i.putExtra("Str_select_type",Str_select_type_id);
                    startActivity(i);
                }
            }
        });

        linear_five_year.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Str_select_type_id = "5";

                System.out.println("PARAMS : " + "PDU :" + Str_select_pdu_id + "MONTH " +
                        Str_select_month_id + "TYPE " + Str_select_type_id);

                if (Str_select_month_id == null || Str_select_month_id.equals("")){

                    Snackbar.make(parent_view, "Please Select Month", Snackbar.LENGTH_SHORT).show();
                }else {

                    Intent i = new Intent(getApplicationContext(), Activity_TestReport_Image_Preview.class);
                    i.putExtra("Str_select_pdu_id",Str_select_pdu_id);
                    i.putExtra("Str_select_month",Str_select_month_id);
                    i.putExtra("Str_select_type",Str_select_type_id);
                    startActivity(i);
                }
            }
        });
    }

    private void MonthPicker(){

        Calendar c = Calendar.getInstance();
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH)+1;

        System.out.println("Current Month" + month);

        if(month==1){
            txt_month1.setText("November");
            txt_month2.setText("December");
            txt_month3.setText("January");
        }else if(month==2){
            txt_month1.setText("December");
            txt_month2.setText("January");
            txt_month3.setText("February");
        }else if(month==3){
            txt_month1.setText("January");
            txt_month2.setText("February");
            txt_month3.setText("March");
        }else if(month==4){
            txt_month1.setText("February");
            txt_month2.setText("March");
            txt_month3.setText("April");
        }else if(month==5){
            txt_month1.setText("March");
            txt_month2.setText("April");
            txt_month3.setText("May");
        }else if(month==6){
            txt_month1.setText("April");
            txt_month2.setText("May");
            txt_month3.setText("June");
        }else if(month==7){
            txt_month1.setText("May");
            txt_month2.setText("June");
            txt_month3.setText("July");
        }else if(month==8){
            txt_month1.setText("June");
            txt_month2.setText("July");
            txt_month3.setText("August");
        }else if(month==9){
            txt_month1.setText("July");
            txt_month2.setText("August");
            txt_month3.setText("September");
        }else if(month==10){
            txt_month1.setText("August");
            txt_month2.setText("September");
            txt_month3.setText("October");
        }else if(month==11){
            txt_month1.setText("September");
            txt_month2.setText("October");
            txt_month3.setText("November");
        }else if(month==12){
            txt_month1.setText("October");
            txt_month2.setText("November");
            txt_month3.setText("December");
        }
    }

    private void GetMonthId() {

        if (Str_select_month.equals("January")){
            Str_select_month_id = "1";
        }else if (Str_select_month.equals("February")){
            Str_select_month_id = "2";
        }else if (Str_select_month.equals("March")){
            Str_select_month_id = "3";
        }else if (Str_select_month.equals("April")){
            Str_select_month_id = "4";
        }else if (Str_select_month.equals("May")){
            Str_select_month_id = "5";
        }else if (Str_select_month.equals("June")){
            Str_select_month_id = "6";
        }else if (Str_select_month.equals("July")){
            Str_select_month_id = "7";
        }else if (Str_select_month.equals("August")){
            Str_select_month_id = "8";
        }else if (Str_select_month.equals("September")){
            Str_select_month_id = "9";
        }else if (Str_select_month.equals("October")){
            Str_select_month_id = "10";
        }else if (Str_select_month.equals("November")){
            Str_select_month_id = "11";
        }else if (Str_select_month.equals("December")){
            Str_select_month_id = "12";
        }else {
            Str_select_month_id = "0";
        }
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

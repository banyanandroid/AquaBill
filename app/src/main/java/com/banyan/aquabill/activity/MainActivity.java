package com.banyan.aquabill.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.banyan.aquabill.R;
import com.banyan.aquabill.fragment.GenerateInvoiceFragment;
import com.banyan.aquabill.fragment.HomeFragment;
import com.banyan.aquabill.fragment.ManagePDUFragment;
import com.banyan.aquabill.fragment.MasterFragment;
import com.banyan.aquabill.fragment.ReportsFragment;
import com.banyan.aquabill.global.SessionManager;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.snackbar.Snackbar;
import com.mikhaellopez.circularimageview.CircularImageView;

import java.util.HashMap;


public class MainActivity extends AppCompatActivity {

    private NavigationView navigationView;
    private DrawerLayout drawer;
    private View navHeader;
    private ImageView imgNavHeaderBg;
    private CircularImageView imgProfile;
    private TextView txtName, txtWebsite;
    private Toolbar toolbar;
    SessionManager session;
    private View parent_view;

    // urls to load navigation header background image

    // index to identify current nav menu item
    public static int navItemIndex = 0;

    // tags used to attach the fragments
    private static final String TAG_PROFILE = "Dashboard";
    private static final String TAG_MASTER = "master";
    private static final String TAG_MANAGE_PDU = "manage_pdu";
    private static final String TAG_GENERATE_INVOICE = "generate_invoice";
    private static final String TAG_INVOICE_REPORT = "Reports";
    public static String CURRENT_TAG = TAG_PROFILE;

    // toolbar titles respected to selected nav menu item
    private String[] activityTitles;

    // flag to load home fragment when user presses back key
    private boolean shouldLoadHomeFragOnBackPress = true;
    private Handler mHandler;

    private String str_dealer_id, str_usertype, str_dealer_code, str_dealer_name, str_email_id, str_mobile_no, str_language;

    private String from_class = "" ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        parent_view = findViewById(android.R.id.content);

        setSupportActionBar(toolbar);

        mHandler = new Handler();

        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        navigationView = (NavigationView) findViewById(R.id.nav_view);

        // Navigation view header
        navHeader = navigationView.getHeaderView(0);
        txtName = (TextView) navHeader.findViewById(R.id.name);
        txtWebsite = (TextView) navHeader.findViewById(R.id.website);
        imgProfile = (CircularImageView) navHeader.findViewById(R.id.img_profile);


        session = new SessionManager(getApplicationContext());
        session.checkLogin();

        // get user data from session
        HashMap<String, String> user = session.getUserDetails();

        str_dealer_id = user.get(SessionManager.KEY_DEALER_ID);
        str_usertype= user.get(SessionManager.KEY_USER_TYPE);
        str_dealer_code = user.get(SessionManager.KEY_DEALER_CODE);
        str_dealer_name = user.get(SessionManager.KEY_DEALER_NAME);
        str_email_id = user.get(SessionManager.KEY_EMAIL_ID);
        str_mobile_no = user.get(SessionManager.KEY_MOBILE_NO);
        str_language = user.get(SessionManager.KEY_LANGUAGE);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            from_class = extras.getString("manage_class");

            System.out.println("value : " + from_class);

            if (from_class != null && !from_class.isEmpty()) {

            } else {
                from_class = "nothing";
            }

        }else {

        }



            // load toolbar titles from string resources
        activityTitles = getResources().getStringArray(R.array.nav_item_activity_titles);

        // load nav menu header data
        loadNavHeader();

        Menu menu = navigationView.getMenu();
        MenuItem nav_account = menu.findItem(R.id.nav_account);
        MenuItem nav_master = menu.findItem(R.id.nav_master);
        MenuItem nav_manage_pdu = menu.findItem(R.id.nav_manage_pdu);
        MenuItem nav_generate_invoice = menu.findItem(R.id.nav_generate_invoice);
        MenuItem nav_invoice_report = menu.findItem(R.id.nav_invoice_report);
       // nav_account.setTitle("உ்இஉ்இ்உ்இஉ்இஉ்இஉ்");


        // initializing navigation menu
        setUpNavigationView();

        if (savedInstanceState == null) {
            navItemIndex = 0;
            CURRENT_TAG = TAG_PROFILE;
            loadHomeFragment();
        }

        imgProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent i = new Intent(getApplicationContext(), Activity_Profile.class);
                startActivity(i);
                finish();

            }
        });

        if (!from_class.equals("") && !from_class.isEmpty() && from_class != null) {

            if (from_class.equals("master")){
                navItemIndex = 1;
                CURRENT_TAG = TAG_MASTER;
                loadHomeFragment();
            }else if (from_class.equals("managepdu")){
                navItemIndex = 2;
                CURRENT_TAG = TAG_MANAGE_PDU;
                loadHomeFragment();
            }else if (from_class.equals("invoice")){
                navItemIndex = 3;
                CURRENT_TAG = TAG_GENERATE_INVOICE;
                loadHomeFragment();
            }else if (from_class.equals("reports")){
                navItemIndex = 4;
                CURRENT_TAG = TAG_INVOICE_REPORT;
                loadHomeFragment();
            }else {

                navItemIndex = 0;
                CURRENT_TAG = TAG_PROFILE;
                loadHomeFragment();
            }

        }
    }

    /***
     * Load navigation menu header information
     * like background image, profile image
     * name, website, notifications action view (dot)
     */
    private void loadNavHeader() {
        // name, website
        txtName.setText(str_dealer_name + " | "+ str_dealer_code);
        txtWebsite.setText(str_email_id);
    }

    /***
     * Returns respected fragment that user
     * selected from navigation menu
     */
    private void loadHomeFragment() {
        // selecting appropriate nav menu item
        selectNavMenu();

        // set toolbar title
        setToolbarTitle();

        // if user select the current navigation menu again, don't do anything
        // just close the navigation drawer
        if (getSupportFragmentManager().findFragmentByTag(CURRENT_TAG) != null) {
            drawer.closeDrawers();

            // show or hide the fab button
            toggleFab();
            return;
        }


        Runnable mPendingRunnable = new Runnable() {
            @Override
            public void run() {
                // update the main content by replacing fragments
                Fragment fragment = getHomeFragment();
                FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
                fragmentTransaction.setCustomAnimations(android.R.anim.fade_in,
                        android.R.anim.fade_out);
                fragmentTransaction.replace(R.id.frame, fragment, CURRENT_TAG);
                fragmentTransaction.commitAllowingStateLoss();
            }
        };

        // If mPendingRunnable is not null, then add to the message queue
        if (mPendingRunnable != null) {
            mHandler.post(mPendingRunnable);
        }

        // show or hide the fab button
        toggleFab();

        //Closing drawer on item click
        drawer.closeDrawers();

        // refresh toolbar menu
        invalidateOptionsMenu();
    }

    private Fragment getHomeFragment() {
        switch (navItemIndex) {
            case 0:
                HomeFragment homeFragment = new HomeFragment();
                return homeFragment;
            case 1:
                MasterFragment masterFragment = new MasterFragment();
                return masterFragment;
            case 2:
                ManagePDUFragment managePDUFragment = new ManagePDUFragment();
                return managePDUFragment;

            case 3:
                GenerateInvoiceFragment generateInvoiceFragment = new GenerateInvoiceFragment();
                return generateInvoiceFragment;

            case 4:
                ReportsFragment reportsFragment = new ReportsFragment();
                return reportsFragment;

            default:
                return new HomeFragment();
        }
    }

    private void setToolbarTitle() {
        getSupportActionBar().setTitle(activityTitles[navItemIndex]);
    }

    private void selectNavMenu() {
        navigationView.getMenu().getItem(navItemIndex).setChecked(true);
    }

    private void setUpNavigationView() {
        //Setting Navigation View Item Selected Listener to handle the item click of the navigation menu
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {

            // This method will trigger on item Click of navigation menu
            @Override
            public boolean onNavigationItemSelected(MenuItem menuItem) {

                //Check to see which item was being clicked and perform appropriate action
                switch (menuItem.getItemId()) {
                    //Replacing the main content with ContentFragment Which is our Inbox View;
                    case R.id.nav_account:
                        navItemIndex = 0;
                        CURRENT_TAG = TAG_PROFILE;
                        break;
                    case R.id.nav_master:
                        navItemIndex = 1;
                        CURRENT_TAG = TAG_MASTER;
                        break;
                    case R.id.nav_manage_pdu:
                        navItemIndex = 2;
                        CURRENT_TAG = TAG_MANAGE_PDU;
                        break;
                    case R.id.nav_generate_invoice:
                        navItemIndex = 3;
                        CURRENT_TAG = TAG_GENERATE_INVOICE;
                        break;
                    case R.id.nav_invoice_report:
                        navItemIndex = 4;
                        CURRENT_TAG = TAG_INVOICE_REPORT;
                        break;
                    default:
                        navItemIndex = 0;
                }

                //Checking if the item is in checked state or not, if not make it in checked state
                if (menuItem.isChecked()) {
                    menuItem.setChecked(false);
                } else {
                    menuItem.setChecked(true);
                }
                menuItem.setChecked(true);

                loadHomeFragment();

                return true;
            }
        });


        ActionBarDrawerToggle actionBarDrawerToggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.openDrawer, R.string.closeDrawer) {

            @Override
            public void onDrawerClosed(View drawerView) {
                // Code here will be triggered once the drawer closes as we dont want anything to happen so we leave this blank
                super.onDrawerClosed(drawerView);
            }

            @Override
            public void onDrawerOpened(View drawerView) {
                // Code here will be triggered once the drawer open as we dont want anything to happen so we leave this blank
                super.onDrawerOpened(drawerView);
            }
        };

        //Setting the actionbarToggle to drawer layout
        drawer.setDrawerListener(actionBarDrawerToggle);

        //calling sync state is necessary or else your hamburger icon wont show up
        actionBarDrawerToggle.syncState();
    }

    @Override
    public void onBackPressed() {
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawers();
            return;
        }

        // This code loads home fragment when back key is pressed
        // when user is in other fragment than home
        if (shouldLoadHomeFragOnBackPress) {
            // checking if user is on other navigation menu
            // rather than home
            if (navItemIndex != 0) {
                navItemIndex = 0;
                CURRENT_TAG = TAG_PROFILE;
                loadHomeFragment();
                return;
            }
        }

        super.onBackPressed();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.

        // show menu only when home fragment is selected
        if (navItemIndex == 0) {
            getMenuInflater().inflate(R.menu.main, menu);
        }

        // when fragment is notifications, load the menu created for notifications
        if (navItemIndex == 3) {
            getMenuInflater().inflate(R.menu.notifications, menu);
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_logout) {
            Snackbar.make(parent_view, "Logout ", Snackbar.LENGTH_SHORT).show();
            session.logoutUser();
            return true;
        }

        // user is in notifications fragment
        // and selected 'Mark all as Read'
        if (id == R.id.action_mark_all_read) {
            Toast.makeText(getApplicationContext(), "All notifications marked as read!", Toast.LENGTH_LONG).show();
        }

        // user is in notifications fragment
        // and selected 'Clear All'
        if (id == R.id.action_clear_notifications) {
            Toast.makeText(getApplicationContext(), "Clear all notifications!", Toast.LENGTH_LONG).show();
        }

        return super.onOptionsItemSelected(item);
    }

    // show or hide the fab
    private void toggleFab() {

    }
}

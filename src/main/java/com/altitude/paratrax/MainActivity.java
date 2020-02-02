package com.altitude.paratrax;

import androidx.annotation.RequiresApi;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.FragmentTransaction;

import androidx.appcompat.widget.SearchView;
import androidx.core.view.MenuItemCompat;
import androidx.fragment.app.Fragment;

import android.content.Intent;
import android.graphics.Color;
import android.hardware.Sensor;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.altitude.paratrax.Classes.Sensors.Accelerometer;
import com.altitude.paratrax.Classes.Sensors.Gyroscope;
import com.altitude.paratrax.Classes.Sensors.Humidity;
import com.altitude.paratrax.Classes.Sensors.Temperature;
import com.altitude.paratrax.Firebase.Auth.EmailPasswordActivity;
import com.altitude.paratrax.Fragments.Full_Logbook_Fragment;
import com.altitude.paratrax.Fragments.Home_Fragment;
import com.altitude.paratrax.Fragments.Profile_Fragment;
import com.altitude.paratrax.Fragments.Quick_Log_Fragment;
import com.altitude.paratrax.Fragments.Quick_Log_Update_Fragment;
import com.altitude.paratrax.ResideMenu.ResideMenu;
import com.altitude.paratrax.ResideMenu.ResideMenuItem;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Stack;

import static android.os.Build.VERSION_CODES.M;

public class MainActivity extends BaseActivity implements View.OnClickListener,
                                                            Quick_Log_Update_Fragment.OnFragmentInteractionListener,
                                                            Full_Logbook_Fragment.OnFragmentInteractionListener{

    @Override
    public void onFragmentInteraction(Uri uri){
        //you can leave it empty
    }

    //TODO: SensorManager
    private Accelerometer accelerometer;
    private Gyroscope gyroscope;
    private Humidity humidity;
    private Temperature temperature;

    FirebaseUser user;
    private FirebaseAuth mAuth;
    private void enablePersistence() {
        // [START rtdb_enable_persistence]
        FirebaseDatabase.getInstance().setPersistenceEnabled(true);
        // [END rtdb_enable_persistence]
    }

    private boolean isHumiditySensorPresent;
    private boolean isTemperatureSensorPresent;
    private TextView mRelativeHumidityValue;
    private TextView mAbsoluteHumidityValue;
    private TextView mDewPointValue;
    private TextView mTemperatureValue;
    private float mLastKnownRelativeHumidity = 0;
    private float mLastKnownTemperature = 0;

    public Stack<String> mFragmentStack;
    TextView txt_logon;
    Button btn_login;

    private String mUserId;
    private boolean mSignedIn = false;

    private ResideMenu resideMenu;
    private ResideMenuItem itemHome;
    private ResideMenuItem itemProfile;
    private ResideMenuItem itemPayments;
    private ResideMenuItem itemSettings;
    private ResideMenuItem itemMessage;
    private ResideMenuItem itemFriends;
    private ResideMenuItem itemLogbookFull;
    //my new additions here
    private ResideMenuItem itemLogbook;
    private MainActivity mContext;

    /////////////////////////title_bar_Ootions//////
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
//
//        MenuItem hamburger = menu.getItem(R.id.app_bar);
//        hamburger.setOnMenuItemClickListener((MenuItem.OnMenuItemClickListener) this)
        MenuItem searchItem = menu.findItem(R.id.action_search);

        final SearchView searchView = (SearchView) MenuItemCompat.getActionView(searchItem);
        ////////section customizing search view///////////////////////////
        int searchImgId = androidx.appcompat.R.id.search_button;
        ImageView v = (ImageView) searchView.findViewById(searchImgId);
        v.setImageResource(R.drawable.ic_search_custom_swirl);
        // Customize searchview text and hint colors
        int searchEditId = androidx.appcompat.R.id.search_src_text;
        EditText et = (EditText) searchView.findViewById(searchEditId);
        et.setTextColor(Color.BLACK);
        et.setHintTextColor(Color.BLACK);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                // perform query here
                // workaround to avoid issues with some emulators and keyboard devices firing twice if a keyboard enter is used
                // see https://code.google.com/p/android/issues/detail?id=24599
                //https://developer.android.com/guide/topics/search/search-dialog
                searchView.clearFocus();
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });


        return super.onCreateOptionsMenu(menu);
    }

    /////////////////handling action bar click
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle presses on the action bar items
        switch (item.getItemId()) {
            case R.id.miCompose:
                composeMessage();
                return true;
            case R.id.miProfile:
                showProfileView();
                return true;
            case R.id.milogin:
                Intent intent = new Intent(mContext, EmailPasswordActivity.class);//NB: MainActivity.this
                startActivity(intent);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void composeMessage() {

    }

    public void showProfileView() {
    }

    ///////////////////////////////
    ////////////////////////Called when the activity is first created.
    @Override
    public void onStart() {
        super.onStart();
        mAuth = FirebaseAuth.getInstance();

        if (mAuth.getCurrentUser() != null) {
            mUserId = mAuth.getCurrentUser().getUid();
        } else {
            Intent intent = new Intent(mContext, EmailPasswordActivity.class);
            startActivity(intent);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = this.findViewById(R.id.app_bar);
        setSupportActionBar(toolbar);   //.setDisplayShowTitleEnabled(false);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        accelerometer = new Accelerometer(this);
        gyroscope = new Gyroscope(this);
        humidity = new Humidity(this);
        temperature = new Temperature(this);

        mRelativeHumidityValue = toolbar.findViewById(R.id.relativehumiditytext);
        mAbsoluteHumidityValue = toolbar.findViewById(R.id.absolutehumiditytext);
        mDewPointValue = toolbar.findViewById(R.id.dewpointtext);
        mTemperatureValue = toolbar.findViewById(R.id.temperaturetext);

        accelerometer.setListener(new Accelerometer.Listener() {
            @Override
            @RequiresApi(M)
            public void onTranslation(float tx, float ty, float tz) {
//                if (tx > 1.0f) {
//                    //TODO: //i.e. turn the screen red
//                    //getWindow().getDecorView().setBackgroundColor(Color.RED);
//                    changeFragment(new Home_Fragment());
//                } else if (tx < -1.0f) {
//                    //TODO: //i.e. turn the screen blue
//                    getWindow().getDecorView().setBackgroundColor(Color.BLUE);
//                }
            }
        });
        gyroscope.setListener(new Gyroscope.Listener() {
            @Override
            public void onRotation(float rx, float ry, float rz) {
//                if (rx > 1.0f) {
//                    //TODO: //i.e. turn the screen red
//                    getWindow().getDecorView().setBackgroundColor(Color.GREEN);
//                } else if (rx < -1.0f) {
//                    //TODO: //i.e. turn the screen blue
//                    getWindow().getDecorView().setBackgroundColor(Color.YELLOW);
//                }
            }
        });


        humidity.setListener(new Humidity.Listener() {
            @Override
            public void onSweating(float hx) {
                mRelativeHumidityValue.setText("rH: " + Math.round(hx * 10) / 10.0 + " | ");
                mLastKnownRelativeHumidity = hx;
            }
        });

        temperature.setListener(new Temperature.Listener() {
            @Override
            public void onTempChange(float tc) {
                mTemperatureValue.setText("rT: " + Math.round(tc * 10) / 10.0 + " | ");
                mLastKnownTemperature = tc;
                if (mLastKnownRelativeHumidity != 0) {
                    float temp = tc;
                    float absoluteHumidity = calculateAbsoluteHumidity(temp, mLastKnownRelativeHumidity);
                    mAbsoluteHumidityValue.setText("");  //("aH: " + Math.round(absoluteHumidity * 10) / 10.0 + " | ");
                    float dewPoint = calculateDewPoint(temp, mLastKnownRelativeHumidity);
                    mDewPointValue.setText("cB: " + Math.round(dewPoint * 10) / 10.0);

                }
            }
        });


        setVisible(false);//TODO: set landing page fragment (maybe just transparent arrows for swipe) so backstack will work on top level

        mContext = this;

        //full screen mode initaialized

        /////
        mFragmentStack = new Stack<String>();
        //FireBase auth Instance
        mAuth = FirebaseAuth.getInstance();
        mSignedIn = mAuth.getCurrentUser() != null;
        if (mSignedIn) {
            mUserId = mAuth.getCurrentUser().getUid();
        }


        ///Reside menu
        setUpMenu();
        if (savedInstanceState == null)
            resideMenu.openMenu(ResideMenu.DIRECTION_LEFT);


    }

    private void setUpMenu() {

        // attach to current activity;
        resideMenu = new ResideMenu(this);
        //old
        resideMenu.setUse3D(true);
        resideMenu.setBackground(R.drawable.paxtakoo_v2);

        resideMenu.attachToActivity(this);
        resideMenu.setMenuListener(menuListener);
        // create menu items;
        itemLogbook = new ResideMenuItem(this, R.drawable.ic_dark_events, "Log flight");
        itemLogbookFull = new ResideMenuItem(this, R.drawable.ic_dark_events, "Log book");
        itemHome = new ResideMenuItem(this, R.drawable.ic_home_dark, "Notices");
        itemPayments = new ResideMenuItem(this, R.drawable.icon_calendar, "Payments");
        itemSettings = new ResideMenuItem(this, R.drawable.icon_settings, "Settings");
        itemProfile = new ResideMenuItem(this, R.drawable.ic_dark_profile, "Profile");
        itemMessage = new ResideMenuItem(this, R.drawable.ic_dark_message, "Message");
        itemFriends = new ResideMenuItem(this, R.drawable.ic_dark_friends, "Friends");


        itemHome.setOnClickListener(this);
        itemProfile.setOnClickListener(this);
        itemPayments.setOnClickListener(this);
        itemSettings.setOnClickListener(this);
        itemMessage.setOnClickListener(this);
        itemFriends.setOnClickListener(this);
        itemLogbookFull.setOnClickListener(this);
        ///
        itemLogbook.setOnClickListener(this);

        //Add or remove fragments from main menu here
        resideMenu.addMenuItem(itemLogbook, ResideMenu.DIRECTION_LEFT);
        resideMenu.addMenuItem(itemLogbookFull, ResideMenu.DIRECTION_LEFT);
        resideMenu.addMenuItem(itemHome, ResideMenu.DIRECTION_LEFT);
        resideMenu.addMenuItem(itemPayments, ResideMenu.DIRECTION_LEFT);
        resideMenu.addMenuItem(itemSettings, ResideMenu.DIRECTION_LEFT);
        resideMenu.addMenuItem(itemProfile, ResideMenu.DIRECTION_LEFT);
        resideMenu.addMenuItem(itemMessage, ResideMenu.DIRECTION_LEFT);
        resideMenu.addMenuItem(itemFriends, ResideMenu.DIRECTION_LEFT);

        resideMenu.setSwipeDirectionDisable(ResideMenu.DIRECTION_RIGHT);
    }


    @Override
    public void onClick(View view) {

        if (view == itemLogbook) {
            changeFragment(new Quick_Log_Fragment());
//            Intent intent = new Intent(this, Quick_Log.class);
//            startActivity(intent);
        } else if (view == itemProfile) {
            changeFragment(new Profile_Fragment());
        } else if (view == itemLogbookFull) {
            changeFragment(new Full_Logbook_Fragment());
        } else if (view == itemHome) {
            changeFragment(new Home_Fragment());
        } else if (view == itemPayments) {
            changeFragment(new Home_Fragment());
        }

        resideMenu.closeMenu();
    }

    private ResideMenu.OnMenuListener menuListener = new ResideMenu.OnMenuListener() {
        @Override
        public void openMenu() {
            //  Toast.makeText(mContext, "Menu is opened!", Toast.LENGTH_SHORT).show();
        }

        @Override
        public void closeMenu() {
            //  Toast.makeText(mContext, "Menu is closed!", Toast.LENGTH_SHORT).show();
        }
    };

    @Override
    public void onBackPressed() {
        // from the stack we can get the latest fragment
        //  androidx.fragment.app.Fragment fragment = getSupportFragmentManager().findFragmentByTag(mFragmentStack.peek());
        // If its an instance of Fragment1 I don't want to finish my activity, so I launch a Toast instead.
//        if (fragment instanceof Home_Fragment) {
//            Toast.makeText(getApplicationContext(), "Swipe right to view / left to close menu", Toast.LENGTH_SHORT).show();
//        } else {
        //  finish();
        //  removeFragment();
        resideMenu.openMenu(ResideMenu.DIRECTION_LEFT);
        //  super.onBackPressed();
        //   }
    }

    private void removeFragment() {
        // remove the current fragment from the stack.
        mFragmentStack.pop();
        androidx.fragment.app.FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        // get fragment that is to be shown (in our case fragment1).
        Fragment fragment = getSupportFragmentManager().findFragmentByTag(mFragmentStack.peek());
        // This time I set an animation with no fade in, so the user doesn't wait for the animation in back press
        transaction.setCustomAnimations(FragmentTransaction.TRANSIT_NONE, FragmentTransaction.TRANSIT_FRAGMENT_FADE);
        // We must use the show() method.
        transaction.show(fragment);
        transaction.commit();
    }

    private void changeFragment(Fragment targetFragment) {

        resideMenu.clearIgnoredViewList();
        Fragment fragment = new Home_Fragment();
        String tag = fragment.toString();
        mFragmentStack.add(tag);
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.main_fragment, targetFragment, tag)
                .setTransitionStyle(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
                .addToBackStack(tag)
                .commit();

    }

    @Override
    protected void onResume() {
        super.onResume();
        humidity.register();
        accelerometer.register();
        gyroscope.register();
        temperature.register();
    }

    @Override
    protected void onPause() {
        super.onPause();
        humidity.unregister();
        accelerometer.unregister();
        gyroscope.unregister();
        temperature.unregister();
    }

    /* Meaning of the constants
 Dv: Absolute humidity in grams/meter3
 m: Mass constant
 Tn: Temperature constant
 Ta: Temperature constant
 Rh: Actual relative humidity in percent (%) from phone’s sensor
 Tc: Current temperature in degrees C from phone’ sensor
 A: Pressure constant in hP
 K: Temperature constant for converting to kelvin
 */
    public float calculateAbsoluteHumidity(float temperature, float relativeHumidity) {
        float Dv = 0;
        float m = 17.62f;
        float Tn = 243.12f;
        float Ta = 216.7f;
        float Rh = relativeHumidity;
        float Tc = temperature;
        float A = 6.112f;
        float K = 273.15f;

        Dv = (float) (Ta * (Rh / 100) * A * Math.exp(m * Tc / (Tn + Tc)) / (K + Tc));

        return Dv;
    }

    /* Meaning of the constants
    Td: Dew point temperature in degrees Celsius
    m: Mass constant
    Tn: Temperature constant
    Rh: Actual relative humidity in percent (%) from phone’s sensor
    Tc: Current temperature in degrees C from phone’ sensor
    */
    public float calculateDewPoint(float temperature, float relativeHumidity) {
        float Td = 0;
        float m = 17.62f;
        float Tn = 243.12f;
        float Rh = relativeHumidity;
        float Tc = temperature;

        Td = (float) (Tn * ((Math.log(Rh / 100) + m * Tc / (Tn + Tc)) / (m - (Math.log(Rh / 100) + m * Tc / (Tn + Tc)))));

        return Td;
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        // mSensorManager = null;
        humidity = null;
        temperature = null;
        accelerometer = null;
        gyroscope = null;
    }


}
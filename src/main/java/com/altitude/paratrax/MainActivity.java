package com.altitude.paratrax;

import androidx.fragment.app.FragmentTransaction;

import androidx.appcompat.widget.SearchView;
import androidx.core.view.MenuItemCompat;
import androidx.fragment.app.Fragment;

//import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.altitude.paratrax.Firebase.Auth.EmailPasswordActivity;
import com.altitude.paratrax.Fragments.Full_Logbook_Fragment;
import com.altitude.paratrax.Fragments.Home_Fragment;
import com.altitude.paratrax.Fragments.Profile_Fragment;
import com.altitude.paratrax.Fragments.Quick_Log_Fragment;
import com.altitude.paratrax.Firebase.Auth.ChooserActivity;
import com.altitude.paratrax.ResideMenu.ResideMenu;
import com.altitude.paratrax.ResideMenu.ResideMenuItem;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.Stack;

//import com.google.firebase.FirebaseApp;

public class MainActivity extends BaseActivity implements View.OnClickListener {       //AppCompatActivity

    private FirebaseAuth mAuth;
    public Stack<String> mFragmentStack;
    TextView txt_logon;
    Button btn_login;

    private String mUserId;
    private boolean mSignedIn = false;

    private ResideMenu resideMenu;
    private ResideMenuItem itemHome;
    private ResideMenuItem itemProfile;
    private ResideMenuItem itemCalendar;
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
        MenuItem searchItem = menu.findItem(R.id.action_search);
        final SearchView searchView = (SearchView) MenuItemCompat.getActionView(searchItem);
        ///////section expanding search view
//        searchItem.expandActionView();
//        searchView.requestFocus();

        ////////section customizing search view///////////////////////////
        int searchImgId = androidx.appcompat.R.id.search_button; //android.support.v7.appcompat.R.id.search_button;
        ImageView v = (ImageView) searchView.findViewById(searchImgId);
        v.setImageResource(R.drawable.ic_search_custom_swirl);
        // Customize searchview text and hint colors
        int searchEditId = androidx.appcompat.R.id.search_src_text;
        EditText et = (EditText) searchView.findViewById(searchEditId);
        et.setTextColor(Color.BLACK);
        et.setHintTextColor(Color.BLACK);
        //////////////////////////////////
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
            case R.id.title_bar_left_menu_item:
                resideMenu.openMenu(ResideMenu.DIRECTION_LEFT);
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
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser != null)
            mAuth.updateCurrentUser(currentUser);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setVisible(false);//TODO: set landing page fragment (maybe just transparent arrows for swipe) so backstack will work on top level
        mContext = this;
        hideSystemUI();//full screen mode initaialized //TODO: needs to be put in base to persist
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
        itemHome = new ResideMenuItem(this, R.drawable.ic_home_dark, "Notices");
        itemCalendar = new ResideMenuItem(this, R.drawable.icon_calendar, "Calendar");
        itemSettings = new ResideMenuItem(this, R.drawable.icon_settings, "Settings");
        itemProfile = new ResideMenuItem(this, R.drawable.ic_dark_profile, "Profile");
        itemMessage = new ResideMenuItem(this, R.drawable.ic_dark_message, "Message");
        itemFriends = new ResideMenuItem(this, R.drawable.ic_dark_friends, "Friends");
        itemLogbookFull = new ResideMenuItem(this, R.drawable.ic_dark_events, "Log book");
        itemLogbook = new ResideMenuItem(this, R.drawable.ic_dark_events, "Log flight");

        itemHome.setOnClickListener(this);
        itemProfile.setOnClickListener(this);
        itemCalendar.setOnClickListener(this);
        itemSettings.setOnClickListener(this);
        itemMessage.setOnClickListener(this);
        itemFriends.setOnClickListener(this);
        itemLogbookFull.setOnClickListener(this);
        ///
        itemLogbook.setOnClickListener(this);

        //Add or remove fragments from main menu here
        resideMenu.addMenuItem(itemHome, ResideMenu.DIRECTION_LEFT);
        resideMenu.addMenuItem(itemProfile, ResideMenu.DIRECTION_LEFT);
        resideMenu.addMenuItem(itemMessage, ResideMenu.DIRECTION_LEFT);
        resideMenu.addMenuItem(itemFriends, ResideMenu.DIRECTION_LEFT);
        resideMenu.addMenuItem(itemLogbookFull, ResideMenu.DIRECTION_LEFT);
        //my new addition
        resideMenu.addMenuItem(itemSettings, ResideMenu.DIRECTION_LEFT);
        ///
        resideMenu.addMenuItem(itemLogbook, ResideMenu.DIRECTION_LEFT);

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
//        }else if (view == itemMessage){
//            changeFragment(new MessageFragment());
//        }else if (view == itemFriends){
//            changeFragment(new FriendsFragment());
        } else if (view == itemLogbookFull) {
            changeFragment(new Full_Logbook_Fragment());
        } else if (view == itemHome) {
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
    public void onBackPressed(){
        // from the stack we can get the latest fragment
        androidx.fragment.app.Fragment fragment = getSupportFragmentManager().findFragmentByTag(mFragmentStack.peek());
        // If its an instance of Fragment1 I don't want to finish my activity, so I launch a Toast instead.
        if (fragment instanceof Home_Fragment){
            Toast.makeText(getApplicationContext(), "Swipe right to view menu", Toast.LENGTH_SHORT).show();
        }
        else{
            // Remove the framg
            removeFragment();
            super.onBackPressed();
        }
    }
    private void removeFragment(){
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


    // This method hides the system bars and resize the content
    private void hideSystemUI() {
        getWindow().getDecorView().setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION // hide nav bar
                        | View.SYSTEM_UI_FLAG_FULLSCREEN // hide status bar
                        // remove the following flag for version < API 19
                        | View.SYSTEM_UI_FLAG_IMMERSIVE
        );
    }


}


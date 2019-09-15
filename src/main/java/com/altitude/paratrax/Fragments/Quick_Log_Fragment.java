package com.altitude.paratrax.Fragments;

import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.Toast;
import androidx.appcompat.widget.Toolbar;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProviders;

import com.altitude.paratrax.Classes.Quick_Log;
import com.altitude.paratrax.Quick_Log_RecyclerViewHolder;
import com.altitude.paratrax.Models.Quick_Log_ViewModel;
import com.altitude.paratrax.R;
import com.altitude.paratrax.ResideMenu.ResideMenu;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.jaredrummler.materialspinner.MaterialSpinner;

/**
 * A simple {@link Fragment} subclass.
 */
public class Quick_Log_Fragment extends Fragment {

    View view;
    FragmentManager fm;

    // private ResideMenu resideMenu;

    private FirebaseAuth mAuth;
    private String mUserId;

    private View rootView;
    private boolean mSignedIn = false;

    ///controls

    ArrayAdapter<CharSequence> adapter;

    private EditText txt_fname, txt_lname, txt_weight, txt_pax_age, txt_email, txt_phone, txt_additional;
    private CheckBox chk_medical;  //, chk_pics, chk_sherpa, chk_transport, chk_sd_given;
    private Switch chk_pics, chk_sherpa, chk_transport, chk_sd_given;
    private Button btn_Quick_Log_Post;
    private Spinner comp_spinner, loc_spinner;


    //Firebase

    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;

    FirebaseRecyclerOptions<Quick_Log> options;
    FirebaseRecyclerAdapter<Quick_Log, Quick_Log_RecyclerViewHolder> fb_adapter;

    Quick_Log selectedQuick_Log_;
    String selectedKey;

    public Quick_Log_Fragment() {
        // Required empty public constructor
    }

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment Full_Logbook_Fragment.
     */
    // TODO: Rename and change types and number of parameters
    public static Quick_Log_Fragment newInstance(String param1, String param2) {
        Quick_Log_Fragment ql_fragment = new Quick_Log_Fragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        ql_fragment.setArguments(args);
        return ql_fragment;
    }

    private Quick_Log_ViewModel mViewModel;

    public static Quick_Log_Fragment newInstance() {
        return new Quick_Log_Fragment();
    }


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
          view = inflater.inflate(R.layout.fragment_quick__log, container, false);
        setupToolbar(view);
        return view;
    }

    public void setupToolbar(View v) {
        Toolbar toolbar = v.findViewById(R.id.app_bar);
        AppCompatActivity appCompatActivity = (AppCompatActivity) getActivity();
        if(appCompatActivity != null)
            appCompatActivity.setSupportActionBar(toolbar);


    }
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

      //  comp_spinner = (Spinner) view.findViewById(R.id.mv_spinner_company);
      //  loc_spinner = (Spinner) view.findViewById(R.id.spinner_locations_array);
        txt_fname = (EditText) view.findViewById(R.id.txt_fname);
        txt_lname = (EditText) view.findViewById(R.id.txt_lname);
        txt_weight = (EditText) view.findViewById(R.id.txt_weight);
        txt_pax_age = (EditText) view.findViewById(R.id.txt_pax_age);
        txt_email = (EditText) view.findViewById(R.id.txt_email);
        txt_phone = (EditText) view.findViewById(R.id.txt_phone);
        txt_additional = (EditText) view.findViewById(R.id.txt_additional);
        chk_medical = (CheckBox) view.findViewById(R.id.chk_medical);
        chk_pics = (Switch) view.findViewById(R.id.chk_pics);
        chk_sherpa = (Switch) view.findViewById(R.id.chk_sherpa);
        chk_transport = (Switch) view.findViewById(R.id.chk_transport);
        btn_Quick_Log_Post = (Button) view.findViewById(R.id.btn_Quick_Log_Post);

        //Firebase db change listener
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference();
        //reading from the db
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                //adapter.notifyDataSetChanged();
                //displayComment();
                //   //  //
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
//                String value = dataSnapshot.getValue(String.class);
//                Log.d(TAG, "Value is: " + value);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });

        btn_Quick_Log_Post.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Quick_Log_Post_Entry();
            }
        });

        MaterialSpinner co_spinner = (MaterialSpinner) view.findViewById(R.id.mv_spinner_company);
        co_spinner.setItems("Parapax", "CTTP", "Fly Cape Town", "CTTA", "Tandem Flight Company","Hi-5","Paraglide South Africa","SkyWings","TITS",
                "Para Taxi","Icarus");
        co_spinner.setOnItemSelectedListener(new MaterialSpinner.OnItemSelectedListener<String>() {

            @Override public void onItemSelected(MaterialSpinner view, int position, long id, String item) {
                Snackbar.make(view, " " + item + " selected", Snackbar.LENGTH_LONG).show();
            }
        });
        MaterialSpinner loc_spinner = (MaterialSpinner) view.findViewById(R.id.mv_spinner_location);
        loc_spinner.setItems("Signal Hill", "Lions Head");
        loc_spinner.setOnItemSelectedListener(new MaterialSpinner.OnItemSelectedListener<String>() {

            @Override public void onItemSelected(MaterialSpinner view, int position, long id, String item) {
                Snackbar.make(view, " " + item + " selected", Snackbar.LENGTH_LONG).show();
            }
        });

    }


    private void Quick_Log_Post_Entry() {


        //data fields to upload to db
        String fname = txt_fname.getText().toString();
        String lname = txt_lname.getText().toString();
        String weight = txt_weight.getText().toString();
        String age = txt_pax_age.getText().toString();
        String email = txt_email.getText().toString();
        String phone = txt_phone.getText().toString();
        String additional = txt_additional.getText().toString();
        Long tsLong = System.currentTimeMillis() / 1000;
        String dateTime = tsLong.toString();

        Quick_Log Quick_Log = new Quick_Log(fname, lname, weight, age, email, phone, additional, mUserId, dateTime);

        //adding the below !=0 to prevent blank entries. needed for update and delete
        //but not sure that shouldnt be done on layout logic..think that should be the case.
        if (fname.length() != 0 && lname.length() != 0) {

            //databaseReference.push().setValue(Quick_Log); //create unique id for content
            databaseReference.child("Quick_log").push().setValue(Quick_Log, new DatabaseReference.CompletionListener() {
                @Override
                public void onComplete(@Nullable DatabaseError databaseError, @NonNull DatabaseReference databaseReference) {


                    ////clear required fields
//                    txt_fname.setText("");
//                    txt_lname.setText("");
//                    txt_email.setText("");
//                    txt_additional.setText("");
//                    txt_phone.setText("");
//                    txt_pax_age.setText("");
//                    txt_weight.setText("");


                    Toast.makeText(getActivity(), "Quick log entry added.", Toast.LENGTH_SHORT).show();


                    // //Restart the fragmant
                    ReplaceCurrentFragment();


                    ////Change to logbook fragment onComplete
                    // changeFragment(new Full_Logbook_Fragment());


                }
            });
        }

    }


    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
//        adapter = ArrayAdapter.createFromResource(getActivity(), R.array.companys_array, android.R.layout.simple_spinner_item);
//        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//        comp_spinner.setAdapter(adapter);

        mViewModel = ViewModelProviders.of(this).get(Quick_Log_ViewModel.class);
        // TODO: Use the ViewModel`1
        //TODO:/populate from/to viewmodel and firebase

        //FireBase auth Instance
        mAuth = FirebaseAuth.getInstance();
        mSignedIn = mAuth.getCurrentUser() != null;
        if (mSignedIn) {
            mUserId = mAuth.getCurrentUser().getUid();
        }
    }

    public void ReplaceCurrentFragment() {
        Fragment fragment = new Full_Logbook_Fragment();
        FragmentManager fm = getFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();

        if (Build.VERSION.SDK_INT >= 26) {
            ft.setReorderingAllowed(false);
        }
        ft.replace(R.id.main_fragment, fragment, newInstance().toString());
        ft.addToBackStack(newInstance().toString());//TODO: backstack not working
        ft.commit();
    }


}


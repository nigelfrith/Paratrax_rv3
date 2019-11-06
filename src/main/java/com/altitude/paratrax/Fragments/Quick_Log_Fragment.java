package com.altitude.paratrax.Fragments;

//import com.altitude.paratrax.Firebase.FirebaseDatabaseHelper.DataStatus;

import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProviders;

import com.altitude.paratrax.Classes.Quick_Log;
import com.altitude.paratrax.Models.Quick_Log_ViewModel;
import com.altitude.paratrax.Quick_Log_RecyclerViewHolder;
import com.altitude.paratrax.R;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.jaredrummler.materialspinner.MaterialSpinner;

import java.util.ArrayList;
import java.util.List;

import static com.firebase.ui.auth.AuthUI.TAG;

/**
 * A simple {@link Fragment} subclass.
 */
public class Quick_Log_Fragment extends Fragment {

    public interface DataStatus {
        void DataIsLoaded(List<Quick_Log> logs, List<String> keys);

        void DataIsInserted();

        void DataIsUpdated();

        void DataIsDeleted();
    }

    private DataStatus iiDataStatus;

    public void setiDataStatus(DataStatus iiDataStatus) {
        this.iiDataStatus = iiDataStatus;
    }

    View view;
    FragmentManager fm;

    // private ResideMenu resideMenu;

    private FirebaseAuth mAuth;
    private String mUserId;

    private View rootView;
    private boolean mSignedIn = false;

    ///controls

    ArrayAdapter<CharSequence> adapter;

    private EditText txt_fname, txt_lname, txt_weight, txt_pax_age, txt_email, txt_phone, txt_additional, txt_last_flight;
    private CheckBox chk_medical, chk_disability, chk_baggage, chk_pics, chk_sherpa, chk_transport, chk_sd_given, chk_packing;
    //  private Switch chk_pics, chk_sherpa, chk_transport, chk_sd_given;
    private Button btn_Quick_Log_Post;
    private Spinner comp_spinner, loc_spinner;


    //Firebase
    private List<Quick_Log> logs = new ArrayList<>();

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

        return view;
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
        chk_disability = (CheckBox) view.findViewById(R.id.chk_disability);
        chk_baggage = (CheckBox) view.findViewById(R.id.chk_baggage);
        chk_pics = (CheckBox) view.findViewById(R.id.chk_pics);
        chk_sherpa = (CheckBox) view.findViewById(R.id.chk_sherpa);
        chk_transport = (CheckBox) view.findViewById(R.id.chk_transport);
        chk_packing = (CheckBox) view.findViewById(R.id.chk_packing);
        chk_sd_given = (CheckBox) view.findViewById(R.id.chk_sd_given);
        btn_Quick_Log_Post = (Button) view.findViewById(R.id.btn_Quick_Log_Post);
        txt_last_flight = (EditText) view.findViewById(R.id.txt_last_flight);

        //Firebase db change listener
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference("logbooks");//.child("Quick_log");//.child(mUserId);

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                logs.clear();
                List<String> keys = new ArrayList<>();
                for (DataSnapshot keyNode : dataSnapshot.getChildren()) {
                    keys.add(keyNode.getKey());
                    Quick_Log quick_log = keyNode.getValue(Quick_Log.class);
                    logs.add(quick_log);
                }
                if (iiDataStatus != null) {
                    iiDataStatus.DataIsLoaded(logs, keys);
                }
                //TODO:
                //adapter.notifyDataSetChanged();

                // String value = dataSnapshot.getValue(String.class);
                // Log.d(TAG, "Value is: " + value);
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
        co_spinner.setItems("Parapax", "CTTP", "Fly Cape Town", "CTTA", "Tandem Flight Company", "Hi-5", "Paraglide South Africa", "SkyWings", "TITS",
                "Para Taxi", "Icarus");
        co_spinner.setOnItemSelectedListener(new MaterialSpinner.OnItemSelectedListener<String>() {

            @Override
            public void onItemSelected(MaterialSpinner view, int position, long id, String item) {
                Snackbar.make(view, " " + item + " selected", Snackbar.LENGTH_LONG).show();
            }
        });
        MaterialSpinner loc_spinner = (MaterialSpinner) view.findViewById(R.id.mv_spinner_location);
        loc_spinner.setItems("Signal Hill", "Lions Head", "Other");
        loc_spinner.setOnItemSelectedListener(new MaterialSpinner.OnItemSelectedListener<String>() {

            @Override
            public void onItemSelected(MaterialSpinner view, int position, long id, String item) {
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

        String lastFlight = txt_last_flight.getText().toString();

        boolean hasMedical = chk_medical.isChecked();
        boolean hasDisability = chk_disability.isChecked();
        boolean hasTransport = chk_transport.isChecked();
        boolean hasBaggage = chk_baggage.isChecked();
        boolean hasPics = chk_pics.isChecked();
        boolean hasSherpa = chk_sherpa.isChecked();
        boolean hasPacking = chk_packing.isChecked();
        boolean hasSDGiven = chk_sd_given.isChecked();


        Long tsLong = System.currentTimeMillis() / 1000;
        String dateTime = tsLong.toString();

        Quick_Log ql = new Quick_Log(fname, lname, weight, age, email, phone, lastFlight, additional,
                hasMedical, hasDisability, hasTransport, hasBaggage, hasPics, hasSherpa, hasPacking, hasSDGiven,
                mUserId, dateTime);

        if (fname.length() != 0 && lname.length() != 0) {

            String key = databaseReference.child("Quick_log").push().getKey();
            databaseReference.child("Quick_log").child(key).setValue(ql)
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            iiDataStatus = new DataStatus() {
                                @Override
                                public void DataIsLoaded(List<Quick_Log> logs, List<String> keys) {

                                }

                                @Override
                                public void DataIsInserted() {
                                    Toast.makeText(getActivity(), "Logbook entry added.", Toast.LENGTH_LONG).show();
                                }

                                @Override
                                public void DataIsUpdated() {

                                }

                                @Override
                                public void DataIsDeleted() {

                                }
                            };



                            ReplaceCurrentFragment();
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


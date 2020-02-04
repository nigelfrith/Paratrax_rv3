package com.altitude.paratrax.Fragments;

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
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.jaredrummler.materialspinner.MaterialSpinner;

import java.text.DateFormat;
import java.text.Format;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static com.firebase.ui.auth.AuthUI.TAG;
import static com.firebase.ui.auth.AuthUI.getApplicationContext;

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

    private List<Quick_Log> logs = new ArrayList<>();

    //Firebase//

    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;
    FirebaseAuth auth;
    FirebaseUser firebaseuser;
    private String uid;

    FirebaseRecyclerOptions<Quick_Log> options;
    FirebaseRecyclerAdapter<Quick_Log, Quick_Log_RecyclerViewHolder> fb_adapter;


    //view//
    private View rootView;
    private boolean mSignedIn = false;

    //controls//
    ArrayAdapter<String> adapter;
//    ArrayAdapter<CharSequence> adapter;

    private EditText txt_brief, txt_fname, txt_lname, txt_weight, txt_pax_age, txt_email, txt_phone, txt_additional, txt_last_flight;
    private CheckBox chk_medical, chk_disability, chk_baggage, chk_pics, chk_sherpa, chk_transport, chk_sd_given, chk_packing;
    //  private Switch chk_pics, chk_sherpa, chk_transport, chk_sd_given;
    private Button btn_Quick_Log_Post;
    private MaterialSpinner spin_company, spin_location;


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

        txt_brief = (EditText) view.findViewById(R.id.txt_brief);
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


        spin_company = (MaterialSpinner) view.findViewById(R.id.mv_spinner_company);
        spin_company.setItems("Parapax", "CTTP", "Fly Cape Town", "CTTA", "Tandem Flight Company", "Hi-5", "Paraglide South Africa", "SkyWings", "TITS",
                "Para Taxi", "Icarus");
        spin_company.setOnItemSelectedListener(new MaterialSpinner.OnItemSelectedListener<String>() {

            @Override
            public void onItemSelected(MaterialSpinner view, int position, long id, String item) {
                Snackbar.make(view, " " + item + " selected", Snackbar.LENGTH_LONG).show();
            }
        });
        spin_location = (MaterialSpinner) view.findViewById(R.id.mv_spinner_location);
        spin_location.setItems("Signal Hill", "Lions Head", "Other");
        spin_location.setOnItemSelectedListener(new MaterialSpinner.OnItemSelectedListener<String>() {

            @Override
            public void onItemSelected(MaterialSpinner view, int position, long id, String item) {
                Snackbar.make(view, " " + item + " selected", Snackbar.LENGTH_LONG).show();
            }
        });

        btn_Quick_Log_Post.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Quick_Log_Post_Entry();
            }
        });

    }


    public void Quick_Log_Post_Entry() {


        //data fields to upload to db
        String brief = txt_brief.getText().toString();
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

        String company = spin_company.getText().toString();
        String location = spin_location.getText().toString();

        Date tsLong = new Date(); //System.currentTimeMillis() / 1000;
        String dateTime =  tsLong.toString();     //DateFormat.getDateInstance(DateFormat.LONG).format(tsLong);

        Quick_Log ql = new Quick_Log(brief, fname, lname, weight, age, email, phone, lastFlight, additional,
                hasMedical, hasDisability, hasTransport, hasBaggage, hasPics, hasSherpa, hasPacking, hasSDGiven,
                uid, dateTime, company, location);

        if (brief.length() != 0) {
//TODO: i think .getkey here has to be the uid . or uid is nested above it
            //String key = databaseReference.child("Quick_Log").push().getKey();
            //String key = databaseReference.child("quick_log").child(uid).push().getKey();
            auth = FirebaseAuth.getInstance();
            if (auth.getCurrentUser() != null) {
                uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
            } else {
                uid = "No uid";
            }
            //TODO:persistance
            //FirebaseDatabase.getInstance().setPersistenceEnabled(true);
            databaseReference = FirebaseDatabase.getInstance().getReference("quick_log").child(uid);
            String key = databaseReference.push().getKey();

            databaseReference.child(key).setValue(ql)
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                Toast.makeText(getContext(), "insert successful", Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(getContext(), "insert failed sorry.",
                                        Toast.LENGTH_SHORT).show();

                            }
                        }
                    })
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            iiDataStatus = new DataStatus() {
                                @Override
                                public void DataIsLoaded(List<Quick_Log> logs, List<String> keys) {

                                }

                                @Override
                                public void DataIsInserted() {
                                    Toast.makeText(getActivity(), "Log entry added.", Toast.LENGTH_LONG).show();
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
        mViewModel = ViewModelProviders.of(this).get(Quick_Log_ViewModel.class);
        // TODO: Use the ViewModel`1
        //TODO:/populate from/to viewmodel and firebase
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


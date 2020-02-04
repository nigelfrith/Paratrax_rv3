package com.altitude.paratrax.Fragments;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.altitude.paratrax.Classes.Quick_Log;
import com.altitude.paratrax.R;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.MutableData;
import com.google.firebase.database.Query;
import com.google.firebase.database.Transaction;
import com.google.firebase.database.ValueEventListener;
import com.jaredrummler.materialspinner.MaterialSpinner;

import org.jetbrains.annotations.NotNull;

import java.util.Date;

import static com.firebase.ui.auth.AuthUI.TAG;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link Quick_Log_Update_Fragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link Quick_Log_Update_Fragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Quick_Log_Update_Fragment extends Fragment {

    DatabaseReference mDatabase;
    FirebaseAuth auth;
    TextView tvUpd;
    View mMainView;
    String uid;
    Quick_Log ql;

    private EditText txt_brief, txt_fname, txt_lname, txt_weight, txt_pax_age, txt_email, txt_phone, txt_additional, txt_last_flight;
    private CheckBox chk_medical, chk_disability, chk_baggage, chk_pics, chk_sherpa, chk_transport, chk_sd_given, chk_packing;
    //  private Switch chk_pics, chk_sherpa, chk_transport, chk_sd_given;
    private Button btn_Quick_Log_Update;
    private MaterialSpinner spin_company, spin_location;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    public Quick_Log_Update_Fragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment Quick_Log_Update_Fragment.
     */
    // TODO: Rename and change types and number of parameters
    public static Quick_Log_Update_Fragment newInstance(String param1, String param2) {
        Quick_Log_Update_Fragment fragment = new Quick_Log_Update_Fragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        mMainView = inflater.inflate(R.layout.fragment_quick__log__update_, container, false);
        auth = FirebaseAuth.getInstance();
        if (auth.getCurrentUser() != null) {
            uid = auth.getCurrentUser().getUid();
        }
//        tvUpd = mMainView.findViewById(R.id.txt_updateEntry);
//        tvUpd.setText("Update Card param 1  " + mParam1 + " param2 " + mParam2 );

        txt_brief = (EditText) mMainView.findViewById(R.id.txt_brief);
        txt_fname = (EditText) mMainView.findViewById(R.id.txt_fname);
        txt_lname = (EditText) mMainView.findViewById(R.id.txt_lname);
        txt_weight = (EditText) mMainView.findViewById(R.id.txt_weight);
        txt_pax_age = (EditText) mMainView.findViewById(R.id.txt_pax_age);
        txt_email = (EditText) mMainView.findViewById(R.id.txt_email);
        txt_phone = (EditText) mMainView.findViewById(R.id.txt_phone);
        chk_medical = (CheckBox) mMainView.findViewById(R.id.chk_medical);
        chk_disability = (CheckBox) mMainView.findViewById(R.id.chk_disability);
        chk_baggage = (CheckBox) mMainView.findViewById(R.id.chk_baggage);
        chk_pics = (CheckBox) mMainView.findViewById(R.id.chk_pics);
        chk_sherpa = (CheckBox) mMainView.findViewById(R.id.chk_sherpa);
        chk_transport = (CheckBox) mMainView.findViewById(R.id.chk_transport);
        chk_packing = (CheckBox) mMainView.findViewById(R.id.chk_packing);
        chk_sd_given = (CheckBox) mMainView.findViewById(R.id.chk_sd_given);
        txt_last_flight = (EditText) mMainView.findViewById(R.id.txt_last_flight);
        txt_additional = (EditText) mMainView.findViewById(R.id.txt_additional);
        btn_Quick_Log_Update = (Button) mMainView.findViewById(R.id.btn_Quick_Log_Update);

        //use mParam1 to load the firebase live content


        spin_company = (MaterialSpinner) mMainView.findViewById(R.id.mv_spinner_company);
        spin_company.setItems("Parapax", "CTTP", "Fly Cape Town", "CTA", "Tandem Flight Company", "Hi-5", "Paraglide South Africa", "SkyWings", "TITS",
                "Para Taxi", "Icarus");
        spin_location = (MaterialSpinner) mMainView.findViewById(R.id.mv_spinner_location);
        spin_location.setItems("Signal Hill", "Lions Head", "Other");


        mDatabase = FirebaseDatabase.getInstance().getReference().child("quick_log").child(uid);//.child(mParam1);

        Query query = mDatabase.orderByKey();

        mDatabase.child(mParam1).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull final DataSnapshot dataSnapshot) {
                ql = dataSnapshot.getValue(Quick_Log.class);
                txt_brief.setText(ql.getBrief());
                txt_fname.setText(ql.getFname());
                txt_lname.setText(ql.getLname());
                txt_email.setText(ql.getEmail());
                txt_phone.setText(ql.getPhone());
                txt_weight.setText(ql.getWeight());
                txt_pax_age.setText(ql.getAge());
                txt_last_flight.setText((ql.getLastFlight()));
                txt_additional.setText(ql.getAdditional());

                if(spin_location.length() > 0 && spin_company.length() >0) {
                    String compareValue = "some value";
                    ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getContext(), R.array.companys_array, android.R.layout.simple_spinner_dropdown_item);
                    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    spin_company.setAdapter(adapter);
                    if (compareValue != null) {
                        int spinnerPosition = adapter.getPosition(ql.getCompany());
                        spin_company.setSelectedIndex(spinnerPosition);
                    }
                    ArrayAdapter<CharSequence> adapterP = ArrayAdapter.createFromResource(getContext(), R.array.locations_array, android.R.layout.simple_spinner_dropdown_item);
                    adapterP.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    spin_location.setAdapter(adapterP);
                    if (compareValue != null) {
                        int spinnerPosition = adapterP.getPosition(ql.getLocation());
                        spin_location.setSelectedIndex(spinnerPosition);
                    }
                }

                if (ql.isHasMedical() == true) {
                    chk_medical.setChecked(true);
                } else chk_medical.setChecked(false);

                if (ql.isHasDisability() == true) {
                    chk_disability.setChecked(true);
                } else chk_disability.setChecked(false);

                if (ql.isHasBaggage() == true) {
                    chk_baggage.setChecked(true);
                } else chk_baggage.setChecked(false);

                if (ql.isHasTransport() == true) {
                    chk_transport.setChecked(true);
                } else chk_transport.setChecked(false);

                if (ql.isHasPics() == true) {
                    chk_pics.setChecked(true);
                } else chk_pics.setChecked(false);

                if (ql.isHasSherpa() == true) {
                    chk_sherpa.setChecked(true);
                } else chk_sherpa.setChecked(false);

                if (ql.isHasPacking() == true) {
                    chk_packing.setChecked(true);
                } else chk_packing.setChecked(false);

                if (ql.isHasSDGiven() == true) {
                    chk_sd_given.setChecked(true);
                } else chk_sd_given.setChecked(false);

                btn_Quick_Log_Update.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        Quick_Log_Update();


                    }
                });

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        return mMainView;
    }

    private void Quick_Log_Update() {



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

                Quick_Log pql = new Quick_Log(brief, fname, lname, weight, age, email, phone, lastFlight, additional,
                        hasMedical, hasDisability, hasTransport, hasBaggage, hasPics, hasSherpa, hasPacking, hasSDGiven,
                        uid, dateTime, company, location);

                mDatabase.child(mParam1).setValue(pql)
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()) {
                                    Toast.makeText(getContext(), "Update successful", Toast.LENGTH_SHORT).show();
                                    Fragment fragment = new Quick_Log_Update_Fragment();
                                    String tag = fragment.toString();
                                    getFragmentManager()
                                            .beginTransaction()
                                            .replace(R.id.main_fragment, new Full_Logbook_Fragment(), tag)
                                            .setTransitionStyle(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
                                            .addToBackStack(tag)
                                            .commit();
                                } else {
                                    Toast.makeText(getContext(), "Update failed sorry.",
                                            Toast.LENGTH_SHORT).show();

                                }
                            }
                        });

            }
    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onViewCreated(@NotNull @NonNull View mMainView, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(mMainView, savedInstanceState);


    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }


    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}

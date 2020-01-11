package com.altitude.paratrax.Fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import android.util.SparseBooleanArray;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.altitude.paratrax.Classes.Helper;
import com.altitude.paratrax.Classes.Quick_Log;
import com.altitude.paratrax.Models.Full_Logbook_Model;
import com.altitude.paratrax.R;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.lang.reflect.Array;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


public class Full_Logbook_Fragment extends Fragment {



    public String refKey;
    private String mUserId;
    String uid;
    private boolean mSignedIn = false;

    private View mMainView;
    private Toast mToastText;


    //firebase//
    DatabaseReference mDatabase;
    FirebaseAuth auth;

    private ProgressBar progressBar;

    EditText userid, fname, lname, email, date;
    Button button, btn_Delete,btn_Edit;
    private RecyclerView recyclerView;
    private LinearLayoutManager linearLayoutManager;
    private FirebaseRecyclerAdapter adapter;


    public Full_Logbook_Fragment() {
        // Required empty public constructor
    }

    @Override
    public void onStart() {
        super.onStart();
        adapter.startListening();
    }

    @Override
    public void onStop() {
        super.onStop();
        adapter.stopListening();
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
    public static Full_Logbook_Fragment newInstance(String param1, String param2) {
        Full_Logbook_Fragment fragment = new Full_Logbook_Fragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    public static Full_Logbook_Fragment newInstance() {
        return new Full_Logbook_Fragment();
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
    public View onCreateView(LayoutInflater inflater, final ViewGroup container,
                             final Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        mMainView = inflater.inflate(R.layout.fragment_logbook_full, container, false);

        auth = FirebaseAuth.getInstance();
        if (auth.getCurrentUser() != null) {
            uid = auth.getCurrentUser().getUid();
        }

        progressBar = mMainView.findViewById(R.id.progressBar);


        final View l1 = mMainView.findViewById(R.id.l1);
        final View l2 = mMainView.findViewById(R.id.LinearLayout);

        final Animation fadeOut = AnimationUtils.loadAnimation(getContext(), android.R.anim.fade_out);
        final Animation fadeIn = AnimationUtils.loadAnimation(getContext(), android.R.anim.fade_in);


        button = (Button) mMainView.findViewById(R.id.btn_new);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentTransaction fragmentTransaction = getActivity()
                        .getSupportFragmentManager().beginTransaction();
                fragmentTransaction.replace(R.id.main_fragment, new Quick_Log_Fragment(), newInstance().toString());
                fragmentTransaction.addToBackStack(newInstance().toString());
                fragmentTransaction.commit();
            }
        });

        //reversing the layout so in descending order when viewed
        linearLayoutManager = new LinearLayoutManager(getContext());
        linearLayoutManager.setReverseLayout(true);
        linearLayoutManager.setStackFromEnd(true);
        //rv
        recyclerView = (RecyclerView) mMainView.findViewById(R.id.rv_logbook_list);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(linearLayoutManager);
        //firebase////

        mDatabase = FirebaseDatabase.getInstance().getReference().child("quick_log").child(uid);
        mDatabase.keepSynced(true);

        Query query = mDatabase.orderByKey();


        FirebaseRecyclerOptions<Quick_Log> options = new FirebaseRecyclerOptions.Builder<Quick_Log>()
                .setQuery(query, Quick_Log.class).build();

        adapter = new FirebaseRecyclerAdapter<Quick_Log, ViewHolder>(options) {

            @Override
            protected void onBindViewHolder(@NonNull final ViewHolder holder, final int position, @NonNull Quick_Log model) {

                holder.settxtEmail(model.getEmail());
                holder.settxtfName(model.getFname());
                holder.settxtlName(model.getLname());
                holder.setPhone(model.getPhone());
                holder.settxtDate(model.getDateTime());


                holder.rvD.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        deleteEntry(refKey, null);
                    }
                });

            }

            @NonNull
            @Override
            public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.full_logbook_list_item, parent, false);

                return new ViewHolder(view);
            }

//            @Override
//            public int getItemCount() {
//                return this.model.size();
//            }

        };


        mDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                     refKey = snapshot.getKey();
                }
                //animation
                fadeOut.setAnimationListener(new Animation.AnimationListener() {
                    @Override
                    public void onAnimationStart(Animation animation) {
                    }

                    @Override
                    public void onAnimationRepeat(Animation animation) {
                    }

                    @Override
                    public void onAnimationEnd(Animation animation) {
                        l1.setVisibility(View.GONE);
                    }
                });
                l1.startAnimation(fadeOut);
                l2.setVisibility(View.VISIBLE);
                l2.startAnimation(fadeIn);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });

        recyclerView.setAdapter(adapter);
        return mMainView;
    }

    public void deleteEntry(String key, final Quick_Log_Fragment.DataStatus dataStatus) {
        mDatabase = FirebaseDatabase.getInstance().getReference().child("quick_log").child(uid);
        mDatabase.child(key).setValue(null)//not letting set to null
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        dataStatus.DataIsDeleted();
                    }
                });
    }

    public class ViewHolder extends RecyclerView.ViewHolder
            implements View.OnClickListener {
        public LinearLayout root;
        public TextView txtfName;
        public TextView txtlName;
        public TextView txtEmail;
        public TextView txtDate;
        public Button rvD;

        private String key;
        private SparseBooleanArray selectedItems = new SparseBooleanArray();

        public ViewHolder(View itemView) {
            super(itemView);
            //itemView.setOnClickListener(this);

            root = itemView.findViewById(R.id.list_root);
            txtfName = itemView.findViewById(R.id.fname);
            txtlName = itemView.findViewById(R.id.lname);
            txtEmail = itemView.findViewById(R.id.email);
            txtDate = itemView.findViewById(R.id.txt_date);
            rvD = itemView.findViewById(R.id.btn_Delete);

            root.setOnClickListener(this);
        }


        @Override
        public void onClick(View view) {
            if (selectedItems.get(getAdapterPosition(), false)) {
                selectedItems.delete(getAdapterPosition());
                view.setSelected(false);
                Toast.makeText(view.getContext(), "De-Selected log entry = " + getPosition(), Toast.LENGTH_SHORT).show();
            } else {
                selectedItems.put(getAdapterPosition(), true);
                view.setSelected(true);
                String text = "Selected log entry = " + getPosition();
               // customToast(getPosition(), text);
                 Toast.makeText(view.getContext(), "Selected log entry = " + getPosition(), Toast.LENGTH_SHORT).show();

            }
        }

        public void settxtfName(String fname) {
            txtfName.setText(fname);
        }

        public void settxtlName(String string) {
            txtlName.setText(string);
        }

        public void settxtEmail(String string) {
            txtEmail.setText(string);
        }
        public void setPhone(String phone) {
            TextView txtPhone = (TextView) itemView.findViewById(R.id.phone);
            txtPhone.setText(phone);
        }
        public void settxtDate(String date) {
            txtDate.setText(String.format(date, "yyyy-MM-dd HH:mm:ss"));
        }
    }

    private void customToast(int position, final String text) {
        View toastView = getLayoutInflater().inflate(R.layout.custom_toastview_log_entry_selected, null);
        Toast toast = Toast.makeText(this.getContext(), text, Toast.LENGTH_LONG);
        toast.setView(toastView);
        toast.setGravity(Gravity.BOTTOM, 0, 0);
        toast.show();
    }

    private void displayText(final String message) {
        mToastText.cancel();
        mToastText.setText(message);
        mToastText.show();
    }


}

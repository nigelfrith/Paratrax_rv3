package com.altitude.paratrax.Fragments;

import android.content.DialogInterface;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import android.util.SparseBooleanArray;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.altitude.paratrax.Classes.Quick_Log;
import com.altitude.paratrax.R;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;


public class Full_Logbook_Fragment extends Fragment {

    //TODO: Offline firebase data required. firebase offline
    //TODO: Day/Date divider lines
    //TODO: Add company|weather stamp|location
    //TODO:

    public interface DataStatus {
        void DataIsLoaded(List<Full_Logbook_Fragment> logs, List<String> keys);

        void DataIsInserted();

        void DataIsUpdated();

        void DataIsDeleted();
    }

    private Quick_Log_Fragment.DataStatus iiDataStatus;

    public void setiDataStatus(Quick_Log_Fragment.DataStatus iiDataStatus) {
        this.iiDataStatus = iiDataStatus;
    }

    public String refKey;
    private String mUserId;
    String uid;
    String pos;
    String pos2;
    private boolean mSignedIn = false;

    private View mMainView;
    private Toast mToastText;


    //firebase//
    DatabaseReference mDatabase;
    FirebaseAuth auth;


    private ProgressBar progressBar;

    EditText userid, fname, lname, email, date;
    Button new_button, btn_Edit;    //btn_Delete,
    private RecyclerView recyclerView;
    private LinearLayoutManager linearLayoutManager;
    private FirebaseRecyclerAdapter adapter;
    long mDays;


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
        args.putString(ARG_PARAM1, param1); //key, value pairs
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    public static Quick_Log_Update_Fragment newInstanceQLU(String param1, String param2) {
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


        new_button = (Button) mMainView.findViewById(R.id.btn_new);
        new_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getFragmentManager().beginTransaction().addToBackStack(newInstance(pos, "2").toString()).replace(R.id.main_fragment, new Quick_Log_Fragment()).commit();

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

        //set persistance for offline?
        // FirebaseDatabase.getInstance().setPersistenceEnabled(true);


        Query query = mDatabase.orderByKey();


        FirebaseRecyclerOptions<Quick_Log> options = new FirebaseRecyclerOptions.Builder<Quick_Log>()
                .setQuery(query, Quick_Log.class).build();

        adapter = new FirebaseRecyclerAdapter<Quick_Log, ViewHolder>(options) {

            @NonNull
            @Override
            public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.full_logbook_list_item, parent, false);

                return new ViewHolder(view);
            }

            @Override
            protected void onBindViewHolder(@NonNull final ViewHolder holder, final int position, @NonNull final Quick_Log model) {

                holder.settxtEmail(model.getEmail());
                holder.settxtfName(model.getFname() + " " + model.getLname());
                // holder.settxtlName(model.getLname());
                holder.setPhone(model.getPhone());
                holder.settxtBrief(model.getBrief());
                holder.settxtDate(model.getDateTime().toString());

                Long l = model.getDateTimeL();
                String dt = getDisplayableTime(l);
                holder.setTxtDays(dt);


                holder.setTxtCompany(model.getCompany() + "  |  " + model.getLocation());
                holder.setTxtAge(model.getAge() + "yrs" + "  |  " + model.getWeight() + "kgs");


                holder.rvD.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                        builder.setTitle("Confirm logbook entry delete!");
                        builder.setMessage("You are about to delete this entry. Do you really want to proceed?");
                        builder.setCancelable(false);
                        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                                adapter.getRef(position).removeValue();

                                Toast.makeText(getContext(), "Record deleted", Toast.LENGTH_SHORT).show();
                                return;
                            }
                        });

                        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Toast.makeText(getContext(), "Deletion cancelled", Toast.LENGTH_SHORT).show();
                            }
                        });

                        builder.show();


                    }
                });

                holder.rvEdit.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        pos = adapter.getRef(position).getKey();
                        pos2 = adapter.getRef(position).child("dateTime").toString();
                        Fragment fragment = new Quick_Log_Fragment();
                        String tag = fragment.toString();

                        getFragmentManager()
                                .beginTransaction()
                                .replace(R.id.main_fragment, newInstanceQLU(pos, pos2), tag)
                                .setTransitionStyle(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
                                .addToBackStack(tag)
                                .commit();
                    }
                });

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

    public class ViewHolder extends RecyclerView.ViewHolder
            implements View.OnClickListener {
        public LinearLayout root;
        public TextView txtfName;
        public TextView txtlName;
        public TextView txtEmail;
        public TextView txtDate;
        public TextView txtDays;
        public TextView txtBrief;
        public TextView txtAge;
        public TextView txtWeight;


        public Button rvD;
        private Button rvEdit;

        private String key;
        private SparseBooleanArray selectedItems = new SparseBooleanArray();

        public TextView txtCompany;
        public TextView txtLocation;

        public ViewHolder(View itemView) {
            super(itemView);
            //itemView.setOnClickListener(this);

            root = itemView.findViewById(R.id.list_root);
            txtfName = itemView.findViewById(R.id.fname);
            // txtlName = itemView.findViewById(R.id.lname);
            txtEmail = itemView.findViewById(R.id.email);
            txtDate = itemView.findViewById(R.id.txt_date);
            rvD = itemView.findViewById(R.id.btn_Delete);
            rvEdit = itemView.findViewById(R.id.btn_Edit);
            txtBrief = itemView.findViewById(R.id.txt_brief);

            txtCompany = itemView.findViewById(R.id.txt_co);
            // txtLocation = itemView.findViewById(R.id.txt_loc);
            txtAge = itemView.findViewById(R.id.txt_age);


            root.setOnClickListener(this);
        }

        public void setTxtAge(String age) {
            txtAge.setText(age);
        }

        public void setTxtCompany(String company) {
            txtCompany.setText(company);
        }

        public void setTxtLocation(String location) {
            txtLocation.setText(location);
        }

        public void settxtBrief(String brief) {
            txtBrief.setText(brief);
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


        public void setTxtDays(String days) {

            txtDays = itemView.findViewById(R.id.txt_days);
            txtDays.setText(days);
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

    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }

    public static String getDisplayableTime(long delta) {
        long difference = 0;
        Long mDate = java.lang.System.currentTimeMillis();

        if (mDate > delta) {
            difference = mDate - delta;
            final long seconds = difference / 1000;
            final long minutes = seconds / 60;
            final long hours = minutes / 60;
            final long days = hours / 24;
            final long months = days / 31;
            final long years = days / 365;

            if (seconds < 0) {
                return "not yet";
            } else if (seconds < 60) {
                return seconds == 1 ? "one second ago" : seconds + " seconds ago";
            } else if (seconds < 120) {
                return "a minute ago";
            } else if (seconds < 2700) // 45 * 60
            {
                return minutes + " minutes ago";
            } else if (seconds < 5400) // 90 * 60
            {
                return "an hour ago";
            } else if (seconds < 86400) // 24 * 60 * 60
            {
                return hours + " hours ago";
            } else if (seconds < 172800) // 48 * 60 * 60
            {
                return "yesterday";
            } else if (seconds < 2592000) // 30 * 24 * 60 * 60
            {
                return days + " days ago";
            } else if (seconds < 31104000) // 12 * 30 * 24 * 60 * 60
            {

                return months <= 1 ? "one month ago" : days + " months ago";
            } else {

                return years <= 1 ? "one year ago" : years + " years ago";
            }
        }
        return null;
    }
}

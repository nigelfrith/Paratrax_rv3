//package com.altitude.paratrax.Firebase;
//
//import androidx.annotation.NonNull;
//
//import com.altitude.paratrax.Classes.Quick_Log;
//import com.google.firebase.database.DataSnapshot;
//import com.google.firebase.database.DatabaseError;
//import com.google.firebase.database.DatabaseReference;
//import com.google.firebase.database.FirebaseDatabase;
//import com.google.firebase.database.ValueEventListener;
//
//import java.util.ArrayList;
//import java.util.List;
//
//public class FirebaseDatabaseHelper {
//
//
//    private FirebaseDatabase mDatabase;
//    private DatabaseReference mReferenceLogs;
//
//    private List<Quick_Log> logs = new ArrayList<>();
//
//    public FirebaseDatabaseHelper() {
//
//        mDatabase = FirebaseDatabase.getInstance();
//        mReferenceLogs = mDatabase.getReference("logbooks");
//    }
//
//    public interface DataStatus{
//        void DataIsLoaded(List<Quick_Log> logs, List<String> keys);
//        void DataIsInserted();
//        void DataIsUpdated();
//        void DataIsDeleted();
//    }
//
//    public void readLogbooks(final DataStatus dataStatus) {
//
//        mReferenceLogs.addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                logs.clear();
//                List<String> keys = new ArrayList<>();
//                for (DataSnapshot keyNode : dataSnapshot.getChildren()){
//                    keys.add(keyNode.getKey());
//                    Quick_Log quick_log = keyNode.getValue(Quick_Log.class);
//                    logs.add(quick_log);
//                }
//                dataStatus.DataIsLoaded(logs, keys);
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError databaseError) {
//
//            }
//        });
//    }
//}

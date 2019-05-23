package com.muratakcam.scratch;

import android.app.Application;
import android.support.annotation.NonNull;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class CapsleApplication extends Application {
    public static String uname;

    @Override
    public void onCreate() {
        super.onCreate();
        if(FirebaseAuth.getInstance().getCurrentUser()!=null)
            setUname();

    }
    public static void setUname(){
            FirebaseDatabase.getInstance().
                    getReference().
                    child("Usernames").
                    addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange (@NonNull DataSnapshot dataSnapshot){
                            uname = dataSnapshot.child(FirebaseAuth.getInstance().getCurrentUser().getEmail().replace(".", ",")).getValue().toString();
                        }

                        @Override
                        public void onCancelled (@NonNull DatabaseError databaseError){

                        }
                    });
    }


}

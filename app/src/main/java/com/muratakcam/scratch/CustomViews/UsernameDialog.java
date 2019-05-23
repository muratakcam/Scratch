package com.muratakcam.scratch.CustomViews;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatDialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.muratakcam.scratch.Activities.MainActivity;
import com.muratakcam.scratch.R;

public class UsernameDialog extends AppCompatDialogFragment {

    private EditText usernameEdit;
    private TextView textView;
    private Context context;
    private Button button;
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        final AlertDialog.Builder builder= new AlertDialog.Builder(getActivity(), R.style.MyDialogTheme);
        LayoutInflater inflater=getActivity().getLayoutInflater();

        View view=inflater.inflate(R.layout.username_dialog,null);
        button=view.findViewById(R.id.button);
                button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseDatabase.getInstance().getReference().child("Usernames").addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if(usernameEdit.getText().length()>0
                                && usernameEdit.getText().length()<16
                                && !dataSnapshot.hasChild(usernameEdit.getText().toString()))
                        {
                            FirebaseDatabase.getInstance().getReference().child("Usernames")
                                    .child(FirebaseAuth.getInstance().getCurrentUser().getEmail().replace(".",","))
                                    .setValue(usernameEdit.getText().toString());
                            FirebaseDatabase.getInstance().getReference().child("Usernames")
                                    .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                                    .setValue(System.currentTimeMillis());
                            context.startActivity(new Intent(context, MainActivity.class));

                        }else{
                            textView.setText("Bu kullanıcı adı kullanılmaktadır.");
                        }


                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
            }
        });
        usernameEdit=view.findViewById(R.id.username_edit);
        textView=view.findViewById(R.id.textView);
        builder.setView(view)
                .setTitle("Kullanıcı Adı Seçin");

        return builder.create();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.context=context;

    }
}
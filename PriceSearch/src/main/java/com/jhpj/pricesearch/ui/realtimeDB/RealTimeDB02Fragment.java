package com.jhpj.pricesearch.ui.realtimeDB;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.jhpj.pricesearch.databinding.FragmentRealtimedb2Binding;

import java.util.HashMap;

public class RealTimeDB02Fragment extends Fragment {

    private FragmentRealtimedb2Binding binding;
    private DatabaseReference mdataref;
    private Button btn_save, btn_road;
    private EditText edt_name, edt_email, edt_age, edt_id;
    private TextView txt_data;
    private int i = 1;

    private Toast toast;

    private final String TAG = this.getClass().getSimpleName();

    @Override

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentRealtimedb2Binding.inflate(inflater, container, false);
        View root = binding.getRoot();

        mdataref = FirebaseDatabase.getInstance().getReference();

        edt_name = binding.edtName;
        edt_email = binding.edtEmail;
        edt_age = binding.edtAge;
        btn_save = binding.btnSave;
        edt_id = binding.edtId;
        btn_road = binding.btnRoad;

        return root;
    }

    @Override
    public void onStart() {
        super.onStart();

        btn_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String getUserName = edt_name.getText().toString();
                String getUserEmail = edt_email.getText().toString();
                String getUserAge = edt_age.getText().toString();

                HashMap result = new HashMap<>();
                result.put("name", getUserName);
                result.put("email", getUserEmail);
                result.put("age", getUserAge);

                writeUser(Integer.toString(i++), getUserName, getUserEmail, getUserAge);

                edt_name.setText("");
                edt_email.setText("");
                edt_age.setText("");

                // 키보드 내림
                edt_name.clearFocus();
                edt_email.clearFocus();
                edt_age.clearFocus();
            }
        });

        btn_road.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                readUser(edt_id.getText().toString());
            }
        });
    }

    private void writeUser(String userid, String name, String email, String age) {
        User user = new User(name, email, age);

        // 데이터 저장
        mdataref.child("users").child(userid).setValue(user).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                toast = Toast.makeText(getContext(), "실시간 DB정보를 저장했습니다.", Toast.LENGTH_SHORT);
                toast.show();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

            }
        });
    }

    private void readUser(String userid) {
        mdataref.child("users").child(userid).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Log.d(TAG, "snapshot : " + String.valueOf(snapshot.getValue().toString()));
                User user = snapshot.getValue(User.class);
                txt_data.setText("이름 : " + user.name + "#13#10" + "이메일 : " + user.email + "#13#10"+ "나이 : " + user.age + "#13#10");
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                toast = Toast.makeText(getContext(), "실시간 DB정보를 가져오는데 실패하였습니다.", Toast.LENGTH_SHORT);
                toast.show();
            }
        });
    }
}
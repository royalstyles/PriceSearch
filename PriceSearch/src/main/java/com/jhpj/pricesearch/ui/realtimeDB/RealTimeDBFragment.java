package com.jhpj.pricesearch.ui.realtimeDB;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.jhpj.pricesearch.databinding.FragmentRealtimedbBinding;

public class RealTimeDBFragment extends Fragment {

    private FragmentRealtimedbBinding binding;
    DatabaseReference mdataref = FirebaseDatabase.getInstance().getReference();
    DatabaseReference conditionref = mdataref.child("Data");

    private TextView txt_showview;
    private EditText edt_inputtext;
    private Button btn_send;

    private Toast toast;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        RealTimeDBViewModel realTimeDBViewModel =
                new ViewModelProvider(this).get(RealTimeDBViewModel.class);

        binding = FragmentRealtimedbBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        final TextView textView = binding.txtMainview;
        realTimeDBViewModel.getText().observe(getViewLifecycleOwner(), textView::setText);

        txt_showview = binding.txtShowview;
        edt_inputtext = binding.edtInputtext;
        btn_send = binding.btnSend;

        return root;
    }

    @Override
    public void onStart() {
        super.onStart();

        conditionref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (txt_showview.getText().equals("")) {
                    toast = Toast.makeText(getContext(), "실시간 DB정보를 불러왔습니다.", Toast.LENGTH_SHORT);
                } else {
                    toast = Toast.makeText(getContext(), "DB가 변경되었습니다.", Toast.LENGTH_SHORT);
                }
                toast.show();


                String text = snapshot.getValue(String.class);
                txt_showview.setText(text);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        btn_send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                conditionref.setValue(edt_inputtext.getText().toString());
                edt_inputtext.setText("");
            }
        });

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
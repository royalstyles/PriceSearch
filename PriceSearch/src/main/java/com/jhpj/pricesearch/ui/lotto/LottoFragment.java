package com.jhpj.pricesearch.ui.lotto;

import static com.jhpj.pricesearch.R.drawable.circle_blue;
import static com.jhpj.pricesearch.R.drawable.circle_gray;
import static com.jhpj.pricesearch.R.drawable.circle_green;
import static com.jhpj.pricesearch.R.drawable.circle_red;
import static com.jhpj.pricesearch.R.drawable.circle_yellow;

import android.os.Build;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.NumberPicker;
import android.widget.TextView;
import android.widget.Toast;

import com.jhpj.pricesearch.databinding.FragmentLottoBinding;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;

public class LottoFragment extends Fragment {

    private FragmentLottoBinding binding;

    private NumberPicker numberPicker;
    private List<TextView> numberTextViewList;
    private TextView lottonum1;
    private TextView lottonum2;
    private TextView lottonum3;
    private TextView lottonum4;
    private TextView lottonum5;
    private TextView lottonum6;

    private boolean didRun = false;
    private HashSet<Integer> lottoSet = new HashSet<>();
    private HashSet<Integer> lottoSetSave = new HashSet<>();
    private ArrayList<Integer> arrList = new ArrayList<>();


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        binding = FragmentLottoBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        initRunButton();
        initAddButton();
        initClearButton();
        return root;
    }

    private void initRunButton() {
        lottonum1 = binding.lottonum1;
        lottonum2 = binding.lottonum2;
        lottonum3 = binding.lottonum3;
        lottonum4 = binding.lottonum4;
        lottonum5 = binding.lottonum5;
        lottonum6 = binding.lottonum6;

        Button runButton = binding.runButton;
        runButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                didRun = true;

                // 초기화 하지 않으면 번호추가하기했던 리스트를 따로 저장해 놓는다.
                if (lottoSetSave.size() > 0) {
                    lottoSet.addAll(lottoSetSave);
                }

                while (lottoSet.size() < 6) {
                    int num = (int) (Math.random() * 45);
                    if (num != 0) {
                        Log.d("Lotto : ", String.valueOf(num));
                        lottoSet.add(Integer.valueOf(num + ""));
                    }
                }

                arrList.addAll(lottoSet);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                    // 오름차순 정렬
                    arrList.sort(Comparator.naturalOrder());
                }

                int index = 1;
                for (int a : arrList) {
                    Log.d("Lotto 출력 : ", String.valueOf(a));
                    if (index == 1) {
                        lottonum1.setText(String.valueOf(a));
                        lottonum1.setVisibility(View.VISIBLE);
                        setNumberBackground(a, lottonum1);
                    } else if (index == 2) {
                        lottonum2.setText(String.valueOf(a));
                        lottonum2.setVisibility(View.VISIBLE);
                        setNumberBackground(a, lottonum2);
                    } else if (index == 3) {
                        lottonum3.setText(String.valueOf(a));
                        lottonum3.setVisibility(View.VISIBLE);
                        setNumberBackground(a, lottonum3);
                    } else if (index == 4) {
                        lottonum4.setText(String.valueOf(a));
                        lottonum4.setVisibility(View.VISIBLE);
                        setNumberBackground(a, lottonum4);
                    } else if (index == 5) {
                        lottonum5.setText(String.valueOf(a));
                        lottonum5.setVisibility(View.VISIBLE);
                        setNumberBackground(a, lottonum5);
                    } else if (index == 6) {
                        lottonum6.setText(String.valueOf(a));
                        lottonum6.setVisibility(View.VISIBLE);
                        setNumberBackground(a, lottonum6);
                    }
                    index++;
                }
                lottoSet.clear();
                arrList.clear();
            }
        });
    }

    private void initAddButton() {
        numberPicker = binding.numberPicker;
        numberPicker.setMinValue(1);
        numberPicker.setMaxValue(45);

        Button addButton = binding.addButton;
        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("didRun : ", String.valueOf(didRun));
                Log.d("lottoSet.size() : ", String.valueOf(lottoSet.size()));
                if (didRun) {
                    Toast.makeText(getContext(), "초기화 후에 시도해주세요.", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (lottoSet.contains(numberPicker.getValue())) {
                    Toast.makeText(getContext(), "이미 선택된 번호 입니다.", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (lottoSet.size() > 4) {
                    Toast.makeText(getContext(), "번호는 5개까지 선택할 수 있습니다.", Toast.LENGTH_SHORT).show();
                    return;
                }

                lottoSet.add(numberPicker.getValue());
                lottoSetSave.add(numberPicker.getValue());
                switch (lottoSet.size()) {
                    case 1:
                        lottonum1.setText(String.valueOf(numberPicker.getValue()));
                        lottonum1.setVisibility(View.VISIBLE);
                        setNumberBackground(numberPicker.getValue(), lottonum1);
                        break;
                    case 2:
                        lottonum2.setText(String.valueOf(numberPicker.getValue()));
                        lottonum2.setVisibility(View.VISIBLE);
                        setNumberBackground(numberPicker.getValue(), lottonum2);
                        break;
                    case 3:
                        lottonum3.setText(String.valueOf(numberPicker.getValue()));
                        lottonum3.setVisibility(View.VISIBLE);
                        setNumberBackground(numberPicker.getValue(), lottonum3);
                        break;
                    case 4:
                        lottonum4.setText(String.valueOf(numberPicker.getValue()));
                        lottonum4.setVisibility(View.VISIBLE);
                        setNumberBackground(numberPicker.getValue(), lottonum4);
                        break;
                    case 5:
                        lottonum5.setText(String.valueOf(numberPicker.getValue()));
                        lottonum5.setVisibility(View.VISIBLE);
                        setNumberBackground(numberPicker.getValue(), lottonum5);
                        break;
                    case 6:
                        lottonum6.setText(String.valueOf(numberPicker.getValue()));
                        lottonum6.setVisibility(View.VISIBLE);
                        setNumberBackground(numberPicker.getValue(), lottonum6);
                        break;
                }
            }
        });
    }

    private void initClearButton() {

        Button clearButton = binding.clearButton;
        clearButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                didRun = false;
                lottonum1.setVisibility(View.GONE);
                lottonum2.setVisibility(View.GONE);
                lottonum3.setVisibility(View.GONE);
                lottonum4.setVisibility(View.GONE);
                lottonum5.setVisibility(View.GONE);
                lottonum6.setVisibility(View.GONE);
                lottoSet.clear();
                lottoSetSave.clear();
            }
        });
    }

    private void setNumberBackground(Integer number, TextView lottonum) {
        if (1 <= number && number <= 10) {
            lottonum.setBackgroundResource(circle_yellow);
        } else if (11 <= number && number <= 20) {
            lottonum.setBackgroundResource(circle_blue);
        } else if (21 <= number && number <= 30) {
            lottonum.setBackgroundResource(circle_red);
        } else if (31 <= number && number <= 40) {
            lottonum.setBackgroundResource(circle_gray);
        } else if (41 <= number && number <= 45) {
            lottonum.setBackgroundResource(circle_green);
        }
    }
}
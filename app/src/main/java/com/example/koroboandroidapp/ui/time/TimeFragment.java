package com.example.koroboandroidapp.ui.time;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import android.widget.EditText;
import android.os.CountDownTimer;

import android.os.Vibrator;
import android.widget.Toast;
import android.content.Context;
import com.example.koroboandroidapp.R;
//import com.example.koroboandroidapp.ui.dashboard.DashboardViewModel;

public class TimeFragment extends Fragment {
    private TimeViewModel TimeViewModel;
    int start =0;
    int inputTime;
    int o=10;
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_time, container, false);


        return root;
    }
    @Override
    public void onStart() {
        super.onStart();
        final String START_MESSAGE = "START";
        final String STOP_MESSAGE = "STOP";
        final String FINISH_MESSAGE = "カウント終了";
        Button StartButton = (Button)getActivity().findViewById(R.id.StartButton);
        Button StopButton = (Button)getActivity().findViewById(R.id.StopButton);
        final EditText editTime = (EditText)getActivity().findViewById(R.id.editTime);
        final CountDownTimer[] cdt = new CountDownTimer[1];
        //Button button = (Button)getActivity().findViewById(R.id.StartButton);
        //button.setOnClickListener(new View.OnClickListener() {
        if(start==1){
            o++;
            TextView text = (TextView)getActivity().findViewById(R.id.textView_time);
            Integer t = Integer.valueOf(o);
            text.setText(t.toString());
        }

        // スタートボタンイベントリスナー
        StartButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    // スタートトースト
                    //Toast toast = Toast.makeText(v.getContext(), START_MESSAGE, Toast.LENGTH_SHORT);
                    //toast.show();

                    // 入力時間取得
                    inputTime = Integer.parseInt(String.valueOf(editTime.getText()));

                    TextView text = (TextView)getActivity().findViewById(R.id.textView_time);
                    Integer t = Integer.valueOf(o);
                    text.setText(t.toString());
                    start=start+1;


                }
            });

            // ストップボタンイベントリスナー
        StopButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    // ストップトースト
                    Toast toast = Toast.makeText(v.getContext(), STOP_MESSAGE, Toast.LENGTH_SHORT);
                    toast.show();

                    // カウントダウンタイマー停止
                    cdt[0].cancel();
                }
            });

    }
}

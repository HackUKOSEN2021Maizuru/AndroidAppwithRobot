package com.example.koroboandroidapp;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.annotation.NonNull;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.app.Activity;
import androidx.appcompat.app.AppCompatActivity;
import java.util.Locale;
import java.util.Timer;
import java.util.TimerTask;
import android.os.Handler;
import android.os.Looper;


public class Fragment01 extends Fragment {

    private int cnt = 0;
    private Timer timer;
    private CountUpTimerTask timerTask;
    // 'Handler()' is deprecated as of API 30: Android 11.0 (R)
    private final Handler handler = new Handler(Looper.getMainLooper());

    private TextView timerText;
    private long count, delay, period;
    private String zero;
    static Fragment01 newInstance(int count){
        // Fragemnt01 インスタンス生成
        Fragment01 fragment01 = new Fragment01();

        // Bundle にパラメータを設定
        Bundle args = new Bundle();
        args.putInt("Counter", count);
        fragment01.setArguments(args);

        return fragment01;
    }

    // FragmentのViewを生成して返す
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment01,
                container, false);
    }




    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        Bundle args = getArguments();

        if(args != null ){
            int count = args.getInt("Counter");

            cnt = count +1;
            String str = "Fragment01: " + String.valueOf(cnt);

            TextView textView = view.findViewById(R.id.textview_01);
            textView.setText(str);
        }
        delay = 0;
        period = 100;
        // "00:00.0"
        zero = getString(R.string.zero);

        timerText = getActivity().findViewById(R.id.textView);
        timerText.setText(zero);

        Button startButton = getActivity().findViewById(R.id.start_button);
        startButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // タイマーが走っている最中にボタンをタップされたケース
                if(null != timer){
                    timer.cancel();
                    timer = null;
                }

                // Timer インスタンスを生成
                timer = new Timer();

                // TimerTask インスタンスを生成
                timerTask = new CountUpTimerTask();

                // スケジュールを設定 100msec
                // public void schedule (TimerTask task, long delay, long period)
                timer.schedule(timerTask, delay, period);

                // カウンター
                count = 0;
                timerText.setText(zero);

            }
        });

        // タイマー終了
        Button stopButton = getActivity().findViewById(R.id.stop_button);
        stopButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                // timer がnullでない、起動しているときのみcancleする
                if(null != timer){
                    // Cancel
                    timer.cancel();
                    timer = null;
                    timerText.setText(zero);
                }
            }
        });

        Button button01 = view.findViewById(R.id.button_01);
        button01.setOnClickListener( v -> {
            FragmentManager fragmentManager = getFragmentManager();

            if(fragmentManager != null){
                FragmentTransaction fragmentTransaction =
                        fragmentManager.beginTransaction();
                // BackStackを設定
                fragmentTransaction.addToBackStack(null);

                fragmentTransaction.replace(R.id.container,
                        Fragment02.newInstance(cnt));
                fragmentTransaction.commit();
            }

        });

        // BackStackで１つ戻す
        Button pop01 = view.findViewById(R.id.pop_01);
        pop01.setOnClickListener( v -> {
            FragmentManager fragmentManager = getFragmentManager();
            if(fragmentManager != null) {
                fragmentManager.popBackStack();
            }
        });
    }
    class CountUpTimerTask extends TimerTask {
        @Override
        public void run() {
            // handlerを使って処理をキューイングする
            handler.post(new Runnable() {
                public void run() {
                    cnt++;
                    long mm = cnt*100 / 1000 / 60;
                    long ss = cnt*100 / 1000 % 60;
                    long ms = (cnt*100 - ss * 1000 - mm * 1000 * 60)/100;
                    // 桁数を合わせるために02d(2桁)を設定
                    timerText.setText(
                            String.format(Locale.US, "%1$02d:%2$02d.%3$01d", mm, ss, ms));

                }
            });
        }
    }


}
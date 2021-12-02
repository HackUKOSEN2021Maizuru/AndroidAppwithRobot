package com.example.koroboandroidapp.ui.time;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.annotation.NonNull;

import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.app.Activity;
import androidx.appcompat.app.AppCompatActivity;

import java.lang.ref.WeakReference;
import java.util.Locale;
import java.util.Timer;
import java.util.TimerTask;
import android.os.Handler;
import android.os.Looper;
import com.example.koroboandroidapp.R;
import com.example.koroboandroidapp.db.AppDatabase;
import com.example.koroboandroidapp.db.AppDatabaseSingleton;
import com.example.koroboandroidapp.db.LogDao;
import com.example.koroboandroidapp.db.Log;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import java.security.Timestamp;
import java.util.ArrayList;

import androidx.appcompat.app.AppCompatActivity;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.lang.ref.WeakReference;
import java.util.List;
//import com.example.koroboandroidapp.ui.dashboard.DashboardViewModel;



public class TimeFragment extends Fragment {
    private int cnt = 0;
    private Timer timer;
    private CountUpTimerTask timerTask;
    // 'Handler()' is deprecated as of API 30: Android 11.0 (R)
    private final Handler handler = new Handler(Looper.getMainLooper());

    private TextView timerText;
    private TextView logText;
    private TextView log;
    private long count, delay, period;
    private String zero;
    private WeakReference<Activity> weakActivity;
    private AppDatabase db;

    static TimeFragment newInstance(int count){
        // Fragemnt01 インスタンス生成
        TimeFragment fragment01 = new TimeFragment();

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
        return inflater.inflate(R.layout.fragment_time,
                container, false);
    }


    public String getTime(long time){
        long mm = count*100 / 1000 / 60;
        long ss = count*100 / 1000 % 60;
        long ms = (count*100 - ss * 1000 - mm * 1000 * 60)/100;

        return String.format(Locale.US, "%1$02d:%2$02d.%3$01d", mm, ss, ms);
    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        db = AppDatabaseSingleton.getInstance(null);
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
        timerText = getActivity().findViewById(R.id.TimeText);
        timerText.setText(zero);
        logText = getActivity().findViewById(R.id.LogText);
        new DataStoreAsyncTask(db, getActivity(), logText,null).execute();




        Button startButton = getActivity().findViewById(R.id.StartButton);
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
                timerTask = new TimeFragment.CountUpTimerTask();

                // スケジュールを設定 100msec
                // public void schedule (TimerTask task, long delay, long period)
                timer.schedule(timerTask, delay, period);

                // カウンター
                count = 0;
                timerText.setText(zero);

            }
        });

        // タイマー終了
        Button stopButton = getActivity().findViewById(R.id.StopButton);
        stopButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                // timer がnullでない、起動しているときのみcancleする
                if(null != timer){

                    new DataStoreAsyncTask(db, getActivity(), logText,getTime(count)).execute();

                    // Cancel
                    timer.cancel();
                    timer = null;
                    timerText.setText(zero);
                    count = 0;


                }
            }
        });



        // BackStackで１つ戻す
        /*
        Button pop01 = view.findViewById(R.id.pop_01);
        pop01.setOnClickListener( v -> {
            FragmentManager fragmentManager = getFragmentManager();
            if(fragmentManager != null) {
                fragmentManager.popBackStack();
            }
        });
        */
    }




    class CountUpTimerTask extends TimerTask {
        @Override
        public void run() {
            // handlerを使って処理をキューイングする
            handler.post(new Runnable() {
                public void run() {
                    count++;
                    // 桁数を合わせるために02d(2桁)を設定
                    timerText.setText(getTime(count));


                }
            });
        }
    }
    private static class DataStoreAsyncTask extends AsyncTask<Void, Void, Integer> {
        private WeakReference<Activity> weakActivity;
        private AppDatabase db;
        private TextView textView;
        private StringBuilder sb;
        String s;


        public DataStoreAsyncTask(AppDatabase db, Activity activity, TextView textView,String s) {
            this.db = db;
            weakActivity = new WeakReference<>(activity);
            this.textView = textView;
            this.s=s;
        }

        @Override
        protected Integer doInBackground(Void... params) {
            LogDao logDao = db.LogDao();

            if(s!=null){
                logDao.insert(new Log(s));
            }
            sb = new StringBuilder();
            List<Log> atList = logDao.getAll();
            for (Log at: atList) {
                sb.append(at.getLog()).append("\n");
            }


            return 0;
        }

        @Override
        protected void onPostExecute(Integer code) {
            Activity activity = weakActivity.get();
            if(activity == null) {
                return;
            }

            textView.setText(sb.toString());

        }
    }


}

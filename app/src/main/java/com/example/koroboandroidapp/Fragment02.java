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

public class Fragment02 extends Fragment {

    private int cnt = 0;

    static Fragment02 newInstance(int count){
        // Fragemnt02 インスタンス生成
        Fragment02 fragment02 = new Fragment02();

        // Bundleにパラメータを設定
        Bundle barg = new Bundle();
        barg.putInt("Counter", count);
        fragment02.setArguments(barg);

        return fragment02;
    }

    // FragmentのViewを生成して返す
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container,
                             Bundle savedInstanceState) {

        return inflater.inflate(R.layout.fragment02,
                container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        Bundle args = getArguments();

        if(args != null ){
            int count = args.getInt("Counter");
            String str = "Fragment02: " + String.valueOf(count);
            cnt = count +1;

            TextView textView = view.findViewById(R.id.textview_02);
            textView.setText(str);
        }

        Button button02 = view.findViewById(R.id.button_02);
        button02.setOnClickListener( v -> {
            FragmentManager fragmentManager = getFragmentManager();
            if(fragmentManager != null) {
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

                // BackStackを設定
                fragmentTransaction.addToBackStack(null);

                fragmentTransaction.replace(R.id.container, Fragment01.newInstance(cnt));
                fragmentTransaction.commit();
            }
        });

        // BackStackで１つ戻す
        Button pop02 = view.findViewById(R.id.pop_02);
        pop02.setOnClickListener( v -> {
            FragmentManager fragmentManager = getFragmentManager();
            if(fragmentManager != null) {
                fragmentManager.popBackStack();
            }
        });
    }
}
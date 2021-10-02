package com.example.myandroidexperiment2;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.myandroidexperiment2.Model.Word;


public class DetailFragment extends Fragment {

    MainActivity activity;
    private TextView wordView;
    private TextView meaningView;
    private TextView sampleView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_detail, container, false);
        wordView = view.findViewById(R.id.word);
        meaningView = view.findViewById(R.id.meaning);
        sampleView = view.findViewById(R.id.sample);


        activity = (MainActivity) getActivity();
        activity.setOnChangeListener(new MainActivity.OnChangeListener()
        {
            @Override
            public void changeText(Word word)
            {
                wordView.setText(word.getWord());
                meaningView.setText(word.getMeaning());
                sampleView.setText(word.getSample());
            }
        });
        return view;
    }


}
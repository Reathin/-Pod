package com.rair.pod.fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.rair.pod.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class StartFragment extends Fragment {


    public static StartFragment newInstance() {
        return new StartFragment();
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_start, container, false);
    }

}

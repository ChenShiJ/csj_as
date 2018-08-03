package com.hzyc.csj.ordermealsystem.fragment;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.hzyc.csj.oedermealsystem.R;
import com.hzyc.csj.ordermealsystem.Main2Activity;


public class GuideThreeFragment extends Fragment {

    private ImageView imageView;

    public GuideThreeFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_guide_three, container, false);
        imageView = (ImageView) view.findViewById(R.id.start);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(),Main2Activity.class);
                startActivity(intent);
            }
        });
        return view;
    }

}

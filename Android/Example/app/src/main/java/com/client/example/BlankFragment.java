package com.client.example;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;
import java.util.Map;

import cloud.artik.client.ApiCallback;
import cloud.artik.client.ApiException;
import cloud.artik.model.NormalizedMessagesEnvelope;


/**
 * A simple {@link Fragment} subclass.
 */
public class BlankFragment extends Fragment {
public static boolean stats=false;



    public BlankFragment() {
        // Required empty public constructor

    }

public static ImageView slot1,slot2,slot3,slot4,slot5,slot6,slot7,slot8,slot9,slot10,slot11,slot12;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {




      //OBJECT of views are created by inflating in fragments
        // Inflate the layout for this fragment

        View view = inflater.inflate(R.layout.fragment_blank, container, false);
        slot1=(ImageView)view.findViewById(R.id.imageView1);
        slot2=(ImageView)view.findViewById(R.id.imageView2);
        slot3=(ImageView)view.findViewById(R.id.imageView3);
        slot4=(ImageView)view.findViewById(R.id.imageView4);
        slot5=(ImageView)view.findViewById(R.id.imageView5);
        slot6=(ImageView)view.findViewById(R.id.imageView6);
        slot7=(ImageView)view.findViewById(R.id.imageView7);
        slot8=(ImageView)view.findViewById(R.id.imageView8);
        slot9=(ImageView)view.findViewById(R.id.imageView9);
        slot10=(ImageView)view.findViewById(R.id.imageView10);
        slot11=(ImageView)view.findViewById(R.id.imageView11);
        slot12=(ImageView)view.findViewById(R.id.imageView12);
        if(stats==false) {
            //TELLS THE USER THAT DEVICE IS NOT CONNECTED
            Toast.makeText(getActivity(), "Device not online",
                    Toast.LENGTH_LONG).show();
        }



        my_activity.park_check();//TELLS THE APP THAT NOW IT CAN UPDATE UI BECAUSE OBJECT IS NOW CREATED
        //NOW whenever the message is received the UI WILL UPDATE


        return view;
    }
    @Override
    public void onDestroyView() {
        super.onDestroyView();


    }



}

package com.client.example;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;


/**
 * A simple {@link Fragment} subclass.
 */
public class temperature extends Fragment {
public static TextView temp0,temp1,temp2,temp3,temp4;
    public static boolean stats=false;
    public temperature() {
        // Required empty public constructor
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        //OBJECT of views are created by inflating in fragments
         // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_temperature, container, false);
        temp0=(TextView)view.findViewById(R.id.t0);
        temp1=(TextView)view.findViewById(R.id.t4);
        temp2=(TextView)view.findViewById(R.id.t3);
        temp3=(TextView)view.findViewById(R.id.t2);
        temp4=(TextView)view.findViewById(R.id.t5);
        if(stats==false) {//TELLS THE USER THAT DEVICE IS NOT CONNECTED
          Toast.makeText(getActivity(), "Device not online",
                   Toast.LENGTH_LONG).show();
        }

        my_activity.temp_check();//TELLS THE APP THAT NOW IT CAN UPDATE UI BECAUSE OBJECT IS NOW CREATED
        //NOW whenever the message is received the UI WILL UPDATE

        return view;
    }

}

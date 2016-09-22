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
public class waterlevel extends Fragment {
    public static boolean stats=false;
    public static ImageView level1,level2,level3;
    public static TextView tank_text;
    public waterlevel() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        //OBJECT of views are created by inflating in fragments
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_waterlevel, container, false);
        level1=(ImageView)view.findViewById(R.id.lvl1);
        level2=(ImageView)view.findViewById(R.id.lvl2);
        level3=(ImageView)view.findViewById(R.id.lvl3);
        tank_text=(TextView)view.findViewById(R.id.tank_text);


        if(stats==false) {//TELLS THE USER THAT DEVICE IS NOT CONNECTED
            Toast.makeText(getActivity(), "Device not online",
                    Toast.LENGTH_LONG).show();
        }
        my_activity.water_check();//TELLS THE APP THAT NOW IT CAN UPDATE UI BECAUSE OBJECT IS NOW CREATED
        //NOW whenever the message is received the UI WILL UPDATE


        return view;
    }

}

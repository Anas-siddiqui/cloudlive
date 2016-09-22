package com.client.example;


import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.Toast;
import android.widget.ToggleButton;


/**
 * A simple {@link Fragment} subclass.
 */
public class control extends Fragment {
    public static boolean stats=false;
    public static ToggleButton motor_tg,gate_tg;
    public static ImageView motor_red,motor_green,gate_red,gate_green;
    public static String  motor_value;
    public static String  gate_value;
    public static int a=0;
    public static int b=0;

    public control() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
//OBJECT of views are created by inflating in fragments
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_control, container, false);
        motor_red=(ImageView)view.findViewById(R.id.motor_red);
        motor_green=(ImageView)view.findViewById(R.id.motor_green);
        gate_red=(ImageView)view.findViewById(R.id.gate_red);
        gate_green=(ImageView)view.findViewById(R.id.gate_green);
       motor_tg=(ToggleButton)view.findViewById(R.id.toggleButton);
        gate_tg=(ToggleButton)view.findViewById(R.id.toggleButton2);

        motor_tg.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked)
                {
                    //LOGIC TO SEND ACTIONS DEPENDING ON THE SITUATION FROM TOGGLEBUTTON

                    if(stats==true) {
                        if(motor_value.equals("false")) {
                            ArtikCloudSession.getInstance().send_action_motor_on();
                            Toast.makeText(getActivity(), "Please wait.....",
                                    Toast.LENGTH_LONG).show();
                            a=1;
                            motor_tg.setEnabled(false);
                        }
                    }
                    else

                    {
                        Toast.makeText(getActivity(), "Device not online",
                            Toast.LENGTH_LONG).show();}


                }
                else
                {
                    if(stats==true) {
                        if(motor_value.equals("true")) {
                            ArtikCloudSession.getInstance().send_action_motor_off();
                            Toast.makeText(getActivity(), "Please wait.....",
                                    Toast.LENGTH_LONG).show();
                            a=1;
                            motor_tg.setEnabled(false);
                        }
                    }
                    else
                    { Toast.makeText(getActivity(), "Device not online",
                            Toast.LENGTH_LONG).show();}




                }

            }
        });
        gate_tg.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                //LOGIC TO SEND ACTIONS DEPENDING ON THE SITUATION FROM TOGGLEBUTTON
                if(isChecked)
                { if(stats==true) {
                    if (gate_value.equals("false")) {
                        ArtikCloudSession.getInstance().send_action_gate_on();
                        Toast.makeText(getActivity(), "Please wait.....",
                                Toast.LENGTH_LONG).show();
                        b = 1;
                        gate_tg.setEnabled(false);
                    }

                }
                else
                { Toast.makeText(getActivity(), "Device not online",
                        Toast.LENGTH_LONG).show();}




                }
                else
                {
                    if(stats==true) {
                    if(gate_value.equals("true")) {
                        ArtikCloudSession.getInstance().send_action_gate_off();
                        Toast.makeText(getActivity(), "Please wait.....",
                                Toast.LENGTH_LONG).show();
                        b = 1;
                        gate_tg.setEnabled(false);
                    }
                }
                else
                { Toast.makeText(getActivity(), "Device not online",
                        Toast.LENGTH_LONG).show();}


                }

            }
        });




        if(stats==false) {
            //TELLS THE USER THAT DEVICE IS NOT CONNECTED
            Toast.makeText(getActivity(), "Device not online",
                    Toast.LENGTH_LONG).show();
        }
     //TELLS THE APP THAT NOW IT CAN UPDATE UI BECAUSE OBJECT IS NOW CREATED
        //NOW whenever the message is received the UI WILL UPDATE

        my_activity.control_check();
        return view;
    }


}

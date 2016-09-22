package com.client.example;

import android.app.Activity;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.support.v4.app.FragmentManager;
import android.support.design.widget.NavigationView;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import cloud.artik.api.MessagesApi;
import cloud.artik.api.UsersApi;
import cloud.artik.client.ApiException;


public class my_activity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    private static final String TAG = "MessageActivity";
    public static String final_data="No Data";


    public static final String KEY_ACCESS_TOKEN = "";
    //Paste your ACCESS TOKEN above



    public static boolean parker=false;
    public static boolean temper=false;
    public static boolean water=false;
    public static boolean control=false;
    String final_result[];

    public static boolean  park_notify_settings,set_motor,set_gate;
    boolean checker=false;
    boolean notifier=false;
    public static int set_motor_value_min,set_motor_value_max;

   //
    private static final String WS_HEADER = "WebSocket /websocket: ";
    private static final String LIVE_HEADER = "WebSocket /live: ";
    private static final String DEVICE_REGISTERED = "device registered ";
    private static final String CONNECTED = "connected ";














    @Override
    protected void onCreate(Bundle savedInstanceState) {


        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        PreferenceManager.setDefaultValues(this, R.xml.prefrences, false);
        loadPref();
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);




        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
     //NAVIGATION CONTROLS END HERE

        ArtikCloudSession.getInstance().setContext(this);

    }
    @Override
    protected void onResume() {
        super.onResume();

        LocalBroadcastManager.getInstance(this).registerReceiver(mWSUpdateReceiver,
                makeWebsocketUpdateIntentFilter());

        ArtikCloudSession.getInstance().connectFirehoseWS();//non blocking

        ArtikCloudSession.getInstance().connectDeviceChannelWS();//non blocking

    }
    @Override
    protected void onPause() {
        super.onPause();

    }


    private static IntentFilter makeWebsocketUpdateIntentFilter() {//REGISTERING RECEIVERS TO HANDLE EVENTS
        final IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(ArtikCloudSession.WEBSOCKET_LIVE_ONOPEN);
        intentFilter.addAction(ArtikCloudSession.WEBSOCKET_LIVE_ONMSG);
        intentFilter.addAction(ArtikCloudSession.WEBSOCKET_LIVE_ONCLOSE);
        intentFilter.addAction(ArtikCloudSession.WEBSOCKET_LIVE_ONERROR);
        intentFilter.addAction(ArtikCloudSession.WEBSOCKET_WS_ONOPEN);
        intentFilter.addAction(ArtikCloudSession.WEBSOCKET_WS_ONREG);
        intentFilter.addAction(ArtikCloudSession.WEBSOCKET_WS_ONMSG);
        intentFilter.addAction(ArtikCloudSession.WEBSOCKET_WS_ONACK);
        intentFilter.addAction(ArtikCloudSession.WEBSOCKET_WS_ONCLOSE);
        intentFilter.addAction(ArtikCloudSession.WEBSOCKET_WS_ONERROR);
        return intentFilter;
    }
    private final BroadcastReceiver mWSUpdateReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            final String action = intent.getAction();
            if (ArtikCloudSession.WEBSOCKET_LIVE_ONOPEN.equals(action)) {
                displayLiveStatus(LIVE_HEADER + CONNECTED);
            } else if (ArtikCloudSession.WEBSOCKET_LIVE_ONMSG.equals(action)) {
                String status = intent.getStringExtra(ArtikCloudSession.DEVICE_DATA);
                String updateTime = intent.getStringExtra(ArtikCloudSession.TIMESTEP);
                displayDeviceStatus(status, updateTime);
                //MOST IMPORTANT FUNCTION AS IT WILL BE TRIGGERED EVERYTIME WHEN MESSAGE ARRIVES
                //USING THIS TO CHANGE INTERFACE



            }

            else if (ArtikCloudSession.WEBSOCKET_LIVE_ONCLOSE.equals(action) ||
                    ArtikCloudSession.WEBSOCKET_LIVE_ONERROR.equals(action)) {
                displayLiveStatus(LIVE_HEADER + intent.getStringExtra(ArtikCloudSession.ERROR));
            } else  if (ArtikCloudSession.WEBSOCKET_WS_ONOPEN.equals(action)) {
                displayWSStatus(WS_HEADER + CONNECTED);
            } else  if (ArtikCloudSession.WEBSOCKET_WS_ONREG.equals(action)) {
                displayWSStatus(WS_HEADER + DEVICE_REGISTERED);
            } else if (ArtikCloudSession.WEBSOCKET_WS_ONMSG.equals(action)) {
                displayWSReceived(intent.getStringExtra("msg"));
            } else if (ArtikCloudSession.WEBSOCKET_WS_ONACK.equals(action)) {
                displayWSReceived(intent.getStringExtra(ArtikCloudSession.ACK));
            } else if (ArtikCloudSession.WEBSOCKET_WS_ONCLOSE.equals(action) ||
                    ArtikCloudSession.WEBSOCKET_WS_ONERROR.equals(action)) {
                displayWSStatus(WS_HEADER + intent.getStringExtra(ArtikCloudSession.ERROR));
                msg("closing");
            }
            //OTHER EVENTS WILL BE NECESSARY LATER

        }
    };

    private void displayLiveStatus(String status) {
        Log.d(TAG, status);

    }

    private void displayDeviceStatus(String status, String updateTimems) {
        //THIS FUNCTION IS CALLED AFTER ONMSG

        BlankFragment.stats=true;
        temperature.stats=true;
        waterlevel.stats=true;
        com.client.example.control.stats=true;
        //THESE VARIABLES tells that we are connected to device otherwise
        // if we navigate to the other page it wont update so I will alert user





        final_data=status; //STORING THE INCOMING DATA ON A PUBLIC VARIABLE


           function_tosplit();//FUNCTION TO REMOVE BRACKETS FROM DATA AND SEND IT FOR FURTHER PROCESSING


        /*FOLLOWING VARIABLES TELLS THE APP THAT THE OBJECTS FOR THE VIEWS ARE CREATED AND NOW WE
        CAN UPDATE THE USERINTERFACE WITHOUT CRASHING WITH NULLPOINTEXCEPTION
         */
           if(parker==true) {
               changing_park();
          }
          if(temper==true)
          {
               changing_temp();
          }
        if(water==true)
        {
           changing_water();
        }
        if(control==true) {
        changing_control();
        }
    }

    private void displayWSStatus(String status) {
        Log.d(TAG, status);

    }

    private void displayWSReceived(String status) {
        Log.d(TAG, status);

    }



    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // TODO Auto-generated method stub
        //super.onActivityResult(requestCode, resultCode, data);

  /*
   * To make it simple, always re-load Preference setting.
   */

        loadPref();
    }
    private void loadPref(){
        //METHOD TO GET THE SETTINGS FROM SETTINGS PAGE

        SharedPreferences mySharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);





            park_notify_settings=  mySharedPreferences.getBoolean("set_park",true);
            set_motor=mySharedPreferences.getBoolean("set_motor",true);
        set_motor_value_min=  Integer.parseInt( mySharedPreferences.getString("set_motor_value_min", ""));
        set_motor_value_max=  Integer.parseInt( mySharedPreferences.getString("set_motor_value_max", ""));
        set_gate=mySharedPreferences.getBoolean("set_gate",true);







    }
    public static void  park_check(){

     //FUNCTION FOR OBJECT CREATION
        parker=true;
    }
    public static void temp_check(){
        temper=true;
    } //FUNCTION FOR OBJECT CREATION
    public static void control_check(){
        control=true;
    } //FUNCTION FOR OBJECT CREATION

    public static void water_check(){
        water=true;
    } //FUNCTION FOR OBJECT CREATION




    public  void msg(String a){Toast.makeText(my_activity.this, a,
            Toast.LENGTH_LONG).show();}//EASILY SEND TOAST with msg(string)

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            Intent intent = new Intent();
            intent.setClass(my_activity.this, SetPreferenceActivity.class);
            startActivityForResult(intent, 0);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        //NAVIGATION DRAWER MENU ONCLICK ITEMS

        int id = item.getItemId();

        if (id == R.id.nav_parking) {

           BlankFragment cl=new BlankFragment();
            FragmentManager fr=getSupportFragmentManager();
            fr.beginTransaction().replace(R.id.my_layout,cl,cl.getTag()).commit();
            setTitle("Parking");

        } else if (id == R.id.nav_water) {
           waterlevel cl=new waterlevel();
            FragmentManager fr=getSupportFragmentManager();
               fr.beginTransaction().replace(R.id.my_layout,cl,cl.getTag()).commit();
            setTitle("WaterLevel");
        } else if (id == R.id.nav_temp) {
            temperature cl=new temperature();
            FragmentManager fr=getSupportFragmentManager();
                fr.beginTransaction().replace(R.id.my_layout,cl,cl.getTag()).commit();
            setTitle("Temperature");


        } else if (id == R.id.nav_controls) {

            control cl=new control();
            FragmentManager fr=getSupportFragmentManager();
            fr.beginTransaction().replace(R.id.my_layout,cl,cl.getTag()).commit();
            setTitle("Controls");


        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }


















    public void function_tosplit()
    {
         //PROCESSING THE DATA TO REMOVE BRACKETS and split them by "," so I Can get each variable
        try {
            final_data=final_data.replace("{","");
            final_data=final_data.replace("}","");


                String result[]=final_data.split(",");
            final_result=result.clone();




        }
        catch (Exception e){msg("Message still recieving");return;}






    }
    public void changing_park()   {
        //CHANGING THE INTERFACE WITH 1=car ,0=nocar and chaning the view
        try {


            String result[] = final_result[1].split("=");
            String cars[] = result[1].split("!");

            int count = 0;
            for (int i = 0; i < result[1].length(); i++) {
                if (result[1].charAt(i) == '1') {
                    count++;
                }


            }


            if (count == 4) {
              if(park_notify_settings==true) {

                  if (notifier == false) {

                      notify_parking("Parking full");
                      notifier = true;
                  }
              }
                    if(set_gate==true){ArtikCloudSession.getInstance().send_action_gate_on();}
                //blink LED to notify full parking

            }
            else {
                notifier = false;
                if(set_gate==true){ArtikCloudSession.getInstance().send_action_gate_off();}
            }

            if (cars[0].equals("1")) {

                BlankFragment.slot1.setVisibility(View.VISIBLE);

            } else {
                BlankFragment.slot1.setVisibility(View.INVISIBLE);

            }
            if (cars[1].equals("1")) {
                BlankFragment.slot2.setVisibility(View.VISIBLE);

            } else {
                BlankFragment.slot2.setVisibility(View.INVISIBLE);

            }
            if (cars[2].equals("1")) {
                BlankFragment.slot3.setVisibility(View.VISIBLE);

            } else {
                BlankFragment.slot3.setVisibility(View.INVISIBLE);

            }
            if (cars[3].equals("1")) {
                BlankFragment.slot4.setVisibility(View.VISIBLE);

            } else {
                BlankFragment.slot4.setVisibility(View.INVISIBLE);

            }
            if (cars[4].equals("1")) {
                BlankFragment.slot5.setVisibility(View.VISIBLE);

            } else {
                BlankFragment.slot5.setVisibility(View.INVISIBLE);

            }
            if (cars[5].equals("1")) {
                BlankFragment.slot6.setVisibility(View.VISIBLE);

            } else {
                BlankFragment.slot6.setVisibility(View.INVISIBLE);

            }
            if (cars[6].equals("1")) {
                BlankFragment.slot7.setVisibility(View.VISIBLE);

            } else {
                BlankFragment.slot7.setVisibility(View.INVISIBLE);

            }
            if (cars[7].equals("1")) {
                BlankFragment.slot8.setVisibility(View.VISIBLE);

            } else {
                BlankFragment.slot8.setVisibility(View.INVISIBLE);

            }
            if (cars[8].equals("1")) {
                BlankFragment.slot9.setVisibility(View.VISIBLE);

            } else {
                BlankFragment.slot9.setVisibility(View.INVISIBLE);

            }
            if (cars[9].equals("1")) {
                BlankFragment.slot10.setVisibility(View.VISIBLE);

            } else {
                BlankFragment.slot10.setVisibility(View.INVISIBLE);

            }
            if (cars[10].equals("1")) {
                BlankFragment.slot11.setVisibility(View.VISIBLE);

            } else {
                BlankFragment.slot11.setVisibility(View.INVISIBLE);

            }
            if (cars[11].equals("1")) {
                BlankFragment.slot12.setVisibility(View.VISIBLE);

            } else {
                BlankFragment.slot12.setVisibility(View.INVISIBLE);

            }
        }
        catch (Exception e){return;}



    }
    public void changing_temp()
    {
        try {
            String result[] = final_result[2].split("=");
            //changing the temperature by seperating with ! and setting each

            //  String myt="34!33!22!34!14!31!25";
            String temperature_data[] = result[1].split("!");
            temperature.temp0.setText(temperature_data[0] + " \u2103");
            temperature.temp1.setText(temperature_data[1] + " \u2103");
            temperature.temp2.setText(temperature_data[2] + " \u2103");
            temperature.temp3.setText(temperature_data[3] + " \u2103");
            temperature.temp4.setText(temperature_data[4] + " \u2103");
            //FOR MAKING NOTIFICATION
        }
        catch (Exception e){ msg("Error");return;}

    }
    public void changing_water()
    {
        //GETIING THE DATA AFTER PROCESSING AND PASSING TO THE TEXT VIEW TO SET
     //   String myt="250";

        try {
            String result[] = final_result[4].split("=");
            String myt = result[1];


           int a = Integer.parseInt(myt);
            waterlevel.tank_text.setText(String.valueOf(a) + " cm");
            if(set_motor==true ) {

                if (a < set_motor_value_min) {

                    if(checker==false){
                        ArtikCloudSession.getInstance().send_action_motor_on();
                        notify_all("Motor started");
                    }
                    checker=true;
                }
            }
            if(set_motor==true ) {

                if (a > set_motor_value_max) {
                    if(checker==true){
                        ArtikCloudSession.getInstance().send_action_motor_off();
                        notify_all("Motor stopped");
                    }
                    checker=false;
                }
            }



            if(a<=1){waterlevel.level3.setVisibility(View.INVISIBLE);waterlevel.level2.setVisibility(View.INVISIBLE);waterlevel.level1.setVisibility(View.INVISIBLE);}
            else if(a>5){waterlevel.level3.setVisibility(View.VISIBLE);waterlevel.level2.setVisibility(View.INVISIBLE);waterlevel.level1.setVisibility(View.INVISIBLE);}
            else if(a>9){waterlevel.level3.setVisibility(View.VISIBLE);waterlevel.level2.setVisibility(View.VISIBLE);waterlevel.level1.setVisibility(View.INVISIBLE);}
            else if(a>12){waterlevel.level3.setVisibility(View.VISIBLE);waterlevel.level2.setVisibility(View.VISIBLE);waterlevel.level1.setVisibility(View.VISIBLE);}


        }
        catch (Exception e){ msg("Error");return;}





    }
    public void changing_control()
    {
        //CHANGING THE UI ACCORDING TO THE STATE of motor and gate
        try {
            String result[] = final_result[0].split("=");
            com.client.example.control.motor_value = result[1];

            if (result[1].equals("true")) {

                com.client.example.control.motor_red.setVisibility(View.GONE);
                com.client.example.control.motor_green.setVisibility(View.VISIBLE);
                com.client.example.control.motor_tg.setChecked(true);

            } else {
                com.client.example.control.motor_red.setVisibility(View.VISIBLE);
                com.client.example.control.motor_green.setVisibility(View.GONE);
                com.client.example.control.motor_tg.setChecked(false);
            }


            String gate_result[] = final_result[3].split("=");
            com.client.example.control.gate_value = gate_result[1];


            if (gate_result[1].equals("true")) {


                com.client.example.control.gate_red.setVisibility(View.GONE);
                com.client.example.control.gate_green.setVisibility(View.VISIBLE);
                com.client.example.control.gate_tg.setChecked(true);


            } else {

                com.client.example.control.gate_red.setVisibility(View.VISIBLE);
                com.client.example.control.gate_green.setVisibility(View.GONE);
                com.client.example.control.gate_tg.setChecked(false);
            }
            if (com.client.example.control.a == 1) {
                com.client.example.control.motor_tg.setEnabled(true);
                com.client.example.control.a = 0;
            }
            if (com.client.example.control.b == 1) {
                com.client.example.control.gate_tg.setEnabled(true);
                com.client.example.control.b = 0;
            }
        }
        catch (Exception e){ msg("Error");return;}
    }
    private void notify_parking(String a){
        //METHOD TO MAKE NOTIFICATION WITH SOUND
        Intent intent = new Intent(this,my_activity.class);
        PendingIntent pIntent = PendingIntent.getActivity(this, 0, intent, 0);

        Uri f = Uri.parse("android.resource://"
               + this.getPackageName() + "/" + R.raw.horn);



        Notification n  = new Notification.Builder(this)
                .setContentTitle("Cloud live")

                .setContentText(a)
                .setSmallIcon(R.drawable.cloudy)
                .setContentIntent(pIntent)
                .setAutoCancel(true)
              .setSound(f)

                .build();


        NotificationManager notificationManager =
                (NotificationManager) getSystemService(NOTIFICATION_SERVICE);

        notificationManager.notify(13376166, n);
    }
//Normal notification with sound
    private void notify_all(String a){
        Intent intent = new Intent(this,my_activity.class);
        PendingIntent pIntent = PendingIntent.getActivity(this, 0, intent, 0);

        Uri f = Uri.parse("android.resource://"
                + this.getPackageName() + "/" + R.raw.alarm);



        Notification n  = new Notification.Builder(this)
                .setContentTitle("Cloud live")

                .setContentText(a)
                .setSmallIcon(R.drawable.cloudy)
                .setContentIntent(pIntent)
                .setAutoCancel(true)
                .setSound(f)

                .build();


        NotificationManager notificationManager =
                (NotificationManager) getSystemService(NOTIFICATION_SERVICE);

        notificationManager.notify(13376166, n);
    }


}

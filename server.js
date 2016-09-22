var webSocketUrl = "wss://api.artik.cloud/v1.1/websocket?ack=true";
var device_id = ""; // YOUR DEVICE ID
var device_token = ""; //YOUR DEVICE TOKEN

var WebSocket = require('ws');
var isWebSocketReady = false;
var data="";
var ws = null;
var serialport = require("serialport")
var SerialPort = serialport.SerialPort;
var sp = new SerialPort("/dev/ttyACM0", { //for serial communication with arduino 
    baudrate: 9600,  
// we are using UNO so baudrate is 9600, you might need to change according to your model
    parser: serialport.parsers.readline("\n")
});


var state_motor=0;// variable to check for motor state
var state_gate=0;// variable to check for gate state

/**
 * Gets the current time in millis
 */
function getTimeMillis(){
    return parseInt(Date.now().toString());
}

/**
 * Create a /websocket connection and setup GPIO pin
 */
function start() {
    //Create the WebSocket connection
    isWebSocketReady = false;
    ws = new WebSocket(webSocketUrl);
    ws.on('open', function() {
        console.log("WebSocket connection is open ....");
        register();
    });
    ws.on('message', function(data) {
      //this loop is called whenever the client sends some message
         handleRcvMsg(data); //data is send to the function handleRcvMsg()
    });
    ws.on('close', function() {
        console.log("WebSocket connection is closed ....");

    });

   
      
    
}

/**
 * Sends a register message to /websocket endpoint
 */
//Client will only work when device gets registered from here
function register(){
    console.log("Registering device on the WebSocket connection");
    try{
        var registerMessage = '{"type":"register", "sdid":"'+device_id+'", "Authorization":"bearer '+device_token+'", "cid":"'+getTimeMillis()+'"}';
        console.log('Sending register message ' + registerMessage + '\n');
        ws.send(registerMessage, {mask: true});
        isWebSocketReady = true;
    }
    catch (e) {
        console.error('Failed to register messages. Error in registering message: ' + e.toString());
    }    
}


//data after receiving is sent here for processing
function handleRcvMsg(msg){
    var msgObj = JSON.parse(msg);
    if (msgObj.type != "action") return; //Early return;

    var actions = msgObj.data.actions;
    var actionName = actions[0].name; //assume that there is only one action in actions
    console.log("The received action is " + actionName);
  
    //you must know your registered actions in order to perform accordinlgy
    //in tutorial I registered motor_on, motor_off, gate_on, gate_off
    if (actionName.toLowerCase() == "motor_on") 
    { 
        //our registered action "motor_on" 
        //(if you registered with some other actionname then replace with above )
    state_motor=1;//set the variable to 1 for on
     sp.write("c");
        //sends "c" to arduino, then arduino performs the programmed action (done in arduino file)
    }
    else if (actionName.toLowerCase() == "motor_off") 
    {
      state_motor=0;//set motor to off
     sp.write("d");//sends "d" to arduino
    } 
   else if (actionName.toLowerCase()=="gate_on")
   {
  state_gate=1;//set gate to on
sp.write("e");//sends "e" to arduino

}
else if(actionName.toLowerCase()=="gate_off"){
state_gate=0;//set gate to off
sp.write("f");//sends "f" to arduino
}
     else {
         //this loop executes if some unregistered action is received
         //so you must register every action in cloud
        console.log('Do nothing since receiving unrecognized action ' + actionName);
        return;
    }
   
}



/**
 * Send one message to ARTIK Cloud
 */
//This function is responsible for sending commands to cloud
function sendStateToArtikCloud(parking,temperature,water){
    try{
        ts = ', "ts": '+getTimeMillis();
        var data = {
            "parking_value": parking, 
//setting the parking value from argument to our cloud variable "parking_value"
 //we do the same thing below setting all variable values to send data
            "temperature_value":temperature,
            "water_value":water,
             "state_motor":state_motor,
              "state_gate": state_gate
            };
        var payload = '{"sdid":"'+device_id+'"'+ts+', "data": '+JSON.stringify(data)+', "cid":"'+getTimeMillis()+'"}';
        console.log('Sending payload ' + payload + '\n');
        ws.send(payload, {mask: true});
    } catch (e) {
        console.error('Error in sending a message: ' + e.toString() +'\n');
    }    
}



function exitClosePins() {
    
        console.log('Exit and destroy all pins!');
        process.exit();
    
}


start();
//exectes every second when data is received from arduino (5sec programmed delay from arduino)
sp.on("open", function () {
    sp.on('data', function(data) {

            console.log("Serial port received data:" + data);
            var result=data.split("-");//data gets split by "-" to get the values
            sendStateToArtikCloud(result[0],result[2],result[1]);//parking,temperature,waterlevel
      
           
    });
});


process.on('SIGINT', exitClosePins);

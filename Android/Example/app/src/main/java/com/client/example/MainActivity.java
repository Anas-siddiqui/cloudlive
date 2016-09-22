package com.client.example;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.Toast;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.Toast;

public class MainActivity extends Activity {
    static final String TAG = "MainActivity";
    private static final String ARTIK_CLOUD_AUTH_BASE_URL = "https://accounts.artik.cloud";
    private static final String CLIENT_ID = "";//Application ID
    private static final String REDIRECT_URL = "http://localhost:8000/acdemo/index.php";

    private View mLoginView;
    private WebView mWebView;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //CREATING OBJECT OF VIEWS
        mWebView = (WebView)findViewById(R.id.webview);
        mWebView.setVisibility(View.GONE);
        mLoginView = findViewById(R.id.ask_for_login);
        mLoginView.setVisibility(View.VISIBLE);
        Button button = (Button)findViewById(R.id.btn);

        Log.v(TAG, "::onCreate");
        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                //THIS METHOD IS CALLED WHENEVER THE LOGIN IS PRESSED
                try {
                    Log.v(TAG, ": button is clicked.");
                    loadWebView();//LOAD THE WEBPAGE
                } catch (Exception e) {
                    Log.v(TAG, "Run into Exception");
                    e.printStackTrace();
                }
            }
        });

    }
//ENABLE SCRIPT IN WEBVIEW
    @SuppressLint("SetJavaScriptEnabled")
    private void loadWebView() {
        Log.v(TAG, "::loadWebView");
        mLoginView.setVisibility(View.GONE);
        mWebView.setVisibility(View.VISIBLE);
        mWebView.getSettings().setJavaScriptEnabled(true);
        msg("Loading");
        mWebView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String uri) {
                if ( uri.startsWith(REDIRECT_URL) ) {
                    //CONNECT TO THE URL WITH CLIEND ID and GET ACCESS TOKEN TO USE THE APP
                    String[] sArray = uri.split("&");
                    for (String paramVal : sArray) {
                        if (paramVal.indexOf("access_token=") != -1) {
                            String[] paramValArray = paramVal.split("access_token=");
                            String accessToken = paramValArray[1];
                            onGetAccessToken(accessToken);

                            startMessageActivity(accessToken); //ON GETTING THE ACCESSTOKEN OPEN THE INTERFACE
                            break;
                        }
                    }
                    return true;
                }

                return super.shouldOverrideUrlLoading(view, uri);
            }
        });

        String url = getAuthorizationRequestUri();
        Log.v(TAG, "webview loading url: " + url);
        mWebView.loadUrl(url);
    }

    public String getAuthorizationRequestUri() {

        return ARTIK_CLOUD_AUTH_BASE_URL + "/authorize?client=mobile&response_type=token&" +
                "client_id=" + CLIENT_ID + "&redirect_uri=" + REDIRECT_URL;
    }
    private void onGetAccessToken(String accessToken)
    {

        ArtikCloudSession.getInstance().setAccessToken(accessToken);

    }

    private void startMessageActivity(String accessToken) {
        Intent msgActivityIntent = new Intent(this, my_activity.class);
        msgActivityIntent.putExtra(my_activity.KEY_ACCESS_TOKEN, accessToken);//send access token for use
        startActivity(msgActivityIntent);

    }
    public void msg(String a){Toast.makeText(MainActivity.this, a,
            Toast.LENGTH_LONG).show();}


}

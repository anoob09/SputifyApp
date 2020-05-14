package com.example.sputify;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Looper;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.spotify.sdk.android.auth.AuthorizationClient;
import com.spotify.sdk.android.auth.AuthorizationRequest;
import com.spotify.sdk.android.auth.AuthorizationResponse;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.Locale;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class MainActivity extends AppCompatActivity {

    private static final int PERMISSION_ID = 44;
    //Location
    private FusedLocationProviderClient fusedLocationClient;
    private double mLattitude = 0;
    private double mLongitude = 0;


    //Spotify Auth
    public static final String CLIENT_ID = "0ed69a13f4324b1fa02a4cf26a11bba9";
    public static final String REDIRECT_URI = "https://eflask-app-1.herokuapp.com/";
    public static final int AUTH_TOKEN_REQUEST_CODE = 0x10;
    public static final int AUTH_CODE_REQUEST_CODE = 0x11;

    //okhttp
    private final OkHttpClient mOkHttpClient = new OkHttpClient();
    private String mAccessToken;
    private String mAccessCode;
    private Call mCall;
    String intentData = "";

    private boolean checkPermissions(){
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED){
            return true;
        }
        return false;
    }

    private void requestPermissions(){
        ActivityCompat.requestPermissions(
                this,
                new String[]{Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION},
                PERMISSION_ID
        );
    }

    private boolean isLocationEnabled(){
        LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) || locationManager.isProviderEnabled(
                LocationManager.NETWORK_PROVIDER
        );
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == PERMISSION_ID) {
            if(grantResults.length>0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                // Granted. Start getting the location information
            }
        }
    }

    FusedLocationProviderClient mFusedLocationClient;

    @SuppressLint("MissingPermission")
    private void getLastLocation(){
        if (checkPermissions()) {
            if (isLocationEnabled()) {
                mFusedLocationClient.getLastLocation().addOnCompleteListener(
                        new OnCompleteListener<Location>() {
                            @Override
                            public void onComplete(@NonNull Task<Location> task) {
                                Location location = task.getResult();
                                if (location == null) {
                                    requestNewLocationData();
                                } else {
                                    System.out.println("LATTITUDE " + location.getLatitude() + " LONGITUTDE " + location.getLongitude());
                                    mLattitude = location.getLatitude();
                                    mLongitude = location.getLongitude();
                                }
                            }
                        }
                );
            } else {
                Toast.makeText(this, "Turn on location", Toast.LENGTH_LONG).show();
                Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                startActivity(intent);
            }
        } else {
            requestPermissions();
        }
    }

    @SuppressLint("MissingPermission")

    private LocationCallback mLocationCallback = new LocationCallback() {
        @Override
        public void onLocationResult(LocationResult locationResult) {
            Location mLastLocation = locationResult.getLastLocation();
            System.out.println("LATTITUDE " + mLastLocation.getLatitude() + " LONGITUTDE " + mLastLocation.getLongitude());
            mLattitude = mLastLocation.getLatitude();
            mLongitude = mLastLocation.getLongitude();
        }
    };

    private void requestNewLocationData(){

        LocationRequest mLocationRequest = new LocationRequest();
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        mLocationRequest.setInterval(0);
        mLocationRequest.setFastestInterval(0);
        mLocationRequest.setNumUpdates(1);

        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        mFusedLocationClient.requestLocationUpdates(
                mLocationRequest, mLocationCallback,
                Looper.myLooper()
        );

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        getLastLocation();
        setContentView(R.layout.activity_main);
        getSupportActionBar().setTitle(String.format(
                Locale.US, "Spotify Auth Sample %s", com.spotify.sdk.android.auth.BuildConfig.VERSION_NAME));
    }



    @Override
    protected void onDestroy() {
        cancelCall();
        super.onDestroy();
    }

//    public void onGetUserProfileClicked(View view) {
//        if (mAccessToken == null) {
//            final Snackbar snackbar = Snackbar.make(findViewById(R.id.activity_main), R.string.warning_need_token, Snackbar.LENGTH_SHORT);
//            snackbar.getView().setBackgroundColor(ContextCompat.getColor(this, R.color.colorAccent));
//            snackbar.show();
//            return;
//        }
//
//        final Request request = new Request.Builder()
//                .url("https://api.spotify.com/v1/me")
//                .addHeader("Authorization", "Bearer " + mAccessToken)
//                .build();
//
//        cancelCall();
//        mCall = mOkHttpClient.newCall(request);
//
//        mCall.enqueue(new Callback() {
//            @Override
//            public void onFailure(Call call, IOException e) {
//                setResponse("Failed to fetch data: " + e);
//            }
//
//            @Override
//            public void onResponse(Call call, Response response) throws IOException {
//                try {
//                    final JSONObject jsonObject = new JSONObject(response.body().string());
//                    setResponse(jsonObject.toString(3));
//                    System.out.println(jsonObject);
//                } catch (JSONException e) {
//                    setResponse("Failed to parse data: " + e);
//                }
//            }
//        });
//    }

    public void onRequestCodeClicked(View view) {
        final AuthorizationRequest request = getAuthenticationRequest(AuthorizationResponse.Type.CODE);
        AuthorizationClient.openLoginActivity(this, AUTH_CODE_REQUEST_CODE, request);
    }

//    public void onRequestTokenClicked(View view) {
//        getLastLocation();
//        final AuthorizationRequest request = getAuthenticationRequest(AuthorizationResponse.Type.TOKEN);
//        AuthorizationClient.openLoginActivity(this, AUTH_TOKEN_REQUEST_CODE, request);
//    }

//    public void onClearCredentialsClicked(View view) {
//        AuthorizationClient.clearCookies(this);
//    }

    private AuthorizationRequest getAuthenticationRequest(AuthorizationResponse.Type type) {
        return new AuthorizationRequest.Builder(CLIENT_ID, type, getRedirectUri().toString())
                .setShowDialog(false)
                .setScopes(new String[]{"user-read-email", "user-read-currently-playing", "user-read-recently-played"})
                .setCampaign("your-campaign-token")
                .build();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        final AuthorizationResponse response = AuthorizationClient.getResponse(resultCode, data);

        /* if (AUTH_TOKEN_REQUEST_CODE == requestCode) {
            mAccessToken = response.getAccessToken();
//            System.out.println("mAccesstoken" + mAccessToken);
            updateTokenView();
        } else */ if (AUTH_CODE_REQUEST_CODE == requestCode) {
            mAccessCode = response.getCode();
            try {
                postRequest(mAccessCode);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

//    private void setResponse(final String text) {
//        runOnUiThread(new Runnable() {
//            @Override
//            public void run() {
//                final TextView responseView = findViewById(R.id.response_text_view);
//                responseView.setText(text);
//            }
//        });
//    }

//    private void updateTokenView() {
//        final TextView tokenView = findViewById(R.id.token_text_view);
//        tokenView.setText(getString(R.string.token, mAccessToken));
//    }

    private void sendCode(String code) {
        Intent intent = new Intent(this, UsersActivity.class);
        intent.putExtra("CAR_DETAILS", code);
        Log.e("Before sendingasda", code);
        startActivity(intent);
    }

    private void cancelCall() {
        if (mCall != null) {
            mCall.cancel();
        }
    }

    private Uri getRedirectUri() {
        return Uri.parse(REDIRECT_URI);
    }

    public void postRequest(String access_code) throws IOException {

        MediaType MEDIA_TYPE = MediaType.parse("application/json");

        OkHttpClient client = new OkHttpClient();
        JSONObject postdata = new JSONObject();
        try {
            postdata.put("code", access_code);
            postdata.put("latitude", mLattitude);
            postdata.put("longitude", mLongitude);
        } catch(JSONException e){
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        RequestBody body = RequestBody.create(MEDIA_TYPE, postdata.toString());

        Request requestToSputifyServer = new Request.Builder()
//                .url("https://eflask-app-1.herokuapp.com/")
                .url("http://192.168.43.57:5000/")
                .post(body)
                .header("Accept", "application/json")
                .header("Content-Type", "application/json")
                .build();

        client.newCall(requestToSputifyServer).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                String mMessage = e.getMessage().toString();
                Log.w("failure Response", mMessage);
                //call.cancel();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {

                String mMessage = response.body().string();
                Log.e("POST METHOD", mMessage);
                sendCode(mMessage);
            }
        });

    }


}

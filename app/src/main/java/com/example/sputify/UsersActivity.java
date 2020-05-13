package com.example.sputify;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.spotify.sdk.android.auth.AuthorizationClient;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
public class UsersActivity extends AppCompatActivity {

    // Returns a java for json array input
    private static ArrayList<String> jsonToJava(JSONArray jArray) throws JSONException {
        ArrayList<String> mylist = new ArrayList<>();
        if (jArray == null) {
            System.out.println("json is empty");
        } else {
            int length = jArray.length();
            for (int i = 0; i < length; i++) {
                mylist.add(jArray.get(i).toString());
            }
        }
        return mylist;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_users);
        ArrayList<String> users = new ArrayList<>();
        ArrayList<String> songs = new ArrayList<>();
        ArrayList<String> song_urls = new ArrayList<>();
        ArrayList<String> album_art_urls = new ArrayList<>();

        JSONObject jObject = null;
        String responseString;
        if (savedInstanceState == null) {
            Bundle extras = getIntent().getExtras();
            if (extras == null) {
                responseString = null;
            } else {
                responseString = extras.getString("CAR_DETAILS");
            }
            System.out.println("response_string savedInstanceStateNull" + responseString);

        } else {
            responseString = (String) savedInstanceState.getSerializable("CAR_DETAILS");
            System.out.println("response_string correct" + responseString);
        }

        try {
            assert responseString != null;
            jObject = new JSONObject(responseString);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        try {
            assert jObject != null;
            users = jsonToJava(jObject.getJSONArray("users"));
            songs = jsonToJava(jObject.getJSONArray("song_names"));
            song_urls = jsonToJava(jObject.getJSONArray("song_urls"));
            album_art_urls = jsonToJava(jObject.getJSONArray("album_urls"));
        } catch (JSONException e) {
            e.printStackTrace();
        }

        MyListData[] myListData = new MyListData[users.size()];
        for (int i = 0; i < users.size(); i++) {
//
            try {

                MyListData dummyObj = new MyListData();
                dummyObj.setUserId(users.get(i));
                dummyObj.setSongName(songs.get(i));
                dummyObj.setAlbumUrl(album_art_urls.get(i));
                myListData [i] = dummyObj;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }


//        MyListData[] myListData = new MyListData[] {
//                new MyListData("Say so", "anoob09" ,android.R.drawable.ic_dialog_email),
//                new MyListData("Not ok", "anoob08" ,android.R.drawable.ic_dialog_email),
//                new MyListData("One thing right", "anoob07" ,android.R.drawable.ic_dialog_email),
//        };
        RecyclerView recyclerView = findViewById(R.id.recyclerView);
        MyListAdapter adapter = new MyListAdapter(myListData);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);
        AuthorizationClient.clearCookies(this);
    }
}


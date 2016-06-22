package com.jeemba.friends;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

public class ListActivity extends AppCompatActivity {

    private ListView listView;
    private ArrayAdapter<String> adapter;
    private ArrayList<String> friends;
    private String[] list;
    private ArrayList<HashMap<String,String>> friendsGroup;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);

        friends = new ArrayList<String>();

        listView = (ListView)findViewById(R.id.listview);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

            }
        });

        sendJsonRequest();

    }

    private void sendRequest(){
        // Instantiate the RequestQueue.
        RequestQueue queue = Volley.newRequestQueue(this);
        String url ="http://api.randomuser.me/?results=10";

        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("log","Response is: "+ response);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d("log","That didn't work!");
                    }
                });
        queue.add(stringRequest);
    }

    private void sendJsonRequest(){
        String url ="http://api.randomuser.me/?results=20";

        JsonObjectRequest jsonRequest = new JsonObjectRequest
                (Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d("log","response: "+response.toString());

                        try {
                            JSONArray arr = response.getJSONArray("results");

                            friendsGroup = new ArrayList<HashMap<String,String>>();

                            for(int i=0; i<arr.length(); i++){
                                JSONObject item = arr.getJSONObject(i);
                                JSONObject objName = item.getJSONObject("name");
                                String name = objName.getString("first")+" "+objName.getString("last");

                                JSONObject objPicture = item.getJSONObject("picture");
                                String image = objPicture.getString("thumbnail");

                                String email = item.getString("email");

                                friends.add(name);

                                HashMap<String,String> map = new HashMap<String,String>();
                                map.put("name", name);
                                map.put("picture", image);
                                map.put("email", email);

                                friendsGroup.add(map);
                            }

                            Log.d("log","-->"+friendsGroup.toString());
                            list = new String[friends.size()];
                            for (int j=0; j<friends.size(); j++) {
                                list[j] = friends.get(j);
                            }

                            adapter = new ArrayAdapter<String>(ListActivity.this, android.R.layout.simple_list_item_1, list);
                            listView.setAdapter(adapter);
//                            Log.d("log","---> friends : "+ friends.toString());


                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d("log","Error: "+error.getMessage());
                    }
                });

        Volley.newRequestQueue(this).add(jsonRequest);

    }
}

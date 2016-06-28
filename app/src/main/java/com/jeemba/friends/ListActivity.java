package com.jeemba.friends;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ListActivity extends AppCompatActivity {

    private ListView listView;
    private ArrayAdapter<String> adapter;
    private ArrayList<String> friends;
    private String[] list;
    private ArrayList<HashMap<String,String>> friendsGroup;
    private SharedPreferences pref;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);

        friends = new ArrayList<String>();

        listView = (ListView)findViewById(R.id.listview);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Log.d("log","--->"+friendsGroup.get(i));
                HashMap<String,String> item = friendsGroup.get(i);
                Intent intent = new Intent(ListActivity.this, DetailActivity.class);
                intent.putExtra("name", item.get("name"));
                intent.putExtra("email", item.get("email"));
                intent.putExtra("picture", item.get("picture_large"));
                startActivity(intent);
//                showDetail(item);
            }
        });

        progressDialog = new ProgressDialog(ListActivity.this);
        progressDialog.setMessage("please wait...");
        progressDialog.show();
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

                        progressDialog.dismiss();

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
                                String image_medium = objPicture.getString("large");

                                String email = item.getString("email");

                                friends.add(name);

                                HashMap<String,String> map = new HashMap<String,String>();
                                map.put("name", name);
                                map.put("picture", image);
                                map.put("picture_large", image_medium);
                                map.put("email", email);

                                friendsGroup.add(map);
                            }

                            Log.d("log","-->"+friendsGroup.toString());
                            list = new String[friends.size()];
                            for (int j=0; j<friends.size(); j++) {
                                list[j] = friends.get(j);
                            }

//                            adapter = new ArrayAdapter<String>(ListActivity.this, android.R.layout.simple_list_item_1, list);
//                            listView.setAdapter(adapter);
//                            Log.d("log","---> friends : "+ friends.toString());

                            MyAdapter adapter = new MyAdapter(ListActivity.this, friendsGroup);
                            listView.setAdapter(adapter);


                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        progressDialog.dismiss();
                        Log.d("log","Error: "+error.getMessage());
                    }
                });

        Volley.newRequestQueue(this).add(jsonRequest);

    }

    private void showDetail(HashMap<String,String> item ){
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        final AlertDialog alertDialog = builder.create();
        builder.setTitle("Detail");

        View myview = this.getLayoutInflater().inflate(R.layout.activity_detail, null);
        alertDialog.setView(myview);

        TextView tname = (TextView)myview.findViewById(R.id.nama);
        TextView temail = (TextView)myview.findViewById(R.id.email);
        ImageView image = (ImageView)myview.findViewById(R.id.imageview);

        tname.setText(item.get("name"));
        temail.setText(item.get("email"));

        sendImageRequest(item.get("picture_large"), image);


        Button btClose = (Button) myview.findViewById(R.id.btclose);
        btClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alertDialog.dismiss();
            }
        });



        alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        alertDialog.show();

    }

    private void sendImageRequest(String url, final ImageView imageView){

        ImageRequest request = new ImageRequest(
                url,
                new Response.Listener<Bitmap>() {
                    @Override
                    public void onResponse(Bitmap bitmap) {
                        Log.d("log","load image success : ");
                        imageView.setImageBitmap(bitmap);
                    }
                }, 100, 100, ImageView.ScaleType.FIT_CENTER, Bitmap.Config.RGB_565,
                new Response.ErrorListener() {
                    public void onErrorResponse(VolleyError error) {
                        // Image gak ada! Tampilkan image error.
                        Log.d("log","load image success : ");
                        //image.setImageResource(R.drawable.ic_error_outline_black_24dp);
                    }
                });

        Volley.newRequestQueue(ListActivity.this).add(request);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.main, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == R.id.add){
            Intent intent = new Intent(ListActivity.this, AddActivity.class);
            startActivity(intent);
        }
        return super.onOptionsItemSelected(item);

    }


}

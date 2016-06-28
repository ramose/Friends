package com.jeemba.friends;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

public class AddActivity extends AppCompatActivity implements View.OnClickListener {

    private EditText input1;
    private ProgressDialog progressDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setTitle("Add A Friend");

        progressDialog = new ProgressDialog(AddActivity.this);
        progressDialog.setMessage("please wait...");

        input1 = (EditText)findViewById(R.id.input1);

        Button btAdd = (Button)findViewById(R.id.btn_add);
        btAdd.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        if(view.getId() == R.id.btn_add){
            progressDialog.show();

            // Post using Volley
//            postRequest();

            // Post using HttpURLConnection
            new postTask().execute();
        }
    }

    private void postRequest() {
        // Instantiate the RequestQueue.
//        String url = "api.digidima.com/android/friends/act";
        Log.d("log", "post request");
        String url = "http://jeembastudio.com/tester/post.php";
        JSONObject obj = new JSONObject();
        try {
            obj.put("message", input1.getText().toString());

            JsonObjectRequest jsonRequest = new JsonObjectRequest
                    (Request.Method.POST, url, obj, new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            Log.d("log", "response : " + response.toString());
                            try {
                                String message = response.getString("data");
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                        }
                    }, new Response.ErrorListener() {

                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Log.d("log", "Error: " + error.getMessage());
                        }
                    });

            Volley.newRequestQueue(this).add(jsonRequest);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private class postTask extends AsyncTask<String, String, String> {

        private Map<String,String> param;
        private String userid;
        private HashMap<String,String> userdata;

        public postTask(){
//            mDialog.show();

            InputMethodManager keyboard = (InputMethodManager)AddActivity.this.getSystemService(Context.INPUT_METHOD_SERVICE);
            keyboard.hideSoftInputFromWindow(input1.getWindowToken(), 0);

            userdata = new HashMap<String, String>();
            userdata.put("message", input1.getText().toString());

        }

        @Override
        protected String doInBackground(String... params) {

//            String encodedStr = getEncodedData(userdata);
            JSONObject jsonObject = new JSONObject(userdata);

            Log.d("log","------------- JSON -------------");
            Log.d("log",jsonObject.toString());
            Log.d("log","---------------------------------");

            HttpURLConnection connection = null;
            BufferedReader reader = null;

            try {
                Log.d("log","---> go to url");

//                URL url = new URL("api.digidima.com/android/friends/act");
                URL url = new URL("http://jeembastudio.com/tester/post.php");

                connection = (HttpURLConnection) url.openConnection();
                //connection.connect();

                connection.setRequestMethod("POST");
                connection.setDoOutput(true);
                OutputStreamWriter writer = new OutputStreamWriter(connection.getOutputStream());
                writer.write(jsonObject.toString());
                //writer.write(encodedStr.toString());
                writer.flush();

                StringBuilder sb = new StringBuilder();
                reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));

                StringBuffer buffer = new StringBuffer();
                String line = "";
                while((line = reader.readLine()) != null){

                    buffer.append(line);
                    Log.d("log","---> buffer : "+buffer.toString());
                }

                return buffer.toString();

            } catch (MalformedURLException e) {
                e.printStackTrace();
                int m = Log.d("user", e.toString());
            } catch (IOException e) {
                e.printStackTrace();
                int m = Log.d("user", e.toString());
            } finally {
                if(connection != null){
                    connection.disconnect();
                }
                try {
                    if(reader != null){
                        reader.close();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                    Log.d("log", e.toString());
                }
            }



            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            progressDialog.dismiss();

            Log.d("log","result : "+s.toString());

            if(s != null){
                try {
                    JSONObject json = new JSONObject(s);
                    String data = json.getString("data");
                    Toast.makeText(AddActivity.this, data, Toast.LENGTH_SHORT).show();
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            } else{
                Log.d("log","---> error");
            }
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return(true);
        }

        return super.onOptionsItemSelected(item);

    }
}

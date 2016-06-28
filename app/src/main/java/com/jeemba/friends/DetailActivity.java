package com.jeemba.friends;

import android.graphics.Bitmap;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.request.target.ImageViewTargetFactory;

import org.w3c.dom.Text;

public class DetailActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Bundle bundle = getIntent().getExtras();


        String name = getIntent().getExtras().getString("name").toString();
        String email = getIntent().getExtras().getString("email").toString();
        String pictureUrl = getIntent().getExtras().getString("picture").toString();

        Log.d("log","name:"+name);

        TextView tname = (TextView)findViewById(R.id.nama);
        TextView temail = (TextView)findViewById(R.id.email);
        final ImageView image = (ImageView)findViewById(R.id.imageview);

        tname.setText(name);
        temail.setText(email);

        ImageRequest request = new ImageRequest(
                pictureUrl,
                new Response.Listener<Bitmap>() {
                    @Override
                    public void onResponse(Bitmap bitmap) {
                        Log.d("log","load image success : ");
                        image.setImageBitmap(bitmap);
                    }
                }, 100, 100, ImageView.ScaleType.FIT_CENTER, Bitmap.Config.RGB_565,
                new Response.ErrorListener() {
                    public void onErrorResponse(VolleyError error) {
                        // Image gak ada! Tampilkan image error.
                        Log.d("log","load image success : ");
                        //image.setImageResource(R.drawable.ic_error_outline_black_24dp);
                    }
                });

        Volley.newRequestQueue(DetailActivity.this).add(request);

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
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

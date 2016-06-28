package com.jeemba.friends;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by leo on 6/23/16.
 */
public class MyAdapter extends ArrayAdapter {
    private LayoutInflater inflater;
    private ArrayList<HashMap<String, String>> items;
    private ImageView image;
    private ImageRequest request;

    public MyAdapter(Activity context, ArrayList<HashMap<String, String>> data) {
        super(context, R.layout.my_layout, data);
        this.items = data;
        inflater = context.getWindow().getLayoutInflater();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View myview = inflater.inflate(R.layout.my_layout, parent, false);

        TextView tname = (TextView)myview.findViewById(R.id.name);
        TextView temail = (TextView)myview.findViewById(R.id.email);
        ImageView image = (ImageView)myview.findViewById(R.id.imageview);

        tname.setText(items.get(position).get("name"));
        temail.setText(items.get(position).get("email"));

//        sendImageRequest(items.get(position).get("picture"), image);

        Glide.with(this.getContext())
                .load(items.get(position).get("picture"))
                .into(image);


        return myview;
    }

//    private void sendImageRequest(String url, final ImageView imageView){
//
//        request = new ImageRequest(
//                url,
//                new Response.Listener<Bitmap>() {
//                    @Override
//                    public void onResponse(Bitmap bitmap) {
//                        Log.d("log","load image success : ");
//                        imageView.setImageBitmap(bitmap);
//                    }
//                }, 100, 100, ImageView.ScaleType.FIT_CENTER, Bitmap.Config.RGB_565,
//                new Response.ErrorListener() {
//                    public void onErrorResponse(VolleyError error) {
//                        // Image gak ada! Tampilkan image error.
//                        Log.d("log","load image success : ");
//                        //image.setImageResource(R.drawable.ic_error_outline_black_24dp);
//                    }
//                });
//
//                Volley.newRequestQueue(this.getContext()).add(request);
//
//    }
}

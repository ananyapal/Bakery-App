package com.inducesmile.androidpayexample;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.Toast;

import com.inducesmile.androidpayexample.adapters.ImageAdapter;
import com.inducesmile.androidpayexample.helpers.SpacesItemDecoration;


public class FrontPage extends Activity {

    private static final String TAG = FrontPage.class.getSimpleName();       //???

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.frontpage);

        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);


        GridView gridview = (GridView) findViewById(R.id.gridview);
        gridview.setAdapter(new ImageAdapter(this));

        gridview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent,
                                    View v, int position, long id){
                // Send intent to ShoppingActivity

                if(position==1) {
                    Intent i = new Intent(getApplicationContext(), ShoppingActivity.class);
                    // Pass image index
                    i.putExtra("id", position);
                    //  Toast.makeText(FrontPage.this, "" + position, Toast.LENGTH_SHORT).show();

                    startActivity(i);
                }

            }
        });
    }
}

/*  integerRes.add(R.drawable.cakesmain);
        integerRes.add(R.drawable.pastries);
        integerRes.add(R.drawable.breads);
        integerRes.add(R.drawable.cupcakes);
        integerRes.add(R.drawable.donuts);
        integerRes.add(R.drawable.cookies);
        integerRes.add(R.drawable.macaroons);
        integerRes.add(R.drawable.contactus);

*/
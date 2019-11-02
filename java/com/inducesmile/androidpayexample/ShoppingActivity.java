package com.inducesmile.androidpayexample;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.ImageView;
import android.widget.Toast;

import com.inducesmile.androidpayexample.adapters.ImageAdapter;
import com.inducesmile.androidpayexample.adapters.ShopRecyclerViewAdapter;
import com.inducesmile.androidpayexample.entities.ProductObject;
import com.inducesmile.androidpayexample.helpers.SpacesItemDecoration;

import java.util.ArrayList;
import java.util.List;

public class ShoppingActivity extends AppCompatActivity {

    private static final String TAG = ShoppingActivity.class.getSimpleName();       //???

    private RecyclerView shoppingRecyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shopping);

        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        shoppingRecyclerView = (RecyclerView)findViewById(R.id.product_list);
        GridLayoutManager mGrid = new GridLayoutManager(ShoppingActivity.this, 2);
        shoppingRecyclerView.setLayoutManager(mGrid);
        shoppingRecyclerView.setHasFixedSize(true);
        shoppingRecyclerView.addItemDecoration(new SpacesItemDecoration(2, 12, false));

        ShopRecyclerViewAdapter shopAdapter = new ShopRecyclerViewAdapter(ShoppingActivity.this, getAllProductsOnSale());
        shoppingRecyclerView.setAdapter(shopAdapter);
    }


    private List<ProductObject> getAllProductsOnSale(){
        List<ProductObject> products = new ArrayList<ProductObject>();
        products.add(new ProductObject(1, "Black Forest Cake", R.drawable.blackforest, "Classic Black Forest with Cherries", 250,"1kg"));
        products.add(new ProductObject(1, "Mango Mousse Cake", R.drawable.mango, "The flavor of Summer with Mangoes", 300, "1kg"));
        products.add(new ProductObject(1, "Blueberry Fudge", R.drawable.blueberry, "Blueberries and a velvety mousse to taste", 270,"1kg"));
        products.add(new ProductObject(1, "Dutch Truffle", R.drawable.dutchtruffle, "Dutch Chocolate that melts in the mouth", 260, "1kg"));
        products.add(new ProductObject(1, "Ferrero Rocher", R.drawable.ferrero, "Ferrero Rochers for the love of Chocolate & Hazelnuts", 310, "1kg"));
        products.add(new ProductObject(1, "Butterscotch Cake", R.drawable.butterscotch, "Butterscotch and Cake, what a combination!", 210, "1kg"));
        products.add(new ProductObject(1, "Red Velvet Cake", R.drawable.redvelvet, "British in nature, Velvety in Taste", 320, "1kg"));
        products.add(new ProductObject(1, "Pineapple Cake", R.drawable.pineapple, "Pineapple cream with real pieces of pineapple and cherries", 250, "1kg"));

        return products;
    }
}

package com.inducesmile.androidpayexample.adapters;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.inducesmile.androidpayexample.CheckoutActivity;
import com.inducesmile.androidpayexample.ProductActivity;
import com.inducesmile.androidpayexample.R;
import com.inducesmile.androidpayexample.ShoppingActivity;
import com.inducesmile.androidpayexample.entities.ProductObject;
import com.inducesmile.androidpayexample.helpers.MySharedPreference;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static android.support.v4.app.ActivityCompat.invalidateOptionsMenu;

//The RecyclerView widget is a more advanced and flexible version of ListView .
//This widget is a container for displaying large data sets that can be scrolled very efficiently by maintaining
//        a limited number of views.



public class CheckRecyclerViewAdapter extends RecyclerView.Adapter<CheckRecyclerViewHolder> {

    private Context context;

    private List<ProductObject> mProductObject;



    public CheckRecyclerViewAdapter(Context context, List<ProductObject> mProductObject) {
        this.context = context;
        this.mProductObject = mProductObject;
    }

    @Override
    public CheckRecyclerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

    // A LayoutInflater reads an XML in which we describe how we want a UI layout to be.
    // It then creates actual Viewobjects for UI from that XML.


        View layoutView = LayoutInflater.from(parent.getContext()).inflate(R.layout.check_layout, parent, false);
        CheckRecyclerViewHolder productHolder = new CheckRecyclerViewHolder(layoutView);
        return productHolder;
    }

    @Override
    public void onBindViewHolder(final CheckRecyclerViewHolder holder, final int position) {
        //get product quantity
        holder.quantity.setText("1");
        holder.productName.setText(mProductObject.get(position).getProductName());
        holder.productPrice.setText(String.valueOf(mProductObject.get(position).getProductPrice()) + " Rs. ");

       //CHANGE THIS FOR REMOVE PRODUCT
        holder.removeProduct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                 removeproduct(position);


            }
        });

    }

    private void removeproduct(int position) {


        MySharedPreference sharedPreference =new MySharedPreference(context);

        int cartProductNumber = 0;
        final ProductObject singleProduct = mProductObject.get(position);

        Intent productIntent = new Intent(context, CheckoutActivity.class);

        GsonBuilder builder = new GsonBuilder();
        Gson gson = builder.create();

        String productInStringFormat = gson.toJson(singleProduct); //////

        productIntent.putExtra("PRODUCT", productInStringFormat);


        /////////////////////////////////////////////////////////////////////
        //   Toast.makeText(context, productInStringFormat, Toast.LENGTH_SHORT).show();

        String productsInCart = sharedPreference.retrieveProductFromCart();
        String productsInCart2= "";

        int start=0,end=0;

        if(productsInCart.contains(productInStringFormat))
        {

            if(sharedPreference.retrieveProductCount()>1) {
                end = productsInCart.lastIndexOf(productInStringFormat);
                start = end - productInStringFormat.length() + 1;
                end = end + 2;
                productsInCart2 = productsInCart.substring(0, start) + productsInCart.substring(end, productsInCart.length());
            }


        }



        sharedPreference.addProductToTheCart(productsInCart2);

        sharedPreference.addProductCount(sharedPreference.retrieveProductCount()-1);

        Toast.makeText(context, "Product removed from cart!", Toast.LENGTH_SHORT).show();

        if(productsInCart2.equals("") )
        {
            Intent shop = new Intent(context, ShoppingActivity.class);
            context.startActivity(shop);
        }

        Intent co = new Intent(context, CheckoutActivity.class);
        context.startActivity(co);

    }


    @Override
    public int getItemCount() {
        return mProductObject.size();
    }

    public static List<ProductObject> convertObjectArrayToListObject(ProductObject[] allProducts){
        List<ProductObject> mProduct = new ArrayList<ProductObject>();
        Collections.addAll(mProduct, allProducts);
        return mProduct;
    }



}



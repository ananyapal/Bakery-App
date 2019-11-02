package com.inducesmile.androidpayexample;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.google.android.gms.maps.model.LatLng;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.inducesmile.androidpayexample.helpers.User;
import com.inducesmile.androidpayexample.jsonEntityObjects.PaymentResponseObject;
import com.inducesmile.androidpayexample.jsonEntityObjects.ServerObject;
import com.inducesmile.androidpayexample.network.GsonRequest;
import com.inducesmile.androidpayexample.network.VolleySingleton;
import com.paypal.android.sdk.payments.PayPalConfiguration;
import com.paypal.android.sdk.payments.PayPalPayment;
import com.paypal.android.sdk.payments.PayPalService;
import com.paypal.android.sdk.payments.PaymentActivity;
import com.paypal.android.sdk.payments.PaymentConfirmation;

import org.json.JSONException;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;



public class PayPalCheckoutActivity extends AppCompatActivity {
    private static final int REQUEST_GET_MAP_LOCATION = 0;
    String userId;
    Button ccod;
    EditText name ;
    EditText add ;
    EditText mob ;
    EditText email ;
    LatLng mylocation;
    ImageView mapicon ;
    FirebaseDatabase mFirebaseInstance;
    DatabaseReference mFirebaseDatabase;

    private static final String TAG = PayPalCheckoutActivity.class.getSimpleName();

    private static final int MY_SOCKET_TIMEOUT_MS = 5000;

    private static final String SERVER_PATH = "Path_to_Server_To_Store_Token";

    private double totalCostPrice;

    private static PayPalConfiguration config = new PayPalConfiguration()
            .environment(PayPalConfiguration.ENVIRONMENT_NO_NETWORK)
            .clientId("ENTER PAY PAL CLIENT ID");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pay_pal_checkout);

        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        setTitle("Payment Options");

        totalCostPrice = getIntent().getExtras().getDouble("TOTAL_PRICE");
        Log.d(TAG, "Price " + totalCostPrice);

         final EditText name = (EditText)findViewById(R.id.edName);
         final EditText add = (EditText)findViewById(R.id.edAdd);
        final EditText mob = (EditText)findViewById(R.id.edMob);
        final EditText email = (EditText)findViewById(R.id.edEmail);
        ImageView mapicon = (ImageView) findViewById(R.id.mapicon);
         ccod=(Button) findViewById(R.id.cod);
         mFirebaseInstance=FirebaseDatabase.getInstance();
         mFirebaseDatabase=mFirebaseInstance.getReference("users");



        // store app title to 'app_title' node
        mFirebaseInstance.getReference("app_title").setValue("Realtime Database");

        // app_title change listener
        mFirebaseInstance.getReference("app_title").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Log.e(TAG, "App title updated");

                String appTitle = dataSnapshot.getValue(String.class);

                // update toolbar title
                getSupportActionBar().setTitle(appTitle);
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.e(TAG, "Failed to read app title value.", error.toException());
            }
        });


        ccod.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String uname = name.getText().toString();
                String uadd = add.getText().toString();
                String uemail = email.getText().toString();
                String umob = mob.getText().toString();

                // Check for already existed userId
                if (TextUtils.isEmpty(userId)) {
                    createUser(uname, uadd, umob, uemail);
                } else {
                    updateUser(uname, uadd, umob, uemail);
                }

                Toast.makeText(PayPalCheckoutActivity.this, "User Added/Updated in Firebase. Product will reach you in the next 5 days. Thank you for shopping with us!", Toast.LENGTH_LONG).show();

            }
        });

        toggleButton();


        assert mapicon != null;
        mapicon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            Intent mapp=new Intent(PayPalCheckoutActivity.this,MapsActivity.class);

                startActivityForResult(mapp,REQUEST_GET_MAP_LOCATION);
            }
        });



        Intent intent = new Intent(this, PayPalService.class);
        intent.putExtra(PayPalService.EXTRA_PAYPAL_CONFIGURATION, config);
        startService(intent);



        Button payPalButton = (Button)findViewById(R.id.pay_pal_button);
        assert payPalButton != null;
        payPalButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                initializePayPalPayment();
            }
        });
    }


    private void initializePayPalPayment(){
        PayPalPayment payment = new PayPalPayment(new BigDecimal(String.valueOf(totalCostPrice)), "USD", "Total amount", PayPalPayment.PAYMENT_INTENT_SALE);
        Intent intent = new Intent(this, PaymentActivity.class);
        intent.putExtra(PayPalService.EXTRA_PAYPAL_CONFIGURATION, config);
        intent.putExtra(PaymentActivity.EXTRA_PAYMENT, payment);
        startActivityForResult(intent, 0);
    }

    @Override
    protected void onActivityResult (int requestCode, int resultCode, Intent data) {
        if (resultCode == Activity.RESULT_OK) {
            PaymentConfirmation confirm = data.getParcelableExtra(PaymentActivity.EXTRA_RESULT_CONFIRMATION);
/*
            if(requestCode==REQUEST_GET_MAP_LOCATION && resultCode==Activity.RESULT_OK)
            {
                String mylocation=data.getStringExtra("mylocation");
                add.setText(mylocation);
            }

            */

             if (confirm != null) {
                try {
                    Log.i("paymentExample", confirm.toJSONObject().toString(4));
                    String jsonPaymentResponse = confirm.toJSONObject().toString(4);    //JavaScript Object Notation(JSON)

                    GsonBuilder builder = new GsonBuilder();
                    Gson gson = builder.create();

                    PaymentResponseObject responseObject = gson.fromJson(jsonPaymentResponse, PaymentResponseObject.class);
                    if(responseObject != null){
                        String paymentId = responseObject.getResponse().getId();
                        String paymentState = responseObject.getResponse().getState();

                        Log.d(TAG, "Log payment id and state " + paymentId + " " + paymentState);

                        //send to your server for verification.
                        sendPaymentVerificationToServer(paymentId, paymentState);
                    }

                } catch (JSONException e) {
                    Log.e("paymentExample", "an extremely unlikely failure occurred: ", e);
                }
            }
        }

        else if (resultCode == Activity.RESULT_CANCELED) {
            Log.i("paymentExample", "The user canceled.");
        }
        else if (resultCode == PaymentActivity.RESULT_EXTRAS_INVALID) {
            Log.i("paymentExample", "An invalid Payment or PayPalConfiguration was submitted. Please see the docs.");
        }
    }

    private void sendPaymentVerificationToServer(String id, String state){
        Map<String, String> params = new HashMap<String,String>();
        params.put("PAYMENT_ID", id);
        params.put("PAYMENT_STATE", state);

        GsonRequest<ServerObject> serverRequest = new GsonRequest<ServerObject>(
                Request.Method.POST,
                SERVER_PATH,
                ServerObject.class,
                params,
                createRequestSuccessListener(),
                createRequestErrorListener());

        serverRequest.setRetryPolicy(new DefaultRetryPolicy(
                MY_SOCKET_TIMEOUT_MS,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        VolleySingleton.getInstance(PayPalCheckoutActivity.this).addToRequestQueue(serverRequest);
    }

    private Response.Listener<ServerObject> createRequestSuccessListener() {
        return new Response.Listener<ServerObject>() {
            @Override
            public void onResponse(ServerObject response) {
                try {
                    Log.d(TAG, "Json Response " + response.getSuccess());
                    if(!TextUtils.isEmpty(response.getSuccess())){
                        Toast.makeText(PayPalCheckoutActivity.this, getString(R.string.successful_payment), Toast.LENGTH_LONG).show();
                    }else{
                        Toast.makeText(PayPalCheckoutActivity.this, getString(R.string.server_error), Toast.LENGTH_LONG).show();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            };
        };
    }

    private Response.ErrorListener createRequestErrorListener() {
        return new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
            }
        };
    }

    @Override
    public void onDestroy() {
        stopService(new Intent(this, PayPalService.class));
        super.onDestroy();
    }


    //-------------------------------------FIREBASE----------------------------------------------//

    /*
> getReference(“app_title”) create a node named app_title which stores the toolbar title.

> getReference(“users”) gets reference to users node.

> createUser() method stores a new user in realtime database

> updateUser() method updates user information like name and email.*/

    // Changing button text
    private void toggleButton() {
        if (TextUtils.isEmpty(userId)) {
            ccod.setText("Save for CashOnDelivery");
        } else {
            ccod.setText("Update for CashOnDelivery");
        }
    }

    /**
     * Creating new user node under 'users'
     */
    private void createUser(String name,String add,String mob, String email) {
        // TODO
        // In real apps this userId should be fetched
        // by implementing firebase auth
        if (TextUtils.isEmpty(userId)) {
            userId = mFirebaseDatabase.push().getKey();
        }

        User user = new User(name,add, mob, email);

        mFirebaseDatabase.child(userId).setValue(user);

        addUserChangeListener();
    }

    /**
     * User data change listener
     */
    private void addUserChangeListener() {
        // User data change listener
        mFirebaseDatabase.child(userId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                User user = dataSnapshot.getValue(User.class);

                // Check for null
                if (user == null) {
                    Log.e(TAG, "User data is null!");
                    return;
                }

                Log.e(TAG, "User data is changed!" + user.name + ", " + user.email);


                // clear edit text
                email.setText("");
                name.setText("");
                add.setText("");
                mob.setText("");
                toggleButton();
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.e(TAG, "Failed to read user", error.toException());
            }
        });
    }

    private void updateUser(String name,String add,String mob, String email) {
        // updating the user via child nodes
        if (!TextUtils.isEmpty(name))
            mFirebaseDatabase.child(userId).child("name").setValue(name);
        if (!TextUtils.isEmpty(name))
            mFirebaseDatabase.child(userId).child("add").setValue(add);
        if (!TextUtils.isEmpty(name))
            mFirebaseDatabase.child(userId).child("mob").setValue(mob);
        if (!TextUtils.isEmpty(email))
            mFirebaseDatabase.child(userId).child("email").setValue(email);
    }


}
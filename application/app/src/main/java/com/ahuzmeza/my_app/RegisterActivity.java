package com.ahuzmeza.my_app;

import com.ahuzmeza.my_app.Helpers.*;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.ahuzmeza.my_app.Helpers.RequestHandler;
import com.ahuzmeza.my_app.Helpers.SharedPrefManager;

import org.jetbrains.annotations.NotNull;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

import static okhttp3.MediaType.*;

public class RegisterActivity extends AppCompatActivity {

    private static final String IPV4ADRESS = "192.168.1.6";
    private static final int    PORT_NUMBER = 5000;

    static String postUrl = "http://"+IPV4ADRESS+":"+PORT_NUMBER+"/";

    EditText    editUsername;
    EditText    editEmail;
    EditText    editPassword;
    Button      btnRegister;
    Button      btnLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        editUsername    = findViewById(R.id.tb_username);
        editEmail       = findViewById(R.id.tb_email);
        editPassword    = findViewById(R.id.tb_password);
        btnRegister     = findViewById(R.id.btn_register);
        btnLogin        = findViewById(R.id.btn_login);

        // if the user is already logged in we will directly start the profile activity
        /*
        if (SharedPrefManager.getInstance(this).isLoggedIn()) {
            finish();
            startActivity(new Intent(this, MainActivity.class));
            return;
        }*/

        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                registerUser();
            }
        });

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                startActivity(new Intent(getApplicationContext(), LoginActivity.class));
            }
        });

    } // eOF onCreate

    // Methods =================================================================================

    /***************** *
     * REGISTER USER   *
     * *************** */
    private void registerUser() {
        final String username = editUsername.getText().toString().trim();
        final String email = editEmail.getText().toString().trim();
        final String password = editPassword.getText().toString().trim();

        // validations for fields to be completed
        /*
        if (TextUtils.isEmpty(username)) {
            editUsername.setError("Please enter username");
            editUsername.requestFocus();
            return;
        }
        // email empty or invalid
        if (TextUtils.isEmpty(email)) {
            editEmail.setError("Please enter your email");
            editEmail.requestFocus();
            return;
        }
        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            editEmail.setError("Enter a valid email");
            editEmail.requestFocus();
            return;
        }

        if (TextUtils.isEmpty(password)) {
            editPassword.setError("Enter a password");
            editPassword.requestFocus();
            return;
        }

         */

        // if All validations are passed
        JSONObject registrationForm = new JSONObject();
        try {
            registrationForm.put("subject", "register");
            registrationForm.put("username", username);
            registrationForm.put("email", email);
            registrationForm.put("password", password);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        RequestBody body = RequestBody.create( registrationForm.toString(), parse("application/json; charset=utf-8"));

        postRequest(postUrl, body);

    } // eOD registerUser

    private void postRequest(String postUrl, RequestBody body) {

        OkHttpClient client = new OkHttpClient();

        final Request request = new Request.Builder()
                .url(postUrl)
                .post(body)
                .header("Accept", "application/json")
                .header("Content-Type", "application/json")
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                call.cancel();

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(getApplicationContext(),"Error connecting Flask server!",Toast.LENGTH_SHORT).show();
                    }
                });
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                try {
                    final String responseString = response.body().string().trim();
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            if (responseString.equals("success")) {
                                Toast.makeText(getApplicationContext(),
                                        "Registration completed successfully.", Toast.LENGTH_SHORT).show();
                                finish();
                            } else if (responseString.equals("failure")) {
                                Toast.makeText(getApplicationContext(),
                                        "Error by server", Toast.LENGTH_SHORT).show();
                            } else if (responseString.equals("username")) {
                                Toast.makeText(getApplicationContext(),
                                        "Username already exists.", Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(getApplicationContext(),
                                        "ERR: " + responseString, Toast.LENGTH_SHORT).show();

                            }
                        }
                    });
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } // eOf onResponse
            }); // eOf callRequest

    } // eOf postRequest
} // eOF RegisterActivity


package com.ahuzmeza.my_app;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.ahuzmeza.my_app.Helpers.SharedPrefManager;
import com.ahuzmeza.my_app.Helpers.Users_profile;

import org.jetbrains.annotations.NotNull;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.IOException;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import static okhttp3.MediaType.*;

public class LoginActivity extends AppCompatActivity {

    private static final String IPV4ADRESS = "192.168.1.6";
    private static final int PORT_NUMBER = 5000;

    static String postUrl = "http://" + IPV4ADRESS + ":" + PORT_NUMBER + "/";

    EditText editUsername;
    EditText editPassword;
    Button btnLogin;
    Button btnRegister;

    private String username;

    Users_profile u_profile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        editUsername = findViewById(R.id.tb_username);
        editPassword = findViewById(R.id.tb_password);
        btnLogin = findViewById(R.id.btn_login);
        btnRegister = findViewById(R.id.btn_register);

        // if the user is already logged in we will directly start the profile activity
        if (SharedPrefManager.getInstance(this).isLoggedIn()) {
            finish();
            startActivity(new Intent(this, MainActivity.class));
            return;
        }

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onClick(View v) {
                userLogin();
            }
        });

        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                startActivity(new Intent(getApplicationContext(), RegisterActivity.class));
            }
        });

    } // eOF onCreate

    // Methods =================================================================================

    /***************** *
     * LOGIN USER      *
     * *************** */
    private void userLogin() {
        //first getting the values
        username = editUsername.getText().toString();
        final String password = editPassword.getText().toString();

        //validating inputs
        if (TextUtils.isEmpty(username)) {
            editUsername.setError("Please enter your username");
            editUsername.requestFocus();
            return;
        }

        if (TextUtils.isEmpty(password)) {
            editPassword.setError("Please enter your password");
            editPassword.requestFocus();
            return;
        }

        // if All validations are passed
        JSONObject loginForm = new JSONObject();
        try {
            loginForm.put("subject", "login");
            loginForm.put("username", username);
            loginForm.put("password", password);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        //u_profile = new Users_profile(username, email);
        RequestBody body = RequestBody.create(loginForm.toString(), parse("application/json; charset=utf-8"));
        postRequest(postUrl, body);
    } // eOF userLogin


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
                        Toast.makeText(getApplicationContext(), "Error connecting Flask server!", Toast.LENGTH_SHORT).show();
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

                                Users_profile u_profile = new Users_profile(username, "tempemail");

                                SharedPrefManager.getInstance( getApplicationContext()).userLogin(u_profile);
                                startActivity(new Intent(getApplicationContext(), MainActivity.class));

                                finish();
                            } else if (responseString.equals("failure")) {
                                Toast.makeText(getApplicationContext(),
                                        "Error by server", Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(getApplicationContext(),
                                        "ERR: " + responseString, Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } // eOF on response
        }); // eOF call
    } // eOF postRequest
} // eOF LoginActivity
package com.ahuzmeza.my_app;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.text.InputType;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import com.ahuzmeza.my_app.Helpers.SharedPrefManager;
import com.ahuzmeza.my_app.Helpers.Subject;
import com.ahuzmeza.my_app.Helpers.SubjectsArrayAdapter;

import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

import static okhttp3.MediaType.parse;

public class FirstFragment extends Fragment {

    interface ApiCallback{
        void onOkHttpResponse(String data);
        void onOkHttpFailure(Exception e);
    }

    // server related vars
    /*
    private static final String IPV4ADRESS = "192.168.1.6";
    private static final int    PORT_NUMBER = 5000;
    static String postUrl = "http://"+IPV4ADRESS+":"+PORT_NUMBER+"/";
*/
    // Subject Array Adapter that will contail Subjects got from server
    SubjectsArrayAdapter subjectArrayAdapter;
    public List<Subject> l_subjects = new ArrayList<>();

    @Override
    public View onCreateView(
            LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_first, container, false);
    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        subjectArrayAdapter = new SubjectsArrayAdapter(getActivity(), R.layout.listview_row_layout);
        ListView listView = requireView().findViewById(R.id.listView);
        listView.setAdapter(subjectArrayAdapter);

        // get all the subjects from server
        try {
            subjectArrayAdapter.getAllSubjects();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        view.findViewById(R.id.fab_add).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openAddDialog();
            }
        });

        view.findViewById(R.id.button_first).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                NavHostFragment.findNavController(FirstFragment.this)
                        .navigate(R.id.action_FirstFragment_to_SecondFragment);
            }
        });
    }

    /*
    public void postSubject(String _name) {
        // get the username of logged in user
        String username = SharedPrefManager.getInstance( getActivity()).getUser().getUsername();
        // if All validations are passed
        JSONObject loginForm = new JSONObject();
        try {
            loginForm.put("subject", "insert_subject");
            loginForm.put("usr_username", username);
            loginForm.put("name", _name);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        //u_profile = new Users_profile(username, email);
        RequestBody body = RequestBody.create(loginForm.toString(), parse("application/json; charset=utf-8"));
        postSubject_Request(postUrl, body);
    }
    private void postSubject_Request(String postUrl, RequestBody body) {

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

                Toast.makeText(getActivity(), "Error connecting Flask server!", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                try {
                    final String responseString = response.body().string().trim();
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            switch (responseString) {
                                case "success":
                                    Toast.makeText(getActivity(),
                                            "Added successfully.", Toast.LENGTH_SHORT).show();
                                    break;
                                case "failure":
                                    Toast.makeText(getActivity(),
                                            "Error by server", Toast.LENGTH_SHORT).show();
                                    break;
                                case "subject_already_exists":
                                    Toast.makeText(getActivity(),
                                            "Subject already exists.", Toast.LENGTH_SHORT).show();
                                    break;
                                default:
                                    Toast.makeText(getActivity(),
                                            "ERR: " + responseString, Toast.LENGTH_SHORT).show();
                                    break;
                            }
                        }
                    });
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }); // eOF callback()
    } // eOF onPost 'POST'



    public void getAllSubjects() {

        //List<Subject> resultList = getAllSubjects();

        //List<Subject> resultList = new ArrayList<>();

        //if (elem_1.sameNameExists( l_subjects)) {
        //    Toast.makeText(getActivity(),
        //            "Subject already exists", Toast.LENGTH_SHORT).show();
        //    Log.d("Already exists error: ", elem_1.getSubjectName() + "Already exists");
        //}
        //else {

        //}


        String username = SharedPrefManager.getInstance( getActivity()).getUser().getUsername();
        // if All validations are passed
        JSONObject loginForm = new JSONObject();
        try {
            loginForm.put("subject", "get_all_subjects");
            loginForm.put("usr_username", username);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        //u_profile = new Users_profile(username, email);
        RequestBody body = RequestBody.create(loginForm.toString(), parse("application/json; charset=utf-8"));
        getAllSubjects_Request(postUrl, body, new ApiCallback() {
            @Override
            public void onOkHttpResponse(String _responseString) {
                Log.d("==== TEST ======", "data = " + _responseString);
                // Turn responseString to JSONArray
                // then for each JSON object in JSONArray
                //      get 'name' and add it to List<String> l_subjects
                //l_subjects.add(new Subject("aa",999));
                try {

                    JSONArray subjectsJson = new JSONArray(_responseString);
                    for (int index = 0; index < subjectsJson.length(); index++) {
                        JSONObject obj = subjectsJson.getJSONObject(index);
                        String subj_name = obj.getString("name").trim();

                        Subject got_subject = new Subject(subj_name, 0);
                        subjectArrayAdapter.add( got_subject);
                        //l_subjects.add(got_subject);
                        Log.i("-------- \n", got_subject.getSubjectName());
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            @Override
            public void onOkHttpFailure(Exception exception) {
                Log.d("!!! OkHttpFailure", "failed");
            }
        });

    }

    private void getAllSubjects_Request(String postUrl, RequestBody body, final ApiCallback callback) {

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
                Toast.makeText(getActivity(), "Error connecting Flask server!", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) {
                try {
                    final String responseString = response.body().string().trim();

                    //returnMyString(responseString);
                    Handler mainHandler = new Handler(Looper.getMainLooper());
                    mainHandler.post(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                callback.onOkHttpResponse( responseString);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    });

                    if ( responseString.equals( "no_subject_entries")) {
                        Log.d("--- NotFound entries", "no_subject_entries");
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }


        }); // eOF callback()

        for (Subject s : l_subjects) {
            Log.d("----- Got", s.getSubjectName() + "\n");
        }

    } // eOF onPost

    private void returnMyString(String myString) {
        //do your stuff
    }
*/

    public void openAddDialog() {

        AlertDialog.Builder builder = new AlertDialog.Builder( getActivity());
        builder.setTitle("New subject name:");

        // Set up the input
        final EditText input = new EditText( getActivity());
        // Specify the type of input expected; this, for example, sets the input as a password, and will mask the text
        input.setInputType(InputType.TYPE_CLASS_TEXT);
        builder.setView(input);

        // Set up the buttons
        builder.setPositiveButton("Add", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                try {
                    subjectArrayAdapter.postSubject( input.getText().toString().trim());
                    refreshAdapter();
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        builder.show();
    }

    /*  Method used to refresh the adapter
    *   by: clearAdapter_andItsList()
    *       - clears adapter's views content
    *       - clears list that contains adapter's content
    *   then gets all Subjects  */
    public void refreshAdapter() throws UnsupportedEncodingException {
        subjectArrayAdapter.clearAdapter_andItsList();
        subjectArrayAdapter.getAllSubjects();
    }

} // eOf FirstFragment



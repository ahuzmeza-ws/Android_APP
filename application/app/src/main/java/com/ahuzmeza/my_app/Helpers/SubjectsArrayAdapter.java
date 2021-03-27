package com.ahuzmeza.my_app.Helpers;
import com.ahuzmeza.my_app.R;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;

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

public class SubjectsArrayAdapter extends ArrayAdapter<Subject> {

    interface ApiCallback{
        void onOkHttpResponse(String data);
        void onOkHttpFailure(Exception e);
    }

    // server related vars
    private static final String IPV4ADRESS = "192.168.1.6";
    private static final int    PORT_NUMBER = 5000;
    static String postUrl = "http://"+IPV4ADRESS+":"+PORT_NUMBER+"/";

    private final List<Subject> subjectsList = new ArrayList<>();

    static class SubjectViewHolder {
        TextView subjectName;
        TextView subjectAverage;
    }

    public SubjectsArrayAdapter(Context context, int textViewResourceId) {
        super(context, textViewResourceId);
    }

    @Override
    public void add(Subject _new) {
        this.subjectsList.add(_new);
        super.add(_new);
        super.notifyDataSetChanged();
    }

    @Override
    public void remove(@Nullable Subject object) {
        super.remove(object);
        super.notifyDataSetChanged();
    }

    public void remove_index(int position){
        this.subjectsList.remove(subjectsList.get(position));
    }

    @Override
    public int getCount() {
        return this.subjectsList.size();
    }

    @Override
    public Subject getItem(final int index) {
        return this.subjectsList.get(index);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View row = convertView;
        SubjectViewHolder viewHolder;
        if (row == null) {
            LayoutInflater inflater = (LayoutInflater) this.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            row = inflater.inflate(R.layout.listview_row_layout, parent, false);

            viewHolder = new SubjectViewHolder();
            viewHolder.subjectName = row.findViewById(R.id.subject_name);
            viewHolder.subjectAverage = row.findViewById(R.id.subject_average);

            row.setTag(viewHolder);

        } else {
            viewHolder = (SubjectViewHolder)row.getTag();
        }

        Subject subj = getItem(position);
        viewHolder.subjectName.setText(subj.getSubjectName());
        viewHolder.subjectAverage.setText(Integer.toString( subj.getSubjectAverage()));

        Button deleteBtn = (Button)row.findViewById(R.id.delete_btn);
        deleteBtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                delete_row(position, viewHolder.subjectName.getText().toString());
            }
        });

        return row;
    }

    // Methods =================================================================================
    /***************** *
     * DELETE ROW      *
     * *************** */
    private void delete_row(int position, String subject_name) {

        DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which){
                    case DialogInterface.BUTTON_POSITIVE:
                        // Yes button clicked
                        subjectsList.remove(position);
                        break;
                    case DialogInterface.BUTTON_NEGATIVE:
                        // No button clicked
                        break;
                }
            }
        };

        String question_message = "Delete " + subject_name + " ?";
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setMessage( question_message).setPositiveButton("Yes", dialogClickListener)
                .setNegativeButton("No", dialogClickListener).show();

    }

    public void clearAdapter_andItsList() {
        this.clear();
        this.subjectsList.clear();
        this.notifyDataSetChanged();
    }


    public void postSubject(String _name) throws UnsupportedEncodingException {
        // get the username of logged in user
        String username = SharedPrefManager.getInstance( getContext()).getUser().getUsername();
        // if All validations are passed
        JSONObject postSubject_form = new JSONObject();
        try {
            postSubject_form.put("subject", "insert_subject");
            postSubject_form.put("usr_username", username);
            postSubject_form.put("name", _name);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        //u_profile = new Users_profile(username, email);
        RequestBody body = RequestBody.create( postSubject_form.toString().getBytes("UTF-8"));
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

                Toast.makeText(getContext(), "Error connecting Flask server!", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                try {
                    final String responseString = response.body().string().trim();

                    Log.d("---- onResponse_post", responseString);
                    /*getContext().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            switch (responseString) {
                                case "success":
                                    Toast.makeText( getContext(),
                                            "Added successfully.", Toast.LENGTH_SHORT).show();
                                    break;
                                case "failure":
                                    Toast.makeText( getContext(),
                                            "Error by server", Toast.LENGTH_SHORT).show();
                                    break;
                                case "subject_already_exists":
                                    Toast.makeText( getContext(),
                                            "Subject already exists.", Toast.LENGTH_SHORT).show();
                                    break;
                                default:
                                    Toast.makeText( getContext(),
                                            "ERR: " + responseString, Toast.LENGTH_SHORT).show();
                                    break;
                            }
                        }
                    }); */
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }); // eOF callback()
    } // eOF onPost 'POST'



    public void getAllSubjects() throws UnsupportedEncodingException {

        //List<Subject> resultList = getAllSubjects();

        //List<Subject> resultList = new ArrayList<>();

        //if (elem_1.sameNameExists( l_subjects)) {
        //    Toast.makeText(getActivity(),
        //            "Subject already exists", Toast.LENGTH_SHORT).show();
        //    Log.d("Already exists error: ", elem_1.getSubjectName() + "Already exists");
        //}
        //else {

        //}


        String username = SharedPrefManager.getInstance( getContext()).getUser().getUsername();
        // if All validations are passed
        JSONObject subject_Form = new JSONObject();
        try {
            subject_Form.put("subject", "get_all_subjects");
            subject_Form.put("usr_username", username);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        //u_profile = new Users_profile(username, email);
        RequestBody body = RequestBody.create( subject_Form.toString().getBytes("UTF-8"));
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
                        subjectsList.add( got_subject);
                        SubjectsArrayAdapter.super.add( got_subject);
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
//                Toast.makeText(getContext(), "Error connecting Flask server!", Toast.LENGTH_SHORT).show();
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


    } // eOF onPost

   // private void returnMyString(String myString) {
   //     //do your stuff
   // }





} // eOF SubjectsArrayAdapter

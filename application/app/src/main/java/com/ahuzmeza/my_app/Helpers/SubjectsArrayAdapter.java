package com.ahuzmeza.my_app.Helpers;
import com.ahuzmeza.my_app.R;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;

public class SubjectsArrayAdapter extends ArrayAdapter<Subject> {

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
    }

    @Override
    public void remove(@Nullable Subject object) {
        super.remove(object);
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
                notifyDataSetChanged();
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
                        //Yes button clicked
                        subjectsList.remove(position);
                        notifyDataSetChanged();
                        break;

                    case DialogInterface.BUTTON_NEGATIVE:
                        //No button clicked
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
        this.notifyDataSetChanged();
        this.subjectsList.clear();
    }

} // eOF SubjectsArrayAdapter

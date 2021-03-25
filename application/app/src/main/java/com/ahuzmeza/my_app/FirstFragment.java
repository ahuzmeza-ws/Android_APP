package com.ahuzmeza.my_app;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;
import com.ahuzmeza.my_app.Helpers.Subject;
import com.ahuzmeza.my_app.Helpers.SubjectsArrayAdapter;

import java.util.ArrayList;
import java.util.List;

public class FirstFragment extends Fragment {

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

        ListView listView = requireView().findViewById(R.id.listView);
        SubjectsArrayAdapter subjectArrayAdapter = new SubjectsArrayAdapter(getActivity(), R.layout.listview_row_layout);
        listView.setAdapter(subjectArrayAdapter);

        List<Subject> subjList = readData();
        for(Subject s : subjList ) {

            subjectArrayAdapter.add(s);
        }

        view.findViewById(R.id.button_first).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                NavHostFragment.findNavController(FirstFragment.this)
                        .navigate(R.id.action_FirstFragment_to_SecondFragment);
            }
        });
    }

    public List<Subject> readData(){
        List<Subject> resultList = new ArrayList<>();

        Subject elem_1 = new Subject("a", 1);
        Subject elem_2 = new Subject("b", 2);
        Subject elem_3 = new Subject("c", 3);
        Subject elem_4 = new Subject("d", 4);
        Subject elem_5 = new Subject("e", 5);
        Subject elem_6 = new Subject("f", 6);

        resultList.add(elem_1);
        resultList.add(elem_2);
        resultList.add(elem_3);
        resultList.add(elem_4);
        resultList.add(elem_5);
        resultList.add(elem_6);

        Subject elem_7 = new Subject("a", 888);
        if (elem_7.sameNameExists( resultList)) {
            Toast.makeText(getActivity(),
                    "Subject already exists", Toast.LENGTH_SHORT).show();
            Log.d("Already exists error: ", elem_7.getSubjectName() + "Already exists");
        }
        else {
            resultList.add(elem_7);
        }

        return  resultList;
    }


}
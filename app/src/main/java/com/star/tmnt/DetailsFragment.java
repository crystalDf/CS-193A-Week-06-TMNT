package com.star.tmnt;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.HashSet;
import java.util.Scanner;
import java.util.Set;


public class DetailsFragment extends Fragment {

    private Set<String> mDictionary;

    private Button mSubmitButton;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_details, container, false);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        Intent intent = getActivity().getIntent();

        String result = intent.getStringExtra(MainFragment.RESULT);

        setInfo(result);

        if (mDictionary == null) {
            loadDictionary();
        }

        mSubmitButton = (Button) getActivity().findViewById(R.id.submit);

        mSubmitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText editText = (EditText) getActivity().findViewById(R.id.word);

                String word = editText.getText().toString();

                if (mDictionary.contains(word)) {
                    Intent i = new Intent();
                    i.putExtra("word", word);
                    getActivity().setResult(Activity.RESULT_OK, i);
                    if (getResources().getConfiguration().orientation ==
                            Configuration.ORIENTATION_PORTRAIT) {
                        getActivity().finish();
                    } else {
                        Toast.makeText(getActivity(), "You typed " + word,
                                Toast.LENGTH_LONG).show();
                    }
                } else {
                    Toast.makeText(getActivity(), "Not in dictionary.",
                            Toast.LENGTH_LONG).show();
                }
            }
        });

    }

    public void setInfo(String result) {
        String[] turtles = getResources().getStringArray(R.array.turtles);
        String[] infos = getResources().getStringArray(R.array.infos);
        for (int i = 0; i < turtles.length; i++) {
            if (turtles[i].equals(result)) {
                TextView infoTextView = (TextView) getActivity().findViewById(R.id.turtle_details);
                infoTextView.setText(getResources().getIdentifier(
                        infos[i], "string", getActivity().getPackageName()));
                return;
            }
        }
    }

    private void loadDictionary() {
        mDictionary = new HashSet<>();

        Scanner scanner = new Scanner(getResources().openRawResource(R.raw.dict));

        while (scanner.hasNext()) {
            mDictionary.add(scanner.nextLine());
        }
    }
}

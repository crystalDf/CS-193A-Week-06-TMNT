package com.star.tmnt;

import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;


public class MainFragment extends Fragment {

    public static final String RESULT = "RESULT";

    public static final String TURTLE_ID = "TURTLE_ID";

    public static final int GET_DETAILS = 0;
    public static final int TAKE_PHOTO = 1;

    private Button mTakePhotoButton;

    private Spinner mSpinner;
    private ImageView mImageView;

    private MediaPlayer mMediaPlayer;

    private String mResultString;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_main, container, false);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        mSpinner = (Spinner) getActivity().findViewById(R.id.spinner);
        mSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                mResultString = parent.getSelectedItem().toString();
                setResult(mResultString);
                setImageView(mResultString);
                setInfo(mResultString);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        mImageView = (ImageView) getActivity().findViewById(R.id.imageView);
        mImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (getResources().getConfiguration().orientation ==
                        Configuration.ORIENTATION_LANDSCAPE) {
                    DetailsFragment detailsFragment = (DetailsFragment)
                            getFragmentManager().findFragmentById(R.id.fragment2);
                    detailsFragment.setInfo(mResultString);
                } else {
                    Intent intent = new Intent(getActivity(), DetailsActivity.class);
                    intent.putExtra(RESULT, mResultString);
                    startActivityForResult(intent, GET_DETAILS);
                }
            }
        });

        mTakePhotoButton = (Button) getActivity().findViewById(R.id.take_photo);
        mTakePhotoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(intent, TAKE_PHOTO);
            }
        });

        mMediaPlayer = MediaPlayer.create(getActivity(), R.raw.tmnt_theme);
    }

    @Override
    public void onResume() {
        super.onResume();

        if (mMediaPlayer != null) {
            mMediaPlayer.setLooping(true);
            mMediaPlayer.start();
        }
    }

    @Override
    public void onPause() {
        mMediaPlayer.pause();

        super.onPause();
    }

    @Override
    public void onDestroy() {

        if (mMediaPlayer != null) {
            mMediaPlayer.stop();
            mMediaPlayer.release();
        }

        super.onDestroy();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        outState.putInt(TURTLE_ID, mSpinner.getSelectedItemPosition());

    }

    @Override
    public void onViewStateRestored(@Nullable Bundle savedInstanceState) {
        // without will lose power
        super.onViewStateRestored(savedInstanceState);
        // without will be also OK
//        mSpinner.setSelection(savedInstanceState.getInt(TURTLE_ID));
    }

    private void setResult(String result) {
        TextView resultTextView = (TextView) getActivity().findViewById(R.id.textView);
        resultTextView.setText("You chose " + result);
    }

    private void setImageView(String result) {
        String[] turtles = getResources().getStringArray(R.array.turtles);
        String[] images = getResources().getStringArray(R.array.images);
        for (int i = 0; i < turtles.length; i++) {
            if (turtles[i].equals(result)) {
                ImageView imageView = (ImageView) getActivity().findViewById(R.id.imageView);
                imageView.setImageResource(getResources().getIdentifier(
                        images[i], "mipmap", getActivity().getPackageName()));
                return;
            }
        }
    }

    private void setInfo(String result) {
        String[] turtles = getResources().getStringArray(R.array.turtles);
        String[] infos = getResources().getStringArray(R.array.infos);
        for (int i = 0; i < turtles.length; i++) {
            if (turtles[i].equals(result)) {
                TextView infoTextView = (TextView) getActivity().findViewById(R.id.turtle_info);
                infoTextView.setText(getResources().getIdentifier(
                        infos[i], "string", getActivity().getPackageName()));
                return;
            }
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case GET_DETAILS:
                if (resultCode == getActivity().RESULT_OK) {
                    String word = data.getStringExtra("word");
                    Toast.makeText(getActivity(), "You typed " + word, Toast.LENGTH_LONG).show();
                }
                break;
            case TAKE_PHOTO:
                if (resultCode == getActivity().RESULT_OK) {
                    Bitmap bitmap = (Bitmap) data.getExtras().get("data");
                    mImageView.setImageBitmap(bitmap);
                }
        }
    }
}

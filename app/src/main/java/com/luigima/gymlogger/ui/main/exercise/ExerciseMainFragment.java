package com.luigima.gymlogger.ui.main.exercise;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.luigima.gymlogger.R;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ExerciseMainFragment extends Fragment {

    private OnFragmentInteractionListener mListener;

    @BindView(R.id.textViewCategory)
    TextView textViewCategory;
    @BindView(R.id.textViewExercise)
    TextView textViewExercise;

    public ExerciseMainFragment() {
        // Required empty public constructor
    }

    public static ExerciseMainFragment newInstance() {
        return new ExerciseMainFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_exercise_main, container, false);
        ButterKnife.bind(this, view);

        return view;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onExerciseMainFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onExerciseMainFragmentInteraction(Uri uri);
    }
}

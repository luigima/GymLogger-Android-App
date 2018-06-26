package com.luigima.gymlogger.ui.main.exercise;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.luigima.gymlogger.GymLoggerApp;
import com.luigima.gymlogger.R;
import com.luigima.gymlogger.data.db.entities.Exercise;
import com.luigima.gymlogger.util.ImageOverlayTransformationBuilder;
import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.Picasso;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class DetailsFragment extends Fragment
        implements DetailsMvpView {

    private static final String ARG_EXERCISE_ID = "exerciseId";
    private static final String ARG_WORKOUT_ID = "workoutId";

    @Inject
    Picasso picasso;

    @Inject
    DetailsPresenter presenter;

    @BindView(R.id.txtExerciseTitle)
    TextView txtExerciseTitle;
    @BindView(R.id.imgMuscles)
    ImageView imgMuscles;
    @BindView(R.id.imgExercise1)
    ImageView imgExercise1;
    @BindView(R.id.imgExercise2)
    ImageView imgExercise2;
    @BindView(R.id.txtExerciseDescription)
    TextView txtExerciseDescription;
    @BindView(R.id.txtMuscles)
    TextView txtMuscles;
    @BindView(R.id.txtEquipment)
    TextView txtEquipment;

    private int exerciseId;
    private Unbinder butterKnife;

    private OnFragmentInteractionListener mListener;

    public DetailsFragment() {
        // Required empty public constructor
    }

    public static DetailsFragment newInstance(int exerciseId) {
        DetailsFragment fragment = new DetailsFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_EXERCISE_ID, exerciseId);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            exerciseId = getArguments().getInt(ARG_EXERCISE_ID);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_active_workout_exercise_details, container, false);
        GymLoggerApp.getComponent(view.getContext().getApplicationContext()).inject(this);
        butterKnife =ButterKnife.bind(this, view);

        setHasOptionsMenu(true);
        setHasOptionsMenu(true);
        presenter.attachView(this);

        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        presenter.loadWorkoutExercise(exerciseId);
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
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

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        butterKnife.unbind();
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }

    @Override
    public void showError() {

    }

    @Override
    public void showExercise(Exercise exercise) {
        txtExerciseTitle.setText(exercise.getTitle());
        txtExerciseDescription.setText(exercise.getSteps());

        txtMuscles.setText(exercise.getMuscleListString());
        txtEquipment.setText(exercise.getEquipmentListString());

        ImageOverlayTransformationBuilder imageOverlayTransformationBuilder =
                new ImageOverlayTransformationBuilder(getContext(), exercise.getCategories());
        picasso.load("file:///android_asset/images/body/example/base.png")
                .transform(imageOverlayTransformationBuilder.getTransformation()).memoryPolicy(MemoryPolicy.NO_CACHE)
                .into(imgMuscles);

        //TODO remove replacement. concatenate strings according to best practices
        picasso.load("file:///android_asset/images/" + exercise.getImage1().replace(".jpg",".png"))
                .into(imgExercise1);
        picasso.load("file:///android_asset/images/" + exercise.getImage2().replace(".jpg",".png"))
                .into(imgExercise2);
    }
}

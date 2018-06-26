package com.luigima.gymlogger.ui.main.routine.details;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import com.luigima.gymlogger.GymLoggerApp;
import com.luigima.gymlogger.R;
import com.luigima.gymlogger.data.db.entities.Routine;
import com.luigima.gymlogger.ui.main.workout.ActiveWorkoutActivity;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import timber.log.Timber;

public class RoutineDetailsFragment extends Fragment implements RoutineDetailsMvpView {
    private static final String ARG_ROUTINE_ID = "routineId";

    @Inject
    RoutineDetailsPresenter presenter;

    @BindView(R.id.textView3)
    TextView textView;
    @BindView(R.id.editText_split)
    EditText editTextSplit;

    private int routineId;
    private Routine routine;
    private OnFragmentInteractionListener mListener;

    public RoutineDetailsFragment() {
        // Required empty public constructor
    }

    public static RoutineDetailsFragment newInstance(int routineId) {
        RoutineDetailsFragment fragment = new RoutineDetailsFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_ROUTINE_ID, routineId);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            routineId = getArguments().getInt(ARG_ROUTINE_ID);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_routine_details, container, false);
        GymLoggerApp.getComponent(view.getContext().getApplicationContext()).inject(this);
        ButterKnife.bind(this, view);
        setHasOptionsMenu(true);
        presenter.attachView(this);

        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        presenter.loadRoutine(routineId);
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
        String FRAGMENT_TAG = "RoutineDetailsFragment";

        void onEditRoutineButtonClicked(int routineId);

        void onRoutineDeleted(int routineId);
    }

    @OnClick(R.id.btn_start)
    public void btnStartClicked() {
        Timber.d("Starting new workout..");

        Integer split = Integer.valueOf(editTextSplit.getText().toString());
        if (routine != null) {
            presenter.startNewWorkout(routine, routine.getSplits().get(split - 1).getSplitId());
        }
    }

    @OnClick(R.id.btn_edit)
    public void btnEditClicked() {
        Timber.d("Routine editing started");
        mListener.onEditRoutineButtonClicked(routineId);
    }

    @OnClick(R.id.btn_delete)
    public void btnDeleteClicked() {
        Timber.d("Routine delete clicked");
        presenter.deleteRoutine(routineId);
    }

    @Override
    public void showError() {

    }

    @Override
    public void onRoutineLoaded(Routine routine) {
        this.routine = routine;
        textView.setText("id: " + routine.getRoutineID() + "\n" +
                "title: " + routine.getTitle() + "\n" +
                "description: " + routine.getDescription() + "\n" +
                //"Number of exercises (split 0): " + routine.getSplits().get(0).getExercises().size() + "\n" +
                "Creation date: " + routine.getCreationDate() + "\n" +
                "splits: " + routine.getSplits().size() + "\n" +
                "difficulty: " + routine.getDifficulty() + "\n" +
                "creatorId: " + routine.getCreatorId() + "\n" +
                "isLocal: " + routine.isLocal() + "\n" +
                "rating: " + routine.getRating() + "\n" +
                "workoutcategoryId: " + routine.getRoutineCategoryId());
    }

    @Override
    public void onRoutineDeleted(int routineId) {
        mListener.onRoutineDeleted(routineId);
    }

    @Override
    public void onNewWorkoutStarted(int workoutId, int split) {
        Intent intent = new Intent(getActivity(), ActiveWorkoutActivity.class);
        intent.putExtra(ActiveWorkoutActivity.ARG_WORKOUT_ID, workoutId);
        startActivity(intent);
    }
}

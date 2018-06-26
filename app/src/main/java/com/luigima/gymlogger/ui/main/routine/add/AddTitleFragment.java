package com.luigima.gymlogger.ui.main.routine.add;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.luigima.gymlogger.GymLoggerApp;
import com.luigima.gymlogger.R;
import com.luigima.gymlogger.data.db.entities.Routine;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import timber.log.Timber;

public class AddTitleFragment extends Fragment implements AddRoutineMvpView {
    public static final String ARG_ROUTINE_ID = "routineId";

    @BindView(R.id.editTextTitle)
    EditText editTextTitle;

    @BindView(R.id.editTextDescription)
    EditText editTextDescription;

    @Inject
    AddRoutinePresenter presenter;

    private OnFragmentInteractionListener mListener;
    private int routineId;
    private Routine routine;
    private Unbinder butterKnife;

    public AddTitleFragment() {
        // Required empty public constructor
    }

    public static AddTitleFragment newInstance(int routineId) {
        AddTitleFragment fragment = new AddTitleFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_ROUTINE_ID, routineId);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_add_routine_title, container, false);
        GymLoggerApp.getComponent(view.getContext().getApplicationContext()).inject(this);
        butterKnife = ButterKnife.bind(this,view);
        presenter.attachView(this);


        editTextTitle.setTextColor(ContextCompat.getColor(getContext(), R.color.colorPrimaryLightText));
        editTextDescription.setTextColor(ContextCompat.getColor(getContext(), R.color.colorPrimaryLightText));

        if (getArguments() != null) {
            routineId = getArguments().getInt(ARG_ROUTINE_ID);
        } else {
            routineId = -1;
        }

        if (routineId != -1) {
            // We are editing a routine, lets load the existing data
            Timber.d("Editing existing routine with id %d. Loading data...", routineId);
            presenter.getRoutine(routineId);
        } else {
            Timber.d("Seems to be a new routine!");
        }

        return view;
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
        presenter.unsubscribe();
        mListener = null;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        butterKnife.unbind();
        presenter.unsubscribe();
    }

    @OnClick(R.id.buttonNext)
    public void submit() {
        String title = editTextTitle.getText().toString();
        String description = editTextDescription.getText().toString();

        if (routine != null) {
            routine.setTitle(title);
            routine.setDescription(description);
            presenter.saveRoutine(routine);
            if (mListener != null) {
                mListener.onTitleAndDescriptionSet(routineId);
            }
        } else {
            Timber.d("Creating a new routine");
            presenter.createNewRoutine(title, description);
        }
    }

    public interface OnFragmentInteractionListener {
        void onTitleAndDescriptionSet(int routineId);
    }

    @Override
    public void showError() {

    }

    @Override
    public void onRoutineLoaded(Routine routine) {
        Timber.d("Loading data for routine with id %d finished. Title: %s",
                routine.getRoutineID(), routine.getTitle());
        this.routine = routine;
        editTextTitle.setText(routine.getTitle());
        editTextDescription.setText(routine.getDescription());
    }

    @Override
    public void onRoutineCreated(int id) {
        if (mListener != null) {
            mListener.onTitleAndDescriptionSet(id);
        }
    }

    @Override
    public void onSplitRenamed(boolean wasUpdated) {

    }
}

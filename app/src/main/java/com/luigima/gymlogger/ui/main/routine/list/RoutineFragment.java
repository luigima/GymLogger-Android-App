package com.luigima.gymlogger.ui.main.routine.list;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.view.ViewGroup;
import android.view.ViewGroupOverlay;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.luigima.gymlogger.GymLoggerApp;
import com.luigima.gymlogger.R;
import com.luigima.gymlogger.data.db.entities.Routine;
import com.luigima.gymlogger.ui.main.MainActivity;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import timber.log.Timber;

public class RoutineFragment extends Fragment implements RoutineMvpView {

    @BindView(R.id.recyclerview)
    RecyclerView recyclerView;

    @BindView(R.id.main_content)
    FrameLayout container;

    @Inject
    RoutinePresenter presenter;

    RoutineFragment.OnFragmentInteractionListener mListener;
    private RoutineDataAdapter routineDataAdapter;
    private RecyclerView.LayoutManager layoutManager;
    private List<Routine> mData;
    private static FloatingActionButton floatingActionButton;

    public static RoutineFragment newInstance(FloatingActionButton fab) {
        RoutineFragment fragment = new RoutineFragment();
        floatingActionButton = fab;
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mData = new ArrayList<>();

        routineDataAdapter = new RoutineDataAdapter(mData);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.recyclerview_fragment, container, false);
        GymLoggerApp.getComponent(view.getContext().getApplicationContext()).inject(this);
        ButterKnife.bind(this, view);

        presenter.attachView(this);

        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        // From google docs
        // use this setting to improve performance if you know that changes
        // in content do not change the layout size of the RecyclerView
        recyclerView.setHasFixedSize(false);

        layoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);

        recyclerView.setAdapter(routineDataAdapter);
        presenter.loadRoutines();

        routineDataAdapter.setOnItemClickListener((imageView, routine) -> {
            // Activity will show a detailed exercise fragment
            mListener.onRoutineFragmentShowDetailsInteraction(imageView, routine.getRoutineID());
        });

        if (floatingActionButton != null) {
            floatingActionButton.setOnClickListener(v-> {
                showRevealEffect();
            });
        } else {
            Timber.e("floatingActionButton is null");
        }


    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == MainActivity.ACTIVITY_RESULT_CODE) {
            if (resultCode == Activity.RESULT_OK) {
                int insertedId = data.getIntExtra("insertedId", -1);
                if(insertedId != -1) {
                    presenter.loadRoutineById(insertedId);
                }
            }
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);    /*
        // save current state to support screen rotation, etc...
        if (recyclerViewExpandableItemManager != null) {
            outState.putParcelable(
                    SAVED_STATE_EXPANDABLE_ITEM_MANAGER,
                    recyclerViewExpandableItemManager.getSavedState());
        }*/
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof RoutineFragment.OnFragmentInteractionListener) {
            mListener = (RoutineFragment.OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDestroyView() {
        /*if (recyclerViewExpandableItemManager != null) {
            recyclerViewExpandableItemManager.release();
            recyclerViewExpandableItemManager = null;
        }

        if (recyclerView != null) {
            recyclerView.setItemAnimator(null);
            recyclerView.setAdapter(null);
            recyclerView = null;
        }

        if (exerciseDataAdapter != null) {
            WrapperAdapterUtils.releaseAll(exerciseDataAdapter);
            exerciseDataAdapter = null;
        }
        layoutManager = null;
*/
        super.onDestroyView();
    }

    public void showRevealEffect() {
        ViewGroupOverlay overlay = container.getOverlay();

        final View revealView = new View(getContext());
        revealView.setBottom(container.getHeight());
        revealView.setRight(container.getWidth());
        revealView.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.colorAccent));

        overlay.add(revealView);
        float radius = (float) Math.sqrt(Math.pow(container.getHeight(), 2) + Math.pow(container.getWidth(), 2));
        Animator revealAnimator =
                ViewAnimationUtils.createCircularReveal(revealView,
                        revealView.getWidth(), revealView.getHeight(), 0.0f, radius);
        revealAnimator.setDuration(getResources().getInteger(android.R.integer.config_mediumAnimTime));

        AnimatorSet animatorSet = new AnimatorSet();
        animatorSet.play(revealAnimator);
        animatorSet.setInterpolator(new AccelerateDecelerateInterpolator());
        animatorSet.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                Timber.d("Reveal animation finished");
                mListener.onAddRoutineButtonClicked();
            }
        });
        animatorSet.start();
    }


    public interface OnFragmentInteractionListener {
        String FRAGMENT_TAG = "RoutineFragment";
        void onRoutineFragmentShowDetailsInteraction(ImageView imageView, int id);
        void onAddRoutineButtonClicked();
    }

    /*****
     * MVP View methods implementation
     *****/
    @Override
    public void showRoutines(List<Routine> mData) {
        Timber.d(mData.size() + "");
    }

    @Override
    public void addRoutine(Routine routine) {
        routineDataAdapter.addRoutine(routine);
    }

    @Override
    public void setData(List<Routine> routines) {
        routineDataAdapter.setData(routines);
    }

    @Override
    public void clearRoutines() {
        mData = new ArrayList<>();
        routineDataAdapter.setData(mData);
    }

    @Override
    public void showError() {

    }

}

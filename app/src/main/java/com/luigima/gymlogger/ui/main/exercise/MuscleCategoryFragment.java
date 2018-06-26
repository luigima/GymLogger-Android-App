package com.luigima.gymlogger.ui.main.exercise;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.luigima.gymlogger.GymLoggerApp;
import com.luigima.gymlogger.R;
import com.luigima.gymlogger.data.db.entities.MainMuscleGroup;
import com.luigima.gymlogger.data.db.entities.sql.DBMuscle;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MuscleCategoryFragment extends Fragment implements MuscleCategoryMvpView {
    private static final String SAVED_STATE_EXPANDABLE_ITEM_MANAGER = "RecyclerViewExpandableItemManager";

    private OnFragmentInteractionListener mListener;

    @Inject
    MuscleCategoryPresenter presenter;

    @BindView(R.id.recyclerviewCategories)
    RecyclerView recyclerView;

    private MuscleCategoryDataAdapter exerciseCategoryDataAdapter;
    private RecyclerView.LayoutManager layoutManager;
    private List<MainMuscleGroup> mCategoryData;

    public MuscleCategoryFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment MuscleCategoryFragment.
     */
    public static MuscleCategoryFragment newInstance() {
        return new MuscleCategoryFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_exercise_category, container, false);
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


        /*/ additional decorations
        //noinspection StatementWithEmptyBody
        if (supportsViewElevation()) {
            // Lollipop or later has native drop shadow feature. ItemShadowDecorator is not required.
        } else {
            recyclerView.addItemDecoration(new ItemShadowDecorator((NinePatchDrawable) ContextCompat.getDrawable(getContext(), R.drawable.material_shadow_z1)));
        }
        recyclerView.addItemDecoration(new SimpleListDividerDecorator(ContextCompat.getDrawable(getContext(), R.drawable.list_divider_h), true));
        */
        mCategoryData = new ArrayList<>();
        exerciseCategoryDataAdapter = new MuscleCategoryDataAdapter(mCategoryData);
        recyclerView.setAdapter(exerciseCategoryDataAdapter);
        presenter.loadCategories();

        exerciseCategoryDataAdapter.setOnItemClickListener((view1, position, mainExerciseCategory) -> {
            if (mListener != null && mainExerciseCategory.getMuscles() != null) {
                // Collect all exercise category ids
                List<Integer> idList = new ArrayList<>();
                for (DBMuscle exerciseCategory : mainExerciseCategory.getMuscles()) {
                    idList.add(exerciseCategory.getId());
                }
                mListener.onExerciseCategoryFragmentShowExercisesInteraction(idList);
            }
        });
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
        String FRAGMENT_TAG = "MuscleCategoryFragment";
        void onExerciseCategoryFragmentShowExercisesInteraction(List<Integer> exerciseCategories);
    }

    /*****
     * MVP View methods implementation
     *****/

    @Override
    public void showError() {

    }

    @Override
    public void showCategories(List<MainMuscleGroup> mData) {
        exerciseCategoryDataAdapter.setData(mData);
    }
}

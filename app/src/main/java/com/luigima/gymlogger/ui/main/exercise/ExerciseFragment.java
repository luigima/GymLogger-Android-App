package com.luigima.gymlogger.ui.main.exercise;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.drawable.NinePatchDrawable;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;

import com.h6ah4i.android.widget.advrecyclerview.decoration.ItemShadowDecorator;
import com.h6ah4i.android.widget.advrecyclerview.decoration.SimpleListDividerDecorator;
import com.jakewharton.rxbinding.support.v7.widget.RxRecyclerView;
import com.jakewharton.rxbinding.support.v7.widget.RxRecyclerViewAdapter;
import com.jakewharton.rxbinding.support.v7.widget.RxSearchView;
import com.jakewharton.rxbinding.support.v7.widget.RxToolbar;
import com.jakewharton.rxbinding.view.RxMenuItem;
import com.luigima.gymlogger.GymLoggerApp;
import com.luigima.gymlogger.R;
import com.luigima.gymlogger.data.db.entities.Exercise;
import com.luigima.gymlogger.ui.main.MainActivity;
import com.luigima.gymlogger.util.ThemeUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import timber.log.Timber;

import static com.luigima.gymlogger.util.ThemeUtils.getThemeColor;

public class ExerciseFragment extends Fragment implements ExerciseMvpView {
    private static final String SAVED_STATE_EXPANDABLE_ITEM_MANAGER = "RecyclerViewExpandableItemManager";
    private static final String ARG_PARAM_CATIDS = "CatIds";
    private static final String ARG_PARAM_VIEW_TYPE = "ViewType";
    public static final int VIEW_TYPE_NORMAL = 0;
    public static final int VIEW_TYPE_ADD = 1;

    @BindView(R.id.recyclerview)
    RecyclerView recyclerView;

    @Inject
    ExercisePresenter presenter;

    ExerciseFragment.OnFragmentInteractionListener mListener;
    private ExerciseDataAdapter exerciseDataAdapter;
    private RecyclerView.LayoutManager layoutManager;
    private MenuItem mSearchItem;
    private Toolbar mToolbar;
    private List<Exercise> mData;
    private ArrayList<Integer> exerciseCategoryIds;
    private int viewType;


    public static ExerciseFragment newInstance(List<Integer> exerciseCategoryIds, int viewType) {
        ExerciseFragment fragment = new ExerciseFragment();
        Bundle args = new Bundle();
        args.putIntegerArrayList(ARG_PARAM_CATIDS, (ArrayList<Integer>) exerciseCategoryIds);
        args.putInt(ARG_PARAM_VIEW_TYPE, viewType);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            exerciseCategoryIds = getArguments().getIntegerArrayList(ARG_PARAM_CATIDS);
            viewType = getArguments().getInt(ARG_PARAM_VIEW_TYPE);
        } else {
            viewType = 0;
        }

        mToolbar = (Toolbar) getActivity().findViewById(R.id.toolbar);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.recyclerview_fragment, container, false);
        GymLoggerApp.getComponent(view.getContext().getApplicationContext()).inject(this);
        ButterKnife.bind(this, view);
        setHasOptionsMenu(true);

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


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            // Lollipop or later has native drop shadow feature. ItemShadowDecorator is not required.
        } else {
            recyclerView.addItemDecoration(new ItemShadowDecorator((NinePatchDrawable) ContextCompat.getDrawable(getContext(), R.drawable.material_shadow_z1)));
        }
        recyclerView.addItemDecoration(new SimpleListDividerDecorator(ContextCompat.getDrawable(getContext(), R.drawable.list_divider_h), true));

        mData = new ArrayList<>();

        exerciseDataAdapter = new ExerciseDataAdapter(mData, viewType);
        recyclerView.setAdapter(exerciseDataAdapter);
        presenter.loadExercisesByCategoryIds(exerciseCategoryIds);


        exerciseDataAdapter.setOnItemClickListener(new ExerciseDataAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(ImageView imageView, Exercise exercise) {
                // Activity will show a detailed exercise fragment
                mListener.onExerciseFragmentShowDetailsInteraction(imageView, exercise.getId());
            }

            @Override
            public void onAddButtonClicked(Exercise exercise) {
                mListener.onAddButtonClicked(exercise);
            }
        });
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.list_exercise, menu);
        menu.setGroupVisible(R.id.main_menu_group, false);

        mSearchItem = menu.findItem(R.id.m_search);

        MenuItemCompat.setOnActionExpandListener(mSearchItem, new MenuItemCompat.OnActionExpandListener() {
            @Override
            public boolean onMenuItemActionCollapse(MenuItem item) {
                // Called when SearchView is collapsing
                if (mSearchItem.isActionViewExpanded()) {
                    animateSearchToolbar(1, false, false);
                }
                return true;
            }

            @Override
            public boolean onMenuItemActionExpand(MenuItem item) {
                // Called when SearchView is expanding
                animateSearchToolbar(1, true, true);
                return true;
            }
        });

        // Associate searchable configuration with the SearchView

        SearchView searchView =
                (SearchView) mSearchItem.getActionView().findViewById(R.id.search_view);
        RxSearchView.queryTextChanges(searchView)
                .filter(charSequence ->
                        !TextUtils.isEmpty(charSequence))
                .throttleLast(100, TimeUnit.DAYS.MILLISECONDS)
                .debounce(200, TimeUnit.MILLISECONDS)
                .onBackpressureLatest()
                .subscribe(searchTerm ->
                        presenter.search(searchTerm.toString().toLowerCase()));

        super.onCreateOptionsMenu(menu, inflater);
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
        if (context instanceof ExerciseFragment.OnFragmentInteractionListener) {
            mListener = (ExerciseFragment.OnFragmentInteractionListener) context;
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

    public void animateSearchToolbar(int numberOfMenuIcon, boolean containsOverflow, boolean show) {

        mToolbar.setBackgroundColor(ContextCompat.getColor(getContext(), android.R.color.white));

        if (show) {
            int width = mToolbar.getWidth() -
                    (containsOverflow ? getResources().getDimensionPixelSize(R.dimen.abc_action_button_min_width_overflow_material) : 0) -
                    ((getResources().getDimensionPixelSize(R.dimen.abc_action_button_min_width_material) * numberOfMenuIcon) / 2);
            Animator createCircularReveal = ViewAnimationUtils.createCircularReveal(mToolbar,
                    isRtl(getResources()) ? mToolbar.getWidth() - width : width, mToolbar.getHeight() / 2, 0.0f, (float) width);
            createCircularReveal.setDuration(250);
            createCircularReveal.addListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    super.onAnimationEnd(animation);
                    getActivity().getWindow().setStatusBarColor(ContextCompat.getColor(getContext(), R.color.quantum_grey_600));
                }
            });
            createCircularReveal.start();

        } else {
            int width = mToolbar.getWidth() -
                    (containsOverflow ? getResources().getDimensionPixelSize(R.dimen.abc_action_button_min_width_overflow_material) : 0) -
                    ((getResources().getDimensionPixelSize(R.dimen.abc_action_button_min_width_material) * numberOfMenuIcon) / 2);
            Animator createCircularReveal = ViewAnimationUtils.createCircularReveal(mToolbar,
                    isRtl(getResources()) ? mToolbar.getWidth() - width : width, mToolbar.getHeight() / 2, (float) width, 0.0f);
            createCircularReveal.setDuration(250);
            createCircularReveal.addListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    super.onAnimationEnd(animation);
                    mToolbar.setBackgroundColor(getThemeColor(getContext(), R.attr.colorPrimary));
                    getActivity().getWindow().setStatusBarColor(ContextCompat.getColor(getContext(), R.color.colorPrimaryDark));
                }
            });
            createCircularReveal.start();
        }
    }

    private boolean isRtl(Resources resources) {
        return resources.getConfiguration().getLayoutDirection() == View.LAYOUT_DIRECTION_RTL;
    }

    public interface OnFragmentInteractionListener {
        String FRAGMENT_TAG = "ExerciseFragment";

        void onExerciseFragmentShowDetailsInteraction(ImageView imageView, int id);

        void onAddButtonClicked(Exercise exercise);
    }

    /*****
     * MVP View methods implementation
     *****/
    @Override
    public void showExercises(List<Exercise> mData) {
        exerciseDataAdapter.setData(mData);
    }

    @Override
    public void addExercises(List<Exercise> mData) {
        exerciseDataAdapter.addData(mData);
    }

    @Override
    public void searchExercises(String searchQuery) {
        exerciseDataAdapter.setSearchQuery(searchQuery);
    }

    @Override
    public void showError() {

    }

}

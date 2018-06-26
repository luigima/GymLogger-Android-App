package com.luigima.gymlogger.ui.main.workout.overview;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.NinePatchDrawable;
import android.os.Build;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.h6ah4i.android.widget.advrecyclerview.animator.GeneralItemAnimator;
import com.h6ah4i.android.widget.advrecyclerview.animator.SwipeDismissItemAnimator;
import com.h6ah4i.android.widget.advrecyclerview.decoration.ItemShadowDecorator;
import com.h6ah4i.android.widget.advrecyclerview.decoration.SimpleListDividerDecorator;
import com.h6ah4i.android.widget.advrecyclerview.draggable.RecyclerViewDragDropManager;
import com.h6ah4i.android.widget.advrecyclerview.expandable.RecyclerViewExpandableItemManager;
import com.h6ah4i.android.widget.advrecyclerview.swipeable.RecyclerViewSwipeManager;
import com.h6ah4i.android.widget.advrecyclerview.touchguard.RecyclerViewTouchActionGuardManager;
import com.h6ah4i.android.widget.advrecyclerview.utils.RecyclerViewAdapterUtils;
import com.luigima.gymlogger.GymLoggerApp;
import com.luigima.gymlogger.R;
import com.luigima.gymlogger.data.db.entities.Workout;
import com.luigima.gymlogger.ui.base.advancedrecyclerview.AbstractExpandableDataProvider;
import com.luigima.gymlogger.ui.main.workout.ActiveWorkoutActivity;
import com.luigima.gymlogger.ui.main.workout.WorkoutExercisesAdapter;
import com.luigima.gymlogger.ui.main.workout.WorkoutExercisesProvider;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import timber.log.Timber;


public class ActiveWorkoutListFragment extends Fragment
        implements ActiveWorkoutListMvpView,
        RecyclerViewExpandableItemManager.OnGroupCollapseListener,
        RecyclerViewExpandableItemManager.OnGroupExpandListener {
    private static final String ARG_WORKOUT_ID = "workoutId";
    private static final String SAVED_STATE_EXPANDABLE_ITEM_MANAGER = "RecyclerViewExpandableItemManager";

    @BindView(R.id.recyclerview)
    RecyclerView recyclerview;

    @Inject
    ActiveWorkoutListPresenter presenter;

    private RecyclerView.LayoutManager layoutManager;
    private RecyclerView.Adapter adapter;
    private RecyclerView.Adapter wrappedAdapter;
    private RecyclerViewExpandableItemManager recyclerViewExpandableItemManager;
    private RecyclerViewSwipeManager recyclerViewSwipeManager;
    private RecyclerViewTouchActionGuardManager recyclerViewTouchActionGuardManager;
    private RecyclerViewDragDropManager recyclerViewDragDropManager;
    private int workoutId;

    private OnFragmentInteractionListener mListener;

    public ActiveWorkoutListFragment() {
        // Required empty public constructor
    }

    public static ActiveWorkoutListFragment newInstance(int workoutId) {
        ActiveWorkoutListFragment fragment = new ActiveWorkoutListFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_WORKOUT_ID, workoutId);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            workoutId = getArguments().getInt(ARG_WORKOUT_ID);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_active_workout_list, container, false);
        GymLoggerApp.getComponent(view.getContext().getApplicationContext()).inject(this);
        ButterKnife.bind(this, view);
        setHasOptionsMenu(true);
        presenter.attachView(this);

        return view;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_active_workout, menu);
        menu.setGroupVisible(R.id.main_menu_group, false);

        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.finish_workout) {
            getActivity().finish();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        layoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);

        final Parcelable eimSavedState = (savedInstanceState != null) ? savedInstanceState.getParcelable(SAVED_STATE_EXPANDABLE_ITEM_MANAGER) : null;
        recyclerViewExpandableItemManager = new RecyclerViewExpandableItemManager(eimSavedState);
        recyclerViewExpandableItemManager.setOnGroupExpandListener(this);
        recyclerViewExpandableItemManager.setOnGroupCollapseListener(this);

        // touch guard manager  (this class is required to suppress scrolling while swipe-dismiss animation is running)
        recyclerViewTouchActionGuardManager = new RecyclerViewTouchActionGuardManager();
        recyclerViewTouchActionGuardManager.setInterceptVerticalScrollingWhileAnimationRunning(true);
        recyclerViewTouchActionGuardManager.setEnabled(true);

        recyclerViewDragDropManager = new RecyclerViewDragDropManager();
        recyclerViewDragDropManager.setDraggingItemShadowDrawable(
                (NinePatchDrawable) ContextCompat.getDrawable(getContext(), R.drawable.material_shadow_z3));

        recyclerViewSwipeManager = new RecyclerViewSwipeManager();

        //adapter
        final WorkoutExercisesAdapter myItemAdapter =
                new WorkoutExercisesAdapter(recyclerViewExpandableItemManager, ((ActiveWorkoutActivity) getActivity()).getDataProvider());

        myItemAdapter.setEventListener(new WorkoutExercisesAdapter.EventListener() {
            @Override
            public void onGroupItemRemoved(int groupPosition) {
                Timber.d("onGroupItemRemoved");
                //((ExpandableDraggableSwipeableExampleActivity) getActivity()).onGroupItemRemoved(groupPosition);
            }

            @Override
            public void onChildItemRemoved(int groupPosition, int childPosition) {
                //((ExpandableDraggableSwipeableExampleActivity) getActivity()).onChildItemRemoved(groupPosition, childPosition);
                Timber.d("onChildItemRemoved");
            }

            @Override
            public void onGroupItemPinned(int groupPosition) {
                //((ExpandableDraggableSwipeableExampleActivity) getActivity()).onGroupItemPinned(groupPosition);
                Timber.d("onGroupItemPinned");
            }

            @Override
            public void onChildItemPinned(int groupPosition, int childPosition) {
                //((ExpandableDraggableSwipeableExampleActivity) getActivity()).onChildItemPinned(groupPosition, childPosition);
                Timber.d("onChildItemPinned");
            }

            @Override
            public void onItemViewClicked(View v, boolean pinned) {
                //onItemViewClick(v, pinned);
                RecyclerView.ViewHolder vh = RecyclerViewAdapterUtils.getViewHolder(v);
                int flatPosition = vh.getAdapterPosition();
                if (flatPosition == RecyclerView.NO_POSITION) {
                    return;
                }

                long expandablePosition = recyclerViewExpandableItemManager.getExpandablePosition(flatPosition);
                int groupPosition = RecyclerViewExpandableItemManager.getPackedPositionGroup(expandablePosition);
                int childPosition = RecyclerViewExpandableItemManager.getPackedPositionChild(expandablePosition);

                WorkoutExercisesProvider dataProvider = ((ActiveWorkoutActivity) getActivity()).getDataProvider();
                AbstractExpandableDataProvider.ChildData childItem = dataProvider.getChildItem(groupPosition, childPosition);


                int splitHasExercisesId = ((WorkoutExercisesProvider.ConcreteChildData) childItem).getDataId();
                Workout workout = ((ActiveWorkoutActivity) getActivity()).getWorkout();
                Timber.d("onItemViewClicked " + groupPosition + ": " + childPosition + " | " + splitHasExercisesId + ": " + childItem.getText() + " : " + workout.getExerciseBySplitHasExerciseId(splitHasExercisesId).getExercise().getTitle());
                //TODO just a test
                workout.getExerciseBySplitHasExerciseId(splitHasExercisesId).getSets().get(0).setRepeatsIs(1234);
                //presenter.saveWorkout(workout);
                Intent intent = DataInputActivity.newIntent(getActivity(), workout.getWorkoutId(), splitHasExercisesId);
                startActivity(intent);
                // Check if item is the add button
                if (recyclerViewExpandableItemManager.getChildCount(groupPosition) - 1 == childPosition) {
                    Timber.d("last child clicked");
                    //onExerciseAddButtonClicked(groupPosition);
                }
            }
        });

        adapter = myItemAdapter;

        wrappedAdapter = recyclerViewExpandableItemManager.createWrappedAdapter(myItemAdapter);       // wrap for expanding
        wrappedAdapter = recyclerViewDragDropManager.createWrappedAdapter(wrappedAdapter);           // wrap for dragging
        wrappedAdapter = recyclerViewSwipeManager.createWrappedAdapter(wrappedAdapter);      // wrap for swiping

        final GeneralItemAnimator animator = new SwipeDismissItemAnimator();

        // Change animations are enabled by default since support-v7-recyclerview v22.
        // Disable the change animation in order to make turning back animation of swiped item works properly.
        // Also need to disable them when using animation indicator.
        animator.setSupportsChangeAnimations(false);

        recyclerview.setLayoutManager(layoutManager);
        recyclerview.setAdapter(wrappedAdapter);  // requires *wrapped* adapter
        recyclerview.setItemAnimator(animator);
        recyclerview.setHasFixedSize(false);

        // additional decorations
        //noinspection StatementWithEmptyBody
        if (supportsViewElevation()) {
            // Lollipop or later has native drop shadow feature. ItemShadowDecorator is not required.
        } else {
            recyclerview.addItemDecoration(new ItemShadowDecorator((NinePatchDrawable) ContextCompat.getDrawable(getContext(), R.drawable.material_shadow_z1)));
        }
        recyclerview.addItemDecoration(new SimpleListDividerDecorator(ContextCompat.getDrawable(getContext(), R.drawable.list_divider_h), true));

        // NOTE:
        // The initialization order is very important! This order determines the priority of touch event handling.
        //
        // priority: TouchActionGuard > Swipe > DragAndDrop > ExpandableItem
        recyclerViewTouchActionGuardManager.attachRecyclerView(recyclerview);
        recyclerViewSwipeManager.attachRecyclerView(recyclerview);
        recyclerViewDragDropManager.attachRecyclerView(recyclerview);
        recyclerViewExpandableItemManager.attachRecyclerView(recyclerview);

        presenter.loadWorkout(workoutId);
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
        presenter.detachView();
        mListener = null;
    }

    public interface OnFragmentInteractionListener {
        String FRAGMENT_TAG = "ActiveWorkoutListFragment";

        void onExerciseItemClicked(ActiveWorkoutListFragment fragment, Workout.WorkoutExercise exercise, ImageView imageView, TextView textView);
    }

    @Override
    public void onGroupCollapse(int groupPosition, boolean fromUser) {
        Timber.d("onGroupCollapse");
    }

    @Override
    public void onGroupExpand(int groupPosition, boolean fromUser) {
        Timber.d("onGroupExpand");
        WorkoutExercisesProvider dataProvider = ((ActiveWorkoutActivity) getActivity()).getDataProvider();
        dataProvider.getGroupItem(groupPosition).getDataId();
        int splitHasExercisesId = dataProvider.getGroupItem(groupPosition).getDataId();
        Intent intent = DataInputActivity.newIntent(getActivity(), workoutId, splitHasExercisesId);
        startActivity(intent);
    }

    private boolean supportsViewElevation() {
        return (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP);
    }

    @Override
    public void showError() {

    }

    @Override
    public void onWorkoutLoaded(Workout workout) {
        ((ActiveWorkoutActivity) getActivity()).setWorkout(workout);
        //adapter.setData(workout.getExercises());
        Timber.d("onWorkoutLoaded %d", workout.getWorkoutId());

        // TODO Dirty way to update the list
        WorkoutExercisesProvider dataProvider = ((ActiveWorkoutActivity) getActivity()).getDataProvider();
        dataProvider.clear();

        if (workout.getExercises() != null) {
            for (Workout.WorkoutExercise exercise : workout.getExercises()) {
                int groupPosition = (int) dataProvider.addGroupItem(exercise.getSplitHasExercisesId(), exercise.getExercise().getTitle());

            }
        }

        adapter.notifyDataSetChanged();
    }

    @Override
    public void onWorkoutExerciseLoaded(Workout.WorkoutExercise exercise) {

    }
}

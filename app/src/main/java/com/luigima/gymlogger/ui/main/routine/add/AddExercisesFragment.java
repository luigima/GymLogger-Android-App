package com.luigima.gymlogger.ui.main.routine.add;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.NinePatchDrawable;
import android.os.Build;
import android.os.Bundle;
import android.os.Parcelable;
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
import com.luigima.gymlogger.data.db.entities.Exercise;
import com.luigima.gymlogger.data.db.entities.Routine;

import java.util.ArrayList;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import timber.log.Timber;


public class AddExercisesFragment extends Fragment implements
        RecyclerViewExpandableItemManager.OnGroupCollapseListener,
        RecyclerViewExpandableItemManager.OnGroupExpandListener,
        AddRoutineMvpView {
    private static final String SAVED_STATE_EXPANDABLE_ITEM_MANAGER = "RecyclerViewExpandableItemManager";
    public static final String FRAGMENT_TAG = "AddExercisesFragment";
    public static final String ARG_ROUTINE_ID = "routineId";

    @BindView(R.id.recyclerview)
    RecyclerView recyclerview;

    @Inject
    AddRoutinePresenter presenter;

    private Unbinder butterKnife;
    private RecyclerView.LayoutManager layoutManager;
    private RecyclerView.Adapter adapter;
    private RecyclerView.Adapter wrappedAdapter;
    private RecyclerViewExpandableItemManager recyclerViewExpandableItemManager;
    private RecyclerViewSwipeManager recyclerViewSwipeManager;
    private RecyclerViewTouchActionGuardManager recyclerViewTouchActionGuardManager;
    private RecyclerViewDragDropManager recyclerViewDragDropManager;
    private OnFragmentInteractionListener mListener;
    private Routine routine;
    private int routineId;

    public AddExercisesFragment() {
        // Required empty public constructor
    }

    public static AddExercisesFragment newInstance(int routineId) {
        AddExercisesFragment fragment = new AddExercisesFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_ROUTINE_ID, routineId);
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_add_routine_exercises, container, false);
        GymLoggerApp.getComponent(view.getContext().getApplicationContext()).inject(this);
        butterKnife = ButterKnife.bind(this, view);
        setHasOptionsMenu(true);
        presenter.attachView(this);

        if (getArguments() != null) {
            routineId = getArguments().getInt(ARG_ROUTINE_ID);
        } else {
            Timber.e("Routine id missing!");
            routineId = -1;
        }

        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
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
        final RoutineConfigItemAdapter myItemAdapter =
                new RoutineConfigItemAdapter(recyclerViewExpandableItemManager, ((AddRoutineActivity) getActivity()).getDataProvider());

        myItemAdapter.setEventListener(new RoutineConfigItemAdapter.EventListener() {
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

                Timber.d("onItemViewClicked " + groupPosition + ": " + childPosition);
                // Check if item is the add button
                if (recyclerViewExpandableItemManager.getChildCount(groupPosition) - 1 == childPosition) {
                    onExerciseAddButtonClicked(groupPosition);
                }
            }

            @Override
            public void onSplitTitleChanged(int groupPosition, String text) {
                Timber.d("onSplitTitleChanged");
                RoutineConfigDataProvider dataProvider = ((AddRoutineActivity) getActivity()).getDataProvider();
                dataProvider.setGroupItem(groupPosition, text);
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

        // Load all splits and exercises of the current routineId
        presenter.getRoutine(routineId);
    }


    // TODO: Rename method, update argument and hook method into UI event
    public void onExerciseAddButtonClicked(int groupPosition) {
        if (mListener != null) {
            mListener.onExerciseAddButtonClicked(groupPosition);
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
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.add_routine, menu);
        menu.setGroupVisible(R.id.main_menu_group, false);

        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.add_split) {
            if (routine != null) {
                routine.getSplits().add(Routine.RoutineSplit.newSplit(new ArrayList<>(), "Split " + routine.getSplits().size()));
                presenter.saveRoutine(routine);
            }
        } else if (id == R.id.finish) {
            Timber.d("Routine creation finished!");
            getActivity().finish();
        }

        return super.onOptionsItemSelected(item);
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

    @Override
    public void onGroupCollapse(int groupPosition, boolean fromUser) {
        Timber.d("onGroupCollapse");
    }

    @Override
    public void onGroupExpand(int groupPosition, boolean fromUser) {
        Timber.d("onGroupExpand");
    }

    private boolean supportsViewElevation() {
        return (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP);
    }

    public interface OnFragmentInteractionListener {
        String FRAGMENT_TAG = "AddExercisesFragment";

        void onExerciseAddButtonClicked(int groupPosition);
    }

    @Override
    public void showError() {

    }


    @Override
    public void onRoutineCreated(int id) {

    }

    @Override
    public void onRoutineLoaded(Routine routine) {
        Timber.d("onRoutineExerciseLoaded %d", routine.getRoutineID());
        this.routine = routine;

        // TODO Dirty way to update the list
        RoutineConfigDataProvider dataProvider = ((AddRoutineActivity) getActivity()).getDataProvider();
        dataProvider.clear();

        if (routine.getSplits() != null) {
            for (Routine.RoutineSplit split : routine.getSplits()) {
                int groupPosition = (int) dataProvider.addGroupItem(split.getSplitId(), split.getTitle());
                int splitId = dataProvider.getGroupItem(groupPosition).getDataId();

                for (Routine.RoutineExercise routineExercise : split.getExercises()) {
                    if (split.getSplitId() == splitId) {
                        dataProvider.addChildItem(groupPosition, routineExercise.getExercise().getTitle(), routineExercise.getExercise().getId());
                    }
                }
                dataProvider.addChildItem(groupPosition, "", -1);
            }
        }

        adapter.notifyDataSetChanged();
    }

    @Override
    public void onSplitRenamed(boolean wasUpdated) {
        Timber.d("Test");
    }

    public void addExercise(int splitId, Exercise exercise) {
        Timber.d("Adding a new exercise to splitId %d...", splitId);
        if (routine != null) {
            routine.addNewExerciseWithOneSet(splitId, exercise);
            presenter.saveRoutine(routine);
        } else {
            Timber.e("routine is null");
        }
    }
}

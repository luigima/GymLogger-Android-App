package com.luigima.gymlogger.ui.main.workout.overview;

import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import com.jakewharton.rxbinding.widget.RxTextView;
import com.luigima.gymlogger.R;
import com.luigima.gymlogger.data.db.entities.Workout;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.subscriptions.CompositeSubscription;
import timber.log.Timber;


public class WorkoutExerciseSetDataAdapter extends RecyclerView.Adapter<WorkoutExerciseSetDataAdapter.ViewHolder> {
    private ArrayList<Workout.WorkoutSet> sets;
    private OnChangeListener listener;
    private CompositeSubscription compositeSubscription;

    // In order to autofill the reps input, the last given amount of reps will be stored in a variable
    // This method is better than reading the input above or waiting until the last set is stored
    private int lastRepsInput = 0;

    public WorkoutExerciseSetDataAdapter(List<Workout.WorkoutSet> sets) {
        this.sets = new ArrayList<>(sets);
        //Add header
        this.sets.add(0, new Workout.WorkoutSet(-1, 0, 0, 0, 0, 0, 0, 0));

        compositeSubscription = new CompositeSubscription();
    }

    public void addOnChangeListener(OnChangeListener listener) {
        this.listener = listener;
    }

    @Override
    public WorkoutExerciseSetDataAdapter.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.list_exercise_data_input_item, viewGroup, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(WorkoutExerciseSetDataAdapter.ViewHolder viewHolder, int i) {
        if (i == 0) {
            viewHolder.textViewSet.setText("Set");
            viewHolder.textViewPrevious.setText("Previous");
            viewHolder.textViewReps.setText("Reps");
            viewHolder.textViewWeight.setText("Weight");

        } else {
            Workout.WorkoutSet set = sets.get(i);
            viewHolder.textViewSet.setText(String.valueOf(set.getSetId() + 1));
            viewHolder.textViewPrevious.setText("");
            if (set.getRepeatsIs() == 0) {
                viewHolder.textViewReps.setText("");
            } else {
                viewHolder.textViewReps.setText(String.valueOf(set.getRepeatsIs()));
            }

            if (set.getWeightIs() == 0) {
                viewHolder.textViewWeight.setText("");
            } else {
                viewHolder.textViewWeight.setText(String.valueOf(set.getWeightIs()));
            }

            // Autofills the last amount of reps and focuses the weight input
            if (i == getItemCount() - 1) {
                if (i >= 2) {
                    viewHolder.textViewReps.setText(String.valueOf(lastRepsInput));
                    viewHolder.textViewWeight.requestFocus();
                } else {
                    viewHolder.textViewReps.requestFocus();
                }
            }

            Observable<Boolean> repsValid = RxTextView.textChanges(viewHolder.textViewReps)
                    .map(text -> text.length() > 0)
                    .asObservable();
            compositeSubscription.add(
                    repsValid.map(b -> b ? Color.BLACK : Color.RED)
                            .subscribe(color -> viewHolder.textViewWeight.setTextColor(color)));

            Observable<Boolean> weightValid = RxTextView.textChanges(viewHolder.textViewWeight)
                    .map(text -> text.length() > 0)
                    .asObservable();
            compositeSubscription.add(weightValid.map(b -> b ? Color.BLACK : Color.RED)
                    .subscribe(color -> viewHolder.textViewReps.setTextColor(color)));

            Observable<Boolean> repsWeightValid =
                    Observable.combineLatest(repsValid, weightValid, (a, b) -> a && b)
                            //wait for more inputs
                            .debounce(350, TimeUnit.MILLISECONDS)
                            //only if true
                            .filter(bool -> bool);

            compositeSubscription.add(
                    repsWeightValid.subscribe(bool -> {
                        int reps = Integer.valueOf(viewHolder.textViewReps.getText().toString());
                        listener.onSetChanged(
                                set.getSetId(),
                                reps,
                                Float.parseFloat(viewHolder.textViewWeight.getText().toString())
                        );
                        lastRepsInput = reps;
                    }));

            compositeSubscription.add(repsWeightValid.observeOn(AndroidSchedulers.mainThread())
                    // create a new empty set if the last one got filled out correctly
                    .filter(bool -> i == sets.size() - 1)
                    .subscribe(bool -> addItem(new Workout.WorkoutSet(sets.size() - 1, 0, 0, 0, 0, 0, 0, -1))
                    ));
        }
    }

    @Override
    public int getItemCount() {
        return sets.size();
    }

    public void setData(List<Workout.WorkoutSet> sets) {
        this.sets = new ArrayList<>(sets);
        //Add header
        this.sets.add(0, new Workout.WorkoutSet(-1, 0, 0, 0, 0, 0, 0, 0));

        notifyDataSetChanged();
    }

    public void addItem(Workout.WorkoutSet set) {
        this.sets.add(set);
        notifyItemInserted(sets.size() - 1);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView textViewSet;
        public TextView textViewPrevious;
        public EditText textViewReps;
        public EditText textViewWeight;

        public ViewHolder(View v) {
            super(v);
            textViewSet = (TextView) v.findViewById(R.id.column_set);
            textViewPrevious = (TextView) v.findViewById(R.id.column_previous);
            textViewReps = (EditText) v.findViewById(R.id.column_reps);
            textViewWeight = (EditText) v.findViewById(R.id.column_weight);
        }
    }

    public interface OnChangeListener {
        void onSetChanged(int setId, int reps, float weight);

        void onNewSet();
    }

    @Override
    public void onDetachedFromRecyclerView(RecyclerView recyclerView) {
        super.onDetachedFromRecyclerView(recyclerView);
        compositeSubscription.unsubscribe();
        Timber.d("compositeSubscription.unsubscribe()");
    }
}

package com.luigima.gymlogger.ui.main.exercise;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.h6ah4i.android.widget.advrecyclerview.utils.AbstractExpandableItemViewHolder;
import com.luigima.gymlogger.GymLoggerApp;
import com.luigima.gymlogger.R;
import com.luigima.gymlogger.data.db.entities.Exercise;
import com.luigima.gymlogger.util.ImageOverlayTransformationBuilder;
import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import timber.log.Timber;

public class ExerciseDataAdapter extends RecyclerView.Adapter<ExerciseDataAdapter.ViewHolder> {
    private List<Exercise> filteredData;
    private List<Exercise> data;

    private int baseViewType;
    private String searchQuery;
    OnItemClickListener onItemClickListener;
    OnCheckedChangeListener onCheckedChangeListener;

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int baseViewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_exercise_item, parent, false);
        return new ViewHolder(view);
    }

    public ExerciseDataAdapter(List<Exercise> data, int baseViewType) {
        this.filteredData = data;
        this.data = data;
        this.baseViewType = baseViewType;
        this.searchQuery = "";
    }

    public void setData(List<Exercise> data) {
        this.data = data;
        filterData();
        notifyDataSetChanged();
    }

    public void addData(List<Exercise> data) {
        this.data.addAll(data);
        filterData();
        notifyDataSetChanged();
    }

    public void setSearchQuery(String searchQuery) {
        this.filteredData = new ArrayList<>();
        this.searchQuery = searchQuery;
        filterData();
        notifyDataSetChanged();
    }


    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.bindData(filteredData.get(position));
    }


    @Override
    public int getItemCount() {
        return filteredData.size();
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    public void setOnCheckedChangeListener(OnCheckedChangeListener onCheckedChangeListener) {
        this.onCheckedChangeListener = onCheckedChangeListener;
    }


    public class ViewHolder extends AbstractExpandableItemViewHolder implements View.OnClickListener {

        @Inject
        Picasso picasso;

        Exercise exercise;
        public TextView textViewTitle;
        public TextView textViewEquipment;
        public ImageView imageView;
        public Button addButton;
        private Context context;
        private Boolean onBind;

        public ViewHolder(View view) {
            super(view);
            context = view.getContext();
            GymLoggerApp.getComponent(context.getApplicationContext()).inject(this);
            this.textViewTitle = (TextView) view.findViewById(R.id.textView);
            this.textViewEquipment = (TextView) view.findViewById(R.id.textView_equipment);
            this.imageView = (ImageView) view.findViewById(R.id.imageViewExercise);
            this.addButton = (Button) view.findViewById(R.id.btn_add);

            onBind = false;
            view.setOnClickListener(this);
            addButton.setOnClickListener(this);
        }

        public void bindData(Exercise exercise) {
            onBind = true;
            this.exercise = exercise;

            // The add button is only available by creating a new workout
            if (baseViewType == ExerciseFragment.VIEW_TYPE_NORMAL) {
                addButton.setVisibility(View.GONE);
            } else {
                addButton.setVisibility(View.VISIBLE);
            }

            textViewTitle.setText(exercise.getTitle());
            textViewEquipment.setText(exercise.getEquipmentListString());
            if (exercise.getImage1() != null) {
                //TODO remove replacement. concatenate strings according to best practices
                picasso.load("file:///android_asset/images/" + exercise.getImage1().replace(".jpg",".png"))
                        .into(imageView);
            }
            onBind = false;
        }

        public Exercise getExercise()
        {
            return exercise;
        }

        @Override
        public void onClick(View v) {
            if (onItemClickListener != null) {
                //TODO if condition is not specific
                if (v instanceof Button) {
                    onItemClickListener.onAddButtonClicked(exercise);
                } else {
                    onItemClickListener.onItemClick(imageView, exercise);
                }
            }
        }
    }

    private void filterData() {
        if (!searchQuery.equals("")) {
            for (Exercise exercise : data) {
                if (exercise.containsSearchQuery(searchQuery)) {
                    filteredData.add(exercise);
                }
            }
        }
    }

    public interface OnItemClickListener {
        void onItemClick(ImageView imageView, Exercise exercise);

        void onAddButtonClicked(Exercise exercise);
    }

    public interface OnCheckedChangeListener {
        void onCheckedChanged(Exercise exercise, boolean isChecked);
    }
}

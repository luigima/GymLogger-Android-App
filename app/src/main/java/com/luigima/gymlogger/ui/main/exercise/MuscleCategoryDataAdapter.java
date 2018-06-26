package com.luigima.gymlogger.ui.main.exercise;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.h6ah4i.android.widget.advrecyclerview.utils.AbstractExpandableItemViewHolder;
import com.luigima.gymlogger.GymLoggerApp;
import com.luigima.gymlogger.R;
import com.luigima.gymlogger.data.db.entities.MainMuscleGroup;
import com.squareup.picasso.Picasso;

import java.util.List;

import javax.inject.Inject;

public class MuscleCategoryDataAdapter extends RecyclerView.Adapter<MuscleCategoryDataAdapter.ViewHolder> {
    private List<MainMuscleGroup> data;
    OnItemClickListener onItemClickListener;

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_exercise_category_item, parent, false);
        return new ViewHolder(view);
    }

    public MuscleCategoryDataAdapter(List<MainMuscleGroup> data) {
        this.data = data;
    }

    public void setData(List<MainMuscleGroup> data) {
        this.data = data;
        notifyDataSetChanged();
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.bindData(data.get(position));
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    public class ViewHolder extends AbstractExpandableItemViewHolder implements View.OnClickListener {
        @Inject
        Picasso picasso;

        MainMuscleGroup mainExerciseCategory;
        public TextView textView;
        public ImageView imageView;

        public ViewHolder(View view) {
            super(view);
            GymLoggerApp.getComponent(view.getContext().getApplicationContext()).inject(this);
            this.textView = (TextView) view.findViewById(R.id.textView);
            this.imageView = (ImageView) view.findViewById(R.id.imageView);

            view.setOnClickListener(this);
        }


        public void bindData(MainMuscleGroup mainExerciseCategory) {
            this.mainExerciseCategory = mainExerciseCategory;
            textView.setText(mainExerciseCategory.getTitle());
        }

        public MainMuscleGroup getMainExerciseCategory() {
            return mainExerciseCategory;
        }

        @Override
        public void onClick(View v) {
            if (onItemClickListener != null) {
                onItemClickListener.onItemClick(v, getAdapterPosition(), mainExerciseCategory);
            }
        }
    }

    public interface OnItemClickListener {
        void onItemClick(View view, int position, MainMuscleGroup mainExerciseCategory);
    }
}

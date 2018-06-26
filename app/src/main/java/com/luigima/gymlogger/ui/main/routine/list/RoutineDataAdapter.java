package com.luigima.gymlogger.ui.main.routine.list;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.h6ah4i.android.widget.advrecyclerview.utils.AbstractExpandableItemViewHolder;
import com.luigima.gymlogger.GymLoggerApp;
import com.luigima.gymlogger.R;
import com.luigima.gymlogger.data.db.entities.Routine;
import com.luigima.gymlogger.util.ImageOverlayTransformationBuilder;
import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.Picasso;

import java.util.List;

import javax.inject.Inject;

public class RoutineDataAdapter extends RecyclerView.Adapter<RoutineDataAdapter.ViewHolder> {
    private List<Routine> data;
    OnItemClickListener onItemClickListener;

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_routine_item, parent, false);
        return new ViewHolder(view);
    }

    public RoutineDataAdapter(List<Routine> data) {
        this.data = data;
    }

    public void setData(List<Routine> data) {
        this.data = data;
        notifyDataSetChanged();
    }

    public void addData(List<Routine> data) {
        this.data.addAll(data);
        notifyDataSetChanged();
    }

    public void addRoutine(Routine routine) {
        this.data.add(routine);
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

        Routine routine;
        public TextView textViewTitle;
        public TextView textViewEquipment;
        public ImageView imageView;
        private Context context;

        public ViewHolder(View view) {
            super(view);
            context = view.getContext();
            GymLoggerApp.getComponent(context.getApplicationContext()).inject(this);
            this.textViewTitle = (TextView) view.findViewById(R.id.textView_title);
            this.textViewEquipment = (TextView) view.findViewById(R.id.textView_equipment);
            this.imageView = (ImageView) view.findViewById(R.id.imageView_muscles);
            view.setOnClickListener(this);
        }

        public void bindData(Routine routine) {
            this.routine = routine;
            textViewTitle.setText(routine.getTitle());
            //textViewEquipment.setText(exercise.getEquipmentListString());
            ImageOverlayTransformationBuilder imageOverlayTransformationBuilder =
                    new ImageOverlayTransformationBuilder(context, routine.getMuscles());
            picasso.load("file:///android_asset/images/body/example/base.png")
                    .transform(imageOverlayTransformationBuilder.getTransformation()).memoryPolicy(MemoryPolicy.NO_CACHE)
                    .into(imageView);

        }

        public Routine getRoutine() {
            return routine;
        }

        @Override
        public void onClick(View v) {
            if (onItemClickListener != null) {
                onItemClickListener.onItemClick(imageView, routine);
            }
        }
    }

    public interface OnItemClickListener {
        void onItemClick(ImageView imageView, Routine routine);
    }
}

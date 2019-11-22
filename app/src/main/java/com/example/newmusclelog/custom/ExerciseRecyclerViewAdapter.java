package com.example.newmusclelog.custom;

import android.content.Context;
import android.os.Handler;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import com.example.newmusclelog.ActiveWorkoutFragment;
import com.example.newmusclelog.MainActivity;
import com.example.newmusclelog.R;
import com.example.newmusclelog.data.Exercise;
import com.example.newmusclelog.data.Workout;

import org.w3c.dom.Text;

import java.util.Collections;
import java.util.List;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

public class ExerciseRecyclerViewAdapter extends
        RecyclerView.Adapter<ExerciseRecyclerViewAdapter.ViewHolder> implements
        ItemTouchHelperAdapter {

    private List<Exercise> exercises;
    private final ActiveWorkoutFragment parentFragment;

    // pass data into this object
    public ExerciseRecyclerViewAdapter(Workout workout, ActiveWorkoutFragment parentFragment) {
        exercises = workout.getExercises();
        this.parentFragment = parentFragment;
    }

    // ViewHolder provides direct references to each view in an individual row
    class ViewHolder extends RecyclerView.ViewHolder {
        ConstraintLayout constraintLayout;
        EditText exerciseName;
        EditText repCount;
        EditText currentWeightCount;
        TextView kg;
        TextView reps;

        ViewHolder(View view){
            super(view);
            constraintLayout = view.findViewById(R.id.exercise_constraint_layout);
            exerciseName = view.findViewById(R.id.exercise_name);
            kg = view.findViewById(R.id.lbs);
            currentWeightCount = view.findViewById(R.id.exercise_weight);
            repCount = view.findViewById(R.id.exercise_reps);
            reps = view.findViewById(R.id.reps);
        }
        void showLbsTextView()
        {
            kg.setVisibility(View.VISIBLE);
        }

        void hideLbsTextView()
        {
            kg.setVisibility(View.INVISIBLE);
        }

        void showRepsTextView()
        {
            reps.setVisibility(View.VISIBLE);
        }

        void hideRepsTextView()
        {
            reps.setVisibility(View.INVISIBLE);
        }
    }

    // inflates XML and returns ViewHolder object
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        // Inflate the custom layout
        View exerciseView = inflater.inflate(R.layout.recycler_view_item, parent, false);

        // Return a new holder instance
        return new ViewHolder(exerciseView);
    }

    // Involves populating data into the item through ViewHolder
    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int position) {
        /*
        Get correct Exercise object based on which position the ViewHolder is in
        the RecycleView (so if ViewHolder is 4th in the RecycleView, we grab the 4th
        item in the list
        */
        Exercise exercise = exercises.get(position);

        String currentWeightCountText;
        String repCountText;

        if (exercise.getCurrentWeight() == -1) {
            currentWeightCountText = "";
            viewHolder.kg.setVisibility(View.INVISIBLE);
        } else {
            currentWeightCountText = String.valueOf(exercise.getCurrentWeight());
        }

        if (exercise.getReps() == -1) {
            repCountText = "";
            viewHolder.reps.setVisibility(View.INVISIBLE);
        } else {
            repCountText = String.valueOf(exercise.getReps());
        }

        // Set item views
        viewHolder.exerciseName.setText(exercise.getName());
        viewHolder.currentWeightCount.setText(currentWeightCountText);
        viewHolder.repCount.setText(repCountText);

        setOnFocusChangeListeners(viewHolder, exercise);
        setTextChangedListeners(viewHolder);
    }

    // saves data to exercise when user updates CustomEditText
    private void setOnFocusChangeListeners(final ViewHolder viewHolder, final Exercise exercise) {

        viewHolder.exerciseName.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus && viewHolder.exerciseName.getText() != null)
                    exercise.setName(viewHolder.exerciseName.getText().toString());
            }
        });

        viewHolder.currentWeightCount.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus && viewHolder.currentWeightCount.getText() != null) {
                    String weight = viewHolder.currentWeightCount.getText().toString();

                    if (weight.equals("")) {
                        exercise.setCurrentWeight(-1);
                    } else {
                        exercise.setCurrentWeight(Integer.parseInt(weight));
                    }
                }
            }
        });

        viewHolder.repCount.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus && viewHolder.repCount.getText() != null) {
                    String reps = viewHolder.repCount.getText().toString();

                    if (reps.equals("")) {
                        exercise.setReps(-1);
                    } else {
                        exercise.setReps(Integer.parseInt(reps));
                    }
                }
            }
        });
    }

    private void setTextChangedListeners(final ViewHolder viewHolder)
    {
        viewHolder.currentWeightCount.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {}

            @Override
            public void afterTextChanged(Editable s) {
                if (TextUtils.isEmpty(s)) {
                    viewHolder.hideLbsTextView();
                } else {
                    viewHolder.showLbsTextView();
                }
            }
        });

        viewHolder.repCount.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {}

            @Override
            public void afterTextChanged(Editable s) {
                if (TextUtils.isEmpty(s)) {
                    viewHolder.hideRepsTextView();
                } else {
                    viewHolder.showRepsTextView();
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        if (exercises == null)
            return 0;
        else
            return exercises.size();
    }

    // called when user swipes/drags items in RecyclerView
    @Override
    public boolean onItemMove(int fromPosition, int toPosition) {
        if (fromPosition < toPosition) {
            for (int i = fromPosition; i < toPosition; i++) {
                Collections.swap(exercises, i, i + 1);
            }
        } else {
            for (int i = fromPosition; i > toPosition; i--) {
                Collections.swap(exercises, i, i - 1);
            }
        }
        notifyItemMoved(fromPosition, toPosition);
        return true;
    }

    @Override
    public void onItemDismiss(int position) {
        exercises.remove(position);
        notifyItemRemoved(position);

        if (getItemCount() == 0) {
// Delays TextView reappearing for a couple of milliseconds
            final Handler handler = new Handler();
            handler.postDelayed(parentFragment::showEmptyRecyclerViewText, 300);
        }
    }
}

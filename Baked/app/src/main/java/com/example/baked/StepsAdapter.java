package com.example.baked;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.baked.Model.Step;

import java.util.List;

public class StepsAdapter extends RecyclerView.Adapter<StepsAdapter.StepsAdapterViewHolder> {

    private Context mContext;
    private StepsAdapter.StepAdapterOnClickHandler mClickHandler;
    private List<Step> mStepsList;
    private boolean isInit;
    private boolean mTwoPane;

    public interface StepAdapterOnClickHandler {
        void onClick(Step step);
    }

    public StepsAdapter(Context context, StepAdapterOnClickHandler clickHandler, List<Step> steps, boolean twoPane) {
        mContext = context;
        mClickHandler = clickHandler;
        mStepsList = steps;
        isInit = true;
        mTwoPane = twoPane;
    }

    @NonNull
    @Override
    public StepsAdapterViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        int layoutId = R.layout.list_item_steps_master_list;
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(layoutId, viewGroup, false);
        return new StepsAdapterViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull StepsAdapterViewHolder holder, int position) {
        TextView stepsNameTextView = holder.itemView.findViewById(R.id.list_item_steps_number);
        if (position == 0) {
            stepsNameTextView.setText(R.string.introduction);
        } else {
            String stepNumber = mContext.getString(R.string.step, String.valueOf(mStepsList.get(position).getId()));
            stepsNameTextView.setText(stepNumber);
        }
        if (position == 0 && isInit && mTwoPane) {
            mClickHandler.onClick(mStepsList.get(0));
            isInit = false;
        }

        holder.itemView.setTag(mStepsList.get(position));
    }

    @Override
    public int getItemCount() {
        if (null == mStepsList) {
            return 0;
        }
        return mStepsList.size();
    }

    class StepsAdapterViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        public StepsAdapterViewHolder(@NonNull View itemView) {
            super(itemView);
            itemView.setTag(this);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            int adapterPosition = getAdapterPosition();
            Step step = mStepsList.get(adapterPosition);
            mClickHandler.onClick(step);
        }
    }
}


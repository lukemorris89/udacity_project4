package com.example.baked;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.baked.Model.Ingredient;

import java.util.List;

public class IngredientsAdapter extends RecyclerView.Adapter<IngredientsAdapter.IngredientsAdapterViewHolder> {

    private List<Ingredient> mIngredientsList;

    IngredientsAdapter(List<Ingredient> ingredients) {
        mIngredientsList = ingredients;
    }

    @NonNull
    @Override
    public IngredientsAdapterViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        int layoutId = R.layout.list_item_ingredients_master_list;
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(layoutId, viewGroup, false);
        return new IngredientsAdapterViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull IngredientsAdapterViewHolder holder, int position) {
        TextView ingredientNameTextView = holder.itemView.findViewById(R.id.list_item_ingredients_name);
        TextView ingredientQuantityTextView = holder.itemView.findViewById(R.id.list_item_ingredients_quantity);
        TextView ingredientMeasureTextView = holder.itemView.findViewById(R.id.list_item_ingredients_measure);

        String ingredientName = mIngredientsList.get(position).getIngredient();
        double ingredientQuantity = mIngredientsList.get(position).getQuantity();
        String ingredientMeasure = mIngredientsList.get(position).getMeasure();

        ingredientNameTextView.setText(ingredientName);
        ingredientQuantityTextView.setText(String.valueOf(ingredientQuantity));
        ingredientMeasureTextView.setText(ingredientMeasure);
    }

    @Override
    public int getItemCount() {
        if (null == mIngredientsList) {
            return 0;
        }
        return mIngredientsList.size();
    }

    static class IngredientsAdapterViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        public IngredientsAdapterViewHolder(@NonNull View itemView) {
            super(itemView);
        }

        @Override
        public void onClick(View view) {
        }
    }
}


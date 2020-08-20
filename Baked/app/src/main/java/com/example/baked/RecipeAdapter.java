package com.example.baked;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.baked.Model.Recipe;

import java.util.List;

public class RecipeAdapter extends RecyclerView.Adapter<RecipeAdapter.RecipeAdapterViewHolder> {

    private Context mContext;
    private List<Recipe> mRecipeList;
    private TextView mRecipeNameTextView;
    private TextView mServingSizeTextView;
    private RecipeAdapterOnClickHandler mClickHandler;

    public interface RecipeAdapterOnClickHandler {
        void onClick(Recipe recipe);
    }

    public RecipeAdapter(Context context, RecipeAdapterOnClickHandler clickHandler, List<Recipe> recipes) {
        mContext = context;
        mClickHandler = clickHandler;
        mRecipeList = recipes;
    }

    @NonNull
    @Override
    public RecipeAdapterViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.list_item_recipe_main, viewGroup, false);
        return new RecipeAdapterViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecipeAdapterViewHolder holder, int position) {
        String recipeImageURL = mRecipeList.get(position).getImage();
        String servingSize = mContext.getString(R.string.serves, String.valueOf(mRecipeList.get(position).getServings()));

        mRecipeNameTextView.setText(mRecipeList.get(position).getName());
        mServingSizeTextView.setText(servingSize);

        if (!mRecipeList.get(position).getImage().isEmpty()) {
            ImageView recipeImageView = holder.itemView.findViewById(R.id.main_activity_recipe_imageview);
            Glide.with(mContext).load(recipeImageURL).into(recipeImageView);
        }
    }

    @Override
    public int getItemCount() {
        if (null == mRecipeList) {
            return 0;
        }
        return mRecipeList.size();
    }

    class RecipeAdapterViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public RecipeAdapterViewHolder(@NonNull View itemView) {
            super(itemView);

            mRecipeNameTextView = itemView.findViewById(R.id.main_activity_recipename_textview);
            mServingSizeTextView = itemView.findViewById(R.id.main_activity_servingsize_textview);

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            int adapterPosition = getAdapterPosition();
            Recipe recipe = mRecipeList.get(adapterPosition);
            mClickHandler.onClick(recipe);
        }
    }
}

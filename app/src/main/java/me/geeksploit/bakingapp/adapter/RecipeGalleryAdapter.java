package me.geeksploit.bakingapp.adapter;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import java.util.List;

import me.geeksploit.bakingapp.MainActivity;
import me.geeksploit.bakingapp.R;
import me.geeksploit.bakingapp.RecipeStepListActivity;
import me.geeksploit.bakingapp.data.RecipeEntity;

public final class RecipeGalleryAdapter
        extends RecyclerView.Adapter<RecipeGalleryAdapter.ViewHolder> {

    private final MainActivity mParentActivity;
    private final List<RecipeEntity> mValues;
    private final View.OnClickListener mOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            RecipeEntity item = (RecipeEntity) view.getTag();
            Context context = view.getContext();
            Intent intent = new Intent(context, RecipeStepListActivity.class);
            intent.putExtra(RecipeStepListActivity.EXTRA_RECIPE, item);
            context.startActivity(intent);
        }
    };

    public RecipeGalleryAdapter(MainActivity parent, List<RecipeEntity> items) {
        mParentActivity = parent;
        mValues = items;
    }

    @NonNull
    @Override
    public RecipeGalleryAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.recipe_list_content, parent, false);
        return new RecipeGalleryAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final RecipeGalleryAdapter.ViewHolder holder, int position) {
        Context context = mParentActivity.getApplicationContext();
        Resources res = context.getResources();
        RecipeEntity recipe = mValues.get(position);

        RequestOptions options = new RequestOptions()
                .placeholder(R.drawable.placeholder_recipe_image)
                .error(R.drawable.placeholder_recipe_image);
        Glide.with(context)
                .load(R.drawable.placeholder_recipe_image)
                .apply(options)
                .into(holder.mImageView);

        holder.mNameView.setText(mValues.get(position).getName());

        setText(res, holder.mIngredientsView, R.plurals.label_ingredients, recipe.getIngredients().size());
        setText(res, holder.mStepsView, R.plurals.label_steps, recipe.getSteps().size());

        holder.itemView.setTag(mValues.get(position));
        holder.itemView.setOnClickListener(mOnClickListener);
    }

    @Override
    public int getItemCount() {
        return mValues.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        final ImageView mImageView;
        final TextView mNameView;
        final TextView mIngredientsView;
        final TextView mStepsView;

        ViewHolder(View view) {
            super(view);
            mImageView = view.findViewById(R.id.recipe_image);
            mNameView = view.findViewById(R.id.recipe_name);
            mIngredientsView = view.findViewById(R.id.recipe_ingredients);
            mStepsView = view.findViewById(R.id.recipe_steps);
        }
    }

    private void setText(Resources res, TextView tv, int pluralsId, int count) {
        tv.setText(res.getQuantityString(pluralsId, count, count));
    }
}

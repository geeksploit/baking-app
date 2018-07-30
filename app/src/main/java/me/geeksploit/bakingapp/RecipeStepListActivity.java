package me.geeksploit.bakingapp;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.io.Serializable;
import java.util.List;

import me.geeksploit.bakingapp.data.IngredientEntity;
import me.geeksploit.bakingapp.data.RecipeEntity;
import me.geeksploit.bakingapp.data.StepEntity;

/**
 * An activity representing a list of Recipe Steps. This activity
 * has different presentations for handset and tablet-size devices. On
 * handsets, the activity presents a list of items, which when touched,
 * lead to a {@link RecipeStepDetailActivity} representing
 * item details. On tablets, the activity presents the list of items and
 * item details side-by-side using two vertical panes.
 */
public class RecipeStepListActivity extends AppCompatActivity {

    public static final String EXTRA_RECIPE = "item_id";

    /**
     * Whether or not the activity is in two-pane mode, i.e. running on a tablet
     * device.
     */
    private boolean mTwoPane;
    private RecipeEntity mItem;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipestep_list);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setTitle(getTitle());

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        if (findViewById(R.id.recipestep_detail_container) != null) {
            // The detail container view will be present only in the
            // large-screen layouts (res/values-w900dp).
            // If this view is present, then the
            // activity should be in two-pane mode.
            mTwoPane = true;
        }

        if (getIntent().hasExtra(EXTRA_RECIPE)) {
            // TODO: use a Loader to load content from a content provider.
            mItem = (RecipeEntity) getIntent().getSerializableExtra(EXTRA_RECIPE);
            setTitle(mItem.getName());
        }

        View recyclerView = findViewById(R.id.recipestep_list);
        assert recyclerView != null;
        setupRecyclerView((RecyclerView) recyclerView);
    }

    private void setupRecyclerView(@NonNull RecyclerView recyclerView) {
        recyclerView.setAdapter(new SimpleItemRecyclerViewAdapter(this, mItem.getIngredients(), mItem.getSteps(), mTwoPane));
    }

    public static class SimpleItemRecyclerViewAdapter
            extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

        private static final int TYPE_HEADER_INGREDIENTS = 0;
        private static final int TYPE_ITEM_RECIPE_STEP = 1;

        private final RecipeStepListActivity mParentActivity;
        private final List<IngredientEntity> mIngredients;
        private final List<StepEntity> mValues;
        private final boolean mTwoPane;
        private final View.OnClickListener mOnClickHeaderListener = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                List<IngredientEntity> ingredients = (List<IngredientEntity>) view.getTag();
                if (mTwoPane) {
                    Bundle arguments = new Bundle();
                    arguments.putSerializable(IngredientFragment.ARG_INGREDIENTS_LIST, (Serializable) ingredients);
                    IngredientFragment fragment = new IngredientFragment();
                    fragment.setArguments(arguments);
                    mParentActivity.getSupportFragmentManager().beginTransaction()
                            .replace(R.id.recipestep_detail_container, fragment)
                            .commit();
                } else {
                    Context context = view.getContext();
                    Intent intent = new Intent(context, RecipeIngredientsActivity.class);
                    intent.putExtra(Intent.EXTRA_TITLE, mParentActivity.getTitle());
                    intent.putExtra(IngredientFragment.ARG_INGREDIENTS_LIST, (Serializable) ingredients);
                    context.startActivity(intent);
                }
            }
        };
        private final View.OnClickListener mOnClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                StepEntity item = (StepEntity) view.getTag();
                if (mTwoPane) {
                    Bundle arguments = new Bundle();
                    arguments.putSerializable(RecipeStepDetailFragment.ARG_ITEM, item);
                    RecipeStepDetailFragment fragment = new RecipeStepDetailFragment();
                    fragment.setArguments(arguments);
                    mParentActivity.getSupportFragmentManager().beginTransaction()
                            .replace(R.id.recipestep_detail_container, fragment)
                            .commit();
                } else {
                    Context context = view.getContext();
                    Intent intent = new Intent(context, RecipeStepDetailActivity.class);
                    intent.putExtra(RecipeStepDetailFragment.ARG_ITEM, item);

                    context.startActivity(intent);
                }
            }
        };

        SimpleItemRecyclerViewAdapter(RecipeStepListActivity parent,
                                      List<IngredientEntity> ingredients, List<StepEntity> items,
                                      boolean twoPane) {
            mIngredients = ingredients;
            mValues = items;
            mParentActivity = parent;
            mTwoPane = twoPane;
        }

        @NonNull
        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view;
            switch (viewType) {
                case TYPE_HEADER_INGREDIENTS:
                    view = LayoutInflater.from(parent.getContext())
                            .inflate(R.layout.recipe_ingredients_list_content, parent, false);
                    return new ViewHolderHeader(view);
                case TYPE_ITEM_RECIPE_STEP:
                    view = LayoutInflater.from(parent.getContext())
                            .inflate(R.layout.recipestep_list_content, parent, false);
                    return new ViewHolder(view);
                default:
                    throw new UnsupportedOperationException("unsupported view type");
            }
        }

        @Override
        public void onBindViewHolder(@NonNull final RecyclerView.ViewHolder holder, int position) {
            if (holder instanceof ViewHolderHeader) {
                ViewHolderHeader headerHolder = ((ViewHolderHeader) holder);
                Context context = headerHolder.mLabel.getContext();
                Resources res = context.getResources();
                headerHolder.mLabel.setText(res.getQuantityString(
                        R.plurals.label_ingredients,
                        mIngredients.size(),
                        mIngredients.size())
                );
                holder.itemView.setTag(mIngredients);
                holder.itemView.setOnClickListener(mOnClickHeaderListener);
            } else if (holder instanceof ViewHolder) {
                ViewHolder stepHolder = (ViewHolder) holder;
                StepEntity stepEntity = getRecipeStep(position);
                stepHolder.mIdView.setText(String.valueOf(stepEntity.getId()));
                stepHolder.mContentView.setText(stepEntity.getShortDescription());
                stepHolder.itemView.setTag(stepEntity);
                stepHolder.itemView.setOnClickListener(mOnClickListener);
            }
        }

        @Override
        public int getItemCount() {
            return mValues.size() + 1;
        }

        private StepEntity getRecipeStep(int position) {
            return mValues.get(position - 1);
        }

        @Override
        public int getItemViewType(int position) {
            if (position == 0) {
                return TYPE_HEADER_INGREDIENTS;
            } else {
                return TYPE_ITEM_RECIPE_STEP;
            }
        }

        class ViewHolderHeader extends RecyclerView.ViewHolder {
            final TextView mLabel;

            ViewHolderHeader(View view) {
                super(view);
                mLabel = view.findViewById(R.id.ingredients_label);
            }
        }

        class ViewHolder extends RecyclerView.ViewHolder {
            final TextView mIdView;
            final TextView mContentView;

            ViewHolder(View view) {
                super(view);
                mIdView = view.findViewById(R.id.id_text);
                mContentView = view.findViewById(R.id.content);
            }
        }
    }
}

package me.geeksploit.bakingapp;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import me.geeksploit.bakingapp.data.IngredientEntity;

/**
 * A fragment representing a list of Ingredients.
 */
public class IngredientFragment extends Fragment {

    // TODO: Customize parameter argument names
    public static final String ARG_INGREDIENTS_LIST = "ingredients-list";
    private static final String ARG_COLUMN_COUNT = "column-count";
    // TODO: Customize parameters
    private List<IngredientEntity> mIngredients;
    private int mColumnCount = 1;

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public IngredientFragment() {
    }

    // TODO: Customize parameter initialization
    @SuppressWarnings("unused")
    public static IngredientFragment newInstance(int columnCount) {
        IngredientFragment fragment = new IngredientFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_COLUMN_COUNT, columnCount);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            mColumnCount = getArguments().getInt(ARG_COLUMN_COUNT);
            mIngredients = (List<IngredientEntity>) getArguments().getSerializable(ARG_INGREDIENTS_LIST);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_ingredient_list, container, false);

        // Set the adapter
        if (view instanceof RecyclerView) {
            Context context = view.getContext();
            RecyclerView recyclerView = (RecyclerView) view;
            if (mColumnCount <= 1) {
                recyclerView.setLayoutManager(new LinearLayoutManager(context));
            } else {
                recyclerView.setLayoutManager(new GridLayoutManager(context, mColumnCount));
            }
            recyclerView.setAdapter(new IngredientAdapter(mIngredients));
        }
        return view;
    }
}

package com.prateek.dogengine;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.prateek.dogengine.adapters.BreedsListAdapter;
import com.prateek.dogengine.viewmodel.BreedViewModel;

/**
 * Fragment to display list of dog breeds based on query searched by user
 * <p>
 * It features a SearchView at the top on Toolbar. Its added as an options menu item.
 * It makes use of ViewModel and DataBinding Android frameworks to efficiently load and display data.
 */
public class BreedsListFragment extends Fragment {
    private BreedViewModel mViewModel;
    private TextView mErrorView;
    private RecyclerView mRecyclerView;
    private View mLoadingView;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_list, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        //init view components
        setupUI(view);

        BreedsListAdapter adapter = new BreedsListAdapter();

        //set up observer to observe any changes to our breed list held by our view model
        setupBreedListObserver(adapter);

        //set up observer to observe any error messages received in our search request response
        setupErrorObserver(adapter);
    }

    private void setupErrorObserver(BreedsListAdapter adapter) {
        mViewModel.getError().observe(getViewLifecycleOwner(), s -> {
            //hide the loading view once observer receives an error response to our search request
            mLoadingView.setVisibility(View.GONE);
            //clear the list view to remove any items from the UI from previously search result
            adapter.clear();
            //show the error view and set the error message
            mErrorView.setVisibility(View.VISIBLE);
            mErrorView.setText(s);
        });
    }

    private void setupBreedListObserver(BreedsListAdapter adapter) {
        mViewModel = new ViewModelProvider(this).get(BreedViewModel.class);
        mViewModel.getBreeds().observe(getViewLifecycleOwner(), breeds -> {
            //hide any error that might have be shown in previous request
            mErrorView.setVisibility(View.GONE);

            /*
            A null value is intentionally set by us to let the UI know we are about to make a new
            search request so clear the current UI. Its not  anything necessary but give a nice UX
            */
            if (breeds == null) {
                adapter.clear();
                return;
            }

            //hide the loading view shown during search request
            mLoadingView.setVisibility(View.GONE);

            adapter.setData(breeds);
            mRecyclerView.setAdapter(adapter);

            /*
            Show a predefined error message to user to let them know that their search query did not
            yield any result so request them to try again with a different search query.
             */
            if (breeds.size() == 0) {
                mErrorView.setText(R.string.no_breeds_found);
                mErrorView.setVisibility(View.VISIBLE);
            }
        });
    }

    private void setupUI(@NonNull View view) {
        //Update toolbar title every time user visits this fragment
        ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle(R.string.app_name);

        mRecyclerView = view.findViewById(R.id.recycler_view);
        mLoadingView = view.findViewById(R.id.loading_view);
        mErrorView = view.findViewById(R.id.default_error);
        mErrorView.setVisibility(View.VISIBLE);

        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity(),
                LinearLayoutManager.VERTICAL, false));
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);

        inflater.inflate(R.menu.search_menu, menu);
        final MenuItem menuItem = menu.findItem(R.id.action_search);
        final SearchView searchView = (SearchView) menuItem.getActionView();
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                //Initiate a search request only if user has entered at least one character
                if (query.length() > 0) {
                    //hide any previously shown error view
                    mErrorView.setVisibility(View.GONE);
                    //show the loading view
                    mLoadingView.setVisibility(View.VISIBLE);
                    //make a search request to fetch data based on query entered by user in the search view
                    mViewModel.searchBreeds(query);
                    //clear search view focus to hide the soft keyboard
                    searchView.clearFocus();
                }
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });
    }
}
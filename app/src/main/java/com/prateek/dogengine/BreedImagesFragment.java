package com.prateek.dogengine;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.OrientationHelper;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import com.bumptech.glide.Glide;
import com.prateek.dogengine.adapter.BreedImagesListAdapter;
import com.prateek.dogengine.data.Breed;
import com.prateek.dogengine.viewmodel.BreedImageViewModelFactory;
import com.prateek.dogengine.viewmodel.BreedImagesViewModel;

/**
 * Fragment to display list of dog breed images as selected by user in previous search screen.
 * <p>
 * It features a StaggeredLayoutManager to display searched images.
 * It makes use of ViewModel and Paging Android frameworks from Jetpack to efficiently load and
 * display data via pagination.
 */
public class BreedImagesFragment extends Fragment {

    private static final String ARG_BREED_ID = "breed_id";
    private static final String ARG_BREED_NAME = "breed_name";
    private int mBreedId;
    private String mBreedName;

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param breed The breed of dogs for which images are to be fetched
     * @return A new instance of fragment BreedImagesFragment.
     */
    public static void newInstance(View view, Breed breed) {
        BreedImagesFragment fragment = new BreedImagesFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_BREED_ID, breed.getMId());
        args.putString(ARG_BREED_NAME, breed.getMName());
        fragment.setArguments(args);

        Navigation.findNavController(view).navigate(R.id.loadBreedImages, args);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mBreedId = getArguments().getInt(ARG_BREED_ID);
            mBreedName = getArguments().getString(ARG_BREED_NAME);
        }
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

        //set Toolbar title as the name of dog breed for which images are fetched and displayed
        ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle(mBreedName);

        RecyclerView recyclerView = view.findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new StaggeredGridLayoutManager(2, OrientationHelper.VERTICAL));

        BreedImagesListAdapter adapter = new BreedImagesListAdapter();
        recyclerView.setAdapter(adapter);

        BreedImagesViewModel viewModel = new ViewModelProvider(this, new BreedImageViewModelFactory(mBreedId)).get(BreedImagesViewModel.class);
        viewModel.getBreedImages().observe(getViewLifecycleOwner(), adapter::submitList);
    }

    @Override
    public void onDestroyView() {
        Glide.with(getContext()).clear(getView());
        super.onDestroyView();
    }
}
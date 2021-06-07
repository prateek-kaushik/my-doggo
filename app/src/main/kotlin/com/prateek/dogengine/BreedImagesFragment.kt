package com.prateek.dogengine

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import androidx.paging.PagedList
import androidx.recyclerview.widget.OrientationHelper
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.bumptech.glide.Glide
import com.prateek.dogengine.adapter.BreedImagesListAdapter
import com.prateek.dogengine.data.Breed
import com.prateek.dogengine.data.BreedImage
import com.prateek.dogengine.viewmodel.BreedImageViewModelFactory
import com.prateek.dogengine.viewmodel.BreedImagesViewModel

/**
 * Fragment to display list of dog breed images as selected by user in previous search screen.
 *
 *
 * It features a StaggeredLayoutManager to display searched images.
 * It makes use of ViewModel and Paging Android frameworks from Jetpack to efficiently load and
 * display data via pagination.
 */
class BreedImagesFragment : Fragment() {
    private var mBreedId = 0
    private var mBreedName: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        arguments?.let {
            mBreedId = it.getInt(ARG_BREED_ID)
            mBreedName = it.getString(ARG_BREED_NAME)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_list, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        //set Toolbar title as the name of dog breed for which images are fetched and displayed
        (activity as AppCompatActivity?)!!.supportActionBar!!.setTitle(mBreedName)
        val recyclerView: RecyclerView = view.findViewById(R.id.recycler_view)
        recyclerView.layoutManager = StaggeredGridLayoutManager(2, OrientationHelper.VERTICAL)
        val adapter = BreedImagesListAdapter()
        recyclerView.adapter = adapter
        val viewModel = ViewModelProvider(this, BreedImageViewModelFactory(mBreedId)).get(
            BreedImagesViewModel::class.java
        )
        viewModel.breedImages.observe(
            viewLifecycleOwner,
            { pagedList: PagedList<BreedImage> ->
                adapter.submitList(
                    pagedList
                )
            })
    }

    override fun onDestroyView() {
        context?.let { ctx ->
            {
                view?.let {
                    Glide.with(ctx).clear(it)
                }
            }
        }
        super.onDestroyView()
    }

    companion object {
        private const val ARG_BREED_ID = "breed_id"
        private const val ARG_BREED_NAME = "breed_name"

        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param breed The breed of dogs for which images are to be fetched
         * @return A new instance of fragment BreedImagesFragment.
         */
        fun newInstance(view: View?, breed: Breed) {
            val fragment = BreedImagesFragment()
            val args = Bundle()
            args.putInt(
                ARG_BREED_ID,
                breed.mId!!
            )
            args.putString(ARG_BREED_NAME, breed.mName)
            fragment.arguments = args
            Navigation.findNavController(view!!).navigate(R.id.loadBreedImages, args)
        }
    }
}
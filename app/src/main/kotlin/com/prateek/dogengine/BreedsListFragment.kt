package com.prateek.dogengine

import android.os.Bundle
import android.view.*
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.prateek.dogengine.adapter.BreedsListAdapter
import com.prateek.dogengine.data.Breed
import com.prateek.dogengine.data.DogBreedsRepository
import com.prateek.dogengine.data.RemoteDogBreedDataSource
import com.prateek.dogengine.viewmodel.BreedViewModel
import com.prateek.dogengine.viewmodel.BreedViewModelFactory

/**
 * Fragment to display list of dog breeds based on query searched by user
 *
 *
 * It features a SearchView at the top on Toolbar. Its added as an options menu item.
 * It makes use of ViewModel and DataBinding Android frameworks to efficiently load and display data.
 */
class BreedsListFragment : Fragment() {
    private lateinit var mViewModel: BreedViewModel
    private lateinit var mErrorView: TextView
    private lateinit var mRecyclerView: RecyclerView
    private lateinit var mLoadingView: View

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
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

        //init view components
        setupUI(view)
        val adapter = BreedsListAdapter()

        //set up observer to observe any changes to our breed list held by our view model
        setupBreedListObserver(adapter)

        //set up observer to observe any error messages received in our search request response
        setupErrorObserver(adapter)
    }

    private fun setupErrorObserver(adapter: BreedsListAdapter) {
        mViewModel.getError().observe(viewLifecycleOwner, Observer { s: String? ->
            //hide the loading view once observer receives an error response to our search request
            mLoadingView.visibility = View.GONE
            //clear the list view to remove any items from the UI from previously search result
            adapter.clear()
            //show the error view and set the error message
            mErrorView.visibility = View.VISIBLE
            mErrorView.text = s
        })
    }

    private fun setupBreedListObserver(adapter: BreedsListAdapter) {
        mViewModel = ViewModelProvider(
            this,
            BreedViewModelFactory()
        ).get(BreedViewModel::class.java)

        mViewModel.getBreeds().observe(
            viewLifecycleOwner,
            { breeds: List<Breed>? ->
                //hide any error that might have be shown in previous request
                mErrorView.visibility = View.GONE

                /*
                A null value is intentionally set by us to let the UI know we are about to make a new
                search request so clear the current UI. Its not  anything necessary but give a nice UX
                */
                if (breeds == null) {
                    adapter.clear()
                    return@observe
                }

                //hide the loading view shown during search request
                mLoadingView.visibility = View.GONE
                adapter.setData(breeds as MutableList<Breed>?)
                mRecyclerView.adapter = adapter

                /*
                Show a predefined error message to user to let them know that their search query did not
                yield any result so request them to try again with a different search query.
                 */
                if (breeds.isEmpty()) {
                    mErrorView.setText(R.string.no_breeds_found)
                    mErrorView.visibility = View.VISIBLE
                }
            })
    }

    private fun setupUI(view: View) {
        //Update toolbar title every time user visits this fragment
        (activity as AppCompatActivity?)!!.supportActionBar!!.setTitle(R.string.app_name)
        mRecyclerView = view.findViewById(R.id.recycler_view)
        mLoadingView = view.findViewById(R.id.loading_view)
        mErrorView = view.findViewById(R.id.default_error)
        mErrorView.visibility = View.VISIBLE
        mRecyclerView.layoutManager = LinearLayoutManager(
            activity,
            LinearLayoutManager.VERTICAL, false
        )
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.search_menu, menu)
        val menuItem = menu.findItem(R.id.action_search)
        val searchView = menuItem.actionView as SearchView
        val searchUseCase = SearchDogBreedsUseCase(
            DogBreedsRepository(RemoteDogBreedDataSource())
        )
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String): Boolean {
                //Initiate a search request only if user has entered at least one character
                if (query.isNotEmpty()) {
                    //hide any previously shown error view
                    mErrorView.visibility = View.GONE
                    //show the loading view
                    mLoadingView.visibility = View.VISIBLE
                    //make a search request to fetch data based on query entered by user in the search view
                    mViewModel.searchBreeds(searchUseCase, query)
                    //clear search view focus to hide the soft keyboard
                    searchView.clearFocus()
                }
                return false
            }

            override fun onQueryTextChange(newText: String): Boolean {
                return false
            }
        })
    }
}
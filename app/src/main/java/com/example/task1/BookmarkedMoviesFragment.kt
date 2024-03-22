package com.example.task1

import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.task1.databinding.FragmentBookmarkedMoviesBinding

class BookmarkedMoviesFragment : Fragment(),MovieClicked,BroadcastNotifyAnUpdate.BroadcastReceiverListener {


    private lateinit var bookmarkedMoviesList : MutableList<Movie>
    private lateinit var sharedPreferences : SharedPrefManager

    private lateinit var myCustomAdapter: CustomeAdapter
    private var moviesList = mutableListOf<Movie>()
    private lateinit var broadcastReceiver : BroadcastNotifyAnUpdate

    private var _binding : FragmentBookmarkedMoviesBinding? = null
    private val binding get() = _binding!!



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {

        }

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentBookmarkedMoviesBinding.inflate(inflater, container, false)



        moviesList = arguments?.getParcelableArrayList("moviesList")!!
        broadcastReceiver = BroadcastNotifyAnUpdate(this)
        val filter = IntentFilter("deleteMovie")
        val receiverFlags = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            Context.RECEIVER_NOT_EXPORTED
        } else {
            0
        }
        if(activity != null){
            requireActivity().registerReceiver(broadcastReceiver, filter, receiverFlags)
        }
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initialize()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        if(activity != null)
            requireActivity().unregisterReceiver(broadcastReceiver)
    }

    private fun initialize(){
        binding.recyclerViewBookmarkedFragment.layoutManager= LinearLayoutManager(context)
        bookmarkedMoviesList= mutableListOf<Movie>()
        if(context != null){
            sharedPreferences = SharedPrefManager(requireContext())
        }
        updateBookmarkList()
    }

    private fun updateBookmarkList(){
        bookmarkedMoviesList = mutableListOf()
        val moviesIDs=sharedPreferences.getIdsList()

        for(id in moviesIDs){
            val movie = getMovieData(id)
            if (movie != null) {
                bookmarkedMoviesList.add(movie)
            }
        }

        if(bookmarkedMoviesList.isEmpty()){
            binding.recyclerViewBookmarkedFragment.visibility=View.GONE
            binding.emptyTextBookmarkedFragment.visibility=View.VISIBLE
        }

        else {
            if(context != null) {
                binding.recyclerViewBookmarkedFragment.visibility = View.VISIBLE
                binding.emptyTextBookmarkedFragment.visibility = View.GONE
                myCustomAdapter = CustomeAdapter(this@BookmarkedMoviesFragment, requireContext(), bookmarkedMoviesList)
                binding.recyclerViewBookmarkedFragment.adapter = myCustomAdapter
            }
        }
    }



    private fun getMovieData(id : Int) : Movie?{
        var bookmarkedMovie: Movie? = null
        for (movie in moviesList){
            if(movie.id == id){
                bookmarkedMovie=movie
                break
            }
        }
        return bookmarkedMovie
    }


    override fun onMovieClicked(movieData: Movie) {
        val intent = Intent(context,MovieActivity::class.java)
        intent.putExtra("movie",movieData)
        startActivity(intent)
    }



    private fun deleteMovie(id : Int){
        Log.d("Movie deleted -----> $id","Movie deleted -----> $id")
        val movieToDelete=getMovieData(id)
        if (bookmarkedMoviesList.isNotEmpty()) {
            bookmarkedMoviesList.remove(movieToDelete)
        }

        if(bookmarkedMoviesList.isEmpty()){
            binding.recyclerViewBookmarkedFragment.visibility=View.GONE
            binding.emptyTextBookmarkedFragment.visibility=View.VISIBLE
        }
        else {
            binding.recyclerViewBookmarkedFragment.visibility = View.VISIBLE
            binding.emptyTextBookmarkedFragment.visibility = View.GONE
            myCustomAdapter = CustomeAdapter(this@BookmarkedMoviesFragment, requireContext(), bookmarkedMoviesList)
            binding.recyclerViewBookmarkedFragment.adapter = myCustomAdapter
        }
    }

    override fun onBroadcastReceived(id : Int) {
        deleteMovie(id)
        updateBookmarkList()
    }

}


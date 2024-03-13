package com.example.task1

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import org.w3c.dom.Text
import retrofit2.Call
import retrofit2.Callback

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

class HomeFragment : Fragment(),MovieClicked {

    private lateinit var myCustomAdapter: CustomeAdapter
    private lateinit var recyclerView : RecyclerView
    private var moviesList = mutableListOf<Movie>()
    private lateinit var emptyTextHomeFragment : TextView

    private var param1: String? = null
    private var param2: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }

    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val view = inflater.inflate(R.layout.home_fragment_all_movies, container, false)
        moviesList = arguments?.getParcelableArrayList("moviesList")!!
        return view

    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initialize(view)
    }


    private fun initialize(view : View){
        wrapViews(view)
        getAllData()
    }

    private fun wrapViews(view : View){
        recyclerView=view.findViewById(R.id.homeFragmentRecView)
        emptyTextHomeFragment=view.findViewById(R.id.emptyTextHomeFragment)
        recyclerView.layoutManager= LinearLayoutManager(context)

    }

    private fun getAllData(){
        myCustomAdapter = CustomeAdapter(this@HomeFragment, requireContext(),moviesList)
        recyclerView.adapter=myCustomAdapter
    }


    companion object {
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            HomeFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }

    override fun onMovieClicked(movieData: Movie) {
        val intent = Intent(context,MovieActivity::class.java)
        intent.putExtra("movie",movieData)
        startActivity(intent)
    }
}
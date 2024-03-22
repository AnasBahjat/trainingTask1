package com.example.task1

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.task1.databinding.HomeFragmentAllMoviesBinding


class HomeFragment : Fragment(),MovieClicked {

    private lateinit var myCustomAdapter: CustomeAdapter
    private var moviesList = mutableListOf<Movie>()

    private var _binding : HomeFragmentAllMoviesBinding?=null
    private val binding get()=_binding!!


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {

        }

    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = HomeFragmentAllMoviesBinding.inflate(inflater,container,false)
        moviesList = arguments?.getParcelableArrayList("moviesList")!!
        return binding.root

    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initialize()
    }


    private fun initialize(){
        binding.homeFragmentRecView.layoutManager=LinearLayoutManager(context)
        if(activity != null){
            myCustomAdapter = CustomeAdapter(this@HomeFragment, requireActivity(),moviesList)
            binding.homeFragmentRecView.adapter=myCustomAdapter
        }

    }


    override fun onMovieClicked(movieData : Movie) {
        val intent = Intent(context,MovieActivity::class.java)
        intent.putExtra("movie",movieData)
        startActivity(intent)
    }
}
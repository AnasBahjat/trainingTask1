package com.example.task1


import android.os.Bundle
import android.view.View
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.task1.databinding.HomeScreenBinding

class HomeActivity : AppCompatActivity() {
    private var moviesList = mutableListOf<Movie>()
    private lateinit var bookmarkedMoviesList : MutableList<Movie>
    private lateinit var sharedPreferences : SharedPrefManager
    private lateinit var binding :  HomeScreenBinding
    private lateinit var viewModel: MyViewModel


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = HomeScreenBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initialize()
    }

    private fun initialize(){
        wrapViews()

        viewModel.readDataFromAPI()
        viewModel.returnApiData().observe(this, Observer{ allMovies ->
            moviesList=allMovies
            loadFragment(HomeFragment())
        })

        binding.bottomNav.setOnItemSelectedListener {
            when(it.itemId){
                R.id.homeMovies -> {
                    loadFragment(HomeFragment())
                true}
                R.id.bookmarkMovies ->{
                    loadFragment(BookmarkedMoviesFragment())
                    binding.progressBar.visibility=View.GONE
                    true
                }
                else -> {
                    true
                }
            }
        }
    }


    private fun wrapViews(){
        bookmarkedMoviesList= mutableListOf()
        sharedPreferences = SharedPrefManager(this)
        binding.progressBar.visibility=View.VISIBLE
        binding.bottomNav.menu.getItem(1).isEnabled = false
        viewModel = ViewModelProvider(this)[MyViewModel::class.java]
    }


    private fun loadFragment(fragment : Fragment){
        binding.progressBar.visibility=View.GONE
        val bundle = Bundle().apply {
            putParcelableArrayList(Constants.MOVIES_LIST, ArrayList(moviesList))
        }
        fragment.arguments = bundle
        val transaction = supportFragmentManager.beginTransaction()
        transaction.replace(R.id.fragmentsContainer,fragment)
        transaction.commit()
    }
}

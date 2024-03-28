package com.example.task1


import android.os.Bundle
import android.view.View
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.example.task1.databinding.HomeScreenBinding

class HomeActivity : AppCompatActivity(),DataIsReady,StopTheApp {
    private var moviesList = mutableListOf<Movie>()
    private lateinit var bookmarkedMoviesList : MutableList<Movie>
    private lateinit var sharedPreferences : SharedPrefManager
    private lateinit var myAlertDialog: MyAlertDialog
    private lateinit var binding :  HomeScreenBinding
    private lateinit var readServerData : GetDataFromServer


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = HomeScreenBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initialize()
    }

    private fun initialize(){
        wrapViews()

        readServerData.getAllData()

        binding.bottomNav.setOnItemSelectedListener {
            when(it.itemId){
                R.id.homeMovies -> {
                    loadFragment(HomeFragment())
                true}
                R.id.bookmarkMovies ->{
                    loadFragment(BookmarkedMoviesFragment())
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
        myAlertDialog = MyAlertDialog(this,this)
        readServerData = GetDataFromServer(this,this,this)
    }


    private fun loadFragment(fragment : Fragment){
        val bundle = Bundle().apply {
            putParcelableArrayList(Constants.MOVIES_LIST, ArrayList(moviesList))
        }
        fragment.arguments = bundle
        val transaction = supportFragmentManager.beginTransaction()
        transaction.replace(R.id.fragmentsContainer,fragment)
        transaction.commit()
    }

    override fun dataReadyToBeDisplayed(movies: MutableList<Movie>) {
        binding.progressBar.visibility=View.GONE
        moviesList=movies
        loadFragment(HomeFragment())
    }

    override fun stopApp() {
        finish()
    }
}
interface StopTheApp{
    fun stopApp()
}
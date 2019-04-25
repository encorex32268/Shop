package com.lee.shop

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.android.synthetic.main.activity_movie.*
import kotlinx.android.synthetic.main.row_movie.view.*
import org.jetbrains.anko.AnkoLogger
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.info
import org.jetbrains.anko.uiThread
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import java.net.URL

class MovieActivity : AppCompatActivity() , AnkoLogger{

    var movies: List<Movie>? = null

    val retrofit = Retrofit.Builder()
        .addConverterFactory(GsonConverterFactory.create())
        .baseUrl("https://api.myjson.com/bins/")
        .build()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_movie)

        doAsync {
//            val json = URL("https://api.myjson.com/bins/149im8").readText()
//            movies = Gson().fromJson<List<Movie>>(json,
//                object : TypeToken<List<Movie>>(){}.type)
            val movieService = retrofit.create(MovieService::class.java)
            movies =
                movieService.movieList()
                .execute().body()
            movies?.forEach {
                info {
                    "${it.Title} , ${it.Actors} , ${it.imdbRating}"
                }
            }
            uiThread {
                movieRecycler.layoutManager = LinearLayoutManager(this@MovieActivity)
                movieRecycler.setHasFixedSize(true)
                movieRecycler.adapter = MovieAdapter()
            }
        }


    }


    inner class MovieAdapter : RecyclerView.Adapter<MovieHolder>() {
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MovieHolder {
            val view = LayoutInflater.from(parent.context).inflate(R.layout.row_movie,parent,false)
            return MovieHolder(view)
        }

        override fun getItemCount(): Int {
            return movies?.size?:0
        }

        override fun onBindViewHolder(holder: MovieHolder, position: Int) {
            holder.bindMovie(movies?.get(position))
        }

    }

    inner class MovieHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val title = itemView.movieTitle
        val rate  = itemView.movieRate
        val directer = itemView.movieDirecter
       // val poster = itemView.moviePoster
        fun bindMovie(movie : Movie?){
            title.text = movie?.Title
            rate.text = movie?.imdbRating
            directer.text = movie?.Director
//            Glide.with(this@MovieActivity)
//                .load(movie?.Poster)
//                .override(300)
//                .into(poster)
        }
    }
}



data class Movie(
    val Actors: String,
    val Awards: String,
    val Country: String,
    val Director: String,
    val Genre: String,
    val Images: List<String>,
    val Language: String,
    val Metascore: String,
    val Plot: String,
    val Poster: String,
    val Rated: String,
    val Released: String,
    val Response: String,
    val Runtime: String,
    val Title: String,
    val Type: String,
    val Writer: String,
    val Year: String,
    val imdbID: String,
    val imdbRating: String,
    val imdbVotes: String
)

interface MovieService{

    @GET("149im8")
    fun movieList() : Call<List<Movie>>
}

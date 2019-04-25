package com.lee.shop

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.android.synthetic.main.activity_bus.*
import kotlinx.android.synthetic.main.row_bus.view.*
import org.jetbrains.anko.AnkoLogger
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.info
import org.jetbrains.anko.uiThread
import java.net.URL

class BusActivity : AppCompatActivity(),AnkoLogger {

    var buses :List<Bus>? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_bus)

        doAsync {

            val OPENDATA_URL =
                "http://data.tycg.gov.tw/opendata/datalist/datasetMeta/download?id=b3abedf0-aeae-4523-a804-6e807cbad589&rid=bf55b21a-2b7c-4ede-8048-f75420344aed"
            val readJson = URL(OPENDATA_URL).readText()
            val busDatas = Gson().fromJson<AllBus>(readJson,AllBus::class.java)
            buses = busDatas.datas
            uiThread {
                busRecycler.layoutManager = LinearLayoutManager(this@BusActivity)
                busRecycler.setHasFixedSize(true)
                busRecycler.adapter = BusAdapter()
            }


        }
    }

    inner class BusAdapter : RecyclerView.Adapter<BusViewHolder>(){
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BusViewHolder {
            val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.row_bus,parent,false)
            return BusViewHolder(view)
        }

        override fun getItemCount(): Int {
            return buses?.size?:0
        }

        override fun onBindViewHolder(holder: BusViewHolder, position: Int) {
            holder.bindView(buses!![position])
        }

    }

    inner class BusViewHolder(view : View) : RecyclerView.ViewHolder(view){
        val busId = view.busId
        val busRouteId = view.busRouteId
        val busSpeed = view.busSpeed

        fun bindView(bus : Bus){
            busId.text = bus.BusID
            busRouteId.text = bus.RouteID
            busSpeed.text = bus.Speed
        }

    }


}

/*
  datas:[{
    BusId : ""
    ...
    ,
    BusId : ""
    ...

  }]
 */

data class AllBus(
    val datas: List<Bus>
)

data class Bus(
    val Azimuth: String,
    val BusID: String,
    val BusStatus: String,
    val DataTime: String,
    val DutyStatus: String,
    val GoBack: String,
    val Latitude: String,
    val Longitude: String,
    val ProviderID: String,
    val RouteID: String,
    val Speed: String,
    val ledstate: String,
    val sections: String
)
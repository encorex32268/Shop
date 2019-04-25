package com.lee.shop

import android.os.AsyncTask
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.method.ScrollingMovementMethod
import android.util.Log
import com.google.gson.Gson
import kotlinx.android.synthetic.main.activity_parking.*
import org.jetbrains.anko.*
import java.net.URL

class ParkingActivity : AppCompatActivity(),AnkoLogger {

    private val TAG = ParkingActivity::class.java.simpleName

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_parking)


        //http://data.tycg.gov.tw/opendata/datalist/datasetMeta/download?id=f4cc0b12-86ac-40f9-8745-885bddc18f79&rid=0daad6e6-0632-44f5-bd25-5e1de1e9146f

        val parking ="https://data.tycg.gov.tw/opendata/datalist/datasetMeta/download?id=f4cc0b12-86ac-40f9-8745-885bddc18f79&rid=0daad6e6-0632-44f5-bd25-5e1de1e9146f"

        //Anko
        doAsync {
            val url = URL(parking)
            val json = url.readText()
            info(json)
            uiThread {
                toast("Got it")
                parkingInfo.text = json
                alert("Got it","ALERT"){
                    okButton {
                        parseGson(json)
                    }
                }.show()

            }
        }

        // ParkingTask().execute(parking)

    }

    private fun parseGson(json: String) {
        val parking = Gson().fromJson<Parking>(json,Parking::class.java)
        info(parking.parkingLots.size)
        parking.parkingLots.forEach {
            info { " ${it.areaId} , ${it.areaName} , ${it.parkName}"   }
        }
    }

    inner class ParkingTask : AsyncTask<String,Void,String>(){
        override fun doInBackground(vararg params: String?): String {
            val url = URL(params[0])
            val json = url.readText()
            Log.d(TAG, "doInBackground: $json")
            return json
        }

        override fun onPostExecute(result: String?) {
            super.onPostExecute(result)
            parkingInfo.text = result
            parkingInfo.movementMethod = ScrollingMovementMethod()
        }
    }


}
data class Parking(
    val parkingLots: List<ParkingLot>
)

data class ParkingLot(
    val address: String,
    val areaId: String,
    val areaName: String,
    val introduction: String,
    val parkId: String,
    val parkName: String,
    val payGuide: String,
    val surplusSpace: String,
    val totalSpace: Int,
    val wgsX: Double,
    val wgsY: Double
)



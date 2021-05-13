package com.example.trabalhopraticocm

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.content.pm.PackageManager
import android.location.Location
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.EditText
import android.widget.Toast
import androidx.core.app.ActivityCompat
import com.example.trabalhopraticocm.api.EndPoints
import com.example.trabalhopraticocm.api.OutputReport
import com.example.trabalhopraticocm.api.ServiceBuilder
import com.google.android.gms.location.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ReportReport : AppCompatActivity() {

    private lateinit var titleEdit: EditText
    private lateinit var descrEdit: EditText
    private lateinit var preferences: SharedPreferences
    private var latitude : Double = 0.0
    private var longitude : Double = 0.0

    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private val LOCATION_PERMISSION_REQUEST_CODE = 1
    private lateinit var locationRequest: LocationRequest
    private lateinit var locationCallback: LocationCallback
    private lateinit var lastLocation: Location

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_report_report)

        titleEdit = findViewById(R.id.title)
        descrEdit = findViewById(R.id.descr)
        preferences = getSharedPreferences("preferences", Context.MODE_PRIVATE)

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)

        locationCallback = object : LocationCallback() {
            override fun onLocationResult(p0: LocationResult?) {
                super.onLocationResult(p0)
                lastLocation = p0?.lastLocation!!
                latitude = lastLocation.latitude
                longitude = lastLocation.longitude
            }
        }

        createLocationRequest()
    }

    private fun createLocationRequest() {
        locationRequest = LocationRequest()
        locationRequest.interval = 10000
        locationRequest.priority = LocationRequest.PRIORITY_HIGH_ACCURACY

    }
    fun report(view: View) {
        val request = ServiceBuilder.buildService(EndPoints::class.java)
        val latitude = latitude
        val longitude = longitude
        val titulo = titleEdit.text.toString()
        val descr = descrEdit.text.toString()
        val user_id = preferences.getInt("id", 0)

        val call = request.report(
            latitude = latitude.toString(),
            longitude = longitude.toString(),
            titulo = titulo,
            descr = descr,
            img = "Imagem",
            user_id = user_id)

        call.enqueue(object : Callback<OutputReport> {
            override fun onResponse(call: Call<OutputReport>, response: Response<OutputReport>){
                if (response.isSuccessful){
                    val c: OutputReport = response.body()!!
                    Toast.makeText(this@ReportReport, c.MSG, Toast.LENGTH_LONG).show()
                    val intent = Intent(this@ReportReport, MapsActivity::class.java)
                    startActivity(intent);
                    finish()

                }
            }
            override fun onFailure(call: Call<OutputReport>, t: Throwable){
                Toast.makeText(this@ReportReport,"${t.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }

    override fun onPause() {
        super.onPause()
        fusedLocationClient.removeLocationUpdates(locationCallback)
    }

    override fun onResume() {
        super.onResume()
        startLocationUpdates()
    }

    private fun startLocationUpdates() {
        if(ActivityCompat.checkSelfPermission(this,
                android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED){

            ActivityCompat.requestPermissions(this,
                arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION), LOCATION_PERMISSION_REQUEST_CODE)

            return
        }
        fusedLocationClient.requestLocationUpdates(locationRequest, locationCallback, null)

    }

}
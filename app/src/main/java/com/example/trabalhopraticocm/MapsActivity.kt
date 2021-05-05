package com.example.trabalhopraticocm

import android.content.Context
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.trabalhopraticocm.api.EndPoints
import com.example.trabalhopraticocm.api.Report
import com.example.trabalhopraticocm.api.ServiceBuilder

import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MapsActivity : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var mMap: GoogleMap
    private lateinit var preferences: SharedPreferences
    private lateinit var  reports: List<Report>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_maps)
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)
        preferences = getSharedPreferences("preferences", Context.MODE_PRIVATE)

        val request = ServiceBuilder.buildService(EndPoints::class.java)
        val call = request.getReports()
        var coordenadas: LatLng
        val user_id = preferences.getInt("id", 0)

        call.enqueue(object : Callback<List<Report>> {
            override fun onResponse(call: Call<List<Report>>, response: Response<List<Report>>){
                if (response.isSuccessful){
                    reports = response.body()!!
                    for (report in reports){
                        coordenadas = LatLng(report.latitude.toDouble(), report.longitude.toDouble())
                        if(report.user_id == user_id){
                            mMap.addMarker(MarkerOptions().position(coordenadas).title(report.id.toString()).snippet(report.titulo + "-" + report.descr))
                                    .setIcon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN))
                        }else{
                            mMap.addMarker(MarkerOptions().position(coordenadas).title(report.id.toString()).snippet(report.titulo + "-" + report.descr))
                        }
                    }
                }
            }
            override fun onFailure(call: Call<List<Report>>, t: Throwable){
                Toast.makeText(this@MapsActivity,"${t.message}", Toast.LENGTH_LONG).show()
            }
        })
    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap

        // Add a marker in Sydney and move the camera
        val sydney = LatLng(-34.0, 151.0)
        mMap.addMarker(MarkerOptions().position(sydney).title("Marker in Sydney"))
        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney))
    }
}
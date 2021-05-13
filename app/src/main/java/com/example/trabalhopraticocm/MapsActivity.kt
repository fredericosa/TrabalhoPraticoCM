package com.example.trabalhopraticocm

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.content.pm.PackageManager
import android.location.Location
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.core.app.ActivityCompat
import com.example.trabalhopraticocm.api.*
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MapsActivity : AppCompatActivity(), OnMapReadyCallback, GoogleMap.OnInfoWindowClickListener {

    private lateinit var mMap: GoogleMap
    private lateinit var  reports: List<Report>
    private lateinit var lastLocation: Location
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private val LOCATION_PERMISSION_REQUEST_CODE = 1
    private val newEditReportActivityRequestCode = 1
    private lateinit var preferences: SharedPreferences


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_maps)

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
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

    override fun onInfoWindowClick(p0: Marker?) {
        val request = ServiceBuilder.buildService(EndPoints::class.java)
        val call :Call <List<Report>> = request.getReportById(p0!!.title)
        val user_id = preferences.getInt("id", 0)

        call.enqueue(object : Callback<List<Report>> {
            override fun onResponse(call: Call<List<Report>>, response: Response<List<Report>>) {
                if(response.isSuccessful){
                    reports = response.body()!!
                    for(report in reports){
                        if(report.user_id == user_id){
                            Toast.makeText(this@MapsActivity, report.descr, Toast.LENGTH_SHORT).show()
                            val intent = Intent(this@MapsActivity,EditReport::class.java)
                            intent.putExtra(PARAM_ID2, report.id.toString())
                            intent.putExtra(PARAM_TITLE, report.titulo)
                            intent.putExtra(PARAM_DESCR, report.descr)
                            intent.putExtra(PARAM_LATITUDE, report.latitude)
                            intent.putExtra(PARAM_LONGITUDE, report.longitude)
                            intent.putExtra(PARAM_LONGITUDE, report.longitude)
                            intent.putExtra(PARAM_USER_ID, report.user_id.toString())
                            startActivityForResult(intent, newEditReportActivityRequestCode)
                        }else{
                            Toast.makeText(this@MapsActivity,"Sem permissão para alterar este report.", Toast.LENGTH_LONG).show()
                        }

                    }
                }
            }
            override fun onFailure(call: Call<List<Report>>, t: Throwable) {
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
        /*val sydney = LatLng(-34.0, 151.0)
        mMap.addMarker(MarkerOptions().position(sydney).title("Marker in Sydney"))
        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney))*/
        mMap.setOnInfoWindowClickListener(this)
        setUpMap()
    }

    private fun setUpMap() {
        if(ActivityCompat.checkSelfPermission(this,
                        android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED){

            ActivityCompat.requestPermissions(this,
                    arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION), LOCATION_PERMISSION_REQUEST_CODE)

            return
        } else{
            //1
            mMap.isMyLocationEnabled = true

            //2
            fusedLocationClient.lastLocation.addOnSuccessListener(this) {location ->
                //3
                if(location != null){
                    lastLocation = location
                    //Toast.makeText(this@MapsActivity, lastLocation.toString(), Toast.LENGTH_SHORT).show()
                    val currentLatLng = LatLng(location.latitude, location.longitude)
                    mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(currentLatLng, 12f))
                }
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        // EDITAR E APAGAR NOTA
        if (requestCode == newEditReportActivityRequestCode && resultCode == Activity.RESULT_OK) {
            var id = data?.getStringExtra(EditReport.EDIT_ID)
            var id_delete = data?.getStringExtra(EditReport.DELETE_ID)
            var edit_title = data?.getStringExtra(EditReport.EDIT_TITLE).toString()
            var edit_descr= data?.getStringExtra(EditReport.EDIT_DESCR).toString()
            var edit_latitude = data?.getDoubleExtra(EditReport.EDIT_LATITUDE, 0.0).toString()
            var edit_longitude = data?.getDoubleExtra(EditReport.EDIT_LONGITUDE, 0.0).toString()
            val user_id = preferences.getInt("id", 0)

            if(data?.getStringExtra(EditReport.STATUS) == "EDIT"){
                val request = ServiceBuilder.buildService(EndPoints::class.java)
                val call = request.edit(
                    id = id,
                    latitude = edit_latitude,
                    longitude = edit_longitude,
                    titulo = edit_title,
                    descr = edit_descr,
                    img = "Imagem",
                    user_id = user_id)

                call.enqueue(object : Callback<OutputEdit> {
                    override fun onResponse(call: Call<OutputEdit>, response: Response<OutputEdit>){
                        if (response.isSuccessful){
                            val c: OutputEdit = response.body()!!
                            Toast.makeText(this@MapsActivity, c.MSG, Toast.LENGTH_LONG).show()
                            val intent = Intent(this@MapsActivity, MapsActivity::class.java)
                            startActivity(intent)
                            finish()
                        }
                    }
                    override fun onFailure(call: Call<OutputEdit>, t: Throwable){
                        Toast.makeText(this@MapsActivity,"${t.message}", Toast.LENGTH_SHORT).show()
                    }
                })
            } else if(data?.getStringExtra(EditReport.STATUS) == "DELETE"){

                val request = ServiceBuilder.buildService(EndPoints::class.java)
                val call = request.delete(
                    id = id_delete)

                call.enqueue(object : Callback<OutputDelete> {
                    override fun onResponse(call: Call<OutputDelete>, response: Response<OutputDelete>){
                        if (response.isSuccessful){
                            val c: OutputDelete = response.body()!!
                            Toast.makeText(this@MapsActivity, c.MSG, Toast.LENGTH_LONG).show()
                            val intent = Intent(this@MapsActivity, MapsActivity::class.java)
                            startActivity(intent)
                            finish()
                        }
                    }
                    override fun onFailure(call: Call<OutputDelete>, t: Throwable){
                        Toast.makeText(this@MapsActivity,"${t.message}", Toast.LENGTH_SHORT).show()
                    }
                })
            }

        } else if (resultCode == Activity.RESULT_CANCELED) {
            if(data?.getStringExtra(EditNote.STATUS) == "EDIT"){
                Toast.makeText(this, "Report não alterado. Campos Vazios.", Toast.LENGTH_SHORT).show()
            } else if(data?.getStringExtra(EditNote.STATUS) == "DELETE"){
                Toast.makeText(this, "Report não eliminado. Campos Vazios.", Toast.LENGTH_SHORT).show()
            }
        }
    }

    companion object {
        const val STATUS = ""
        const val DELETE_ID = "DELETE_ID"
        const val PARAM_ID2 = "PARAM_ID2"
        const val PARAM_TITLE = "PARAM_TITLE"
        const val PARAM_DESCR = "PARAM_DESCR"
        const val PARAM_LATITUDE = "PARAM_LATITUDE"
        const val PARAM_LONGITUDE = "PARAM_LONGITUDE"
        const val PARAM_USER_ID = "PARAM_USER_ID"
    }
}
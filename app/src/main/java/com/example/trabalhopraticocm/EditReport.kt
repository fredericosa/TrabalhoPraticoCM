package com.example.trabalhopraticocm

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.content.pm.PackageManager
import android.location.Location
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.widget.Button
import android.widget.EditText
import androidx.core.app.ActivityCompat
import com.example.trabalhopraticocm.MapsActivity.Companion.PARAM_DESCR
import com.example.trabalhopraticocm.MapsActivity.Companion.PARAM_ID2
import com.example.trabalhopraticocm.MapsActivity.Companion.PARAM_TITLE
import com.google.android.gms.location.*

class EditReport : AppCompatActivity() {

    private lateinit var editTitleView: EditText
    private lateinit var editDescrView: EditText
    private var latitude : Double = 0.0
    private var longitude : Double = 0.0
    private lateinit var preferences: SharedPreferences

    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private val LOCATION_PERMISSION_REQUEST_CODE = 1
    private lateinit var locationRequest: LocationRequest
    private lateinit var locationCallback: LocationCallback
    private lateinit var lastLocation: Location

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_report)

        editTitleView = findViewById(R.id.edit_title)
        editDescrView = findViewById(R.id.edit_descr)

        var id = intent.getStringExtra(PARAM_ID2)
        var title = intent.getStringExtra(PARAM_TITLE)
        var descr = intent.getStringExtra(PARAM_DESCR)
        editTitleView.setText(title.toString())
        editDescrView.setText(descr.toString())

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

        val button = findViewById<Button>(R.id.button_edit)
        button.setOnClickListener {
            val replyIntent = Intent()
            replyIntent.putExtra(EDIT_ID, id)
            if (TextUtils.isEmpty(editTitleView.text)  || TextUtils.isEmpty(editDescrView.text)) {
                setResult(Activity.RESULT_CANCELED, replyIntent)
            } else {
                val edit_title = editTitleView.text.toString()
                replyIntent.putExtra(EDIT_TITLE, edit_title)
                val edit_descr = editDescrView.text.toString()
                replyIntent.putExtra(EDIT_DESCR, edit_descr)
                val latitude = latitude
                replyIntent.putExtra(EDIT_LATITUDE, latitude)
                val longitude = longitude
                replyIntent.putExtra(EDIT_LONGITUDE, longitude)
                replyIntent.putExtra(STATUS, "EDIT")
                setResult(Activity.RESULT_OK, replyIntent)
            }

            finish()
        }

        val button_delete = findViewById<Button>(R.id.button_delete)
        button_delete.setOnClickListener {
            val replyIntent = Intent()
            if (TextUtils.isEmpty(editTitleView.text)  || TextUtils.isEmpty(editDescrView.text)) {
                setResult(Activity.RESULT_CANCELED, replyIntent)
            } else {
                replyIntent.putExtra(DELETE_ID, id)
                replyIntent.putExtra(STATUS, "DELETE")
                setResult(Activity.RESULT_OK, replyIntent)
            }

            finish()
        }

    }

    private fun createLocationRequest() {
        locationRequest = LocationRequest()
        locationRequest.interval = 10000
        locationRequest.priority = LocationRequest.PRIORITY_HIGH_ACCURACY

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

    companion object {
        const val STATUS = ""
        const val DELETE_ID = "DELETE_ID"
        const val EDIT_ID = "EDIT_ID"
        const val EDIT_TITLE = "EDIT_TITLE"
        const val EDIT_DESCR = "EDIT_DESCR"
        const val EDIT_LATITUDE = "EDIT_LATITUDE"
        const val EDIT_LONGITUDE = "EDIT_LONGITUDE"
    }
}
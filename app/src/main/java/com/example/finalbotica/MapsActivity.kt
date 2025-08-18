package com.example.finalbotica

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle

import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.example.finalbotica.databinding.ActivityMapsBinding

class MapsActivity : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var mMap: GoogleMap
    private lateinit var binding: ActivityMapsBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar?.apply {
            setDisplayHomeAsUpEnabled(true) // Activa el botón "atrás" (home)
            setHomeAsUpIndicator(R.drawable.ic_baseline_reply_24) // Cambia icono
        }


        binding = ActivityMapsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)
    }
    override fun onOptionsItemSelected(item: android.view.MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> { // Este es el botón "up" (flecha o icono que pusiste)
                val intent = Intent(this, MainActivity3::class.java)
                startActivity(intent)
                finish() // opcional, para que no vuelva aquí al presionar atrás
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
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

        mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL)
        val sydney = LatLng(-6.7666774,-79.842288)
        mMap.addMarker(MarkerOptions().position(sydney).title("Mi Casa en CIX"))
        //mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney))

        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(sydney, 20f))

        mMap.uiSettings.isZoomControlsEnabled = true
    }
}
package com.example.finalbotica

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem

class MainActivity3 : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main3)

        supportActionBar?.apply {
            setDisplayHomeAsUpEnabled(true) // Activa el botón "atrás" (home)
            setHomeAsUpIndicator(R.drawable.ic_baseline_reply_24) // Cambia icono
        }
    }
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu, menu)
        return super.onCreateOptionsMenu(menu)
    }
    /*
    fun atras() {
        finish()
    }
     */

    fun quienes(){
        val pantalla1 = Intent(this, MainActivity4::class.java)
        startActivity(pantalla1)
        finish()
    }

    fun ubicacion(){
        val pantalla1 = Intent(this, MapsActivity::class.java)
        startActivity(pantalla1)
        finish()
    }

    fun misionvision(){
        val pantalla1 = Intent(this, MainActivity5::class.java)
        startActivity(pantalla1)
        finish()
    }

    fun medicamentos(){
        val pantalla1 = Intent(this, MedicamentosActivity::class.java)
        startActivity(pantalla1)
        finish()
    }

    fun laboratorios(){
        val pantalla2 = Intent(this, LaboratorioActivity::class.java)
        startActivity(pantalla2)
        finish()
    }

    fun compras(){
        val pantalla3 = Intent(this, CompraActivity::class.java)
        startActivity(pantalla3)
        finish()
    }
    fun productos(){
        val pantalla4 = Intent(this, ViewMedicamentosActivity::class.java)
        startActivity(pantalla4)
        finish()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            R.id.ubicacion -> ubicacion()
            R.id.qSomos -> quienes()
            R.id.misionvision -> misionvision()
            R.id.medicamento -> medicamentos()
            R.id.laboratorio -> laboratorios()
            R.id.productos -> productos()
            R.id.compra -> compras()
            android.R.id.home -> { // Este es el botón "up" (flecha o icono que pusiste)
                val intent = Intent(this, MainActivity2::class.java)
                startActivity(intent)
                finish() // opcional, para que no vuelva aquí al presionar atrás
                true
            }
        }
        return super.onOptionsItemSelected(item)
    }

}
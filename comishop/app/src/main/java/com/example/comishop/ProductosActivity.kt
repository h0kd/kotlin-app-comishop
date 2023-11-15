package com.example.comishop

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.ListView
import android.widget.TextView
import android.widget.Toast
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError

import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener


class ProductosActivity : AppCompatActivity() {
    lateinit var listaProducto: ListView
    lateinit var botonHistorial: Button
    lateinit var botonSalir: Button
    lateinit var botonVenta: Button
    lateinit var databaseReference: DatabaseReference
    lateinit var campoEmail: TextView
    lateinit var adaptador: AdaptadorDeCeldas


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_productos)
        inicializar()
        databaseReference = FirebaseDatabase.getInstance().getReference("producto")
        llenarLista()
    }

    fun inicializar() {
        val correoCompleto = intent.getStringExtra("nombre")
        val nombreUsuario = obtenerNombreDeCorreo(correoCompleto)

        campoEmail = findViewById(R.id.email_field)
        campoEmail.text = nombreUsuario
        listaProducto = findViewById(R.id.lista_productos)
        botonHistorial = findViewById(R.id.boton_historial)
        botonSalir = findViewById(R.id.boton_salir)
        botonVenta = findViewById(R.id.boton_venta)
        botonHistorial.setOnClickListener {
            val intent = Intent(this, HistorialActivity::class.java)
            startActivity(intent)
        }
        botonSalir.setOnClickListener {
            finish()
        }
        botonVenta.setOnClickListener {
            val itemsSeleccionados = adaptador.getItemSelected()

            if (itemsSeleccionados.isNotEmpty()) {
                val productoAVender = itemsSeleccionados.first()
                val intent = Intent(this, VentaActivity2::class.java)


                intent.putExtra("producto_a_vender", productoAVender)
                startActivity(intent)
            }
        }
    }


    fun llenarLista() {
        databaseReference.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                val listaProductos = mutableListOf<Product>()

                for (snapshot in dataSnapshot.children) {
                    val producto = snapshot.getValue(Product::class.java)
                    if (producto != null) {
                        listaProductos.add(producto)
                    }
                }

                // Crear y configurar el adaptador con la lista de productos
                adaptador =
                    AdaptadorDeCeldas(this@ProductosActivity, listaProductos as ArrayList<Product>)
                listaProducto.adapter = adaptador
            }

            override fun onCancelled(databaseError: DatabaseError) {
                Toast.makeText(this@ProductosActivity, "error en la conexion", Toast.LENGTH_SHORT)
                    .show()
            }
        })
    }

    fun obtenerNombreDeCorreo(correoCompleto: String?): String {
        if (correoCompleto != null) {
            val partes = correoCompleto.split("@")
            if (partes.size >= 1) {
                return partes[0]
            }
        }
        return ""
    }
}
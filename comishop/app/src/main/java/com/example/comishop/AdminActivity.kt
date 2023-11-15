package com.example.comishop

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.ListView
import android.widget.Toast
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class AdminActivity : AppCompatActivity() {
    lateinit var listaProducto: ListView
    lateinit var botonVolver: Button
    lateinit var botonAgregar: Button
    lateinit var botonEliminar: Button
    lateinit var databaseReference: DatabaseReference
    lateinit var adaptador: AdaptadorDeCeldas
    lateinit var botonEditar: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_admin)
        inicializar()
        databaseReference = FirebaseDatabase.getInstance().getReference("producto")
        llenarLista()
    }

    fun inicializar() {
        listaProducto = findViewById(R.id.lista_admin)
        botonVolver = findViewById(R.id.boton_volver_admin)
        botonAgregar = findViewById(R.id.boton_agregar_admin)
        botonEliminar = findViewById(R.id.boton_eliminar_a)
        botonEditar = findViewById(R.id.boton_editar_admin)

        botonVolver.setOnClickListener {
            finish()
        }

        botonAgregar.setOnClickListener {
            finish()
            val intent = Intent(this, AgregarProductoActivity::class.java)
            startActivity(intent)
        }

        botonEliminar.setOnClickListener {
            val itemsSeleccionados = adaptador.getItemSelected()
            for (itemSeleccionado in itemsSeleccionados) {
                eliminarProductoDeFirebase(itemSeleccionado)
            }
            adaptador.removeItemSelected()
        }
        botonEditar.setOnClickListener {
            finish()
            val itemsSeleccionados = adaptador.getItemSelected().toList()

            if (itemsSeleccionados.isNotEmpty()) {
                val productoAEditar = itemsSeleccionados.first()
                val intent = Intent(this, AgregarProductoActivity::class.java)

                // Pasa el producto a editar como un Parcelable
                intent.putExtra("producto_a_editar", productoAEditar)

                startActivity(intent)
            } else {
                Toast.makeText(this, "Selecciona un producto para editar", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun eliminarProductoDeFirebase(producto: Product) {
        val productoNombre = producto.nombre

        if (productoNombre != null) {
            // Elimina el producto de Firebase utilizando el nombre como clave
            Toast.makeText(this, "Producto eliminado: $productoNombre", Toast.LENGTH_SHORT).show()
            val referenciaProducto = databaseReference.child(productoNombre)
            referenciaProducto.removeValue()

        } else {
            Toast.makeText(this, "Nombre del producto es nulo", Toast.LENGTH_SHORT).show()
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

                adaptador = AdaptadorDeCeldas(this@AdminActivity, listaProductos as ArrayList<Product>)
                listaProducto.adapter = adaptador
            }

            override fun onCancelled(databaseError: DatabaseError) {
                Toast.makeText(this@AdminActivity, "error en la conexión", Toast.LENGTH_SHORT).show()
            }
        })
    }
}

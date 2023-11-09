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

class HistorialActivity : AppCompatActivity() {
    lateinit var listaProductoBd: ListView
    lateinit var labelTotal : TextView
    lateinit var botonEditar : Button
    lateinit var botonEliminar : Button
    lateinit var databaseReference: DatabaseReference
    lateinit var adaptador: AdaptadorDeCeldasHistorial

    lateinit var botonRegresar: Button
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_historial2)
        inicializar()
        llenarLista()
    }

    fun inicializar() {
        adaptador = AdaptadorDeCeldasHistorial(this, ArrayList())
        listaProductoBd = findViewById(R.id.lista_admin)
        botonRegresar = findViewById(R.id.boton_regresar)
        botonEditar = findViewById(R.id.boton_editar_historial)
        botonEliminar = findViewById(R.id.boton_eliminar_historial)
        botonRegresar.setOnClickListener {
            finish()
        }
        labelTotal = findViewById(R.id.total_historial)

        // Inicializa databaseReference
        databaseReference = FirebaseDatabase.getInstance().getReference("venta")

        botonEliminar.setOnClickListener {
            val itemsSeleccionados = adaptador.getItemsSeleccionados().toList()
            for (itemSeleccionado in itemsSeleccionados) {
                eliminarProductoDeFirebase(itemSeleccionado)
            }
            adaptador.removeItem()
        }
        botonEditar.setOnClickListener {
            val elementosSeleccionados = adaptador.getItemsSeleccionados().toList()
            if (elementosSeleccionados.isNotEmpty()) {
                // Verifica que se haya seleccionado solo un elemento para editar
                if (elementosSeleccionados.size == 1) {
                    // Obtiene el objeto Venta seleccionado
                    val ventaAEditar = elementosSeleccionados[0]

                    // Crea un Intent para iniciar VentasActivity
                    val intent = Intent(this@HistorialActivity, VentaEditarActivity::class.java)

                    // Agrega el objeto Venta como extra en el Intent
                    intent.putExtra("ventaEditar", ventaAEditar)

                    // Inicia VentasActivity con el Intent
                    startActivity(intent)
                } else {
                    Toast.makeText(this, "Selecciona solo un elemento para editar", Toast.LENGTH_SHORT).show()
                }
            } else {
                Toast.makeText(this, "Selecciona un elemento para editar", Toast.LENGTH_SHORT).show()
            }
        }

    }

    fun llenarLista() {
        // Obtén una referencia al módulo "venta" en Firebase Realtime Database
        val databaseReference = FirebaseDatabase.getInstance().getReference("venta")

        // Realiza una consulta para obtener todas las ventas
        databaseReference.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                val listaVentas = mutableListOf<Venta>()

                for (snapshot in dataSnapshot.children) {
                    val venta = snapshot.getValue(Venta::class.java)
                    if (venta != null) {
                        listaVentas.add(venta)
                    }
                }

                // Utiliza la propiedad adaptador de la clase en lugar de una instancia local
                adaptador = AdaptadorDeCeldasHistorial(this@HistorialActivity, listaVentas as ArrayList<Venta>)
                listaProductoBd.adapter = adaptador

                calcularTotalVentas(listaVentas)
            }

            override fun onCancelled(databaseError: DatabaseError) {
                Toast.makeText(this@HistorialActivity, "Error en la conexión", Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun eliminarProductoDeFirebase(producto: Venta) {
        val productoNombre = producto.nombreProducto

        if (productoNombre != null) {
            // Elimina el producto de Firebase utilizando el nombre como clave
            Toast.makeText(this, "Producto eliminado: $productoNombre", Toast.LENGTH_SHORT).show()
            val referenciaProducto = databaseReference.child(productoNombre)
            referenciaProducto.removeValue()

        } else {
            Toast.makeText(this, "Nombre del producto es nulo", Toast.LENGTH_SHORT).show()
        }
    }

    fun calcularTotalVentas(listaVentas: List<Venta>) {
        var total = 0
        var calculo = 0
        var comision = 0.0
        for (venta in listaVentas) {
            calculo = venta.precioVenta-venta.precioUnitario
            comision = calculo * 0.3
            total += venta.precioUnitario
        }

        labelTotal.text = "Total Venta: $total \n" +
                "comision = $comision"  // Actualiza el texto en el TextView con el total calculado
    }
}


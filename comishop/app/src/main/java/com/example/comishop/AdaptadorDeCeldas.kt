package com.example.comishop

import android.content.Context
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ImageView
import android.widget.TextView
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.squareup.picasso.Picasso

class AdaptadorDeCeldas(private val context: Context, private val almacenDatos: ArrayList<Product>) : BaseAdapter() {

    private val inflador: LayoutInflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
    private val elementosSeleccionados = HashSet<Product>()

    override fun getCount(): Int {
        return almacenDatos.size
    }

    override fun getItem(position: Int): Any {
        return almacenDatos[position]
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val filaCelda = inflador.inflate(R.layout.adaptador_de_celda, parent, false)
        val imagen: ImageView = filaCelda.findViewById(R.id.imagen_producto)
        val nombre_producto: TextView = filaCelda.findViewById(R.id.nombre_producto)
        //val descripcion_producto: TextView = filaCelda.findViewById(R.id.descripcion_field)
        val precio_producto: TextView = filaCelda.findViewById(R.id.precio_field)

        val producto = almacenDatos[position]

        nombre_producto.text = "Nombre: " + producto.nombre
        //descripcion_producto.text = "Descripción: " + producto.descripcion
        precio_producto.text = "Precio: $" + producto.precio.toString()

        // Aplicar un estilo diferente si el producto está seleccionado
        if (elementosSeleccionados.contains(producto)) {
            // Aplicar el estilo para elementos seleccionados
            filaCelda.setBackgroundColor(Color.GREEN)
        } else {
            // Restaurar el estilo normal
            filaCelda.setBackgroundColor(Color.TRANSPARENT)
        }

        Picasso.get()
            .load(producto.url)
            .placeholder(R.mipmap.ic_launcher)
            .into(imagen)

        filaCelda.setOnClickListener {
            if (elementosSeleccionados.contains(producto)) {
                // El elemento ya está seleccionado, deselecciónalo
                elementosSeleccionados.remove(producto)
            } else {
                // El elemento no está seleccionado, selecciónalo
                elementosSeleccionados.add(producto)
            }
            notifyDataSetChanged()
        }

        return filaCelda
    }

    fun getItemsSeleccionados(): Set<Product> {
        return elementosSeleccionados
    }

    fun clearSelection() {
        elementosSeleccionados.clear()
        notifyDataSetChanged()
    }

    fun removeItemsSeleccionados() {
        almacenDatos.removeAll(elementosSeleccionados)
        elementosSeleccionados.clear()
        notifyDataSetChanged()
    }
}




package com.example.comishop

import android.content.Context
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ImageView
import android.widget.TextView
import com.squareup.picasso.Picasso

class AdaptadorDeCeldasHistorial(private val context: Context, private val almacenDatos: ArrayList<Venta>): BaseAdapter() {

    private val inflador: LayoutInflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
    private var elementoSeleccionado: Int = -1

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
        val filaCelda = inflador.inflate(R.layout.adaptador_celda_historial, parent, false)
        val imagen: ImageView = filaCelda.findViewById(R.id.image_historial)
        val nombreProducto: TextView = filaCelda.findViewById(R.id.nombre_p_historiak)
        val precioUnitario: TextView = filaCelda.findViewById(R.id.precio_u_historial)
        val precioDespacho: TextView = filaCelda.findViewById(R.id.valor_despacho_historial)

        val pVenta: TextView = filaCelda.findViewById(R.id.precio_venta_his)
        val cantidadVenta : TextView = filaCelda.findViewById(R.id.cantidad_field_historial)


        val productoVendido = almacenDatos[position]

        nombreProducto.text = "Nombre Producto: " + productoVendido.nombreProducto
        precioUnitario.text = "Precio Unitario: " + productoVendido.precioUnitario.toString()
        pVenta.text = "Precio Venta: " + productoVendido.precioVenta.toString()
        precioDespacho.text = "Precio Despacho: " + productoVendido.valorDespacho.toString()

        cantidadVenta.text ="Cantidad:" + productoVendido.cantidad

        val imageUrl = productoVendido.url

        if (!imageUrl.isNullOrEmpty()) {
            Picasso.get().load(imageUrl).into(imagen)
        } else {
            // Maneja el caso en el que la URL de la imagen está vacía o nula
            // Puedes establecer una imagen de marcador de posición o mostrar un mensaje de error, según lo prefieras.
        }

        if (position == elementoSeleccionado) {
            // Aplica un estilo diferente para el elemento seleccionado
            filaCelda.setBackgroundColor(Color.rgb(173, 216, 230))
        } else {
            // Restaura la apariencia predeterminada para los elementos no seleccionados
            filaCelda.setBackgroundColor(Color.TRANSPARENT)
        }

        filaCelda.setOnClickListener {
            elementoSeleccionado = position
            notifyDataSetChanged()
        }

        return filaCelda
    }

    fun getItemSelected(): List<Venta> {
        return if (elementoSeleccionado != -1) {
            listOf(almacenDatos[elementoSeleccionado])
        } else {
            emptyList()
        }
    }

    fun clearSelection() {
        elementoSeleccionado = -1
        notifyDataSetChanged()
    }

    fun removeItemSelected() {
        if (elementoSeleccionado != -1) {
            almacenDatos.removeAt(elementoSeleccionado)
            clearSelection()
        }
    }
}

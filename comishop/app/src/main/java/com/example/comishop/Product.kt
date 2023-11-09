package com.example.comishop

import android.os.Parcelable
import android.text.Editable
import androidx.room.Entity
import kotlinx.parcelize.Parcelize

data class Productos (
    val products: List<Product>,
    val total: Long,
    val skip: Long,
    val limit: Long
)
@Parcelize
@Entity(tableName = "producto")

data class Product (
    var id: Int = 0,
    var nombre: String = "",
    var descripcion: String = "",
    var precio: Int = 0,
    var url : String=""
): Parcelable

@Parcelize
@Entity(tableName = "venta")
data class Venta(
    var nombreProducto: String = "",
    var precioUnitario: Int = 0,
    var valorDespacho: Int = 0,
    var direccion: String = "",
    var telefono: String = "",
    var nombreCliente: String = "",
    var precioVenta: Int = 0,
    var url : String=""

): Parcelable
package com.tecsup.crud_producto.data.local

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "producto",
    indices = [Index(value = ["codigoBarras"], unique = true)]
)
data class Producto(
    @PrimaryKey(autoGenerate = true)
    val idProducto: Int = 0,
    val nombre: String,
    val categoria: String? = null,
    val precio: Double = 0.0,
    val stock: Int = 0,
    @ColumnInfo(name = "codigoBarras")
    val codigoBarras: String,
    val descripcion: String? = null
)

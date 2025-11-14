package com.tecsup.crud_producto.data.repository

import com.tecsup.crud_producto.data.local.Producto
import com.tecsup.crud_producto.data.local.ProductoDao

class ProductoRepository(private val dao: ProductoDao) {

    fun getAll() = dao.getAll()

    fun search(query: String) = dao.search(query)

    suspend fun insert(producto: Producto): Result<Long> {
        if (producto.precio <= 0) return Result.failure(Exception("El precio debe ser mayor a 0"))
        if (producto.stock < 0) return Result.failure(Exception("El stock no puede ser negativo"))
        if (producto.codigoBarras.length != 13) return Result.failure(Exception("El código de barras debe tener 13 dígitos"))

        val exist = dao.getByCodigo(producto.codigoBarras)
        if (exist != null) return Result.failure(Exception("Código de barras duplicado"))

        return try {
            Result.success(dao.insert(producto))
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun update(producto: Producto): Result<Unit> {
        if (producto.precio <= 0) return Result.failure(Exception("El precio debe ser mayor a 0"))
        if (producto.stock < 0) return Result.failure(Exception("El stock no puede ser negativo"))
        if (producto.codigoBarras.length != 13) return Result.failure(Exception("El código debe tener 13 dígitos"))

        val exist = dao.getByCodigo(producto.codigoBarras)
        if (exist != null && exist.idProducto != producto.idProducto)
            return Result.failure(Exception("Este código de barras ya existe"))

        return try {
            dao.update(producto)
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun delete(producto: Producto) = dao.delete(producto)
}

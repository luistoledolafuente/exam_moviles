package com.tecsup.crud_producto.data.local

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface ProductoDao {

    @Query("SELECT * FROM producto ORDER BY nombre ASC")
    fun getAll(): Flow<List<Producto>>

    @Query("SELECT * FROM producto WHERE nombre LIKE '%' || :q || '%' OR categoria LIKE '%' || :q || '%' ORDER BY nombre ASC")
    fun search(q: String): Flow<List<Producto>>

    @Query("SELECT * FROM producto WHERE codigoBarras = :codigo LIMIT 1")
    suspend fun getByCodigo(codigo: String): Producto?

    @Insert(onConflict = OnConflictStrategy.ABORT)
    suspend fun insert(producto: Producto): Long

    @Update
    suspend fun update(producto: Producto)

    @Delete
    suspend fun delete(producto: Producto)
}

package com.tecsup.crud_producto.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tecsup.crud_producto.data.local.Producto
import com.tecsup.crud_producto.data.repository.ProductoRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class ProductoViewModel(private val repo: ProductoRepository) : ViewModel() {

    // Mensajes para mostrar feedback
    private val _message = MutableStateFlow("")
    val message: StateFlow<String> = _message.asStateFlow()

    // Query para la búsqueda
    private val _searchQuery = MutableStateFlow("")
    val searchQuery = _searchQuery.asStateFlow()

    // Lista de productos que reacciona a la búsqueda
    val productos = _searchQuery.flatMapLatest { query ->
        if (query.isEmpty()) {
            repo.getAll()
        } else {
            repo.search(query)
        }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    // Función para actualizar el query de búsqueda
    fun onSearchQueryChange(newQuery: String) {
        _searchQuery.value = newQuery
    }

    // INSERTAR (Corregido con validación)
    fun insert(producto: Producto, onSuccess: () -> Unit) {
        viewModelScope.launch {
            val result = repo.insert(producto) // Capturamos el resultado
            if (result.isSuccess) {
                _message.value = "Producto registrado correctamente"
                onSuccess() // Solo navegamos si tiene éxito
            } else {
                // Si falla, mostramos el mensaje de error del repositorio
                _message.value = result.exceptionOrNull()?.message ?: "Error al registrar"
            }
        }
    }

    // ACTUALIZAR (Corregido con validación)
    fun update(producto: Producto, onSuccess: () -> Unit) {
        viewModelScope.launch {
            val result = repo.update(producto) // Capturamos el resultado
            if (result.isSuccess) {
                _message.value = "Producto actualizado correctamente"
                onSuccess() // Solo navegamos si tiene éxito
            } else {
                // Si falla, mostramos el mensaje de error del repositorio
                _message.value = result.exceptionOrNull()?.message ?: "Error al actualizar"
            }
        }
    }

    // ELIMINAR
    fun delete(producto: Producto) {
        viewModelScope.launch {
            repo.delete(producto)
            _message.value = "Producto eliminado"
        }
    }

    // Función para limpiar el mensaje después de mostrarlo en el Snackbar
    fun clearMessage() {
        _message.value = ""
    }
}
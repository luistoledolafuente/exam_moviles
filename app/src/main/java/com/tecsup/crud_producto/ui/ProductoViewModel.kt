package com.tecsup.crud_producto.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tecsup.crud_producto.data.local.Producto
import com.tecsup.crud_producto.data.repository.ProductoRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class ProductoViewModel(private val repo: ProductoRepository) : ViewModel() {

    // Mensajes para mostrar feedback
    private val _message = MutableStateFlow("")
    val message: StateFlow<String> = _message

    // Lista completa de productos (NO usamos search)
    val productos = repo.getAll()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(), emptyList())

    // INSERTAR
    fun insert(producto: Producto, onSuccess: () -> Unit) {
        viewModelScope.launch {
            repo.insert(producto)
            _message.value = "Producto registrado correctamente"
            onSuccess()
        }
    }

    // ACTUALIZAR
    fun update(producto: Producto, onSuccess: () -> Unit) {
        viewModelScope.launch {
            repo.update(producto)
            _message.value = "Producto actualizado correctamente"
            onSuccess()
        }
    }

    // ELIMINAR
    fun delete(producto: Producto) {
        viewModelScope.launch {
            repo.delete(producto)
            _message.value = "Producto eliminado"
        }
    }
}

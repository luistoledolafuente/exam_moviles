package com.tecsup.crud_producto.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.tecsup.crud_producto.data.local.Producto
import com.tecsup.crud_producto.ui.ProductoViewModel

@Composable
fun ListScreen(
    vm: ProductoViewModel,
    onEdit: (Int) -> Unit,
    onAdd: () -> Unit
) {
    // Colectamos la lista de productos (que ahora reacciona a la búsqueda)
    val productos by vm.productos.collectAsState()
    // Colectamos el query de búsqueda
    val searchQuery by vm.searchQuery.collectAsState()

    // --- Estados para el Diálogo de Confirmación ---
    var showDeleteDialog by remember { mutableStateOf(false) }
    var productToDelete by remember { mutableStateOf<Producto?>(null) }
    // ------------------------------------------------

    Column(modifier = Modifier.padding(16.dp)) {

        Button(onClick = onAdd) {
            Text("Nuevo Producto")
        }

        Spacer(modifier = Modifier.height(16.dp))

        // --- Campo de Búsqueda ---
        OutlinedTextField(
            value = searchQuery,
            onValueChange = { vm.onSearchQueryChange(it) },
            label = { Text("Buscar por nombre o categoría") },
            modifier = Modifier.fillMaxWidth()
        )
        // -------------------------

        Spacer(modifier = Modifier.height(16.dp))

        // Lista de productos
        productos.forEach { p ->
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 6.dp)
            ) {
                Column(Modifier.padding(12.dp)) {
                    Text("Nombre: ${p.nombre}")
                    Text("Descripción: ${p.descripcion}")
                    Text("Precio: ${p.precio}")
                    Text("Stock: ${p.stock}")
                    Text("Código: ${p.codigoBarras}")

                    Row {
                        Button(
                            onClick = { onEdit(p.idProducto) },
                            modifier = Modifier.padding(end = 8.dp)
                        ) {
                            Text("Editar")
                        }

                        // El botón ahora muestra el diálogo
                        Button(onClick = {
                            productToDelete = p
                            showDeleteDialog = true
                        }) {
                            Text("Eliminar")
                        }
                    }
                }
            }
        }
    }

    // --- Diálogo de Confirmación de Eliminación ---
    if (showDeleteDialog && productToDelete != null) {
        AlertDialog(
            onDismissRequest = {
                showDeleteDialog = false
                productToDelete = null
            },
            title = { Text("Confirmar Eliminación") },
            text = { Text("¿Estás seguro de que deseas eliminar el producto '${productToDelete!!.nombre}'?") },
            confirmButton = {
                Button(
                    onClick = {
                        vm.delete(productToDelete!!)
                        showDeleteDialog = false
                        productToDelete = null
                    }
                ) {
                    Text("Eliminar")
                }
            },
            dismissButton = {
                Button(
                    onClick = {
                        showDeleteDialog = false
                        productToDelete = null
                    }
                ) {
                    Text("Cancelar")
                }
            }
        )
    }
    // ------------------------------------------------
}
package com.tecsup.crud_producto.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.tecsup.crud_producto.ui.ProductoViewModel

@Composable
fun ListScreen(
    vm: ProductoViewModel,
    onEdit: (Int) -> Unit,
    onAdd: () -> Unit
) {

    val productos by vm.productos.collectAsState()

    Column(modifier = Modifier.padding(16.dp)) {

        Button(onClick = onAdd) {
            Text("Nuevo Producto")
        }

        Spacer(modifier = Modifier.height(16.dp))

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

                    Row {
                        Button(
                            onClick = { onEdit(p.idProducto) },
                            modifier = Modifier.padding(end = 8.dp)
                        ) {
                            Text("Editar")
                        }

                        Button(onClick = { vm.delete(p) }) {
                            Text("Eliminar")
                        }
                    }
                }
            }
        }
    }
}

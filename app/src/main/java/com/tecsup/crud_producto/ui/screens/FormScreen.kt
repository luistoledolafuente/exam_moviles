package com.tecsup.crud_producto.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.tecsup.crud_producto.data.local.Producto
import com.tecsup.crud_producto.ui.ProductoViewModel
import com.tecsup.crud_producto.ui.components.InputField

@Composable
fun FormScreen(
    vm: ProductoViewModel,
    producto: Producto?,
    onDone: () -> Unit
) {

    var nombre by remember { mutableStateOf(producto?.nombre ?: "") }
    var categoria by remember { mutableStateOf(producto?.categoria ?: "") }
    var precio by remember { mutableStateOf(producto?.precio?.toString() ?: "") }
    var stock by remember { mutableStateOf(producto?.stock?.toString() ?: "") }
    var codigoBarras by remember { mutableStateOf(producto?.codigoBarras ?: "") }
    var descripcion by remember { mutableStateOf(producto?.descripcion ?: "") }

    Column(modifier = Modifier.padding(20.dp)) {

        InputField(
            label = "Nombre",
            value = nombre,
            onValueChange = { nombre = it }
        )

        InputField(
            label = "Categoría",
            value = categoria,
            onValueChange = { categoria = it }
        )

        InputField(
            label = "Precio",
            value = precio,
            onValueChange = { precio = it }
        )

        InputField(
            label = "Stock",
            value = stock,
            onValueChange = { stock = it }
        )

        InputField(
            label = "Código de Barras",
            value = codigoBarras,
            onValueChange = { codigoBarras = it }
        )

        InputField(
            label = "Descripción",
            value = descripcion,
            onValueChange = { descripcion = it }
        )

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = {

                val p = Producto(
                    idProducto = producto?.idProducto ?: 0,
                    nombre = nombre,
                    categoria = categoria,
                    precio = precio.toDoubleOrNull() ?: 0.0,
                    stock = stock.toIntOrNull() ?: 0,
                    codigoBarras = codigoBarras,
                    descripcion = descripcion
                )

                if (producto == null) vm.insert(p, onDone)
                else vm.update(p, onDone)
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(if (producto == null) "Guardar" else "Actualizar")
        }
    }
}

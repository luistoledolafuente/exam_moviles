package com.tecsup.crud_producto

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.tecsup.crud_producto.data.local.AppDatabase
import com.tecsup.crud_producto.data.repository.ProductoRepository
import com.tecsup.crud_producto.ui.ProductoViewModel
import com.tecsup.crud_producto.ui.screens.FormScreen
import com.tecsup.crud_producto.ui.screens.ListScreen
import kotlinx.coroutines.flow.collectLatest

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val db = AppDatabase.getInstance(this)
        val repo = ProductoRepository(db.productoDao())

        val factory = object : ViewModelProvider.Factory {
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                @Suppress("UNCHECKED_CAST")
                return ProductoViewModel(repo) as T
            }
        }

        setContent {
            // No necesitamos pasar el factory aquí,
            // lo pasaremos directamente a AppNavigation
            AppNavigation(factory)
        }
    }
}

@Composable
fun AppNavigation(factory: ViewModelProvider.Factory) {
    val navController = rememberNavController()
    // Creamos una única instancia del ViewModel aquí
    val vm: ProductoViewModel = viewModel(factory = factory)

    // Estado para el Snackbar
    val snackbarHostState = remember { SnackbarHostState() }
    // Colectamos el mensaje del ViewModel
    val message by vm.message.collectAsState()

    // Efecto para mostrar el Snackbar cuando el mensaje cambie
    LaunchedEffect(message) {
        if (message.isNotEmpty()) {
            snackbarHostState.showSnackbar(message)
            vm.clearMessage() // Limpiamos el mensaje después de mostrarlo
        }
    }

    // Envolvemos la navegación en un Scaffold para tener el SnackbarHost
    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) }
    ) { paddingValues ->

        NavHost(
            navController = navController,
            startDestination = "list",
            // Aplicamos el padding del Scaffold
            modifier = Modifier.padding(paddingValues)
        ) {

            // LISTA
            composable("list") {
                ListScreen(
                    vm = vm, // Usamos la instancia única del VM
                    onEdit = { id -> navController.navigate("form/$id") },
                    onAdd = { navController.navigate("form") }
                )
            }

            // FORMULARIO (CREAR)
            composable("form") {
                FormScreen(
                    vm = vm, // Usamos la instancia única del VM
                    producto = null,
                    onDone = { navController.popBackStack() }
                )
            }

            // FORMULARIO (EDITAR)
            composable("form/{id}") { backStackEntry ->
                val id = backStackEntry.arguments?.getString("id")?.toIntOrNull()
                // Buscamos el producto en la lista ya colectada
                val producto = vm.productos.collectAsState().value.firstOrNull { it.idProducto == id }

                FormScreen(
                    vm = vm, // Usamos la instancia única del VM
                    producto = producto,
                    onDone = { navController.popBackStack() }
                )
            }
        }
    }
}
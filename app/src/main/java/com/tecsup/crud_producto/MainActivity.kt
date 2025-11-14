package com.tecsup.crud_producto

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.Composable
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
import androidx.compose.runtime.collectAsState

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
            AppNavigation(factory)
        }
    }
}

@Composable
fun AppNavigation(factory: ViewModelProvider.Factory) {
    val navController = rememberNavController()
    val vm: ProductoViewModel = viewModel(factory = factory)

    NavHost(
        navController = navController,
        startDestination = "list"
    ) {

        // LISTA
        composable("list") {
            ListScreen(
                vm = vm,
                onEdit = { id -> navController.navigate("form/$id") },
                onAdd = { navController.navigate("form") }
            )
        }

        // FORMULARIO (CREAR)
        composable("form") {
            FormScreen(
                vm = vm,
                producto = null,
                onDone = { navController.popBackStack() }
            )
        }

        // FORMULARIO (EDITAR)
        composable("form/{id}") { backStackEntry ->
            val id = backStackEntry.arguments?.getString("id")?.toIntOrNull()
            val producto = vm.productos.collectAsState().value.firstOrNull { it.idProducto == id }

            FormScreen(
                vm = vm,
                producto = producto,
                onDone = { navController.popBackStack() }
            )
        }
    }
}

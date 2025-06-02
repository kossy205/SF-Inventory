package com.kosiso.sfinventory.ui

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.kosiso.sfinventory.R
import com.kosiso.sfinventory.utils.enum_classes.MainAppNav
import com.kosiso.sfinventory.utils.enum_classes.RootNav
import com.kosiso.sfinventory.ui.navigation.BottomNavItem
import com.kosiso.sfinventory.ui.screens.AddProductScreen
import com.kosiso.sfinventory.ui.screens.DashboardScreen
import com.kosiso.sfinventory.ui.screens.ProductDetailsScreen
import com.kosiso.sfinventory.ui.screens.ProductsScreen
import com.kosiso.sfinventory.ui.screens.ReportScreen
import com.kosiso.sfinventory.ui.screens.EditProductScreen
import com.kosiso.sfinventory.ui.theme.Black
import com.kosiso.sfinventory.ui.theme.Pink
import com.kosiso.sfinventory.ui.theme.White
import com.kosiso.sfinventory.ui.viewmodel.MainViewModel
import dagger.hilt.android.AndroidEntryPoint
import java.util.UUID


@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    lateinit var mainViewModel: MainViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        enableEdgeToEdge()
        setContent {
            mainViewModel = ViewModelProvider(this)[MainViewModel::class.java]
            val rootNavController = rememberNavController()
            RootNavigation(rootNavController, mainViewModel)
        }
    }

    @Composable
    fun RootNavigation(rootNavController: NavHostController, mainViewModel: MainViewModel){
        NavHost(
            navController = rootNavController,
            startDestination = RootNav.MAIN_APP.route
        ) {

            composable(RootNav.MAIN_APP.route) {
                MainApp(rootNavController, mainViewModel)
            }

            composable(
                route = "${RootNav.PRODUCT_DETAILS.route}/{productId}",
                arguments = listOf(navArgument("productId") { type = NavType.StringType })
            ) { backStackEntry ->
                val productId = backStackEntry.arguments?.getString("productId")
                Log.i("product id 1", "product id: $productId")
                ProductDetailsScreen(
                    productId = productId!!,
                    mainViewModel = mainViewModel,
                    onBackClick = { rootNavController.popBackStack() },
                    onNavigateToEditProductScreen = {productId->
                        rootNavController.navigate("${RootNav.EDIT_PRODUCT.route}/${productId}")
                    }
                )
            }
            composable(RootNav.ADD_PRODUCT.route) {
                AddProductScreen(
                    mainViewModel,
                    onBackClick = { rootNavController.popBackStack() }
                )
            }
            composable(
                route = "${RootNav.EDIT_PRODUCT.route}/{productId}",
                arguments = listOf(navArgument("productId") { type = NavType.StringType })
            ) {backStackEntry ->
                val productId = backStackEntry.arguments?.getString("productId")
                Log.i("product id 2", "product id: $productId")
                EditProductScreen(
                    productId = productId!!,
                    mainViewModel,
                    onBackClick = { rootNavController.popBackStack() }
                )
            }
        }
    }

    @Composable
    fun MainApp(rootNavController: NavHostController, mainViewModel: MainViewModel){

        val mainAppNavController = rememberNavController()

        val bottomNavItems = listOf<BottomNavItem>(
            BottomNavItem(
                id = UUID.randomUUID().toString(),
                name = "Dashboard",
                route = MainAppNav.DASHBOARD.route,
                icon = R.drawable.ic_dashboard
            ),
            BottomNavItem(
                id = UUID.randomUUID().toString(),
                name = "Products",
                route = MainAppNav.PRODUCTS.route,
                icon = R.drawable.ic_arrange1
            ),
            BottomNavItem(
                id = UUID.randomUUID().toString(),
                name = "Report",
                route = MainAppNav.REPORT.route,
                icon = R.drawable.ic_chart
            )
        )

        Scaffold(
            bottomBar = {
                BottomNavigationBar(
                    navItems = bottomNavItems,
                    navController = mainAppNavController,
                    onItemClick = {
                        if (mainAppNavController.currentDestination?.route != it.route) {
                            mainAppNavController.navigate(it.route) {
                                // Pop all entries above DASHBOARD, keeping DASHBOARD
                                popUpTo(MainAppNav.DASHBOARD.route) { inclusive = true }
                                launchSingleTop = true // doesn't add a route to back stack if route is reselected.
                            }
                        }
                    }
                )
            }
        ){
            Navigation(
                mainAppNavController= mainAppNavController,
                rootNavController = rootNavController,
                mainViewModel
            )
        }
    }

    @Composable
    fun Navigation(
        mainAppNavController: NavHostController,
        rootNavController: NavHostController,
        mainViewModel: MainViewModel){

        NavHost(navController = mainAppNavController, startDestination = MainAppNav.DASHBOARD.route){
            composable(MainAppNav.DASHBOARD.route){
                DashboardScreen(mainViewModel)
                Log.i("Dashboard screen Clicked", "Dashboard screen Clicked")
            }
            composable(MainAppNav.PRODUCTS.route){
                ProductsScreen(
                    mainViewModel,
                    onNavigateToDetailsScreen = {product->
                        rootNavController.navigate("${RootNav.PRODUCT_DETAILS.route}/${product.id}")
                    },
                    onNavigateToAddProductScreen = {
                        rootNavController.navigate(RootNav.ADD_PRODUCT.route)
                    }
                )
                Log.i("Products screen Clicked", "Products screen Clicked")
            }
            composable(MainAppNav.REPORT.route){
                ReportScreen(mainViewModel)
                Log.i("Report screen Clicked", "Report screen Clicked")
            }
        }
    }


    @Composable
    fun BottomNavigationBar(
        navItems: List<BottomNavItem>,
        navController: NavController,
        modifier: Modifier = Modifier,
        onItemClick: (BottomNavItem) -> Unit
    ){
        val backStackEntry = navController.currentBackStackEntryAsState()
        NavigationBar(
            modifier = modifier.height(60.dp),
            containerColor = White,
            tonalElevation = 5.dp
        ) {
            navItems.forEach{navItem ->

                val selected = navItem.route == backStackEntry.value?.destination?.route
                NavigationBarItem(
                    modifier = Modifier.padding(top = 5.dp),
                    selected = selected,
                    onClick = { onItemClick(navItem) },
                    colors = NavigationBarItemDefaults.colors(
                        selectedIconColor = White,
                        unselectedIconColor = Black.copy(alpha = 0.8f),
                        indicatorColor = Pink
                    ),
                    icon = {
                        BottomNavIconStyle(
                            navItem,
                            selected
                        )
                    },
                    alwaysShowLabel = true,
                    label = {
                        Text(
                            text = navItem.name,
                            textAlign = TextAlign.Center,
                            fontSize = 10.sp
                        )
                    }
                )
            }
        }
    }

    @Composable
    fun BottomNavIconStyle(
        navItem: BottomNavItem,
        selected: Boolean
    ){
        Column(
            horizontalAlignment = Alignment.CenterHorizontally
        ){
            Box{
                Icon(
                    painter = painterResource(navItem.icon),
                    contentDescription = navItem.name,
                    modifier = Modifier.size(24.dp)
                )
            }
            if(selected){
            }
        }
    }



}





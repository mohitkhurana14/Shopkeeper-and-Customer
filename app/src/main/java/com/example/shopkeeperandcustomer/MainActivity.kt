package com.example.shopkeeperandcustomer

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.shopkeeperandcustomer.ui.theme.ShopkeeperCustomerTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ShopkeeperCustomerTheme(useDarkTheme = false) {
                Surface(color = MaterialTheme.colorScheme.background) {
                    ShopkeeperCustomerApp()
                }
            }
        }
    }
}

@Composable
fun ShopkeeperCustomerApp() {
    val navController = rememberNavController()
    val viewModel: OrderQueueViewModel = viewModel()

    NavHost(
        navController = navController,
        startDestination = "home"
    ) {
        composable("home") {
            HomeScreen(navController, viewModel)
        }
        composable("shopkeeper") {
            ShopkeeperScreen(navController, viewModel)
        }
        composable("customer") {
            CustomerScreen(navController, viewModel)
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(navController: NavHostController, viewModel: OrderQueueViewModel) {
    Scaffold(
        topBar = {
            TopAppBar(title = { Text("Online Orders") })
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .padding(20.dp)
                .fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(20.dp)
        ) {
            Text(
                text = viewModel.shopName.value,
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold
            )
            Text(text = "Shop username: ${viewModel.shopUsername.value}")
            Text(
                text = if (viewModel.isServing.value) "Currently serving orders" else "Currently unavailable",
                color = MaterialTheme.colorScheme.secondary,
                fontWeight = FontWeight.SemiBold
            )

            Button(
                onClick = { navController.navigate("shopkeeper") },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Shopkeeper View")
            }
            Button(
                onClick = { navController.navigate("customer") },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Customer View")
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ShopkeeperScreen(navController: NavHostController, viewModel: OrderQueueViewModel) {
    var shopNameInput by remember { mutableStateOf(viewModel.shopName.value) }
    var usernameInput by remember { mutableStateOf(viewModel.shopUsername.value) }

    Scaffold(
        topBar = {
            TopAppBar(title = { Text("Shopkeeper Dashboard") })
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .padding(20.dp)
                .fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            OutlinedTextField(
                value = shopNameInput,
                onValueChange = { shopNameInput = it },
                label = { Text("Shop name") },
                modifier = Modifier.fillMaxWidth()
            )
            OutlinedTextField(
                value = usernameInput,
                onValueChange = { usernameInput = it },
                label = { Text("Public username") },
                modifier = Modifier.fillMaxWidth()
            )
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Button(
                    onClick = { viewModel.updateShopInfo(shopNameInput, usernameInput) },
                    modifier = Modifier.weight(1f)
                ) {
                    Text("Save")
                }
                Button(
                    onClick = { viewModel.toggleServing() },
                    modifier = Modifier.weight(1f)
                ) {
                    Text(if (viewModel.isServing.value) "Pause" else "Resume")
                }
            }
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp)
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Text("Queue overview", style = MaterialTheme.typography.titleMedium)
                    Text("Orders waiting: ${viewModel.orders.size}")
                    Text("Estimated wait: ${viewModel.estimatedWaitMinutes()} minutes")
                }
            }
            Button(
                onClick = { viewModel.serveNext() },
                modifier = Modifier.fillMaxWidth(),
                enabled = viewModel.orders.isNotEmpty()
            ) {
                Text("Serve next order")
            }
            Text("Current queue", style = MaterialTheme.typography.titleMedium)
            QueueList(orders = viewModel.orders)
            Spacer(modifier = Modifier.height(8.dp))
            Button(
                onClick = { navController.navigate("home") },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Back to home")
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CustomerScreen(navController: NavHostController, viewModel: OrderQueueViewModel) {
    var customerName by remember { mutableStateOf("") }
    var orderDetails by remember { mutableStateOf("") }

    Scaffold(
        topBar = {
            TopAppBar(title = { Text("Place an Order") })
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .padding(20.dp)
                .fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp)
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    Text(text = viewModel.shopName.value, style = MaterialTheme.typography.titleMedium)
                    Text(text = "${viewModel.shopUsername.value} · ${availabilityLabel(viewModel.isServing.value)}")
                    Text(text = "Estimated wait: ${viewModel.estimatedWaitMinutes()} minutes")
                }
            }
            OutlinedTextField(
                value = customerName,
                onValueChange = { customerName = it },
                label = { Text("Your name") },
                modifier = Modifier.fillMaxWidth()
            )
            OutlinedTextField(
                value = orderDetails,
                onValueChange = { orderDetails = it },
                label = { Text("Order details") },
                modifier = Modifier.fillMaxWidth()
            )
            Button(
                onClick = {
                    viewModel.addOrder(customerName, orderDetails)
                    customerName = ""
                    orderDetails = ""
                },
                modifier = Modifier.fillMaxWidth(),
                enabled = viewModel.isServing.value
            ) {
                Text("Send order")
            }
            Text("Live queue", style = MaterialTheme.typography.titleMedium)
            QueueList(orders = viewModel.orders)
            Spacer(modifier = Modifier.height(8.dp))
            Button(
                onClick = { navController.navigate("home") },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Back to home")
            }
        }
    }
}

@Composable
fun QueueList(orders: List<Order>) {
    if (orders.isEmpty()) {
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(12.dp)
        ) {
            Text(
                text = "No orders in the queue yet.",
                modifier = Modifier.padding(16.dp)
            )
        }
        return
    }
    LazyColumn(
        modifier = Modifier
            .fillMaxWidth()
            .background(MaterialTheme.colorScheme.surface, RoundedCornerShape(12.dp))
            .padding(12.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        items(orders) { order ->
            Column(
                modifier = Modifier
                    .fillMaxWidth()
            ) {
                Text(order.customerName, fontWeight = FontWeight.Bold)
                Text(order.details)
            }
        }
    }
}

@Composable
fun availabilityLabel(isServing: Boolean): String {
    return if (isServing) "Serving now" else "Temporarily paused"
}

package com.example.shopkeeperandcustomer

import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import java.util.UUID

private const val MINUTES_PER_ORDER = 5

data class Order(
    val id: String = UUID.randomUUID().toString(),
    val customerName: String,
    val details: String,
    val placedAt: Long = System.currentTimeMillis()
)

class OrderQueueViewModel : ViewModel() {
    val shopName = mutableStateOf("GreenLeaf Mart")
    val shopUsername = mutableStateOf("@greenleaf")
    val isServing = mutableStateOf(true)
    val orders = mutableStateListOf<Order>()

    fun updateShopInfo(name: String, username: String) {
        if (name.isNotBlank()) {
            shopName.value = name.trim()
        }
        if (username.isNotBlank()) {
            shopUsername.value = username.trim()
        }
    }

    fun toggleServing() {
        isServing.value = !isServing.value
    }

    fun addOrder(customerName: String, details: String) {
        if (customerName.isBlank() || details.isBlank()) return
        orders.add(
            Order(
                customerName = customerName.trim(),
                details = details.trim()
            )
        )
    }

    fun serveNext() {
        if (orders.isNotEmpty()) {
            orders.removeAt(0)
        }
    }

    fun estimatedWaitMinutes(): Int {
        return orders.size * MINUTES_PER_ORDER
    }
}

package com.example.shopkeeperandcustomer.ui.theme

import androidx.compose.material3.ColorScheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val LightColors = lightColorScheme(
    primary = Color(0xFF2D6A4F),
    onPrimary = Color(0xFFFFFFFF),
    primaryContainer = Color(0xFFB7E4C7),
    onPrimaryContainer = Color(0xFF081C15),
    secondary = Color(0xFF1B4332),
    onSecondary = Color(0xFFFFFFFF),
    background = Color(0xFFF8FAF8),
    onBackground = Color(0xFF1B1B1B),
    surface = Color(0xFFFFFFFF),
    onSurface = Color(0xFF1B1B1B)
)

private val DarkColors = darkColorScheme(
    primary = Color(0xFF74C69D),
    onPrimary = Color(0xFF003020),
    primaryContainer = Color(0xFF2D6A4F),
    onPrimaryContainer = Color(0xFFE6FFF3),
    secondary = Color(0xFF95D5B2),
    onSecondary = Color(0xFF003020),
    background = Color(0xFF0F1412),
    onBackground = Color(0xFFE5E5E5),
    surface = Color(0xFF1B1F1D),
    onSurface = Color(0xFFE5E5E5)
)

@Composable
fun ShopkeeperCustomerTheme(
    useDarkTheme: Boolean,
    content: @Composable () -> Unit
) {
    val colorScheme: ColorScheme = if (useDarkTheme) DarkColors else LightColors

    MaterialTheme(
        colorScheme = colorScheme,
        content = content
    )
}

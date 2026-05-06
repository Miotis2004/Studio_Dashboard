package com.ronniej.studiodashboard

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.ronniej.studiodashboard.ui.widgets.CalendarWidget
import com.ronniej.studiodashboard.ui.widgets.ClockWidget
import com.ronniej.studiodashboard.ui.widgets.WeatherWidget

@Composable
fun DashboardScreen() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(8.dp),
        verticalArrangement = Arrangement.SpaceEvenly
    ) {
        Row(
            modifier = Modifier.weight(1f),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            ClockWidget(modifier = Modifier.weight(1f))
            WeatherWidget(modifier = Modifier.weight(1f))
        }
        Row(
            modifier = Modifier.weight(1f),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            CalendarWidget(modifier = Modifier.weight(1f))
            // Future slot widget
            androidx.compose.foundation.layout.Spacer(modifier = Modifier.weight(1f))
        }
    }
}

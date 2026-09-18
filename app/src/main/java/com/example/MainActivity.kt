package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.model.BadgeTier
import com.example.ui.AppTab
import com.example.ui.HealthPulseViewModel
import com.example.ui.screens.DiscoveryScreen
import com.example.ui.screens.MarketplaceScreen
import com.example.ui.screens.MyBadgeScreen
import com.example.ui.screens.ZkvGatewayScreen
import com.example.ui.theme.*

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            HealthPulseTheme {
                HealthPulseApp()
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HealthPulseApp(
    viewModel: HealthPulseViewModel = viewModel()
) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            TopAppBar(
                title = {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Surface(
                            shape = CircleShape,
                            color = WarmCoral,
                            modifier = Modifier.size(36.dp)
                        ) {
                            Box(contentAlignment = Alignment.Center) {
                                Icon(
                                    Icons.Filled.Favorite,
                                    contentDescription = "Safe Date Logo",
                                    tint = Color.White,
                                    modifier = Modifier.size(20.dp)
                                )
                            }
                        }
                        Spacer(modifier = Modifier.width(10.dp))
                        Column {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Text(
                                    text = "Safe",
                                    style = MaterialTheme.typography.titleMedium,
                                    fontWeight = FontWeight.Black,
                                    color = DeepTealDark
                                )
                                Spacer(modifier = Modifier.width(4.dp))
                                Text(
                                    text = "Date",
                                    style = MaterialTheme.typography.titleMedium,
                                    fontWeight = FontWeight.Black,
                                    color = WarmCoral
                                )
                            }
                            Text(
                                text = "Hospital Verified Health x ZKV",
                                style = MaterialTheme.typography.bodySmall.copy(fontSize = 10.sp),
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }
                },
                actions = {
                    // Current User Tier Chip
                    Surface(
                        shape = RoundedCornerShape(16.dp),
                        color = state.myTier.color.copy(alpha = 0.15f),
                        border = BorderStroke(1.dp, state.myTier.color.copy(alpha = 0.4f)),
                        modifier = Modifier
                            .padding(end = 12.dp)
                            .testTag("my_tier_chip")
                    ) {
                        Row(
                            modifier = Modifier.padding(horizontal = 10.dp, vertical = 5.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(state.myTier.emoji, fontSize = 14.sp)
                            Spacer(modifier = Modifier.width(5.dp))
                            Text(
                                text = state.myTier.shortName,
                                style = MaterialTheme.typography.labelMedium,
                                fontWeight = FontWeight.Bold,
                                color = state.myTier.color
                            )
                        }
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.surface
                )
            )
        },
        bottomBar = {
            NavigationBar(
                containerColor = MaterialTheme.colorScheme.surface,
                tonalElevation = 8.dp
            ) {
                NavigationBarItem(
                    selected = state.currentTab == AppTab.DISCOVERY,
                    onClick = { viewModel.setTab(AppTab.DISCOVERY) },
                    icon = {
                        Icon(
                            if (state.currentTab == AppTab.DISCOVERY) Icons.Filled.Favorite else Icons.Outlined.FavoriteBorder,
                            contentDescription = "Discovery"
                        )
                    },
                    label = { Text(AppTab.DISCOVERY.title, fontSize = 11.sp) },
                    colors = NavigationBarItemDefaults.colors(
                        selectedIconColor = WarmCoral,
                        selectedTextColor = WarmCoral,
                        indicatorColor = WarmCoralLight
                    ),
                    modifier = Modifier.testTag("tab_discovery")
                )

                NavigationBarItem(
                    selected = state.currentTab == AppTab.MARKETPLACE,
                    onClick = { viewModel.setTab(AppTab.MARKETPLACE) },
                    icon = {
                        Icon(
                            if (state.currentTab == AppTab.MARKETPLACE) Icons.Filled.LocalHospital else Icons.Outlined.LocalHospital,
                            contentDescription = "E-Vouchers"
                        )
                    },
                    label = { Text(AppTab.MARKETPLACE.title, fontSize = 11.sp) },
                    colors = NavigationBarItemDefaults.colors(
                        selectedIconColor = DeepTealPrimary,
                        selectedTextColor = DeepTealPrimary,
                        indicatorColor = DeepTealLight
                    ),
                    modifier = Modifier.testTag("tab_marketplace")
                )

                NavigationBarItem(
                    selected = state.currentTab == AppTab.MY_BADGE,
                    onClick = { viewModel.setTab(AppTab.MY_BADGE) },
                    icon = {
                        Icon(
                            if (state.currentTab == AppTab.MY_BADGE) Icons.Filled.VerifiedUser else Icons.Outlined.VerifiedUser,
                            contentDescription = "My Badge"
                        )
                    },
                    label = { Text(AppTab.MY_BADGE.title, fontSize = 11.sp) },
                    colors = NavigationBarItemDefaults.colors(
                        selectedIconColor = DeepTealPrimary,
                        selectedTextColor = DeepTealPrimary,
                        indicatorColor = DeepTealLight
                    ),
                    modifier = Modifier.testTag("tab_my_badge")
                )

                NavigationBarItem(
                    selected = state.currentTab == AppTab.ZKV_GATEWAY,
                    onClick = { viewModel.setTab(AppTab.ZKV_GATEWAY) },
                    icon = {
                        Icon(
                            if (state.currentTab == AppTab.ZKV_GATEWAY) Icons.Filled.Security else Icons.Outlined.Security,
                            contentDescription = "ZKV & FHIR"
                        )
                    },
                    label = { Text(AppTab.ZKV_GATEWAY.title, fontSize = 11.sp) },
                    colors = NavigationBarItemDefaults.colors(
                        selectedIconColor = DeepTealDark,
                        selectedTextColor = DeepTealDark,
                        indicatorColor = DeepTealLight
                    ),
                    modifier = Modifier.testTag("tab_zkv")
                )
            }
        }
    ) { innerPadding ->
        when (state.currentTab) {
            AppTab.DISCOVERY -> DiscoveryScreen(
                state = state,
                viewModel = viewModel,
                modifier = Modifier.padding(innerPadding)
            )
            AppTab.MARKETPLACE -> MarketplaceScreen(
                state = state,
                viewModel = viewModel,
                modifier = Modifier.padding(innerPadding)
            )
            AppTab.MY_BADGE -> MyBadgeScreen(
                state = state,
                viewModel = viewModel,
                modifier = Modifier.padding(innerPadding)
            )
            AppTab.ZKV_GATEWAY -> ZkvGatewayScreen(
                state = state,
                viewModel = viewModel,
                modifier = Modifier.padding(innerPadding)
            )
        }
    }
}

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(text = "Hello $name!", modifier = modifier)
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    HealthPulseTheme {
        HealthPulseApp()
    }
}


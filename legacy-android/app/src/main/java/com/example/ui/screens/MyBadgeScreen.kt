package com.example.ui.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.AppLanguage
import com.example.model.AppStrings
import com.example.model.BadgeTier
import com.example.ui.AppTab
import com.example.ui.HealthPulseUiState
import com.example.ui.HealthPulseViewModel
import com.example.ui.theme.*

@Composable
fun MyBadgeScreen(
    state: HealthPulseUiState,
    viewModel: HealthPulseViewModel,
    modifier: Modifier = Modifier
) {
    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(horizontal = 16.dp),
        contentPadding = PaddingValues(top = 12.dp, bottom = 24.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Section 1: My Health Status & Tier Decay Mechanism
        item {
            MyTierStatusDecayCard(
                tier = state.myTier,
                lastTestDate = state.myLastTestDate,
                hospitalName = state.myHospital,
                daysRemaining = state.daysUntilTierDecay,
                language = state.language,
                onSimulateDecay = { viewModel.simulateTierDecay() },
                onRestoreTier = { viewModel.restoreTierToSilver() },
                onGoToMarketplace = { viewModel.setTab(AppTab.MARKETPLACE) }
            )
        }

        // Section 2: Clinical Epidemiology Explanation of Tier Decay
        item {
            TierDecayEpidemiologyCard(language = state.language)
        }

        // Section 3: Tier Guide & User Perks
        item {
            TierPerksGuideCard(
                currentTier = state.myTier,
                language = state.language,
                onUpgradeTier = { viewModel.setTab(AppTab.MARKETPLACE) }
            )
        }
    }
}

@Composable
private fun MyTierStatusDecayCard(
    tier: BadgeTier,
    lastTestDate: String,
    hospitalName: String,
    daysRemaining: Int,
    language: AppLanguage,
    onSimulateDecay: () -> Unit,
    onRestoreTier: () -> Unit,
    onGoToMarketplace: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(22.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        border = BorderStroke(1.5.dp, tier.color.copy(alpha = 0.5f))
    ) {
        Column(modifier = Modifier.padding(18.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        text = AppStrings.myHealthStatusTitle(language),
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = AppStrings.myHealthStatusSub(language),
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }

                Surface(
                    shape = RoundedCornerShape(12.dp),
                    color = tier.color.copy(alpha = 0.15f)
                ) {
                    Text(
                        text = "${tier.emoji} ${if (language == AppLanguage.TH) tier.shortName else tier.name.replace("_", "/")}",
                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 5.dp),
                        style = MaterialTheme.typography.labelLarge,
                        fontWeight = FontWeight.Bold,
                        color = tier.color
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Decay Meter Progress
            val progress = (daysRemaining / 180f).coerceIn(0f, 1f)
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = AppStrings.tierDecayClock(language),
                    style = MaterialTheme.typography.bodySmall,
                    fontWeight = FontWeight.Medium
                )
                Text(
                    text = AppStrings.daysRemaining(daysRemaining, language),
                    style = MaterialTheme.typography.bodySmall,
                    fontWeight = FontWeight.Bold,
                    color = if (daysRemaining > 30) SafeGreen else DangerRed
                )
            }

            Spacer(modifier = Modifier.height(6.dp))

            LinearProgressIndicator(
                progress = { progress },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(8.dp)
                    .clip(RoundedCornerShape(4.dp)),
                color = if (daysRemaining > 30) tier.color else DangerRed,
                trackColor = MaterialTheme.colorScheme.surfaceVariant
            )

            Spacer(modifier = Modifier.height(14.dp))

            if (tier != BadgeTier.UNVERIFIED) {
                Text(
                    text = "🏥 ${AppStrings.verifiedAt(hospitalName, lastTestDate, language)}",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            } else {
                Text(
                    text = if (language == AppLanguage.TH)
                        "⚠️ สถานะปัจจุบัน: Unverified เนื่องจากยังไม่ตรวจหรือผลตรวจเดิมเกิน 180 วันแล้ว"
                    else
                        "⚠️ Status: Unverified due to unperformed screening or expired beyond 180 days",
                    style = MaterialTheme.typography.bodySmall,
                    color = DangerRed
                )
            }

            Spacer(modifier = Modifier.height(14.dp))

            // Actions to test Tier Decay or Renew
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                OutlinedButton(
                    onClick = onSimulateDecay,
                    modifier = Modifier.weight(1f),
                    colors = ButtonDefaults.outlinedButtonColors(contentColor = WarmCoral)
                ) {
                    Text(AppStrings.simulateDecayButton(language), fontSize = 11.sp)
                }

                Button(
                    onClick = onRestoreTier,
                    modifier = Modifier.weight(1f),
                    colors = ButtonDefaults.buttonColors(containerColor = DeepTealPrimary)
                ) {
                    Text(AppStrings.renewTestButton(language), fontSize = 11.sp, color = Color.White)
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            Button(
                onClick = onGoToMarketplace,
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("renew_voucher_button"),
                colors = ButtonDefaults.buttonColors(containerColor = DeepTealPrimary)
            ) {
                Icon(
                    Icons.Filled.LocalHospital,
                    contentDescription = null,
                    tint = Color.White,
                    modifier = Modifier.size(16.dp)
                )
                Spacer(modifier = Modifier.width(6.dp))
                Text(AppStrings.upgradeBadgeAction(language), fontSize = 12.sp, color = Color.White)
            }
        }
    }
}

@Composable
private fun TierDecayEpidemiologyCard(language: AppLanguage) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = DarkNavySurface)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    Icons.Filled.Science,
                    contentDescription = null,
                    tint = PulseTertiary,
                    modifier = Modifier.size(20.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = AppStrings.decayWhyTitle(language),
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = AppStrings.decayWhyBody(language),
                style = MaterialTheme.typography.bodySmall,
                color = Color(0xFFE2E8F0),
                lineHeight = 18.sp
            )
        }
    }
}

@Composable
private fun TierPerksGuideCard(
    currentTier: BadgeTier,
    language: AppLanguage,
    onUpgradeTier: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant)
    ) {
        Column(modifier = Modifier.padding(18.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    Icons.Filled.WorkspacePremium,
                    contentDescription = null,
                    tint = PulseSecondary,
                    modifier = Modifier.size(22.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = AppStrings.badgeTiersAndPerks(language),
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
            }

            Spacer(modifier = Modifier.height(14.dp))

            BadgeTier.entries.forEach { tier ->
                val isMyCurrentTier = tier == currentTier
                Surface(
                    shape = RoundedCornerShape(12.dp),
                    color = if (isMyCurrentTier) tier.bgColor else SoftSandCard,
                    border = if (isMyCurrentTier) BorderStroke(1.5.dp, tier.color) else null,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 4.dp)
                ) {
                    Column(modifier = Modifier.padding(12.dp)) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Text(tier.emoji, fontSize = 18.sp)
                                Spacer(modifier = Modifier.width(8.dp))
                                Text(
                                    text = tier.getLocalizedTitle(language),
                                    style = MaterialTheme.typography.bodyMedium,
                                    fontWeight = FontWeight.Bold,
                                    color = if (isMyCurrentTier) tier.color else MaterialTheme.colorScheme.onSurface
                                )
                            }
                            if (isMyCurrentTier) {
                                Surface(
                                    shape = RoundedCornerShape(6.dp),
                                    color = tier.color
                                ) {
                                    Text(
                                        text = AppStrings.yourCurrentTier(language),
                                        color = Color.White,
                                        fontSize = 10.sp,
                                        fontWeight = FontWeight.Bold,
                                        modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                                    )
                                }
                            }
                        }

                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = tier.getLocalizedDescription(language),
                            style = MaterialTheme.typography.bodySmall.copy(fontSize = 11.sp),
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )

                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = "✨ ${if (language == AppLanguage.TH) "สิทธิประโยชน์" else "Perks"}: ${tier.getLocalizedPerk(language)}",
                            style = MaterialTheme.typography.bodySmall.copy(fontSize = 11.sp),
                            fontWeight = FontWeight.Medium,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                    }
                }
            }
        }
    }
}

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
                onSimulateDecay = { viewModel.simulateTierDecay() },
                onRestoreTier = { viewModel.restoreTierToSilver() },
                onGoToMarketplace = { viewModel.setTab(AppTab.MARKETPLACE) }
            )
        }

        // Section 2: Clinical Epidemiology Explanation of Tier Decay
        item {
            TierDecayEpidemiologyCard()
        }

        // Section 3: Tier Guide & User Perks
        item {
            TierPerksGuideCard(
                currentTier = state.myTier,
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
                        text = "สถานะสุขภาพและป้าย Badge ของฉัน",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = "อ้างอิงประวัติการตรวจในรอบ 6 เดือน",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }

                Surface(
                    shape = RoundedCornerShape(12.dp),
                    color = tier.color.copy(alpha = 0.15f)
                ) {
                    Text(
                        text = "${tier.emoji} ${tier.shortName}",
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
                    text = "อายุของผลตรวจ (Tier Decay Clock)",
                    style = MaterialTheme.typography.bodySmall,
                    fontWeight = FontWeight.Medium
                )
                Text(
                    text = if (daysRemaining > 0) "เหลือ $daysRemaining / 180 วัน" else "หมดอายุ (0 วัน)",
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
                    text = "🏥 ตรวจยืนยันเมื่อ $lastTestDate ที่ $hospitalName",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            } else {
                Text(
                    text = "⚠️ สถานะปัจจุบัน: Unverified เนื่องจากยังไม่ตรวจหรือผลตรวจเดิมเกิน 180 วันแล้ว",
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
                    Text("จำลอง Badge หมดอายุ", fontSize = 11.sp)
                }

                Button(
                    onClick = onRestoreTier,
                    modifier = Modifier.weight(1f),
                    colors = ButtonDefaults.buttonColors(containerColor = DeepTealPrimary)
                ) {
                    Text("ต่ออายุตรวจซ้ำ", fontSize = 11.sp, color = Color.White)
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
                Text("ตรวจสุขภาพเพื่ออัปเกรด Badge กับ รพ. พันธมิตร", fontSize = 12.sp, color = Color.White)
            }
        }
    }
}

@Composable
private fun TierDecayEpidemiologyCard() {
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
                    text = "หลักการทางระบาดวิทยา: ทำไมต้องมี Tier Decay?",
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "1. พฤติกรรมทางเพศเปลี่ยนแปลงตามกาลเวลา ผลตรวจเลือดในอดีตไม่สามารถรับประกันความเสี่ยงในปัจจุบันได้\n" +
                        "2. คำแนะนำของ CDC (ศูนย์ควบคุมโรคสหรัฐฯ) กำหนดให้ผู้มีเพศสัมพันธ์คัดกรองทุก 3-6 เดือน\n" +
                        "3. กลไก 180-Day Decay ช่วยรักษาความน่าเชื่อถือและความปลอดภัยสูงสุดของคอมมูนิตี้ Safe Date",
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
                    text = "ระดับ Badge และสิทธิประโยชน์บน Safe Date",
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
                                    text = tier.title,
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
                                        text = "ระดับของคุณ",
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
                            text = tier.description,
                            style = MaterialTheme.typography.bodySmall.copy(fontSize = 11.sp),
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )

                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = "✨ สิทธิประโยชน์: ${tier.perkSummary}",
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

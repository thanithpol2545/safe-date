package com.example.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.spring
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.example.model.BadgeTier
import com.example.model.UserProfile
import com.example.ui.HealthPulseUiState
import com.example.ui.HealthPulseViewModel
import com.example.ui.theme.*

@Composable
fun DiscoveryScreen(
    state: HealthPulseUiState,
    viewModel: HealthPulseViewModel,
    modifier: Modifier = Modifier
) {
    val filteredProfiles = viewModel.getFilteredProfiles()
    val currentIndex = state.currentProfileIndex.coerceAtMost(filteredProfiles.size)
    val hasProfiles = currentIndex < filteredProfiles.size

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        // Tier Filter Chips Bar
        TierFilterBar(
            selectedTier = state.tierFilter,
            onSelectTier = { viewModel.setTierFilter(it) },
            myTier = state.myTier
        )

        Box(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 8.dp),
            contentAlignment = Alignment.Center
        ) {
            if (hasProfiles) {
                val profile = filteredProfiles[currentIndex]
                DiscoveryCard(
                    profile = profile,
                    myTier = state.myTier,
                    onLike = { viewModel.likeCurrentProfile() },
                    onPass = { viewModel.passCurrentProfile() },
                    onRewind = { viewModel.rewindProfile() }
                )
            } else {
                EmptyDiscoveryView(
                    tierFilter = state.tierFilter,
                    onResetFilter = { viewModel.setTierFilter(null) }
                )
            }
        }

        // Action Buttons Row (Swipe Controllers)
        if (hasProfiles) {
            ActionControlsRow(
                onPass = { viewModel.passCurrentProfile() },
                onLike = { viewModel.likeCurrentProfile() },
                onRewind = { viewModel.rewindProfile() },
                canRewind = state.currentProfileIndex > 0 && state.myTier != BadgeTier.UNVERIFIED,
                myTier = state.myTier
            )
        }

        Spacer(modifier = Modifier.height(8.dp))
    }

    // Match Success Celebration Dialog
    if (state.isMatchDialogOpen && state.matchedProfile != null) {
        MatchSuccessDialog(
            matchedProfile = state.matchedProfile,
            myTier = state.myTier,
            onDismiss = { viewModel.dismissMatchDialog() }
        )
    }
}

@Composable
private fun TierFilterBar(
    selectedTier: BadgeTier?,
    onSelectTier: (BadgeTier?) -> Unit,
    myTier: BadgeTier
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 6.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        FilterChip(
            selected = selectedTier == null,
            onClick = { onSelectTier(null) },
            label = { Text("ทั้งหมด", style = MaterialTheme.typography.labelMedium) },
            colors = FilterChipDefaults.filterChipColors(
                selectedContainerColor = DeepTealPrimary.copy(alpha = 0.15f),
                selectedLabelColor = DeepTealDark
            ),
            modifier = Modifier.testTag("filter_all")
        )

        BadgeTier.entries.filter { it != BadgeTier.UNVERIFIED }.forEach { tier ->
            val isRestricted = myTier == BadgeTier.UNVERIFIED && tier != BadgeTier.BRONZE
            FilterChip(
                selected = selectedTier == tier,
                onClick = {
                    if (!isRestricted) {
                        onSelectTier(tier)
                    }
                },
                enabled = !isRestricted,
                leadingIcon = {
                    Text(tier.emoji, fontSize = 12.sp)
                },
                label = {
                    Text(tier.shortName, style = MaterialTheme.typography.labelMedium)
                },
                colors = FilterChipDefaults.filterChipColors(
                    selectedContainerColor = tier.bgColor,
                    selectedLabelColor = tier.color
                ),
                modifier = Modifier.testTag("filter_${tier.name}")
            )
        }
    }
}

@Composable
private fun DiscoveryCard(
    profile: UserProfile,
    myTier: BadgeTier,
    onLike: () -> Unit,
    onPass: () -> Unit,
    onRewind: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxSize()
            .shadow(12.dp, RoundedCornerShape(24.dp))
            .testTag("discovery_card"),
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        ),
        border = BorderStroke(
            width = if (profile.badgeTier == BadgeTier.GOLD_PLATINUM) 2.dp else 1.dp,
            color = profile.badgeTier.color.copy(alpha = 0.5f)
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
        ) {
            // Hero Visual / Photo Placeholder with Clinical Tier Overlay
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(260.dp)
                    .background(
                        Brush.verticalGradient(
                            colors = listOf(
                                profile.avatarBgColor,
                                profile.avatarBgColor.copy(alpha = 0.8f),
                                MaterialTheme.colorScheme.surface
                            )
                        )
                    ),
                contentAlignment = Alignment.Center
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Box(
                        modifier = Modifier
                            .size(100.dp)
                            .clip(CircleShape)
                            .background(Color.White.copy(alpha = 0.25f)),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = profile.name.take(1),
                            color = Color.White,
                            fontSize = 44.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    // Tier Status Pill
                    Surface(
                        shape = RoundedCornerShape(20.dp),
                        color = Color.Black.copy(alpha = 0.6f),
                        border = BorderStroke(1.5.dp, profile.badgeTier.color)
                    ) {
                        Row(
                            modifier = Modifier.padding(horizontal = 14.dp, vertical = 6.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(profile.badgeTier.emoji, fontSize = 16.sp)
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = profile.badgeTier.title,
                                color = Color.White,
                                fontWeight = FontWeight.SemiBold,
                                fontSize = 13.sp
                            )
                        }
                    }
                }

                // Distance & Location Pill
                Surface(
                    modifier = Modifier
                        .align(Alignment.TopEnd)
                        .padding(14.dp),
                    shape = RoundedCornerShape(12.dp),
                    color = Color.Black.copy(alpha = 0.5f)
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            Icons.Outlined.LocationOn,
                            contentDescription = null,
                            tint = Color.White,
                            modifier = Modifier.size(14.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = "${profile.distanceKm} กม.",
                            color = Color.White,
                            fontSize = 12.sp
                        )
                    }
                }
            }

            // Info Details
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(20.dp)
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(
                                text = "${profile.name}, ${profile.age}",
                                style = MaterialTheme.typography.headlineSmall,
                                fontWeight = FontWeight.Bold
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            if (profile.badgeTier != BadgeTier.UNVERIFIED) {
                                Icon(
                                    Icons.Filled.Verified,
                                    contentDescription = "Verified Health Badge",
                                    tint = profile.badgeTier.color,
                                    modifier = Modifier.size(24.dp)
                                )
                            }
                        }
                        Text(
                            text = profile.occupation,
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Clinical Verification Box (Clinical Safeguard: Window Period Awareness)
                if (profile.badgeTier != BadgeTier.UNVERIFIED && profile.lastTestedDate != null) {
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        colors = CardDefaults.cardColors(
                            containerColor = profile.badgeTier.bgColor
                        ),
                        shape = RoundedCornerShape(16.dp),
                        border = BorderStroke(1.dp, profile.badgeTier.color.copy(alpha = 0.4f))
                    ) {
                        Column(modifier = Modifier.padding(14.dp)) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(
                                    Icons.Filled.LocalHospital,
                                    contentDescription = null,
                                    tint = profile.badgeTier.color,
                                    modifier = Modifier.size(18.dp)
                                )
                                Spacer(modifier = Modifier.width(6.dp))
                                Text(
                                    text = profile.partnerClinicName ?: "สถานพยาบาลพันธมิตร",
                                    style = MaterialTheme.typography.labelLarge,
                                    fontWeight = FontWeight.Bold,
                                    color = profile.badgeTier.color
                                )
                            }

                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = "📅 เข้ารับการตรวจเมื่อ: ${profile.lastTestedDate}",
                                style = MaterialTheme.typography.bodySmall,
                                fontWeight = FontWeight.Medium,
                                color = MaterialTheme.colorScheme.onSurface
                            )

                            profile.screeningPackageName?.let { pkg ->
                                Text(
                                    text = "🔬 แพ็กเกจ: $pkg",
                                    style = MaterialTheme.typography.bodySmall,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }

                            Spacer(modifier = Modifier.height(8.dp))
                            // Crucial Window Period Awareness Notice
                            Surface(
                                shape = RoundedCornerShape(8.dp),
                                color = MaterialTheme.colorScheme.surface.copy(alpha = 0.8f)
                            ) {
                                Row(
                                    modifier = Modifier.padding(8.dp),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Icon(
                                        Icons.Outlined.Info,
                                        contentDescription = null,
                                        tint = TierSilverColor,
                                        modifier = Modifier.size(14.dp)
                                    )
                                    Spacer(modifier = Modifier.width(6.dp))
                                    Text(
                                        text = "การแสดงผลระบุตามวันตรวจจริง ไม่มีคำกล่าวอ้างว่าปราศจากเชื้อ 100% ตามหลักระบาดวิทยา (Window Period)",
                                        style = MaterialTheme.typography.bodySmall.copy(fontSize = 11.sp),
                                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                                        lineHeight = 15.sp
                                    )
                                }
                            }
                        }
                    }
                } else {
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        colors = CardDefaults.cardColors(
                            containerColor = TierUnverifiedBg
                        ),
                        shape = RoundedCornerShape(16.dp)
                    ) {
                        Row(
                            modifier = Modifier.padding(14.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                Icons.Outlined.HelpOutline,
                                contentDescription = null,
                                tint = TierUnverifiedColor,
                                modifier = Modifier.size(20.dp)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = "ผู้ใช้นี้ยังไม่มีข้อมูลการตรวจสุขภาพในระบบ Safe Date หรือผลตรวจเดิมหมดอายุเกิน 6 เดือนแล้ว",
                                style = MaterialTheme.typography.bodySmall,
                                color = SlateGreyBody
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Bio
                Text(
                    text = "เกี่ยวกับฉัน",
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = profile.bio,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurface,
                    lineHeight = 20.sp
                )

                Spacer(modifier = Modifier.height(14.dp))

                // Interests
                Text(
                    text = "ความสนใจ",
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(8.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    profile.interests.forEach { tag ->
                        Surface(
                            shape = RoundedCornerShape(16.dp),
                            color = MaterialTheme.colorScheme.primary.copy(alpha = 0.1f)
                        ) {
                            Text(
                                text = "#$tag",
                                modifier = Modifier.padding(horizontal = 10.dp, vertical = 5.dp),
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.primary,
                                fontWeight = FontWeight.Medium
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun ActionControlsRow(
    onPass: () -> Unit,
    onLike: () -> Unit,
    onRewind: () -> Unit,
    canRewind: Boolean,
    myTier: BadgeTier
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 24.dp, vertical = 6.dp),
        horizontalArrangement = Arrangement.SpaceEvenly,
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Rewind Button (Bronze+ unlocked) - Slate Grey / Teal tint
        IconButton(
            onClick = onRewind,
            enabled = canRewind,
            modifier = Modifier
                .size(48.dp)
                .shadow(3.dp, CircleShape)
                .background(
                    if (canRewind) PureWhiteSurface else SoftSandCard,
                    CircleShape
                )
                .testTag("action_rewind")
        ) {
            Icon(
                Icons.Filled.Refresh,
                contentDescription = "Rewind",
                tint = if (canRewind) SlateGreyMuted else Color.LightGray,
                modifier = Modifier.size(24.dp)
            )
        }

        // Pass Button
        IconButton(
            onClick = onPass,
            modifier = Modifier
                .size(56.dp)
                .shadow(4.dp, CircleShape)
                .background(PureWhiteSurface, CircleShape)
                .testTag("action_pass")
        ) {
            Icon(
                Icons.Filled.Close,
                contentDescription = "Pass",
                tint = SlateGreyMuted,
                modifier = Modifier.size(28.dp)
            )
        }

        // Super Like / Boost Button - Deep Teal accent
        IconButton(
            onClick = onLike,
            modifier = Modifier
                .size(48.dp)
                .shadow(3.dp, CircleShape)
                .background(PureWhiteSurface, CircleShape)
                .testTag("action_super")
        ) {
            Icon(
                Icons.Filled.Star,
                contentDescription = "Super Like",
                tint = DeepTealPrimary,
                modifier = Modifier.size(24.dp)
            )
        }

        // Like / Match Button - Primary Action: Warm Coral (#F43F5E)
        IconButton(
            onClick = onLike,
            modifier = Modifier
                .size(58.dp)
                .shadow(6.dp, CircleShape)
                .background(
                    Brush.linearGradient(listOf(WarmCoral, WarmCoralRose)),
                    CircleShape
                )
                .testTag("action_like")
        ) {
            Icon(
                Icons.Filled.Favorite,
                contentDescription = "Like",
                tint = Color.White,
                modifier = Modifier.size(30.dp)
            )
        }
    }
}

@Composable
private fun EmptyDiscoveryView(
    tierFilter: BadgeTier?,
    onResetFilter: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(32.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Icon(
            Icons.Outlined.SearchOff,
            contentDescription = null,
            modifier = Modifier.size(64.dp),
            tint = MaterialTheme.colorScheme.primary.copy(alpha = 0.6f)
        )
        Spacer(modifier = Modifier.height(16.dp))
        Text(
            text = "ดูโปรไฟล์ครบในฟิลเตอร์นี้แล้ว",
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold
        )
        Spacer(modifier = Modifier.height(6.dp))
        Text(
            text = if (tierFilter != null) "ลองรีเซ็ตฟิลเตอร์ ${tierFilter.title} เพื่อดูโปรไฟล์อื่นๆ" else "ระบบจะแนะนำโปรไฟล์ใหม่เมื่อมีผู้เข้ารับการตรวจยืนยันเพิ่มขึ้น",
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            textAlign = TextAlign.Center
        )
        Spacer(modifier = Modifier.height(16.dp))
        Button(
            onClick = onResetFilter,
            modifier = Modifier.testTag("button_reset_filter")
        ) {
            Text("ดูโปรไฟล์ทั้งหมด")
        }
    }
}

@Composable
private fun MatchSuccessDialog(
    matchedProfile: UserProfile,
    myTier: BadgeTier,
    onDismiss: () -> Unit
) {
    Dialog(onDismissRequest = onDismiss) {
        Card(
            shape = RoundedCornerShape(28.dp),
            colors = CardDefaults.cardColors(containerColor = PureWhiteSurface),
            border = BorderStroke(1.5.dp, WarmCoral.copy(alpha = 0.3f)),
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
                .testTag("match_dialog")
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "🎉 IT'S A HEALTHY MATCH! 🎉",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Black,
                    color = WarmCoral
                )

                Spacer(modifier = Modifier.height(12.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // My Tier Badge
                    Surface(
                        shape = CircleShape,
                        color = myTier.bgColor,
                        border = BorderStroke(2.dp, myTier.color),
                        modifier = Modifier.size(60.dp)
                    ) {
                        Box(contentAlignment = Alignment.Center) {
                            Text(myTier.emoji, fontSize = 28.sp)
                        }
                    }

                    Icon(
                        Icons.Filled.Favorite,
                        contentDescription = null,
                        tint = WarmCoral,
                        modifier = Modifier
                            .padding(horizontal = 14.dp)
                            .size(32.dp)
                    )

                    // Matched Tier Badge
                    Surface(
                        shape = CircleShape,
                        color = matchedProfile.badgeTier.bgColor,
                        border = BorderStroke(2.dp, matchedProfile.badgeTier.color),
                        modifier = Modifier.size(60.dp)
                    ) {
                        Box(contentAlignment = Alignment.Center) {
                            Text(matchedProfile.badgeTier.emoji, fontSize = 28.sp)
                        }
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                Text(
                    text = "คุณและ ${matchedProfile.name} แมตช์กันสำเร็จ!",
                    style = MaterialTheme.typography.titleSmall,
                    color = SlateGreyText,
                    fontWeight = FontWeight.Bold
                )

                Spacer(modifier = Modifier.height(6.dp))

                Text(
                    text = "ทั้งสองฝ่ายผ่านการยืนยันสุขภาพจากสถานพยาบาลพันธมิตรด้วยมาตรฐานความปลอดภัยสูง",
                    style = MaterialTheme.typography.bodySmall,
                    color = SlateGreyMuted,
                    textAlign = TextAlign.Center
                )

                Spacer(modifier = Modifier.height(20.dp))

                Button(
                    onClick = onDismiss,
                    colors = ButtonDefaults.buttonColors(containerColor = WarmCoral),
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("dismiss_match_button")
                ) {
                    Text("เริ่มบทสนทนาอย่างมั่นใจ", color = Color.White, fontWeight = FontWeight.Bold)
                }
            }
        }
    }
}

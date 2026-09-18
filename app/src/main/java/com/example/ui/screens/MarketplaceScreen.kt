package com.example.ui.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.example.model.AppLanguage
import com.example.model.AppStrings
import com.example.model.BadgeTier
import com.example.model.EVoucherPackage
import com.example.model.PartnerHospital
import com.example.model.PurchasedVoucher
import com.example.ui.HealthPulseUiState
import com.example.ui.HealthPulseViewModel
import com.example.ui.theme.*

@Composable
fun MarketplaceScreen(
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
        // Value Proposition Banner
        item {
            EcosystemValueBanner(language = state.language)
        }

        // Compliance Notice (Thai Medical Council No Brokerage Fee)
        item {
            MedicalCouncilComplianceCard(language = state.language)
        }

        // Section Title: Available Packages
        item {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
            ) {
                Icon(
                    Icons.Filled.LocalOffer,
                    contentDescription = null,
                    tint = PulsePrimary,
                    modifier = Modifier.size(20.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = AppStrings.marketplacePackagesSection(state.language),
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
            }
        }

        // Packages List
        items(state.packages) { pkg ->
            EVoucherPackageCard(
                pkg = pkg,
                language = state.language,
                onSelectBuy = { viewModel.openPurchaseDialog(pkg) }
            )
        }

        // Section Title: Partner Hospital Network
        item {
            Spacer(modifier = Modifier.height(8.dp))
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
            ) {
                Icon(
                    Icons.Filled.Domain,
                    contentDescription = null,
                    tint = PulseSecondary,
                    modifier = Modifier.size(20.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = AppStrings.marketplaceHospitalsSection(state.language),
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
            }
        }

        // Hospital Cards
        items(state.partnerHospitals) { hospital ->
            HospitalPartnerCard(hospital = hospital, language = state.language)
        }
    }

    // Purchase Dialog (Select Hospital & Confirm)
    state.selectedPackageForPurchase?.let { pkg ->
        PurchaseConfirmationDialog(
            pkg = pkg,
            hospitals = state.partnerHospitals,
            selectedHospital = state.selectedHospitalForPurchase,
            language = state.language,
            onSelectHospital = { viewModel.selectHospital(it) },
            onConfirm = { viewModel.confirmPurchaseVoucher() },
            onDismiss = { viewModel.dismissPurchaseDialog() }
        )
    }

    // Dynamic QR Voucher Dialog
    if (state.showVoucherQrDialog && state.activePurchasedVoucher != null) {
        VoucherQrTokenDialog(
            voucher = state.activePurchasedVoucher,
            language = state.language,
            onDismiss = { viewModel.dismissVoucherQrDialog() }
        )
    }
}

@Composable
private fun EcosystemValueBanner(language: AppLanguage) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = DarkNavySurface)
    ) {
        Column(modifier = Modifier.padding(18.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Surface(
                    shape = CircleShape,
                    color = PulsePrimary.copy(alpha = 0.2f),
                    modifier = Modifier.size(36.dp)
                ) {
                    Box(contentAlignment = Alignment.Center) {
                        Icon(
                            Icons.Filled.HealthAndSafety,
                            contentDescription = null,
                            tint = PulsePrimary,
                            modifier = Modifier.size(20.dp)
                        )
                    }
                }
                Spacer(modifier = Modifier.width(10.dp))
                Column {
                    Text(
                        text = AppStrings.ecosystemBannerTitle(language),
                        style = MaterialTheme.typography.titleSmall,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                    Text(
                        text = AppStrings.ecosystemBannerSub(language),
                        style = MaterialTheme.typography.bodySmall,
                        color = Color(0xFF94A3B8)
                    )
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            Text(
                text = AppStrings.ecosystemBannerBody(language),
                style = MaterialTheme.typography.bodySmall,
                color = Color(0xFFE2E8F0),
                lineHeight = 18.sp
            )
        }
    }
}

@Composable
private fun MedicalCouncilComplianceCard(language: AppLanguage) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = DeepTealPrimary.copy(alpha = 0.08f)
        ),
        border = BorderStroke(1.dp, DeepTealPrimary.copy(alpha = 0.25f))
    ) {
        Row(
            modifier = Modifier.padding(14.dp),
            verticalAlignment = Alignment.Top
        ) {
            Icon(
                Icons.Outlined.Gavel,
                contentDescription = null,
                tint = DeepTealPrimary,
                modifier = Modifier.size(20.dp)
            )
            Spacer(modifier = Modifier.width(10.dp))
            Column {
                Text(
                    text = AppStrings.medicalCouncilTitle(language),
                    style = MaterialTheme.typography.labelMedium,
                    fontWeight = FontWeight.Bold,
                    color = DeepTealDark
                )
                Spacer(modifier = Modifier.height(3.dp))
                Text(
                    text = AppStrings.medicalCouncilBody(language),
                    style = MaterialTheme.typography.bodySmall.copy(fontSize = 11.sp),
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    lineHeight = 15.sp
                )
            }
        }
    }
}

@Composable
private fun EVoucherPackageCard(
    pkg: EVoucherPackage,
    language: AppLanguage,
    onSelectBuy: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        ),
        border = BorderStroke(1.dp, pkg.badgeTierUnlocked.color.copy(alpha = 0.4f))
    ) {
        Column(modifier = Modifier.padding(18.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Surface(
                    shape = RoundedCornerShape(12.dp),
                    color = pkg.badgeTierUnlocked.color.copy(alpha = 0.15f)
                ) {
                    Text(
                        text = if (language == AppLanguage.TH) pkg.tag else pkg.badgeTierUnlocked.name.replace("_", "/"),
                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp),
                        style = MaterialTheme.typography.labelMedium,
                        fontWeight = FontWeight.Bold,
                        color = pkg.badgeTierUnlocked.color
                    )
                }

                Row(verticalAlignment = Alignment.Bottom) {
                    Text(
                        text = "฿${String.format("%,d", pkg.priceThb)}",
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Black,
                        color = PulsePrimary
                    )
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            Text(
                text = pkg.title,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.height(4.dp))

            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    Icons.Outlined.Schedule,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier.size(14.dp)
                )
                Spacer(modifier = Modifier.width(4.dp))
                Text(
                    text = if (language == AppLanguage.TH) pkg.turnaroundTime else "Results in 2-4 hrs (Express)",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Included tests list
            Column(
                verticalArrangement = Arrangement.spacedBy(4.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                pkg.includedTests.forEach { testName ->
                    Row(verticalAlignment = Alignment.Top) {
                        Icon(
                            Icons.Filled.CheckCircle,
                            contentDescription = null,
                            tint = SafeGreen,
                            modifier = Modifier
                                .padding(top = 2.dp)
                                .size(14.dp)
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = testName,
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Cost Transparency Breakdown
            Surface(
                shape = RoundedCornerShape(10.dp),
                color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.4f),
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 12.dp, vertical = 8.dp),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = "${AppStrings.costLab(language)}: ฿${String.format("%,d", pkg.wholesalePriceThb)}",
                        style = MaterialTheme.typography.bodySmall.copy(fontSize = 11.sp),
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Text(
                        text = "${AppStrings.platformFee(language)}: ฿${String.format("%,d", pkg.platformFeeThb)}",
                        style = MaterialTheme.typography.bodySmall.copy(fontSize = 11.sp),
                        fontWeight = FontWeight.SemiBold,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            Button(
                onClick = onSelectBuy,
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("buy_voucher_${pkg.id}"),
                colors = ButtonDefaults.buttonColors(containerColor = pkg.badgeTierUnlocked.color)
            ) {
                Icon(
                    Icons.Filled.ConfirmationNumber,
                    contentDescription = null,
                    modifier = Modifier.size(18.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "${AppStrings.buyVoucherButton(language)} (${pkg.badgeTierUnlocked.getLocalizedTitle(language)})",
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}

@Composable
private fun HospitalPartnerCard(hospital: PartnerHospital, language: AppLanguage) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(14.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Surface(
                shape = RoundedCornerShape(12.dp),
                color = DeepTealPrimary.copy(alpha = 0.12f),
                modifier = Modifier.size(44.dp)
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Icon(
                        Icons.Filled.LocalHospital,
                        contentDescription = null,
                        tint = DeepTealPrimary,
                        modifier = Modifier.size(24.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.width(12.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = hospital.name,
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = hospital.branches,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = "⭐ ${hospital.rating}",
                        style = MaterialTheme.typography.bodySmall,
                        fontWeight = FontWeight.Medium
                    )
                    Spacer(modifier = Modifier.width(10.dp))
                    Surface(
                        shape = RoundedCornerShape(6.dp),
                        color = SafeGreenLight
                    ) {
                        Text(
                            text = "HL7 FHIR Live API",
                            color = SafeGreen,
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun PurchaseConfirmationDialog(
    pkg: EVoucherPackage,
    hospitals: List<PartnerHospital>,
    selectedHospital: PartnerHospital,
    language: AppLanguage,
    onSelectHospital: (PartnerHospital) -> Unit,
    onConfirm: () -> Unit,
    onDismiss: () -> Unit
) {
    Dialog(onDismissRequest = onDismiss) {
        Card(
            shape = RoundedCornerShape(24.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp)
                .testTag("purchase_dialog")
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(20.dp)
            ) {
                Text(
                    text = AppStrings.confirmPurchaseTitle(language),
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = pkg.title,
                    style = MaterialTheme.typography.bodyMedium,
                    color = PulsePrimary,
                    fontWeight = FontWeight.SemiBold
                )

                Spacer(modifier = Modifier.height(14.dp))

                Text(
                    text = AppStrings.selectHospitalLabel(language),
                    style = MaterialTheme.typography.labelMedium,
                    fontWeight = FontWeight.Medium
                )

                Spacer(modifier = Modifier.height(8.dp))

                hospitals.forEach { hospital ->
                    val isSelected = hospital.id == selectedHospital.id
                    Surface(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 4.dp)
                            .clickable { onSelectHospital(hospital) },
                        shape = RoundedCornerShape(12.dp),
                        color = if (isSelected) DeepTealLight else SoftSandCard,
                        border = if (isSelected) BorderStroke(1.5.dp, DeepTealPrimary) else null
                    ) {
                        Row(
                            modifier = Modifier.padding(12.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            RadioButton(
                                selected = isSelected,
                                onClick = { onSelectHospital(hospital) },
                                colors = RadioButtonDefaults.colors(selectedColor = DeepTealPrimary)
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Column {
                                Text(
                                    text = hospital.name,
                                    style = MaterialTheme.typography.bodySmall,
                                    fontWeight = FontWeight.SemiBold
                                )
                                Text(
                                    text = hospital.branches,
                                    style = MaterialTheme.typography.bodySmall.copy(fontSize = 11.sp),
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(14.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = AppStrings.totalPayment(language),
                        style = MaterialTheme.typography.titleSmall
                    )
                    Text(
                        text = "฿${String.format("%,d", pkg.priceThb)}",
                        style = MaterialTheme.typography.headlineSmall,
                        fontWeight = FontWeight.Bold,
                        color = PulsePrimary
                    )
                }

                Spacer(modifier = Modifier.height(18.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    OutlinedButton(
                        onClick = onDismiss,
                        modifier = Modifier.weight(1f)
                    ) {
                        Text(AppStrings.cancel(language))
                    }
                    Button(
                        onClick = onConfirm,
                        modifier = Modifier
                            .weight(1.5f)
                            .testTag("confirm_purchase_button"),
                        colors = ButtonDefaults.buttonColors(containerColor = PulsePrimary)
                    ) {
                        Text(AppStrings.payAndGetVoucher(language))
                    }
                }
            }
        }
    }
}

@Composable
private fun VoucherQrTokenDialog(
    voucher: PurchasedVoucher,
    language: AppLanguage,
    onDismiss: () -> Unit
) {
    Dialog(onDismissRequest = onDismiss) {
        Card(
            shape = RoundedCornerShape(24.dp),
            colors = CardDefaults.cardColors(containerColor = DarkNavySurface),
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp)
                .testTag("voucher_qr_dialog")
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = AppStrings.voucherSuccessTitle(language),
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = SafeGreen
                )

                Spacer(modifier = Modifier.height(6.dp))

                Text(
                    text = voucher.packageTitle,
                    style = MaterialTheme.typography.bodyMedium,
                    color = Color.White,
                    fontWeight = FontWeight.SemiBold
                )

                Text(
                    text = "${if (language == AppLanguage.TH) "สถานพยาบาล" else "Hospital"}: ${voucher.hospitalName}",
                    style = MaterialTheme.typography.bodySmall,
                    color = Color(0xFF94A3B8)
                )

                Spacer(modifier = Modifier.height(18.dp))

                // Stylized Dynamic QR Visual Placeholder
                Surface(
                    shape = RoundedCornerShape(16.dp),
                    color = Color.White,
                    modifier = Modifier
                        .size(180.dp)
                        .padding(8.dp)
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center,
                        modifier = Modifier.fillMaxSize()
                    ) {
                        Icon(
                            Icons.Filled.QrCode2,
                            contentDescription = "Voucher QR Code",
                            tint = Color.Black,
                            modifier = Modifier.size(130.dp)
                        )
                        Text(
                            text = voucher.qrToken,
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.DarkGray
                        )
                    }
                }

                Spacer(modifier = Modifier.height(14.dp))

                Text(
                    text = AppStrings.voucherInstructions(language),
                    style = MaterialTheme.typography.bodySmall.copy(fontSize = 11.sp),
                    color = Color(0xFFE2E8F0),
                    textAlign = TextAlign.Center,
                    lineHeight = 16.sp
                )

                Spacer(modifier = Modifier.height(20.dp))

                Button(
                    onClick = onDismiss,
                    colors = ButtonDefaults.buttonColors(containerColor = PulsePrimary),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(AppStrings.acknowledgeAndReturn(language))
                }
            }
        }
    }
}

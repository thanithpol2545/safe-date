package com.example.ui.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.PartnerHospital
import com.example.model.ZkvVerificationLog
import com.example.ui.HealthPulseUiState
import com.example.ui.HealthPulseViewModel
import com.example.ui.theme.*

@Composable
fun ZkvGatewayScreen(
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
        // Architectural Intro Card
        item {
            ZkvArchitectureIntroCard()
        }

        // PDPA Compliance Gateway
        item {
            PdpaConsentGatewayCard(
                explicitConsent = state.pdpaExplicitConsentChecked,
                dataMinimization = state.pdpaDataMinimizationChecked,
                onToggleExplicit = { viewModel.togglePdpaConsent(it) },
                onToggleMinimization = { viewModel.toggleDataMinimization(it) }
            )
        }

        // Interactive Live FHIR & ZKV Simulator
        item {
            FhirSimulatorCard(
                hospitals = state.partnerHospitals,
                selectedHospital = state.selectedFhirHospital,
                onSelectHospital = { viewModel.selectFhirHospital(it) },
                isVerifying = state.isZkvVerifying,
                canRun = state.pdpaExplicitConsentChecked && state.pdpaDataMinimizationChecked,
                onRunVerification = { viewModel.runZkvVerification() }
            )
        }

        // Live JSON Payload Comparison
        item {
            JsonPayloadComparisonCard(zkvLog = state.zkvResult)
        }

        // Audit Logs Timeline
        if (state.fhirVerificationLogs.isNotEmpty()) {
            item {
                Text(
                    text = "ประวัติการตรวจสอบย้อนหลัง (Audit Log - No PHI)",
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.Bold
                )
            }

            items(state.fhirVerificationLogs) { log ->
                AuditLogItemCard(log = log)
            }
        }
    }
}

@Composable
private fun ZkvArchitectureIntroCard() {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = DarkNavySurface)
    ) {
        Column(modifier = Modifier.padding(18.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Surface(
                    shape = CircleShape,
                    color = DeepTealPrimary.copy(alpha = 0.2f),
                    modifier = Modifier.size(36.dp)
                ) {
                    Box(contentAlignment = Alignment.Center) {
                        Icon(
                            Icons.Filled.Security,
                            contentDescription = null,
                            tint = DeepTealPrimary,
                            modifier = Modifier.size(20.dp)
                        )
                    }
                }
                Spacer(modifier = Modifier.width(10.dp))
                Column {
                    Text(
                        text = "HL7 FHIR & Zero-Knowledge Verification",
                        style = MaterialTheme.typography.titleSmall,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                    Text(
                        text = "Technical Architecture & Privacy Compliance",
                        style = MaterialTheme.typography.bodySmall,
                        color = Color(0xFF94A3B8)
                    )
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            Text(
                text = "Safe Date ปฏิเสธวิธีอัปโหลดภาพถ่ายเอกสารผลแล็บเพราะเสี่ยงต่อการปลอมแปลง (Document Tampering) เราเชื่อมตรงกับระบบ HIS โรงพยาบาลผ่านมาตรฐาน HL7 FHIR API และเทคโนโลยี ZKV โดยโรงพยาบาลประมวลผลแล้วส่งกลับมาเพียงค่า Boolean Logic เซิร์ฟเวอร์ของแอปจึงไม่เคยจัดเก็บข้อมูลเวชระเบียนของผู้ใช้แม้แต่บรรทัดเดียว",
                style = MaterialTheme.typography.bodySmall,
                color = Color(0xFFE2E8F0),
                lineHeight = 18.sp
            )
        }
    }
}

@Composable
private fun PdpaConsentGatewayCard(
    explicitConsent: Boolean,
    dataMinimization: Boolean,
    onToggleExplicit: (Boolean) -> Unit,
    onToggleMinimization: (Boolean) -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(18.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    Icons.Filled.VerifiedUser,
                    contentDescription = null,
                    tint = SafeGreen,
                    modifier = Modifier.size(20.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "PDPA Consent Gateway (มาตรา 26 ข้อมูลอ่อนไหว)",
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.Bold
                )
            }

            Spacer(modifier = Modifier.height(10.dp))

            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
            ) {
                Checkbox(
                    checked = explicitConsent,
                    onCheckedChange = onToggleExplicit,
                    colors = CheckboxDefaults.colors(checkedColor = DeepTealPrimary),
                    modifier = Modifier.testTag("checkbox_explicit_consent")
                )
                Spacer(modifier = Modifier.width(4.dp))
                Text(
                    text = "ข้าพเจ้ายินยอมโดยชัดแจ้ง (Explicit Consent) ให้ Safe Date ดึงสถานะ Boolean จาก HIS เพื่อแสดง Verified Badge",
                    style = MaterialTheme.typography.bodySmall
                )
            }

            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
            ) {
                Checkbox(
                    checked = dataMinimization,
                    onCheckedChange = onToggleMinimization,
                    colors = CheckboxDefaults.colors(checkedColor = DeepTealPrimary),
                    modifier = Modifier.testTag("checkbox_data_minimization")
                )
                Spacer(modifier = Modifier.width(4.dp))
                Text(
                    text = "รับทราบหลักการ Data Minimization: แอปจะไม่เข้าถึง ไม่ส่งต่อ และไม่บันทึกค่าผลแล็บละเอียดใดๆ ทั้งสิ้น",
                    style = MaterialTheme.typography.bodySmall
                )
            }
        }
    }
}

@Composable
private fun FhirSimulatorCard(
    hospitals: List<PartnerHospital>,
    selectedHospital: PartnerHospital,
    onSelectHospital: (PartnerHospital) -> Unit,
    isVerifying: Boolean,
    canRun: Boolean,
    onRunVerification: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(18.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        border = BorderStroke(1.dp, DeepTealPrimary.copy(alpha = 0.3f))
    ) {
        Column(modifier = Modifier.padding(18.dp)) {
            Text(
                text = "จำลองการทดสอบ HL7 FHIR Interoperability",
                style = MaterialTheme.typography.titleSmall,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(6.dp))
            Text(
                text = "เลือกสถานพยาบาลเป้าหมายเพื่อทำ Handshake ผ่าน OAuth 2.0:",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            Spacer(modifier = Modifier.height(10.dp))

            hospitals.take(3).forEach { hosp ->
                val isSelected = hosp.id == selectedHospital.id
                Surface(
                    shape = RoundedCornerShape(10.dp),
                    color = if (isSelected) DeepTealLight else SoftSandCard,
                    border = if (isSelected) BorderStroke(1.dp, DeepTealPrimary) else null,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 4.dp)
                ) {
                    Row(
                        modifier = Modifier.padding(10.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        RadioButton(
                            selected = isSelected,
                            onClick = { onSelectHospital(hosp) },
                            colors = RadioButtonDefaults.colors(selectedColor = DeepTealPrimary)
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Column {
                            Text(
                                text = hosp.name,
                                style = MaterialTheme.typography.bodySmall,
                                fontWeight = FontWeight.Bold
                            )
                            Text(
                                text = "Endpoint: ${hosp.fhirEndpoint}",
                                style = MaterialTheme.typography.bodySmall.copy(
                                    fontSize = 10.sp,
                                    fontFamily = FontFamily.Monospace
                                ),
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            Button(
                onClick = onRunVerification,
                enabled = canRun && !isVerifying,
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("run_zkv_button"),
                colors = ButtonDefaults.buttonColors(containerColor = DeepTealPrimary)
            ) {
                if (isVerifying) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(18.dp),
                        color = Color.White,
                        strokeWidth = 2.dp
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("กำลังเชื่อมต่อ HIS & ประมวลผล ZKV...", color = Color.White)
                } else {
                    Icon(
                        Icons.Filled.Sensors,
                        contentDescription = null,
                        tint = Color.White,
                        modifier = Modifier.size(18.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("ยิงคำขอ FHIR Observation (ZKV Handshake)", color = Color.White)
                }
            }
        }
    }
}

@Composable
private fun JsonPayloadComparisonCard(zkvLog: ZkvVerificationLog?) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(18.dp),
        colors = CardDefaults.cardColors(containerColor = DarkNavyCard)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = "ZKV Data Minimization Showcase",
                style = MaterialTheme.typography.titleSmall,
                fontWeight = FontWeight.Bold,
                color = Color.White
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = "เปรียบเทียบข้อมูลภายใน รพ. (PHI) กับข้อมูลที่ส่งมา Dating App (ZKV)",
                style = MaterialTheme.typography.bodySmall.copy(fontSize = 11.sp),
                color = Color(0xFF94A3B8)
            )

            Spacer(modifier = Modifier.height(12.dp))

            // Enclave inside Hospital
            Surface(
                shape = RoundedCornerShape(10.dp),
                color = Color(0xFF0F172A),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(10.dp)) {
                    Text(
                        text = "🔒 ภายใน Hospital Secure Perimeter (ห้ามส่งออก):",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        color = DangerRed
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = """
                        {
                          "resourceType": "DiagnosticReport",
                          "patientHN": "HN-9824128",
                          "labResults": [
                            {"test": "HIV 4th Gen Ag/Ab", "val": "NON-REACTIVE"},
                            {"test": "Syphilis RPR", "val": "NON-REACTIVE"},
                            {"test": "Chlamydia trachomatis PCR", "val": "NEGATIVE"}
                          ]
                        }
                        """.trimIndent(),
                        fontFamily = FontFamily.Monospace,
                        fontSize = 10.sp,
                        color = Color(0xFFFCA5A5)
                    )
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            // Sanitized ZKV Response to Dating Server
            Surface(
                shape = RoundedCornerShape(10.dp),
                color = Color(0xFF0F172A),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(10.dp)) {
                    Text(
                        text = "✅ สิ่งที่ส่งกลับมา HealthPulse Dating (Zero-Knowledge):",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        color = SafeGreen
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = """
                        {
                          "patientTokenHash": "${zkvLog?.anonymizedPatientHash ?: "0x8f4c2e19ba33de"}",
                          "verified": true,
                          "tierAwarded": "${zkvLog?.tierAwarded?.name ?: "GOLD_PLATINUM"}",
                          "testDate": "2026-09-18",
                          "daysValid": 180
                        }
                        """.trimIndent(),
                        fontFamily = FontFamily.Monospace,
                        fontSize = 10.sp,
                        color = SafeGreen
                    )
                }
            }
        }
    }
}

@Composable
private fun AuditLogItemCard(log: ZkvVerificationLog) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                Icons.Filled.CheckCircle,
                contentDescription = null,
                tint = SafeGreen,
                modifier = Modifier.size(24.dp)
            )
            Spacer(modifier = Modifier.width(10.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = "${log.hospitalName} (${log.latencyMs} ms)",
                    style = MaterialTheme.typography.bodySmall,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = "Hash: ${log.anonymizedPatientHash} | ${log.timestamp}",
                    style = MaterialTheme.typography.bodySmall.copy(
                        fontSize = 10.sp,
                        fontFamily = FontFamily.Monospace
                    ),
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
            Surface(
                shape = RoundedCornerShape(8.dp),
                color = log.tierAwarded.color.copy(alpha = 0.2f)
            ) {
                Text(
                    text = log.tierAwarded.emoji,
                    modifier = Modifier.padding(6.dp),
                    fontSize = 14.sp
                )
            }
        }
    }
}

package com.example.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.HealthPulseDataSource
import com.example.model.*
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.format.DateTimeFormatter

enum class AppTab(val title: String, val iconName: String) {
    DISCOVERY("Discovery", "Favorite"),
    MARKETPLACE("E-Vouchers", "LocalHospital"),
    MY_BADGE("My Badge", "VerifiedUser"),
    ZKV_GATEWAY("ZKV & FHIR", "Security")
}

data class HealthPulseUiState(
    val currentTab: AppTab = AppTab.DISCOVERY,
    val profiles: List<UserProfile> = HealthPulseDataSource.mockProfiles,
    val currentProfileIndex: Int = 0,
    val tierFilter: BadgeTier? = null,
    val matchedProfile: UserProfile? = null,
    val isMatchDialogOpen: Boolean = false,

    // Current User Health Status
    val myTier: BadgeTier = BadgeTier.SILVER,
    val myLastTestDate: String = "2026-07-28",
    val myHospital: String = "BDMS Wellness Clinic",
    val daysUntilTierDecay: Int = 142,
    val isPdpaConsentGranted: Boolean = true,

    // E-Voucher Marketplace
    val packages: List<EVoucherPackage> = HealthPulseDataSource.voucherPackages,
    val partnerHospitals: List<PartnerHospital> = HealthPulseDataSource.partnerHospitals,
    val selectedPackageForPurchase: EVoucherPackage? = null,
    val selectedHospitalForPurchase: PartnerHospital = HealthPulseDataSource.partnerHospitals[0],
    val activePurchasedVoucher: PurchasedVoucher? = null,
    val showVoucherQrDialog: Boolean = false,

    // ZKV & FHIR Simulation
    val selectedFhirHospital: PartnerHospital = HealthPulseDataSource.partnerHospitals[0],
    val isZkvVerifying: Boolean = false,
    val zkvResult: ZkvVerificationLog? = null,
    val fhirVerificationLogs: List<ZkvVerificationLog> = emptyList(),
    val pdpaExplicitConsentChecked: Boolean = true,
    val pdpaDataMinimizationChecked: Boolean = true
)

class HealthPulseViewModel : ViewModel() {

    private val _uiState = MutableStateFlow(HealthPulseUiState())
    val uiState: StateFlow<HealthPulseUiState> = _uiState.asStateFlow()

    fun setTab(tab: AppTab) {
        _uiState.update { it.copy(currentTab = tab) }
    }

    fun setTierFilter(tier: BadgeTier?) {
        _uiState.update { it.copy(tierFilter = tier, currentProfileIndex = 0) }
    }

    fun likeCurrentProfile() {
        val filtered = getFilteredProfiles()
        val currentIndex = _uiState.value.currentProfileIndex
        if (currentIndex < filtered.size) {
            val profile = filtered[currentIndex]
            // If liked profile is verified and user is verified, trigger Match Dialog!
            if (profile.badgeTier != BadgeTier.UNVERIFIED && _uiState.value.myTier != BadgeTier.UNVERIFIED) {
                _uiState.update {
                    it.copy(
                        matchedProfile = profile,
                        isMatchDialogOpen = true,
                        currentProfileIndex = (currentIndex + 1).coerceAtMost(filtered.size)
                    )
                }
            } else {
                _uiState.update {
                    it.copy(currentProfileIndex = (currentIndex + 1).coerceAtMost(filtered.size))
                }
            }
        }
    }

    fun passCurrentProfile() {
        val filtered = getFilteredProfiles()
        val currentIndex = _uiState.value.currentProfileIndex
        if (currentIndex < filtered.size) {
            _uiState.update {
                it.copy(currentProfileIndex = (currentIndex + 1).coerceAtMost(filtered.size))
            }
        }
    }

    fun rewindProfile() {
        val currentIndex = _uiState.value.currentProfileIndex
        if (currentIndex > 0) {
            _uiState.update { it.copy(currentProfileIndex = currentIndex - 1) }
        }
    }

    fun dismissMatchDialog() {
        _uiState.update { it.copy(isMatchDialogOpen = false, matchedProfile = null) }
    }

    // E-Voucher Purchase
    fun openPurchaseDialog(pkg: EVoucherPackage) {
        _uiState.update { it.copy(selectedPackageForPurchase = pkg) }
    }

    fun selectHospital(hospital: PartnerHospital) {
        _uiState.update { it.copy(selectedHospitalForPurchase = hospital) }
    }

    fun dismissPurchaseDialog() {
        _uiState.update { it.copy(selectedPackageForPurchase = null) }
    }

    fun confirmPurchaseVoucher() {
        val pkg = _uiState.value.selectedPackageForPurchase ?: return
        val hospital = _uiState.value.selectedHospitalForPurchase
        val today = LocalDate.now().format(DateTimeFormatter.ISO_LOCAL_DATE)
        val expire = LocalDate.now().plusMonths(6).format(DateTimeFormatter.ISO_LOCAL_DATE)
        val token = "HP-VCH-${(100000..999999).random()}-${hospital.id.uppercase()}"

        val voucher = PurchasedVoucher(
            voucherId = token,
            packageTitle = pkg.title,
            tier = pkg.badgeTierUnlocked,
            hospitalName = hospital.name,
            qrToken = token,
            purchaseDate = today,
            expireDate = expire
        )

        _uiState.update {
            it.copy(
                selectedPackageForPurchase = null,
                activePurchasedVoucher = voucher,
                showVoucherQrDialog = true,
                // Upgrade current user status upon completing screening test
                myTier = pkg.badgeTierUnlocked,
                myHospital = hospital.name,
                myLastTestDate = today,
                daysUntilTierDecay = 180
            )
        }
    }

    fun dismissVoucherQrDialog() {
        _uiState.update { it.copy(showVoucherQrDialog = false) }
    }

    // Tier Decay simulation
    fun simulateTierDecay() {
        _uiState.update {
            it.copy(
                myTier = BadgeTier.UNVERIFIED,
                daysUntilTierDecay = 0
            )
        }
    }

    fun restoreTierToSilver() {
        _uiState.update {
            it.copy(
                myTier = BadgeTier.SILVER,
                daysUntilTierDecay = 142,
                myLastTestDate = "2026-07-28"
            )
        }
    }

    // ZKV & HL7 FHIR API Testing
    fun selectFhirHospital(hospital: PartnerHospital) {
        _uiState.update { it.copy(selectedFhirHospital = hospital) }
    }

    fun togglePdpaConsent(explicit: Boolean) {
        _uiState.update { it.copy(pdpaExplicitConsentChecked = explicit) }
    }

    fun toggleDataMinimization(checked: Boolean) {
        _uiState.update { it.copy(pdpaDataMinimizationChecked = checked) }
    }

    fun runZkvVerification() {
        viewModelScope.launch {
            _uiState.update { it.copy(isZkvVerifying = true) }
            delay(1200) // Simulate OAuth 2.0 handshake and HL7 FHIR query

            val hospital = _uiState.value.selectedFhirHospital
            val hash = "0x" + (1..16).map { ('a'..'f') + ('0'..'9') }.flatten().shuffled().take(16).joinToString("")
            val newLog = ZkvVerificationLog(
                timestamp = "2026-09-18 10:45:22",
                hospitalName = hospital.name,
                anonymizedPatientHash = hash,
                fhirResourceTested = "DiagnosticReport?category=LAB&code=48676-1",
                isVerified = true,
                tierAwarded = BadgeTier.GOLD_PLATINUM,
                windowPeriodDisclaimer = "สถานพยาบาลส่งกลับเฉพาะค่า Boolean Logic ตามหลัก ZKV เซิร์ฟเวอร์ไม่เก็บเวชระเบียน",
                latencyMs = 284
            )

            _uiState.update {
                it.copy(
                    isZkvVerifying = false,
                    zkvResult = newLog,
                    fhirVerificationLogs = listOf(newLog) + it.fhirVerificationLogs,
                    myTier = BadgeTier.GOLD_PLATINUM,
                    daysUntilTierDecay = 180,
                    myHospital = hospital.name
                )
            }
        }
    }

    fun getFilteredProfiles(): List<UserProfile> {
        val filter = _uiState.value.tierFilter
        return if (filter == null) {
            _uiState.value.profiles
        } else {
            _uiState.value.profiles.filter { it.badgeTier == filter }
        }
    }
}

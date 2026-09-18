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
    CHATS("Chats", "ChatBubble"),
    MARKETPLACE("E-Vouchers", "LocalHospital"),
    MY_BADGE("My Badge", "VerifiedUser"),
    ZKV_GATEWAY("ZKV & FHIR", "Security")
}

data class HealthPulseUiState(
    val currentTab: AppTab = AppTab.DISCOVERY,
    val language: AppLanguage = AppLanguage.TH,
    val profiles: List<UserProfile> = HealthPulseDataSource.mockProfiles,
    val currentProfileIndex: Int = 0,
    val tierFilter: BadgeTier? = null,
    val matchedProfile: UserProfile? = null,
    val isMatchDialogOpen: Boolean = false,

    // Chat & Matching State
    val matchedProfilesList: List<UserProfile> = listOf(
        HealthPulseDataSource.mockProfiles[0], // หมอแพรว
        HealthPulseDataSource.mockProfiles[2]  // มายด์
    ),
    val conversations: List<ChatConversation> = HealthPulseDataSource.initialConversations,
    val activeChatConversation: ChatConversation? = null,
    val chatDraftMessage: String = "",
    val isPartnerTyping: Boolean = false,

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

    fun toggleLanguage() {
        _uiState.update {
            val newLang = if (it.language == AppLanguage.TH) AppLanguage.EN else AppLanguage.TH
            it.copy(language = newLang)
        }
    }

    fun setLanguage(language: AppLanguage) {
        _uiState.update { it.copy(language = language) }
    }

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
                val currentMatches = _uiState.value.matchedProfilesList.toMutableList()
                if (currentMatches.none { it.id == profile.id }) {
                    currentMatches.add(0, profile)
                }
                _uiState.update {
                    it.copy(
                        matchedProfile = profile,
                        matchedProfilesList = currentMatches,
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

    // Chat Management
    fun startChatWithMatchedProfile() {
        val matched = _uiState.value.matchedProfile ?: return
        openChatWithProfile(matched)
        _uiState.update {
            it.copy(
                isMatchDialogOpen = false,
                matchedProfile = null,
                currentTab = AppTab.CHATS
            )
        }
    }

    fun openChatWithProfile(profile: UserProfile) {
        val currentConvs = _uiState.value.conversations.toMutableList()
        var existing = currentConvs.find { it.partnerProfile.id == profile.id }
        if (existing == null) {
            val clinicName = profile.partnerClinicName ?: "รพ.พันธมิตรเครือข่าย HIS"
            val newConv = ChatConversation(
                conversationId = "conv_${profile.id}",
                partnerProfile = profile,
                messages = listOf(
                    ChatMessage(
                        id = "msg_sys_${System.currentTimeMillis()}",
                        senderId = "system",
                        text = "🔒 Mutual Health Verification: คุณ (${_uiState.value.myTier.shortName}) และ ${profile.name} (${profile.badgeTier.shortName}) ต่างผ่านการตรวจสอบผลแล็บทางเวชระเบียนจาก $clinicName ปลอดภัย 100%",
                        timestamp = "เมื่อสักครู่",
                        isFromMe = false,
                        isSystemSafetyCard = true
                    ),
                    ChatMessage(
                        id = "msg_intro_${System.currentTimeMillis()}",
                        senderId = profile.id,
                        text = if (_uiState.value.language == AppLanguage.TH)
                            "ยินดีที่ได้แมตช์นะคะ! ดีใจที่เจอคนที่ให้ความสำคัญกับสุขภาพและความปลอดภัยเหมือนกัน ✨"
                        else
                            "Great to match! Love seeing someone who values health and transparency ✨",
                        timestamp = "เมื่อสักครู่",
                        isFromMe = false
                    )
                ),
                unreadCount = 0,
                matchedDate = "วันนี้"
            )
            currentConvs.add(0, newConv)
            existing = newConv
        } else {
            existing = existing.copy(unreadCount = 0)
            val index = currentConvs.indexOfFirst { it.conversationId == existing.conversationId }
            if (index >= 0) {
                currentConvs[index] = existing
            }
        }

        _uiState.update {
            it.copy(
                conversations = currentConvs,
                activeChatConversation = existing,
                chatDraftMessage = "",
                currentTab = AppTab.CHATS
            )
        }
    }

    fun openChatConversation(conv: ChatConversation) {
        val updatedConvs = _uiState.value.conversations.map {
            if (it.conversationId == conv.conversationId) it.copy(unreadCount = 0) else it
        }
        val target = updatedConvs.find { it.conversationId == conv.conversationId } ?: conv
        _uiState.update {
            it.copy(
                conversations = updatedConvs,
                activeChatConversation = target,
                chatDraftMessage = ""
            )
        }
    }

    fun closeChatRoom() {
        _uiState.update { it.copy(activeChatConversation = null) }
    }

    fun setChatDraft(text: String) {
        _uiState.update { it.copy(chatDraftMessage = text) }
    }

    fun sendCurrentChatMessage() {
        val draft = _uiState.value.chatDraftMessage.trim()
        if (draft.isEmpty()) return
        sendMessage(draft)
    }

    fun sendSafeDateProposal() {
        val active = _uiState.value.activeChatConversation ?: return
        val text = if (_uiState.value.language == AppLanguage.TH)
            "☕ ชวนไปเดตปลอดภัยที่คาเฟ่ Specialty ในพื้นที่เปิดโล่ง บรรยากาศสบายใจและปลอดภัยทั้งสองฝ่าย"
        else
            "☕ Proposed a public safe date at a specialty cafe with open, relaxed ambiance."
        sendMessage(text = text, isProposal = true)
    }

    fun sendMessage(text: String, isProposal: Boolean = false) {
        val active = _uiState.value.activeChatConversation ?: return
        val myMessage = ChatMessage(
            id = "msg_me_${System.currentTimeMillis()}",
            senderId = "me",
            text = text,
            timestamp = "เมื่อสักครู่",
            isFromMe = true,
            isSafeDateProposal = isProposal
        )

        val updatedMessages = active.messages + myMessage
        val updatedConv = active.copy(
            messages = updatedMessages,
            unreadCount = 0
        )

        val updatedConvs = _uiState.value.conversations.map {
            if (it.conversationId == active.conversationId) updatedConv else it
        }

        _uiState.update {
            it.copy(
                chatDraftMessage = "",
                activeChatConversation = updatedConv,
                conversations = updatedConvs
            )
        }

        // Simulate partner response
        viewModelScope.launch {
            delay(400)
            _uiState.update { it.copy(isPartnerTyping = true) }
            delay(1200)
            _uiState.update { it.copy(isPartnerTyping = false) }

            val replyText = if (isProposal) {
                if (_uiState.value.language == AppLanguage.TH) {
                    "ยินดีมากๆ ค่ะ! ชอบนัดเจอที่คาเฟ่เปิดโล่งแบบนี้ สบายใจและปลอดภัยดีค่ะ เดี๋ยวส่งพิกัดร้านโปรดแถวนั้นให้นะคะ ☕✨"
                } else {
                    "I'd love that! Meeting in a bright public cafe sounds super comfortable and safe. Let me pick a nice spot! ☕✨"
                }
            } else {
                when {
                    text.contains("วิ่ง", ignoreCase = true) || text.contains("run", ignoreCase = true) -> {
                        if (_uiState.value.language == AppLanguage.TH) "ใช่เลยค่ะ ปกติวิ่งเพซสบายๆ 6:30 อากาศดีมาก ไว้ไปวิ่งด้วยกันนะคะ 🏃‍♀️" else "Yes! I usually jog at a relaxed pace, great weather lately! 🏃‍♀️"
                    }
                    text.contains("กาแฟ", ignoreCase = true) || text.contains("coffee", ignoreCase = true) || text.contains("cafe", ignoreCase = true) -> {
                        if (_uiState.value.language == AppLanguage.TH) "มีร้านแถวสุขุมวิทกาแฟดีมาก เมล็ดคั่วกลางหอมกลิ่นดอกไม้ ชวนไปชิมได้เลยค่ะ ☕" else "I know a cozy spot with superb pour-over coffee, would love to show you! ☕"
                    }
                    text.contains("ตรวจ", ignoreCase = true) || text.contains("health", ignoreCase = true) || text.contains("badge", ignoreCase = true) -> {
                        if (_uiState.value.language == AppLanguage.TH) "เห็นด้วยมากๆ ค่ะ การตรวจและเปิดเผยผ่านระบบ ZKV ทำให้คุยกันได้อย่างสบายใจ ไร้กังวลจริงๆ ค่ะ 👍" else "Totally agree! ZKV verification makes dating so refreshing and reassuring 👍"
                    }
                    else -> {
                        if (_uiState.value.language == AppLanguage.TH) {
                            "น่าสนใจมากค่ะ! ยินดีที่ได้คุยกันนะคะ มีอะไรแนะนำหรืออยากแลกเปลี่ยนอีกไหมคะ 😊"
                        } else {
                            "Sounds wonderful! Really enjoying our conversation, what else do you like to do on weekends? 😊"
                        }
                    }
                }
            }

            val partnerMessage = ChatMessage(
                id = "msg_partner_${System.currentTimeMillis()}",
                senderId = active.partnerProfile.id,
                text = replyText,
                timestamp = "เมื่อสักครู่",
                isFromMe = false
            )

            val finalMessages = updatedConv.messages + partnerMessage
            val finalConv = updatedConv.copy(messages = finalMessages)
            val finalConvs = _uiState.value.conversations.map {
                if (it.conversationId == finalConv.conversationId) finalConv else it
            }

            _uiState.update {
                it.copy(
                    activeChatConversation = if (it.activeChatConversation?.conversationId == finalConv.conversationId) finalConv else it.activeChatConversation,
                    conversations = finalConvs
                )
            }
        }
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

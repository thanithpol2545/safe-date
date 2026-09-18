package com.example.model

import androidx.compose.ui.graphics.Color
import com.example.ui.theme.*

enum class BadgeTier(
    val title: String,
    val shortName: String,
    val emoji: String,
    val color: Color,
    val bgColor: Color,
    val description: String,
    val perkSummary: String
) {
    UNVERIFIED(
        title = "Unverified Tier",
        shortName = "ยังไม่ตรวจ",
        emoji = "⚪",
        color = TierUnverifiedColor,
        bgColor = TierUnverifiedBg,
        description = "ผู้ใช้ใหม่หรือผลตรวจหมดอายุเกิน 6 เดือน",
        perkSummary = "จำกัดการปัดขวา 10 ครั้ง/วัน และไม่สามารถดูรายละเอียด Badge ของผู้อื่นได้"
    ),
    BRONZE(
        title = "Bronze Tier",
        shortName = "Bronze",
        emoji = "🥉",
        color = TierBronzeColor,
        bgColor = TierBronzeBg,
        description = "ตรวจคัดกรองพื้นฐาน (HIV Ag/Ab, Syphilis) ในรอบ 6 เดือน",
        perkSummary = "ปลดล็อก Unlimited Likes + Rewind ปัดย้อนกลับได้ไม่จำกัด"
    ),
    SILVER(
        title = "Silver Tier",
        shortName = "Silver",
        emoji = "🥈",
        color = TierSilverColor,
        bgColor = TierSilverBg,
        description = "ตรวจสม่ำเสมอทุก 3–6 เดือนตามคำแนะนำของ CDC",
        perkSummary = "อัลกอริทึม Profile Boost 2x + ฟิลเตอร์ค้นหาเฉพาะ Verified Users"
    ),
    GOLD_PLATINUM(
        title = "Gold / Platinum Tier",
        shortName = "Gold/Platinum",
        emoji = "🥇",
        color = TierGoldColor,
        bgColor = TierGoldBg,
        description = "ตรวจ PCR Multiplex DNA (14–28 เชื้อ) + ฉีดวัคซีน HPV ครบโดส",
        perkSummary = "Priority Match อันดับแรกสุด + กรอบโปรไฟล์ทองคำ + ส่วนลดคลินิกพันธมิตร"
    )
}

data class UserProfile(
    val id: String,
    val name: String,
    val age: Int,
    val occupation: String,
    val location: String,
    val distanceKm: Double,
    val bio: String,
    val avatarBgColor: Color,
    val badgeTier: BadgeTier,
    val lastTestedDate: String?,
    val partnerClinicName: String?,
    val screeningPackageName: String?,
    val interests: List<String>,
    val hpvVaccinated: Boolean = false,
    val pcrMultiplexDone: Boolean = false
)

data class EVoucherPackage(
    val id: String,
    val title: String,
    val badgeTierUnlocked: BadgeTier,
    val priceThb: Int,
    val wholesalePriceThb: Int,
    val platformFeeThb: Int,
    val tag: String,
    val turnaroundTime: String,
    val includedTests: List<String>,
    val clinicalGuidance: String
)

data class PartnerHospital(
    val id: String,
    val name: String,
    val hospitalGroup: String,
    val branches: String,
    val fhirEndpoint: String,
    val rating: Double,
    val isZkvActive: Boolean
)

data class PurchasedVoucher(
    val voucherId: String,
    val packageTitle: String,
    val tier: BadgeTier,
    val hospitalName: String,
    val qrToken: String,
    val purchaseDate: String,
    val expireDate: String,
    var isRedeemed: Boolean = false
)

data class ZkvVerificationLog(
    val timestamp: String,
    val hospitalName: String,
    val anonymizedPatientHash: String,
    val fhirResourceTested: String,
    val isVerified: Boolean,
    val tierAwarded: BadgeTier,
    val windowPeriodDisclaimer: String,
    val latencyMs: Long
)

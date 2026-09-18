package com.example.model

enum class AppLanguage {
    TH,
    EN
}

object AppStrings {
    // Top Bar & Branding
    fun appSubheader(lang: AppLanguage) = when (lang) {
        AppLanguage.TH -> "Hospital Verified Health x ZKV"
        AppLanguage.EN -> "Hospital Verified Health x ZKV"
    }

    // Tabs
    fun tabDiscovery(lang: AppLanguage) = when (lang) {
        AppLanguage.TH -> "หน้าหาคู่"
        AppLanguage.EN -> "Discovery"
    }
    fun tabChats(lang: AppLanguage) = when (lang) {
        AppLanguage.TH -> "แชท & แมตช์"
        AppLanguage.EN -> "Chats"
    }
    fun tabMarketplace(lang: AppLanguage) = when (lang) {
        AppLanguage.TH -> "แพ็กเกจตรวจ"
        AppLanguage.EN -> "E-Vouchers"
    }
    fun tabMyBadge(lang: AppLanguage) = when (lang) {
        AppLanguage.TH -> "Badge ของฉัน"
        AppLanguage.EN -> "My Badge"
    }
    fun tabZkvGateway(lang: AppLanguage) = when (lang) {
        AppLanguage.TH -> "ระบบ ZKV & FHIR"
        AppLanguage.EN -> "ZKV & FHIR"
    }

    // Discovery Screen
    fun filterAll(lang: AppLanguage) = when (lang) {
        AppLanguage.TH -> "ทั้งหมด"
        AppLanguage.EN -> "All"
    }
    fun filterVerifiedOnly(lang: AppLanguage) = when (lang) {
        AppLanguage.TH -> "เฉพาะที่ตรวจแล้ว"
        AppLanguage.EN -> "Verified Only"
    }
    fun kmAway(km: Double, lang: AppLanguage) = when (lang) {
        AppLanguage.TH -> "$km กม."
        AppLanguage.EN -> "$km km away"
    }
    fun verifiedHealthBadge(lang: AppLanguage) = when (lang) {
        AppLanguage.TH -> "ผลตรวจผ่านการรับรองจาก รพ."
        AppLanguage.EN -> "Hospital Verified Health"
    }
    fun verifiedAt(hospital: String, date: String, lang: AppLanguage) = when (lang) {
        AppLanguage.TH -> "ตรวจ ณ $hospital (เมื่อ $date)"
        AppLanguage.EN -> "Verified at $hospital ($date)"
    }
    fun verifiedPackage(pkg: String, lang: AppLanguage) = when (lang) {
        AppLanguage.TH -> "แพ็กเกจ: $pkg"
        AppLanguage.EN -> "Package: $pkg"
    }
    fun pcrMultiplexDone(lang: AppLanguage) = when (lang) {
        AppLanguage.TH -> "ตรวจ PCR Multiplex DNA ครบถ้วน"
        AppLanguage.EN -> "PCR Multiplex DNA Verified"
    }
    fun hpvVaccinated(lang: AppLanguage) = when (lang) {
        AppLanguage.TH -> "ฉีดวัคซีน HPV ป้องกันมะเร็งครบโดส"
        AppLanguage.EN -> "HPV Vaccinated (Full Course)"
    }
    fun unverifiedNotice(lang: AppLanguage) = when (lang) {
        AppLanguage.TH -> "ผู้ใช้นี้ยังไม่มีข้อมูลผลตรวจในระบบ Safe Date หรือผลตรวจเดิมหมดอายุเกิน 6 เดือน"
        AppLanguage.EN -> "No active verification record on Safe Date or previous test has expired (> 6 months)."
    }
    fun aboutMe(lang: AppLanguage) = when (lang) {
        AppLanguage.TH -> "เกี่ยวกับฉัน"
        AppLanguage.EN -> "About Me"
    }
    fun interests(lang: AppLanguage) = when (lang) {
        AppLanguage.TH -> "ความสนใจ"
        AppLanguage.EN -> "Interests"
    }
    fun emptyDiscoveryTitle(lang: AppLanguage) = when (lang) {
        AppLanguage.TH -> "ดูโปรไฟล์ครบในฟิลเตอร์นี้แล้ว"
        AppLanguage.EN -> "You've seen all profiles here"
    }
    fun emptyDiscoverySub(hasFilter: Boolean, tierName: String, lang: AppLanguage) = when (lang) {
        AppLanguage.TH -> if (hasFilter) "ลองรีเซ็ตฟิลเตอร์ $tierName เพื่อดูโปรไฟล์อื่นๆ" else "ระบบจะแนะนำโปรไฟล์ใหม่เมื่อมีผู้เข้ารับการตรวจเพิ่มขึ้น"
        AppLanguage.EN -> if (hasFilter) "Try resetting the $tierName filter to explore more profiles." else "New profiles will appear as more members complete clinical screening."
    }
    fun resetFilterButton(lang: AppLanguage) = when (lang) {
        AppLanguage.TH -> "ดูโปรไฟล์ทั้งหมด"
        AppLanguage.EN -> "View All Profiles"
    }

    // Match Dialog
    fun matchSuccessTitle(lang: AppLanguage) = when (lang) {
        AppLanguage.TH -> "🎉 IT'S A SAFE MATCH! 🎉"
        AppLanguage.EN -> "🎉 IT'S A SAFE MATCH! 🎉"
    }
    fun matchSuccessBody(name: String, lang: AppLanguage) = when (lang) {
        AppLanguage.TH -> "คุณและ $name แมตช์กันสำเร็จ!"
        AppLanguage.EN -> "You and $name matched!"
    }
    fun matchSuccessHealthNote(lang: AppLanguage) = when (lang) {
        AppLanguage.TH -> "ทั้งสองฝ่ายผ่านการยืนยันสุขภาพจากสถานพยาบาลพันธมิตรด้วยมาตรฐานความปลอดภัยสูง"
        AppLanguage.EN -> "Both members are clinically verified by partner medical centers under ZKV privacy."
    }
    fun startChatButton(lang: AppLanguage) = when (lang) {
        AppLanguage.TH -> "เริ่มบทสนทนาอย่างมั่นใจ"
        AppLanguage.EN -> "Start Safe Conversation"
    }
    fun keepSwipingButton(lang: AppLanguage) = when (lang) {
        AppLanguage.TH -> "ดูโปรไฟล์ต่อไป"
        AppLanguage.EN -> "Keep Swiping"
    }

    // Chat & Messages Screen
    fun newMatchesHeader(lang: AppLanguage) = when (lang) {
        AppLanguage.TH -> "แมตช์ที่ยืนยันผลตรวจแล้ว (Safe Matches)"
        AppLanguage.EN -> "Verified Safe Matches"
    }
    fun conversationsHeader(lang: AppLanguage) = when (lang) {
        AppLanguage.TH -> "ข้อความสนทนา"
        AppLanguage.EN -> "Messages"
    }
    fun noChatsYetTitle(lang: AppLanguage) = when (lang) {
        AppLanguage.TH -> "ยังไม่มีบทสนทนา"
        AppLanguage.EN -> "No Conversations Yet"
    }
    fun noChatsYetSub(lang: AppLanguage) = when (lang) {
        AppLanguage.TH -> "ปัดถูกใจโปรไฟล์ที่ผ่านการตรวจแล็บเพื่อเริ่มต้นแมตช์และเปิดห้องแชทที่ปลอดภัย!"
        AppLanguage.EN -> "Swipe right on verified profiles to match and open a secure, transparent chat room!"
    }
    fun chatMutualSafetyTitle(lang: AppLanguage) = when (lang) {
        AppLanguage.TH -> "🔒 Mutual Health Verification (ผลแล็บปลอดภัยสองฝ่าย)"
        AppLanguage.EN -> "🔒 Mutual Health Verification (Both Clinically Certified)"
    }
    fun chatMutualSafetyDesc(myTier: String, partnerTier: String, partnerClinic: String, lang: AppLanguage) = when (lang) {
        AppLanguage.TH -> "คุณ ($myTier) และคู่เดต ($partnerTier) ได้รับการยืนยันผลแล็บผ่าน FHIR HIS จาก $partnerClinic ปราศจากการเปิดเผยเวชระเบียนส่วนบุคคล"
        AppLanguage.EN -> "You ($myTier) and partner ($partnerTier) are verified via FHIR HIS from $partnerClinic with zero sensitive medical disclosures."
    }
    fun chatInputPlaceholder(lang: AppLanguage) = when (lang) {
        AppLanguage.TH -> "พิมพ์ข้อความที่สุภาพและปลอดภัย..."
        AppLanguage.EN -> "Type a respectful, safe message..."
    }
    fun chatSafeDateProposeButton(lang: AppLanguage) = when (lang) {
        AppLanguage.TH -> "📍 เสนอนัดพบปลอดภัย (Safe Date)"
        AppLanguage.EN -> "📍 Propose Public Safe Date"
    }
    fun chatSafeDateCardTitle(lang: AppLanguage) = when (lang) {
        AppLanguage.TH -> "☕ ชวนนัดพบที่ Specialty Cafe (พื้นที่เปิด ปลอดภัย)"
        AppLanguage.EN -> "☕ Public Specialty Cafe Date Proposal"
    }
    fun chatSafeDateCardDesc(lang: AppLanguage) = when (lang) {
        AppLanguage.TH -> "เสนอนัดพบในสถานที่สาธารณะ แสงสว่างเพียงพอ ปลอดภัยและสบายใจต่อทั้งสองฝ่าย"
        AppLanguage.EN -> "Meet in a bright, public specialty cafe. Relaxed, open, and secure for both parties."
    }
    fun chatOnlineStatus(lang: AppLanguage) = when (lang) {
        AppLanguage.TH -> "ออนไลน์อยู่"
        AppLanguage.EN -> "Active now"
    }
    fun backToChats(lang: AppLanguage) = when (lang) {
        AppLanguage.TH -> "กล่องข้อความ"
        AppLanguage.EN -> "Chats"
    }
    fun icebreakerTitle(lang: AppLanguage) = when (lang) {
        AppLanguage.TH -> "💡 ประโยคเปิดบทสนทนายอดนิยม:"
        AppLanguage.EN -> "💡 Suggested Safe Icebreakers:"
    }
    fun icebreaker1(lang: AppLanguage) = when (lang) {
        AppLanguage.TH -> "สวัสดีครับ! ยินดีที่ได้แมตช์ ดีใจที่เจอคนที่ให้ความสำคัญกับสุขภาพเหมือนกัน ✨"
        AppLanguage.EN -> "Hi! Great to match, really respect that you value sexual health and transparency ✨"
    }
    fun icebreaker2(lang: AppLanguage) = when (lang) {
        AppLanguage.TH -> "เห็นในโปรไฟล์ชอบวิ่งเหมือนกัน ซ้อมที่ไหนเป็นประจำเหรอครับ? 🏃"
        AppLanguage.EN -> "Saw on your profile that you love running too, where do you train? 🏃"
    }
    fun icebreaker3(lang: AppLanguage) = when (lang) {
        AppLanguage.TH -> "วันหยุดนี้ไปคาเฟ่ร้านไหนดี มีร้าน Dirty หรือ Specialty แนะนำไหมครับ? ☕"
        AppLanguage.EN -> "Any favorite coffee spots around town? Up for a specialty cafe this weekend? ☕"
    }
    fun activeChatWith(name: String, lang: AppLanguage) = when (lang) {
        AppLanguage.TH -> "แชทกับ $name"
        AppLanguage.EN -> "Chat with $name"
    }
    fun verifiedBadgeChipLabel(tierName: String, lang: AppLanguage) = when (lang) {
        AppLanguage.TH -> "ยืนยันผล $tierName"
        AppLanguage.EN -> "$tierName Verified"
    }

    // My Badge Screen
    fun myHealthBadgeTitle(lang: AppLanguage) = when (lang) {
        AppLanguage.TH -> "สถานะสุขภาพและอายุ Badge ของฉัน"
        AppLanguage.EN -> "My Verified Health Status & Validity"
    }
    fun myHealthStatusTitle(lang: AppLanguage) = when (lang) {
        AppLanguage.TH -> "สถานะสุขภาพและป้าย Badge ของฉัน"
        AppLanguage.EN -> "My Health Status & Verified Badge"
    }
    fun myHealthStatusSub(lang: AppLanguage) = when (lang) {
        AppLanguage.TH -> "อ้างอิงประวัติการตรวจในรอบ 6 เดือน"
        AppLanguage.EN -> "Based on clinical records in the past 6 months"
    }
    fun tierDecayClock(lang: AppLanguage) = when (lang) {
        AppLanguage.TH -> "อายุของผลตรวจ (Tier Decay Clock)"
        AppLanguage.EN -> "Screening Validity (Tier Decay Clock)"
    }
    fun daysRemaining(days: Int, lang: AppLanguage) = when (lang) {
        AppLanguage.TH -> if (days > 0) "เหลือ $days / 180 วัน" else "หมดอายุ (0 วัน)"
        AppLanguage.EN -> if (days > 0) "$days / 180 days remaining" else "Expired (0 days)"
    }
    fun renewTestButton(lang: AppLanguage) = when (lang) {
        AppLanguage.TH -> "ต่ออายุตรวจซ้ำ"
        AppLanguage.EN -> "Renew Test"
    }
    fun upgradeBadgeAction(lang: AppLanguage) = when (lang) {
        AppLanguage.TH -> "ตรวจสุขภาพเพื่ออัปเกรด Badge กับ รพ. พันธมิตร"
        AppLanguage.EN -> "Screen at Partner Hospital to Upgrade Badge"
    }
    fun decayWhyTitle(lang: AppLanguage) = when (lang) {
        AppLanguage.TH -> "หลักการทางระบาดวิทยา: ทำไมต้องมี Tier Decay?"
        AppLanguage.EN -> "Clinical Epidemiology: Why 180-Day Tier Decay?"
    }
    fun decayWhyBody(lang: AppLanguage) = when (lang) {
        AppLanguage.TH -> "1. พฤติกรรมทางเพศเปลี่ยนแปลงตามกาลเวลา ผลตรวจเลือดในอดีตไม่สามารถรับประกันความเสี่ยงในปัจจุบันได้\n" +
                "2. คำแนะนำของ CDC (ศูนย์ควบคุมโรคสหรัฐฯ) กำหนดให้ผู้มีเพศสัมพันธ์คัดกรองทุก 3-6 เดือน\n" +
                "3. กลไก 180-Day Decay ช่วยรักษาความน่าเชื่อถือและความปลอดภัยสูงสุดของคอมมูนิตี้ Safe Date"
        AppLanguage.EN -> "1. Sexual activity is dynamic over time; historical blood tests cannot guarantee current safety.\n" +
                "2. CDC guidelines strongly recommend STI screening every 3–6 months for active individuals.\n" +
                "3. The 180-day decay protocol maintains peer trust and genuine safety across the Safe Date community."
    }
    fun badgeTiersAndPerks(lang: AppLanguage) = when (lang) {
        AppLanguage.TH -> "ระดับ Badge และสิทธิประโยชน์บน Safe Date"
        AppLanguage.EN -> "Badge Tiers & Exclusive Dating Perks"
    }
    fun yourCurrentTier(lang: AppLanguage) = when (lang) {
        AppLanguage.TH -> "ระดับของคุณ"
        AppLanguage.EN -> "Your Current Tier"
    }
    fun verifiedHospitalLabel(hospital: String, lang: AppLanguage) = when (lang) {
        AppLanguage.TH -> "สถานพยาบาลที่ตรวจ: $hospital"
        AppLanguage.EN -> "Verified Facility: $hospital"
    }
    fun lastTestDateLabel(date: String, lang: AppLanguage) = when (lang) {
        AppLanguage.TH -> "วันที่ตรวจล่าสุด: $date"
        AppLanguage.EN -> "Latest Screening Date: $date"
    }
    fun tierDecayTimerLabel(days: Int, lang: AppLanguage) = when (lang) {
        AppLanguage.TH -> "อายุผลตรวจคงเหลือ: $days วัน (จาก 180 วัน)"
        AppLanguage.EN -> "Screening Validity: $days days left (of 180)"
    }
    fun tierDecayExpiredLabel(lang: AppLanguage) = when (lang) {
        AppLanguage.TH -> "⚠️ ผลตรวจหมดอายุแล้ว กรุณาตรวจซ้ำเพื่อต่ออายุ Badge"
        AppLanguage.EN -> "⚠️ Screening expired. Renew now to restore your badge."
    }
    fun simulateDecayButton(lang: AppLanguage) = when (lang) {
        AppLanguage.TH -> "จำลอง Badge หมดอายุ"
        AppLanguage.EN -> "Simulate Badge Expired"
    }
    fun restoreTierButton(lang: AppLanguage) = when (lang) {
        AppLanguage.TH -> "ต่ออายุผลตรวจซ้ำ"
        AppLanguage.EN -> "Renew Screening"
    }
    fun renewAtHospitalButton(lang: AppLanguage) = when (lang) {
        AppLanguage.TH -> "เลือกแพ็กเกจตรวจกับ รพ. พันธมิตร"
        AppLanguage.EN -> "Book Hospital Screening Package"
    }
    fun epidemiologyTitle(lang: AppLanguage) = when (lang) {
        AppLanguage.TH -> "ทำไมผลตรวจต้องมีอายุ 180 วัน? (หลักระบาดวิทยา)"
        AppLanguage.EN -> "Why 180-Day Decay? (Clinical Epidemiology)"
    }
    fun epidemiologyDesc(lang: AppLanguage) = when (lang) {
        AppLanguage.TH -> "ศูนย์ควบคุมและป้องกันโรค (CDC) แนะนำให้ผู้ที่มีเพศสัมพันธ์ตรวจคัดกรองทุก 3-6 เดือน เนื่องจากการมีเพศสัมพันธ์ใหม่หรือพฤติกรรมเสี่ยงอาจเกิดขึ้นได้หลังการตรวจ Safe Date จึงใช้อายุผลตรวจ 180 วันเพื่อความปลอดภัยร่วมกันอย่างแท้จริง"
        AppLanguage.EN -> "CDC guidelines recommend STI screening every 3–6 months for sexually active individuals. Because new encounters or risk factors can happen over time, Safe Date enforces an honest 180-day cycle to maintain genuine peer trust."
    }
    fun tierPerksTitle(lang: AppLanguage) = when (lang) {
        AppLanguage.TH -> "คู่มือระดับ Badge และสิทธิประโยชน์"
        AppLanguage.EN -> "Badge Tier Guide & Matchmaking Perks"
    }

    // Marketplace Screen
    fun marketplaceTitle(lang: AppLanguage) = when (lang) {
        AppLanguage.TH -> "E-Voucher ตรวจสุขภาพกับ รพ. พันธมิตร"
        AppLanguage.EN -> "Hospital Partner E-Vouchers"
    }
    fun marketplacePackagesSection(lang: AppLanguage) = when (lang) {
        AppLanguage.TH -> "แพ็กเกจตรวจคัดกรองสุขภาพทางเพศ (E-Vouchers)"
        AppLanguage.EN -> "STI Screening E-Voucher Packages"
    }
    fun marketplaceHospitalsSection(lang: AppLanguage) = when (lang) {
        AppLanguage.TH -> "เครือข่ายสถานพยาบาลพันธมิตร (HIS Connected)"
        AppLanguage.EN -> "Partner Hospital Network (HIS Connected)"
    }
    fun ecosystemBannerTitle(lang: AppLanguage) = when (lang) {
        AppLanguage.TH -> "เปลี่ยนงบ Subscription สู่สุขภาพจริง"
        AppLanguage.EN -> "Turn Subscription Costs into Real Health"
    }
    fun ecosystemBannerSub(lang: AppLanguage) = when (lang) {
        AppLanguage.TH -> "จาก Subscription Fatigue สู่ Health Gamification"
        AppLanguage.EN -> "From Subscription Fatigue to Health Gamification"
    }
    fun ecosystemBannerBody(lang: AppLanguage) = when (lang) {
        AppLanguage.TH -> "แทนที่จะจ่ายค่าสมาชิกแอปรายเดือนโดยเปล่าประโยชน์ Safe Date ให้คุณเปลี่ยนเป็น E-Voucher ตรวจเลือดที่โรงพยาบาล พร้อมปลดล็อกป้าย Verified Badge สัญลักษณ์แห่งความใส่ใจและความรับผิดชอบต่อคู่เดต"
        AppLanguage.EN -> "Instead of paying monthly subscription fees that vanish, Safe Date lets you convert that spend into certified clinic screening E-Vouchers, unlocking your Verified Badge to show genuine care and safety."
    }
    fun medicalCouncilTitle(lang: AppLanguage) = when (lang) {
        AppLanguage.TH -> "สอดคล้องข้อบังคับแพทยสภา (No Brokerage Fee)"
        AppLanguage.EN -> "Medical Council Compliant (Zero Brokerage Fee)"
    }
    fun medicalCouncilBody(lang: AppLanguage) = when (lang) {
        AppLanguage.TH -> "โครงสร้างรายได้โปร่งใสผ่านโมเดล E-Voucher Marketplace และ B2B SaaS Platform Fee ไม่มีการคิดค่านายหน้าหรือส่วนแบ่งค่ารักษาพยาบาลวิชาชีพเวชกรรมตามกฎหมายไทย"
        AppLanguage.EN -> "Transparent revenue model via E-Voucher Marketplace and B2B SaaS platform fees. Zero referral fee or medical commission under Thai Medical Council regulations."
    }
    fun costLab(lang: AppLanguage) = when (lang) {
        AppLanguage.TH -> "ค่าแล็บสถานพยาบาล"
        AppLanguage.EN -> "Hospital Lab Cost"
    }
    fun platformFee(lang: AppLanguage) = when (lang) {
        AppLanguage.TH -> "Platform Fee"
        AppLanguage.EN -> "Platform Tech Fee"
    }
    fun buyVoucherButton(lang: AppLanguage) = when (lang) {
        AppLanguage.TH -> "ซื้อ E-Voucher และรับสิทธิ์"
        AppLanguage.EN -> "Get E-Voucher & Unlock"
    }
    fun confirmPurchaseTitle(lang: AppLanguage) = when (lang) {
        AppLanguage.TH -> "ยืนยันการซื้อ E-Voucher"
        AppLanguage.EN -> "Confirm E-Voucher Purchase"
    }
    fun selectHospitalLabel(lang: AppLanguage) = when (lang) {
        AppLanguage.TH -> "เลือกสถานพยาบาลที่จะเข้ารับการตรวจ:"
        AppLanguage.EN -> "Select Preferred Hospital Branch:"
    }
    fun totalPayment(lang: AppLanguage) = when (lang) {
        AppLanguage.TH -> "ยอดชำระสุทธิ:"
        AppLanguage.EN -> "Total Amount:"
    }
    fun cancel(lang: AppLanguage) = when (lang) {
        AppLanguage.TH -> "ยกเลิก"
        AppLanguage.EN -> "Cancel"
    }
    fun payAndGetVoucher(lang: AppLanguage) = when (lang) {
        AppLanguage.TH -> "ชำระเงินและรับคูปอง"
        AppLanguage.EN -> "Pay & Generate Voucher"
    }
    fun voucherSuccessTitle(lang: AppLanguage) = when (lang) {
        AppLanguage.TH -> "🎉 ออก E-Voucher สำเร็จ!"
        AppLanguage.EN -> "🎉 E-Voucher Issued Successfully!"
    }
    fun voucherInstructions(lang: AppLanguage) = when (lang) {
        AppLanguage.TH -> "แสดง QR Code นี้ต่อเจ้าหน้าที่เคาน์เตอร์เวชระเบียนของสถานพยาบาล เมื่อผลแล็บเสร็จสิ้น ระบบจะอัปเดต Verified Badge โดยอัตโนมัติผ่าน Zero-Knowledge Verification"
        AppLanguage.EN -> "Show this QR Code at the hospital registration counter. Once lab results are finalized, your Verified Badge will update automatically via Zero-Knowledge Verification."
    }
    fun acknowledgeAndReturn(lang: AppLanguage) = when (lang) {
        AppLanguage.TH -> "รับทราบ & กลับสู่แอป"
        AppLanguage.EN -> "Done & Return to App"
    }
    fun medicalCouncilNoticeTitle(lang: AppLanguage) = when (lang) {
        AppLanguage.TH -> "สอดคล้องข้อบังคับแพทยสภา (No Brokerage Fee)"
        AppLanguage.EN -> "Medical Council Compliant (Zero Brokerage Fee)"
    }
    fun medicalCouncilNoticeDesc(lang: AppLanguage) = when (lang) {
        AppLanguage.TH -> "Safe Date ไม่คิดค่านายหน้าหรือส่วนแบ่งจากการส่งต่อผู้ป่วย ค่าธรรมเนียมทั้งหมดเป็นค่าบริหารจัดการเทคโนโลยีและการประมวลผล ZKV อย่างโปร่งใส"
        AppLanguage.EN -> "Safe Date charges no patient referral or brokerage fees. All proceeds directly cover medical costs and ZKV cryptographic verification technology."
    }
    fun buyVoucherButton(price: Int, lang: AppLanguage) = when (lang) {
        AppLanguage.TH -> "ซื้อ E-Voucher ฿$price"
        AppLanguage.EN -> "Get E-Voucher ฿$price"
    }
    fun selectHospitalTitle(lang: AppLanguage) = when (lang) {
        AppLanguage.TH -> "เลือกสถานพยาบาลที่สะดวกรับบริการ"
        AppLanguage.EN -> "Select Preferred Hospital Branch"
    }
    fun confirmPurchaseButton(lang: AppLanguage) = when (lang) {
        AppLanguage.TH -> "ยืนยันและออก QR Token"
        AppLanguage.EN -> "Confirm & Generate QR Token"
    }
    fun cancelButton(lang: AppLanguage) = when (lang) {
        AppLanguage.TH -> "ยกเลิก"
        AppLanguage.EN -> "Cancel"
    }
    fun voucherIssuedTitle(lang: AppLanguage) = when (lang) {
        AppLanguage.TH -> "E-Voucher ออกสำเร็จแล้ว!"
        AppLanguage.EN -> "E-Voucher Issued Successfully!"
    }
    fun voucherInstruction(lang: AppLanguage) = when (lang) {
        AppLanguage.TH -> "แสดง QR Token นี้ ณ แผนกเวชระเบียนหรือห้องตรวจสุขภาพของสถานพยาบาล ผลตรวจจะเชื่อมโยงเข้า Safe Date ผ่านระบบ ZKV อัตโนมัติ"
        AppLanguage.EN -> "Present this encrypted QR Token at the hospital registration counter. Test results are securely verified into Safe Date via ZKV automatically."
    }
    fun closeButton(lang: AppLanguage) = when (lang) {
        AppLanguage.TH -> "ปิด"
        AppLanguage.EN -> "Close"
    }

    // ZKV Screen
    fun zkvScreenTitle(lang: AppLanguage) = when (lang) {
        AppLanguage.TH -> "Zero-Knowledge Verification & FHIR Gateway"
        AppLanguage.EN -> "Zero-Knowledge Verification & FHIR Gateway"
    }
    fun zkvArchitectureTitle(lang: AppLanguage) = when (lang) {
        AppLanguage.TH -> "HL7 FHIR & Zero-Knowledge Verification"
        AppLanguage.EN -> "HL7 FHIR & Zero-Knowledge Verification"
    }
    fun zkvArchitectureSub(lang: AppLanguage) = when (lang) {
        AppLanguage.TH -> "สถาปัตยกรรมทางเทคนิค & ความสอดคล้องตามกฎหมาย PDPA"
        AppLanguage.EN -> "Technical Architecture & Privacy Compliance"
    }
    fun zkvArchitectureDesc(lang: AppLanguage) = when (lang) {
        AppLanguage.TH -> "Safe Date ปฏิเสธวิธีอัปโหลดภาพถ่ายเอกสารผลแล็บเพราะเสี่ยงต่อการปลอมแปลง (Document Tampering) เราเชื่อมตรงกับระบบ HIS โรงพยาบาลผ่านมาตรฐาน HL7 FHIR API และเทคโนโลยี ZKV โดยโรงพยาบาลประมวลผลแล้วส่งกลับมาเพียงค่า Boolean Logic เซิร์ฟเวอร์ของแอปจึงไม่เคยจัดเก็บข้อมูลเวชระเบียนของผู้ใช้แม้แต่บรรทัดเดียว"
        AppLanguage.EN -> "Safe Date rejects document photo uploads due to tampering vulnerabilities. We interface directly with Hospital Information Systems (HIS) via HL7 FHIR APIs and ZKV cryptographic handshakes. The hospital processes tests internally and only transmits Boolean logic—zero patient medical records are ever stored on our servers."
    }
    fun pdpaConsentTitle(lang: AppLanguage) = when (lang) {
        AppLanguage.TH -> "PDPA Consent Gateway (มาตรา 26 ข้อมูลอ่อนไหว)"
        AppLanguage.EN -> "PDPA Consent Gateway (Section 26 Sensitive Data)"
    }
    fun explicitConsentDesc(lang: AppLanguage) = when (lang) {
        AppLanguage.TH -> "ข้าพเจ้ายินยอมโดยชัดแจ้ง (Explicit Consent) ให้ Safe Date ดึงสถานะ Boolean จาก HIS เพื่อแสดง Verified Badge"
        AppLanguage.EN -> "I explicitly consent to Safe Date requesting Boolean verification status from the HIS to display my Verified Badge."
    }
    fun dataMinimizationDesc(lang: AppLanguage) = when (lang) {
        AppLanguage.TH -> "รับทราบหลักการ Data Minimization: แอปจะไม่เข้าถึง ไม่ส่งต่อ และไม่บันทึกค่าผลแล็บละเอียดใดๆ ทั้งสิ้น"
        AppLanguage.EN -> "Acknowledge Data Minimization: Safe Date never accesses, forwards, or stores any raw laboratory diagnostics."
    }
    fun fhirSimTitle(lang: AppLanguage) = when (lang) {
        AppLanguage.TH -> "จำลองการทดสอบ HL7 FHIR Interoperability"
        AppLanguage.EN -> "Simulate HL7 FHIR Interoperability"
    }
    fun fhirSimSub(lang: AppLanguage) = when (lang) {
        AppLanguage.TH -> "เลือกสถานพยาบาลเป้าหมายเพื่อทำ Handshake ผ่าน OAuth 2.0:"
        AppLanguage.EN -> "Select target medical center to execute OAuth 2.0 handshake:"
    }
    fun zkvVerifyingState(lang: AppLanguage) = when (lang) {
        AppLanguage.TH -> "กำลังเชื่อมต่อ HIS & ประมวลผล ZKV..."
        AppLanguage.EN -> "Connecting to HIS & Computing ZKV Proof..."
    }
    fun zkvRunAction(lang: AppLanguage) = when (lang) {
        AppLanguage.TH -> "ยิงคำขอ FHIR Observation (ZKV Handshake)"
        AppLanguage.EN -> "Trigger FHIR Observation (ZKV Handshake)"
    }
    fun zkvShowcaseTitle(lang: AppLanguage) = when (lang) {
        AppLanguage.TH -> "ZKV Data Minimization Showcase"
        AppLanguage.EN -> "ZKV Data Minimization Showcase"
    }
    fun zkvShowcaseSub(lang: AppLanguage) = when (lang) {
        AppLanguage.TH -> "เปรียบเทียบข้อมูลภายใน รพ. (PHI) กับข้อมูลที่ส่งมา Dating App (ZKV)"
        AppLanguage.EN -> "Compare internal hospital data (PHI) vs. payload sent to Safe Date (ZKV)"
    }
    fun hospitalPerimeterLabel(lang: AppLanguage) = when (lang) {
        AppLanguage.TH -> "🔒 ภายใน Hospital Secure Perimeter (ห้ามส่งออก):"
        AppLanguage.EN -> "🔒 Inside Hospital Secure Perimeter (Strictly Internal):"
    }
    fun sanitizedZkvLabel(lang: AppLanguage) = when (lang) {
        AppLanguage.TH -> "✅ สิ่งที่ส่งกลับมา Safe Date (Zero-Knowledge Payload):"
        AppLanguage.EN -> "✅ Transmitted to Safe Date Server (Zero-Knowledge):"
    }
    fun auditLogsSectionTitle(lang: AppLanguage) = when (lang) {
        AppLanguage.TH -> "ประวัติการตรวจสอบย้อนหลัง (Audit Log - No PHI)"
        AppLanguage.EN -> "Verification Audit Trail (Tamper-evident, No PHI)"
    }
}

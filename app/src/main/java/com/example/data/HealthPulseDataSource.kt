package com.example.data

import androidx.compose.ui.graphics.Color
import com.example.model.BadgeTier
import com.example.model.EVoucherPackage
import com.example.model.PartnerHospital
import com.example.model.UserProfile

object HealthPulseDataSource {

    val mockProfiles = listOf(
        UserProfile(
            id = "p1",
            name = "หมอแพรว",
            age = 27,
            occupation = "แพทย์ประจำบ้าน (Resident)",
            location = "สุขุมวิท, กรุงเทพฯ",
            distanceKm = 2.4,
            bio = "เชื่อในความสัมพันธ์ที่ดีและความรับผิดชอบต่อสุขภาพซึ่งกันและกัน ชอบวิ่งมาราธอน ดื่ม Specialty Coffee ☕🏃‍♀️",
            avatarBgColor = Color(0xFFBE185D),
            badgeTier = BadgeTier.GOLD_PLATINUM,
            lastTestedDate = "2026-08-15",
            partnerClinicName = "รพ.บำรุงราษฎร์ อินเตอร์เนชั่นแนล",
            screeningPackageName = "PCR Multiplex 28 เชื้อ + ฉีดวัคซีน HPV 9 สายพันธุ์ครบ",
            interests = listOf("Healthcare", "Running", "Coffee", "Classical Music"),
            hpvVaccinated = true,
            pcrMultiplexDone = true
        ),
        UserProfile(
            id = "p2",
            name = "กานต์",
            age = 29,
            occupation = "Software Architect & Tech Lead",
            location = "สาทร, กรุงเทพฯ",
            distanceKm = 4.1,
            bio = "Dev สายระบบ Cloud & ZKV Privacy สนใจเรื่อง HealthTech และความสัมพันธ์ที่โปร่งใส ซื่อสัตย์ 💻✨",
            avatarBgColor = Color(0xFF0369A1),
            badgeTier = BadgeTier.GOLD_PLATINUM,
            lastTestedDate = "2026-08-01",
            partnerClinicName = "BDMS Wellness Clinic",
            screeningPackageName = "Comprehensive STI Multiplex Panel 28 Targets",
            interests = listOf("Tech & Coding", "Board Games", "Sci-Fi", "Tennis"),
            hpvVaccinated = true,
            pcrMultiplexDone = true
        ),
        UserProfile(
            id = "p3",
            name = "มายด์",
            age = 25,
            occupation = "Digital Marketing Strategist",
            location = "อารีย์, กรุงเทพฯ",
            distanceKm = 5.8,
            bio = "ชอบไปคาเฟ่ ถ่ายภาพฟิล์ม และฟังอินดี้ป็อป มองหาคนที่ใส่ใจสุขภาพและดูแลตัวเองสม่ำเสมอ 🌿📷",
            avatarBgColor = Color(0xFF0D9488),
            badgeTier = BadgeTier.SILVER,
            lastTestedDate = "2026-07-20",
            partnerClinicName = "Pulse Clinic Siam",
            screeningPackageName = "CDC Routine Screening 14 Targets",
            interests = listOf("Photography", "Cafes", "Indie Music", "Pilates"),
            hpvVaccinated = true,
            pcrMultiplexDone = false
        ),
        UserProfile(
            id = "p4",
            name = "วิน",
            age = 31,
            occupation = "Private Equity Analyst",
            location = "สีลม, กรุงเทพฯ",
            distanceKm = 3.2,
            bio = "ทำงานด้านการเงิน แต่ให้ความสำคัญกับ Work-life Balance ตรวจสุขภาพตามรอบ CDC ทุก 3-6 เดือน 📈",
            avatarBgColor = Color(0xFF4F46E5),
            badgeTier = BadgeTier.SILVER,
            lastTestedDate = "2026-06-25",
            partnerClinicName = "รพ.กรุงเทพ (Bangkok Hospital)",
            screeningPackageName = "Routine Blood & Urine Panel",
            interests = listOf("Finance", "Wine Tasting", "Golf", "Economics"),
            hpvVaccinated = false,
            pcrMultiplexDone = false
        ),
        UserProfile(
            id = "p5",
            name = "บอส",
            age = 28,
            occupation = "Creative Director",
            location = "ทองหล่อ, กรุงเทพฯ",
            distanceKm = 6.5,
            bio = "สายอาร์ต ชอบเสพงานดีไซน์ เพิ่งตรวจสุขภาพพื้นฐานเพื่อปลดล็อก Badge ปัดแมตช์ 🎨",
            avatarBgColor = Color(0xFFD97706),
            badgeTier = BadgeTier.BRONZE,
            lastTestedDate = "2026-05-10",
            partnerClinicName = "รพ.ธนบุรี บำรุงเมือง",
            screeningPackageName = "Basic 3-in-1 Screening (HIV/Syphilis/HepB)",
            interests = listOf("Art Gallery", "Film", "Design", "Vinyl"),
            hpvVaccinated = false,
            pcrMultiplexDone = false
        ),
        UserProfile(
            id = "p6",
            name = "แจน",
            age = 24,
            occupation = "Fashion Stylist",
            location = "สยาม, กรุงเทพฯ",
            distanceKm = 7.0,
            bio = "ผู้ใช้ใหม่ กำลังศึกษาข้อมูลแพ็กเกจตรวจคัดกรองในแอป ชอบคุยเรื่องแฟชั่นและอาหารอร่อยๆ 👗🍣",
            avatarBgColor = Color(0xFF64748B),
            badgeTier = BadgeTier.UNVERIFIED,
            lastTestedDate = null,
            partnerClinicName = null,
            screeningPackageName = null,
            interests = listOf("Fashion", "Foodie", "Travel", "Pop Culture"),
            hpvVaccinated = false,
            pcrMultiplexDone = false
        )
    )

    val voucherPackages = listOf(
        EVoucherPackage(
            id = "pkg_bronze",
            title = "Basic STI Screening",
            badgeTierUnlocked = BadgeTier.BRONZE,
            priceThb = 1490,
            wholesalePriceThb = 1190,
            platformFeeThb = 300,
            tag = "🥉 ปลดล็อก Bronze Tier",
            turnaroundTime = "ทราบผลใน 2-4 ชั่วโมง (Rapid Test)",
            includedTests = listOf(
                "HIV 4th Gen Ag/Ab Combo Test",
                "Syphilis RPR/TPHA Screening",
                "Hepatitis B Surface Antigen (HBsAg)"
            ),
            clinicalGuidance = "เหมาะสำหรับผู้ที่ต้องการตรวจคัดกรองพื้นฐานเบื้องต้นตามแนวทางระบาดวิทยา"
        ),
        EVoucherPackage(
            id = "pkg_silver",
            title = "Comprehensive 14 STIs Panel",
            badgeTierUnlocked = BadgeTier.SILVER,
            priceThb = 2990,
            wholesalePriceThb = 2400,
            platformFeeThb = 590,
            tag = "🥈 แนะนำโดย CDC (Silver Tier)",
            turnaroundTime = "ทราบผลใน 24-48 ชั่วโมง",
            includedTests = listOf(
                "HIV 4th Gen Ag/Ab Combo",
                "Syphilis RPR & Treponema Specific",
                "Hepatitis B & C Antibodies",
                "Chlamydia trachomatis (Urine PCR)",
                "Neisseria gonorrhoeae (Urine PCR)",
                "Trichomonas vaginalis PCR",
                "Mycoplasma genitalium / hominis PCR"
            ),
            clinicalGuidance = "ตรวจคัดกรองตามรอบ 3–6 เดือน สำหรับผู้ที่มีเพศสัมพันธ์สม่ำเสมอ ปลดล็อก Profile Boost 2x"
        ),
        EVoucherPackage(
            id = "pkg_gold",
            title = "Ultra Multiplex DNA 28 + HPV Vaccine",
            badgeTierUnlocked = BadgeTier.GOLD_PLATINUM,
            priceThb = 5900,
            wholesalePriceThb = 4800,
            platformFeeThb = 1100,
            tag = "🥇 สูงสุด Gold/Platinum Tier",
            turnaroundTime = "ทราบผลใน 48 ชั่วโมง",
            includedTests = listOf(
                "Multiplex DNA PCR 28 เชื้อก่อโรคระบบทางเดินปัสสาวะและสืบพันธุ์",
                "Ureaplasma urealyticum & parvum DNA",
                "HSV 1 & 2 DNA PCR",
                "ฉีดวัคซีนป้องกันมะเร็งปากมดลูก/หูดหงอนไก่ Gardasil 9 (1 เข็ม)",
                "ปรึกษาแพทย์เฉพาะทางโรคติดเชื้อผ่าน Telehealth ฟรี 1 ครั้ง"
            ),
            clinicalGuidance = "มาตรฐานสูงสุดด้านความปลอดภัยทางสุขภาพ ได้รับ Priority Matchmaking อันดับ 1 บนแอป"
        )
    )

    val partnerHospitals = listOf(
        PartnerHospital(
            id = "hosp_bdms",
            name = "BDMS Wellness & รพ.กรุงเทพ",
            hospitalGroup = "Bangkok Dusit Medical Services",
            branches = "ซอยศูนย์วิจัย, สยามพารากอน, ชิดลม",
            fhirEndpoint = "https://fhir.bdms.co.th/r4/Observation",
            rating = 4.9,
            isZkvActive = true
        ),
        PartnerHospital(
            id = "hosp_bh",
            name = "รพ.บำรุงราษฎร์ อินเตอร์เนชั่นแนล",
            hospitalGroup = "Bumrungrad Health Network",
            branches = "สุขุมวิท ซอย 3 (นานาเหนือ)",
            fhirEndpoint = "https://api.bumrungrad.com/fhir/DiagnosticReport",
            rating = 4.9,
            isZkvActive = true
        ),
        PartnerHospital(
            id = "hosp_pulse",
            name = "Pulse Clinic Bangkok",
            hospitalGroup = "Pulse Healthcare Asia",
            branches = "สีลม, สยาม, อโศก, พญาไท",
            fhirEndpoint = "https://api.pulse-clinic.com/fhir/v1/ZkvCheck",
            rating = 4.8,
            isZkvActive = true
        ),
        PartnerHospital(
            id = "hosp_thonburi",
            name = "รพ.ธนบุรี บำรุงเมือง",
            hospitalGroup = "Thonburi Healthcare Group",
            branches = "ถนนบำรุงเมือง, เขตป้อมปราบฯ",
            fhirEndpoint = "https://fhir.thonburi.com/his/zkv",
            rating = 4.7,
            isZkvActive = true
        )
    )
}

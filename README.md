# Safe Date: Hospital Verified Health x ZKV Privacy Dating App
### (แอปพลิเคชันหาคู่ที่ขับเคลื่อนด้วยสุขภาพ ความโปร่งใส และความเป็นส่วนตัวขั้นสูง)

Safe Date เป็นแอปพลิเคชันหาคู่มิติใหม่ที่ผสานระหว่าง **Dating Experience** (ความตื่นเต้น ความรัก ความผูกพัน) และ **Healthcare Trust** (ความสะอาด ความถูกต้องทางการแพทย์ และความปลอดภัยของข้อมูลตามกฎหมาย PDPA) โดยใช้เทคโนโลยี **Zero-Knowledge Verification (ZKV)** และการเชื่อมโยงระบบโรงพยาบาลผ่านมาตรฐาน **HL7 FHIR API**

---

## 🌟 จุดเด่นของระบบ (Core Features)

### 1. Zero-Knowledge Health Verification (ZKV) & PDPA Compliance
- **ไม่มีการเก็บเวชระเบียนบนเซิร์ฟเวอร์**: Safe Date ไม่เคยบันทึกประวัติการรักษา ผลเลือด หรือข้อมูลไวรัสของผู้ใช้
- **Boolean Logic Handshake**: ระบบของโรงพยาบาลพันธมิตรประมวลผลผลการตรวจและส่งกลับเฉพาะผลลัพธ์เชิงตรรกะแบบเข้ารหัส (`isVerified = true/false` พร้อม Hashes)
- **สอดคล้องกับ พ.ร.บ. คุ้มครองข้อมูลส่วนบุคคล (PDPA)**: ผู้ใช้ต้องให้ความยินยอมโดยชัดแจ้ง (Explicit Consent) และยึดหลัก Data Minimization ทุกขั้นตอน

### 2. Verified Clinical Tier & 180-Day Tier Decay Engine
- ⚪ **Unverified**: ผู้ใช้ใหม่หรือผลตรวจหมดอายุเกิน 6 เดือน
- 🥉 **Bronze**: ผ่านการตรวจคัดกรองเบื้องต้น (HIV Ag/Ab, Syphilis) ภายใน 6 เดือน
- 🥈 **Silver**: ตรวจคัดกรองสม่ำเสมอทุก 3–6 เดือนตามเกณฑ์มาตรฐาน CDC
- 🥇 **Gold / Platinum**: ตรวจ Multiplex PCR 28 เชื้อ + ฉีดวัคซีนป้องกันมะเร็งปากมดลูก (HPV 9-Valent) ครบโดส
- **180-Day Window Period Decay**: นับถอยหลังอายุผลตรวจอัตโนมัติ เพื่อกระตุ้นให้ตรวจซ้ำอย่างปลอดภัยตามหลักระบาดวิทยา

### 3. Hospital E-Voucher Marketplace (No Brokerage Fee)
- ซื้อ E-Voucher แพ็กเกจตรวจสุขภาพทางเพศกับโรงพยาบาลและคลินิกชั้นนำ (เช่น BDMS Wellness, Bumrungrad, Bangkok Hospital, Pulse Clinic)
- ได้รับ QR Token เข้ารหัสเพื่อนำไปยื่นตรวจ ณ สถานพยาบาล
- **ถูกต้องตามข้อบังคับแพทยสภา**: ไม่คิดค่าหัวคิวหรือค่านายหน้าจากการส่งต่อคนไข้ (No Medical Brokerage Fee) รายได้มาจากค่าธรรมเนียมแพลตฟอร์มด้านเทคโนโลยี

### 4. Bilingual Support (TH / EN)
- สลับภาษาได้ทันทีระหว่างภาษาไทยและภาษาอังกฤษทั้งแอป ด้วยการแตะปุ่มสลับภาษา `TH | EN` บน Top Bar

### 5. Design System: "Warm Teal & Modern Coral"
- **Deep Teal** (`#0D9488`): สะท้อนความสะอาด ความปลอดภัย และความเชี่ยวชาญทางการแพทย์
- **Warm Coral** (`#F43F5E`): สื่อถึงความรัก ความตื่นเต้น และเสน่ห์ของ Dating App
- **Warm Cream** (`#FAF8F5`): พื้นหลังอบอุ่น นุ่มนวล ไม่แข็งกระด้าง
- ดีไซน์คลีน ปราศจากองค์ประกอบที่รกตา (Clean, Intentional & Human-Centric UI)

---

## 🏗️ สถาปัตยกรรมระบบ (Architecture)

- **Language**: Kotlin 100%
- **UI Framework**: Jetpack Compose + Material Design 3 (M3)
- **Architecture**: MVVM (Model-View-ViewModel) + StateFlow / Coroutines
- **State Management**: Reactive Single-Source of Truth (`HealthPulseViewModel`, `HealthPulseUiState`)
- **Visual Assets**: Adaptive Custom Icon, Modern Elevation, High-Contrast Typography
- **Privacy & Healthcare Standards**: HL7 FHIR Observation Schema, SHA-256 Hash ZKV Proofs

---

## 📱 โครงสร้างหน้าจอ (Screens)

1. **Discovery Screen (`/discovery`)**:
   - การ์ดแนะนำคู่เดทแบบอินเทอร์แอคทีฟ (Swipe / Like / Pass / Rewind)
   - Badge สถานะสุขภาพที่ยืนยันจาก รพ. จริง
   - ฟิลเตอร์ค้นหาตามระดับ Clinical Tier
   - ระบบแจ้งเตือนเมื่อ Match กับผู้ที่ตรวจสุขภาพแล้ว

2. **E-Voucher Marketplace (`/marketplace`)**:
   - ตลาด E-Voucher แพ็กเกจตรวจคัดกรองกับ รพ. พันธมิตร
   - ตรวจสอบราคา เวลาทราบผล และการรับรองความถูกต้อง
   - ระบบจำลองการชำระเงินและออก Dynamic QR Token

3. **My Badge (`/my_badge`)**:
   - ตรวจสอบระดับ Badge ของตนเอง และเวลานับถอยหลัง 180 วัน
   - คำอธิบายด้านระบาดวิทยา (Window Period & Screening Intervals)
   - สิทธิประโยชน์ของแต่ละ Tier และปุ่มต่ออายุ

4. **ZKV & FHIR Gateway (`/zkv`)**:
   - คอนโซลจำลองการทดสอบเชื่อมต่อ HL7 FHIR REST API กับ รพ.
   - บันทึกการทำ ZKV Cryptographic Handshake
   - การควบคุมความยินยอม PDPA & Data Minimization

---

## 🛠️ วิธีการรันโปรเจกต์ (Build & Run)

```bash
# คอมไพล์โปรเจกต์
gradle assembleDebug

# ตรวจสอบ Unit Test
gradle :app:testDebugUnitTest
```

---

## 📄 ใบอนุญาตและความปลอดภัย (Compliance & Privacy)
- ข้อมูลผลการตรวจเป็นทรัพย์สินส่วนบุคคลของผู้ใช้และโรงพยาบาลผู้ตรวจ
- Safe Date เป็นระบบตัวกลางยืนยันความถูกต้องผ่าน ZKV เท่านั้น

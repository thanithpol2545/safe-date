# SafeDate

แอปหาคู่ที่ยืนยันผลตรวจสุขภาพจากโรงพยาบาลจริงผ่าน Zero-Knowledge Verification

โค้ดชุดเดียว รันได้ทั้ง **Android** และ **iOS** ด้วย Expo (React Native)

---

## รันโปรเจกต์

```bash
npm install
npx expo start
```

สแกน QR ด้วยแอป **Expo Go** (มีทั้ง Play Store และ App Store) หรือ

```bash
npx expo start --android
npx expo start --ios       # ต้องมี Xcode
npx expo start --web
```

### Build ขึ้นสโตร์

```bash
npm install -g eas-cli
eas login
eas build:configure
eas build -p android --profile production   # .aab สำหรับ Play Store
eas build -p ios --profile production       # .ipa สำหรับ App Store
```

iOS build ทำผ่าน EAS ได้แม้ไม่มีเครื่อง Mac เพราะ build บนเครื่องของ Expo

---

## ฟีเจอร์

### 1. Zero-Knowledge Health Verification

เซิร์ฟเวอร์ไม่เก็บเวชระเบียน ผลเลือด หรือค่าผลตรวจใดๆ โรงพยาบาลพันธมิตรประมวลผลฝั่งตัวเอง
แล้วส่งกลับเฉพาะ `isVerified: true/false` พร้อม hash ที่ไม่ระบุตัวตน
หน้า **ZKV & FHIR** เป็นคอนโซลจำลองการ handshake พร้อมแสดง payload ที่ส่งกลับจริง

ยึดหลัก PDPA ตลอดสาย: explicit consent ก่อนดึงข้อมูลทุกครั้ง และ data minimization
ขอกลับมาเฉพาะ boolean

### 2. Clinical Tier + นาฬิกา 180 วัน

| Tier | เกณฑ์ | สิทธิ์ |
|---|---|---|
| ⚪ Unverified | ผู้ใช้ใหม่ หรือผลหมดอายุ | ปัดขวา 10 ครั้ง/วัน |
| 🥉 Bronze | HIV Ag/Ab, ซิฟิลิส, HBsAg | ปัดขวาไม่จำกัด + rewind |
| 🥈 Silver | 14 เชื้อ รอบ 3–6 เดือนตาม CDC | Boost 2x + กรอง verified |
| 🥇 Gold | Multiplex DNA PCR 28 เชื้อ | Priority match + ส่วนลดคลินิก |
| 💎 Platinum | PCR 28 + วัคซีน HPV 9 สายพันธุ์ครบโดส | ทุกสิทธิ์ + อันดับแรกสุด |

Badge หมดอายุใน 180 วันแล้วตกกลับเป็น Unverified โดยอัตโนมัติ
เพราะ HIV มี window period ได้ถึง 3 เดือนบนชุดตรวจรุ่นที่ 4 ผลตรวจจึงบอกอดีต ไม่ใช่ปัจจุบัน
การให้ badge หมดอายุคือสิ่งที่กันไม่ให้ badge ถูกอ่านเป็นคำรับประกัน

### 3. E-Voucher Marketplace

ซื้อแพ็กเกจตรวจกับ รพ. พันธมิตร ได้ QR token ไปยื่นที่เคาน์เตอร์ ผลส่งกลับเข้าแอปผ่าน FHIR

หน้าซื้อแสดงโครงสร้างราคาแยกให้เห็นว่าเงินไปไหน: ค่าตรวจที่จ่ายให้สถานพยาบาล
กับค่าธรรมเนียมแพลตฟอร์ม SafeDate ไม่เก็บค่าหัวคิวจากการส่งต่อคนไข้ ตามข้อบังคับแพทยสภา

มีแพ็กเกจตรวจ HIV ฟรีของคลินิกนิรนามสภากาชาดไทยอยู่ในรายการด้วย
คนที่จ่ายไม่ไหวยังเข้าถึง badge ระดับแรกได้

### 4. แชทพร้อม safety card

ทุกห้องแชทเปิดด้วยการ์ดยืนยันสุขภาพร่วมกันของทั้งสองฝ่าย และมีปุ่มชวนเดตในที่สาธารณะ

### 5. สองภาษา TH / EN

สลับทั้งแอปด้วยปุ่ม `TH | EN` บน top bar

### 6. Design System "Warm Teal & Modern Coral"

| บทบาท | สี |
|---|---|
| Primary | `#0D9488` / `#0F766E` |
| Secondary | `#F43F5E` / `#FB7185` |
| Background | `#FAF8F5` |
| Surface | `#FFFFFF` / `#F5F2EB` |
| Text | `#1E293B` / `#334155` |

Teal คุมทุกส่วนที่เกี่ยวกับการแพทย์และ marketplace, Coral อยู่เฉพาะปุ่ม like กับ badge แจ้งเตือน
เพื่อไม่ให้ความรู้สึกแบบแอปหาคู่ไปลดทอนความน่าเชื่อถือของส่วนผลตรวจ

---

## โครงไฟล์

```
App.js                        shell + แท็บล่าง + ปุ่มสลับภาษา
src/theme.js                  design tokens, นิยาม tier, TIER_DECAY_DAYS
src/i18n.js                   สตริง TH/EN + LangProvider + pick()
src/store.js                  state กลางทั้งแอป (พอร์ตจาก HealthPulseViewModel)
src/data.js                   ข้อมูลสาธิต — แทนที่ด้วย API ได้เลย
src/components/ui.js          TierBadge, Avatar, Button, Card, Chip, Checkbox, Sheet, Toast
src/screens/DiscoveryScreen   ปัดการ์ด, ฟิลเตอร์ tier, โควตา like, match dialog
src/screens/ChatsScreen       รายการแชท + ห้องแชท + safety card
src/screens/MarketplaceScreen แพ็กเกจ, โครงสร้างราคา, QR voucher
src/screens/MyBadgeScreen     สถานะ badge, นาฬิกา 180 วัน, สิทธิ์แต่ละ tier, ความเป็นส่วนตัว
src/screens/ZkvScreen         คอนโซล ZKV/FHIR + log การตรวจสอบ
legacy-android/               โปรเจกต์ Kotlin + Jetpack Compose เดิม (Android เท่านั้น)
```

---

## ยังต้องต่อฝั่งหลังบ้าน

- Auth — แนะนำ OTP เบอร์โทร และยืนยันตัวตนด้วยบัตรประชาชนสำหรับ tier สูง
- FHIR gateway จริง: OAuth 2.0 client credentials ต่อ HIS ของแต่ละโรงพยาบาล
- เก็บ consent log ทุกครั้งที่มีการเปิดเผยสถานะ พร้อม timestamp และขอบเขต
- Chat realtime (Firebase, Supabase หรือ WebSocket ของตัวเอง)
- Payment gateway สำหรับ E-Voucher (Omise, 2C2P, GBPrime)
- QR จริงด้วย `react-native-qrcode-svg` — ตอนนี้เป็น placeholder ที่ render จาก token
- Job ตัดรอบ 180 วัน พร้อม push notification เตือนก่อนหมดอายุ

## ข้อควรระวังด้านผลิตภัณฑ์

Badge บอกว่า "ตรวจแล้วเมื่อวันนั้น ผลเป็นลบ" ไม่ได้บอกว่า "ปลอดภัยตอนนี้"
ข้อความในแอปต้องไม่พูดเกินกว่านี้ และไม่ควรมีคำว่า "ปลอดภัย 100%" อยู่ในหน้าจอไหนเลย

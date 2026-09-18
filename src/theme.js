// SafeDate — design tokens
// Palette: "Warm Teal & Modern Coral"

export const colors = {
  teal: '#0D9488',
  tealDeep: '#0F766E',
  tealSoft: '#CCFBF1',

  coral: '#F43F5E',
  coralSoft: '#FB7185',

  bg: '#FAF8F5',
  surface: '#FFFFFF',
  sand: '#F5F2EB',

  text: '#1E293B',
  textMuted: '#334155',
  slate: '#64748B',
  line: '#E2E8F0',

  danger: '#E11D48',
  warn: '#D97706',
};

// Clinical tiers. Gold และ Platinum แยกกันตาม design doc
// (repo Kotlin เดิมรวมเป็น GOLD_PLATINUM ก้อนเดียว)
export const tiers = {
  unverified: {
    key: 'unverified', label: 'ยังไม่ยืนยัน', labelEn: 'Unverified',
    fg: '#64748B', bg: '#E2E8F0', icon: '⚪',
    desc: 'ผู้ใช้ใหม่ หรือผลตรวจหมดอายุเกิน 180 วัน',
    descEn: 'New member, or screening expired beyond 180 days',
    perk: 'ปัดขวาได้ 10 ครั้ง/วัน และดูรายละเอียด Badge ของคนอื่นไม่ได้',
    perkEn: '10 likes per day, badge details of others hidden',
  },
  bronze: {
    key: 'bronze', label: 'Bronze', labelEn: 'Bronze',
    fg: '#D97706', bg: '#FEF3C7', icon: '🥉',
    desc: 'ตรวจคัดกรองพื้นฐาน (HIV Ag/Ab, ซิฟิลิส, HBsAg) ภายใน 6 เดือน',
    descEn: 'Basic screen (HIV Ag/Ab, Syphilis, HBsAg) within 6 months',
    perk: 'ปัดขวาไม่จำกัด + ย้อนกลับได้ไม่จำกัด',
    perkEn: 'Unlimited likes and unlimited rewind',
  },
  silver: {
    key: 'silver', label: 'Silver', labelEn: 'Silver',
    fg: '#475569', bg: '#E2E8F0', icon: '🥈',
    desc: 'ตรวจ 14 เชื้อ ตามรอบ 3–6 เดือนที่ CDC แนะนำ',
    descEn: '14-target panel on the 3–6 month CDC cadence',
    perk: 'Profile Boost 2x + กรองเห็นเฉพาะคนที่ยืนยันแล้ว',
    perkEn: '2x discovery boost and verified-only filter',
  },
  gold: {
    key: 'gold', label: 'Gold', labelEn: 'Gold',
    fg: '#CA8A04', bg: '#FEF08A', icon: '🥇',
    desc: 'ตรวจ Multiplex DNA PCR 28 เชื้อ ครบชุด',
    descEn: 'Full 28-target Multiplex DNA PCR panel',
    perk: 'Priority Match + กรอบโปรไฟล์ทอง + ส่วนลดคลินิกพันธมิตร',
    perkEn: 'Priority matchmaking, gold profile frame, partner clinic discounts',
  },
  platinum: {
    key: 'platinum', label: 'Platinum', labelEn: 'Platinum',
    fg: '#0F766E', bg: '#CCFBF1', icon: '💎',
    desc: 'PCR 28 เชื้อ + ฉีดวัคซีน HPV 9 สายพันธุ์ครบโดส',
    descEn: '28-target PCR plus a complete HPV 9-valent course',
    perk: 'ทุกสิทธิ์ของ Gold + ขึ้นอันดับแรกสุดในการแนะนำ',
    perkEn: 'Everything in Gold, plus top placement in discovery',
  },
};

export const tierOrder = ['unverified', 'bronze', 'silver', 'gold', 'platinum'];

// อายุผลตรวจตามหลักระบาดวิทยา — หมดอายุแล้ว tier ตกกลับเป็น unverified
export const TIER_DECAY_DAYS = 180;

export const radius = { sm: 10, md: 16, lg: 24, pill: 999 };

export const shadow = {
  card: {
    shadowColor: '#0F172A', shadowOpacity: 0.08, shadowRadius: 18,
    shadowOffset: { width: 0, height: 8 }, elevation: 4,
  },
  raised: {
    shadowColor: '#0F172A', shadowOpacity: 0.14, shadowRadius: 28,
    shadowOffset: { width: 0, height: 14 }, elevation: 8,
  },
};

export const type = {
  display: { fontSize: 27, fontWeight: '700', letterSpacing: -0.6, color: colors.text },
  title: { fontSize: 20, fontWeight: '700', letterSpacing: -0.3, color: colors.text },
  body: { fontSize: 15, lineHeight: 22, color: colors.textMuted },
  small: { fontSize: 13, lineHeight: 19, color: colors.slate },
  tiny: { fontSize: 11.5, lineHeight: 17, color: colors.slate },
  mono: { fontFamily: undefined, fontSize: 12, letterSpacing: 0.2 },
};

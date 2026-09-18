// State กลางของแอป — พอร์ตมาจาก HealthPulseViewModel.kt
import React, { createContext, useContext, useMemo, useState, useCallback } from 'react';
import { TIER_DECAY_DAYS, tierOrder } from './theme';
import { profiles, initialConversations, me as initialMe, hospitals } from './data';

const Ctx = createContext(null);

const FREE_LIKES_PER_DAY = 10; // Unverified ปัดขวาได้วันละ 10 ครั้ง

const randHex = (n) =>
  Array.from({ length: n }, () => '0123456789abcdef'[Math.floor(Math.random() * 16)]).join('');

const todayISO = () => new Date().toISOString().slice(0, 10);
const plusDaysISO = (d) => new Date(Date.now() + d * 864e5).toISOString().slice(0, 10);

export function StoreProvider({ children }) {
  const [tab, setTab] = useState('discovery');
  const [index, setIndex] = useState(0);
  const [minTier, setMinTier] = useState('any');
  const [showBadge, setShowBadge] = useState(true);

  const [myTier, setMyTier] = useState(initialMe.tier);
  const [myClinic, setMyClinic] = useState(initialMe.clinic);
  const [myTestedOn, setMyTestedOn] = useState(initialMe.testedOn);
  const [daysLeft, setDaysLeft] = useState(initialMe.daysLeft);

  const [likesUsed, setLikesUsed] = useState(0);
  const [matches, setMatches] = useState([profiles[0], profiles[2]]);
  const [matchPopup, setMatchPopup] = useState(null);

  const [conversations, setConversations] = useState(initialConversations);
  const [activeChatId, setActiveChatId] = useState(null);
  const [partnerTyping, setPartnerTyping] = useState(false);

  const [vouchers, setVouchers] = useState([]);
  const [zkvLogs, setZkvLogs] = useState([]);

  const [toast, setToast] = useState(null);
  const say = useCallback((msg) => {
    setToast(msg);
    setTimeout(() => setToast((t) => (t === msg ? null : t)), 2800);
  }, []);

  const badgeExpired = daysLeft <= 0;
  const effectiveTier = badgeExpired ? 'unverified' : myTier;
  const unlimitedLikes = effectiveTier !== 'unverified';

  const visibleProfiles = useMemo(() => {
    if (minTier === 'any') return profiles;
    return profiles.filter((p) => tierOrder.indexOf(p.tier) >= tierOrder.indexOf(minTier));
  }, [minTier]);

  const like = useCallback(() => {
    const p = visibleProfiles[index];
    if (!p) return false;
    if (!unlimitedLikes && likesUsed >= FREE_LIKES_PER_DAY) return 'locked';
    if (!unlimitedLikes) setLikesUsed((n) => n + 1);
    setIndex((i) => i + 1);
    // แมตช์เกิดเมื่อทั้งสองฝ่ายยืนยันแล้ว
    if (p.tier !== 'unverified' && effectiveTier !== 'unverified') {
      setMatches((m) => (m.some((x) => x.id === p.id) ? m : [p, ...m]));
      setMatchPopup(p);
    }
    return true;
  }, [visibleProfiles, index, unlimitedLikes, likesUsed, effectiveTier]);

  const pass = useCallback(() => setIndex((i) => i + 1), []);
  const rewind = useCallback(() => setIndex((i) => Math.max(0, i - 1)), []);
  const resetDeck = useCallback(() => setIndex(0), []);

  const openChatWith = useCallback(
    (profile) => {
      setConversations((cs) => {
        const found = cs.find((c) => c.partnerId === profile.id);
        if (found) return cs.map((c) => (c.id === found.id ? { ...c, unread: 0 } : c));
        const conv = {
          id: 'conv_' + profile.id,
          partnerId: profile.id,
          matchedOn: 'วันนี้',
          matchedOnEn: 'Today',
          unread: 0,
          messages: [
            {
              id: 'sys' + Date.now(),
              system: true,
              text: `คุณและ${profile.name}ต่างผ่านการยืนยันผลตรวจจาก${profile.clinic || 'โรงพยาบาลพันธมิตร'}`,
              textEn: `You and ${profile.nameEn} are both verified by ${profile.clinicEn || 'a partner hospital'}.`,
              time: 'เมื่อสักครู่',
            },
          ],
        };
        return [conv, ...cs];
      });
      setActiveChatId('conv_' + profile.id);
      setTab('chats');
      setMatchPopup(null);
    },
    []
  );

  const openConversation = useCallback((id) => {
    setConversations((cs) => cs.map((c) => (c.id === id ? { ...c, unread: 0 } : c)));
    setActiveChatId(id);
  }, []);

  const closeChat = useCallback(() => setActiveChatId(null), []);

  const sendMessage = useCallback(
    (text, { proposal = false, lang = 'th' } = {}) => {
      const body = text.trim();
      if (!body || !activeChatId) return;
      const mine = {
        id: 'me' + Date.now(),
        mine: true,
        text: body,
        textEn: body,
        time: lang === 'th' ? 'เมื่อสักครู่' : 'just now',
        proposal,
      };
      setConversations((cs) =>
        cs.map((c) => (c.id === activeChatId ? { ...c, messages: [...c.messages, mine] } : c))
      );

      setTimeout(() => setPartnerTyping(true), 400);
      setTimeout(() => {
        setPartnerTyping(false);
        const reply = autoReply(body, proposal);
        setConversations((cs) =>
          cs.map((c) =>
            c.id === activeChatId
              ? {
                  ...c,
                  messages: [
                    ...c.messages,
                    {
                      id: 'p' + Date.now(),
                      mine: false,
                      text: reply.th,
                      textEn: reply.en,
                      time: lang === 'th' ? 'เมื่อสักครู่' : 'just now',
                    },
                  ],
                }
              : c
          )
        );
      }, 1600);
    },
    [activeChatId]
  );

  const buyVoucher = useCallback((pkg, hospital) => {
    const token = `SD-${Math.floor(100000 + Math.random() * 899999)}-${hospital.id
      .replace('hosp_', '')
      .toUpperCase()}`;
    const voucher = {
      id: token,
      pkgId: pkg.id,
      title: pkg.title,
      titleEn: pkg.titleEn,
      tier: pkg.unlocks,
      hospital: hospital.name,
      hospitalEn: hospital.nameEn,
      purchasedOn: todayISO(),
      expiresOn: plusDaysISO(180),
      redeemed: false,
    };
    setVouchers((v) => [voucher, ...v]);
    return voucher;
  }, []);

  // ผลตรวจกลับมาแล้ว -> อัปเกรด tier และรีเซ็ตนาฬิกา 180 วัน
  const applyScreeningResult = useCallback((tier, hospitalName, testedOnLabel) => {
    setMyTier(tier);
    setMyClinic(hospitalName);
    setMyTestedOn(testedOnLabel || todayISO());
    setDaysLeft(TIER_DECAY_DAYS);
  }, []);

  const runZkv = useCallback(
    (hospital, tierAwarded = 'gold') =>
      new Promise((resolve) => {
        setTimeout(() => {
          const log = {
            id: 'zkv' + Date.now(),
            timestamp: new Date().toISOString().slice(0, 19).replace('T', ' '),
            hospital: hospital.name,
            hospitalEn: hospital.nameEn,
            endpoint: hospital.fhir,
            patientHash: '0x' + randHex(24),
            resource: 'DiagnosticReport?category=LAB&code=48676-1',
            verified: true,
            tierAwarded,
            latencyMs: 180 + Math.floor(Math.random() * 220),
          };
          setZkvLogs((l) => [log, ...l]);
          applyScreeningResult(tierAwarded, hospital.name, todayISO());
          resolve(log);
        }, 1400);
      }),
    [applyScreeningResult]
  );

  const simulateDecay = useCallback(() => {
    setDaysLeft(0);
  }, []);
  const restoreTier = useCallback(() => {
    setMyTier(initialMe.tier);
    setMyTestedOn(initialMe.testedOn);
    setMyClinic(initialMe.clinic);
    setDaysLeft(initialMe.daysLeft);
  }, []);

  const value = {
    tab, setTab,
    index, minTier, setMinTier: (t) => { setMinTier(t); setIndex(0); },
    showBadge, setShowBadge,
    visibleProfiles, like, pass, rewind, resetDeck,
    myTier, effectiveTier, badgeExpired, myClinic, myTestedOn, daysLeft,
    likesUsed, likesLeft: Math.max(0, FREE_LIKES_PER_DAY - likesUsed), unlimitedLikes,
    matches, matchPopup, setMatchPopup,
    conversations, activeChatId, openChatWith, openConversation, closeChat,
    sendMessage, partnerTyping,
    vouchers, buyVoucher, applyScreeningResult,
    zkvLogs, runZkv, hospitals,
    simulateDecay, restoreTier,
    toast, say,
  };

  return <Ctx.Provider value={value}>{children}</Ctx.Provider>;
}

export const useStore = () => useContext(Ctx);

function autoReply(text, proposal) {
  const s = text.toLowerCase();
  if (proposal)
    return {
      th: 'ชอบเลยค่ะ นัดที่คาเฟ่เปิดโล่งแบบนี้สบายใจดี เดี๋ยวส่งพิกัดร้านโปรดให้นะคะ',
      en: 'I would love that. A bright public cafe sounds comfortable. I will send you my favourite spot.',
    };
  if (s.includes('วิ่ง') || s.includes('run'))
    return {
      th: 'ปกติวิ่งเพซสบายๆ ประมาณ 6:30 ค่ะ ไว้ไปวิ่งด้วยกันนะคะ',
      en: 'I usually run an easy 6:30 pace. We should go together sometime.',
    };
  if (s.includes('กาแฟ') || s.includes('coffee') || s.includes('คาเฟ่'))
    return {
      th: 'มีร้านแถวสุขุมวิทคั่วกลางหอมมาก ชวนไปชิมได้เลยค่ะ',
      en: 'There is a medium roast place near Sukhumvit I love. Happy to take you.',
    };
  if (s.includes('ตรวจ') || s.includes('health') || s.includes('badge'))
    return {
      th: 'เห็นด้วยค่ะ พอทั้งคู่ตรวจแล้วมันคุยกันได้สบายใจกว่าเยอะ',
      en: 'Agreed. When both people have screened, the conversation is much easier.',
    };
  return {
    th: 'น่าสนใจมากค่ะ ปกติวันหยุดชอบทำอะไรบ้างคะ',
    en: 'That sounds great. What do you usually get up to on weekends?',
  };
}

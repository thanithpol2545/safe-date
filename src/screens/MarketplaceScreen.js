import React, { useState } from 'react';
import { View, Text, ScrollView, StyleSheet, Pressable } from 'react-native';
import { colors, radius, type, tiers } from '../theme';
import { useLang, pick } from '../i18n';
import { useStore } from '../store';
import { packages } from '../data';
import { Card, Button, SectionTitle, TierBadge, Sheet } from '../components/ui';

export default function MarketplaceScreen() {
  const { t, lang } = useLang();
  const st = useStore();
  const [buying, setBuying] = useState(null);
  const [hospital, setHospital] = useState(st.hospitals[0]);
  const [issued, setIssued] = useState(null);

  const confirm = () => {
    const v = st.buyVoucher(buying, hospital);
    setBuying(null);
    setIssued(v);
  };

  return (
    <ScrollView contentContainerStyle={{ padding: 20, paddingBottom: 40 }}>
      <SectionTitle hint={t.market_hint}>{t.market_title}</SectionTitle>

      {st.vouchers.length > 0 ? (
        <View style={{ marginBottom: 22 }}>
          <Text style={[type.small, { marginBottom: 8 }]}>{t.my_vouchers}</Text>
          {st.vouchers.map((v) => (
            <Pressable key={v.id} onPress={() => setIssued(v)} style={s.voucher}>
              <View style={{ flex: 1 }}>
                <Text style={{ color: '#fff', fontWeight: '700', fontSize: 15 }}>{v.id}</Text>
                <Text style={{ color: '#D4F5EF', fontSize: 12.5, marginTop: 3 }}>
                  {pick(v, 'hospital', lang)} · {t.voucher_expire(v.expiresOn)}
                </Text>
              </View>
              <Text style={{ fontSize: 22 }}>🎟️</Text>
            </Pressable>
          ))}
        </View>
      ) : null}

      <View style={{ gap: 14 }}>
        {packages.map((p) => {
          const tier = tiers[p.unlocks];
          return (
            <Card key={p.id}>
              <View style={{ flexDirection: 'row', justifyContent: 'space-between', alignItems: 'flex-start', gap: 12 }}>
                <Text style={[type.title, { fontSize: 17, flex: 1 }]}>{pick(p, 'title', lang)}</Text>
                <TierBadge tier={p.unlocks} size="sm" />
              </View>

              <View style={{ marginTop: 12, gap: 6 }}>
                {pick(p, 'tests', lang).map((i) => (
                  <Text key={i} style={type.small}>
                    ✓ {i}
                  </Text>
                ))}
              </View>

              <Text style={[type.tiny, { marginTop: 10 }]}>{pick(p, 'turnaround', lang)}</Text>
              <Text style={[type.tiny, { marginTop: 4, color: colors.textMuted }]}>
                {pick(p, 'guidance', lang)}
              </Text>

              <View style={s.foot}>
                <Text style={s.price}>
                  {p.price === 0 ? (lang === 'th' ? 'ฟรี' : 'Free') : `฿${p.price.toLocaleString()}`}
                </Text>
                <Button
                  label={t.market_buy}
                  onPress={() => {
                    setBuying(p);
                    setHospital(st.hospitals[0]);
                  }}
                  style={{ paddingHorizontal: 20, paddingVertical: 12 }}
                />
              </View>

              <Text style={[type.tiny, { marginTop: 10, color: tier.fg }]}>
                {t.market_unlocks(lang === 'en' ? tier.labelEn : tier.label)}
              </Text>
            </Card>
          );
        })}
      </View>

      <Text style={[type.tiny, { marginTop: 20 }]}>{t.no_brokerage}</Text>

      {/* แผ่นซื้อ */}
      <Sheet visible={!!buying} onClose={() => setBuying(null)}>
        {buying ? (
          <ScrollView showsVerticalScrollIndicator={false}>
            <Text style={type.title}>{pick(buying, 'title', lang)}</Text>

            <Text style={[type.small, { marginTop: 18, marginBottom: 8 }]}>{t.market_choose_hosp}</Text>
            <View style={{ gap: 8 }}>
              {st.hospitals.map((h) => {
                const on = h.id === hospital.id;
                return (
                  <Pressable
                    key={h.id}
                    onPress={() => setHospital(h)}
                    style={[s.hosp, on && { borderColor: colors.teal, backgroundColor: colors.tealSoft + '55' }]}
                  >
                    <View style={{ flex: 1 }}>
                      <Text style={{ fontWeight: '600', color: colors.text, fontSize: 14.5 }}>
                        {pick(h, 'name', lang)}
                      </Text>
                      <Text style={type.tiny}>
                        {pick(h, 'branches', lang)} · ★ {h.rating}
                      </Text>
                    </View>
                    {on ? <Text style={{ color: colors.teal, fontWeight: '700' }}>✓</Text> : null}
                  </Pressable>
                );
              })}
            </View>

            {buying.price > 0 ? (
              <View style={{ marginTop: 20 }}>
                <Text style={[type.small, { marginBottom: 8 }]}>{t.price_breakdown}</Text>
                <Row label={t.price_lab} value={`฿${buying.wholesale.toLocaleString()}`} />
                <Row label={t.price_fee} value={`฿${buying.fee.toLocaleString()}`} />
                <View style={s.divider} />
                <Row label="" value={`฿${buying.price.toLocaleString()}`} bold />
                <Text style={[type.tiny, { marginTop: 10 }]}>{t.no_brokerage}</Text>
              </View>
            ) : null}

            <Button label={t.market_confirm} onPress={confirm} style={{ marginTop: 22 }} />
            <Pressable onPress={() => setBuying(null)} style={{ alignItems: 'center', paddingVertical: 14 }}>
              <Text style={{ color: colors.slate, fontSize: 14 }}>{t.market_cancel}</Text>
            </Pressable>
          </ScrollView>
        ) : null}
      </Sheet>

      {/* QR voucher */}
      <Sheet visible={!!issued} onClose={() => setIssued(null)}>
        {issued ? (
          <View style={{ alignItems: 'center', gap: 12 }}>
            <Text style={type.title}>{t.voucher_title}</Text>
            <Text style={[type.small, { textAlign: 'center' }]}>{t.voucher_show}</Text>
            <QrPlaceholder seed={issued.id} />
            <Text style={{ fontWeight: '700', fontSize: 16, color: colors.tealDeep, letterSpacing: 1 }}>
              {issued.id}
            </Text>
            <Text style={[type.small, { textAlign: 'center' }]}>
              {pick(issued, 'title', lang)}
              {'\n'}
              {pick(issued, 'hospital', lang)}
              {'\n'}
              {t.voucher_expire(issued.expiresOn)}
            </Text>
            <Button label={t.voucher_close} onPress={() => setIssued(null)} style={{ alignSelf: 'stretch', marginTop: 8 }} />
          </View>
        ) : null}
      </Sheet>
    </ScrollView>
  );
}

function Row({ label, value, bold }) {
  return (
    <View style={{ flexDirection: 'row', justifyContent: 'space-between', paddingVertical: 5 }}>
      <Text style={[type.small, bold && { fontWeight: '700', color: colors.text, fontSize: 15 }]}>{label}</Text>
      <Text style={[type.small, bold && { fontWeight: '700', color: colors.tealDeep, fontSize: 17 }]}>{value}</Text>
    </View>
  );
}

// QR จำลองแบบ deterministic — ของจริงให้ต่อ react-native-qrcode-svg
function QrPlaceholder({ seed }) {
  const size = 13;
  let h = 0;
  for (let i = 0; i < seed.length; i++) h = (h * 31 + seed.charCodeAt(i)) >>> 0;
  const cells = [];
  for (let i = 0; i < size * size; i++) {
    h = (h * 1103515245 + 12345) >>> 0;
    cells.push((h >> 16) % 3 !== 0);
  }
  return (
    <View style={s.qr}>
      {Array.from({ length: size }).map((_, r) => (
        <View key={r} style={{ flexDirection: 'row' }}>
          {Array.from({ length: size }).map((__, c) => (
            <View
              key={c}
              style={{
                width: 11,
                height: 11,
                backgroundColor: cells[r * size + c] ? colors.text : 'transparent',
              }}
            />
          ))}
        </View>
      ))}
    </View>
  );
}

const s = StyleSheet.create({
  voucher: {
    flexDirection: 'row',
    alignItems: 'center',
    gap: 12,
    backgroundColor: colors.tealDeep,
    borderRadius: radius.md,
    padding: 16,
    marginBottom: 10,
  },
  foot: {
    flexDirection: 'row',
    alignItems: 'center',
    justifyContent: 'space-between',
    marginTop: 14,
    paddingTop: 14,
    borderTopWidth: 1,
    borderTopColor: colors.line,
  },
  price: { fontSize: 22, fontWeight: '700', color: colors.tealDeep },
  hosp: {
    flexDirection: 'row',
    alignItems: 'center',
    gap: 10,
    borderWidth: 1.5,
    borderColor: colors.line,
    borderRadius: radius.md,
    padding: 14,
    backgroundColor: colors.surface,
  },
  divider: { height: 1, backgroundColor: colors.line, marginVertical: 8 },
  qr: { padding: 14, backgroundColor: '#fff', borderRadius: radius.md, borderWidth: 1, borderColor: colors.line },
});

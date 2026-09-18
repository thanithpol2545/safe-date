import React from 'react';
import { View, Text, ScrollView, StyleSheet, Pressable, Switch } from 'react-native';
import { colors, radius, type, tiers, tierOrder, TIER_DECAY_DAYS } from '../theme';
import { useLang, pick } from '../i18n';
import { useStore } from '../store';
import { me } from '../data';
import { Card, Avatar, TierBadge, SectionTitle, Button } from '../components/ui';

export default function MyBadgeScreen() {
  const { t, lang } = useLang();
  const st = useStore();
  const tier = tiers[st.effectiveTier];
  const pctLeft = Math.max(0, Math.min(1, st.daysLeft / TIER_DECAY_DAYS));

  return (
    <ScrollView contentContainerStyle={{ padding: 20, paddingBottom: 40 }}>
      <View style={{ alignItems: 'center', gap: 10, marginBottom: 20 }}>
        <Avatar name={pick(me, 'name', lang)} color={colors.teal} size={92} />
        <Text style={type.display}>
          {pick(me, 'name', lang)}, {me.age}
        </Text>
        <Text style={type.small}>{pick(me, 'job', lang)}</Text>
        <TierBadge tier={st.effectiveTier} expired={st.badgeExpired} />
      </View>

      <Card style={{ backgroundColor: tier.bg + '44', borderColor: tier.fg + '33' }}>
        <Text style={{ fontWeight: '700', color: tier.fg, fontSize: 15 }}>{t.badge_title}</Text>
        <Text style={[type.small, { marginTop: 6 }]}>
          {t.badge_last(st.myTestedOn)} · {t.badge_at(st.myClinic)}
        </Text>

        <View style={s.bar}>
          <View
            style={[
              s.barFill,
              { width: `${pctLeft * 100}%`, backgroundColor: st.badgeExpired ? colors.danger : tier.fg },
            ]}
          />
        </View>
        <Text style={[type.small, { marginTop: 8, color: st.badgeExpired ? colors.danger : colors.slate }]}>
          {st.badgeExpired ? t.badge_decayed : t.badge_decay(st.daysLeft)}
        </Text>

        <Text style={[type.tiny, { marginTop: 14, fontWeight: '700', color: colors.textMuted }]}>
          {t.badge_perk}
        </Text>
        <Text style={[type.small, { marginTop: 3 }]}>{pick(tier, 'perk', lang)}</Text>

        <Button label={t.badge_renew} onPress={() => st.setTab('market')} style={{ marginTop: 16 }} />
      </Card>

      <Card style={{ marginTop: 16 }}>
        <Text style={{ fontWeight: '700', fontSize: 15, color: colors.text }}>{t.badge_why_title}</Text>
        <Text style={[type.small, { marginTop: 8 }]}>{t.badge_why}</Text>
      </Card>

      <SectionTitle>{t.badge_all_tiers}</SectionTitle>
      <View style={{ gap: 10 }}>
        {tierOrder.map((k) => {
          const x = tiers[k];
          const reached = tierOrder.indexOf(k) <= tierOrder.indexOf(st.effectiveTier);
          return (
            <View key={k} style={[s.tierRow, reached && { borderColor: x.fg + '55', backgroundColor: x.bg + '33' }]}>
              <Text style={{ fontSize: 18 }}>{x.icon}</Text>
              <View style={{ flex: 1 }}>
                <Text style={{ fontWeight: '700', color: reached ? x.fg : colors.slate, fontSize: 14.5 }}>
                  {lang === 'en' ? x.labelEn : x.label}
                </Text>
                <Text style={[type.tiny, { marginTop: 2 }]}>{pick(x, 'desc', lang)}</Text>
                <Text style={[type.tiny, { marginTop: 3, color: colors.textMuted }]}>{pick(x, 'perk', lang)}</Text>
              </View>
            </View>
          );
        })}
      </View>

      <SectionTitle>{t.privacy}</SectionTitle>
      <Card style={{ gap: 14 }}>
        <View style={{ flexDirection: 'row', alignItems: 'center', gap: 12 }}>
          <View style={{ flex: 1 }}>
            <Text style={{ fontWeight: '600', color: colors.text, fontSize: 15 }}>{t.show_badge}</Text>
            <Text style={[type.tiny, { marginTop: 3 }]}>{t.show_badge_sub}</Text>
          </View>
          <Switch
            value={st.showBadge}
            onValueChange={st.setShowBadge}
            trackColor={{ true: colors.teal, false: colors.line }}
            thumbColor="#fff"
          />
        </View>
        <View style={{ height: 1, backgroundColor: colors.line }} />
        <Pressable onPress={() => st.say(t.erase_done)}>
          <Text style={{ color: colors.danger, fontWeight: '600', fontSize: 14 }}>{t.erase}</Text>
        </Pressable>
      </Card>

      <View style={{ flexDirection: 'row', gap: 10, marginTop: 20 }}>
        <Button label={t.sim_decay} tone="ghost" onPress={st.simulateDecay} style={{ flex: 1, paddingHorizontal: 8 }} />
        <Button label={t.sim_restore} tone="ghost" onPress={st.restoreTier} style={{ flex: 1, paddingHorizontal: 8 }} />
      </View>
    </ScrollView>
  );
}

const s = StyleSheet.create({
  bar: { height: 8, borderRadius: 4, backgroundColor: colors.line, marginTop: 16, overflow: 'hidden' },
  barFill: { height: 8, borderRadius: 4 },
  tierRow: {
    flexDirection: 'row',
    gap: 12,
    alignItems: 'flex-start',
    borderWidth: 1,
    borderColor: colors.line,
    borderRadius: radius.md,
    padding: 14,
    backgroundColor: colors.surface,
  },
});

import React, { useRef } from 'react';
import {
  View,
  Text,
  StyleSheet,
  Animated,
  PanResponder,
  Dimensions,
  Pressable,
  Modal,
  ScrollView,
} from 'react-native';
import { colors, radius, type, shadow, tiers, tierOrder } from '../theme';
import { useLang, pick } from '../i18n';
import { useStore } from '../store';
import { TierBadge, Avatar, Chip, Button } from '../components/ui';

const { width } = Dimensions.get('window');
const THRESHOLD = width * 0.26;

export default function DiscoveryScreen() {
  const { t, lang } = useLang();
  const st = useStore();
  const position = useRef(new Animated.ValueXY()).current;

  const list = st.visibleProfiles;
  const profile = list[st.index];
  const next = list[st.index + 1];

  const settle = () =>
    Animated.spring(position, { toValue: { x: 0, y: 0 }, friction: 6, useNativeDriver: false }).start();

  const fly = (dir) => {
    Animated.timing(position, {
      toValue: { x: dir === 'right' ? width * 1.3 : -width * 1.3, y: 50 },
      duration: 220,
      useNativeDriver: false,
    }).start(() => {
      position.setValue({ x: 0, y: 0 });
      if (dir === 'right') {
        const r = st.like();
        if (r === 'locked') st.say(t.like_locked);
      } else st.pass();
    });
  };

  const guardLike = () => {
    if (!st.unlimitedLikes && st.likesLeft <= 0) {
      st.say(t.like_locked);
      settle();
      return;
    }
    fly('right');
  };

  const pan = useRef(
    PanResponder.create({
      onMoveShouldSetPanResponder: (_, g) => Math.abs(g.dx) > 6,
      onPanResponderMove: (_, g) => position.setValue({ x: g.dx, y: g.dy * 0.3 }),
      onPanResponderRelease: (_, g) => {
        if (g.dx > THRESHOLD) guardLike();
        else if (g.dx < -THRESHOLD) fly('left');
        else settle();
      },
    })
  ).current;

  const rotate = position.x.interpolate({
    inputRange: [-width, 0, width],
    outputRange: ['-9deg', '0deg', '9deg'],
  });
  const likeOp = position.x.interpolate({ inputRange: [0, THRESHOLD], outputRange: [0, 1], extrapolate: 'clamp' });
  const nopeOp = position.x.interpolate({ inputRange: [-THRESHOLD, 0], outputRange: [1, 0], extrapolate: 'clamp' });

  const filterOptions = [
    { key: 'any', label: t.filter_all },
    ...tierOrder.slice(1).map((k) => ({
      key: k,
      label: `${lang === 'en' ? tiers[k].labelEn : tiers[k].label} ${t.filter_from}`,
    })),
  ];

  return (
    <View style={{ flex: 1 }}>
      <ScrollView
        horizontal
        showsHorizontalScrollIndicator={false}
        contentContainerStyle={{ gap: 8, paddingHorizontal: 16, paddingBottom: 12 }}
        style={{ flexGrow: 0 }}
      >
        {filterOptions.map((o) => (
          <Chip key={o.key} label={o.label} active={st.minTier === o.key} onPress={() => st.setMinTier(o.key)} />
        ))}
      </ScrollView>

      {!st.unlimitedLikes ? (
        <Text style={[type.tiny, { paddingHorizontal: 20, marginBottom: 6 }]}>
          {lang === 'th'
            ? `เหลือปัดขวาอีก ${st.likesLeft} ครั้งวันนี้`
            : `${st.likesLeft} likes left today`}
        </Text>
      ) : null}

      <View style={s.deck}>
        {!profile ? (
          <View style={s.empty}>
            <Text style={[type.title, { textAlign: 'center' }]}>{t.deck_empty_title}</Text>
            <Text style={[type.body, { textAlign: 'center' }]}>{t.deck_empty_body}</Text>
            <Button label={t.deck_again} onPress={st.resetDeck} style={{ marginTop: 14 }} />
          </View>
        ) : (
          <>
            {next ? (
              <View style={[s.card, s.behind]} pointerEvents="none">
                <Body profile={next} />
              </View>
            ) : null}
            <Animated.View
              key={profile.id}
              {...pan.panHandlers}
              style={[
                s.card,
                shadow.raised,
                { transform: [{ translateX: position.x }, { translateY: position.y }, { rotate }] },
              ]}
            >
              <Animated.View style={[s.stamp, s.stampLike, { opacity: likeOp }]}>
                <Text style={[s.stampText, { color: colors.teal }]}>{t.stamp_like}</Text>
              </Animated.View>
              <Animated.View style={[s.stamp, s.stampNope, { opacity: nopeOp }]}>
                <Text style={[s.stampText, { color: colors.slate }]}>{t.stamp_pass}</Text>
              </Animated.View>
              <Body profile={profile} />
            </Animated.View>
          </>
        )}
      </View>

      <View style={s.actions}>
        <Pressable style={[s.act, s.actSkip]} onPress={() => profile && fly('left')} accessibilityLabel={t.stamp_pass}>
          <Text style={{ fontSize: 22, color: colors.slate }}>✕</Text>
        </Pressable>
        <Pressable style={[s.act, s.actBack]} onPress={st.rewind} accessibilityLabel="rewind">
          <Text style={{ fontSize: 17, color: colors.tealDeep }}>↺</Text>
        </Pressable>
        <Pressable style={[s.act, s.actLike]} onPress={() => profile && guardLike()} accessibilityLabel={t.stamp_like}>
          <Text style={{ fontSize: 24, color: '#fff' }}>♥</Text>
        </Pressable>
      </View>

      <MatchDialog />
    </View>
  );
}

function Body({ profile }) {
  const { t, lang } = useLang();
  const tier = tiers[profile.tier];
  return (
    <View style={{ flex: 1 }}>
      <View style={[s.hero, { backgroundColor: profile.accent + '18' }]}>
        <Avatar name={pick(profile, 'name', lang)} color={profile.accent} size={90} />
        <View style={{ position: 'absolute', top: 14, right: 14 }}>
          <TierBadge tier={profile.tier} />
        </View>
      </View>

      <ScrollView contentContainerStyle={{ padding: 18, gap: 12 }} showsVerticalScrollIndicator={false}>
        <View>
          <Text style={type.display}>
            {pick(profile, 'name', lang)}, {profile.age}
          </Text>
          <Text style={[type.small, { marginTop: 2 }]}>
            {pick(profile, 'job', lang)} · {pick(profile, 'area', lang)} · {t.away(profile.km)}
          </Text>
        </View>

        <Text style={type.body}>{pick(profile, 'bio', lang)}</Text>

        <View style={s.tags}>
          {pick(profile, 'tags', lang).map((tag) => (
            <View key={tag} style={s.tag}>
              <Text style={{ color: colors.textMuted, fontSize: 13 }}>{tag}</Text>
            </View>
          ))}
        </View>

        <View style={[s.health, { backgroundColor: tier.bg + '55', borderColor: tier.fg + '33' }]}>
          <Text style={{ fontWeight: '700', color: tier.fg, fontSize: 13.5 }}>
            {profile.testedOn ? t.tested_on(pick(profile, 'testedOn', lang)) : t.not_tested}
          </Text>
          {profile.clinic ? (
            <Text style={[type.tiny, { marginTop: 4 }]}>{t.verified_by(pick(profile, 'clinic', lang))}</Text>
          ) : null}
          {profile.panel ? (
            <Text style={[type.tiny, { marginTop: 2 }]}>{pick(profile, 'panel', lang)}</Text>
          ) : (
            <Text style={[type.tiny, { marginTop: 4 }]}>{t.no_result_note}</Text>
          )}
          <Text style={[type.tiny, { marginTop: 8 }]}>{t.zkv_note}</Text>
        </View>
      </ScrollView>
    </View>
  );
}

function MatchDialog() {
  const { t, lang } = useLang();
  const st = useStore();
  const p = st.matchPopup;
  return (
    <Modal visible={!!p} transparent animationType="fade" onRequestClose={() => st.setMatchPopup(null)}>
      <View style={s.modalWrap}>
        <View style={s.modal}>
          {p ? (
            <>
              <View style={{ flexDirection: 'row', gap: -14 }}>
                <Avatar name="♥" color={colors.coral} size={72} />
                <Avatar name={pick(p, 'name', lang)} color={p.accent} size={72} />
              </View>
              <Text style={[type.display, { textAlign: 'center' }]}>{t.match_title}</Text>
              <Text style={[type.body, { textAlign: 'center' }]}>{t.match_body(pick(p, 'name', lang))}</Text>
              <TierBadge tier={p.tier} />
              <Button label={t.match_chat} tone="coral" onPress={() => st.openChatWith(p)} style={{ alignSelf: 'stretch' }} />
              <Pressable onPress={() => st.setMatchPopup(null)}>
                <Text style={{ color: colors.slate, fontSize: 14, paddingVertical: 6 }}>{t.match_later}</Text>
              </Pressable>
            </>
          ) : null}
        </View>
      </View>
    </Modal>
  );
}

const s = StyleSheet.create({
  deck: { flex: 1, marginHorizontal: 16 },
  card: {
    position: 'absolute',
    left: 0,
    right: 0,
    top: 0,
    bottom: 0,
    backgroundColor: colors.surface,
    borderRadius: radius.lg,
    overflow: 'hidden',
    borderWidth: 1,
    borderColor: colors.line,
  },
  behind: { transform: [{ scale: 0.95 }, { translateY: 14 }], opacity: 0.55 },
  hero: { height: 176, alignItems: 'center', justifyContent: 'center' },
  tags: { flexDirection: 'row', flexWrap: 'wrap', gap: 8 },
  tag: { backgroundColor: colors.sand, paddingVertical: 6, paddingHorizontal: 12, borderRadius: radius.pill },
  health: { borderRadius: radius.md, padding: 14, borderWidth: 1 },
  stamp: {
    position: 'absolute',
    top: 22,
    zIndex: 10,
    paddingVertical: 8,
    paddingHorizontal: 16,
    borderRadius: radius.sm,
    borderWidth: 2.5,
    backgroundColor: '#ffffffE6',
  },
  stampLike: { left: 20, borderColor: colors.teal, transform: [{ rotate: '-12deg' }] },
  stampNope: { right: 20, borderColor: colors.slate, transform: [{ rotate: '12deg' }] },
  stampText: { fontWeight: '800', fontSize: 18 },
  actions: { flexDirection: 'row', justifyContent: 'center', alignItems: 'center', gap: 18, paddingVertical: 16 },
  act: { width: 62, height: 62, borderRadius: 31, alignItems: 'center', justifyContent: 'center' },
  actSkip: { backgroundColor: colors.surface, borderWidth: 1.5, borderColor: colors.line },
  actBack: { width: 46, height: 46, borderRadius: 23, backgroundColor: colors.tealSoft },
  actLike: { backgroundColor: colors.coral, ...shadow.card },
  empty: { flex: 1, alignItems: 'center', justifyContent: 'center', padding: 30, gap: 8 },
  modalWrap: { flex: 1, backgroundColor: '#0F172A88', alignItems: 'center', justifyContent: 'center', padding: 28 },
  modal: {
    backgroundColor: colors.bg,
    borderRadius: 28,
    padding: 26,
    gap: 14,
    alignItems: 'center',
    width: '100%',
    maxWidth: 360,
  },
});

import React from 'react';
import { View, Text, Pressable, StyleSheet, Modal } from 'react-native';
import { colors, tiers, radius, type, shadow } from '../theme';
import { useLang } from '../i18n';

export function TierBadge({ tier, size = 'md', expired = false }) {
  const { lang } = useLang();
  const t = tiers[tier] || tiers.unverified;
  const small = size === 'sm';
  const label = lang === 'en' ? t.labelEn : t.label;
  return (
    <View
      style={[
        s.badge,
        {
          backgroundColor: t.bg,
          paddingVertical: small ? 3 : 5,
          paddingHorizontal: small ? 8 : 10,
          opacity: expired ? 0.5 : 1,
        },
      ]}
    >
      <Text style={{ fontSize: small ? 10 : 12 }}>{t.icon}</Text>
      <Text style={{ color: t.fg, fontWeight: '700', fontSize: small ? 11 : 12.5 }}>{label}</Text>
    </View>
  );
}

export function Avatar({ name, color = colors.teal, size = 48 }) {
  return (
    <View
      style={{
        width: size,
        height: size,
        borderRadius: size / 2,
        backgroundColor: color + '22',
        alignItems: 'center',
        justifyContent: 'center',
      }}
    >
      <Text style={{ color, fontWeight: '700', fontSize: size * 0.38 }}>
        {String(name || '?').slice(0, 1)}
      </Text>
    </View>
  );
}

export function Button({ label, onPress, tone = 'teal', disabled, style }) {
  const bg = tone === 'coral' ? colors.coral : tone === 'ghost' ? 'transparent' : colors.teal;
  const fg = tone === 'ghost' ? colors.teal : '#fff';
  return (
    <Pressable
      onPress={disabled ? undefined : onPress}
      accessibilityRole="button"
      style={({ pressed }) => [
        s.btn,
        { backgroundColor: bg, opacity: disabled ? 0.45 : pressed ? 0.85 : 1 },
        tone === 'ghost' && { borderWidth: 1.5, borderColor: colors.teal },
        style,
      ]}
    >
      <Text style={{ color: fg, fontWeight: '700', fontSize: 15 }}>{label}</Text>
    </Pressable>
  );
}

export function Card({ children, style }) {
  return <View style={[s.card, shadow.card, style]}>{children}</View>;
}

export function SectionTitle({ children, hint }) {
  return (
    <View style={{ marginBottom: 12, marginTop: 4 }}>
      <Text style={type.title}>{children}</Text>
      {hint ? <Text style={[type.small, { marginTop: 3 }]}>{hint}</Text> : null}
    </View>
  );
}

export function Chip({ label, active, onPress }) {
  return (
    <Pressable
      onPress={onPress}
      accessibilityRole="button"
      accessibilityState={{ selected: !!active }}
      style={[s.chip, active && { backgroundColor: colors.teal, borderColor: colors.teal }]}
    >
      <Text
        style={{
          color: active ? '#fff' : colors.textMuted,
          fontSize: 13.5,
          fontWeight: active ? '700' : '500',
        }}
      >
        {label}
      </Text>
    </Pressable>
  );
}

export function Checkbox({ checked, onToggle, title, sub }) {
  return (
    <Pressable
      onPress={onToggle}
      accessibilityRole="checkbox"
      accessibilityState={{ checked }}
      style={{ flexDirection: 'row', gap: 12, alignItems: 'flex-start', paddingVertical: 8 }}
    >
      <View style={[s.box, checked && { backgroundColor: colors.teal, borderColor: colors.teal }]}>
        {checked ? <Text style={{ color: '#fff', fontSize: 13, fontWeight: '700' }}>✓</Text> : null}
      </View>
      <View style={{ flex: 1 }}>
        <Text style={{ fontSize: 14.5, fontWeight: '600', color: colors.text }}>{title}</Text>
        {sub ? <Text style={[type.tiny, { marginTop: 2 }]}>{sub}</Text> : null}
      </View>
    </Pressable>
  );
}

export function Sheet({ visible, onClose, children }) {
  return (
    <Modal visible={visible} transparent animationType="slide" onRequestClose={onClose}>
      <Pressable style={s.backdrop} onPress={onClose} />
      <View style={s.sheet}>{children}</View>
    </Modal>
  );
}

export function Toast({ text }) {
  if (!text) return null;
  return (
    <View pointerEvents="none" style={s.toast}>
      <Text style={{ color: '#fff', fontSize: 13.5, textAlign: 'center' }}>{text}</Text>
    </View>
  );
}

const s = StyleSheet.create({
  badge: {
    flexDirection: 'row',
    alignItems: 'center',
    gap: 5,
    borderRadius: radius.pill,
    alignSelf: 'flex-start',
  },
  btn: {
    paddingVertical: 14,
    paddingHorizontal: 20,
    borderRadius: radius.md,
    alignItems: 'center',
    justifyContent: 'center',
  },
  card: {
    backgroundColor: colors.surface,
    borderRadius: radius.lg,
    padding: 18,
    borderWidth: 1,
    borderColor: colors.line,
  },
  chip: {
    paddingVertical: 9,
    paddingHorizontal: 14,
    borderRadius: radius.pill,
    borderWidth: 1,
    borderColor: colors.line,
    backgroundColor: colors.surface,
  },
  box: {
    width: 22,
    height: 22,
    borderRadius: 6,
    borderWidth: 1.5,
    borderColor: colors.line,
    alignItems: 'center',
    justifyContent: 'center',
    marginTop: 1,
  },
  backdrop: { flex: 1, backgroundColor: '#0F172A66' },
  sheet: {
    backgroundColor: colors.bg,
    borderTopLeftRadius: 28,
    borderTopRightRadius: 28,
    padding: 22,
    paddingBottom: 34,
    maxHeight: '86%',
  },
  toast: {
    position: 'absolute',
    left: 24,
    right: 24,
    bottom: 26,
    backgroundColor: colors.text,
    paddingVertical: 12,
    paddingHorizontal: 18,
    borderRadius: 14,
    zIndex: 50,
  },
});

import React from 'react';
import { View, Text, StyleSheet, Pressable, Platform, StatusBar } from 'react-native';
import { SafeAreaProvider, SafeAreaView } from 'react-native-safe-area-context';
import { colors, type, tiers } from './src/theme';
import { LangProvider, useLang } from './src/i18n';
import { StoreProvider, useStore } from './src/store';
import { Toast } from './src/components/ui';
import DiscoveryScreen from './src/screens/DiscoveryScreen';
import ChatsScreen from './src/screens/ChatsScreen';
import MarketplaceScreen from './src/screens/MarketplaceScreen';
import MyBadgeScreen from './src/screens/MyBadgeScreen';
import ZkvScreen from './src/screens/ZkvScreen';

export default function App() {
  return (
    <SafeAreaProvider>
      <LangProvider>
        <StoreProvider>
          <Shell />
        </StoreProvider>
      </LangProvider>
    </SafeAreaProvider>
  );
}

function Shell() {
  const { t, lang, toggle } = useLang();
  const st = useStore();

  const tabs = [
    { key: 'discovery', label: t.tab_discovery, icon: '◎' },
    { key: 'chats', label: t.tab_chats, icon: '✉' },
    { key: 'market', label: t.tab_market, icon: '＋' },
    { key: 'badge', label: t.tab_badge, icon: '☺' },
    { key: 'zkv', label: t.tab_zkv, icon: '⛨' },
  ];

  const unread = st.conversations.reduce((n, c) => n + (c.unread || 0), 0);

  return (
    <SafeAreaView style={s.root} edges={['top', 'bottom']}>
      <StatusBar barStyle="dark-content" backgroundColor={colors.bg} />

      <View style={s.header}>
        <View style={{ flex: 1 }}>
          <Text style={s.wordmark}>
            Safe<Text style={{ color: colors.coral }}>Date</Text>
          </Text>
          <Text style={type.tiny}>
            {st.minTier === 'any'
              ? t.showing_all
              : t.showing_filtered(lang === 'en' ? tiers[st.minTier].labelEn : tiers[st.minTier].label)}
          </Text>
        </View>
        <Pressable onPress={toggle} style={s.langBtn} accessibilityLabel="switch language">
          <Text style={{ color: lang === 'th' ? colors.teal : colors.slate, fontWeight: '700', fontSize: 13 }}>TH</Text>
          <Text style={{ color: colors.line }}>|</Text>
          <Text style={{ color: lang === 'en' ? colors.teal : colors.slate, fontWeight: '700', fontSize: 13 }}>EN</Text>
        </Pressable>
      </View>

      <View style={{ flex: 1 }}>
        {st.tab === 'discovery' && <DiscoveryScreen />}
        {st.tab === 'chats' && <ChatsScreen />}
        {st.tab === 'market' && <MarketplaceScreen />}
        {st.tab === 'badge' && <MyBadgeScreen />}
        {st.tab === 'zkv' && <ZkvScreen />}
        <Toast text={st.toast} />
      </View>

      <View style={s.tabbar}>
        {tabs.map((tab) => {
          const on = st.tab === tab.key;
          return (
            <Pressable
              key={tab.key}
              style={s.tab}
              onPress={() => st.setTab(tab.key)}
              accessibilityRole="tab"
              accessibilityState={{ selected: on }}
            >
              <View>
                <Text style={{ fontSize: 18, color: on ? colors.teal : colors.slate }}>{tab.icon}</Text>
                {tab.key === 'chats' && unread > 0 ? <View style={s.pip} /> : null}
              </View>
              <Text
                style={{
                  fontSize: 10.5,
                  marginTop: 3,
                  color: on ? colors.teal : colors.slate,
                  fontWeight: on ? '700' : '500',
                }}
                numberOfLines={1}
              >
                {tab.label}
              </Text>
            </Pressable>
          );
        })}
      </View>
    </SafeAreaView>
  );
}

const s = StyleSheet.create({
  root: { flex: 1, backgroundColor: colors.bg },
  header: {
    flexDirection: 'row',
    alignItems: 'center',
    gap: 12,
    paddingHorizontal: 20,
    paddingTop: Platform.OS === 'android' ? 12 : 4,
    paddingBottom: 10,
  },
  wordmark: { fontSize: 22, fontWeight: '800', color: colors.tealDeep, letterSpacing: -0.5 },
  langBtn: {
    flexDirection: 'row',
    alignItems: 'center',
    gap: 6,
    borderWidth: 1,
    borderColor: colors.line,
    borderRadius: 999,
    paddingVertical: 7,
    paddingHorizontal: 12,
    backgroundColor: colors.surface,
  },
  tabbar: {
    flexDirection: 'row',
    backgroundColor: colors.surface,
    borderTopWidth: 1,
    borderTopColor: colors.line,
    paddingTop: 10,
    paddingBottom: 6,
  },
  tab: { flex: 1, alignItems: 'center', justifyContent: 'center', paddingHorizontal: 2 },
  pip: {
    position: 'absolute',
    top: -2,
    right: -6,
    width: 8,
    height: 8,
    borderRadius: 4,
    backgroundColor: colors.coral,
  },
});

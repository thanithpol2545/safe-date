import React, { useRef, useState, useEffect } from 'react';
import {
  View,
  Text,
  ScrollView,
  StyleSheet,
  Pressable,
  TextInput,
  KeyboardAvoidingView,
  Platform,
} from 'react-native';
import { colors, radius, type } from '../theme';
import { useLang, pick } from '../i18n';
import { useStore } from '../store';
import { profiles } from '../data';
import { Avatar, TierBadge, SectionTitle } from '../components/ui';

const byId = (id) => profiles.find((p) => p.id === id);

export default function ChatsScreen() {
  const st = useStore();
  if (st.activeChatId) return <ChatRoom />;
  return <ChatList />;
}

function ChatList() {
  const { t, lang } = useLang();
  const st = useStore();

  return (
    <ScrollView contentContainerStyle={{ padding: 20, paddingBottom: 40 }}>
      <SectionTitle>{t.chats_new}</SectionTitle>
      <ScrollView horizontal showsHorizontalScrollIndicator={false} style={{ marginBottom: 22 }}>
        <View style={{ flexDirection: 'row', gap: 16, paddingRight: 20 }}>
          {st.matches.map((p) => (
            <Pressable key={p.id} onPress={() => st.openChatWith(p)} style={{ alignItems: 'center', width: 72 }}>
              <View style={[s.ring, { borderColor: p.accent }]}>
                <Avatar name={pick(p, 'name', lang)} color={p.accent} size={56} />
              </View>
              <Text style={[type.small, { marginTop: 6 }]} numberOfLines={1}>
                {pick(p, 'name', lang)}
              </Text>
            </Pressable>
          ))}
        </View>
      </ScrollView>

      <SectionTitle>{t.chats_msgs}</SectionTitle>
      {st.conversations.map((c) => {
        const p = byId(c.partnerId);
        if (!p) return null;
        const last = c.messages[c.messages.length - 1];
        return (
          <Pressable
            key={c.id}
            onPress={() => st.openConversation(c.id)}
            style={({ pressed }) => [s.row, pressed && { backgroundColor: colors.sand }]}
          >
            <Avatar name={pick(p, 'name', lang)} color={p.accent} size={52} />
            <View style={{ flex: 1 }}>
              <View style={{ flexDirection: 'row', alignItems: 'center', gap: 8 }}>
                <Text style={{ fontWeight: '700', color: colors.text, fontSize: 15.5 }}>
                  {pick(p, 'name', lang)}
                </Text>
                {st.showBadge ? <TierBadge tier={p.tier} size="sm" /> : null}
              </View>
              <Text style={[type.small, { marginTop: 4 }]} numberOfLines={1}>
                {last ? pick(last, 'text', lang) : ''}
              </Text>
            </View>
            <View style={{ alignItems: 'flex-end', gap: 6 }}>
              <Text style={type.tiny}>{last?.time}</Text>
              {c.unread > 0 ? (
                <View style={s.dot}>
                  <Text style={{ color: '#fff', fontSize: 11, fontWeight: '700' }}>{c.unread}</Text>
                </View>
              ) : null}
            </View>
          </Pressable>
        );
      })}
    </ScrollView>
  );
}

function ChatRoom() {
  const { t, lang } = useLang();
  const st = useStore();
  const conv = st.conversations.find((c) => c.id === st.activeChatId);
  const partner = conv ? byId(conv.partnerId) : null;
  const [draft, setDraft] = useState('');
  const scroller = useRef(null);

  useEffect(() => {
    const id = setTimeout(() => scroller.current?.scrollToEnd({ animated: true }), 80);
    return () => clearTimeout(id);
  }, [conv?.messages.length, st.partnerTyping]);

  if (!conv || !partner) return null;

  const send = () => {
    st.sendMessage(draft, { lang });
    setDraft('');
  };

  const propose = () => {
    const text =
      lang === 'th'
        ? 'ชวนไปเดตที่คาเฟ่ในพื้นที่เปิดโล่ง บรรยากาศสบายและปลอดภัยทั้งสองฝ่ายนะครับ'
        : 'How about a date at a cafe in an open public space? Comfortable and safe for both of us.';
    st.sendMessage(text, { proposal: true, lang });
  };

  return (
    <KeyboardAvoidingView
      style={{ flex: 1 }}
      behavior={Platform.OS === 'ios' ? 'padding' : undefined}
      keyboardVerticalOffset={90}
    >
      <View style={s.chatHeader}>
        <Pressable onPress={st.closeChat} hitSlop={10}>
          <Text style={{ color: colors.teal, fontSize: 15, fontWeight: '600' }}>‹ {t.chat_back}</Text>
        </Pressable>
        <View style={{ flex: 1, flexDirection: 'row', alignItems: 'center', gap: 10 }}>
          <Avatar name={pick(partner, 'name', lang)} color={partner.accent} size={36} />
          <View style={{ flex: 1 }}>
            <Text style={{ fontWeight: '700', fontSize: 15, color: colors.text }}>
              {pick(partner, 'name', lang)}
            </Text>
            {st.partnerTyping ? <Text style={type.tiny}>{t.chat_typing}</Text> : null}
          </View>
        </View>
        {st.showBadge ? <TierBadge tier={partner.tier} size="sm" /> : null}
      </View>

      <ScrollView ref={scroller} contentContainerStyle={{ padding: 16, gap: 10, paddingBottom: 20 }}>
        {conv.messages.map((m) =>
          m.system ? (
            <View key={m.id} style={s.safety}>
              <Text style={{ fontWeight: '700', color: colors.tealDeep, fontSize: 13 }}>
                🔒 {t.safety_card}
              </Text>
              <Text style={[type.tiny, { marginTop: 5, color: colors.textMuted }]}>
                {pick(m, 'text', lang)}
              </Text>
            </View>
          ) : (
            <View key={m.id} style={[s.bubbleWrap, m.mine ? { alignItems: 'flex-end' } : { alignItems: 'flex-start' }]}>
              <View style={[s.bubble, m.mine ? s.mine : s.theirs, m.proposal && s.proposal]}>
                {m.proposal ? <Text style={{ fontSize: 12, marginBottom: 3 }}>☕ {t.chat_propose}</Text> : null}
                <Text style={{ color: m.mine ? '#fff' : colors.text, fontSize: 14.5, lineHeight: 21 }}>
                  {pick(m, 'text', lang)}
                </Text>
              </View>
              <Text style={[type.tiny, { marginTop: 3 }]}>{m.time}</Text>
            </View>
          )
        )}
      </ScrollView>

      <View style={s.composer}>
        <Pressable onPress={propose} style={s.proposeBtn} accessibilityLabel={t.chat_propose}>
          <Text style={{ fontSize: 18 }}>☕</Text>
        </Pressable>
        <TextInput
          value={draft}
          onChangeText={setDraft}
          placeholder={t.chat_placeholder}
          placeholderTextColor={colors.slate}
          style={s.input}
          onSubmitEditing={send}
          returnKeyType="send"
        />
        <Pressable onPress={send} style={s.sendBtn} accessibilityLabel={t.chat_send}>
          <Text style={{ color: '#fff', fontWeight: '700' }}>↑</Text>
        </Pressable>
      </View>
    </KeyboardAvoidingView>
  );
}

const s = StyleSheet.create({
  ring: { padding: 3, borderWidth: 2.5, borderRadius: 999 },
  row: { flexDirection: 'row', alignItems: 'center', gap: 14, paddingVertical: 12, paddingHorizontal: 10, borderRadius: radius.md },
  dot: {
    minWidth: 20,
    height: 20,
    borderRadius: 10,
    backgroundColor: colors.coral,
    alignItems: 'center',
    justifyContent: 'center',
    paddingHorizontal: 6,
  },
  chatHeader: {
    flexDirection: 'row',
    alignItems: 'center',
    gap: 12,
    paddingHorizontal: 16,
    paddingBottom: 12,
    borderBottomWidth: 1,
    borderBottomColor: colors.line,
  },
  safety: {
    backgroundColor: colors.tealSoft + '99',
    borderWidth: 1,
    borderColor: colors.teal + '33',
    borderRadius: radius.md,
    padding: 14,
    marginVertical: 4,
  },
  bubbleWrap: { width: '100%' },
  bubble: { maxWidth: '80%', paddingVertical: 10, paddingHorizontal: 14, borderRadius: 18 },
  mine: { backgroundColor: colors.teal, borderBottomRightRadius: 6 },
  theirs: { backgroundColor: colors.surface, borderWidth: 1, borderColor: colors.line, borderBottomLeftRadius: 6 },
  proposal: { borderWidth: 1.5, borderColor: colors.coral },
  composer: {
    flexDirection: 'row',
    alignItems: 'center',
    gap: 10,
    padding: 12,
    borderTopWidth: 1,
    borderTopColor: colors.line,
    backgroundColor: colors.surface,
  },
  proposeBtn: {
    width: 42,
    height: 42,
    borderRadius: 21,
    backgroundColor: colors.sand,
    alignItems: 'center',
    justifyContent: 'center',
  },
  input: {
    flex: 1,
    backgroundColor: colors.bg,
    borderWidth: 1,
    borderColor: colors.line,
    borderRadius: radius.pill,
    paddingHorizontal: 16,
    paddingVertical: Platform.OS === 'ios' ? 12 : 8,
    fontSize: 15,
    color: colors.text,
  },
  sendBtn: {
    width: 42,
    height: 42,
    borderRadius: 21,
    backgroundColor: colors.teal,
    alignItems: 'center',
    justifyContent: 'center',
  },
});

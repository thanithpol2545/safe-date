import React, { useState } from 'react';
import { View, Text, ScrollView, StyleSheet, Pressable, ActivityIndicator, Platform } from 'react-native';
import { colors, radius, type, tiers } from '../theme';
import { useLang, pick } from '../i18n';
import { useStore } from '../store';
import { Card, Button, SectionTitle, TierBadge, Checkbox } from '../components/ui';

const MONO = Platform.select({ ios: 'Menlo', android: 'monospace', default: 'monospace' });

export default function ZkvScreen() {
  const { t, lang } = useLang();
  const st = useStore();
  const [hospital, setHospital] = useState(st.hospitals[0]);
  const [consent, setConsent] = useState(true);
  const [minimize, setMinimize] = useState(true);
  const [busy, setBusy] = useState(false);

  const run = async () => {
    if (!consent || !minimize) return st.say(t.zkv_need_consent);
    setBusy(true);
    await st.runZkv(hospital, 'gold');
    setBusy(false);
  };

  return (
    <ScrollView contentContainerStyle={{ padding: 20, paddingBottom: 40 }}>
      <SectionTitle hint={t.zkv_hint}>{t.zkv_title}</SectionTitle>

      <Text style={[type.small, { marginBottom: 8 }]}>{t.zkv_pick}</Text>
      <View style={{ gap: 8, marginBottom: 20 }}>
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
                <Text style={[type.tiny, { fontFamily: MONO, marginTop: 3 }]}>{h.fhir}</Text>
              </View>
              {h.zkvActive ? <View style={s.live} /> : null}
            </Pressable>
          );
        })}
      </View>

      <Card>
        <Checkbox checked={consent} onToggle={() => setConsent((v) => !v)} title={t.zkv_consent} sub={t.zkv_consent_sub} />
        <Checkbox checked={minimize} onToggle={() => setMinimize((v) => !v)} title={t.zkv_minim} sub={t.zkv_minim_sub} />
        <Button
          label={busy ? t.zkv_running : t.zkv_run}
          onPress={run}
          disabled={busy || !consent || !minimize}
          style={{ marginTop: 14 }}
        />
        {busy ? <ActivityIndicator color={colors.teal} style={{ marginTop: 14 }} /> : null}
      </Card>

      <SectionTitle>{t.zkv_log}</SectionTitle>
      {st.zkvLogs.length === 0 ? (
        <Text style={type.small}>{t.zkv_empty}</Text>
      ) : (
        <View style={{ gap: 12 }}>
          {st.zkvLogs.map((log) => (
            <Card key={log.id} style={{ gap: 8 }}>
              <View style={{ flexDirection: 'row', justifyContent: 'space-between', alignItems: 'center' }}>
                <Text style={{ fontWeight: '700', color: colors.teal, fontSize: 14 }}>✓ {t.zkv_result_ok}</Text>
                <Text style={type.tiny}>{log.timestamp}</Text>
              </View>
              <Text style={{ fontSize: 14, color: colors.text, fontWeight: '600' }}>
                {pick(log, 'hospital', lang)}
              </Text>
              <Field label={t.zkv_hash} value={log.patientHash} mono />
              <Field label={t.zkv_resource} value={log.resource} mono />
              <Field label={t.zkv_latency} value={`${log.latencyMs} ms`} />
              <View style={{ flexDirection: 'row', alignItems: 'center', gap: 8, marginTop: 2 }}>
                <Text style={type.tiny}>{t.zkv_awarded}</Text>
                <TierBadge tier={log.tierAwarded} size="sm" />
              </View>
              <View style={s.payload}>
                <Text style={[type.tiny, { fontFamily: MONO, color: colors.textMuted }]}>
                  {`{ "resourceType": "Parameters",\n  "isVerified": true,\n  "tier": "${
                    tiers[log.tierAwarded].labelEn
                  }",\n  "validUntil": "+180d",\n  "value": null }`}
                </Text>
              </View>
              <Text style={type.tiny}>{t.zkv_note}</Text>
            </Card>
          ))}
        </View>
      )}
    </ScrollView>
  );
}

function Field({ label, value, mono }) {
  return (
    <View>
      <Text style={type.tiny}>{label}</Text>
      <Text
        style={[
          { fontSize: 13, color: colors.textMuted, marginTop: 2 },
          mono && { fontFamily: MONO, fontSize: 11.5 },
        ]}
      >
        {value}
      </Text>
    </View>
  );
}

const s = StyleSheet.create({
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
  live: { width: 9, height: 9, borderRadius: 5, backgroundColor: colors.teal },
  payload: {
    backgroundColor: colors.sand,
    borderRadius: radius.sm,
    padding: 12,
    marginTop: 4,
  },
});

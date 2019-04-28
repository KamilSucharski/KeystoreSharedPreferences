package com.sengami.keystoresharedpreferences;

import android.content.Context;
import android.content.SharedPreferences;

import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public final class KeystoreSharedPreferences implements SharedPreferences {

    private final Context context;
    private final SharedPreferences sharedPreferences;

    public KeystoreSharedPreferences(final Context context,
                                     final SharedPreferences sharedPreferences) {
        this.context = context;
        this.sharedPreferences = sharedPreferences;
    }

    @Override
    public Map<String, ?> getAll() {
        final Map<String, ?> encryptedValues = sharedPreferences.getAll();
        if (encryptedValues == null)  {
            return Collections.emptyMap();
        }

        final Map<String, String> decryptedValues = new HashMap<>();
        for (final Map.Entry<String, ?> entry : encryptedValues.entrySet()) {
            final String decryptedString = KeyStoreProxy.decryptString(context, (String) entry.getValue());
            decryptedValues.put(entry.getKey(), decryptedString);
        }

        return decryptedValues;
    }

    @Override
    public String getString(final String key,
                            final String defaultValue) {
        final String decryptedString = decryptAndGetString(key);
        return decryptedString != null ? decryptedString : defaultValue;
    }

    @Override
    public Set<String> getStringSet(final String key,
                                    final Set<String> defaultValues) {
        final Set<String> encryptedValues = sharedPreferences.getStringSet(key, null);
        if (encryptedValues == null)  {
            return defaultValues;
        }

        final Set<String> decryptedValues = new HashSet<>();
        for (final String value : encryptedValues) {
            decryptedValues.add(KeyStoreProxy.decryptString(context, value));
        }

        return decryptedValues;
    }

    @Override
    public int getInt(final String key,
                      final int defaultValue) {
        final String decryptedString = decryptAndGetString(key);
        return decryptedString != null ? Integer.valueOf(decryptedString) : defaultValue;
    }

    @Override
    public long getLong(final String key,
                        final long defaultValue) {
        final String decryptedString = decryptAndGetString(key);
        return decryptedString != null ? Long.valueOf(decryptedString) : defaultValue;
    }

    @Override
    public float getFloat(final String key,
                          final float defaultValue) {
        final String decryptedString = decryptAndGetString(key);
        return decryptedString != null ? Float.valueOf(decryptedString) : defaultValue;
    }

    @Override
    public boolean getBoolean(final String key,
                              final boolean defaultValue) {
        final String decryptedString = decryptAndGetString(key);
        return decryptedString != null ? Boolean.valueOf(decryptedString) : defaultValue;
    }

    @Override
    public boolean contains(final String key) {
        return sharedPreferences.contains(key);
    }

    @Override
    public Editor edit() {
        return new KeystoreSharedPreferencesEditor(context, sharedPreferences.edit());
    }

    @Override
    public void registerOnSharedPreferenceChangeListener(final OnSharedPreferenceChangeListener listener) {
        sharedPreferences.registerOnSharedPreferenceChangeListener(listener);
    }

    @Override
    public void unregisterOnSharedPreferenceChangeListener(final OnSharedPreferenceChangeListener listener) {
        sharedPreferences.unregisterOnSharedPreferenceChangeListener(listener);
    }
    
    private String decryptAndGetString(final String key) {
        final String encryptedValue = sharedPreferences.getString(key, null);
        if (encryptedValue == null) {
            return null;
        }
        
        return KeyStoreProxy.decryptString(context, encryptedValue);
    }
}
package com.sengami.keystoresharedpreferences;

import android.content.Context;
import android.content.SharedPreferences;

import java.util.HashSet;
import java.util.Set;

public final class KeystoreSharedPreferencesEditor implements SharedPreferences.Editor {

    private final Context context;
    private final SharedPreferences.Editor editor;

    public KeystoreSharedPreferencesEditor(final Context context,
                                           final SharedPreferences.Editor editor) {
        this.context = context;
        this.editor = editor;
    }

    @Override
    public SharedPreferences.Editor putString(final String key,
                                              final String value) {
        return encryptAndPutString(key, value);
    }

    @Override
    public SharedPreferences.Editor putStringSet(final String key,
                                                 final Set<String> values) {
        final Set<String> encryptedValues = new HashSet<>();
        for (final String value : values) {
            encryptedValues.add(KeyStoreProxy.encryptString(context, value));
        }
        editor.putStringSet(key, encryptedValues);
        return this;
    }

    @Override
    public SharedPreferences.Editor putInt(final String key,
                                           final int value) {
        final String valueAsString = Integer.valueOf(value).toString();
        return encryptAndPutString(key, valueAsString);
    }

    @Override
    public SharedPreferences.Editor putLong(final String key,
                                            final long value) {
        final String valueAsString = Long.valueOf(value).toString();
        return encryptAndPutString(key, valueAsString);
    }

    @Override
    public SharedPreferences.Editor putFloat(final String key,
                                             final float value) {
        final String valueAsString = Float.valueOf(value).toString();
        return encryptAndPutString(key, valueAsString);
    }

    @Override
    public SharedPreferences.Editor putBoolean(final String key,
                                               final boolean value) {
        final String valueAsString = Boolean.valueOf(value).toString();
        return encryptAndPutString(key, valueAsString);
    }

    @Override
    public SharedPreferences.Editor remove(final String key) {
        editor.remove(key);
        return this;
    }

    @Override
    public SharedPreferences.Editor clear() {
        editor.clear();
        return this;
    }

    @Override
    public boolean commit() {
        editor.commit();
        return true;
    }

    @Override
    public void apply() {
        editor.apply();
    }

    private SharedPreferences.Editor encryptAndPutString(final String key,
                                                         final String value) {
        final String encryptedValue = KeyStoreProxy.encryptString(context, value);
        editor.putString(key, encryptedValue);
        return this;
    }
}
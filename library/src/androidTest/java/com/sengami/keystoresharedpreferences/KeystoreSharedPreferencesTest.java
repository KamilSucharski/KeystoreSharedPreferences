package com.sengami.keystoresharedpreferences;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.Collections;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import static org.junit.Assert.assertEquals;

@RunWith(AndroidJUnit4.class)
public class KeystoreSharedPreferencesTest {

    private final Context context = InstrumentationRegistry.getTargetContext();
    private final SharedPreferences plaintextSharedPreferences = context.getSharedPreferences("test_preferences", Context.MODE_PRIVATE);
    private final SharedPreferences sharedPreferences = new KeystoreSharedPreferences(context, plaintextSharedPreferences);

    @Before
    public void before() {
        sharedPreferences
            .edit()
            .clear()
            .commit();
    }

    @Test
    public void stringSavesSuccessfully() {
        final String key = "string_key";
        final String expectedValue = "string_value";
        final String defaultValue = "wrong";

        sharedPreferences
            .edit()
            .putString(key, expectedValue)
            .commit();

        final String actualValue = sharedPreferences.getString(key, defaultValue);

        assertEquals(expectedValue, actualValue);
    }

    @Test
    public void stringSetSavesSuccessfully() {
        final String key = "string_set_key";
        final Set<String> expectedSet = new HashSet<>();
        expectedSet.add("string_value_1");
        expectedSet.add("string_value_2");
        expectedSet.add("string_value_3");

        final Set<String> defaultValue = Collections.emptySet();

        sharedPreferences
            .edit()
            .putStringSet(key, expectedSet)
            .commit();

        final Set<String> actualSet = sharedPreferences.getStringSet(key, defaultValue);

        assertEquals(expectedSet, actualSet);
    }

    @Test
    public void intSavesSuccessfully() {
        final String key = "int_key";
        final int expectedValue = 123;
        final int defaultValue = 666;

        sharedPreferences
            .edit()
            .putInt(key, expectedValue)
            .commit();

        final int actualValue = sharedPreferences.getInt(key, defaultValue);

        assertEquals(expectedValue, actualValue);
    }

    @Test
    public void longSavesSuccessfully() {
        final String key = "long_key";
        final long expectedValue = 123L;
        final long defaultValue = 666L;

        sharedPreferences
            .edit()
            .putLong(key, expectedValue)
            .commit();

        final long actualValue = sharedPreferences.getLong(key, defaultValue);

        assertEquals(expectedValue, actualValue);
    }

    @Test
    public void floatSavesSuccessfully() {
        final String key = "float_key";
        final float expectedValue = 123.456F;
        final float defaultValue = 666.666F;

        sharedPreferences
            .edit()
            .putFloat(key, expectedValue)
            .commit();

        final float actualValue = sharedPreferences.getFloat(key, defaultValue);

        assertEquals(expectedValue, actualValue, 0);
    }

    @Test
    public void booleanSavesSuccessfully() {
        final String key = "boolean_key";
        final boolean expectedValue = true;
        final boolean defaultValue = false;

        sharedPreferences
            .edit()
            .putBoolean(key, expectedValue)
            .commit();

        final boolean actualValue = sharedPreferences.getBoolean(key, defaultValue);

        assertEquals(expectedValue, actualValue);
    }

    @Test
    public void canGetMapOfEveryEntry() {
        sharedPreferences
            .edit()
            .putString("a", "asd")
            .putInt("b", 1)
            .putFloat("c", 2F)
            .putLong("d", 3L)
            .putBoolean("e", true)
            .commit();

        final Map<String, ?> allEntries = sharedPreferences.getAll();

        assertEquals(5, allEntries.size());
    }
}
package com.sengami.keystoresharedpreferences;

import android.content.Context;
import android.security.KeyPairGeneratorSpec;
import android.util.Base64;
import android.util.Log;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigInteger;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.UnrecoverableEntryException;
import java.security.cert.CertificateException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.crypto.Cipher;
import javax.crypto.CipherInputStream;
import javax.crypto.CipherOutputStream;
import javax.crypto.NoSuchPaddingException;
import javax.security.auth.x500.X500Principal;

public final class KeyStoreProxy {

    public static String encryptString(final Context context,
                                       final String value) {
        try {
            final KeyStore keyStore = getKeyStore();
            final KeyStore.PrivateKeyEntry entry = getKeyStoreEntry(context, keyStore);
            final PublicKey publicKey = getPublicKey(entry);
            final Cipher encryptingCipher = getEncryptingCipher(publicKey);
            final ByteArrayOutputStream byteOutput = new ByteArrayOutputStream();
            final CipherOutputStream cipherOutputStream = getEncryptingStream(encryptingCipher, byteOutput);
            cipherOutputStream.write(value.getBytes(Constants.KEYSTORE_CHARSET));
            cipherOutputStream.close();
            return Base64.encodeToString(byteOutput.toByteArray(), Base64.DEFAULT);
        } catch (final Exception e) {
            logError("Error encrypting value: " + value, e);
            return value;
        }
    }

    public static String decryptString(final Context context,
                                       final String value) {
        try {
            final KeyStore keyStore = getKeyStore();
            final KeyStore.PrivateKeyEntry entry = getKeyStoreEntry(context, keyStore);
            final PrivateKey privateKey = getPrivateKey(entry);
            final Cipher decryptingCipher = getDecryptingCipher(privateKey);
            final CipherInputStream cipherInputStream = getDecryptingStream(decryptingCipher, value);
            byte[] bytes = getDecryptedByteArray(cipherInputStream);
            return new String(bytes, 0, bytes.length, Constants.KEYSTORE_CHARSET);
        } catch (final Exception e) {
            logError("Error decrypting value: " + value, e);
            return value;
        }
    }

    private static KeyStore getKeyStore() throws CertificateException,
                                                 NoSuchAlgorithmException,
                                                 IOException,
                                                 KeyStoreException {
        final KeyStore keyStore = KeyStore.getInstance(Constants.KEYSTORE_NAME);
        keyStore.load(null);
        return keyStore;
    }

    private static KeyStore.PrivateKeyEntry getKeyStoreEntry(final Context context,
                                                      final KeyStore keyStore) throws UnrecoverableEntryException,
                                                                                      NoSuchAlgorithmException,
                                                                                      InvalidAlgorithmParameterException,
                                                                                      KeyStoreException,
                                                                                      NoSuchProviderException {
        if (!keyStore.containsAlias(Constants.KEYSTORE_ALIAS)) {
            createNewKeyPair(context);
        }

        return getExistingKeyStoreEntry(keyStore);
    }

    private static KeyStore.PrivateKeyEntry getExistingKeyStoreEntry(final KeyStore keyStore) throws UnrecoverableEntryException,
                                                                                                     NoSuchAlgorithmException,
                                                                                                     KeyStoreException {
        return (KeyStore.PrivateKeyEntry) keyStore.getEntry(Constants.KEYSTORE_ALIAS, null);
    }

    private static void createNewKeyPair(final Context context) throws NoSuchAlgorithmException,
                                                                       InvalidAlgorithmParameterException,
                                                                       NoSuchProviderException {
        final KeyPairGenerator generator = getKeyPairGenerator();
        final KeyPairGeneratorSpec spec = getKeyPairGeneratorSpec(context);
        generator.initialize(spec);
        generator.generateKeyPair();
    }

    private static KeyPairGenerator getKeyPairGenerator() throws NoSuchProviderException,
                                                                 NoSuchAlgorithmException {
        return KeyPairGenerator.getInstance(Constants.KEYSTORE_ALGORITHM, Constants.KEYSTORE_NAME);
    }

    private static KeyPairGeneratorSpec getKeyPairGeneratorSpec(final Context context) {
        return new KeyPairGeneratorSpec.Builder(context)
                .setAlias(Constants.KEYSTORE_ALIAS)
                .setSubject(new X500Principal(Constants.KEYSTORE_CERTIFICATE_SUBJECT))
                .setSerialNumber(BigInteger.ONE)
                .setStartDate(getCertificateStartDate())
                .setEndDate(getCertificateEndDate())
                .build();
    }

    private static Date getCertificateStartDate() {
        final Calendar start = Calendar.getInstance();
        return start.getTime();
    }

    private static Date getCertificateEndDate() {
        final Calendar end = Calendar.getInstance();
        end.add(Calendar.YEAR, Constants.KEYSTORE_CERTIFICATE_VALIDITY_YEARS);
        return end.getTime();
    }

    private static PublicKey getPublicKey(final KeyStore.PrivateKeyEntry entry) {
        return entry.getCertificate().getPublicKey();
    }

    private static PrivateKey getPrivateKey(final KeyStore.PrivateKeyEntry entry) {
        return entry.getPrivateKey();
    }

    private static Cipher getEncryptingCipher(final PublicKey publicKey) throws NoSuchPaddingException,
                                                                                NoSuchAlgorithmException,
                                                                                NoSuchProviderException,
                                                                                InvalidKeyException {
        final Cipher cipher = getCipher();
        cipher.init(Cipher.ENCRYPT_MODE, publicKey);
        return cipher;
    }

    private static Cipher getDecryptingCipher(final PrivateKey privateKey) throws NoSuchPaddingException,
                                                                                  NoSuchAlgorithmException,
                                                                                  NoSuchProviderException,
                                                                                  InvalidKeyException {
        final Cipher cipher = getCipher();
        cipher.init(Cipher.DECRYPT_MODE, privateKey);
        return cipher;
    }

    private static Cipher getCipher() throws NoSuchPaddingException,
                                             NoSuchAlgorithmException,
                                             NoSuchProviderException {
        return Cipher.getInstance(Constants.KEYSTORE_TRANSFORMATION, Constants.KEYSTORE_PROVIDER);
    }

    private static CipherOutputStream getEncryptingStream(final Cipher encryptingCipher,
                                                          final ByteArrayOutputStream byteOutput) {
        return new CipherOutputStream(byteOutput, encryptingCipher);
    }

    private static CipherInputStream getDecryptingStream(final Cipher decryptingCipher,
                                                         final String value) {
        final ByteArrayInputStream byteInput = new ByteArrayInputStream(Base64.decode(value, Base64.DEFAULT));
        return new CipherInputStream(byteInput, decryptingCipher);
    }

    private static byte[] getDecryptedByteArray(final CipherInputStream decryptingStream) throws IOException {
        final List<Byte> byteList = getByteListFromInputStream(decryptingStream);
        return toByteArray(byteList);
    }

    private static List<Byte> getByteListFromInputStream(final InputStream inputStream) throws IOException {
        final List<Byte> bytes = new ArrayList<>();
        int nextByte;

        while ((nextByte = inputStream.read()) != -1) {
            bytes.add((byte) nextByte);
        }

        return bytes;
    }

    private static byte[] toByteArray(final List<Byte> byteList) {
        byte[] bytes = new byte[byteList.size()];
        for(int i = 0; i < bytes.length; i++) {
            bytes[i] = byteList.get(i);
        }

        return bytes;
    }
    
    private static void logError(final String message,
                          final Throwable throwable) {
        Log.e(Constants.TAG, message, throwable);
    }
}
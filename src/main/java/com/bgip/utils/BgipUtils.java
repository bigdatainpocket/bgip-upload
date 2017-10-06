package com.bgip.utils;


import static java.lang.Long.parseLong;
import static java.net.URLEncoder.encode;
import static org.apache.commons.codec.binary.Base64.decodeBase64;
import static org.apache.commons.codec.binary.Base64.encodeBase64;
import static org.slf4j.LoggerFactory.getLogger;

import java.util.Date;
import java.util.Properties;

import org.jasypt.encryption.pbe.StandardPBEStringEncryptor;
import org.jasypt.salt.StringFixedSaltGenerator;
import org.slf4j.Logger;

import com.bgip.exception.BgipException;

public class BgipUtils {

    private static final Logger LOGGER = getLogger(BgipUtils.class);
    private static final String UTF8_ENCODING = "UTF-8";
    private static StandardPBEStringEncryptor userEncryptor = null;

    private BgipUtils() {
    }


    private static StandardPBEStringEncryptor getUserEncryptorInstance(Properties appProperties) {
        LOGGER.info("getUserEncryptorInstance method  :: entry");
        try {
            if (userEncryptor == null) {
                userEncryptor = new StandardPBEStringEncryptor();
                userEncryptor.setPassword(appProperties.getProperty("app.userEncryptor.password"));
                // Set salt value from property file
                StringFixedSaltGenerator salt = new StringFixedSaltGenerator(appProperties.getProperty("app.userEncryptor.salt"));
                userEncryptor.setSaltGenerator(salt);
            }
        } catch (Exception ex) {
            LOGGER.error("getUserEncryptorInstance Error {}", ex);
        }
        LOGGER.info("getUserEncryptorInstance method  :: exit");
        return userEncryptor;
    }

    public static String encryptString(String toEncrypt, Properties appProperties) {
        StandardPBEStringEncryptor userEncryptorInstance = getUserEncryptorInstance(appProperties);
        byte[] userEncoded = encodeBase64((userEncryptorInstance.encrypt(toEncrypt)).getBytes());
        return new String(userEncoded);
    }

    public static String decryptString(String encryptedString, Properties appProperties) {
    	StandardPBEStringEncryptor userEncryptorInstance = getUserEncryptorInstance(appProperties);
        byte[] decoded = decodeBase64(encryptedString.getBytes());
        String decodedString = new String(decoded);
        return userEncryptorInstance.decrypt(decodedString);
    }




    public static String generateAccessToken(String username, String role, String delim, Properties appProperties) throws BgipException {
        try {
            String unencryptedString = username + delim + new Date().getTime()  + delim + role;
            String authToken = encryptString(unencryptedString, appProperties);
            return encode(authToken, UTF8_ENCODING);
        } catch (Exception ex) {
            LOGGER.error("Error during Token generation : ", ex);
            throw new BgipException(ex.getMessage(), ex);
        }
    }

    public static boolean isTokenValid(String tokenIssuedTime) {
       
        Long todayTime = new Date().getTime();
        Long timeElapsed = todayTime - parseLong(tokenIssuedTime);
        //return timeElapsed <= 3600000; // 1 hour
       return timeElapsed <= 86400000;// 24 hour
    }



    
    
}

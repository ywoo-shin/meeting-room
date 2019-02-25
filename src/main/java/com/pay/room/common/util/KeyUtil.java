package com.pay.room.common.util;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.RandomStringUtils;

@Slf4j
public class KeyUtil {
    private KeyUtil() {
        throw new AssertionError();
    }

    public static String createUuidTo16() {
        return createUuid(16);
    }

    public static String createUuid(int keyLength) {
        return RandomStringUtils.randomAlphanumeric(keyLength);
    }
}

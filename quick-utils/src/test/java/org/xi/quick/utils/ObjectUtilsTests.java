package org.xi.quick.utils;

import org.junit.Assert;
import org.junit.Test;

import java.util.HashMap;

public class ObjectUtilsTests {

    @Test
    public void isNullOrEmptyTest() {
        Assert.assertTrue(ObjectUtils.isNullOrEmpty(null));
        Assert.assertTrue(ObjectUtils.isNullOrEmpty(new Object[]{}));
        Assert.assertTrue(ObjectUtils.isNullOrEmpty(new HashMap<>()));
        Assert.assertTrue(ObjectUtils.isNullOrEmpty(""));

        Assert.assertFalse(ObjectUtils.isNullOrEmpty(new Object()));
        Assert.assertFalse(ObjectUtils.isNullOrEmpty(12));
        Assert.assertFalse(ObjectUtils.isNullOrEmpty("Hello"));
    }
}

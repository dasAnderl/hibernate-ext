package com.anderl.hibernate.ext.helper;

import org.junit.Test;

import java.util.Date;
import java.util.List;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.*;

public class ReflectionHelperTest {

    private class Dummy implements IDummy<Dummy> {
        private String s = "s";
        private String ss = "ss";

        public String getS() {
            return s;
        }

        public String getSs() {
            return ss;
        }
    }

    private interface IDummy<T> {}

    private Dummy dummy = new Dummy();

    @Test
    public void testInvokeGettersByReturnType() throws Exception {
        List<String> strings = ReflectionHelper.invokeGettersByReturnType(String.class, dummy);
        assertThat("wrong result", strings, containsInAnyOrder(dummy.getS(), dummy.getSs()));
    }

    @Test
    public void testGetGenericInterfaceType() throws Exception {
        Class type = ReflectionHelper.getGenericInterfaceType(Dummy.class, 0);
        assertEquals(type, Dummy.class);
    }

    @Test
    public void testFieldExistsRecursiveExisting() throws Exception {
        assertTrue(ReflectionHelper.fieldExistsRecursive(Dummy.class, "ss"));
        assertTrue(ReflectionHelper.fieldExistsRecursive(Dummy.class, "date.fastTime"));
    }

    @Test(expected = NoSuchFieldException.class)
    public void testFieldExistsRecursiveNonExisting() throws Exception {
        assertFalse(ReflectionHelper.fieldExistsRecursive(Dummy.class, "nonExisting"));
    }

    @Test(expected = NoSuchFieldException.class)
    public void testFieldExistsRecursiveNonExisting2() throws Exception {
        assertFalse(ReflectionHelper.fieldExistsRecursive(Dummy.class, "date.NonExisting"));
    }
}
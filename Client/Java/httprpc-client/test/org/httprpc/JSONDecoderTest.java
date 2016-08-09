/*
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.httprpc;

import java.io.IOException;
import java.io.StringReader;

import org.junit.Assert;
import org.junit.Test;

import static org.httprpc.WebServiceProxy.listOf;
import static org.httprpc.WebServiceProxy.mapOf;
import static org.httprpc.WebServiceProxy.entry;

public class JSONDecoderTest {
    @Test
    public void testString() throws IOException {
        Assert.assertTrue(decode("\"abcdéfg\"").equals("abcdéfg"));
        Assert.assertTrue(decode("\"\\b\\f\\r\\n\\t\"").equals("\b\f\r\n\t"));
        Assert.assertTrue(decode("\"\\u00E9\"").equals("é"));
    }

    @Test
    public void testNumber() throws IOException {
        Assert.assertTrue(decode("42").equals(42));
        Assert.assertTrue(decode("42").equals(42L));
        Assert.assertTrue(decode("42").equals(42F));
        Assert.assertTrue(decode("42").equals(42.0));

        Assert.assertFalse(decode("42").equals(42.5));

        Assert.assertEquals((new NumberAdapter(Integer.valueOf(42))).hashCode(), (new NumberAdapter(Long.valueOf(42L))).hashCode());

        Assert.assertTrue(decode("123.0").equals(123));
        Assert.assertTrue(decode("123.0").equals(123L));
        Assert.assertTrue(decode("123.0").equals(123F));
        Assert.assertTrue(decode("123.0").equals(123.0));

        Assert.assertFalse(decode("123.0").equals(123.5));

        Assert.assertEquals((new NumberAdapter(Float.valueOf(123F))).hashCode(), (new NumberAdapter(Double.valueOf(123.0))).hashCode());
    }

    @Test
    public void testBoolean() throws IOException {
        Assert.assertTrue(decode("true").equals(true));
        Assert.assertTrue(decode("false").equals(false));
    }

    @Test
    public void testArray() throws IOException {
        Object value = decode("[\"abc\",\t123,,,  true,\n[1, 2.0, 3.0],\n{\"x\": 1, \"y\": 2.0, \"z\": 3.0}]");

        Assert.assertTrue(value.equals(listOf(
            "abc",
            123,
            true,
            listOf(1, 2L, 3.0),
            mapOf(entry("x", 1), entry("y", 2F), entry("z", 3.0))
        )));
    }

    @Test(expected=IOException.class)
    public void testMissingObjectCommas() throws IOException {
        decode("{a:1 b:2 c:3}");
    }

    @Test(expected=IOException.class)
    public void testMissingObjectClosingBracket() throws IOException {
        decode("{a:1, b:2, c:3");
    }

    @Test(expected=IOException.class)
    public void testWrongObjectClosingBracket() throws IOException {
        decode("{a:1, b:2, c:3]");
    }

    @Test(expected=IOException.class)
    public void testMissingArrayCommas() throws IOException {
        decode("[1 2 3]");
    }

    @Test(expected=IOException.class)
    public void testMissingArrayClosingBracket() throws IOException {
        decode("[1 2 3");
    }

    @Test(expected=IOException.class)
    public void testWrongArrayClosingBracket() throws IOException {
        decode("[1 2 3}");
    }

    @Test
    public void testObject() throws IOException {
        Object value = decode("{\"a\": \"abc\", \"b\":\t123,,,  \"c\": true,\n\"d\": [1, 2.0, 3.0],\n\"e\": {\"x\": 1, \"y\": 2.0, \"z\": 3.0}}");

        Assert.assertTrue(value.equals(mapOf(
            entry("a", "abc"),
            entry("b", 123),
            entry("c", true),
            entry("d", listOf(1, 2L, 3.0)),
            entry("e", mapOf(entry("x", 1), entry("y", 2F), entry("z", 3.0)))
        )));
    }

    @SuppressWarnings("unchecked")
    private <V> V decode(String json) throws IOException {
        JSONDecoder decoder = new JSONDecoder();

        return (V)decoder.readValue(new StringReader(json));
    }
}

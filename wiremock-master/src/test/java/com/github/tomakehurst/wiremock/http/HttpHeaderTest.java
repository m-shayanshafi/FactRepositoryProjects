/*
 * Copyright (C) 2011 Thomas Akehurst
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.github.tomakehurst.wiremock.http;

import com.github.tomakehurst.wiremock.matching.ValuePattern;
import org.junit.Test;

import static org.hamcrest.Matchers.hasItems;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

public class HttpHeaderTest {

    @Test
    public void returnsIsPresentFalseWhenNoValuesPresent() {
        HttpHeader header = HttpHeader.absent("Test-Header");
        assertThat(header.isPresent(), is(false));
    }

    @Test
    public void returnsIsPresentTrueWhenOneValuePresent() {
        HttpHeader header = new HttpHeader("Test-Header", "value");
        assertThat(header.isPresent(), is(true));
    }

    @Test
    public void returnsFirstValueWhenOneSpecified() {
        HttpHeader header = new HttpHeader("Test-Header", "value");
        assertThat(header.firstValue(), is("value"));
    }

    @Test
    public void returnsAllValuesWhenManySpecified() {
        HttpHeader header = new HttpHeader("Test-Header", "value1", "value2", "value3");
        assertThat(header.values(), hasItems("value1", "value2", "value3"));
    }

    @Test
    public void correctlyIndicatesWhenHeaderContainsValue() {
        HttpHeader header = new HttpHeader("Test-Header", "value1", "value2", "value3");
        assertThat(header.containsValue("value2"), is(true));
        assertThat(header.containsValue("value72727"), is(false));
    }

    @Test(expected=IllegalStateException.class)
    public void throwsExceptionWhenAttemptingToAccessFirstValueWhenAbsent() {
        HttpHeader.absent("Something").firstValue();
    }

    @Test(expected=IllegalStateException.class)
    public void throwsExceptionWhenAttemptingToAccessValuesWhenAbsent() {
        HttpHeader.absent("Something").values();
    }

    @Test
    public void shouldMatchSingleValueToValuePattern() {
        HttpHeader header = new HttpHeader("My-Header", "my-value");

        assertThat(header.hasValueMatching(ValuePattern.equalTo("my-value")), is(true));
        assertThat(header.hasValueMatching(ValuePattern.equalTo("other-value")), is(false));
    }

    @Test
    public void shouldMatchMultiValueToValuePattern() {
        HttpHeader header = new HttpHeader("My-Header", "value1", "value2", "value3");

        assertThat(header.hasValueMatching(ValuePattern.matches("value.*")), is(true));
        assertThat(header.hasValueMatching(ValuePattern.equalTo("value2")), is(true));
        assertThat(header.hasValueMatching(ValuePattern.equalTo("value4")), is(false));
    }
}

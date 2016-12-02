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
package com.github.tomakehurst.wiremock.standalone;

import com.github.tomakehurst.wiremock.common.FileSource;
import com.github.tomakehurst.wiremock.common.ProxySettings;
import com.github.tomakehurst.wiremock.extension.ResponseTransformer;
import com.github.tomakehurst.wiremock.http.CaseInsensitiveKey;
import com.github.tomakehurst.wiremock.http.Request;
import com.github.tomakehurst.wiremock.http.ResponseDefinition;
import com.google.common.base.Optional;
import org.junit.Test;

import java.util.Map;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.assertThat;

public class CommandLineOptionsTest {

	@Test
	public void returnsVerboseTrueWhenOptionPresent() {
		CommandLineOptions options = new CommandLineOptions("--verbose");
		assertThat(options.verboseLoggingEnabled(), is(true));
	}
	
	@Test
	public void returnsVerboseFalseWhenOptionNotPresent() {
		CommandLineOptions options = new CommandLineOptions("");
		assertThat(options.verboseLoggingEnabled(), is(false));
	}

	@Test
	public void returnsRecordMappingsTrueWhenOptionPresent() {
		CommandLineOptions options = new CommandLineOptions("--record-mappings");
		assertThat(options.recordMappingsEnabled(), is(true));
	}
	
    @Test
    public void returnsHeaderMatchingEnabledWhenOptionPresent() {
    	CommandLineOptions options =  new CommandLineOptions("--match-headers", "Accept,Content-Type");
    	assertThat(options.matchingHeaders(),
                hasItems(CaseInsensitiveKey.from("Accept"), CaseInsensitiveKey.from("Content-Type")));
    }
	
	@Test
	public void returnsRecordMappingsFalseWhenOptionNotPresent() {
		CommandLineOptions options = new CommandLineOptions("");
		assertThat(options.recordMappingsEnabled(), is(false));
	}
	
	@Test
     public void setsPortNumberWhenOptionPresent() {
        CommandLineOptions options = new CommandLineOptions("--port", "8086");
        assertThat(options.portNumber(), is(8086));
    }

    @Test
    public void enablesHttpsAndSetsPortNumberWhenOptionPresent() {
        CommandLineOptions options = new CommandLineOptions("--https-port", "8443");
        assertThat(options.httpsSettings().enabled(), is(true));
        assertThat(options.httpsSettings().port(), is(8443));
    }

    @Test
    public void defaultsKeystorePathIfNotSpecifiedWhenHttpsEnabled() {
        CommandLineOptions options = new CommandLineOptions("--https-port", "8443");
        assertThat(options.httpsSettings().keyStorePath(), endsWith("/keystore"));
    }

    @Test
    public void setsRequireClientCert() {
        CommandLineOptions options = new CommandLineOptions("--https-port", "8443",
                "--https-keystore", "/my/keystore",
                "--https-truststore", "/my/truststore",
                "--https-require-client-cert");
        assertThat(options.httpsSettings().needClientAuth(), is(true));
    }

    @Test
    public void setsTrustStorePathAndPassword() {
        CommandLineOptions options = new CommandLineOptions("--https-port", "8443",
                "--https-keystore", "/my/keystore",
                "--https-truststore", "/my/truststore",
                "--truststore-password", "sometrustpwd");
        assertThat(options.httpsSettings().trustStorePath(), is("/my/truststore"));
        assertThat(options.httpsSettings().trustStorePassword(), is("sometrustpwd"));
    }

    @Test
    public void setsKeyStorePathAndPassword() {
        CommandLineOptions options = new CommandLineOptions("--https-port", "8443", "--https-keystore", "/my/keystore", "--keystore-password", "someotherpwd");
        assertThat(options.httpsSettings().keyStorePath(), is("/my/keystore"));
        assertThat(options.httpsSettings().keyStorePassword(), is("someotherpwd"));
    }

    @Test(expected=IllegalArgumentException.class)
    public void throwsExceptionIfKeyStoreSpecifiedWithoutHttpsPort() {
        new CommandLineOptions("--https-keystore", "/my/keystore");
    }
	
	@Test(expected=Exception.class)
	public void throwsExceptionWhenPortNumberSpecifiedWithoutNumber() {
		new CommandLineOptions("--port");
	}
    
    @Test
    public void returnsCorrecteyParsedBindAddress(){
        CommandLineOptions options = new CommandLineOptions("--bind-address", "127.0.0.1");
        assertThat(options.bindAddress(), is("127.0.0.1"));
    }
    
	@Test
	public void setsProxyAllRootWhenOptionPresent() {
		CommandLineOptions options = new CommandLineOptions("--proxy-all", "http://someotherhost.com/site");
		assertThat(options.specifiesProxyUrl(), is(true));
		assertThat(options.proxyUrl(), is("http://someotherhost.com/site"));
	}

    @Test
    public void setsProxyHostHeaderWithTrailingPortInformation() {
        CommandLineOptions options = new CommandLineOptions("--proxy-all", "http://someotherhost.com:8080/site");
        assertThat(options.proxyHostHeader(), is("someotherhost.com:8080"));
    }

    @Test(expected=Exception.class)
	public void throwsExceptionWhenProxyAllSpecifiedWithoutUrl() {
		new CommandLineOptions("--proxy-all");
	}
	
	@Test
	public void returnsBrowserProxyingEnabledWhenOptionSet() {
		CommandLineOptions options = new CommandLineOptions("--enable-browser-proxying");
		assertThat(options.browserProxyingEnabled(), is(true));
	}
	
	@Test
	public void setsAll() {
		CommandLineOptions options = new CommandLineOptions("--verbose", "--record-mappings", "--port", "8088", "--proxy-all", "http://somewhere.com");
		assertThat(options.verboseLoggingEnabled(), is(true));
		assertThat(options.recordMappingsEnabled(), is(true));
		assertThat(options.portNumber(), is(8088));
		assertThat(options.specifiesProxyUrl(), is(true));
		assertThat(options.proxyUrl(), is("http://somewhere.com"));
	}
	
	@SuppressWarnings("unchecked")
	@Test
	public void returnsHelpText() {
		CommandLineOptions options = new CommandLineOptions("--help");
		assertThat(options.helpText(), allOf(containsString("verbose")));
	}

    @Test
    public void returnsCorrectlyParsedProxyViaParameter() {
        CommandLineOptions options = new CommandLineOptions("--proxy-via", "somehost.mysite.com:8080");
        assertThat(options.proxyVia().host(), is("somehost.mysite.com"));
        assertThat(options.proxyVia().port(), is(8080));
    }

    @Test
    public void returnsNoProxyWhenNoProxyViaSpecified() {
        CommandLineOptions options = new CommandLineOptions();
        assertThat(options.proxyVia(), is(ProxySettings.NO_PROXY));
    }

    @Test
    public void returnsDisabledRequestJournal() {
        CommandLineOptions options = new CommandLineOptions("--no-request-journal");
        assertThat(options.requestJournalDisabled(), is(true));
    }

    @Test
    public void returnsMaxRequestJournalEntries() {
        CommandLineOptions options = new CommandLineOptions("--max-request-journal-entries", "2");
        assertThat(options.maxRequestJournalEntries(), is(Optional.of(2)));
        CommandLineOptions optionsNoMax = new CommandLineOptions("");
        assertThat(optionsNoMax.maxRequestJournalEntries().isPresent(), is(false));
    }

    @Test
    public void returnPreserveHostHeaderTrueWhenPresent() {
        CommandLineOptions options = new CommandLineOptions("--preserve-host-header");
        assertThat(options.shouldPreserveHostHeader(), is(true));
    }

    @Test
    public void returnPreserveHostHeaderFalseWhenNotPresent() {
        CommandLineOptions options = new CommandLineOptions("--port", "8080");
        assertThat(options.shouldPreserveHostHeader(), is(false));
    }

    @Test
    public void returnsCorrectlyParsedNumberOfThreads() {
        CommandLineOptions options = new CommandLineOptions("--container-threads", "300");
        assertThat(options.containerThreads(), is(300));
    }

    @Test
    public void defaultsContainerThreadsTo200() {
        CommandLineOptions options = new CommandLineOptions();
        assertThat(options.containerThreads(), is(200));
    }

    @Test
    public void returnsCorrectlyParsedJettyAcceptorThreads() {
        CommandLineOptions options = new CommandLineOptions("--jetty-acceptor-threads", "400");
        assertThat(options.jettySettings().getAcceptors().get(), is(400));
    }

    @Test
    public void returnsCorrectlyParsedJettyAcceptQueueSize() {
        CommandLineOptions options = new CommandLineOptions("--jetty-accept-queue-size", "10");
        assertThat(options.jettySettings().getAcceptQueueSize().get(), is(10));
    }

    @Test
    public void returnsCorrectlyParsedJettyHeaderBufferSize() {
        CommandLineOptions options = new CommandLineOptions("--jetty-header-buffer-size", "16384");
        assertThat(options.jettySettings().getRequestHeaderSize().get(), is(16384));
    }

    @Test
    public void returnsAbsentIfJettyAcceptQueueSizeNotSet() {
        CommandLineOptions options = new CommandLineOptions();
        assertThat(options.jettySettings().getAcceptQueueSize().isPresent(), is(false));
    }

    @Test
    public void returnsAbsentIfJettyAcceptorsNotSet() {
        CommandLineOptions options = new CommandLineOptions();
        assertThat(options.jettySettings().getAcceptors().isPresent(), is(false));
    }

    @Test
    public void returnsAbsentIfJettyHeaderBufferSizeNotSet() {
        CommandLineOptions options = new CommandLineOptions();
        assertThat(options.jettySettings().getRequestHeaderSize().isPresent(), is(false));
    }

    @Test(expected=IllegalArgumentException.class)
    public void preventsRecordingWhenRequestJournalDisabled() {
        new CommandLineOptions("--no-request-journal", "--record-mappings");
    }

    @Test
    public void returnsExtensionsSpecifiedAsClassNames() {
        CommandLineOptions options = new CommandLineOptions(
                "--extensions",
                "com.github.tomakehurst.wiremock.standalone.CommandLineOptionsTest$Ext1,com.github.tomakehurst.wiremock.standalone.CommandLineOptionsTest$Ext2");
        Map<String, ResponseTransformer> extensions = options.extensionsOfType(ResponseTransformer.class);
        assertThat(extensions.get("one"), instanceOf(Ext1.class));
        assertThat(extensions.get("two"), instanceOf(Ext2.class));
    }
    
    public static class Ext1 extends ResponseTransformer {
        @Override
        public ResponseDefinition transform(Request request, ResponseDefinition responseDefinition, FileSource files) { return null; }

        @Override
        public String name() { return "one"; }
    }

    public static class Ext2 extends ResponseTransformer {
        @Override
        public ResponseDefinition transform(Request request, ResponseDefinition responseDefinition, FileSource files) { return null; }

        @Override
        public String name() { return "two"; }
    }
}

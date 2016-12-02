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
package com.github.tomakehurst.wiremock;

import com.github.tomakehurst.wiremock.client.WireMock;
import com.github.tomakehurst.wiremock.core.Options;
import com.github.tomakehurst.wiremock.core.WireMockConfiguration;
import com.github.tomakehurst.wiremock.http.Fault;
import com.github.tomakehurst.wiremock.http.HttpClientFactory;
import com.google.common.io.Resources;
import org.apache.http.HttpResponse;
import org.apache.http.MalformedChunkCodingException;
import org.apache.http.NoHttpResponseException;
import org.apache.http.ProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.conn.ssl.SSLContexts;
import org.apache.http.conn.ssl.TrustSelfSignedStrategy;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.junit.After;
import org.junit.Test;

import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLHandshakeException;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;

import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static com.github.tomakehurst.wiremock.common.Exceptions.throwUnchecked;
import static com.github.tomakehurst.wiremock.core.WireMockConfiguration.wireMockConfig;
import static org.hamcrest.Matchers.instanceOf;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

public class HttpsAcceptanceTest {

    private static final String TRUST_STORE_PATH = toPath("test-clientstore");
    private static final String KEY_STORE_PATH = toPath("test-keystore");
    private static final String TRUST_STORE_PASSWORD = "mytruststorepassword";

    private WireMockServer wireMockServer;
    private WireMockServer proxy;
    private HttpClient httpClient;

    @After
    public void serverShutdown() {
        wireMockServer.stop();
        if (proxy != null) {
            proxy.shutdown();
        }
    }

    @Test
    public void shouldReturnStubOnSpecifiedPort() throws Exception {
        startServerWithDefaultKeystore();
        stubFor(get(urlEqualTo("/https-test")).willReturn(aResponse().withStatus(200).withBody("HTTPS content")));

        assertThat(contentFor(url("/https-test")), is("HTTPS content"));
    }

    @Test
    public void emptyResponseFault() {
        startServerWithDefaultKeystore();
        stubFor(get(urlEqualTo("/empty/response")).willReturn(
                aResponse()
                        .withFault(Fault.EMPTY_RESPONSE)));


        getAndAssertUnderlyingExceptionInstanceClass(url("/empty/response"), NoHttpResponseException.class);
    }

    @Test
    public void malformedResponseChunkFault() {
        startServerWithDefaultKeystore();
        stubFor(get(urlEqualTo("/malformed/response")).willReturn(
                aResponse()
                        .withFault(Fault.MALFORMED_RESPONSE_CHUNK)));

        getAndAssertUnderlyingExceptionInstanceClass(url("/malformed/response"), MalformedChunkCodingException.class);
    }

    @Test
    public void randomDataOnSocketFault() {
        startServerWithDefaultKeystore();
        stubFor(get(urlEqualTo("/random/data")).willReturn(
                aResponse()
                        .withFault(Fault.RANDOM_DATA_THEN_CLOSE)));

        getAndAssertUnderlyingExceptionInstanceClass(url("/random/data"), ProtocolException.class);
    }

    @Test(expected = Exception.class)
    public void throwsExceptionWhenBadAlternativeKeystore() {
        String testKeystorePath = Resources.getResource("bad-keystore").toString();
        startServerWithKeystore(testKeystorePath);
    }

    @Test
    public void acceptsAlternativeKeystore() throws Exception {
        String testKeystorePath = Resources.getResource("test-keystore").toString();
        startServerWithKeystore(testKeystorePath);
        stubFor(get(urlEqualTo("/https-test")).willReturn(aResponse().withStatus(200).withBody("HTTPS content")));

        assertThat(contentFor(url("/https-test")), is("HTTPS content"));
    }

    @Test
    public void acceptsAlternativeKeystoreWithNonDefaultPassword() throws Exception {
        String keystorePath = Resources.getResource("test-keystore-pwd").toString();
        startServerWithKeystore(keystorePath, "anotherpassword");
        stubFor(get(urlEqualTo("/alt-password-https")).willReturn(aResponse().withStatus(200).withBody("HTTPS content")));

        assertThat(contentFor(url("/alt-password-https")), is("HTTPS content"));
    }

    @Test(expected = SSLHandshakeException.class)
    public void rejectsWithoutClientCertificate() throws Exception {
        startServerEnforcingClientCert(KEY_STORE_PATH, TRUST_STORE_PATH, TRUST_STORE_PASSWORD);
        stubFor(get(urlEqualTo("/https-test")).willReturn(aResponse().withStatus(200).withBody("HTTPS content")));

        contentFor(url("/https-test")); // this lacks the required client certificate
    }

    @Test
    public void acceptWithClientCertificate() throws Exception {
        String testTrustStorePath = TRUST_STORE_PATH;
        String testClientCertPath = TRUST_STORE_PATH;

        startServerEnforcingClientCert(KEY_STORE_PATH, testTrustStorePath, TRUST_STORE_PASSWORD);
        stubFor(get(urlEqualTo("/https-test")).willReturn(aResponse().withStatus(200).withBody("HTTPS content")));

        assertThat(secureContentFor(url("/https-test"), testClientCertPath, TRUST_STORE_PASSWORD), is("HTTPS content"));
    }

    @Test
    public void supportsProxyingWhenTargetRequiresClientCert() throws Exception {
        startServerEnforcingClientCert(KEY_STORE_PATH, TRUST_STORE_PATH, TRUST_STORE_PASSWORD);
        stubFor(get(urlEqualTo("/client-cert-proxy")).willReturn(aResponse().withStatus(200)));

        proxy = new WireMockServer(wireMockConfig()
                .port(Options.DYNAMIC_PORT)
                .trustStorePath(TRUST_STORE_PATH)
                .trustStorePassword(TRUST_STORE_PASSWORD));
        proxy.start();
        proxy.stubFor(get(urlEqualTo("/client-cert-proxy")).willReturn(aResponse().proxiedFrom("https://localhost:" + wireMockServer.httpsPort())));

        HttpGet get = new HttpGet("http://localhost:" + proxy.port() + "/client-cert-proxy");
        HttpResponse response = httpClient.execute(get);
        assertThat(response.getStatusLine().getStatusCode(), is(200));
    }

    @Test
    public void proxyingFailsWhenTargetServiceRequiresClientCertificatesAndProxyDoesNotSend() throws Exception {
        startServerEnforcingClientCert(KEY_STORE_PATH, TRUST_STORE_PATH, TRUST_STORE_PASSWORD);
        stubFor(get(urlEqualTo("/client-cert-proxy-fail")).willReturn(aResponse().withStatus(200)));

        proxy = new WireMockServer(wireMockConfig().port(Options.DYNAMIC_PORT));
        proxy.start();
        proxy.stubFor(get(urlEqualTo("/client-cert-proxy-fail")).willReturn(aResponse().proxiedFrom("https://localhost:8443")));

        HttpGet get = new HttpGet("http://localhost:" + proxy.port() + "/client-cert-proxy-fail");
        HttpResponse response = httpClient.execute(get);
        assertThat(response.getStatusLine().getStatusCode(), is(500));
    }

    private String url(String path) {
        return String.format("https://localhost:%d%s", wireMockServer.httpsPort(), path);
    }

    private static String toPath(String resourcePath) {
        try {
            return new File(Resources.getResource(resourcePath).toURI()).getCanonicalPath();
        } catch (Exception e) {
            return throwUnchecked(e, String.class);
        }
    }

    private void getAndAssertUnderlyingExceptionInstanceClass(String url, Class<?> expectedClass) {
        boolean thrown = false;
        try {
            contentFor(url);
        } catch (Exception e) {
            Throwable cause = e.getCause();
            if (cause != null) {
                assertThat(e.getCause(), instanceOf(expectedClass));
            } else {
                assertThat(e, instanceOf(expectedClass));
            }

            thrown = true;
        }

        assertTrue("No exception was thrown", thrown);
    }

    private String contentFor(String url) throws Exception {
        HttpGet get = new HttpGet(url);
        HttpResponse response = httpClient.execute(get);
        String content = EntityUtils.toString(response.getEntity());
        return content;
    }

    private void startServerEnforcingClientCert(String keystorePath, String truststorePath, String trustStorePassword) {
        WireMockConfiguration config = wireMockConfig().dynamicPort().dynamicHttpsPort();
        if (keystorePath != null) {
            config.keystorePath(keystorePath);
        }
        if (truststorePath != null) {
            config.trustStorePath(truststorePath);
            config.trustStorePassword(trustStorePassword);
            config.needClientAuth(true);
        }
        config.bindAddress("localhost");

        wireMockServer = new WireMockServer(config);
        wireMockServer.start();
        WireMock.configureFor(wireMockServer.port());

        httpClient = HttpClientFactory.createClient();
    }

    private void startServerWithKeystore(String keystorePath, String keystorePassword) {
        WireMockConfiguration config = wireMockConfig().dynamicPort().dynamicHttpsPort();
        if (keystorePath != null) {
            config.keystorePath(keystorePath);
            config.keystorePassword(keystorePassword);
        }

        wireMockServer = new WireMockServer(config);
        wireMockServer.start();
        WireMock.configureFor(wireMockServer.port());

        httpClient = HttpClientFactory.createClient();
    }

    private void startServerWithKeystore(String keystorePath) {
        startServerWithKeystore(keystorePath, "password");
    }

    private void startServerWithDefaultKeystore() {
        startServerWithKeystore(null);
    }

    static String secureContentFor(String url, String clientTrustStore, String trustStorePassword) throws Exception {
        KeyStore trustStore = readKeyStore(clientTrustStore, trustStorePassword);

        // Trust own CA and all self-signed certs
        SSLContext sslcontext = SSLContexts.custom()
                .loadTrustMaterial(null, new TrustSelfSignedStrategy())
                .loadKeyMaterial(trustStore, trustStorePassword.toCharArray())
                .useTLS()
                .build();

        // Allow TLSv1 protocol only
        SSLConnectionSocketFactory sslsf = new SSLConnectionSocketFactory(
                sslcontext,
                new String[] { "TLSv1" }, // supported protocols
                null,  // supported cipher suites
                SSLConnectionSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER);

        CloseableHttpClient httpClient = HttpClients.custom()
                .setSSLSocketFactory(sslsf)
                .build();

        HttpGet get = new HttpGet(url);
        HttpResponse response = httpClient.execute(get);
        String content = EntityUtils.toString(response.getEntity());
        return content;
    }

    static KeyStore readKeyStore(String path, String password) throws KeyStoreException, IOException, NoSuchAlgorithmException, CertificateException {
        KeyStore trustStore  = KeyStore.getInstance(KeyStore.getDefaultType());
        FileInputStream instream = new FileInputStream(path);
        try {
            trustStore.load(instream, password.toCharArray());
        } finally {
            instream.close();
        }
        return trustStore;
    }
}

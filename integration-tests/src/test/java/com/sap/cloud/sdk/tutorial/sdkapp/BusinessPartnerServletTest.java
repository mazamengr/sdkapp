package com.sap.cloud.sdk.tutorial.sdkapp;

import java.net.URL;

import javax.annotation.meta.When;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.arquillian.test.api.ArquillianResource;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;

import com.sap.cloud.sdk.cloudplatform.logging.CloudLoggerFactory;
import com.sap.cloud.sdk.testutil.MockUtil;
import com.sun.xml.xsom.impl.Ref.ContentType;

import io.restassured.RestAssured;
import io.restassured.module.jsv.JsonSchemaValidator;

import static io.restassured.RestAssured.when;

@RunWith(Arquillian.class)
public class BusinessPartnerServletTest {
    private static final MockUtil mockUtil = new MockUtil();
    private static final Logger logger = CloudLoggerFactory.getLogger(BusinessPartnerServletTest.class);

    @ArquillianResource
    private URL baseUrl;

    @Deployment
    public static WebArchive createDeployment() {
        return TestUtil.createDeployment(BusinessPartnerServlet.class);
    }

    @BeforeClass
    public static void beforeClass() {
        mockUtil.mockDefaults();
        mockUtil.mockErpDestination();
    }

    @Before
    public void before() {
        RestAssured.baseURI = baseUrl.toExternalForm();
    }

    @Test
    public void testService() {
        // JSON schema validation from resource definition
        final JsonSchemaValidator jsonValidator = JsonSchemaValidator
                .matchesJsonSchemaInClasspath("businesspartners-schema.json");

        // HTTP GET response OK, JSON header and valid schema
        when()
        .get("/api/business-partners")
        .then()
                .statusCode(200)
                .contentType(io.restassured.http.ContentType.JSON)
                .body(jsonValidator);
    }
}

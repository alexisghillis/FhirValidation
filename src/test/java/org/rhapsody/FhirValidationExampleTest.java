package org.rhapsody;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.parser.IParser;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class FhirValidationExampleTest {
    private IParser jsonParser;
    private IParser xmlParser;

    @BeforeEach
    void setUp() {
        jsonParser = FhirContext.forR5().newJsonParser();
        xmlParser = FhirContext.forR5().newXmlParser();
    }

    @Test
    void testValidateFHIRResourceJsonSuccess() {
        String filePath = "src/test/resources/valid/sample.json";
        FhirValidationExample.setJsonParser(jsonParser);
        assertDoesNotThrow(() -> FhirValidationExample.validateFHIRResource(filePath));
    }

    @Test
    void testValidateFHIRResourceXmlSuccess() {
        String filePath = "src/test/resources/valid/sample.xml";
        FhirValidationExample.setXmlParser(xmlParser);
        assertDoesNotThrow(() -> FhirValidationExample.validateFHIRResource(filePath));
    }

    @Test
    void testValidateFHIRResourceUnsupportedFormat() {
        String filePath = "src/test/resources/invalid/sample.txt";
        assertDoesNotThrow(() -> FhirValidationExample.validateFHIRResource(filePath));
        // Check the console output for the "Unsupported file format" message
    }

    @Test
    void testValidateFHIRResourceParserNotSet() {
        String filePath = "src/test/resources/valid/sample.json";
        assertDoesNotThrow(() -> FhirValidationExample.validateFHIRResource(filePath));
        // Check the console output for the "Parser not set" message
    }

    @Test
    void testSetJsonParser() {
        FhirValidationExample.setJsonParser(jsonParser);
        assertNotNull(jsonParser);
    }

    @Test
    void testSetXmlParser() {
        FhirValidationExample.setXmlParser(xmlParser);
        assertNotNull(xmlParser);
    }
}

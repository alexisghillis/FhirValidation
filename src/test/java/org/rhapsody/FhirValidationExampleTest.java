package org.rhapsody;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.parser.DataFormatException;
import ca.uhn.fhir.parser.IParser;
import ca.uhn.fhir.validation.ValidationResult;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.FileNotFoundException;

import static org.junit.jupiter.api.Assertions.*;

class FhirValidationExampleTest {
    private IParser jsonParser;
    private IParser xmlParser;

    @BeforeEach
    void setUp() {
        jsonParser = FhirContext.forR5().newJsonParser();
        xmlParser = FhirContext.forR5().newXmlParser();
    }

    @Test
    void testValidateFHIRResourceJsonSuccess() throws FileNotFoundException {
        String filePath = "src/test/resources/valid/sample.json";
        FhirValidationExample.setJsonParser(jsonParser);
        ValidationResult validationResult = FhirValidationExample.validateFHIRResource(filePath);
        assertDoesNotThrow(() -> validationResult);
        assertTrue(validationResult.isSuccessful());
    }

    @Test
    void testValidateFHIRResourceXmlSuccess() throws FileNotFoundException {
        String filePath = "src/test/resources/valid/sample.xml";
        FhirValidationExample.setXmlParser(xmlParser);
        ValidationResult validationResult = FhirValidationExample.validateFHIRResource(filePath);
        assertDoesNotThrow(() -> validationResult);
        assertTrue(validationResult.isSuccessful());
    }

    @Test
    void testValidateFHIRResourceJsonFail() {
        String filePath = "src/test/resources/invalid/sample.json";
        FhirValidationExample.setJsonParser(jsonParser);

        assertThrows(DataFormatException.class, () -> FhirValidationExample.validateFHIRResource(filePath));
    }

    @Test
    void testValidateFHIRResourceXmlFail() {
        String filePath = "src/test/resources/invalid/sample.xml";
        FhirValidationExample.setXmlParser(xmlParser);
        assertThrows(DataFormatException.class, () -> FhirValidationExample.validateFHIRResource(filePath));
    }

    @Test
    void testValidateFHIRResourceUnsupportedFormat() {
        String filePath = "src/test/resources/invalid/sample.txt";
        assertThrows(DataFormatException.class, () -> FhirValidationExample.validateFHIRResource(filePath));
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

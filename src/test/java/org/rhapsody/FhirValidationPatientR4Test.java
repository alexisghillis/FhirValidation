package org.rhapsody;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.parser.DataFormatException;
import ca.uhn.fhir.parser.IParser;
import ca.uhn.fhir.validation.ValidationResult;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.FileNotFoundException;

import static org.junit.jupiter.api.Assertions.*;

public class FhirValidationPatientR4Test {

    private IParser jsonParser;
    private IParser xmlParser;
    private FhirContext fhirContext;

    @BeforeEach
    void setUp() {
        jsonParser = FhirContext.forR4().newJsonParser();
        xmlParser = FhirContext.forR4().newXmlParser();
        fhirContext = FhirContext.forR4();
    }

    @Test
    void validatePatientJsonSuccess() throws FileNotFoundException {
        String filePath = "src/test/resources/valid/valid_patient.json";
        FhirValidationExample.setJsonParser(jsonParser);
        ValidationResult validationResult = FhirValidationExample.validateFHIRResource(filePath, fhirContext);
        assertDoesNotThrow(() -> validationResult);
        assertTrue(validationResult.isSuccessful());
    }

    @Test
    void validatePatientXmlSuccess() throws FileNotFoundException {
        String filePath = "src/test/resources/valid/valid_patient.xml";
        FhirValidationExample.setXmlParser(xmlParser);
        ValidationResult validationResult = FhirValidationExample.validateFHIRResource(filePath, fhirContext);
        assertDoesNotThrow(() -> validationResult);
        assertTrue(validationResult.isSuccessful());
    }

    @Test
    void validatePatientJsonFail() {
        String filePath = "src/test/resources/invalid/invalid_patient.json";
        FhirValidationExample.setJsonParser(jsonParser);

        assertThrows(DataFormatException.class, () -> FhirValidationExample.validateFHIRResource(filePath, fhirContext));
    }

    @Test
    void validatePatientXmlFail() {
        String filePath = "src/test/resources/invalid/invalid_patient.xml";
        FhirValidationExample.setXmlParser(xmlParser);

        assertThrows(DataFormatException.class, () -> FhirValidationExample.validateFHIRResource(filePath, fhirContext));
    }
}

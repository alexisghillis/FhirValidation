package org.rhapsody;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.parser.DataFormatException;
import ca.uhn.fhir.parser.IParser;
import ca.uhn.fhir.validation.FhirValidator;
import ca.uhn.fhir.validation.ValidationResult;
import org.hl7.fhir.instance.model.api.IBaseResource;

import java.io.FileNotFoundException;
import java.io.FileReader;

public class FhirValidationExample {


    private static IParser jsonParser;
    private static IParser xmlParser;

    public static void main(String[] args) throws FileNotFoundException {
        FhirContext fhirContextR5 = FhirContext.forR5();
        setJsonParser(fhirContextR5.newJsonParser());
        setXmlParser(fhirContextR5.newXmlParser());

        validateFHIRResource("src/test/resources/valid/sample.json", fhirContextR5);
        validateFHIRResource("src/test/resources/valid/sample.xml", fhirContextR5);
        validateFHIRResource("src/test/resources/valid/valid_patient.xml", fhirContextR5);
        validateFHIRResource("src/test/resources/valid/valid_patient.json", fhirContextR5);

        FhirContext fhirContextR4 = FhirContext.forR4();
        setJsonParser(fhirContextR4.newJsonParser());
        setXmlParser(fhirContextR4.newXmlParser());

        validateFHIRResource("src/test/resources/valid/sample.json", fhirContextR4);
        validateFHIRResource("src/test/resources/valid/sample.xml", fhirContextR4);
        validateFHIRResource("src/test/resources/valid/valid_patient.xml", fhirContextR4);
        validateFHIRResource("src/test/resources/valid/valid_patient.json", fhirContextR4);


    }

    public static void setJsonParser(IParser parser) {
        jsonParser = parser;
    }

    public static void setXmlParser(IParser parser) {
        xmlParser = parser;
    }

    protected static ValidationResult validateFHIRResource(String filePath, FhirContext fhirContext) throws FileNotFoundException {

        FhirValidator validator = fhirContext.newValidator();

        IParser parser;
        if (filePath.endsWith(".json")) {
            parser = jsonParser;
        } else if (filePath.endsWith(".xml")) {
            parser = xmlParser;
        } else {
            throw new DataFormatException("Unsupported file format. Please provide a JSON or XML file.");
        }

        if (parser == null) {
            throw new DataFormatException("Parser not set. Please set the JSON or XML parser.");
        }


            // Parse resource from JSON or XML file
            IBaseResource resource = parser.parseResource(new FileReader(filePath));
        try {
            // Validate the resource
            ValidationResult result = validator.validateWithResult(resource);

            if (result.isSuccessful()) {
                System.out.println("Validation successful!");
            } else {
                System.out.println("Validation failed with errors:");
                result.getMessages().forEach(message ->
                        System.out.println(message.getLocationString() + " " + message.getMessage()));
            }
            return result;
        } catch (Exception e) {
            throw new DataFormatException(e.getMessage());
        }

    }
}
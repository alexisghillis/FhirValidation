package org.rhapsody;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.parser.IParser;
import ca.uhn.fhir.validation.FhirValidator;
import ca.uhn.fhir.validation.ValidationResult;
import org.hl7.fhir.instance.model.api.IBaseResource;

import java.io.FileReader;

public class FhirValidationExample {


    private static IParser jsonParser;
    private static IParser xmlParser;

    public static void main(String[] args) {
        setJsonParser(FhirContext.forR5().newJsonParser());
        setXmlParser(FhirContext.forR5().newXmlParser());

        validateFHIRResource("src/test/resources/valid/sample.json");

        validateFHIRResource("src/test/resources/valid/sample.xml");
    }

    public static void setJsonParser(IParser parser) {
        jsonParser = parser;
    }

    public static void setXmlParser(IParser parser) {
        xmlParser = parser;
    }

    protected static void validateFHIRResource(String filePath) {
        try {
            FhirContext fhirContext = FhirContext.forR5();
            FhirValidator validator = fhirContext.newValidator();

            // Use the set JSON or XML parser
            IParser parser;
            if (filePath.endsWith(".json")) {
                parser = jsonParser;
            } else if (filePath.endsWith(".xml")) {
                parser = xmlParser;
            } else {
                System.out.println("Unsupported file format. Please provide a JSON or XML file.");
                return;
            }

            if (parser == null) {
                System.out.println("Parser not set. Please set the JSON or XML parser.");
                return;
            }

            // Parse resource from JSON or XML file
            IBaseResource resource = parser.parseResource(new FileReader(filePath));

            // Validate the resource
            ValidationResult result = validator.validateWithResult(resource);

            if (result.isSuccessful()) {
                System.out.println("Validation successful!");
            } else {
                System.out.println("Validation failed with errors:");
                result.getMessages().forEach(message ->
                        System.out.println(message.getLocationString() + " " + message.getMessage()));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
package org.rhapsody;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.parser.IParser;
import ca.uhn.fhir.validation.ValidationResult;
import org.apache.commons.collections.map.HashedMap;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class FhirValidationPerformanceTest {

    private IParser jsonParser;
    private FhirContext fhirContext;
    private Map<Integer, String> filePathMap = new HashedMap();

    @BeforeEach
    void setUp() {
        jsonParser = FhirContext.forR4().newJsonParser();
        fhirContext = FhirContext.forR4();

        filePathMap.put(0, "src/test/resources/performance/zula_beer.json");
        filePathMap.put(1, "src/test/resources/performance/zachary_kutch.json");
        filePathMap.put(2, "src/test/resources/performance/Yi_weiss.json");
        filePathMap.put(3, "src/test/resources/performance/Willy_Koepp.json");
        filePathMap.put(4, "src/test/resources/performance/Willis_Crona.json");
    }
    @Test
    public void testFhirValidationPerformanceInParallel() throws ExecutionException, InterruptedException {

        // Create an ExecutorService with a fixed number of threads
        ExecutorService executorService = Executors.newWorkStealingPool();

        // List to store CompletableFuture for each method
        List<CompletableFuture<ValidationResult>> futures = new ArrayList<>();

        int iterator = 0;

        do {
            callValidation(executorService, futures, 0);
            callValidation(executorService, futures, 1);
            callValidation(executorService, futures, 2);
            callValidation(executorService, futures, 3);
            callValidation(executorService, futures, 4);
            iterator++;
        }while(iterator < 50);

        // Wait for all CompletableFuture tasks to complete
        CompletableFuture<Void> allOf = CompletableFuture.allOf(futures.toArray(new CompletableFuture[0]));
        allOf.get(); // Wait for completion

        // Shutdown the ExecutorService
        executorService.shutdown();
    }

    private void callValidation(ExecutorService executorService, List<CompletableFuture<ValidationResult>> futures, int pathIndex) throws InterruptedException, ExecutionException {
        CompletableFuture<ValidationResult> bigFile = CompletableFuture.supplyAsync(() -> {
            long startTime = System.currentTimeMillis();
            // Call the method you want to test
            ValidationResult result;
            try {
                result = validateJsonFhirMessage(filePathMap.get(pathIndex));
            } catch (FileNotFoundException e) {
                throw new RuntimeException(e);
            }
            long endTime = System.currentTimeMillis();
            long duration = endTime - startTime;

            System.out.println("Validation Execution Time of file name " + filePathMap.get(pathIndex) + " took " + duration + " ms");

            // Return the result for further assertions if needed
            return result;
        }, executorService);

        assertTrue(bigFile.get().isSuccessful());
        futures.add(bigFile);
    }

    private ValidationResult validateJsonFhirMessage(String filePath) throws FileNotFoundException {
        FhirValidationExample.setJsonParser(jsonParser);
        return FhirValidationExample.validateFHIRResource(filePath, fhirContext);
    }
}

package org.jrapidoc.model.generator;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import org.jrapidoc.model.ResourceListing;

import java.io.File;
import java.io.IOException;

/**
 * Created by papa on 3.4.15.
 */
public class ModelGenerator {

    public static void generateModel(ResourceListing model, File output) {
        ObjectMapper mapper = new ObjectMapper();
//        mapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
        mapper.setVisibilityChecker(mapper.getSerializationConfig().getDefaultVisibilityChecker()
                .withFieldVisibility(JsonAutoDetect.Visibility.ANY)
                .withGetterVisibility(JsonAutoDetect.Visibility.NONE)
                .withSetterVisibility(JsonAutoDetect.Visibility.NONE)
                .withCreatorVisibility(JsonAutoDetect.Visibility.NONE));
        try {
            mapper.writerWithDefaultPrettyPrinter().writeValue(output, model);
        } catch (IOException e) {
            e.printStackTrace();//TODO
        }
    }
}

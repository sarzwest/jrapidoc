package org.jrapidoc.model.generator;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import org.jrapidoc.logger.Logger;
import org.jrapidoc.model.ResourceListing;

import java.io.*;

/**
 * Created by papa on 3.4.15.
 */
public class ModelGenerator {

    public static void generateModel(Object model, File output) throws FileNotFoundException {
        FileOutputStream outputStream = null;
        try{
            outputStream = new FileOutputStream(output);
            generateModel(model, outputStream);
        }finally {
            if(outputStream != null){
                try {
                    outputStream.close();
                } catch (IOException e) {
                    Logger.warn(e, "File output stream {0} could not be closed", output.getAbsolutePath());
                }
            }
        }
    }

    public static void generateModel(Object model, OutputStream output) {
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

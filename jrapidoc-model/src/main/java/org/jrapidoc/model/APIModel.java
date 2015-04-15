package org.jrapidoc.model;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import org.apache.commons.lang3.StringUtils;
import org.jrapidoc.logger.Logger;
import org.jrapidoc.model.object.type.Type;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by papa on 23.12.14.
 */
@JsonPropertyOrder({"baseUrl", "resources"})
public class APIModel {

    private String baseUrl;
    private Map<String, Resource> resources = new HashMap<String, Resource>();
    private Map<String, Type> types;

    private APIModel(String baseUrl, Map<String, Resource> resources, Map<String, Type> types) {
        this.baseUrl = baseUrl;
        this.resources = resources;
        this.types = types;
    }

    public Map<String, Resource> getResources() {
        return resources;
    }

    public String getBaseUrl() {
        return baseUrl;
    }

    public static class APIModelBuilder {
        private String baseUrl;
        private Map<String, Resource> resources = new HashMap<String, Resource>();
        private Map<String, Type> types;

        public APIModelBuilder baseUrl(String baseUrl) {
            this.baseUrl = baseUrl;
            return this;
        }

        public APIModelBuilder resource(Resource resource) {
            String key = (StringUtils.isNotEmpty(resource.getPath()))?resource.getPath():resource.getName();
            Logger.debug(this.getClass().getSimpleName() + " " +key);
            this.resources.put(key, resource);
            return this;
        }

        public APIModelBuilder types(Map<String, Type> types) {
            this.types = types;
            return this;
        }

        public APIModel build(){
            return new APIModel(baseUrl, resources, types);
        }
    }
}

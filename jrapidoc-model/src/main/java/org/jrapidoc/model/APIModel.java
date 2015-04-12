package org.jrapidoc.model;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import org.jrapidoc.model.object.type.Type;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by papa on 23.12.14.
 */
@JsonPropertyOrder({"baseUrl", "resources"})
public class APIModel {

    private String baseUrl;
    private List<Resource> resources = new ArrayList<Resource>();
    private Map<String, Type> types;

    private APIModel(String baseUrl, List<Resource> resources, Map<String, Type> types) {
        this.baseUrl = baseUrl;
        this.resources = resources;
        this.types = types;
    }

    public List<Resource> getResources() {
        return resources;
    }

    public String getBaseUrl() {
        return baseUrl;
    }

    public static class APIModelBuilder {
        private String baseUrl;
        private List<Resource> resources = new ArrayList<Resource>();
        private Map<String, Type> types;

        public APIModelBuilder baseUrl(String baseUrl) {
            this.baseUrl = baseUrl;
            return this;
        }

        public APIModelBuilder resource(Resource resource) {
            this.resources.add(resource);
            return this;
        }

        public APIModelBuilder resources(List<Resource> resources) {
            this.resources.addAll(resources);
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

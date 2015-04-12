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
public class ResourceListing {

    private String baseUrl;
    private List<Resource> resources = new ArrayList<Resource>();
    private Map<String, Type> types;

    private ResourceListing(String baseUrl, List<Resource> resources, Map<String, Type> types) {
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

    public static class ResourceListingBuilder{
        private String baseUrl;
        private List<Resource> resources = new ArrayList<Resource>();
        private Map<String, Type> types;

        public  ResourceListingBuilder baseUrl(String baseUrl) {
            this.baseUrl = baseUrl;
            return this;
        }

        public  ResourceListingBuilder resource(Resource resource) {
            this.resources.add(resource);
            return this;
        }

        public  ResourceListingBuilder resources(List<Resource> resources) {
            this.resources.addAll(resources);
            return this;
        }

        public  ResourceListingBuilder types(Map<String, Type> types) {
            this.types = types;
            return this;
        }

        public ResourceListing build(){
            return new ResourceListing(baseUrl, resources, types);
        }
    }
}

package org.jrapidoc.model;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by papa on 23.12.14.
 */
@JsonPropertyOrder({"baseUrl", "resources"})
public class ResourceListing {

    private String baseUrl;
    private List<Resource> resources = new ArrayList<Resource>();

    private ResourceListing(String baseUrl, List<Resource> resources) {
        this.baseUrl = baseUrl;
        this.resources = resources;
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

        public  ResourceListingBuilder baseUrl(String baseUrl) {
            this.baseUrl = baseUrl;
            return this;
        }

        public  ResourceListingBuilder resource(Resource resource) {
            this.resources.add(resource);
            return this;
        }

        public ResourceListing build(){
            return new ResourceListing(baseUrl, resources);
        }
    }
}

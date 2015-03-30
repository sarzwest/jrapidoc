package org.jrapidoc.model;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by papa on 23.12.14.
 */
public class ResourceListing {

    private String baseUrl;
    private List<Resource> resources = new ArrayList<Resource>();

    public ResourceListing(String baseUrl, List<Resource> resources) {
        this.baseUrl = baseUrl;
        this.resources = resources;
    }

    public static ResourceListingBuilder baseUrl(String baseUrl) {
        return new ResourceListingBuilder().baseUrl(baseUrl);
    }

    public static ResourceListingBuilder resource(Resource resource) {
        return new ResourceListingBuilder().resource(resource);
    }

    public List<Resource> getResources() {
        return resources;
    }

    public Resource getResourceByPath(String path) {
        for(Resource resource:resources){
            if(resource.getPath().equals(path)){
                return resource;
            }
        }
        return null;
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

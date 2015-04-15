package org.jrapidoc.model;

import com.fasterxml.jackson.annotation.JsonProperty;
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
@JsonPropertyOrder({"info", "baseUrl", "resources"})
public class APIModel {

    private String baseUrl;
    @JsonProperty("info")
    private Map<String, String> customInfo;
    private Map<String, Resource> resources = new HashMap<String, Resource>();
    private Map<String, Type> types;

    private APIModel(String baseUrl, Map<String, Resource> resources, Map<String, Type> types, Map<String, String> customInfo) {
        this.baseUrl = baseUrl;
        this.resources = resources;
        this.types = types;
        this.customInfo = customInfo;
    }

    public Map<String, Resource> getResources() {
        return resources;
    }

    public String getBaseUrl() {
        return baseUrl;
    }

    public Map<String, String> getCustomInfo() {
        return customInfo;
    }

    public Map<String, Type> getTypes() {
        return types;
    }

    public static class APIModelBuilder {
        private String baseUrl;
        private Map<String, String> customInfo = new HashMap<String, String>();
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

        public APIModelBuilder customInfo(String key, String value) {
            this.customInfo.put(key, value);
            return this;
        }

        public APIModel build(){
            return new APIModel(baseUrl, resources, types, (customInfo.isEmpty())? null: customInfo);
        }
    }
}

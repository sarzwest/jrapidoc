package org.jrapidoc.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import org.jrapidoc.logger.Logger;
import org.jrapidoc.model.object.type.Type;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Tomas "sarzwest" Jiricek on 23.12.14.
 */
@JsonPropertyOrder({"info", "baseUrl", "resources"})
public class APIModel {

    private Map<String, ServiceGroup> resourceGroups;
    @JsonProperty("info")
    private Map<String, String> customInfo;
//    private Map<String, Service> resources = new HashMap<String, Service>();
    private Map<String, Type> types;

    private APIModel(/*String baseUrl, Map<String, Service> resources, */Map<String, ServiceGroup> resourceGroups, Map<String, Type> types, Map<String, String> customInfo) {
//        this.baseUrl = baseUrl;
//        this.resources = resources;
        this.resourceGroups = resourceGroups;
        this.types = types;
        this.customInfo = customInfo;
    }

    public void setCustomInfo(Map<String, String> customInfo) {
        this.customInfo = customInfo;
    }

    public void setTypes(Map<String, Type> types) {
        this.types = types;
    }

    public void setResourceGroups(Map<String, ServiceGroup> resourceGroups) {
        this.resourceGroups = resourceGroups;
    }

    public Map<String, String> getCustomInfo() {
        return customInfo;
    }

    public Map<String, Type> getTypes() {
        return types;
    }

    public Map<String, ServiceGroup> getResourceGroups() {
        return resourceGroups;
    }

    public static class APIModelBuilder {
        private Map<String, ServiceGroup> resourceGroups = new HashMap<String, ServiceGroup>();
        private Map<String, String> customInfo = new HashMap<String, String>();
        private Map<String, Type> types;

        public APIModelBuilder resourceGroup(ServiceGroup serviceGroup) {
            String key = serviceGroup.getBaseUrl();
            Logger.debug("Service group identifier: {0}", key);
            if(key == null){
                Logger.warn("Putting null key into map");
            }
            if(resourceGroups.containsKey(key)){
                Logger.warn("Overwriting resource group object, because resource group with such key {0} is already present in model", key);
            }
            this.resourceGroups.put(serviceGroup.getBaseUrl(), serviceGroup);
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
            return new APIModel(resourceGroups, types, (customInfo.isEmpty())? null: customInfo);
        }
    }
}

package org.jrapidoc.model;

import org.apache.commons.lang3.StringUtils;
import org.jrapidoc.logger.Logger;

import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

/**
 * Created by Tomas "sarzwest" Jiricek on 21.4.15.
 */
public class ServiceGroup {

    private String baseUrl;
    private String description;
    private Map<String, Service> services = new TreeMap<String, Service>();

    private ServiceGroup(String baseUrl, String description, Map<String, Service> services) {
        this.baseUrl = baseUrl;
        this.description = description;
        this.services = services;
    }

    public void setBaseUrl(String baseUrl) {
        this.baseUrl = baseUrl;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setServices(Map<String, Service> services) {
        this.services = services;
    }

    public String getBaseUrl() {
        return baseUrl;
    }

    public String getDescription() {
        return description;
    }

    public Map<String, Service> getServices() {
        return services;
    }

    public static class ServiceGroupBuilder {
        private String baseUrl;
        private String description;
        private Map<String, Service> services = new TreeMap<String, Service>();

        public ServiceGroupBuilder baseUrl(String baseUrl){
            this.baseUrl = baseUrl;
            return this;
        }
        public ServiceGroupBuilder description(String description){
            this.description = description;
            return this;
        }

        public ServiceGroupBuilder service(Service service) {
            String key = (StringUtils.isNotEmpty(service.getPath()))? service.getPath(): service.getName();
            Logger.debug("Service identifier: {0}", key);
            this.services.put(key, service);
            return this;
        }

        public ServiceGroup build(){
            return new ServiceGroup(baseUrl, description, services);
        }
    }
}

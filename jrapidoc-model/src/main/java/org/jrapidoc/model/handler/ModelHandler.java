package org.jrapidoc.model.handler;

import org.jrapidoc.model.ResourceListing;

/**
 * Created by papa on 12.4.15.
 */
public interface ModelHandler {

    void handleModel(ResourceListing model) throws HandlerException;
}

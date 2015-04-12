package org.jrapidoc.model.handler;

import org.jrapidoc.model.Method;
import org.jrapidoc.model.Resource;
import org.jrapidoc.model.ResourceListing;

import java.text.MessageFormat;

/**
 * Created by papa on 12.4.15.
 */
public class AvoidSoapOneWayMethodsHandler implements ModelHandler {
    @Override
    public void handleModel(ResourceListing model) throws HandlerException {
        for (Resource resource:model.getResources()){
            for(Method method:resource.getMethods()){
                if(method.getReturnOptions().isEmpty()){
                    throw new HandlerException(MessageFormat.format("Found one way method {1} in {0}", new Object[]{resource, method.getName()}), HandlerException.Action.STOP_ALL);
                }
            }
        }
    }
}

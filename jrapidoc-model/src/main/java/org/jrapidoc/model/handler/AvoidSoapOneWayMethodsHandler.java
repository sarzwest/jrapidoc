package org.jrapidoc.model.handler;

import org.jrapidoc.model.APIModel;
import org.jrapidoc.model.Method;
import org.jrapidoc.model.Service;
import org.jrapidoc.model.ServiceGroup;

import java.text.MessageFormat;

/**
 * Created by Tomas "sarzwest" Jiricek on 12.4.15.
 */
public class AvoidSoapOneWayMethodsHandler implements ModelHandler {
    @Override
    public void handleModel(APIModel model) throws HandlerException {
        for (ServiceGroup serviceGroup : model.getServiceGroups().values()) {
            for (Service service : serviceGroup.getServices().values()) {
                for (Method method : service.getMethods().values()) {
                    if (method.getReturnOptions().isEmpty()) {
                        throw new HandlerException(MessageFormat.format("Found one way method {1} in {0}", new Object[]{service.getName(), method.getName()}), HandlerException.Action.STOP_ALL);
                    }
                }
            }
        }
    }
}

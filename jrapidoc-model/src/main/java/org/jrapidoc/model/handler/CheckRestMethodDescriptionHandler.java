package org.jrapidoc.model.handler;

import org.apache.commons.lang3.StringUtils;
import org.jrapidoc.logger.Logger;
import org.jrapidoc.model.APIModel;
import org.jrapidoc.model.Method;
import org.jrapidoc.model.Resource;

/**
 * Created by papa on 12.4.15.
 */
public class CheckRestMethodDescriptionHandler implements ModelHandler {
    @Override
    public void handleModel(APIModel model) throws HandlerException {
        for(Resource resource:model.getResources().values()){
            for (Method method:resource.getMethods().values()){
                if(StringUtils.isEmpty(method.getDescription())){
                    Logger.warn("Method {0} has not set description", method.getPath());
                }
            }
        }
    }
}

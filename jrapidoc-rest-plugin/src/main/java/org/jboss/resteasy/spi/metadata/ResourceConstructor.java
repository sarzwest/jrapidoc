package org.jboss.resteasy.spi.metadata;

import org.jrapidoc.annotation.Description;
import org.jrapidoc.annotation.rest.IsRequired;

import java.lang.annotation.Annotation;
import java.lang.reflect.Constructor;

/**
 * @author <a href="mailto:bill@burkecentral.com">Bill Burke</a>
 * @version $Revision: 1 $
 */
public class ResourceConstructor
{
   protected ResourceClass resourceClass;
   protected Constructor constructor;
   protected ConstructorParameter[] params = {};

   public ResourceConstructor(ResourceClass resourceClass, Constructor constructor)
   {
      this.resourceClass = resourceClass;
      this.constructor = constructor;
      if (constructor.getParameterTypes() != null)
      {
         this.params = new ConstructorParameter[constructor.getParameterTypes().length];
         for (int i = 0; i < constructor.getParameterTypes().length; i++)
         {
             Description desc = null;
             IsRequired isReq = null;
             Annotation[] annotations = constructor.getParameterAnnotations()[i];
             for(Annotation a:annotations){
                 if(a.annotationType().equals(Description.class)){
                     desc = (Description)a;
                 }else if(a.annotationType().equals(IsRequired.class)){
                     isReq = (IsRequired)a;
                 }
             }
            this.params[i] = new ConstructorParameter(this, constructor.getParameterTypes()[i], constructor.getGenericParameterTypes()[i], constructor.getParameterAnnotations()[i], desc, isReq);
         }
      }
   }

   public ResourceClass getResourceClass()
   {
      return resourceClass;
   }

   public Constructor getConstructor()
   {
      return constructor;
   }

   public ConstructorParameter[] getParams()
   {
      return params;
   }
}

package org.jboss.resteasy.spi.metadata;

import org.jrapidoc.annotation.Description;

import java.lang.annotation.Annotation;
import java.lang.reflect.AccessibleObject;
import java.lang.reflect.Type;

/**
 * @author <a href="mailto:bill@burkecentral.com">Bill Burke</a>
 * @version $Revision: 1 $
 */
public class ConstructorParameter extends Parameter
{
   protected Annotation[] annotations = {};
   protected ResourceConstructor constructor;

   protected ConstructorParameter(ResourceConstructor constructor, Class<?> type, Type genericType, Annotation[] annotations, Description description)
   {
      super(constructor.getResourceClass(), type, genericType, (description == null)?null:description.value());
      this.annotations = annotations;
      this.constructor = constructor;
   }

   @Override
   public AccessibleObject getAccessibleObject()
   {
      return constructor.getConstructor();
   }

   public Annotation[] getAnnotations()
   {
      return annotations;
   }
}

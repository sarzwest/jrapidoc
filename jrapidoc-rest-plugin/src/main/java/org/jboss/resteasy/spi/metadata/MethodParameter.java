package org.jboss.resteasy.spi.metadata;

import org.jrapidoc.annotation.Description;
import org.jrapidoc.annotation.IsRequired;

import java.lang.annotation.Annotation;
import java.lang.reflect.AccessibleObject;
import java.lang.reflect.Type;

/**
 * @author <a href="mailto:bill@burkecentral.com">Bill Burke</a>
 * @version $Revision: 1 $
 */
public class MethodParameter extends Parameter
{
   protected Annotation[] annotations = {};
   protected ResourceLocator locator;

   protected MethodParameter(ResourceLocator locator, Class<?> type, Type genericType, Annotation[] annotations, Description description, IsRequired isRequired)
   {
      super(locator.getResourceClass(), type, genericType, (description == null)?null:description.value(), (isRequired == null)?null:isRequired.value());
      this.annotations = annotations;
      this.locator = locator;
   }

   @Override
   public AccessibleObject getAccessibleObject()
   {
      return locator.method;
   }

   public Annotation[] getAnnotations()
   {
      return annotations;
   }
}

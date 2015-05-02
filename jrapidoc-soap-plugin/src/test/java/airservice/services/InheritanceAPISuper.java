package airservice.services;

import javax.jws.WebMethod;

/**
 * Created by papa on 11.3.15.
 */
//@WebService(targetNamespace = "iapisuper") tady je jedno co je, tady nic nezdedim
public interface InheritanceAPISuper {

    @WebMethod(operationName = "renamed")
    public void foo();
}

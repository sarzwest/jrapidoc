package airservice.services;

import javax.jws.WebMethod;
import javax.jws.WebService;

/**
 * Created by papa on 11.3.15.
 */
@WebService//kdyz je zakomentovano tak to neni SEI
public class WithoutAnnotationService {

    @WebMethod
    public void fooMethod(){

    }
}

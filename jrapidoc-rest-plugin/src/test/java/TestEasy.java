import org.junit.Test;

import javax.ws.rs.core.GenericEntity;
import javax.ws.rs.core.Response;
import java.lang.reflect.Method;
import java.util.List;

/**
 * Created by papa on 23.3.15.
 */
public class TestEasy {

    public Response foo1(){
        return null;
    }

    public GenericEntity<List<String>> foo2(){
        return null;
    }

    public void foo3(){

    }

    public GenericEntity foo4(){
        return null;
    }

    @Test
    public void main() throws NoSuchMethodException {
        Method m1 = TestEasy.class.getMethod("foo1");
        Method m2 = TestEasy.class.getMethod("foo2");
        Method m3 = TestEasy.class.getMethod("foo3");
        Method m4 = TestEasy.class.getMethod("foo4");
    }
}

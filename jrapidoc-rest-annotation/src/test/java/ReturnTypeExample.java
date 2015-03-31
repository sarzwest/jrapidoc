import org.jrapidoc.annotation.Return;
import org.jrapidoc.annotation.Returns;

/**
 * Created by papa on 31.3.15.
 */
public class ReturnTypeExample {

    @Return(http = 200, type = Object.class, headers = {"X-Header", "X-Option"}, cookies = {"sessionid"})
    public void foo(){}

    @Returns({
            @Return(http = 200, type = Object.class, structure = Return.Structure.ARRAY, headers = {"X-Header", "X-Option"}, cookies = {"sessionid"}),
            @Return(http = 200, type = Object.class, structure = Return.Structure.MAP, headers = {"X-Header", "X-Option"}, cookies = {"sessionid"})
    })
    public void foo2(){}
}

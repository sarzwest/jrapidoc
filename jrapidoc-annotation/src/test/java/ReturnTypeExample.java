import org.jrapidoc.annotation.rest.Return;
import org.jrapidoc.annotation.rest.Returns;

/**
 * Created by papa on 31.3.15.
 */
public class ReturnTypeExample {

    @Return(http = 200, type = Object.class, headers = {"X-Header", "X-Option"}, cookies = {"sessionid"}, description = "Some description")
    public void foo(){}

    @Returns({
            @Return(http = 200, type = Object.class, structure = Return.Structure.ARRAY, headers = {"X-Header", "X-Option"}, cookies = {"sessionid"}),
            @Return(http = 200, type = Object.class, structure = Return.Structure.MAP, headers = {"X-Header", "X-Option"}, cookies = {"sessionid"})
    })
    public void foo2(){}
}

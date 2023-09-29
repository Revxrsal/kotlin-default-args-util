package revxrsal.args;

import revxrsal.args.util.DefaultFunction;
import revxrsal.args.util.DefaultFunctionFinder;
import revxrsal.args.util.MissingParameterNameLogger;

import java.lang.reflect.Method;
import java.util.HashMap;

public class Main {

    public static void main(String[] args) throws Throwable {
        MissingParameterNameLogger.setEnabled(false);

        Method testStatic = Test.class.getMethod("testStatic", String.class, String.class, String.class);
        DefaultFunction defaultMethod = DefaultFunctionFinder.findDefaultFunction(
                null,
                testStatic
        );
        defaultMethod.callByParameterNames(new HashMap<String, Object>() {{
            put("arg1", "HELLO");
//            put("arg2", "BELLO");
            put("arg3", "CELLO");
        }}, p -> p.getName().equals("arg2"));
        defaultMethod.callByParameterNames(new HashMap<String, Object>() {{
            put("arg1", "HELLO");
            put("arg2", "BELLO");
            put("arg3", "CELLO");
        }}, p -> p.getName().equals("arg2"));
    }
}
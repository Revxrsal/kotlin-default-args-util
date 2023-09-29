package revxrsal.args.reflect;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.jetbrains.annotations.NotNull;
import revxrsal.args.reflect.MethodCaller.BoundMethodCaller;
import revxrsal.args.util.Preconditions;

import java.lang.reflect.Method;
import java.lang.reflect.Parameter;

public final class MethodWrapper implements BoundMethodCaller {

    @Getter
    private final Method method;
    private final BoundMethodCaller caller;

    MethodWrapper(@NotNull Method method, @NotNull BoundMethodCaller caller) {
        this.method = method;
        this.caller = caller;
    }

    public Class<?> getDeclaringClass() {
        return method.getDeclaringClass();
    }

    public String getName() {
        return method.getName();
    }

    public Class<?>[] getParameterTypes() {
        return method.getParameterTypes();
    }

    public int getParameterCount() {
        return method.getParameterCount();
    }

    public Parameter[] getParameters() {
        return method.getParameters();
    }

    @Override
    public Object call(Object... arguments) {
        return caller.call(arguments);
    }

    public static MethodWrapper of(@NotNull Method method, @NotNull BoundMethodCaller caller) {
        Preconditions.checkNotNull(method, "method");
        Preconditions.checkNotNull(caller, "caller");
        return new MethodWrapper(method, caller);
    }

}

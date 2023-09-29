package revxrsal.args;

import org.jetbrains.annotations.NotNull;

import java.lang.reflect.Parameter;
import java.util.Map;
import java.util.function.Function;

public interface KotlinFunction {

    <T> T callByIndices(
            @NotNull Map<Integer, Object> arguments,
            @NotNull Function<Parameter, Boolean> isOptional
    );

    <T> T callByParameterNames(
            @NotNull Map<String, Object> arguments,
            @NotNull Function<Parameter, Boolean> isOptional
    );

    <T> T callByParameters(
            @NotNull Map<Parameter, Object> arguments,
            @NotNull Function<Parameter, Boolean> isOptional
    );
}

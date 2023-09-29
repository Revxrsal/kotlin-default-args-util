package revxrsal.args.util;

import lombok.Getter;
import lombok.Setter;

import java.lang.reflect.Parameter;
import java.util.StringJoiner;
import java.util.logging.Logger;

public final class MissingParameterNameLogger {

    @Getter
    @Setter
    private static boolean enabled = true;

    private static boolean warned = false;

    private MissingParameterNameLogger() {
    }

    public static void warnIfMissing(Parameter parameter) {
        if (enabled && !warned && !parameter.isNamePresent()) {
            StringJoiner message = new StringJoiner("\n");
            message.add("Parameters did not preserve their compile-time names");
            message.add("Attempting to invoke methods from parameter names may not work");
            message.add("To preserve names, compile the code with the '-parameters' flag.");
            message.add("To disable this warning, call 'MissingParameterNameLogger.setEnabled(false);'");
            Logger.getGlobal().warning(message.toString());
            warned = true;
        }
    }

}

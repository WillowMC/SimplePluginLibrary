package net.mcaurora.spl.command;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * Annotation required for every command. Contains information about the command.
 */
@Retention(RetentionPolicy.RUNTIME)
public @interface CommandData {
    String name();

    String description() default "";

    String[] aliases() default {};

    String completion() default "";

    boolean playerOnly() default true;
}

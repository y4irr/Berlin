package net.cyruspvp.hub.command;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface Command {

    String name() default "";
    String permission() default "";
    String[] aliases() default {};
    String description() default "";
    String usage() default "";
    boolean inGameOnly() default true;
}


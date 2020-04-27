package com.angel.common.valid;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.*;
import static java.lang.annotation.ElementType.TYPE_USE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

//绑定校验器
@Constraint(validatedBy = {ListValueConstraintValidator.class})
@Target({ METHOD, FIELD, ANNOTATION_TYPE, CONSTRUCTOR, PARAMETER, TYPE_USE })
@Retention(RUNTIME)
public @interface ListValue {

    //默认会从类路径下的ValidationMessages.properties文件中获取值，key就是此处指定的值：com.angel.common.valid.message
    String message() default "{com.angel.common.valid.message}";

    Class<?>[] groups() default { };

    Class<? extends Payload>[] payload() default { };
    //以上三个属性是任何一个校验注解都应该拥有的

    // 自定义一个属性
    int[] vals() default {};

}

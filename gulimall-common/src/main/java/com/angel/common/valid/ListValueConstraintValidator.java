package com.angel.common.valid;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.HashSet;
import java.util.Set;

// 泛型参数一：我们自定义的注解，泛型参数二：要校验的类型
public class ListValueConstraintValidator implements ConstraintValidator<ListValue,Integer> {

    //用来存放使用者给该注解传递的值
    private Set<Integer> set = new HashSet<Integer>();

    /**
     * 初始化方法
     * @param constraintAnnotation 该注解被使用的详细信息
     */
    @Override
    public void initialize(ListValue constraintAnnotation) {
        //拿到使用者给该注解传递的值
        int[] vals = constraintAnnotation.vals();
        for (int val : vals) {
            //将获取到的值保存起来
            set.add(val);
        }
    }

    /**
     * 在该方法内进行校验逻辑
     * @param integer 需要校验的值
     * @param constraintValidatorContext
     * @return 使用该注解的字段的值是否在该注解被使用时指定的值中
     */
    @Override
    public boolean isValid(Integer integer, ConstraintValidatorContext constraintValidatorContext) {
        // 进行校验，该处的需求是，使用该注解的字段的值必须在该注解被使用时指定的值中
        return set.contains(integer);
    }
}

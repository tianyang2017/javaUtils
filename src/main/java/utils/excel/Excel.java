package utils.excel;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * @author : tianyang
 * @description :自定义excel注解 用于导入导出
 * @date :2018年10月31日
 */
@Target(FIELD)
@Retention(RUNTIME)
@Documented
public @interface Excel {

     //导入名称全部用小写
     String name() default "";

     //导出名称无限制
     String outName() default "";

}

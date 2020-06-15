package util.springAop.sysLog;

import java.util.Date;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
/**
 * <p>          
 *       <discription>概述：方法执行时间计算工具 </discription>
 * </p>   
 * @Author          创建人：    ryl
 * @Project_name    项目名称：iis
 * @Company         公司名称：双旗科技
 * @Date            创建时间：2016年12月30日 下午12:55:45
 */
//声明这是一个切面Bean
@Aspect
//声明这是一个组件
@Component
public class classLog {
   //日志引用
   private static final Logger logger = LoggerFactory.getLogger(classLog.class);
   /**
    * <p>          
    *       <discription> 概述：配置切入点,该方法无方法体,
    *       主要为方便同类中其他方法使用此处配置的切入点 </discription>
    * </p>  
    * @Author         创建人：       RYL
    * @CreateDate     创建时间：   2016年12月30日 下午1:54:02
    * @UpdateDate     更新时间：   2016年12月30日 下午1:54:02
    * @Package_name   包名：          iis/util.springAop.sysLog
    * @Param          参数：            
    * @Rerurn         返回：          void
    */
   @Pointcut("execution(* controller..*.*(..))")
   private void aspect(){}
   /**
    * <p>          
    *       <discription> 概述：执行环绕增强实现方法块 执行时间计算 </discription>
    * </p>  
    * @Author         创建人：       RYL
    * @CreateDate     创建时间：   2016年12月30日 下午1:53:12
    * @UpdateDate     更新时间：   2016年12月30日 下午1:53:12
    * @Package_name   包名：          iis/util.springAop.sysLog
    * @Param          参数：          @param proceedingJoinPoint
    * @Param          参数：          @throws Throwable  
    * @Rerurn         返回：          void
    */
   @Around("aspect()")
   private Object classRunTimes(ProceedingJoinPoint proceedingJoinPoint) throws Throwable{
		Long startTime=new Date().getTime();
		String currentMethod=getcurrentMethod(proceedingJoinPoint);
		Object object=proceedingJoinPoint.proceed();
		Long endTime=new Date().getTime();
		String runTime=Long.toString(endTime-startTime);
		logger.debug("AOP耗时计算==> "+currentMethod+" 耗时: "+runTime+" 毫秒");
		return object;
   }
  /**
   * <p>          
   *       <discription> 概述：提取环绕增强方法日志信息 </discription>
   * </p>  
   * @Author         创建人：       RYL
   * @CreateDate     创建时间：   2016年12月30日 下午1:52:42
   * @UpdateDate     更新时间：   2016年12月30日 下午1:52:42
   * @Package_name   包名：          iis/util.springAop.sysLog
   * @Param          参数：          @param proceedingJoinPoint
   * @Param          参数：          @throws NoSuchMethodException
   * @Param          参数：          @throws SecurityException  
   * @Rerurn         返回：          String
   */
   private  String getcurrentMethod(ProceedingJoinPoint proceedingJoinPoint) throws NoSuchMethodException, SecurityException{
	    Signature sig = proceedingJoinPoint.getSignature();
        MethodSignature msig = null;
        if (!(sig instanceof MethodSignature)) {
            throw new IllegalArgumentException("该注解只能用于方法");
        }
        msig = (MethodSignature) sig;
        String message="包名: [ "+msig.getDeclaringTypeName()+" ] - 方法名: [ "+msig.getName()+"() ] -";
        return message;
   }
}

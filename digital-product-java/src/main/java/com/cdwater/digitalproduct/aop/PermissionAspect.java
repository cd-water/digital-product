package com.cdwater.digitalproduct.aop;

import com.cdwater.digitalproduct.common.anno.Permission;
import com.cdwater.digitalproduct.common.enums.ReturnMsg;
import com.cdwater.digitalproduct.common.exception.BusinessException;
import com.cdwater.digitalproduct.common.utils.ThreadUtil;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;

/**
 * 管理员日志切面
 */
@Aspect
@Component
@Slf4j
public class PermissionAspect {

    /**
     * 切入点
     */
    @Pointcut("@annotation(com.cdwater.digitalproduct.common.anno.Permission)")
    public void permissionPointCut() {
    }

    /**
     * 通知
     */
    @Before("permissionPointCut()")
    public void permissionOperation(JoinPoint joinPoint) {
        //获取注解角色
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        Permission permission = signature.getMethod().getAnnotation(Permission.class);
        Integer role = permission.value().getValue();

        if (!ThreadUtil.hasPermission(ThreadUtil.getId(), role)) {
            throw new BusinessException(ReturnMsg.FORBIDDEN_ACCESS);
        }
    }
}

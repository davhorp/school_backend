package com.school.app.audit;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.MDC;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.UUID;

@Aspect
@Component
@RequiredArgsConstructor
@Slf4j
public class AuditAspect {

    private final AuditService auditService;
    private final HttpServletRequest request;
    private final LogEncryptor logEncryptor;

    @Around("@annotation(auditable) && args(nombreUsuario, ..)")
    public Object auditAction(ProceedingJoinPoint joinPoint, Auditable auditable, String nombreUsuario) throws Throwable {
        // 1. Captura de metadatos iniciales
        String ip = request.getRemoteAddr();
        String metodoHttp = request.getMethod();
        String uri = request.getRequestURI();
        String className = joinPoint.getTarget().getClass().getSimpleName();
        String args = Arrays.toString(joinPoint.getArgs());
        // Configuración de MDC para trazabilidad en logs
        MDC.put("traceId", UUID.randomUUID().toString());
        MDC.put("userOp", nombreUsuario);
        MDC.put("clientIp", ip);
        long inicio = System.currentTimeMillis();
        Object result = null;
        boolean exito = false;
        String mensajeError = null;
        try {
            result = joinPoint.proceed();
            exito = true;
            return result;
        } catch (Exception e) {
            exito = false;
            mensajeError = e.getMessage();
            throw e; // Importante re-lanzar para no romper el flujo del servicio
        } finally {
            // 2. Lógica centralizada de finalización (Éxito o Error)
            long tiempo = System.currentTimeMillis() - inicio;
            String status = exito ? "SUCCESS" : "ERROR";
            // Log Estructurado
            log.info("type=AUDIT action={} user={} status={} duration_ms={} error={}",
                    auditable.accion(), logEncryptor.encrypt(nombreUsuario), status, tiempo, mensajeError != null ? mensajeError : "");
            // Persistencia asíncrona (Encriptamos el nombreUsuario para la DB)
            auditService.saveLog(
                    auditable.accion(),
                    logEncryptor.encrypt(nombreUsuario),
                    className, args, ip, metodoHttp, uri, tiempo, exito, mensajeError
            );
            // 3. ¡CRÍTICO! Limpieza del MDC
            MDC.clear();
        }
    }
}

package com.lctking.fluxthrottle.executor;

import com.lctking.fluxthrottle.annotation.Throttle;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import org.aspectj.lang.ProceedingJoinPoint;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
public class ThrottleArgsWrapper {
    private ProceedingJoinPoint joinPoint;

    private Throttle throttle;

    private String keyForLock;
}

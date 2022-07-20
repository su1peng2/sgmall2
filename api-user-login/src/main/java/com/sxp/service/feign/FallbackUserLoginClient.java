package com.sxp.service.feign;

import com.sxp.entity.Users;
import org.springframework.stereotype.Component;

@Component
public class FallbackUserLoginClient implements UserLoginServiceClient{
    @Override
    public Users userCheck(String name) {
        return null;
    }
}

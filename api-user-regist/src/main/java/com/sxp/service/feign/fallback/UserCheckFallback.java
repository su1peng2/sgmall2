package com.sxp.service.feign.fallback;

import com.sxp.entity.Users;
import com.sxp.service.feign.UserCheckClient;
import org.springframework.stereotype.Component;

/**
 * @author 粟小蓬
 */
@Component
public class UserCheckFallback implements UserCheckClient {
    @Override
    public Users userCheck(String name) {
        Users users = new Users();
        users.setUsername("test");
        return users;
    }
}

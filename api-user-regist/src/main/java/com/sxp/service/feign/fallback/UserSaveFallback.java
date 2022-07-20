package com.sxp.service.feign.fallback;

import com.sxp.entity.Users;
import com.sxp.service.feign.UserSaveClient;
import org.springframework.stereotype.Component;

@Component
public class UserSaveFallback implements UserSaveClient {
    @Override
    public int save(Users users) {
        return 3;
    }
}

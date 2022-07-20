package com.sxp.service.feign;

import com.sxp.entity.Users;
import com.sxp.service.feign.fallback.UserSaveFallback;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

/**
 * @author 粟小蓬
 */
@FeignClient(value = "user-save",fallback = UserSaveFallback.class)
@Repository
public interface UserSaveClient {

    @PostMapping("/user/save")
    public int save( Users users);
}

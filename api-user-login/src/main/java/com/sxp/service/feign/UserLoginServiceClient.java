package com.sxp.service.feign;

import com.sxp.entity.Users;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * @author 粟小蓬
 */
@FeignClient(value = "user-check",fallback = FallbackUserLoginClient.class)
@Repository
public interface UserLoginServiceClient {
    @GetMapping("/user/check")
    public Users userCheck(@RequestParam("name") String name);
}

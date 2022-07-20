package com.sxp.service.feign;

import com.sxp.entity.Users;
import com.sxp.service.feign.fallback.UserCheckFallback;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(value = "user-check",fallback = UserCheckFallback.class)
@Repository
public interface UserCheckClient {
    @GetMapping("/user/check")
    public Users userCheck(@RequestParam("name") String name);

}

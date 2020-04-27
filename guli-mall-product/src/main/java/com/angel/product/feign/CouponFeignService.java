package com.angel.product.feign;

import com.angel.common.to.SkuReductionTo;
import com.angel.common.to.SpuBoundsTo;
import com.angel.common.utils.R;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.Mapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "coupon-service")
public interface CouponFeignService {

    @PostMapping("/coupon/spubounds/save")
    R saveSpuBounds(@RequestBody SpuBoundsTo spuBoundsTo);

    @PostMapping("/coupon/skufullreduction/saveReductionInfo")
    R saveSkuReduction(@RequestBody SkuReductionTo skuReductionTo);
}

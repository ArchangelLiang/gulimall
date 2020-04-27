package com.angel.test;

import com.angel.product.ProductApplication;
import com.angel.product.entity.BrandEntity;
import com.angel.product.service.BrandService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@SpringBootTest(classes = ProductApplication.class)
public class ServiceTest {

    @Autowired
    private BrandService brandService;

    @Test
    public void testInsert(){
        BrandEntity brand = new BrandEntity();
        brand.setDescript("test inset");
        brand.setFirstLetter("G");
        brand.setLogo("xxxx");
        boolean save = this.brandService.save(brand);
        System.out.println(save);
    }

    @Test
    public void testSort(){
        ArrayList<Integer> integers = new ArrayList<>();
        integers.add(5);
        integers.add(1);
        integers.add(3);
        List<Integer> collect = integers.stream().sorted(Comparator.comparingInt(i -> i)).collect(Collectors.toList());
        System.out.println(collect);
    }

}

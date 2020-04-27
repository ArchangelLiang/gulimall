package com.angel.ware.vo;

import lombok.Data;

@Data
public class PurchaseDetailVo {

    //[{itemId:1,status:4,reason:""}]//完成/失败的需求详情

    private Long itemId;
    private Long status;
    private String reason;

}

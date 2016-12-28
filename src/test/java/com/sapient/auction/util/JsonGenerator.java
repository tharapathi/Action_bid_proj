package com.sapient.auction.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sapient.auction.common.model.SaleVO;
import com.sapient.auction.common.model.UserVO;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.Date;
import java.util.concurrent.TimeUnit;

/**
 * Created by dpadal on 11/14/2016.
 */
public class JsonGenerator {
    public static void main(String[] args) throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        UserVO userVO = UserVO.builder()
                .withEmail("dpadala@sapient.com").build();

        SaleVO saleVO = SaleVO.builder()
                .withStartTime(new Date())
                .withEndTime(new Date(System.currentTimeMillis() + TimeUnit.DAYS.toMillis(2)))
                .withPrice(new BigDecimal(15000))
                .withProductId("124512-Len")
                .withProductDesc("Lenovo thinkpad.")
                .withProductName("Lenovo Laptop")
                .withProductType("Electronic")
                .withUserVO(userVO).build();


        objectMapper.writeValue(System.out, saleVO);
    }
}

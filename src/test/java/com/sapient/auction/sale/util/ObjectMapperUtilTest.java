package com.sapient.auction.sale.util;

import com.sapient.auction.common.model.SaleVO;
import com.sapient.auction.common.model.UserVO;
import com.sapient.auction.sale.entity.Bid;
import com.sapient.auction.sale.entity.Sale;
import com.sapient.auction.user.entity.User;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import java.math.BigDecimal;
import java.util.Date;
import java.util.HashSet;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

/**
 * Created by dpadal on 11/15/2016.
 */
@RunWith(JUnit4.class)
public class ObjectMapperUtilTest {
    private SaleVO saleVO;

    @Before
    public void before() {
        UserVO userVO = UserVO.builder()
                .withId("treddy4")
                .withFirstName("tharapathi")
                .withLastName("reddy")
                .withEmail("treddy4@sapient.com")
                .withPassword("123456789")
                .withContact("8123717649")
                .build();

        saleVO = SaleVO.builder()
                .withId(1234L)
                .withStartTime(new Date())
                .withEndTime(new Date())
                .withPrice(new BigDecimal(15000))
                .withProductId("124512-Len")
                .withProductDesc("Lenovo thinkpad.")
                .withProductName("Lenovo Laptop")
                .withProductType("Electronic")
                .withUserVO(userVO).build();
    }

    private Sale sale() {
        Sale sale = new Sale();
        sale.setEndTime(new Date());
        sale.setProductId("abc");
        User user = new User();
        user.setEmail("treddy4@sapient.com");
        sale.setUser(user);
        Bid bid = new Bid();
        bid.setId(1);
        bid.setPrice(new BigDecimal(1500));
        User bidder = new User();
        bidder.setEmail("skukreti@sapient.com");
        bid.setUser(bidder);
        sale.setBids(new HashSet<Bid>() {
            {
                add(bid);
            }
        });
        return sale;
    }

    @Test
    public void testSaleVo() {
        Sale sale = ObjectMapperUtil.saleEntity(saleVO);
        assertNotNull(sale);
        assertEquals(sale.getId(), saleVO.getId());
        assertEquals(sale.getStartTime(), saleVO.getStartTime());
        assertEquals(sale.getEndTime(), saleVO.getEndTime());
        assertEquals(sale.getPrice(), saleVO.getPrice());
    }

    @Test
    public void testSaleEntity() {

        SaleVO saleVO = ObjectMapperUtil.saleVO(sale());
        assertNotNull(saleVO);
        assertNotNull(saleVO.getEndTime());
        assertNotNull(saleVO.getProductId());
        assertEquals(saleVO.getProductId(), "abc");
        assertNotNull(saleVO.getUser());
        assertEquals(saleVO.getUser().getEmail(), "treddy4@sapient.com");
        assertNotNull(saleVO.getBids());
        assertTrue(saleVO.getBids().size() > 0);
    }


    @After
    public void tearDown() {

    }
}

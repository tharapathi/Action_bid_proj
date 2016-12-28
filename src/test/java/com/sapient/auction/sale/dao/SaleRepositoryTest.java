package com.sapient.auction.sale.dao;

import com.sapient.auction.SapAuctionSiteApplication;
import com.sapient.auction.sale.entity.Bid;
import com.sapient.auction.sale.entity.Sale;
import com.sapient.auction.sale.repository.SaleRepository;
import com.sapient.auction.user.entity.User;
import com.sapient.auction.user.repository.UserRepository;
import org.junit.After;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.concurrent.TimeUnit;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

/**
 * Created by dpadal on 11/19/2016.
 */
@Ignore
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = SapAuctionSiteApplication.class)
@Transactional
public class SaleRepositoryTest {

    @Autowired
    private SaleRepository saleRepository;

    @Autowired
    private UserRepository userRepository;

    @Before
    public void before() {

    }

    @Test
    public void testIsSaleRepositoryNull() {
        assertNotNull(saleRepository);
    }

    @Test
    public void testDbOperation() {
        User user = userRepository.register(user());
        assertNotNull(user.getId());
        Sale sale = sale();
        sale.setUser(user);
        final Sale finalSale = sale;
        user.setSales(new HashSet<Sale>(){
            {
                add(finalSale);
            }
        });

        sale = saleRepository.create(sale);
        assertNotNull(sale.getId());

        Sale dbSale = saleRepository.detail(sale.getId());
        assertNotNull(dbSale);
        assertEquals(sale.getProductId(), dbSale.getProductId());

        List<Sale> sales = saleRepository.list();
        assertNotNull(sales);
        assertTrue(sales.size() > 0);

        Bid bid = new Bid();
        bid.setPrice(new BigDecimal(1000));
        bid.setTime(new Date());
        bid.setSale(sale);
        bid.setUser(user);
        Bid finalBid = bid;
        sale.setBids(new HashSet<Bid>() {
            {
                add(finalBid);
            }
        });

        Bid finalBid1 = bid;
        user.setBids(new HashSet<Bid>() {
            {
                add(finalBid1);
            }
        });

        Bid bid1 = saleRepository.bid(bid);
        assertNotNull(bid1);
        assertNotNull(bid1.getId());

        bid = new Bid();
        bid.setPrice(new BigDecimal(900));
        bid.setTime(new Date());
        bid.setSale(sale);
        bid.setUser(user);
        Bid finalBid2 = bid;
        sale.setBids(new HashSet<Bid>() {
            {
                add(finalBid2);
            }
        });

        Bid finalBid3 = bid;
        user.setBids(new HashSet<Bid>() {
            {
                add(finalBid3);
            }
        });

        Bid bid2 = saleRepository.bid(bid);
        assertNotNull(bid2);
        assertNotNull(bid2.getId());

        Bid dbBid = saleRepository.getLatestBid(sale.getId());
        assertNotNull(dbBid);
        assertEquals(new BigDecimal(1000), dbBid.getPrice());
    }

    @Test
    public void testIsSaleExist() {
        boolean flag = saleRepository.isSaleExist(1L);
        assertFalse(flag);
    }

    @Test
    public void testSaleList() {
        User user = userRepository.register(user());
        assertNotNull(user.getId());
        //first sale.
        Sale sale = sale();
        sale.setEndTime(new Date(System.currentTimeMillis() + TimeUnit.DAYS.toMillis(1)));
        System.out.println("End date 1 : " + sale.getEndTime());
        sale.setUser(user);
        final Sale fs1 = sale;
        user.setSales(new HashSet<Sale>(){
            {
                add(fs1);
            }
        });

        sale = saleRepository.create(sale);
        assertNotNull(sale.getId());

        //second sale.
        sale = sale();
        sale.setUser(user);
        final Sale fs2 = sale;
        user.setSales(new HashSet<Sale>(){
            {
                add(fs2);
            }
        });
        sale.setEndTime(new Date(System.currentTimeMillis() - TimeUnit.DAYS.toMillis(1)));
        System.out.println("End date 2 : " + sale.getEndTime());

        sale = saleRepository.create(sale);
        assertNotNull(sale.getId());

        //second sale.
        sale = sale();
        sale.setUser(user);
        final Sale fs3 = sale;
        user.setSales(new HashSet<Sale>(){
            {
                add(fs3);
            }
        });
        sale.setEndTime(new Date());
        System.out.println("End date 3 : " + sale.getEndTime());

        sale = saleRepository.create(sale);
        assertNotNull(sale.getId());

        List<Sale> sales = saleRepository.list();
        assertNotNull(sales);
        System.out.println("Sales ....>>>>>" + sales.size());
        //assertTrue(sales.size() == 1);
        for (Sale s : sales) {
            System.out.println("Ret Sale eTime: " + s.getEndTime());
            System.out.println("Ret Sale Id : " + s.getId());
        }
    }

    private User user() {
        User user = new User();
        user.setPassword("123456789");
        user.setFirstName("Durga");
        user.setLastName("Lovababu");
        user.setContact("8123717649");
        user.setEmail("dpadala@sapient.com");
        user.setAddress("Bangalore");
        return user;
    }

    private Sale sale() {
        Sale sale = new Sale();
        sale.setStartTime(new Date());
        sale.setEndTime(new Date());
        sale.setProductId("Lenovo-3245");
        sale.setProductName("Lenovo  tab");
        sale.setProductDesc("Lenovo tablet.");
        sale.setPrice(new BigDecimal(15000));
        sale.setProductType("Electronic");

        return sale;
    }

    @After
    public void tearDown() {

    }
}

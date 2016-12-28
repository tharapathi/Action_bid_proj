package com.sapient.auction.sale.service;

import com.sapient.auction.sale.entity.Bid;
import com.sapient.auction.sale.entity.Sale;
import com.sapient.auction.sale.exception.SaleNotFoundException;
import com.sapient.auction.sale.repository.SaleRepository;
import com.sapient.auction.sale.service.impl.SaleServiceImpl;
import com.sapient.auction.user.entity.User;
import com.sapient.auction.user.exception.UserNotFoundException;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyLong;

/**
 * JUnit test cases for SaleService, by mock the Repository class.
 *
 * Created by dpadal on 11/19/2016.
 */
@RunWith(JUnit4.class)
public class SaleServiceTest {

    @InjectMocks
    private SaleService saleService = new SaleServiceImpl();

    @Mock
    private SaleRepository saleRepository;


    @Before
    public void before() {
        MockitoAnnotations.initMocks(this);
        Mockito.when(saleRepository.create(any(Sale.class))).thenReturn(fakeSale(1L));
        Mockito.when(saleRepository.detail(anyLong())).thenAnswer(invocationOnMock -> {
           Long id = invocationOnMock.getArgumentAt(0, Long.class);
            if (id == 0) {
                return null;
            } else {
                return fakeSale(1L);
            }
        });

        Mockito.when(saleRepository.bid(any(Bid.class))).thenReturn(fakeBid());
        Mockito.when(saleRepository.getLatestBid(anyLong())).thenReturn(fakeBid());
    }

    @Test
    public void testSaleCreate() throws UserNotFoundException {
        Sale sale = saleService.create(fakeSale(1L));
        assertNotNull(sale);
        assertNotNull(sale.getId());
        assertNotNull(sale.getStartTime());
    }

    @Test(expected = Exception.class)
    public void testSaleCreateFailed() throws UserNotFoundException {
        Mockito.when(saleRepository.create(any(Sale.class))).thenThrow(Exception.class);
        Sale sale = saleService.create(fakeSale(1L));
        assertNull(sale);
    }

    @Test
    public void testSaleDetail() throws SaleNotFoundException {
        Sale sale = saleService.detail(1L);
        assertNotNull(sale);
        assertNotNull(sale.getId());
    }

    @Test(expected = SaleNotFoundException.class)
    public void testSaleDetailFailed() throws SaleNotFoundException {
        Sale sale = saleService.detail(0L);
        assertNull(sale);
    }

    @Test
    public void testSaleList() throws SaleNotFoundException {
        Mockito.when(saleRepository.list()).thenReturn(Arrays.asList(fakeSale(1L)));
        List<Sale> sales = saleService.list();
        assertNotNull(sales);
        assertTrue(sales.size() > 0);
    }

    @Test(expected = SaleNotFoundException.class)
    public void testSaleListFailed() throws SaleNotFoundException {
        Mockito.when(saleRepository.list()).thenReturn(null);
        List<Sale> sales = saleService.list();
        assertNull(sales);
    }

    private Sale fakeSale(Long id) {
        Sale sale = new Sale();
        sale.setId(id);
        sale.setEndTime(new Date());
        sale.setPrice(new BigDecimal(15000));
        sale.setProductId("Lenovo-3245");
        sale.setProductName("Lenovo  tab");
        sale.setProductDesc("Lenovo tablet.");
        sale.setProductType("Electronic");
        return sale;
    }


    private Bid fakeBid() {

        Bid fakeBid = new Bid();
        fakeBid.setId(1);
        fakeBid.setPrice(new BigDecimal(1000));
        fakeBid.setSale(fakeSale(1L));
        fakeBid.setUser(new User());
        return fakeBid;
    }

}

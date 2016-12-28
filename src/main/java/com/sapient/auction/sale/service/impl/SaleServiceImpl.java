package com.sapient.auction.sale.service.impl;

import com.sapient.auction.sale.entity.Bid;
import com.sapient.auction.sale.entity.Sale;
import com.sapient.auction.sale.exception.InvalidBidAmountException;
import com.sapient.auction.sale.exception.SaleNotFoundException;
import com.sapient.auction.sale.repository.SaleRepository;
import com.sapient.auction.sale.service.SaleService;
import com.sapient.auction.user.exception.UserNotFoundException;
import com.sapient.auction.user.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.util.Date;
import java.util.List;

/**
 * SaleService implementation class, responsible for create/get Sale info and bid.
 * <p>
 * Created by dpadal on 11/14/2016.
 */
@Service
@Slf4j
public class SaleServiceImpl implements SaleService {

    @Autowired
    private SaleRepository saleRepository;

    @Autowired
    private UserRepository userRepository;

    /**
     * Create new Sale.
     * Set the sale time to current time.
     *
     * @param sale
     * @return
     * @throws UserNotFoundException
     */
    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public Sale create(Sale sale) throws UserNotFoundException {
        sale.setStartTime(new Date());
        saleRepository.create(sale);
        log.info("Sale record saved in db with id: {}", sale.getId());
        return sale;
    }

    /**
     * Returns the Sale info for specified sale id.
     * throws <code>SaleNotFoundException</code> if sale not exist.
     *
     * @param id
     * @return
     * @throws SaleNotFoundException
     */
    @Override
    @Transactional(readOnly = true)
    public Sale detail(long id) throws SaleNotFoundException {
        log.info("Fetching the sale id: {}", id);
        Sale sale = saleRepository.detail(id);
        if (sale == null) {
            throw new SaleNotFoundException("Sale not found .");
        }
        //to avoid lazy initialization exception.
        sale.getUser().getEmail();
        for (Bid bid : sale.getBids()) {
            bid.getUser().getEmail();
        }
        return sale;
    }

    /**
     * Returns List sales.
     * Throws <code>SaleNotFoundException</code> if zero sales.
     * @return
     * @throws SaleNotFoundException
     */
    @Override
    @Transactional(readOnly = true)
    public List<Sale> list() throws SaleNotFoundException {
        List<Sale> sales = saleRepository.list();
        if (CollectionUtils.isEmpty(sales)) {
            throw new SaleNotFoundException("Sales not found.");
        }
        log.info("Fetched no of sales: {}", sales.size());
        //To Avoid lazy initialization exception.
        for (Sale sale : sales) {
            sale.getUser().getEmail();
            for (Bid bid : sale.getBids()) {
                bid.getUser().getEmail();
            }
        }
        return sales;
    }

    /**
     * Post bid on sale.
     * Throws <code>SaleNotFoundException</code>, when the requested sale id not exist.
     * Throws <code>InvalidBidAmountException</code>, if the Bid price is less than existing bid for the sale.
     * @param bid
     * @return
     * @throws SaleNotFoundException
     * @throws InvalidBidAmountException
     */
    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public Bid bid(Bid bid) throws SaleNotFoundException, InvalidBidAmountException {
        Sale sale = saleRepository.detail(bid.getSale().getId());
        if (sale == null) {
            throw new SaleNotFoundException("Sale not found.");
        } else {
            boolean isBidPriceLess = false;
            if (!CollectionUtils.isEmpty(sale.getBids())) {
                isBidPriceLess = sale.getBids().stream().anyMatch(existingBid -> {
                    if (existingBid.getPrice().compareTo(bid.getPrice()) >= 0) {
                        return true;
                    } else {
                        return false;
                    }
                });
            }

            if (!isBidPriceLess) {
                bid.setTime(new Date());
                bid.setSale(sale);
                saleRepository.bid(bid);
                log.info("Bid record saved in db with id: {}", bid.getId());
            } else {
                throw new InvalidBidAmountException("Bid price is Invalid.");
            }
        }
        return bid;
    }

    /**
     * Get the Latest Bid.
     * Returns the bid which is higher price.
     * Throws <code>SaleNotFoundException</code>, when the sale id not exist.
     *
     * @param saleId
     * @return
     * @throws SaleNotFoundException
     */
    @Override
    @Transactional(readOnly = true)
    public Bid getLatestBid(Long saleId) throws SaleNotFoundException {
        if (!saleRepository.isSaleExist(saleId)) {
            throw new SaleNotFoundException("Sale not found.");
        } else {
            Bid bid = saleRepository.getLatestBid(saleId);
            bid.getUser().getEmail();
            bid.getSale().getProductId();
            return bid;
        }
    }
}

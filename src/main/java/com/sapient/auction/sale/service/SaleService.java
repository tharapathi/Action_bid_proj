package com.sapient.auction.sale.service;

import com.sapient.auction.sale.entity.Bid;
import com.sapient.auction.sale.entity.Sale;
import com.sapient.auction.sale.exception.InvalidBidAmountException;
import com.sapient.auction.sale.exception.SaleNotFoundException;
import com.sapient.auction.user.exception.UserNotFoundException;

import java.util.List;

/**
 * Created by dpadal on 11/14/2016.
 */
public interface SaleService {

    Sale create(Sale sale) throws UserNotFoundException;

    Sale detail(long id) throws SaleNotFoundException;

    List<Sale> list() throws SaleNotFoundException;

    Bid bid(Bid bid) throws SaleNotFoundException, InvalidBidAmountException;

    Bid getLatestBid(Long saleId) throws SaleNotFoundException;
}

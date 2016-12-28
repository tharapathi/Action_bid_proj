package com.sapient.auction.sale.repository;

import com.sapient.auction.sale.entity.Bid;
import com.sapient.auction.sale.entity.Sale;

import java.util.List;

/**
 * JPA repository class, to perform all Sale related CRUD operations and lookup.
 *
 * Created by dpadal on 11/14/2016.
 */
public interface SaleRepository {
	Sale create(Sale sale);

	Sale detail(long id);

	boolean isSaleExist(long id);

	List<Sale> list();

	Bid bid(Bid bid);

	Bid getLatestBid(Long saleId);
}

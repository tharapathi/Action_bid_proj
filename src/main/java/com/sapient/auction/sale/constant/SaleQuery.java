package com.sapient.auction.sale.constant;

public final class SaleQuery {
	public static final String SALE_ID = "from Sale where id = :id";
	public static final String ALL_SALE = "from Sale sale where parsedateTime(sale.endTime, 'yyyy-MM-dd') >= " +
			"parsedateTime(now(), 'yyyy-MM-dd')";
	public static final String IS_SALE_EXIST = "Select count(*) from SALE where id = :saleId";
	public static final String LATEST_BID = "select bid from Bid bid where bid.sale.id = :saleId and bid.price = " +
			"(select max(b.price) from Bid b where b.sale.id = :saleId)";
}

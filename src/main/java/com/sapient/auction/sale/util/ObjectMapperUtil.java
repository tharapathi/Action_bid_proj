package com.sapient.auction.sale.util;

import com.sapient.auction.common.model.BidVO;
import com.sapient.auction.common.model.SaleVO;
import com.sapient.auction.common.model.UserVO;
import com.sapient.auction.sale.entity.Bid;
import com.sapient.auction.sale.entity.Sale;
import com.sapient.auction.user.entity.User;
import org.springframework.beans.BeanUtils;

import java.util.stream.Collectors;

/**
 * Object conversion util class, used to convert Entity to Model and vice versa.
 * <p>
 * Created by dpadal on 11/14/2016.
 */
public final class ObjectMapperUtil {

    public static Sale saleEntity(SaleVO saleVO) {
        Sale saleEntity = new Sale();
        BeanUtils.copyProperties(saleVO, saleEntity);
        return saleEntity;
    }

    public static SaleVO saleVO(Sale saleEntity) {
        SaleVO saleVO = SaleVO.builder()
                .withId(saleEntity.getId())
                .withStartTime(saleEntity.getStartTime())
                .withEndTime(saleEntity.getEndTime())
                .withPrice(saleEntity.getPrice())
                .withProductId(saleEntity.getProductId())
                .withProductName(saleEntity.getProductName())
                .withProductDesc(saleEntity.getProductDesc())
                .withProductType(saleEntity.getProductType())
                .withProductImageUrl(saleEntity.getProductImageUrl())
                .withUserVO(
                        user(saleEntity.getUser())
                ).withBids(
                        saleEntity.getBids().stream().map(bid ->
                                bidVO(bid)
                        ).collect(Collectors.toSet())
                ).build();
        return saleVO;
    }

    public static Bid bidEntity(BidVO bidVO) {
        Bid bid = new Bid();
        BeanUtils.copyProperties(bidVO, bid);
        Sale sale = new Sale();
        BeanUtils.copyProperties(bidVO.getSale(), sale);
        bid.setSale(sale);
        return bid;
    }

    public static BidVO bidVO(Bid bid) {
        BidVO bidVO = BidVO.builder()
                .withId(bid.getId())
                .withPrice(bid.getPrice())
                .withTime(bid.getTime())
                .withUser(user(bid.getUser()))
                .withSale(
                        SaleVO.builder().withId(bid.getSale().getId())
                                .withPrice(bid.getSale().getPrice())
                                .withProductName(bid.getSale().getProductName())
                                .withProductType(bid.getSale().getProductType())
                                .withStartTime(bid.getSale().getStartTime())
                                .withEndTime(bid.getSale().getEndTime())
                                .build()
                ).build();

        return bidVO;
    }

    private static UserVO user(User user) {
        return UserVO.builder().withEmail(user.getEmail())
                .withContact(user.getContact())
                .withFirstName(user.getFirstName()).build();
    }

}

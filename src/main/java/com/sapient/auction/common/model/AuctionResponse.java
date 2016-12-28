package com.sapient.auction.common.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

/**
 * JSON class used to return the response. All resources/Apis must return this instance as response.
 *
 * Created by dpadal on 11/10/2016.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
@NoArgsConstructor
@Getter
public class AuctionResponse {

    private int statusCode;
    private String message;
    private Set<ErrorVO> errorVOs;
    private Set<SaleVO> saleVOs;
    private UserVO userVO;
    private BidVO bidVO;

    private AuctionResponse(Builder builder) {
        this.statusCode = builder.statusCode;
        this.message = builder.message;
        this.errorVOs = builder.errorVOs;
        this.saleVOs = builder.saleVOs;
        this.userVO = builder.userVO;
        this.bidVO = builder.bidVO;
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private int statusCode;
        private String message;
        private Set<ErrorVO> errorVOs;
        private Set<SaleVO> saleVOs;
        private UserVO userVO;
        private BidVO bidVO;

        public final AuctionResponse build() {
            return new AuctionResponse(this);
        }

        public Builder withStatusCode(int statusCode) {
            this.statusCode = statusCode;
            return this;
        }

        public Builder withMessage(String message) {
            this.message = message;
            return this;
        }

        public Builder withErrorVO(ErrorVO errorVO) {
            if (this.errorVOs == null) {
                this.errorVOs = new HashSet<>();
            }
            this.errorVOs.add(errorVO);
            return this;
        }

        public Builder withSaleVO(SaleVO saleVO) {
            if (this.saleVOs == null) {
                this.saleVOs = new HashSet<>();
            }
            this.saleVOs.add(saleVO);
            return this;
        }

        public Builder withSaleVO(Set<SaleVO> saleVOs) {
            if (this.saleVOs == null) {
                this.saleVOs = new HashSet<>();
            }
            this.saleVOs.addAll(saleVOs);
            return this;
        }

        public Builder withUserVO(UserVO userVO) {
            this.userVO = userVO;
            return this;
        }
        
        public Builder withBidVO(BidVO bidVO) {
            this.bidVO = bidVO;
            return this;
        }
    }
}

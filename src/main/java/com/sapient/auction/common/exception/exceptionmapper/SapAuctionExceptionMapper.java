package com.sapient.auction.common.exception.exceptionmapper;

import com.sapient.auction.common.exception.SapAuctionException;
import com.sapient.auction.common.model.AuctionResponse;
import com.sapient.auction.common.model.ErrorVO;
import lombok.extern.slf4j.Slf4j;

import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

/**
 * Globally Handling exceptions thrown by application and respond back to the user with error message and status code.
 * <p>
 * Created by dpadal on 11/11/2016.
 */
@Slf4j
@Provider
public class SapAuctionExceptionMapper implements ExceptionMapper<SapAuctionException> {
    public SapAuctionExceptionMapper() {
        log.info("Exception mapper registered.");
    }

    @Override
    public Response toResponse(SapAuctionException e) {
        e.printStackTrace();
        log.error("Exception occured, statusCode: {} & message: {}", e.getStatusCode(), e.getMessage());
        ErrorVO errorVO = ErrorVO.builder()
                .withCode(e.getStatusCode())
                .withMessage(e.getMessage()).build();
        return Response.status(e.getStatusCode()).entity(
                AuctionResponse.builder().withStatusCode(e.getStatusCode()).withErrorVO(errorVO).build())
                .build();
    }
}

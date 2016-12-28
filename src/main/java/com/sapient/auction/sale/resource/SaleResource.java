package com.sapient.auction.sale.resource;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.stream.Collectors;

import javax.validation.Valid;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;

import com.sapient.auction.sale.exception.InvalidBidAmountException;
import org.springframework.beans.factory.annotation.Autowired;

import com.sapient.auction.common.exception.AuthenticationFailedException;
import com.sapient.auction.common.exception.SapAuctionException;
import com.sapient.auction.common.model.AuctionResponse;
import com.sapient.auction.common.model.BidVO;
import com.sapient.auction.common.model.SaleVO;
import com.sapient.auction.common.security.AuthenticationHelper;
import com.sapient.auction.common.security.session.SessionUser;
import com.sapient.auction.sale.entity.Bid;
import com.sapient.auction.sale.entity.Sale;
import com.sapient.auction.sale.exception.SaleNotFoundException;
import com.sapient.auction.sale.service.SaleService;
import com.sapient.auction.sale.util.ObjectMapperUtil;

import lombok.extern.slf4j.Slf4j;

/**
 * Sale resource class.
 * <p>
 * Created by dpadal on 11/14/2016.
 */
@Path(value = "/sale")
@Produces(MediaType.APPLICATION_JSON)
@Slf4j
public class SaleResource {

    @Context
    private UriInfo uriInfo;

    @Autowired
    private SaleService saleService;

    /**
     * Create brand new Sale/Auction.
     * Request must have "Authorization" header with base64 encoded value (email:<hashed password>).
     *
     * @return Response.
     * @throws SapAuctionException
     */
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    public Response create(@Valid SaleVO saleVO) throws SapAuctionException, URISyntaxException {
        Sale saleEntity;
        try {
            AuthenticationHelper.isRequestAuthenticated(SessionUser.getSessionUser());
            saleEntity = ObjectMapperUtil.saleEntity(saleVO);
            saleEntity.setUser(SessionUser.getSessionUser());
            saleService.create(saleEntity);
        } catch (AuthenticationFailedException ae) {
            throw new SapAuctionException(Response.Status.UNAUTHORIZED.getStatusCode(),
                    "Authentication failed.");
        } catch (Exception e) {
            log.error("Exception: ", e);
            throw new SapAuctionException(Response.Status.INTERNAL_SERVER_ERROR.getStatusCode(),
                    "Unable to process the request, please try again.");
        }
        return Response.created(new URI(uriInfo.getAbsolutePath() + "/" + saleEntity.getId())).entity(
                AuctionResponse.builder().withStatusCode(Response.Status.CREATED.getStatusCode())
                        .withMessage(String.format("Sale %d created successfuly.", saleEntity.getId()))
                        .withSaleVO(SaleVO.builder().withId(saleEntity.getId())
                                .withProductId(saleEntity.getProductId()).build())
                        .build()
        ).build();
    }

    /**
     * Returns the requested Sale details.
     * Request must have "Authorization" header with base64 encoded value (email:<hashed password>).
     *
     * @return Response.
     * @throws SapAuctionException
     */
    @GET
    @Path("/{saleId}")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response detail(@PathParam("saleId") long saleId) throws SapAuctionException {
        log.info("Processing Sale detail request for Id: {}", saleId);
        Sale saleEntity;
        try {
            AuthenticationHelper.isRequestAuthenticated(SessionUser.getSessionUser());
            saleEntity = saleService.detail(saleId);
        } catch (SaleNotFoundException se) {
            throw new SapAuctionException(Response.Status.NOT_FOUND.getStatusCode(), se.getMessage());
        } catch (AuthenticationFailedException ae) {
            throw new SapAuctionException(Response.Status.UNAUTHORIZED.getStatusCode(), "Authentication failed.");
        } catch (Exception e) {
            log.error("Exception: ", e);
            throw new SapAuctionException(Response.Status.INTERNAL_SERVER_ERROR.getStatusCode(),
                    "Unable to process the request, please try again.");
        }
        return Response.ok().entity(
                AuctionResponse.builder().withStatusCode(Response.Status.OK.getStatusCode())
                        .withSaleVO(ObjectMapperUtil.saleVO(saleEntity)).build()
        ).build();
    }

    /**
     * Return List of all Sales which are in active.
     * Request must have "Authorization" header with base64 encoded value (email:<hashed password>).
     *
     * @return Response
     * @throws SapAuctionException
     */
    @GET
    @Path("/list")
    public Response list() throws SapAuctionException {
        List<Sale> sales;
        try {
            AuthenticationHelper.isRequestAuthenticated(SessionUser.getSessionUser());
            sales = saleService.list();
        } catch (SaleNotFoundException se) {
            throw new SapAuctionException(Response.Status.NOT_FOUND.getStatusCode(), se.getMessage());
        } catch (AuthenticationFailedException ae) {
            throw new SapAuctionException(Response.Status.UNAUTHORIZED.getStatusCode(), "Authentication failed.");
        } catch (Exception e) {
            log.error("Exception: ", e);
            throw new SapAuctionException(Response.Status.INTERNAL_SERVER_ERROR.getStatusCode(),
                    "Unable to process the request, please try again.");
        }
        return Response.ok().entity(
                AuctionResponse.builder().withStatusCode(Response.Status.OK.getStatusCode())
                        .withSaleVO(sales.stream().map(sale
                                -> ObjectMapperUtil.saleVO(sale)).collect(Collectors.toSet())).build()
        ).build();
    }

    /**
     * Bid for sale.
     * Request must have "Authorization" header with base64 encoded value (email:<hashed password>).
     *
     * @return Response
     * @throws SapAuctionException
     */
    @POST
    @Path("/{saleId}/bid")
    public Response bid(@Valid BidVO bidVO) throws SapAuctionException, URISyntaxException {
        Bid bidEntity;
        try {
            AuthenticationHelper.isRequestAuthenticated(SessionUser.getSessionUser());
            bidEntity = ObjectMapperUtil.bidEntity(bidVO);
            bidEntity.setUser(SessionUser.getSessionUser());
            bidEntity = saleService.bid(bidEntity);
        } catch (SaleNotFoundException se) {
            throw new SapAuctionException(Response.Status.BAD_REQUEST.getStatusCode(), se.getMessage());
        } catch (AuthenticationFailedException ae) {
            throw new SapAuctionException(Response.Status.UNAUTHORIZED.getStatusCode(), "Authentication failed.");
        } catch (InvalidBidAmountException ie) {
            throw new SapAuctionException(Response.Status.BAD_REQUEST.getStatusCode(), ie.getMessage());
        } catch (Exception ex) {
            log.error("Exception: ", ex);
            throw new SapAuctionException(Response.Status.INTERNAL_SERVER_ERROR.getStatusCode(),
                    "Unable to process the request, please try again.");
        }
        return Response.created(new URI(
                uriInfo.getAbsolutePath() + "/sale/" + bidEntity.getSale().getId() + "/bid/" + bidEntity.getId()))
                .entity(AuctionResponse.builder().withStatusCode(Response.Status.CREATED.getStatusCode())
                        .withMessage("Bid posted successfully.")
                        .withBidVO(BidVO.builder().withId(bidEntity.getId()).build())
                        .build())
                .build();
    }

    /**
     * Retrieves the Latest Bid for the specified Sale.
     * Request must have "Authorization" header with base64 encoded value (email:<hashed password>).
     *
     * @return Response.
     * @throws SapAuctionException
     */
    @GET
    @Path("/{saleId}/bid")
    public Response latestBid(@PathParam("saleId") long saleId) throws SapAuctionException {
        Bid bid;
        try {
            AuthenticationHelper.isRequestAuthenticated(SessionUser.getSessionUser());
            bid = saleService.getLatestBid(saleId);
        } catch (AuthenticationFailedException ae) {
            throw new SapAuctionException(Response.Status.UNAUTHORIZED.getStatusCode(), "Authentication failed.");
        } catch (Exception ex) {
            log.error("Exception: ", ex);
            throw new SapAuctionException(Response.Status.INTERNAL_SERVER_ERROR.getStatusCode(),
                    "Unable to process the request, please try again.");
        }
        return Response.ok().entity(
                AuctionResponse.builder().withStatusCode(Response.Status.OK.getStatusCode())
                        .withBidVO(ObjectMapperUtil.bidVO(bid)).build()
        ).build();
    }

}

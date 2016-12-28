package com.sapient.auction.common.config;

import com.sapient.auction.common.exception.exceptionmapper.SapAuctionExceptionMapper;
import com.sapient.auction.sale.resource.SaleResource;
import com.sapient.auction.user.resource.UserResource;
import org.glassfish.jersey.server.ResourceConfig;

import javax.ws.rs.ApplicationPath;

/**
 * Jersey resource configuration class.
 *
 * Created by dpadal on 11/11/2016.
 */
@ApplicationPath("/auction")
public class RestConfig extends ResourceConfig{

    public RestConfig() {
        //TODO: add other Resouce classes here with comma separated.
        registerClasses(UserResource.class, SaleResource.class, SapAuctionExceptionMapper.class);
    }
}

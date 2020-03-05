package com.ally.demo.api.repository;


import com.ally.demo.api.config.SqlQueryConfigProperties;
import com.ally.demo.api.model.AuctionItem;
import com.ally.demo.api.model.Bid;
import com.ally.demo.api.model.Item;
import com.ally.demo.api.utils.Constants;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;

import java.util.*;

import static java.lang.Math.max;

@Slf4j
@Repository
public class DBRepositoryImpl implements DBRepository{

    @Autowired
    @Qualifier("appJdbcTemplate")
    JdbcTemplate appJdbcTemplate;

    @Autowired
    @Qualifier("appNamedParameterJdbcTemplate")
    NamedParameterJdbcTemplate appNamedParameterJdbcTemplate;

    @Autowired
    SqlQueryConfigProperties sqlQuery;

    @Override
    public AuctionItem getAuctionItem(int auctionItemId) {
        log.info("AuctionId: " + auctionItemId);
        AuctionItem auctionItem = appJdbcTemplate.query(sqlQuery.getSQL_SELECT_AUCTION_ITEM(), new Integer[] {auctionItemId}, new AuctionItemMapper());
        return auctionItem;
    }

    @Override
    public List<AuctionItem> getAuctionItems() {
        log.info("Retrieving auction items ...");
        List<AuctionItem> auctionItems = appJdbcTemplate.query(sqlQuery.getSQL_SELECT_AUCTION_ITEMS(),new AuctionItemsMapper());
        return auctionItems;
    }

    @Override
    public AuctionItem saveAuctionItem(AuctionItem auctionItem) {
        log.info("Saving Auction Item ...");

        String itemId = Optional.ofNullable(auctionItem).map(AuctionItem::getItem).map(Item::getItemId).orElse(null);
        String description = Optional.ofNullable(auctionItem).map(AuctionItem::getItem).map(Item::getDescription).orElse(null);
        Double reservePrice = Optional.ofNullable(auctionItem).map(AuctionItem::getReservePrice).get().doubleValue();
        Double currentBid = Optional.ofNullable(auctionItem).map(AuctionItem::getCurrentBid).get().doubleValue();
        Double maxAutoBidAmount = Optional.ofNullable(auctionItem).map(AuctionItem::getMaxAutoBidAmount).get().doubleValue();
        String bidderName = Optional.ofNullable(auctionItem).map(AuctionItem::getBidderName).orElse(null);

        if(itemId!=null) {
            //Check if item exists
            Item item = appJdbcTemplate.query(sqlQuery.getSQL_SELECT_ITEMS(), new String[]{itemId}, new ItemMapper());

            //Create item if it doesn't exist
            if(item == null){
                log.info("Inserting item ... ");
                appJdbcTemplate.update(sqlQuery.getSQL_INSERT_ITEMS(),itemId,description);
            }

            final Map<String, Object> parameters = new HashMap<String, Object>();
            parameters.put("CURRENT_BID", currentBid);
            parameters.put("RESERVE_PRICE", reservePrice);
            parameters.put("BIDDER_NAME", bidderName);
            parameters.put("MAX_AUTO_BID_AMOUNT", maxAutoBidAmount);
            parameters.put("ITEM_ID", itemId);

            SimpleJdbcInsert simpleJdbcInsert = new SimpleJdbcInsert(appJdbcTemplate);
            simpleJdbcInsert.withTableName("AUCTION_ITEMS").usingGeneratedKeyColumns("AUCTION_ITEM_ID");
            int auctionItemId = (int) simpleJdbcInsert.executeAndReturnKey(parameters);

            auctionItem = new AuctionItem();
            auctionItem.setAuctionItemId(auctionItemId);
        }

        return auctionItem;
    }

    @Override
    public AuctionItem saveBid(AuctionItem auctionItem) {

        //TODO: Refactor to move business logic to service implementation.
        AuctionItem response = new AuctionItem();

        int auctionItemId = Optional.ofNullable(auctionItem).map(AuctionItem::getAuctionItemId).orElse(null);
        Double maxAutoBidAmount = Optional.ofNullable(auctionItem).map(AuctionItem::getMaxAutoBidAmount).orElse(null);
        String bidderName = Optional.ofNullable(auctionItem).map(AuctionItem::getBidderName).orElse(null);
        response.setAuctionItemId(auctionItemId);
        response.setMaxAutoBidAmount(maxAutoBidAmount);

        AuctionItem exitingItem = getAuctionItem(auctionItemId);
        Double reserveAmount = Optional.ofNullable(exitingItem).map(AuctionItem::getReservePrice).get().doubleValue();
        Double currentBidAmountExisting = Optional.ofNullable(exitingItem).map(AuctionItem::getCurrentBid).get().doubleValue();

        if(reserveAmount >= maxAutoBidAmount){
            double currentBid = max(maxAutoBidAmount,currentBidAmountExisting);
            response.setCurrentBid(currentBid);
            response.setReserveMet(false);
        }else{
            response.setReserveMet(true);
            response.setCurrentBid(maxAutoBidAmount);
        }
        appJdbcTemplate.update(sqlQuery.getSQL_INSERT_BIDS(),auctionItemId,bidderName,maxAutoBidAmount);

        //Get the highest bidder.
        List<Bid> bidsByHighestBidder = appJdbcTemplate.query(sqlQuery.getSQL_SELECT_HIGHEST_BIDDER(), new Integer[] {auctionItemId} ,new BidsMapper());

        if(bidsByHighestBidder!=null) {
            bidsByHighestBidder.sort(Comparator.comparing(Bid::getMaxAutoBidAmount).reversed());
        }

        //Set highest bidder to Auction Items
        if(bidsByHighestBidder!=null && bidsByHighestBidder.size()>0){

            //Update Auctions with highest bidder information
            response.setBidderName(bidsByHighestBidder.get(Constants.FIRST_BIDDER_POINTER).getBidderName());
            response.setMaxAutoBidAmount(bidsByHighestBidder.get(Constants.FIRST_BIDDER_POINTER).getMaxAutoBidAmount());

            //Ensure the highest bidder's bid amount is 1 + than the next highest bidder.
            if(bidsByHighestBidder.size()>=2){
                Double bidAmountDiff = (bidsByHighestBidder.get(Constants.FIRST_BIDDER_POINTER).getMaxAutoBidAmount() - bidsByHighestBidder.get(Constants.SECOND_BIDDER_POINTER).getMaxAutoBidAmount());
                if(bidAmountDiff>1){
                    response.setMaxAutoBidAmount(bidsByHighestBidder.get(Constants.FIRST_BIDDER_POINTER).getMaxAutoBidAmount()+1);
                    //Notify Bidder in second place, that they have been outBid.
                    String outBidBidderName = bidsByHighestBidder.get(Constants.SECOND_BIDDER_POINTER).getBidderName();
                    log.info("Bidder has been out bid:" + outBidBidderName);
                }
            }
        }

        appJdbcTemplate.update(sqlQuery.getSQL_UPDATE_AUCTION_ITEM(),response.getBidderName(),response.getMaxAutoBidAmount(),response.getCurrentBid(),response.getAuctionItemId());

        log.info("Bid Added");
        return response;
    }
}

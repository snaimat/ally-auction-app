package com.ally.demo.api.repository;

import com.ally.demo.api.model.AuctionItem;
import com.ally.demo.api.model.Item;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

@Slf4j
@Data
public class AuctionItemMapper implements ResultSetExtractor<AuctionItem> {

    @Override
    public AuctionItem extractData(ResultSet rs) throws SQLException, DataAccessException {
        AuctionItem auctionItem = null;
        if(rs.next()){
            log.info("Mapping AuctionItem Results");
            auctionItem = new AuctionItem();
            auctionItem.setAuctionItemId(rs.getInt("AUCTION_ITEM_ID"));
            auctionItem.setCurrentBid(rs.getDouble("CURRENT_BID"));
            auctionItem.setReservePrice(rs.getDouble("RESERVE_PRICE"));
            auctionItem.setBidderName(rs.getString("BIDDER_NAME"));
            auctionItem.setMaxAutoBidAmount(rs.getDouble("MAX_AUTO_BID_AMOUNT"));
            auctionItem.setItem(new Item(rs.getString("ITEM_ID"),rs.getString("DESCRIPTION")));
        }
        return auctionItem;
    }
}

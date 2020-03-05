package com.ally.demo.api.repository;

import com.ally.demo.api.model.AuctionItem;
import com.ally.demo.api.model.Bid;
import com.ally.demo.api.model.Item;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.ResultSetExtractor;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Data
public class BidsMapper implements ResultSetExtractor<List<Bid>> {

    @Override
    public List<Bid> extractData(ResultSet rs) throws SQLException, DataAccessException {
        List<Bid> bids = new ArrayList<>();
        while(rs.next()){
            log.info("Mapping Bids Results");
            Bid bid = new Bid();
            bid.setAuctionItemId(rs.getInt("AUCTION_ITEM_ID"));
            bid.setBidderName(rs.getString("BIDDER_NAME"));
            bid.setMaxAutoBidAmount(rs.getDouble("MAX_AUTO_BID_AMOUNT"));
            bids.add(bid);
            log.info("Bids Mapping completed.");
        }
        return bids;
    }
}

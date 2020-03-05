package com.ally.demo.api.repository;

import com.ally.demo.api.model.AuctionItem;
import com.ally.demo.api.model.Item;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.ResultSetExtractor;

import java.sql.ResultSet;
import java.sql.SQLException;

@Slf4j
@Data
public class ItemMapper implements ResultSetExtractor<Item> {

    @Override
    public Item extractData(ResultSet rs) throws SQLException, DataAccessException {
        Item item = null;
        if(rs.next()){
            log.info("Mapping Item Results");
            item = new Item();
            item.setItemId(rs.getString("ID"));
            item.setDescription(rs.getString("DESCRIPTION"));
        }
        return item;
    }
}

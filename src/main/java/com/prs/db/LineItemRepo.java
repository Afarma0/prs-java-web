package com.prs.db;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.prs.model.LineItem;

public interface LineItemRepo extends JpaRepository<LineItem, Integer> {

    // ✅ Custom query that eagerly loads the Product with each LineItem
    @Query("SELECT li FROM LineItem li JOIN FETCH li.product WHERE li.request.id = :reqId")
    List<LineItem> findByRequestId(@Param("reqId") int reqId);
}

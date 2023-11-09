package com.prodemy.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.prodemy.entity.HistoryPemesanan;

@Repository
public interface HistoryPemesananRepository extends JpaRepository<HistoryPemesanan, Long> {
    @Query(value = "SELECT * FROM history WHERE user_id = :user_id", nativeQuery = true)
    public List<HistoryPemesanan> getHistoryByUserId(@Param("user_id") long id);
}

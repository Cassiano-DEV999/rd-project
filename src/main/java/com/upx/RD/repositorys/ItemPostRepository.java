package com.upx.RD.repositorys;

import com.upx.RD.model.ItemPost;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ItemPostRepository extends JpaRepository<ItemPost, Long> {
}
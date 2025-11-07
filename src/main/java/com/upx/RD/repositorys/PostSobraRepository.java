package com.upx.RD.repositorys;

import com.upx.RD.model.PostSobra;
import com.upx.RD.model.StatusPost;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PostSobraRepository extends JpaRepository<PostSobra, Long> {

    @Query("SELECT p FROM PostSobra p " +
            "JOIN FETCH p.obraOrigem o " +
            "JOIN FETCH o.materiais m " +
            "WHERE p.status = :status")
    List<PostSobra> findByStatusWithDetails(@Param("status") StatusPost status);

}
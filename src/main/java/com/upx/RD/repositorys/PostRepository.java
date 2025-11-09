package com.upx.RD.repositorys;

import com.upx.RD.model.Post;
import com.upx.RD.model.StatusPost;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PostRepository extends JpaRepository<Post, Long> {

    @Query("SELECT p FROM Post p LEFT JOIN FETCH p.itens WHERE p.id = :id")
    Optional<Post> findByIdWithItens(@Param("id") Long id);

    @Query("SELECT p FROM Post p " +
            "JOIN FETCH p.proprietario " +
            "LEFT JOIN FETCH p.itens " +
            "WHERE p.status = :status " +
            "ORDER BY p.dataCriacao DESC")
    List<Post> findAllWithDetailsByStatus(@Param("status") StatusPost status);
}
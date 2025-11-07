package com.upx.RD.repositorys;

import com.upx.RD.model.Obra;
import com.upx.RD.model.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ObraRepository extends JpaRepository<Obra, Long> {

    List<Obra> findByProprietario(Usuario proprietario);

    List<Obra> findByProprietarioId(Long usuarioId);

}
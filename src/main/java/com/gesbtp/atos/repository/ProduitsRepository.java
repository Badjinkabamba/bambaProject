package com.gesbtp.atos.repository;

import com.gesbtp.atos.domain.Produits;
import org.springframework.stereotype.Repository;

import org.springframework.data.jpa.repository.*;
import java.util.List;

/**
 * Spring Data JPA repository for the Produits entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ProduitsRepository extends JpaRepository<Produits, Long> {

    @Query("select produits from Produits produits where produits.user.login = ?#{principal.username}")
    List<Produits> findByUserIsCurrentUser();

}

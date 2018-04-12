package com.gesbtp.atos.repository.search;

import com.gesbtp.atos.domain.Produits;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the Produits entity.
 */
public interface ProduitsSearchRepository extends ElasticsearchRepository<Produits, Long> {
}

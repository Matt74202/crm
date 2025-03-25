package site.easy.to.build.crm.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import site.easy.to.build.crm.entity.Rate;


@Repository
public interface RateRepository extends JpaRepository<Rate, Integer> {
    public Rate findByRateId(int rateId);
    Rate findTopByOrderByInsertedAtDesc(); //dernier insere 

}

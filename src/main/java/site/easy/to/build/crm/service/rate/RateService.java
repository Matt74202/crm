package site.easy.to.build.crm.service.rate;

import java.math.BigDecimal;
import java.util.List;

import site.easy.to.build.crm.entity.Rate;


public interface RateService {

    public Rate save(BigDecimal rateValue);

    public List<Rate> findAll();

    public Rate getLast();
    
}

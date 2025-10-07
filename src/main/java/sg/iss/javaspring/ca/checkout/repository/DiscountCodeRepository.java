package sg.iss.javaspring.ca.checkout.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import sg.iss.javaspring.ca.checkout.model.DiscountCode;

public interface DiscountCodeRepository extends JpaRepository<DiscountCode, Integer> {
    @Query("SELECT d FROM DiscountCode d WHERE d.code=:code")
    public DiscountCode findDiscountCodeByCode(@Param("code") String code);

}

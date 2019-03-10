package ee.taltech.debty.entity;

import lombok.Data;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Currency;
import java.util.List;

@Data
@Entity
public class Bill {
    @Id
    @GeneratedValue
    private Long id;
    private String title;
    private String description;
    @ManyToOne
    private Person creator;
    private LocalDateTime createdAt;
    private LocalDateTime modifiedAt;
    private BigDecimal sum;
    private Currency currency;
    @ManyToOne
    private Person buyer;
    @ManyToMany
    private List<Person> people;
    @ManyToMany(cascade = CascadeType.ALL)
    private List<BillPayment> billPayments;
}

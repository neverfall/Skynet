package com.pernix_central.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Objects;

/**
 * A CustomerSatisfactionNPS.
 */
@Entity
@Table(name = "customer_satisfaction_nps")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class CustomerSatisfactionNPS implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "value")
    private Integer value;

    @Column(name = "compliance")
    private Integer compliance;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getValue() {
        return value;
    }

    public void setValue(Integer value) {
        this.value = value;
    }

    public Integer getCompliance() {
        return compliance;
    }

    public void setCompliance(Integer compliance) {
        this.compliance = compliance;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        CustomerSatisfactionNPS customerSatisfactionNPS = (CustomerSatisfactionNPS) o;
        if(customerSatisfactionNPS.id == null || id == null) {
            return false;
        }
        return Objects.equals(id, customerSatisfactionNPS.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "CustomerSatisfactionNPS{" +
            "id=" + id +
            ", value='" + value + "'" +
            ", compliance='" + compliance + "'" +
            '}';
    }
}

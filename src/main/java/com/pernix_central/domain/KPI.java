package com.pernix_central.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Objects;

/**
 * A KPI.
 */
@Entity
@Table(name = "kpi")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class KPI implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @ManyToOne
    private Collaborator collaborator;

    @OneToOne
    @JoinColumn(unique = true)
    private Duration duration;

    @OneToOne
    @JoinColumn(unique = true)
    private Productivity productivity;

    @OneToOne
    @JoinColumn(unique = true)
    private FollowUp followUp;

    @OneToOne
    @JoinColumn(unique = true)
    private CustomerSatisfactionNPS customerSatisfactionNPS;

    @OneToOne
    @JoinColumn(unique = true)
    private CustomerSatisfactionGrade customerSatisfactionGrade;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Collaborator getCollaborator() {
        return collaborator;
    }

    public void setCollaborator(Collaborator collaborator) {
        this.collaborator = collaborator;
    }

    public Duration getDuration() {
        return duration;
    }

    public void setDuration(Duration duration) {
        this.duration = duration;
    }

    public Productivity getProductivity() {
        return productivity;
    }

    public void setProductivity(Productivity productivity) {
        this.productivity = productivity;
    }

    public FollowUp getFollowUp() {
        return followUp;
    }

    public void setFollowUp(FollowUp followUp) {
        this.followUp = followUp;
    }

    public CustomerSatisfactionNPS getCustomerSatisfactionNPS() {
        return customerSatisfactionNPS;
    }

    public void setCustomerSatisfactionNPS(CustomerSatisfactionNPS customerSatisfactionNPS) {
        this.customerSatisfactionNPS = customerSatisfactionNPS;
    }

    public CustomerSatisfactionGrade getCustomerSatisfactionGrade() {
        return customerSatisfactionGrade;
    }

    public void setCustomerSatisfactionGrade(CustomerSatisfactionGrade customerSatisfactionGrade) {
        this.customerSatisfactionGrade = customerSatisfactionGrade;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        KPI kPI = (KPI) o;
        if(kPI.id == null || id == null) {
            return false;
        }
        return Objects.equals(id, kPI.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "KPI{" +
            "id=" + id +
            '}';
    }
}

package com.pernix_central.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;

/**
 * A Gamification.
 */
@Entity
@Table(name = "gamification")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class Gamification implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "yearly_score")
    private Integer yearlyScore;

    @Column(name = "period_score")
    private Integer periodScore;

    @OneToMany(mappedBy = "gamification")
    @JsonIgnore
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<Category> categories = new HashSet<>();

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getYearlyScore() {
        return yearlyScore;
    }

    public void setYearlyScore(Integer yearlyScore) {
        this.yearlyScore = yearlyScore;
    }

    public Integer getPeriodScore() {
        return periodScore;
    }

    public void setPeriodScore(Integer periodScore) {
        this.periodScore = periodScore;
    }

    public Set<Category> getCategories() {
        return categories;
    }

    public void setCategories(Set<Category> categories) {
        this.categories = categories;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Gamification gamification = (Gamification) o;
        if(gamification.id == null || id == null) {
            return false;
        }
        return Objects.equals(id, gamification.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "Gamification{" +
            "id=" + id +
            ", yearlyScore='" + yearlyScore + "'" +
            ", periodScore='" + periodScore + "'" +
            '}';
    }
}

package com.pernix_central.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;

/**
 * A Collaborator.
 */
@Entity
@Table(name = "collaborator")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class Collaborator implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @NotNull
    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "last_name")
    private String lastName;

    @NotNull
    @Column(name = "email", nullable = false)
    private String email;

    @NotNull
    @Column(name = "apprentice", nullable = false)
    private Boolean apprentice;

    @OneToMany(mappedBy = "collaborator")
    @JsonIgnore
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<KPI> kpis = new HashSet<>();

    @OneToOne
    @JoinColumn(unique = true)
    private Gamification gamification;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Boolean isApprentice() {
        return apprentice;
    }

    public void setApprentice(Boolean apprentice) {
        this.apprentice = apprentice;
    }

    public Set<KPI> getKpis() {
        return kpis;
    }

    public void setKpis(Set<KPI> kPIS) {
        this.kpis = kPIS;
    }

    public Gamification getGamification() {
        return gamification;
    }

    public void setGamification(Gamification gamification) {
        this.gamification = gamification;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Collaborator collaborator = (Collaborator) o;
        if(collaborator.id == null || id == null) {
            return false;
        }
        return Objects.equals(id, collaborator.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "Collaborator{" +
            "id=" + id +
            ", name='" + name + "'" +
            ", lastName='" + lastName + "'" +
            ", email='" + email + "'" +
            ", apprentice='" + apprentice + "'" +
            '}';
    }
}

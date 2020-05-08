package com.mps.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.*;

import java.io.Serializable;
import java.util.Objects;
import java.time.LocalDate;

import com.mps.domain.enumeration.WaiverType;

import com.mps.domain.enumeration.ActiveStatus;

/**
 * A Waiver.
 */
@Entity
@Table(name = "waiver")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class Waiver implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "waiver_type", nullable = false)
    private WaiverType waiverType;

    @NotNull
    @Column(name = "entity_name", nullable = false)
    private String entityName;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "active_status", nullable = false)
    private ActiveStatus activeStatus;

    @Column(name = "date_created")
    private LocalDate dateCreated;

    @Column(name = "date_modified")
    private LocalDate dateModified;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public WaiverType getWaiverType() {
        return waiverType;
    }

    public Waiver waiverType(WaiverType waiverType) {
        this.waiverType = waiverType;
        return this;
    }

    public void setWaiverType(WaiverType waiverType) {
        this.waiverType = waiverType;
    }

    public String getEntityName() {
        return entityName;
    }

    public Waiver entityName(String entityName) {
        this.entityName = entityName;
        return this;
    }

    public void setEntityName(String entityName) {
        this.entityName = entityName;
    }

    public ActiveStatus getActiveStatus() {
        return activeStatus;
    }

    public Waiver activeStatus(ActiveStatus activeStatus) {
        this.activeStatus = activeStatus;
        return this;
    }

    public void setActiveStatus(ActiveStatus activeStatus) {
        this.activeStatus = activeStatus;
    }

    public LocalDate getDateCreated() {
        return dateCreated;
    }

    public Waiver dateCreated(LocalDate dateCreated) {
        this.dateCreated = dateCreated;
        return this;
    }

    public void setDateCreated(LocalDate dateCreated) {
        this.dateCreated = dateCreated;
    }

    public LocalDate getDateModified() {
        return dateModified;
    }

    public Waiver dateModified(LocalDate dateModified) {
        this.dateModified = dateModified;
        return this;
    }

    public void setDateModified(LocalDate dateModified) {
        this.dateModified = dateModified;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Waiver)) {
            return false;
        }
        return id != null && id.equals(((Waiver) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    @Override
    public String toString() {
        return "Waiver{" +
            "id=" + getId() +
            ", waiverType='" + getWaiverType() + "'" +
            ", entityName='" + getEntityName() + "'" +
            ", activeStatus='" + getActiveStatus() + "'" +
            ", dateCreated='" + getDateCreated() + "'" +
            ", dateModified='" + getDateModified() + "'" +
            "}";
    }
}

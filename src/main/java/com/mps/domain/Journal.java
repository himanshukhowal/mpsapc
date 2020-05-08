package com.mps.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.*;

import java.io.Serializable;
import java.util.Objects;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

import com.mps.domain.enumeration.ActiveStatus;

/**
 * A Journal.
 */
@Entity
@Table(name = "journal")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class Journal implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Column(name = "journal_acronym", nullable = false)
    private String journalAcronym;

    @NotNull
    @Column(name = "journal_title", nullable = false)
    private String journalTitle;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "active_status", nullable = false)
    private ActiveStatus activeStatus;

    @Column(name = "date_created")
    private LocalDate dateCreated;

    @Column(name = "date_modified")
    private LocalDate dateModified;

    @OneToMany(mappedBy = "manuscriptJournalAcronym")
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<Manuscript> journalAcronyms = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getJournalAcronym() {
        return journalAcronym;
    }

    public Journal journalAcronym(String journalAcronym) {
        this.journalAcronym = journalAcronym;
        return this;
    }

    public void setJournalAcronym(String journalAcronym) {
        this.journalAcronym = journalAcronym;
    }

    public String getJournalTitle() {
        return journalTitle;
    }

    public Journal journalTitle(String journalTitle) {
        this.journalTitle = journalTitle;
        return this;
    }

    public void setJournalTitle(String journalTitle) {
        this.journalTitle = journalTitle;
    }

    public ActiveStatus getActiveStatus() {
        return activeStatus;
    }

    public Journal activeStatus(ActiveStatus activeStatus) {
        this.activeStatus = activeStatus;
        return this;
    }

    public void setActiveStatus(ActiveStatus activeStatus) {
        this.activeStatus = activeStatus;
    }

    public LocalDate getDateCreated() {
        return dateCreated;
    }

    public Journal dateCreated(LocalDate dateCreated) {
        this.dateCreated = dateCreated;
        return this;
    }

    public void setDateCreated(LocalDate dateCreated) {
        this.dateCreated = dateCreated;
    }

    public LocalDate getDateModified() {
        return dateModified;
    }

    public Journal dateModified(LocalDate dateModified) {
        this.dateModified = dateModified;
        return this;
    }

    public void setDateModified(LocalDate dateModified) {
        this.dateModified = dateModified;
    }

    public Set<Manuscript> getJournalAcronyms() {
        return journalAcronyms;
    }

    public Journal journalAcronyms(Set<Manuscript> manuscripts) {
        this.journalAcronyms = manuscripts;
        return this;
    }

    public Journal addJournalAcronym(Manuscript manuscript) {
        this.journalAcronyms.add(manuscript);
        manuscript.setManuscriptJournalAcronym(this);
        return this;
    }

    public Journal removeJournalAcronym(Manuscript manuscript) {
        this.journalAcronyms.remove(manuscript);
        manuscript.setManuscriptJournalAcronym(null);
        return this;
    }

    public void setJournalAcronyms(Set<Manuscript> manuscripts) {
        this.journalAcronyms = manuscripts;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Journal)) {
            return false;
        }
        return id != null && id.equals(((Journal) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    @Override
    public String toString() {
        return "Journal{" +
            "id=" + getId() +
            ", journalAcronym='" + getJournalAcronym() + "'" +
            ", journalTitle='" + getJournalTitle() + "'" +
            ", activeStatus='" + getActiveStatus() + "'" +
            ", dateCreated='" + getDateCreated() + "'" +
            ", dateModified='" + getDateModified() + "'" +
            "}";
    }
}

package com.mps.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.*;

import java.io.Serializable;
import java.util.Objects;
import java.time.LocalDate;

import com.mps.domain.enumeration.APCStatus;

/**
 * A Manuscript.
 */
@Entity
@Table(name = "manuscript")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class Manuscript implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Column(name = "manuscript_id", nullable = false)
    private String manuscriptId;

    @NotNull
    @Column(name = "manuscript_title", nullable = false)
    private String manuscriptTitle;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "apc_status", nullable = false)
    private APCStatus apcStatus;

    @Column(name = "date_created")
    private LocalDate dateCreated;

    @Column(name = "date_modified")
    private LocalDate dateModified;

    @OneToOne(optional = false)
    @NotNull
    @JoinColumn(unique = true)
    private Payment linkedPayment;

    @ManyToOne(optional = false)
    @NotNull
    @JsonIgnoreProperties("journalAcronyms")
    private Journal manuscriptJournalAcronym;

    @ManyToOne(optional = false)
    @NotNull
    @JsonIgnoreProperties("firstNames")
    private Author manuscriptAuthorName;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getManuscriptId() {
        return manuscriptId;
    }

    public Manuscript manuscriptId(String manuscriptId) {
        this.manuscriptId = manuscriptId;
        return this;
    }

    public void setManuscriptId(String manuscriptId) {
        this.manuscriptId = manuscriptId;
    }

    public String getManuscriptTitle() {
        return manuscriptTitle;
    }

    public Manuscript manuscriptTitle(String manuscriptTitle) {
        this.manuscriptTitle = manuscriptTitle;
        return this;
    }

    public void setManuscriptTitle(String manuscriptTitle) {
        this.manuscriptTitle = manuscriptTitle;
    }

    public APCStatus getApcStatus() {
        return apcStatus;
    }

    public Manuscript apcStatus(APCStatus apcStatus) {
        this.apcStatus = apcStatus;
        return this;
    }

    public void setApcStatus(APCStatus apcStatus) {
        this.apcStatus = apcStatus;
    }

    public LocalDate getDateCreated() {
        return dateCreated;
    }

    public Manuscript dateCreated(LocalDate dateCreated) {
        this.dateCreated = dateCreated;
        return this;
    }

    public void setDateCreated(LocalDate dateCreated) {
        this.dateCreated = dateCreated;
    }

    public LocalDate getDateModified() {
        return dateModified;
    }

    public Manuscript dateModified(LocalDate dateModified) {
        this.dateModified = dateModified;
        return this;
    }

    public void setDateModified(LocalDate dateModified) {
        this.dateModified = dateModified;
    }

    public Payment getLinkedPayment() {
        return linkedPayment;
    }

    public Manuscript linkedPayment(Payment payment) {
        this.linkedPayment = payment;
        return this;
    }

    public void setLinkedPayment(Payment payment) {
        this.linkedPayment = payment;
    }

    public Journal getManuscriptJournalAcronym() {
        return manuscriptJournalAcronym;
    }

    public Manuscript manuscriptJournalAcronym(Journal journal) {
        this.manuscriptJournalAcronym = journal;
        return this;
    }

    public void setManuscriptJournalAcronym(Journal journal) {
        this.manuscriptJournalAcronym = journal;
    }

    public Author getManuscriptAuthorName() {
        return manuscriptAuthorName;
    }

    public Manuscript manuscriptAuthorName(Author author) {
        this.manuscriptAuthorName = author;
        return this;
    }

    public void setManuscriptAuthorName(Author author) {
        this.manuscriptAuthorName = author;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Manuscript)) {
            return false;
        }
        return id != null && id.equals(((Manuscript) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    @Override
    public String toString() {
        return "Manuscript{" +
            "id=" + getId() +
            ", manuscriptId='" + getManuscriptId() + "'" +
            ", manuscriptTitle='" + getManuscriptTitle() + "'" +
            ", apcStatus='" + getApcStatus() + "'" +
            ", dateCreated='" + getDateCreated() + "'" +
            ", dateModified='" + getDateModified() + "'" +
            "}";
    }
}

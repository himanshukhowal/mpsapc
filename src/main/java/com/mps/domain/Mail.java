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

import com.mps.domain.enumeration.ActiveStatus;

/**
 * A Mail.
 */
@Entity
@Table(name = "mail")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class Mail implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Column(name = "mail_configuration_name", nullable = false)
    private String mailConfigurationName;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "stage_name", nullable = false)
    private APCStatus stageName;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "active_status", nullable = false)
    private ActiveStatus activeStatus;

    @Column(name = "date_created")
    private LocalDate dateCreated;

    @Column(name = "date_modified")
    private LocalDate dateModified;

    @ManyToOne(optional = false)
    @NotNull
    @JsonIgnoreProperties("mailingConfigurations")
    private Journal associatedJournal;

    @ManyToOne(optional = false)
    @NotNull
    @JsonIgnoreProperties("templateNames")
    private MailTemplates associatedMailTemplate;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getMailConfigurationName() {
        return mailConfigurationName;
    }

    public Mail mailConfigurationName(String mailConfigurationName) {
        this.mailConfigurationName = mailConfigurationName;
        return this;
    }

    public void setMailConfigurationName(String mailConfigurationName) {
        this.mailConfigurationName = mailConfigurationName;
    }

    public APCStatus getStageName() {
        return stageName;
    }

    public Mail stageName(APCStatus stageName) {
        this.stageName = stageName;
        return this;
    }

    public void setStageName(APCStatus stageName) {
        this.stageName = stageName;
    }

    public ActiveStatus getActiveStatus() {
        return activeStatus;
    }

    public Mail activeStatus(ActiveStatus activeStatus) {
        this.activeStatus = activeStatus;
        return this;
    }

    public void setActiveStatus(ActiveStatus activeStatus) {
        this.activeStatus = activeStatus;
    }

    public LocalDate getDateCreated() {
        return dateCreated;
    }

    public Mail dateCreated(LocalDate dateCreated) {
        this.dateCreated = dateCreated;
        return this;
    }

    public void setDateCreated(LocalDate dateCreated) {
        this.dateCreated = dateCreated;
    }

    public LocalDate getDateModified() {
        return dateModified;
    }

    public Mail dateModified(LocalDate dateModified) {
        this.dateModified = dateModified;
        return this;
    }

    public void setDateModified(LocalDate dateModified) {
        this.dateModified = dateModified;
    }

    public Journal getAssociatedJournal() {
        return associatedJournal;
    }

    public Mail associatedJournal(Journal journal) {
        this.associatedJournal = journal;
        return this;
    }

    public void setAssociatedJournal(Journal journal) {
        this.associatedJournal = journal;
    }

    public MailTemplates getAssociatedMailTemplate() {
        return associatedMailTemplate;
    }

    public Mail associatedMailTemplate(MailTemplates mailTemplates) {
        this.associatedMailTemplate = mailTemplates;
        return this;
    }

    public void setAssociatedMailTemplate(MailTemplates mailTemplates) {
        this.associatedMailTemplate = mailTemplates;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Mail)) {
            return false;
        }
        return id != null && id.equals(((Mail) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    @Override
    public String toString() {
        return "Mail{" +
            "id=" + getId() +
            ", mailConfigurationName='" + getMailConfigurationName() + "'" +
            ", stageName='" + getStageName() + "'" +
            ", activeStatus='" + getActiveStatus() + "'" +
            ", dateCreated='" + getDateCreated() + "'" +
            ", dateModified='" + getDateModified() + "'" +
            "}";
    }
}

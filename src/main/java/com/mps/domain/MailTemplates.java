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

/**
 * A MailTemplates.
 */
@Entity
@Table(name = "mail_templates")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class MailTemplates implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Column(name = "template_name", nullable = false)
    private String templateName;

    @Column(name = "mail_cc")
    private String mailCC;

    @Column(name = "mail_bcc")
    private String mailBCC;

    @Column(name = "mail_subject")
    private String mailSubject;

    @Column(name = "mail_body")
    private String mailBody;

    @Column(name = "date_created")
    private LocalDate dateCreated;

    @Column(name = "date_modified")
    private LocalDate dateModified;

    @OneToMany(mappedBy = "associatedMailTemplate")
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<Mail> templateNames = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTemplateName() {
        return templateName;
    }

    public MailTemplates templateName(String templateName) {
        this.templateName = templateName;
        return this;
    }

    public void setTemplateName(String templateName) {
        this.templateName = templateName;
    }

    public String getMailCC() {
        return mailCC;
    }

    public MailTemplates mailCC(String mailCC) {
        this.mailCC = mailCC;
        return this;
    }

    public void setMailCC(String mailCC) {
        this.mailCC = mailCC;
    }

    public String getMailBCC() {
        return mailBCC;
    }

    public MailTemplates mailBCC(String mailBCC) {
        this.mailBCC = mailBCC;
        return this;
    }

    public void setMailBCC(String mailBCC) {
        this.mailBCC = mailBCC;
    }

    public String getMailSubject() {
        return mailSubject;
    }

    public MailTemplates mailSubject(String mailSubject) {
        this.mailSubject = mailSubject;
        return this;
    }

    public void setMailSubject(String mailSubject) {
        this.mailSubject = mailSubject;
    }

    public String getMailBody() {
        return mailBody;
    }

    public MailTemplates mailBody(String mailBody) {
        this.mailBody = mailBody;
        return this;
    }

    public void setMailBody(String mailBody) {
        this.mailBody = mailBody;
    }

    public LocalDate getDateCreated() {
        return dateCreated;
    }

    public MailTemplates dateCreated(LocalDate dateCreated) {
        this.dateCreated = dateCreated;
        return this;
    }

    public void setDateCreated(LocalDate dateCreated) {
        this.dateCreated = dateCreated;
    }

    public LocalDate getDateModified() {
        return dateModified;
    }

    public MailTemplates dateModified(LocalDate dateModified) {
        this.dateModified = dateModified;
        return this;
    }

    public void setDateModified(LocalDate dateModified) {
        this.dateModified = dateModified;
    }

    public Set<Mail> getTemplateNames() {
        return templateNames;
    }

    public MailTemplates templateNames(Set<Mail> mail) {
        this.templateNames = mail;
        return this;
    }

    public MailTemplates addTemplateName(Mail mail) {
        this.templateNames.add(mail);
        mail.setAssociatedMailTemplate(this);
        return this;
    }

    public MailTemplates removeTemplateName(Mail mail) {
        this.templateNames.remove(mail);
        mail.setAssociatedMailTemplate(null);
        return this;
    }

    public void setTemplateNames(Set<Mail> mail) {
        this.templateNames = mail;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof MailTemplates)) {
            return false;
        }
        return id != null && id.equals(((MailTemplates) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    @Override
    public String toString() {
        return "MailTemplates{" +
            "id=" + getId() +
            ", templateName='" + getTemplateName() + "'" +
            ", mailCC='" + getMailCC() + "'" +
            ", mailBCC='" + getMailBCC() + "'" +
            ", mailSubject='" + getMailSubject() + "'" +
            ", mailBody='" + getMailBody() + "'" +
            ", dateCreated='" + getDateCreated() + "'" +
            ", dateModified='" + getDateModified() + "'" +
            "}";
    }
}

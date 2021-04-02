package com.jan.learning.domain;

import java.io.Serializable;
import java.time.LocalDate;
import javax.persistence.*;
import javax.validation.constraints.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A Gratitude.
 */
@Entity
@Table(name = "gratitude")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Gratitude implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Column(name = "created_date", nullable = false)
    private LocalDate createdDate;

    @Column(name = "loved")
    private Boolean loved;

    @Column(name = "achieved")
    private Boolean achieved;

    @Lob
    @Column(name = "grateful_note", nullable = false)
    private String gratefulNote;

    // jhipster-needle-entity-add-field - JHipster will add fields here
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Gratitude id(Long id) {
        this.id = id;
        return this;
    }

    public LocalDate getCreatedDate() {
        return this.createdDate;
    }

    public Gratitude createdDate(LocalDate createdDate) {
        this.createdDate = createdDate;
        return this;
    }

    public void setCreatedDate(LocalDate createdDate) {
        this.createdDate = createdDate;
    }

    public Boolean getLoved() {
        return this.loved;
    }

    public Gratitude loved(Boolean loved) {
        this.loved = loved;
        return this;
    }

    public void setLoved(Boolean loved) {
        this.loved = loved;
    }

    public Boolean getAchieved() {
        return this.achieved;
    }

    public Gratitude achieved(Boolean achieved) {
        this.achieved = achieved;
        return this;
    }

    public void setAchieved(Boolean achieved) {
        this.achieved = achieved;
    }

    public String getGratefulNote() {
        return this.gratefulNote;
    }

    public Gratitude gratefulNote(String gratefulNote) {
        this.gratefulNote = gratefulNote;
        return this;
    }

    public void setGratefulNote(String gratefulNote) {
        this.gratefulNote = gratefulNote;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Gratitude)) {
            return false;
        }
        return id != null && id.equals(((Gratitude) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Gratitude{" +
            "id=" + getId() +
            ", createdDate='" + getCreatedDate() + "'" +
            ", loved='" + getLoved() + "'" +
            ", achieved='" + getAchieved() + "'" +
            ", gratefulNote='" + getGratefulNote() + "'" +
            "}";
    }
}

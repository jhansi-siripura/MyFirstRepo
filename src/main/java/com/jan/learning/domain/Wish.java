package com.jan.learning.domain;

import com.jan.learning.domain.enumeration.WishCategory;
import java.io.Serializable;
import java.time.LocalDate;
import javax.persistence.*;
import javax.validation.constraints.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A Wish.
 */
@Entity
@Table(name = "wish")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Wish implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Column(name = "my_wish_note", nullable = false)
    private String myWishNote;

    @Column(name = "start_date")
    private LocalDate startDate;

    @Column(name = "fulfilled")
    private Boolean fulfilled;

    @Column(name = "fulfilled_date")
    private LocalDate fulfilledDate;

    @Column(name = "duration")
    private Integer duration;

    @Enumerated(EnumType.STRING)
    @Column(name = "category")
    private WishCategory category;

    // jhipster-needle-entity-add-field - JHipster will add fields here
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Wish id(Long id) {
        this.id = id;
        return this;
    }

    public String getMyWishNote() {
        return this.myWishNote;
    }

    public Wish myWishNote(String myWishNote) {
        this.myWishNote = myWishNote;
        return this;
    }

    public void setMyWishNote(String myWishNote) {
        this.myWishNote = myWishNote;
    }

    public LocalDate getStartDate() {
        return this.startDate;
    }

    public Wish startDate(LocalDate startDate) {
        this.startDate = startDate;
        return this;
    }

    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

    public Boolean getFulfilled() {
        return this.fulfilled;
    }

    public Wish fulfilled(Boolean fulfilled) {
        this.fulfilled = fulfilled;
        return this;
    }

    public void setFulfilled(Boolean fulfilled) {
        this.fulfilled = fulfilled;
    }

    public LocalDate getFulfilledDate() {
        return this.fulfilledDate;
    }

    public Wish fulfilledDate(LocalDate fulfilledDate) {
        this.fulfilledDate = fulfilledDate;
        return this;
    }

    public void setFulfilledDate(LocalDate fulfilledDate) {
        this.fulfilledDate = fulfilledDate;
    }

    public Integer getDuration() {
        return this.duration;
    }

    public Wish duration(Integer duration) {
        this.duration = duration;
        return this;
    }

    public void setDuration(Integer duration) {
        this.duration = duration;
    }

    public WishCategory getCategory() {
        return this.category;
    }

    public Wish category(WishCategory category) {
        this.category = category;
        return this;
    }

    public void setCategory(WishCategory category) {
        this.category = category;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Wish)) {
            return false;
        }
        return id != null && id.equals(((Wish) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Wish{" +
            "id=" + getId() +
            ", myWishNote='" + getMyWishNote() + "'" +
            ", startDate='" + getStartDate() + "'" +
            ", fulfilled='" + getFulfilled() + "'" +
            ", fulfilledDate='" + getFulfilledDate() + "'" +
            ", duration=" + getDuration() +
            ", category='" + getCategory() + "'" +
            "}";
    }
}

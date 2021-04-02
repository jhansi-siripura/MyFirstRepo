package com.jan.learning.domain;

import java.io.Serializable;
import java.time.Instant;
import javax.persistence.*;
import javax.validation.constraints.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A TradeWish.
 */
@Entity
@Table(name = "trade_wish")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class TradeWish implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Column(name = "twish", nullable = false)
    private Integer twish;

    @Column(name = "picked")
    private Boolean picked;

    @NotNull
    @Column(name = "picked_date", nullable = false)
    private Instant pickedDate;

    // jhipster-needle-entity-add-field - JHipster will add fields here
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public TradeWish id(Long id) {
        this.id = id;
        return this;
    }

    public Integer getTwish() {
        return this.twish;
    }

    public TradeWish twish(Integer twish) {
        this.twish = twish;
        return this;
    }

    public void setTwish(Integer twish) {
        this.twish = twish;
    }

    public Boolean getPicked() {
        return this.picked;
    }

    public TradeWish picked(Boolean picked) {
        this.picked = picked;
        return this;
    }

    public void setPicked(Boolean picked) {
        this.picked = picked;
    }

    public Instant getPickedDate() {
        return this.pickedDate;
    }

    public TradeWish pickedDate(Instant pickedDate) {
        this.pickedDate = pickedDate;
        return this;
    }

    public void setPickedDate(Instant pickedDate) {
        this.pickedDate = pickedDate;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof TradeWish)) {
            return false;
        }
        return id != null && id.equals(((TradeWish) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "TradeWish{" +
            "id=" + getId() +
            ", twish=" + getTwish() +
            ", picked='" + getPicked() + "'" +
            ", pickedDate='" + getPickedDate() + "'" +
            "}";
    }
}

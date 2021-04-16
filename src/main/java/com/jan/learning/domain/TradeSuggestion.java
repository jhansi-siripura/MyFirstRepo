package com.jan.learning.domain;

import com.jan.learning.domain.enumeration.TradeAction;
import com.jan.learning.domain.enumeration.TradeResult;
import com.jan.learning.domain.enumeration.TradeStatus;
import com.jan.learning.domain.enumeration.TradeType;
import java.io.Serializable;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZonedDateTime;
import javax.persistence.*;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A TradeSuggestion.
 */
@Entity
@Table(name = "trade_suggestion")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class TradeSuggestion implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(name = "action")
    private TradeAction action;

    @Column(name = "trade_in_price")
    private Integer tradeInPrice;

    @Column(name = "min_trade_out_price")
    private Integer minTradeOutPrice;

    @Column(name = "min_profit_points")
    private Integer minProfitPoints;

    @Column(name = "better_tradeout_price")
    private Integer betterTradeoutPrice;

    @Column(name = "better_trade_out_profit_points")
    private Integer betterTradeOutProfitPoints;

    @Column(name = "actual_tradeout_price")
    private Integer actualTradeoutPrice;

    @Column(name = "actual_profit_points")
    private Integer actualProfitPoints;

    @Column(name = "sl_points")
    private Integer slPoints;

    @Enumerated(EnumType.STRING)
    @Column(name = "trade_status")
    private TradeStatus tradeStatus;

    @Enumerated(EnumType.STRING)
    @Column(name = "trade_results")
    private TradeResult tradeResults;

    @Column(name = "trade_in_time")
    private ZonedDateTime tradeInTime;

    @Column(name = "trade_out_time")
    private ZonedDateTime tradeOutTime;

    @Column(name = "trade_duration")
    private Integer tradeDuration;

    @Column(name = "trade_date")
    private LocalDate tradeDate;

    @Column(name = "trade_suggestion_time")
    private Instant tradeSuggestionTime;

    @Enumerated(EnumType.STRING)
    @Column(name = "trade_type")
    private TradeType tradeType;

    @Column(name = "actual_pl")
    private Integer actualPL;

    @Column(name = "sl_price")
    private Integer slPrice;

    @NotNull
    @Min(value = 1000)
    @Max(value = 9999)
    @Column(name = "current_market_price", nullable = false)
    private Integer currentMarketPrice;

    // jhipster-needle-entity-add-field - JHipster will add fields here
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public TradeSuggestion id(Long id) {
        this.id = id;
        return this;
    }

    public TradeAction getAction() {
        return this.action;
    }

    public TradeSuggestion action(TradeAction action) {
        this.action = action;
        return this;
    }

    public void setAction(TradeAction action) {
        this.action = action;
    }

    public Integer getTradeInPrice() {
        return this.tradeInPrice;
    }

    public TradeSuggestion tradeInPrice(Integer tradeInPrice) {
        this.tradeInPrice = tradeInPrice;
        return this;
    }

    public void setTradeInPrice(Integer tradeInPrice) {
        this.tradeInPrice = tradeInPrice;
    }

    public Integer getMinTradeOutPrice() {
        return this.minTradeOutPrice;
    }

    public TradeSuggestion minTradeOutPrice(Integer minTradeOutPrice) {
        this.minTradeOutPrice = minTradeOutPrice;
        return this;
    }

    public void setMinTradeOutPrice(Integer minTradeOutPrice) {
        this.minTradeOutPrice = minTradeOutPrice;
    }

    public Integer getMinProfitPoints() {
        return this.minProfitPoints;
    }

    public TradeSuggestion minProfitPoints(Integer minProfitPoints) {
        this.minProfitPoints = minProfitPoints;
        return this;
    }

    public void setMinProfitPoints(Integer minProfitPoints) {
        this.minProfitPoints = minProfitPoints;
    }

    public Integer getBetterTradeoutPrice() {
        return this.betterTradeoutPrice;
    }

    public TradeSuggestion betterTradeoutPrice(Integer betterTradeoutPrice) {
        this.betterTradeoutPrice = betterTradeoutPrice;
        return this;
    }

    public void setBetterTradeoutPrice(Integer betterTradeoutPrice) {
        this.betterTradeoutPrice = betterTradeoutPrice;
    }

    public Integer getBetterTradeOutProfitPoints() {
        return this.betterTradeOutProfitPoints;
    }

    public TradeSuggestion betterTradeOutProfitPoints(Integer betterTradeOutProfitPoints) {
        this.betterTradeOutProfitPoints = betterTradeOutProfitPoints;
        return this;
    }

    public void setBetterTradeOutProfitPoints(Integer betterTradeOutProfitPoints) {
        this.betterTradeOutProfitPoints = betterTradeOutProfitPoints;
    }

    public Integer getActualTradeoutPrice() {
        return this.actualTradeoutPrice;
    }

    public TradeSuggestion actualTradeoutPrice(Integer actualTradeoutPrice) {
        this.actualTradeoutPrice = actualTradeoutPrice;
        return this;
    }

    public void setActualTradeoutPrice(Integer actualTradeoutPrice) {
        this.actualTradeoutPrice = actualTradeoutPrice;
    }

    public Integer getActualProfitPoints() {
        return this.actualProfitPoints;
    }

    public TradeSuggestion actualProfitPoints(Integer actualProfitPoints) {
        this.actualProfitPoints = actualProfitPoints;
        return this;
    }

    public void setActualProfitPoints(Integer actualProfitPoints) {
        this.actualProfitPoints = actualProfitPoints;
    }

    public Integer getSlPoints() {
        return this.slPoints;
    }

    public TradeSuggestion slPoints(Integer slPoints) {
        this.slPoints = slPoints;
        return this;
    }

    public void setSlPoints(Integer slPoints) {
        this.slPoints = slPoints;
    }

    public TradeStatus getTradeStatus() {
        return this.tradeStatus;
    }

    public TradeSuggestion tradeStatus(TradeStatus tradeStatus) {
        this.tradeStatus = tradeStatus;
        return this;
    }

    public void setTradeStatus(TradeStatus tradeStatus) {
        this.tradeStatus = tradeStatus;
    }

    public TradeResult getTradeResults() {
        return this.tradeResults;
    }

    public TradeSuggestion tradeResults(TradeResult tradeResults) {
        this.tradeResults = tradeResults;
        return this;
    }

    public void setTradeResults(TradeResult tradeResults) {
        this.tradeResults = tradeResults;
    }

    public ZonedDateTime getTradeInTime() {
        return this.tradeInTime;
    }

    public TradeSuggestion tradeInTime(ZonedDateTime tradeInTime) {
        this.tradeInTime = tradeInTime;
        return this;
    }

    public void setTradeInTime(ZonedDateTime tradeInTime) {
        this.tradeInTime = tradeInTime;
    }

    public ZonedDateTime getTradeOutTime() {
        return this.tradeOutTime;
    }

    public TradeSuggestion tradeOutTime(ZonedDateTime tradeOutTime) {
        this.tradeOutTime = tradeOutTime;
        return this;
    }

    public void setTradeOutTime(ZonedDateTime tradeOutTime) {
        this.tradeOutTime = tradeOutTime;
    }

    public Integer getTradeDuration() {
        return this.tradeDuration;
    }

    public TradeSuggestion tradeDuration(Integer tradeDuration) {
        this.tradeDuration = tradeDuration;
        return this;
    }

    public void setTradeDuration(Integer tradeDuration) {
        this.tradeDuration = tradeDuration;
    }

    public LocalDate getTradeDate() {
        return this.tradeDate;
    }

    public TradeSuggestion tradeDate(LocalDate tradeDate) {
        this.tradeDate = tradeDate;
        return this;
    }

    public void setTradeDate(LocalDate tradeDate) {
        this.tradeDate = tradeDate;
    }

    public Instant getTradeSuggestionTime() {
        return this.tradeSuggestionTime;
    }

    public TradeSuggestion tradeSuggestionTime(Instant tradeSuggestionTime) {
        this.tradeSuggestionTime = tradeSuggestionTime;
        return this;
    }

    public void setTradeSuggestionTime(Instant tradeSuggestionTime) {
        this.tradeSuggestionTime = tradeSuggestionTime;
    }

    public TradeType getTradeType() {
        return this.tradeType;
    }

    public TradeSuggestion tradeType(TradeType tradeType) {
        this.tradeType = tradeType;
        return this;
    }

    public void setTradeType(TradeType tradeType) {
        this.tradeType = tradeType;
    }

    public Integer getActualPL() {
        return this.actualPL;
    }

    public TradeSuggestion actualPL(Integer actualPL) {
        this.actualPL = actualPL;
        return this;
    }

    public void setActualPL(Integer actualPL) {
        this.actualPL = actualPL;
    }

    public Integer getSlPrice() {
        return this.slPrice;
    }

    public TradeSuggestion slPrice(Integer slPrice) {
        this.slPrice = slPrice;
        return this;
    }

    public void setSlPrice(Integer slPrice) {
        this.slPrice = slPrice;
    }

    public Integer getCurrentMarketPrice() {
        return this.currentMarketPrice;
    }

    public TradeSuggestion currentMarketPrice(Integer currentMarketPrice) {
        this.currentMarketPrice = currentMarketPrice;
        return this;
    }

    public void setCurrentMarketPrice(Integer currentMarketPrice) {
        this.currentMarketPrice = currentMarketPrice;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof TradeSuggestion)) {
            return false;
        }
        return id != null && id.equals(((TradeSuggestion) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "TradeSuggestion{" +
            "\nid=" + getId() +
            "\n, action='" + getAction() + "'" +
            "\n, tradeInPrice=" + getTradeInPrice() +
            "\n, minTradeOutPrice=" + getMinTradeOutPrice() +
            "\n, minProfitPoints=" + getMinProfitPoints() +
            "\n, betterTradeoutPrice=" + getBetterTradeoutPrice() +
            "\n, betterTradeOutProfitPoints=" + getBetterTradeOutProfitPoints() +
            "\n, actualTradeoutPrice=" + getActualTradeoutPrice() +
            "\n, actualProfitPoints=" + getActualProfitPoints() +
            "\n, slPoints=" + getSlPoints() +
            "\n, tradeStatus='" + getTradeStatus() + "'" +
            "\n, tradeResults='" + getTradeResults() + "'" +
            "\n, tradeInTime='" + getTradeInTime() + "'" +
            "\n, tradeOutTime='" + getTradeOutTime() + "'" +
            "\n, tradeDuration=" + getTradeDuration() +
            "\n, tradeDate='" + getTradeDate() + "'" +
            "\n, tradeSuggestionTime='" + getTradeSuggestionTime() + "'" +
            "\n, tradeType='" + getTradeType() + "'" +
            "\n, actualPL=" + getActualPL() +
            "\n, slPrice=" + getSlPrice() +
            "\n, currentMarketPrice=" + getCurrentMarketPrice() +
            "}";
    }
}

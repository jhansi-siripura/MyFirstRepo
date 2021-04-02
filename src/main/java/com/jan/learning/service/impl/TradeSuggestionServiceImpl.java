package com.jan.learning.service.impl;

import com.jan.learning.domain.TradeSuggestion;
import com.jan.learning.repository.TradeSuggestionRepository;
import com.jan.learning.service.TradeSuggestionService;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link TradeSuggestion}.
 */
@Service
@Transactional
public class TradeSuggestionServiceImpl implements TradeSuggestionService {

    private final Logger log = LoggerFactory.getLogger(TradeSuggestionServiceImpl.class);

    private final TradeSuggestionRepository tradeSuggestionRepository;

    public TradeSuggestionServiceImpl(TradeSuggestionRepository tradeSuggestionRepository) {
        this.tradeSuggestionRepository = tradeSuggestionRepository;
    }

    @Override
    public TradeSuggestion save(TradeSuggestion tradeSuggestion) {
        log.debug("Request to save TradeSuggestion : {}", tradeSuggestion);
        return tradeSuggestionRepository.save(tradeSuggestion);
    }

    @Override
    public Optional<TradeSuggestion> partialUpdate(TradeSuggestion tradeSuggestion) {
        log.debug("Request to partially update TradeSuggestion : {}", tradeSuggestion);

        return tradeSuggestionRepository
            .findById(tradeSuggestion.getId())
            .map(
                existingTradeSuggestion -> {
                    if (tradeSuggestion.getAction() != null) {
                        existingTradeSuggestion.setAction(tradeSuggestion.getAction());
                    }
                    if (tradeSuggestion.getTradeInPrice() != null) {
                        existingTradeSuggestion.setTradeInPrice(tradeSuggestion.getTradeInPrice());
                    }
                    if (tradeSuggestion.getMinTradeOutPrice() != null) {
                        existingTradeSuggestion.setMinTradeOutPrice(tradeSuggestion.getMinTradeOutPrice());
                    }
                    if (tradeSuggestion.getMinProfitPoints() != null) {
                        existingTradeSuggestion.setMinProfitPoints(tradeSuggestion.getMinProfitPoints());
                    }
                    if (tradeSuggestion.getBetterTradeoutPrice() != null) {
                        existingTradeSuggestion.setBetterTradeoutPrice(tradeSuggestion.getBetterTradeoutPrice());
                    }
                    if (tradeSuggestion.getBetterTradeOutProfitPoints() != null) {
                        existingTradeSuggestion.setBetterTradeOutProfitPoints(tradeSuggestion.getBetterTradeOutProfitPoints());
                    }
                    if (tradeSuggestion.getActualTradeoutPrice() != null) {
                        existingTradeSuggestion.setActualTradeoutPrice(tradeSuggestion.getActualTradeoutPrice());
                    }
                    if (tradeSuggestion.getActualProfitPoints() != null) {
                        existingTradeSuggestion.setActualProfitPoints(tradeSuggestion.getActualProfitPoints());
                    }
                    if (tradeSuggestion.getSlPoints() != null) {
                        existingTradeSuggestion.setSlPoints(tradeSuggestion.getSlPoints());
                    }
                    if (tradeSuggestion.getTradeStatus() != null) {
                        existingTradeSuggestion.setTradeStatus(tradeSuggestion.getTradeStatus());
                    }
                    if (tradeSuggestion.getTradeResults() != null) {
                        existingTradeSuggestion.setTradeResults(tradeSuggestion.getTradeResults());
                    }
                    if (tradeSuggestion.getTradeInTime() != null) {
                        existingTradeSuggestion.setTradeInTime(tradeSuggestion.getTradeInTime());
                    }
                    if (tradeSuggestion.getTradeOutTime() != null) {
                        existingTradeSuggestion.setTradeOutTime(tradeSuggestion.getTradeOutTime());
                    }
                    if (tradeSuggestion.getTradeDuration() != null) {
                        existingTradeSuggestion.setTradeDuration(tradeSuggestion.getTradeDuration());
                    }
                    if (tradeSuggestion.getTradeDate() != null) {
                        existingTradeSuggestion.setTradeDate(tradeSuggestion.getTradeDate());
                    }
                    if (tradeSuggestion.getTradeSuggestionTime() != null) {
                        existingTradeSuggestion.setTradeSuggestionTime(tradeSuggestion.getTradeSuggestionTime());
                    }
                    if (tradeSuggestion.getTradeType() != null) {
                        existingTradeSuggestion.setTradeType(tradeSuggestion.getTradeType());
                    }
                    if (tradeSuggestion.getActualPL() != null) {
                        existingTradeSuggestion.setActualPL(tradeSuggestion.getActualPL());
                    }
                    if (tradeSuggestion.getSlPrice() != null) {
                        existingTradeSuggestion.setSlPrice(tradeSuggestion.getSlPrice());
                    }
                    if (tradeSuggestion.getCurrentMarketPrice() != null) {
                        existingTradeSuggestion.setCurrentMarketPrice(tradeSuggestion.getCurrentMarketPrice());
                    }

                    return existingTradeSuggestion;
                }
            )
            .map(tradeSuggestionRepository::save);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<TradeSuggestion> findAll(Pageable pageable) {
        log.debug("Request to get all TradeSuggestions");
        return tradeSuggestionRepository.findAll(pageable);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<TradeSuggestion> findOne(Long id) {
        log.debug("Request to get TradeSuggestion : {}", id);
        return tradeSuggestionRepository.findById(id);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete TradeSuggestion : {}", id);
        tradeSuggestionRepository.deleteById(id);
    }
}

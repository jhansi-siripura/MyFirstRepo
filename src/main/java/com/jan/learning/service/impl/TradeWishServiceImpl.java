package com.jan.learning.service.impl;

import com.jan.learning.domain.TradeWish;
import com.jan.learning.repository.TradeWishRepository;
import com.jan.learning.service.TradeWishService;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link TradeWish}.
 */
@Service
@Transactional
public class TradeWishServiceImpl implements TradeWishService {

    private final Logger log = LoggerFactory.getLogger(TradeWishServiceImpl.class);

    private final TradeWishRepository tradeWishRepository;

    public TradeWishServiceImpl(TradeWishRepository tradeWishRepository) {
        this.tradeWishRepository = tradeWishRepository;
    }

    @Override
    public TradeWish save(TradeWish tradeWish) {
        log.debug("Request to save TradeWish : {}", tradeWish);
        return tradeWishRepository.save(tradeWish);
    }

    @Override
    public Optional<TradeWish> partialUpdate(TradeWish tradeWish) {
        log.debug("Request to partially update TradeWish : {}", tradeWish);

        return tradeWishRepository
            .findById(tradeWish.getId())
            .map(
                existingTradeWish -> {
                    if (tradeWish.getTradeWishNote() != null) {
                        existingTradeWish.setTradeWishNote(tradeWish.getTradeWishNote());
                    }
                    if (tradeWish.getPicked() != null) {
                        existingTradeWish.setPicked(tradeWish.getPicked());
                    }
                    if (tradeWish.getPickedDate() != null) {
                        existingTradeWish.setPickedDate(tradeWish.getPickedDate());
                    }

                    return existingTradeWish;
                }
            )
            .map(tradeWishRepository::save);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<TradeWish> findAll(Pageable pageable) {
        log.debug("Request to get all TradeWishes");
        return tradeWishRepository.findAll(pageable);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<TradeWish> findOne(Long id) {
        log.debug("Request to get TradeWish : {}", id);
        return tradeWishRepository.findById(id);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete TradeWish : {}", id);
        tradeWishRepository.deleteById(id);
    }
}

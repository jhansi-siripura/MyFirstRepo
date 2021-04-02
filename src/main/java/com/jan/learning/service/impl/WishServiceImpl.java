package com.jan.learning.service.impl;

import com.jan.learning.domain.Wish;
import com.jan.learning.repository.WishRepository;
import com.jan.learning.service.WishService;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link Wish}.
 */
@Service
@Transactional
public class WishServiceImpl implements WishService {

    private final Logger log = LoggerFactory.getLogger(WishServiceImpl.class);

    private final WishRepository wishRepository;

    public WishServiceImpl(WishRepository wishRepository) {
        this.wishRepository = wishRepository;
    }

    @Override
    public Wish save(Wish wish) {
        log.debug("Request to save Wish : {}", wish);
        return wishRepository.save(wish);
    }

    @Override
    public Optional<Wish> partialUpdate(Wish wish) {
        log.debug("Request to partially update Wish : {}", wish);

        return wishRepository
            .findById(wish.getId())
            .map(
                existingWish -> {
                    if (wish.getMyWishNote() != null) {
                        existingWish.setMyWishNote(wish.getMyWishNote());
                    }
                    if (wish.getStartDate() != null) {
                        existingWish.setStartDate(wish.getStartDate());
                    }
                    if (wish.getFulfilled() != null) {
                        existingWish.setFulfilled(wish.getFulfilled());
                    }
                    if (wish.getFulfilledDate() != null) {
                        existingWish.setFulfilledDate(wish.getFulfilledDate());
                    }
                    if (wish.getDuration() != null) {
                        existingWish.setDuration(wish.getDuration());
                    }
                    if (wish.getCategory() != null) {
                        existingWish.setCategory(wish.getCategory());
                    }

                    return existingWish;
                }
            )
            .map(wishRepository::save);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<Wish> findAll(Pageable pageable) {
        log.debug("Request to get all Wishes");
        return wishRepository.findAll(pageable);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Wish> findOne(Long id) {
        log.debug("Request to get Wish : {}", id);
        return wishRepository.findById(id);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete Wish : {}", id);
        wishRepository.deleteById(id);
    }
}

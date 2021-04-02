package com.jan.learning.service.impl;

import com.jan.learning.domain.Gratitude;
import com.jan.learning.repository.GratitudeRepository;
import com.jan.learning.service.GratitudeService;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link Gratitude}.
 */
@Service
@Transactional
public class GratitudeServiceImpl implements GratitudeService {

    private final Logger log = LoggerFactory.getLogger(GratitudeServiceImpl.class);

    private final GratitudeRepository gratitudeRepository;

    public GratitudeServiceImpl(GratitudeRepository gratitudeRepository) {
        this.gratitudeRepository = gratitudeRepository;
    }

    @Override
    public Gratitude save(Gratitude gratitude) {
        log.debug("Request to save Gratitude : {}", gratitude);
        return gratitudeRepository.save(gratitude);
    }

    @Override
    public Optional<Gratitude> partialUpdate(Gratitude gratitude) {
        log.debug("Request to partially update Gratitude : {}", gratitude);

        return gratitudeRepository
            .findById(gratitude.getId())
            .map(
                existingGratitude -> {
                    if (gratitude.getGratefulNote() != null) {
                        existingGratitude.setGratefulNote(gratitude.getGratefulNote());
                    }
                    if (gratitude.getCreatedDate() != null) {
                        existingGratitude.setCreatedDate(gratitude.getCreatedDate());
                    }
                    if (gratitude.getLoved() != null) {
                        existingGratitude.setLoved(gratitude.getLoved());
                    }
                    if (gratitude.getAchieved() != null) {
                        existingGratitude.setAchieved(gratitude.getAchieved());
                    }

                    return existingGratitude;
                }
            )
            .map(gratitudeRepository::save);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<Gratitude> findAll(Pageable pageable) {
        log.debug("Request to get all Gratitudes");
        return gratitudeRepository.findAll(pageable);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Gratitude> findOne(Long id) {
        log.debug("Request to get Gratitude : {}", id);
        return gratitudeRepository.findById(id);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete Gratitude : {}", id);
        gratitudeRepository.deleteById(id);
    }
}

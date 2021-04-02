package com.jan.learning.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.jan.learning.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class TradeWishTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(TradeWish.class);
        TradeWish tradeWish1 = new TradeWish();
        tradeWish1.setId(1L);
        TradeWish tradeWish2 = new TradeWish();
        tradeWish2.setId(tradeWish1.getId());
        assertThat(tradeWish1).isEqualTo(tradeWish2);
        tradeWish2.setId(2L);
        assertThat(tradeWish1).isNotEqualTo(tradeWish2);
        tradeWish1.setId(null);
        assertThat(tradeWish1).isNotEqualTo(tradeWish2);
    }
}

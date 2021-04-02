package com.jan.learning.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.jan.learning.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class TradeSuggestionTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(TradeSuggestion.class);
        TradeSuggestion tradeSuggestion1 = new TradeSuggestion();
        tradeSuggestion1.setId(1L);
        TradeSuggestion tradeSuggestion2 = new TradeSuggestion();
        tradeSuggestion2.setId(tradeSuggestion1.getId());
        assertThat(tradeSuggestion1).isEqualTo(tradeSuggestion2);
        tradeSuggestion2.setId(2L);
        assertThat(tradeSuggestion1).isNotEqualTo(tradeSuggestion2);
        tradeSuggestion1.setId(null);
        assertThat(tradeSuggestion1).isNotEqualTo(tradeSuggestion2);
    }
}

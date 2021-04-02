package com.jan.learning.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.jan.learning.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class GratitudeTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Gratitude.class);
        Gratitude gratitude1 = new Gratitude();
        gratitude1.setId(1L);
        Gratitude gratitude2 = new Gratitude();
        gratitude2.setId(gratitude1.getId());
        assertThat(gratitude1).isEqualTo(gratitude2);
        gratitude2.setId(2L);
        assertThat(gratitude1).isNotEqualTo(gratitude2);
        gratitude1.setId(null);
        assertThat(gratitude1).isNotEqualTo(gratitude2);
    }
}

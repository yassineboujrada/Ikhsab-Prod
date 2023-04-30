package com.agri40.management.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.agri40.management.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class CowTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Cow.class);
        Cow cow1 = new Cow();
        cow1.setId("id1");
        Cow cow2 = new Cow();
        cow2.setId(cow1.getId());
        assertThat(cow1).isEqualTo(cow2);
        cow2.setId("id2");
        assertThat(cow1).isNotEqualTo(cow2);
        cow1.setId(null);
        assertThat(cow1).isNotEqualTo(cow2);
    }
}

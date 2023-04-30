package com.agri40.management.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.agri40.management.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class EnclosTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Enclos.class);
        Enclos enclos1 = new Enclos();
        enclos1.setId("id1");
        Enclos enclos2 = new Enclos();
        enclos2.setId(enclos1.getId());
        assertThat(enclos1).isEqualTo(enclos2);
        enclos2.setId("id2");
        assertThat(enclos1).isNotEqualTo(enclos2);
        enclos1.setId(null);
        assertThat(enclos1).isNotEqualTo(enclos2);
    }
}

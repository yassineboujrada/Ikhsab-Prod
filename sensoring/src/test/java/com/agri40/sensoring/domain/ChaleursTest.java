package com.agri40.sensoring.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.agri40.sensoring.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class ChaleursTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Chaleurs.class);
        Chaleurs chaleurs1 = new Chaleurs();
        chaleurs1.setId("id1");
        Chaleurs chaleurs2 = new Chaleurs();
        chaleurs2.setId(chaleurs1.getId());
        assertThat(chaleurs1).isEqualTo(chaleurs2);
        chaleurs2.setId("id2");
        assertThat(chaleurs1).isNotEqualTo(chaleurs2);
        chaleurs1.setId(null);
        assertThat(chaleurs1).isNotEqualTo(chaleurs2);
    }
}

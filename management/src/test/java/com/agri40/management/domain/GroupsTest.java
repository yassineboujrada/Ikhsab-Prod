package com.agri40.management.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.agri40.management.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class GroupsTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Groups.class);
        Groups groups1 = new Groups();
        groups1.setId("id1");
        Groups groups2 = new Groups();
        groups2.setId(groups1.getId());
        assertThat(groups1).isEqualTo(groups2);
        groups2.setId("id2");
        assertThat(groups1).isNotEqualTo(groups2);
        groups1.setId(null);
        assertThat(groups1).isNotEqualTo(groups2);
    }
}

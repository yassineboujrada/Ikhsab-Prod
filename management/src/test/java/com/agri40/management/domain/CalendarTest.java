package com.agri40.management.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.agri40.management.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class CalendarTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Calendar.class);
        Calendar calendar1 = new Calendar();
        calendar1.setId("id1");
        Calendar calendar2 = new Calendar();
        calendar2.setId(calendar1.getId());
        assertThat(calendar1).isEqualTo(calendar2);
        calendar2.setId("id2");
        assertThat(calendar1).isNotEqualTo(calendar2);
        calendar1.setId(null);
        assertThat(calendar1).isNotEqualTo(calendar2);
    }
}

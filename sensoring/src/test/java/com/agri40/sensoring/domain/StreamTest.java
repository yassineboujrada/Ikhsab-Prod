package com.agri40.sensoring.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.agri40.sensoring.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class StreamTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Stream.class);
        Stream stream1 = new Stream();
        stream1.setId("id1");
        Stream stream2 = new Stream();
        stream2.setId(stream1.getId());
        assertThat(stream1).isEqualTo(stream2);
        stream2.setId("id2");
        assertThat(stream1).isNotEqualTo(stream2);
        stream1.setId(null);
        assertThat(stream1).isNotEqualTo(stream2);
    }
}

package com.agri40.notification.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.agri40.notification.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class JobRequestTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(JobRequest.class);
        JobRequest jobRequest1 = new JobRequest();
        jobRequest1.setId("id1");
        JobRequest jobRequest2 = new JobRequest();
        jobRequest2.setId(jobRequest1.getId());
        assertThat(jobRequest1).isEqualTo(jobRequest2);
        jobRequest2.setId("id2");
        assertThat(jobRequest1).isNotEqualTo(jobRequest2);
        jobRequest1.setId(null);
        assertThat(jobRequest1).isNotEqualTo(jobRequest2);
    }
}

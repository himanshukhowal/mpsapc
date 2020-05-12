package com.mps.domain;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import com.mps.web.rest.TestUtil;

public class WaiverTest {

    @Test
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Waiver.class);
        Waiver waiver1 = new Waiver();
        waiver1.setId(1L);
        Waiver waiver2 = new Waiver();
        waiver2.setId(waiver1.getId());
        assertThat(waiver1).isEqualTo(waiver2);
        waiver2.setId(2L);
        assertThat(waiver1).isNotEqualTo(waiver2);
        waiver1.setId(null);
        assertThat(waiver1).isNotEqualTo(waiver2);
    }
}

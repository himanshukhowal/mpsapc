package com.mps.domain;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import com.mps.web.rest.TestUtil;

public class ManuscriptTest {

    @Test
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Manuscript.class);
        Manuscript manuscript1 = new Manuscript();
        manuscript1.setId(1L);
        Manuscript manuscript2 = new Manuscript();
        manuscript2.setId(manuscript1.getId());
        assertThat(manuscript1).isEqualTo(manuscript2);
        manuscript2.setId(2L);
        assertThat(manuscript1).isNotEqualTo(manuscript2);
        manuscript1.setId(null);
        assertThat(manuscript1).isNotEqualTo(manuscript2);
    }
}

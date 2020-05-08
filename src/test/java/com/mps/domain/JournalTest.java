package com.mps.domain;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import com.mps.web.rest.TestUtil;

public class JournalTest {

    @Test
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Journal.class);
        Journal journal1 = new Journal();
        journal1.setId(1L);
        Journal journal2 = new Journal();
        journal2.setId(journal1.getId());
        assertThat(journal1).isEqualTo(journal2);
        journal2.setId(2L);
        assertThat(journal1).isNotEqualTo(journal2);
        journal1.setId(null);
        assertThat(journal1).isNotEqualTo(journal2);
    }
}

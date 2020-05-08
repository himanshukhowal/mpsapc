package com.mps.domain;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import com.mps.web.rest.TestUtil;

public class MailTemplatesTest {

    @Test
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(MailTemplates.class);
        MailTemplates mailTemplates1 = new MailTemplates();
        mailTemplates1.setId(1L);
        MailTemplates mailTemplates2 = new MailTemplates();
        mailTemplates2.setId(mailTemplates1.getId());
        assertThat(mailTemplates1).isEqualTo(mailTemplates2);
        mailTemplates2.setId(2L);
        assertThat(mailTemplates1).isNotEqualTo(mailTemplates2);
        mailTemplates1.setId(null);
        assertThat(mailTemplates1).isNotEqualTo(mailTemplates2);
    }
}

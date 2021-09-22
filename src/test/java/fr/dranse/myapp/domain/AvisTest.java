package fr.dranse.myapp.domain;

import static org.assertj.core.api.Assertions.assertThat;

import fr.dranse.myapp.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class AvisTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Avis.class);
        Avis avis1 = new Avis();
        avis1.setId(1L);
        Avis avis2 = new Avis();
        avis2.setId(avis1.getId());
        assertThat(avis1).isEqualTo(avis2);
        avis2.setId(2L);
        assertThat(avis1).isNotEqualTo(avis2);
        avis1.setId(null);
        assertThat(avis1).isNotEqualTo(avis2);
    }
}

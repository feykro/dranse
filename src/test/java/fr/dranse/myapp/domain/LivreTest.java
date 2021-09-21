package fr.dranse.myapp.domain;

import static org.assertj.core.api.Assertions.assertThat;

import fr.dranse.myapp.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class LivreTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Livre.class);
        Livre livre1 = new Livre();
        livre1.setId(1L);
        Livre livre2 = new Livre();
        livre2.setId(livre1.getId());
        assertThat(livre1).isEqualTo(livre2);
        livre2.setId(2L);
        assertThat(livre1).isNotEqualTo(livre2);
        livre1.setId(null);
        assertThat(livre1).isNotEqualTo(livre2);
    }
}

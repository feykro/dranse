package fr.dranse.myapp.domain;

import static org.assertj.core.api.Assertions.assertThat;

import fr.dranse.myapp.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class LigneCommandeTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(LigneCommande.class);
        LigneCommande ligneCommande1 = new LigneCommande();
        ligneCommande1.setId(1L);
        LigneCommande ligneCommande2 = new LigneCommande();
        ligneCommande2.setId(ligneCommande1.getId());
        assertThat(ligneCommande1).isEqualTo(ligneCommande2);
        ligneCommande2.setId(2L);
        assertThat(ligneCommande1).isNotEqualTo(ligneCommande2);
        ligneCommande1.setId(null);
        assertThat(ligneCommande1).isNotEqualTo(ligneCommande2);
    }
}

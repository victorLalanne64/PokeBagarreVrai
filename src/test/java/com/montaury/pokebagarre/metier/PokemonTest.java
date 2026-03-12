package com.montaury.pokebagarre.metier;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

class PokemonTest {


    @Test
    void premier_serait_vainqueur_avec_meilleur_attaque() {
        //GIVEN
        var premier = new Pokemon("Pikachu", "urlImage", new Stats(12,12));
        var second = new Pokemon("chocapic", "urlImage", new Stats(10,12));
        //WHEN
        var estVainqueur = premier.estVainqueurContre(second);
        // THEN
        assertThat(estVainqueur).isTrue();
    }
    @Test
    void second_serait_vainqueur_avec_meilleur_attaque() {
        //GIVEN
        var premier = new Pokemon("Pikachu", "urlImage", new Stats(10,12));
        var second = new Pokemon("chocapic", "urlImage", new Stats(12,12));
        //WHEN
        var estVainqueur = second.estVainqueurContre(premier);
        // THEN
        assertThat(estVainqueur).isTrue();
    }
    @Test
    void premier_serait_vainqueur_avec_attaque_et_defense_egale() {
        //GIVEN
        var premier = new Pokemon("Pikachu", "urlImage", new Stats(12,12));
        var second = new Pokemon("chocapic", "urlImage", new Stats(12,12));
        //WHEN
        var estVainqueur = premier.estVainqueurContre(second);
        // THEN
        assertThat(estVainqueur).isTrue();
    }
    @Test
    void premier_serait_vainqueur_avec_attaque_egale_et_meilleur_defense() {
        //GIVEN
        var premier = new Pokemon("Pikachu", "urlImage", new Stats(12,14));
        var second = new Pokemon("chocapic", "urlImage", new Stats(12,12));
        //WHEN
        var estVainqueur = premier.estVainqueurContre(second);
        // THEN
        assertThat(estVainqueur).isTrue();
    }
    @Test
    void second_serait_vainqueur_avec_attaque_egale_et_meilleur_defense() {
        //GIVEN
        var premier = new Pokemon("Pikachu", "urlImage", new Stats(12,12));
        var second = new Pokemon("chocapic", "urlImage", new Stats(12,14));
        //WHEN
        var estVainqueur = second.estVainqueurContre(premier);
        // THEN
        assertThat(estVainqueur).isTrue();
    }
}


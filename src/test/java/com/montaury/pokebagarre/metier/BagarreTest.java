package com.montaury.pokebagarre.metier;

import com.montaury.pokebagarre.erreurs.ErreurMemePokemon;
import com.montaury.pokebagarre.erreurs.ErreurPokemonNonRenseigne;
import com.montaury.pokebagarre.erreurs.ErreurRecuperationPokemon;
import com.montaury.pokebagarre.webapi.PokeBuildApi;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.time.Duration;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

import static org.assertj.core.api.Assertions.*;

class BagarreTest {

    @Test
    void demarrer_Devrait_Lever_Exception_Si_Premier_Pokemon_Vide() {
        // Given
        Bagarre bagarre = new Bagarre();

        // When
        Throwable thrown = catchThrowable(() -> bagarre.demarrer("", "Bulbizarre"));

        // Then
        assertThat(thrown)
                .isInstanceOf(ErreurPokemonNonRenseigne.class)
                .hasMessage("Le nom du premier Pokémon est vide ou non renseigné.");
    }

    @Test
    void demarrer_devrait_lever_exception_si_premier_pokemon_null() {
        // Given
        Bagarre bagarre = new Bagarre();

        // When
        Throwable thrown = catchThrowable(() -> bagarre.demarrer(null, "Bulbizarre"));

        // Then
        assertThat(thrown)
                .isInstanceOf(ErreurPokemonNonRenseigne.class)
                .hasMessage("Le nom du premier Pokémon est vide ou non renseigné.");
    }

    @Test
    void demarrer_Devrait_Lever_Exception_Si_Premier_Pokemon_Compose_De_Espaces() {
        // Given
        Bagarre bagarre = new Bagarre();

        // When
        Throwable thrown = catchThrowable(() -> bagarre.demarrer("    ", "Bulbizarre"));

        // Then
        assertThat(thrown)
                .isInstanceOf(ErreurPokemonNonRenseigne.class)
                .hasMessage("Le nom du premier Pokémon est vide ou non renseigné.");
    }

    @Test
    void demarrer_devrait_lever_exception_si_deuxieme_pokemon_vide(){

        // Given
        Bagarre bagarre = new Bagarre();

        // When
        Throwable thrown = catchThrowable(() -> bagarre.demarrer("Buldbizrare", ""));

        // Then
        assertThat(thrown)
                .isInstanceOf(ErreurPokemonNonRenseigne.class)
                .hasMessage("Le nom du deuxième Pokémon est vide ou non renseigné.");

    }

    @Test
    void demarrer_devrait_lever_exception_si_deuxieme_pokemon_null() {
        // Given
        Bagarre bagarre = new Bagarre();

        // When
        Throwable thrown = catchThrowable(() -> bagarre.demarrer("Bulbizarre", null));

        // Then
        assertThat(thrown)
                .isInstanceOf(ErreurPokemonNonRenseigne.class)
                .hasMessage("Le nom du deuxieme Pokémon est vide ou non renseigné.");
    }

    @Test
    void demarrer_Devrait_Lever_Exception_Si_Deuxieme_Pokemon_Compose_De_Espaces() {
        // Given
        Bagarre bagarre = new Bagarre();

        // When
        Throwable thrown = catchThrowable(() -> bagarre.demarrer("Bulbizarre", "   "));

        // Then
        assertThat(thrown)
                .isInstanceOf(ErreurPokemonNonRenseigne.class)
                .hasMessage("Le nom du deuxieme Pokémon est vide ou non renseigné.");
    }

    @Test
    void demarrer_devrait_lever_exception_si_les_noms_sont_egaux() {

        // Given
        Bagarre bagarre = new Bagarre();

        // When
        Throwable thrown = catchThrowable(() -> bagarre.demarrer("Buldbizarre", "Buldbizarre"));

        // Then
        assertThat(thrown)
                .isInstanceOf(ErreurMemePokemon.class)
                .hasMessage("Impossible de faire se bagarrer un pokemon avec lui-meme");
    }

    private PokeBuildApi fausseApi;
    private Bagarre bagarre;

    @BeforeEach
    void setUp() {
        fausseApi = Mockito.mock(PokeBuildApi.class);
        bagarre = new Bagarre(fausseApi);
    }

    @Test
    void demarrer_devrait_determiner_le_vainqueur_quand_l_api_repond() {
        // Given
        var pikachu = new Pokemon("Pikachu", "url1", new Stats(10, 5));
        var bulbizarre = new Pokemon("Bulbizarre", "url2", new Stats(5, 5));

        Mockito.when(fausseApi.recupererParNom("Pikachu"))
                .thenReturn(CompletableFuture.completedFuture(pikachu));

        Mockito.when(fausseApi.recupererParNom("Bulbizarre"))
                .thenReturn(CompletableFuture.completedFuture(bulbizarre));

        // When
        var futurVainqueur = bagarre.demarrer("Pikachu", "Bulbizarre");

        // Then
        assertThat(futurVainqueur)
                .succeedsWithin(Duration.ofSeconds(2))
            .satisfies(pokemon -> {
            assertThat(pokemon.getNom()).isEqualTo("Pikachu");
        });
    }
    @Test
    void demarrer_devrait_echouer_si_l_api_renvoie_une_erreur() {
        // Given
        Mockito.when(fausseApi.recupererParNom("Inconnu"))
                .thenReturn(CompletableFuture.failedFuture(new ErreurRecuperationPokemon("Inconnu")));

        // When
        var futurVainqueur = bagarre.demarrer("Inconnu", "Bulbizarre");

        // Then
        assertThat(futurVainqueur)
                .failsWithin(Duration.ofSeconds(2))
            .withThrowableOfType(ExecutionException.class)
            .havingCause()
            .isInstanceOf(ErreurRecuperationPokemon.class);
    }

}



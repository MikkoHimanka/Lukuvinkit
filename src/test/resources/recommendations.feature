Feature: Sovellus antaa suosituksia, jos haku ei tuota tuloksia

Scenario: sovellus antaa suosituksia, jos luetuksi merkkaaminen ei tuota hakutuloksia
    Given tietokanta on alustettu
    And tietokannassa on esimerkkikirjasto
    When suoritetaan komennot:
        | M |
        | L |
        | bring |
        | P |
    Then sovellus tulostaa "Lukuvinkkeja ei loytynyt annetulla haulla."
    And "Linkki: www.bing.com" on ylempana kuin "Linkki: www.google.com"

Scenario: sovellus antaa suosituksia, jos poistaminen ei tuota hakutuloksia
    Given tietokanta on alustettu
    And tietokannassa on esimerkkikirjasto
    When suoritetaan komennot:
        | P |
        | O |
        | frox |
        | P |
    Then sovellus tulostaa "Lukuvinkkeja ei loytynyt annetulla haulla."
    And "Linkki: www.fox.com" on ylempana kuin "Linkki: www.oracle.com"

Scenario: sovellus antaa suosituksia, jos muokkaaminen ei tuota hakutuloksia
    Given tietokanta on alustettu
    And tietokannassa on esimerkkikirjasto
    When suoritetaan komennot:
        | MU |
        | O |
        | gitgrub |
        | P |
    Then sovellus tulostaa "Lukuvinkkeja ei loytynyt annetulla haulla."
    And "Linkki: www.github.com" on ylempana kuin "Linkki: www.bing.com"

Scenario: lukuvinkki on mahdollista poistaa valitsemalla lukuvinkin numero suositukset-listalta
    Given tietokanta on alustettu
    And tietokannassa on esimerkkikirjasto
    When suoritetaan komennot:
        | P |
        | O |
        | frox |
        | V |
        | 1 |
        | K |
    Then sovellus tulostaa "Lukuvinkki on poistettu!"

Scenario: lukuvinkki on mahdollista merkata luetuksi valitsemalla lukuvinkin numero suositukset-listalta
    Given tietokanta on alustettu
    And tietokannassa on esimerkkikirjasto
    When suoritetaan komennot:
        | M |
        | O |
        | gorogle |
        | V |
        | 1 |
    Then sovellus tulostaa "Lukuvinkki merkitty luetuksi!"

Scenario: lukuvinkki on mahdollista muokata valitsemalla lukuvinkin numero suositukset-listalta
    Given tietokanta on alustettu
    And tietokannassa on esimerkkikirjasto
    When suoritetaan komennot:
        | MU |
        | O |
        | bring |
        | V |
        | 1 |
        | O |
        | Bong |
    Then sovellus tulostaa "Lukuvinkin tiedot paivitetty onnistuneesti."
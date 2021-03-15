Feature: Kayttajana voin lisata lukuvinkin, jonka lisaysaika tallennetaan

Scenario: lukuvinkin lisayksen yhteydessa tallennetaan lisaysaika
    Given tietokanta on alustettu
    When  tietokantaan tallennetaan lukuvinkki aikaleimalla "02-22-2022 kello 00:00" 
    And   valitaan komento "N"
    Then  sovellus tulostaa "Luotu: 02-22-2022 kello 00:00"

Scenario: sovellus nayttaa lisaysajan lukuvinkkeja listatessa
    Given tietokanta on alustettu
    When  valitaan komento "L" ja syotetaan linkki "www.is.fi" seka otsikko "", eika syoteta tageja tietoja pyydettaessa
    And   valitaan komento "N"
    Then  sovellus nayttaa lukuvinkille oikean lisaysajan

Scenario: lukuvinkin lisaysaika ei muutu lukuvinkin tietoja muokatessa
    Given tietokanta on alustettu
    When  tietokantaan tallennetaan lukuvinkki aikaleimalla "02-22-2022 kello 00:00"
    And suoritetaan komennot:
        | Mu |
        | V |
        | 1 |
        | O |
        | iltasanomat |
    And   valitaan komento "N"
    Then  sovellus tulostaa "Luotu: 02-22-2022 kello 00:00"

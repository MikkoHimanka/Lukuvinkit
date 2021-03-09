Feature: Kayttajana voin lukea listan lukuvinkeista

Scenario: sovellus ilmoittaa jos tietokantaan ei ole tallennettu lukuvinkkeja
    Given tietokanta on alustettu
    When  valitaan komento "N"
    Then  sovellus tulostaa "Lukuvinkkeja ei loytynyt."

Scenario: sovellus listaa olemassaolevat lukuvinkit
    Given tietokanta on alustettu
    When  tietokantaan tallennetaan kaksi lukuvinkkia
    And   valitaan komento "L" ja syotetaan linkki "www.is.fi" seka otsikko "lehti", eika syoteta tageja tietoja pyydettaessa
    And   valitaan komento "N"
    Then  sovellus tulostaa "Loytyi 3 lukuvinkkia:"
    Then  sovellus listaa lukuvinkit
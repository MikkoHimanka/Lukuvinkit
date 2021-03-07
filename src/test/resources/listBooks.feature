Feature: Käyttäjänä voin lukea listan lukuvinkeista

Scenario: sovellus ilmoittaa jos tietokantaan ei ole tallennettu lukuvinkkejä
    Given tietokanta on alustettu
    When  valitaan komento "N"
    Then  sovellus tulostaa "Lukuvinkkejä ei löytynyt."

Scenario: sovellus listaa olemassaolevat lukuvinkit
    Given tietokanta on alustettu
    When  tietokantaan tallennetaan kaksi lukuvinkkiä
    And   valitaan komento "L" ja syötetään linkki "www.is.fi" sekä otsikko "lehti" tietoja pyydettäessä
    And   valitaan komento "N"
    Then  sovellus tulostaa "Löytyi 3 lukuvinkkiä:"
    Then  sovellus listaa lukuvinkit
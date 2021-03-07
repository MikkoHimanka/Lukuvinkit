Feature: Käyttäjänä haluan merkata lukuvinkin luetuksi ja näen vain lukemattomat vinkit

Scenario: lukuvinkki asetetaan luetuksi lukuvinkin linkin mukaan
    Given tietokanta on alustettu
    When  tietokantaan tallennetaan kaksi lukuvinkkiä
    And   merkataan toinen lukuvinkki luetuksi valitsemalla komennot "M", "V" ja "1"
    Then  sovellus tulostaa "Lukuvinkki merkitty luetuksi!"

Scenario: sovellus listaa vain lukemattomat lukuvinkit
    Given tietokanta on alustettu
    When  tietokantaan tallennetaan kaksi lukuvinkkiä
    And   merkataan toinen lukuvinkki luetuksi valitsemalla komennot "M", "V" ja "1"
    And   valitaan komento "Li"
    Then  sovellus tulostaa "Löytyi 1 lukuvinkkiä:"

Scenario: sovellus ilmoittaa jos lukemattomia lukuvinkkejä ei ole
    Given tietokanta on alustettu
    When  tietokantaan tallennetaan kaksi lukuvinkkiä
    And   merkataan lukuvinkit luetuiksi valitsemalla komennot "M", "V" ja "1" sekä "M" 
    And   valitaan komento "Li"
    Then  sovellus tulostaa "Lukuvinkkejä ei löytynyt."


Feature: Kayttajana haluan merkata lukuvinkin luetuksi ja naen vain lukemattomat vinkit

Scenario: lukuvinkki asetetaan luetuksi lukuvinkin linkin mukaan
    Given tietokanta on alustettu
    When  tietokantaan tallennetaan kaksi lukuvinkkia
    And   merkataan toinen lukuvinkki luetuksi valitsemalla komennot "M", "V" ja "1"
    Then  sovellus tulostaa "Lukuvinkki merkitty luetuksi!"

Scenario: sovellus listaa vain lukemattomat lukuvinkit
    Given tietokanta on alustettu
    When  tietokantaan tallennetaan kaksi lukuvinkkia
    And   merkataan toinen lukuvinkki luetuksi valitsemalla komennot "M", "V" ja "1"
    And   valitaan komento "Li"
    Then  sovellus tulostaa "Loytyi 1 lukuvinkkia:"

Scenario: sovellus ilmoittaa jos lukemattomia lukuvinkkeja ei ole
    Given tietokanta on alustettu
    When  tietokantaan tallennetaan kaksi lukuvinkkia
    And   merkataan lukuvinkit luetuiksi valitsemalla komennot "M", "V" ja "1" seka "M"
    And   valitaan komento "Li"
    Then  sovellus tulostaa "Lukuvinkkeja ei loytynyt."


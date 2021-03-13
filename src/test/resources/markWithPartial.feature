Feature: Kayttajana voin merkita lukuvinkin luetuksi antamalla osan urlista tai otsikosta

Scenario: jos urlin perusteella loytyy vain yksi linkki, merkitaan se luetuksi
    Given tietokanta on alustettu
    When tietokantaan tallennetaan kaksi lukuvinkkia
    And merkataan toinen lukuvinkki luetuksi valitsemalla komennot "M", "T", "L" ja "goog"
    And valitaan komento "Li"
    Then sovellus tulostaa "Loytyi 1 lukuvinkkia:"

Scenario: jos otsikon perusteella loytyy vain yksi linkki, merkitaan se luetuksi
    Given tietokanta on alustettu
    When tietokantaan tallennetaan linkki "yle.fi" otsikolla "yle"
    And tietokantaan tallennetaan linkki "ohjelmointiputka.net" otsikolla "putka"
    And merkataan toinen lukuvinkki luetuksi valitsemalla komennot "M", "T", "O" ja "put"
    And valitaan komento "Li"
    Then sovellus tulostaa "Loytyi 1 lukuvinkkia:"

Scenario: jos otsikon perusteella loytyy useampi linkki, pyydetaan kayttajaa tarkentamaan
    Given tietokanta on alustettu
    When tietokantaan tallennetaan linkki "ohjelmointimutka.net" otsikolla "ohjelmointimutka"
    And tietokantaan tallennetaan linkki "ohjelmointiputka.net" otsikolla "ohjelmointiputka"
    And suoritetaan komennot:
        | M |
        | T |
        | O |
        | ohjelmointi |
        | T |
        | O |
        | mutka |
    And valitaan komento "Li"
    Then sovellus tulostaa "Loytyi 1 lukuvinkkia:"

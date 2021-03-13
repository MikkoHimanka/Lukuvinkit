Feature: Kayttajana haluan poistaa lukuvinkin

Scenario: lukuvinkin voi poistaa antamalla osan urlista
    Given tietokanta on alustettu
    When tietokantaan tallennetaan linkki "aapeli.com" otsikolla "aapeli"
    When tietokantaan tallennetaan linkki "hs.fi" otsikolla "hesari"
    And suoritetaan komennot:
        | P |
        | T |
        | L |
        | aap |
        | N |
    Then sovellus tulostaa "Loytyi 1 lukuvinkkia:"

Scenario: lukuvinkin voi poistaa antamalla osan otsikosta
    Given tietokanta on alustettu
    When tietokantaan tallennetaan linkki "aapeli.com" otsikolla "aapeli"
    When tietokantaan tallennetaan linkki "hs.fi" otsikolla "hesari"
    And suoritetaan komennot:
        | P |
        | T |
        | O |
        | hesa |
        | N |
    Then sovellus tulostaa "Loytyi 1 lukuvinkkia:"

Scenario: jos annettu hakusana löytää useamman vinkin, pyydetään käyttäjää tarkentamaan
    Given tietokanta on alustettu
    When tietokantaan tallennetaan linkki "hs.fi" otsikolla "helsingin sanomat"
    When tietokantaan tallennetaan linkki "helsinki.fi" otsikolla "helsingin yliopisto"
    And suoritetaan komennot:
        | P |
        | T |
        | O |
        | helsingin |
        | T |
        | O |
        | helsingin sanomat |
        | n |
    Then sovellus tulostaa "Loytyi 1 lukuvinkkia:"

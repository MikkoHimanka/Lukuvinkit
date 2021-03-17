Feature: Kayttajana haluan poistaa lukuvinkin

Scenario: lukuvinkin voi poistaa antamalla osan urlista vahvistamalla poiston
    Given tietokanta on alustettu
    When tietokantaan tallennetaan linkki "aapeli.com" otsikolla "aapeli"
    When tietokantaan tallennetaan linkki "hs.fi" otsikolla "hesari"
    And suoritetaan komennot:
        | P |
        | T |
        | L |
        | aap |
        | K |
        | N |
    Then sovellus tulostaa "Loytyi 1 lukuvinkkia:"

Scenario: lukuvinkin voi poistaa antamalla osan otsikosta vahvistamalla poiston
    Given tietokanta on alustettu
    When tietokantaan tallennetaan linkki "aapeli.com" otsikolla "aapeli"
    When tietokantaan tallennetaan linkki "hs.fi" otsikolla "hesari"
    And suoritetaan komennot:
        | P |
        | T |
        | O |
        | hesa |
        | K |
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
        | K |
        | N |
    Then sovellus tulostaa "Loytyi 1 lukuvinkkia:"

Scenario: lukuvinkkia ei poisteta annetulla validilla otsikon osalla mikali kayttaja ei vahvista poistoa
    Given tietokanta on alustettu
        When tietokantaan tallennetaan linkki "aapeli.com" otsikolla "aapeli"
        When tietokantaan tallennetaan linkki "hs.fi" otsikolla "hesari"
        And suoritetaan komennot:
            | P |
            | T |
            | O |
            | hesa |
            | E |
            | N |
        Then sovellus tulostaa "Loytyi 2 lukuvinkkia:"

Scenario: lukuvinkkia ei poisteta mikali tarkennus ei loyda yhtaan poistettavaa lukuvinkkia
    Given tietokanta on alustettu
        When tietokantaan tallennetaan linkki "aapeli.com" otsikolla "aapeli"
        When tietokantaan tallennetaan linkki "hs.fi" otsikolla "hesari"
        And suoritetaan komennot:
            | P |
            | T |
            | O |
            | iltasanomat |
            | P |
            | N |
        Then sovellus tulostaa "Loytyi 2 lukuvinkkia:"


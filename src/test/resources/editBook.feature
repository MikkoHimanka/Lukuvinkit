Feature: Kayttajana voin muokata tallennetun lukuvinkin tietoja

Scenario: otsikon vaihtaminen onnistuu
    Given tietokanta on alustettu
    When tietokantaan tallennetaan linkki "hs.fi" otsikolla "hesari"
    When tietokantaan tallennetaan linkki "is.fi" otsikolla "iltasanomat"
    And suoritetaan komennot:
        | Mu |
        | V |
        | 1 |
        | O |
        | helsingin sanomat |
    And valitaan komento "N"
    Then sovellus tulostaa "Otsikko: helsingin sanomat"

Scenario: kuvauksen vaihtaminen onnistuu
    Given tietokanta on alustettu
    When tietokantaan tallennetaan linkki "hs.fi" otsikolla "hesari"
    When tietokantaan tallennetaan linkki "is.fi" otsikolla "iltasanomat"
    And suoritetaan komennot:
        | Mu |
        | V |
        | 2 |
        | K |
        | lempparisivu |
    And valitaan komento "N"
    Then sovellus tulostaa "Kuvaus: lempparisivu"

Scenario: linkin vaihtaminen onnistuu
    Given tietokanta on alustettu
    When tietokantaan tallennetaan linkki "hs.fi" otsikolla "hesari"
    When tietokantaan tallennetaan linkki "is.fi" otsikolla "iltasanomat"
    And suoritetaan komennot:
        | Mu |
        | V |
        | 1 |
        | L |
        | http://www.bing.com |
    And valitaan komento "N"
    Then sovellus tulostaa "Linkki: http://www.bing.com"


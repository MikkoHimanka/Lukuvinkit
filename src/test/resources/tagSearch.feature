Feature: Kayttajana voin hakea lukuvinkkeja tagin perusteella

Scenario: Vain haetun tagin sisältävät linkit näytetään
    Given tietokanta on alustettu
    When tietokantaan tallennetaan linkki "hs.fi" otsikolla "hesari" ja tagilla "tagi1"
    And tietokantaan tallennetaan linkki "yle.fi" otsikolla "yle" ja tagilla "tagi2"
    And suoritetaan komennot:
        | E |
        | T |
        | tagi1 |
    Then sovellus tulostaa "Linkki: hs.fi"

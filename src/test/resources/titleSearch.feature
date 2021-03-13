Feature: Kayttajana voin hakea lukuvinkkeja otsikon avulla

Scenario: lukuvinkin haku onnistuu, kun syotetty otsikko vastaa tasmalleen lukuvinkin otsikkoa
    Given tietokanta on alustettu
    When tietokantaan tallennetaan linkki "hs.fi" otsikolla "hesari"
    And tietokantaan tallennetaan linkki "yle.fi/uutiset" otsikolla "ylen uutiset"
    And haetaan lukuvinkeista komennoilla "E", "O" ja syotetaan hakuehto "hesari"
    Then sovellus tulostaa "Loytyi 1 lukuvinkkia:"


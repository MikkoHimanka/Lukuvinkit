Feature: Kayttajana voin lisata uuden lukuvinkin linkin ja otsikon

Scenario: lukuvinkin lisays onnistuu kun syotetaan toimiva linkki ja otsikko
    Given tietokanta on alustettu
    When  valitaan komento "L" ja syotetaan linkki "www.is.fi" seka otsikko "lehti", eika syoteta tageja tietoja pyydettaessa
    Then  sovellus hyvaksyy syotteet ja tulostaa "Lukuvinkki lisatty onnistuneesti"

Scenario: lukuvinkin lisays onnistuu kun syotetaan toimiva linkki ja tyhja otsikko
    Given tietokanta on alustettu
    When  valitaan komento "L" ja syotetaan linkki "www.is.fi" seka otsikko "", eika syoteta tageja tietoja pyydettaessa
    Then  sovellus hyvaksyy syotteet ja tulostaa "Lukuvinkki lisatty onnistuneesti"

Scenario: lukuvinkin lisays ei onnistu kun syotetaan epakelpo linkki
    Given tietokanta on alustettu
    When  valitaan komento "L" ja syotetaan linkki "oispa kaljaa" linkkia pyydettaessa
    Then  sovellus tulostaa "Lukuvinkin lisays ei onnistunut!"

Feature: Kayttaja voi lisata uuden lukuvinkin linkin ja tarkastella listan sisaltoa

Scenario: kayttaja voi lisata lukuvinkin linkin ja antaa sille otsikon
    Given tietokanta on alustettu
    When  linkki "www.is.fi" ja otsikko "lehti" ovat annettu
    Then  lukuvinkki on lisatty listalle
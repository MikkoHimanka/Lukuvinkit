Feature: Kayttajana voin lisata kirjan isbn-tunnuksen avulla

Scenario: lukuvinkin lisays onnistuu, kun annettu isbn-tunnus toimii ja lisays vahvistetaan
    Given tietokanta on alustettu
    Given testiapi on alustettu
    When yritetaan hakea kirja oikealla isbn-tunnuksella
    And vahvistetaan kirjan lisays
    Then sovellus tulostaa "Kirja lisattiin onnistuneesti!"

Scenario: lukuvinkin lisays ei onnistu, kun annettu isbn-tunnus toimii ja lisaysta ei vahvisteta vahvistetaan
    Given tietokanta on alustettu
    Given testiapi on alustettu
    When yritetaan hakea kirja oikealla isbn-tunnuksella
    And ei vahvisteta kirjan lisaysta
    Then sovellus ei tulosta "Kirja lisattiin onnistuneesti!"

Scenario: lukuvinkin lisays epaonnistuu, kun annettu tunnus ei toimi
    Given tietokanta on alustettu
    Given testiapi on alustettu niin etta se ei loyda kirjaa
    When yritetaan hakea kirja vaaralla isbn-tunnuksella
    Then sovellus tulostaa "Kirjan hakeminen epaonnistui!"
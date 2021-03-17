Feature: Kayttajana voin antaa vain toimivan linkin

Scenario: kayttaja voi lisata toimivan linkin
    Given tietokanta on alustettu
    When suoritetaan komennot:
        | L |
        | www.google.com |
        | google |
        |  x |
        |  x |
    And valitaan komento "N"
    Then  sovellus tulostaa "Linkki: www.google.com"

Scenario: kayttaja ei voi lisata vaaraa linkkia
    Given tietokanta on alustettu
    When suoritetaan komennot:
        | L |
        | asfewaef.asdfwe2323e |
        | google |
        |  x |
        |  x |
    Then  sovellus tulostaa "Lukuvinkin lisays ei onnistunut!"

Scenario: kayttajalle ilmoitetaan, jos internet-yhteys ei ole paalla
    Given tietokanta on alustettu
    And internet-yhteys on pois paalta
    When suoritetaan komennot:
        | L |
        | asfewaef.asdfwe2323e |
        | google |
        |  x |
        |  x |
    Then  sovellus tulostaa "Internet-yhteyden luonti epaonnistui: Linkin oikeellisuus epavarmaa."

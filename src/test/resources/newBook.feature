Feature: Käyttäjänä voin lisätä uuden lukuvinkin linkin ja otsikon

Scenario: lukuvinkin lisäys onnistuu kun syötetään toimiva linkki ja otsikko
    Given tietokanta on alustettu
    When  valitaan komento "L" ja syötetään linkki "www.is.fi" sekä otsikko "lehti" tietoja pyydettäessä
    Then  sovellus hyväksyy syötteet ja tulostaa "Lukuvinkki lisätty onnistuneesti"

Scenario: lukuvinkin lisäys onnistuu kun syötetään toimiva linkki ja tyhjä otsikko
    Given tietokanta on alustettu
    When  valitaan komento "L" ja syötetään linkki "www.is.fi" sekä otsikko "" tietoja pyydettäessä
    Then  sovellus hyväksyy syötteet ja tulostaa "Lukuvinkki lisätty onnistuneesti"

Scenario: lukuvinkin lisäys ei onnistu kun syötetään epäkelpo linkki
    Given tietokanta on alustettu
    When  valitaan komento "L" ja syötetään linkki "oispa kaljaa" linkkiä pyydettäessä
    Then  sovellus tulostaa "Lukuvinkin lisäys ei onnistunut!"

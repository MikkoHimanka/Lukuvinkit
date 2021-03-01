# Käyttöohje

## Ohjelman asennus

Ohjelman voi ottaa käyttöön lataamalla Githubin repositorion omalle tietokoneelleen.

## Ohjelman käynnistys

Ohjelma käynnistetään ajamalla komentorivillä komento:

```
gradle run
```
tai vaihtoehtoisesti komennolla 
```
gradle run -q --console=plain
```
jos haluat välttää Gradlen omat tulostukset

Ohjelman voi myös käynnistää ohjelmointiympäristössä, esimerkiksi Netbeans IDE:n suorittamana.

## Toiminnon valinta

Ohjelmaaa käytetään tekstikäyttöliittymän kautta. Käynnistyessään ohjelma listaa valittavassa olevat komennot. Komentoa vastaava kirjain on merkitty sulkuihin (kirjainkoolla ei ole merkitystä).

```
(L)isää uusi lukuvinkki
(N)äytä tallennetut lukuvinkit
(S)ulje sovellus
```

## Uuden lukuvinkin lisäys
```
L
```
Painamalla L käyttäjä voi tallentaa uuden lukuvinkin. Sovellus pyytää käyttäjää kirjoittamaan lukuvinkin linkin (pakollinen) ja otsikon.

## Lukuvinkkien näyttäminen
```
N
```
Painamalla N käyttäjä voi listata kaikki sovellukseen tallennetut lukuvinkit.

## Sovelluksen sulkeminen
```
S
```
Painamalla S sovellus sulkeutuu.







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

Ohjelmaaa käytetään tekstikäyttöliittymän kautta. Käynnistyessään ohjelma listaa valittavassa olevat komennot. Komentoa vastaava kirjain tai kirjainyhdistelmä on merkitty sulkuihin (kirjainkoolla ei ole merkitystä).

```
(L)isää uusi lukuvinkki
(N)äytä tallennetut lukuvinkit
(M)erkitse lukuvinkki luetuksi
(Li)staa lukemattomat lukuvinkit
(E)tsi lukuvinkkejä
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


## Lukuvinkin merkitseminen
```
M
```
Painamalla M käyttäjä voi merkitä lukuvinkin luetuksi linkin mukaan.


## Merkitsemättömien lukuvinkkien näyttäminen
```
Li
```
Painamalla Li käyttäjä voi listata kaikki sovellukseen tallennetut merkitsemättömät lukuvinkit.

## Lukuvinkkien etsiminen
````
E
````
Painamalla E käyttäjä voi hakea sovelluksesta otsikon perusteella lukuvinkkejä. Sovellus pyytää käyttäjää kirjoittamaan hakusanan, jonka avulla lukuvinkkejä haetaan.

## Lukuvinkin poistaminen
````
P
````
Painamalla P käyttäjä voi avata alinäkymän lukuvinkin poistamiselle. Alinäkymässä sovellus listaa ja numeroi lukuvinkit tai pyytää tarvittaessa tarkentamaan hakua. Painamalla V käyttäjä pääsee syöttämään poistettavan lukuvinkin numeron. Painamalla P käyttäjä voi siirtyä takaisin päävalikkoon poistamatta lukuvinkkiä.

## Lukuvinkin muokkaaminen
````
Mu
````
Kirjoittamalla Mu käyttäjä voi avata alinäkymän lukuvinkin muokkaamiselle. Alinäkymässä sovellus listaa ja numeroi lukuvinkit tai pyytää tarvittaessa tarkentamaan hakua. Painamalla V käyttäjä pääsee syöttämään muokattavan lukuvinkin numeron. Painamalla P käyttäjä voi siirtyä takaisin päävalikkoon muokkaamatta lukuvinkkiä. Lukuvinkin valinnan jälkeen käyttäjä voi muokata lukuvinkin linkkiä painamalla L, muokata otsikkoa painamalla O tai palata aikaisempaan näkymään (lukuvinkin valintaan) painamalla V.

## Ohjelman sulkeminen
```
S
```
Painamalla S sovellus sulkeutuu.







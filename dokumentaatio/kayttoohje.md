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
Komennot:
(L)isaa uusi lukuvinkki
(I)sbn-tunnuksen avulla lisaaminen
(N)ayta tallennetut lukuvinkit
(M)erkitse lukuvinkki luetuksi
(Li)staa lukemattomat lukuvinkit
(E)tsi lukuvinkkeja
(P)oista lukuvinkki
(Mu)okkaa lukuvinkkia
(S)ulje sovellus
```

## Uuden lukuvinkin lisäys
```
L
```
Painamalla `L` käyttäjä voi tallentaa uuden lukuvinkin. Sovellus pyytää käyttäjää asettamaan lukuvinkille linkin, otsikon, kuvauksen ja tageja. Näistä vain linkki on pakollinen, muut kentät voi jättää myös tyhjäksi. 

## Uuden lukuvinkin lisäys isbn-numeron perusteella
```
I
```
Painamalla `I` käyttäjä voi tallentaa uuden lukuvinkin isbn-numeron perusteella. **HUOM** Toiminnon käyttämiseen tarvitaan Googlen API-key. Ohjeet avaimen luomiseen löydät [täältä](https://developers.google.com/books/docs/v1/using#APIKey). Kun olet luonut avaimen, pitää luoda projektin juureen `.env`-tiedosto, joka sisältää yhden rivin:
```
API_KEY=<avain>
```

## Lukuvinkkien näyttäminen
```
N
```
Painamalla `N` käyttäjä voi listata kaikki sovellukseen tallennetut lukuvinkit.


## Lukuvinkin merkitseminen
```
M
```
Painamalla `M` käyttäjä voi merkitä lukuvinkin luetuksi linkin perusteella.


## Merkitsemättömien lukuvinkkien näyttäminen
```
Li
```
Kirjoittamalla `Li` käyttäjä voi listata kaikki sovellukseen tallennetut merkitsemättömät lukuvinkit.

## Lukuvinkkien etsiminen
````
E
````
Painamalla `E` käyttäjä voi hakea sovelluksesta lukuvinkkejä. Lukuvinkkejä
voi etsiä joko otsikon tai tagien perusteella. Hakukriteeri valitaan
painamalla `O` (otsikko) tai `T` (tagi). Käyttäjä voi myös palata päävalikkoon
painamalla `P`. Jos käyttäjä ei palannut päävalikkoon, voi haun suorittaa
syöttämällä ohjelmalle hakuparametrin arvon.

## Lukuvinkin poistaminen
````
P
````
Painamalla `P` käyttäjä voi avata alinäkymän lukuvinkin poistamiselle. Alinäkymässä sovellus listaa ja numeroi lukuvinkit tai pyytää tarvittaessa tarkentamaan hakua. Painamalla `V` käyttäjä pääsee syöttämään poistettavan lukuvinkin numeron. Painamalla `P` käyttäjä voi siirtyä takaisin päävalikkoon poistamatta lukuvinkkiä.

## Lukuvinkin muokkaaminen
````
Mu
````
Kirjoittamalla `Mu` käyttäjä voi avata alinäkymän lukuvinkin muokkaamiselle. Alinäkymässä sovellus listaa ja numeroi lukuvinkit tai pyytää tarvittaessa tarkentamaan hakua. Painamalla `V` käyttäjä pääsee syöttämään muokattavan lukuvinkin numeron. Painamalla `P` käyttäjä voi siirtyä takaisin päävalikkoon muokkaamatta lukuvinkkiä. Lukuvinkin valinnan jälkeen käyttäjä voi muokata lukuvinkin linkkiä painamalla `L`, muokata otsikkoa painamalla `O` tai palata aikaisempaan näkymään (lukuvinkin valintaan) painamalla `V`.

## Ohjelman sulkeminen
```
S
```
Painamalla `S` sovellus sulkeutuu.







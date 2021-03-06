# Loppuraportti
#### Opiskelijat
* Mikko Himanka
* Tuomas Aaltonen
* Leo Varis
* Kalle Luopajärvi
* Tapio Salonen


## Lähtöasetelma
Kellään ryhmämme jäsenistä ei ollut aikaisempaa kokemusta alan työtehtävistä tai varsinaisesta tiimityöskentelystä ohjelmistokehityksen parissa, joten lähdimme siltä osin miniprojektiin samalta viivalta. Teknisessä osaamisessa oli toki jonkin verran eroja opiskelijoiden välillä. Ryhmässämme oli myös python-projektiin orientoitunut opiskelija, mutta enemmistön päätöksellä päädyttiin Java-kieliseen toteutukseen.

Työskentelykulttuuria hahmoteltiin korona-aikakauden vaikuttamana niin, että daily scrum-palaverit pidettäisiin kaksi kertaa viikossa zoom-sovelluksen kautta. Zoomin lisäksi perustimme kommunikaatiota varten ryhmällemme Telegram-kanavan. Työskentelyajat olisivat jokaisen itsenäisesti päätettävissä &ndash; mielellään kuitenkin niin, että työ edistyisi tasaisesti sprintin aikana ohjeistuksen mukaisesti. Päätimme myös, että käyttäisimme versionhallinnassa vain pääkehityshaaraa.

## Sprint I
Pienen pohdinnan jälkeen lähdimme toteuttamaan tekstikäyttöliittymällistä "Minimun Viable Product"-tuotetta. Keskusteluissa oli myös web-käyttöliittymä mahdollisesti Springin avulla toteutettuna, mutta jätimme tuon optioksi seuraaviin sprintteihin jos aikaa jäisi. Projektin alustus konfiguraatioineen sujui kivuttomasti. Arkkitehtuurissa hyödynnettiin dao-mallia ja tietokannaksi valikoitui sqlite. Myös GitHub Actionsin tarjoama CI-ominaisuus saatiin nopeasti käyttöön.

Ensimmäisessä dailyssa huomasimme, että  olimme valinneet asiakastapaamisessa/suunnittelupalaverissa user storyja turhankin varovaisesti toteutettavaksi. MVP ja kaikki valitut user storyt yksikkötesteineen olivat jo kutakuinkin toteutettu, hyväksymistestauskin (Cucumber) oli jo työn alla. Koska työtunteja oli vielä rutkasti pankissa, poimimme vielä product backlogista muutaman user storyn lisää loppusprintille. Samalla päätimme vaihtaa käyttöliittymän kielen englannista suomeksi oletuksella, että suomenkielinen asiakas haluaa sovelluksen todennäköisemmin suomeksi. Tämä päätös aiheutti jatkossa harmia ääkkösten toimimattomuuden muodossa.

## Sprint II
Ensimmäisen sprintin tuloksiin ja työskentelytapoihin oltiin pääosin tyytyväisiä. Omassa definition of donessa määrittelemämme koodikatselmointi ei kuitenkaan toteutunut tai ainakaan ollut kovin järjestelmällistä. Ohjaajan vinkistä siirryimme trunk-pohjaisesta kehitysmallista feature branchien käyttöön toisen sprintin alussa. Tähän sprinttiin valitsimme jo asiakastapaamisessa enemmän ja hieman haastavampia user storyja, ja tekemistä olikin tarpeeksi. Kun jo kerran nujerretuksi uskomamme ääkkösongelmakin palasi hieman yllättäen takaisin, tuli sovelluksen julkaisukuntoon saamisessa kiire. Lopulta vaadittu release valmistui noin 15 minuuttia ennen asiakastapaamista. Testikattavuus jäi tällä sprintillä hieman tavoitteista.

## Sprint III
Viimeistään tässä vaiheessa luovuimme yhteisellä päätöksellä web-käyttöliittymään siirtymisestä. Sovellus oli paisunut sen verran isoksi, että refaktorointi olisi ollut erittäin työlästä ja Spring-sovelluskehyksen tietämyksemme oli yhtä opiskelijaa lukuunottamatta melko heikkoa. Sprintti sujui kokonaisuutena hyvin. Yksikkötestien kattavuus nostetiin tavoitetasojen yläpuolelle. Myös hyväksymistestaukseen kiinnitettiin huomiota ja jokainen opiskelija loi tässä sprintissä toteuttamaansa user storyynsa hyväksymistestit. 

## Projektityöskentely kokonaisuutena
Projektityöskentely toimi ilman isompia ongelmia koko kehitystyön ajan. Tapaamisiin tultiin tunnollisesti ja ajallaan. Taskien jako ja tekeminen hoitui pääsääntöisesti hyvin. Myös kommunikaatio pelasi ryhmän välillä riittävän hyvin, eikä suurempia katkoksia tai epäselvyyksiä syntynyt. Kaksi dailya per sprintti tuntui myös näin jälkikäteen tarkoituksenmukaiselta tahdilta tämän projektin tarpeisiin. Palaverien välillä ongelmia ratkottiin onnistuneesti Telegramissa.

Backlogia olisi voinut jälkeenpäin ajateltuna hyödyntää tehokkaammin. User storyt kyllä määriteltiin kattavasti ja taskit merkattiin sprintin alussa "varatuiksi" tehtävienjaon yhteydessä. Sprintin aikana backlogia ei kuitenkaan aina muistettu päivittää, vaikka task olisikin jo tehty. Lisäksi, vaikka ohjeistuksen mukaan ainakaan user storyja ei kokonaisuudessaan tarvinnutkaan estimoida, olisi oppimiskokemuksen kannalta voinut olla mielekästä estimoida ja päivittää taskeja hieman realistisemmin. Nyt taskien tuntipäivitys tapahtui usein niin, että tunnit lisättiin sprintin lopussa toteutuneiden työmäärien pohjalta &ndash; varsinaista estimointia ei ainakaan paperille juuri päätynyt. Ehkä jonkin verran epäselväksi jäi myös, mitä kaikkea sprintin taskeihin olisi ollut syytä kirjata.

Ryhmällämme oli aluksi negatiivisia mielikuvia versionhallinnan haarojen käyttöönotosta. Mielikuvat osoittautuivat kuitenkin lähinnä ennakkoluuloiksi. Feature branchit tekivät koodikatselmoinnista järjestelmällistä ja master-branch pysyi hajoamattomana. Vaikka ainakin pari opiskelijaa teki projektin aikana ensimmäisen pull requestinsa, työskentely branchien kanssa tuntui nopeasti luontevalta, eikä isompia ongelmia syntynyt &ndash; emme olleetkaan Google. Sen yhden isomman konfliktin ratkojalla voi toki olla aiheesta hieman eriävä mielipide.

## Tekninen aspekti
Teknisellä puolella projekti eteni koko projektin ajan suunnitellusti, pääsääntöisesti ilman suurempia ongelmia. Luonnollisesti vastaan tuli muutama pieni bugi, jotka saatiin kuitenkin yleensä nopeasti ratkaistua. Ainoa pidempikestoinen ongelma oli ääkkösten käyttäytyminen tulostuksissa. Ryhmämme opiskelijat käyttivät työskentelyyn erilaisia kokoonpanoja ja vaikka ongelma poistui Linuxin käyttäjiltä, ainakin yhdellä Windows-käyttäjällä ääkköset eivät suostuneet näyttäytymään missään vaiheessa toivotulla tavalla. 

Koodin laadun (luettavuuden) tarkkailuun emme käyttäneet mitään automaattista työkalua (esim. Checkstyle), mutta pyrimme pitämään koodin suhteellisen luettavana ja hyvien käytänteiden mukaisena koko projektin ajan. Subjektiivisen arvioinnin mukaan onnistuimme tässä vähintään kohtalaisesti. Sovelluksen koon kasvaessa metodeja ja luokkia tarvittaessa pilkottiin, eikä esimerkiksi copy paste-koodia jäänyt sovellukseen ylitsevuotavan paljon. Ehkä seuraavassa/isommassa projektissa tätäkin voisi automatisoida.

## Yhteenveto
Projekti oli siis pääosin hyvin onnistunut sekä projektityöskentelyn että teknisen puolen osalta. Oppimiskokemuksena projekti oli varmasti enemmän kuin yhden opintopisteen arvoinen, eikä mikään sen osa-alue tuntunut varsinaisesti turhalta.



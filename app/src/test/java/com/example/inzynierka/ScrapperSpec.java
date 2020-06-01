package com.example.inzynierka;

import com.example.inzynierka.informations.citiesInformations.WikipediaScrapper;
import com.example.inzynierka.informations.descriptionViews.detailsViews.DescriptionEntity;
import com.example.inzynierka.informations.descriptionViews.detailsViews.DetailsScrapper;
import com.example.inzynierka.informations.listOfInformations.Scrapper;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static junit.framework.TestCase.assertNotNull;
import static junit.framework.TestCase.assertTrue;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;


public class ScrapperSpec {
    Scrapper scrapper;
    DetailsScrapper muzeumScrapper;
    DetailsScrapper detailsScrapper;
    WikipediaScrapper wikipediaScrapper;
    @BeforeEach
    void init(){
        scrapper = new Scrapper();
        scrapper.openDocument();
        detailsScrapper = new DetailsScrapper("https://historia.org.pl/2020/01/26/narodowy-dzien-pamieci-zolnierzy-wykletych-2020-program/");
        muzeumScrapper = new DetailsScrapper("https://historia.org.pl/2020/01/04/noc-muzeow-w-krakowie-2020-zobacz-tegoroczny-program/");
        wikipediaScrapper = new WikipediaScrapper("https://pl.wikipedia.org/wiki/Wroc%C5%82aw");
    }

    @Test
    public void isConnectionOk(){
        Scrapper scrapperTest = new Scrapper();
        scrapperTest.openDocument();
        assertNotNull(scrapperTest);
    }
    @Test
    public void isJanuaryScrapperInHeadings(){
        List<String> results = scrapper.getMonths();
        assertTrue(results.contains("Styczeń"));
    }
    @Test
    public void isScrappingOk(){
        DescriptionEntity entity = detailsScrapper.scrap();
        String result = entity.getFirstParagraph();
        String expected = "W dniu 1 marca 2020 r. obchodzony będzie Narodowy Dzień Pamięci Żołnierzy Wyklętych.";
        assertThat("Zle pobrano pierwszy paragraf", result, is(expected));
    }
    @Test
    public void areParagraphsOk(){
        DescriptionEntity entity = detailsScrapper.scrap();
        List<String> result = entity.getParagraphs();
        String expected = "W dniu 1 marca 2020 r. obchodzony będzie Narodowy Dzień Pamięci Żołnierzy Wyklętych.";
        String exptected2 =  "Narodowy Dzień Pamięci żołnierzy Wyklętych został ustanowiony w hołdzie „Żołnierzom Wyklętym” — "+
                        "bohaterom antykomunistycznego podziemia, którzy w obronie niepodległego bytu Państwa " +
                        "Polskiego, walcząc o prawo do samostanowienia i urzeczywistnienie dążeń demokratycznych " +
                        "społeczeństwa polskiego, z bronią w ręku, jak i w inny sposób, przeciwstawili się sowieckiej " +
                        "agresji i narzuconemu siłą reżimowi komunistycznemu.";
        String expected3 =
        "Zobacz więcej ciekawych wydarzeń historycznych w naszym kalendarzu, w którym zebraliśmy " +
                "mnóstwo informacji o terminach tegorocznych: rekonstrukcji, turniejów, pokazów, " +
                "festiwali, zlotów i innych imprez historycznych. Tam też znajduje się udostępniona mapa, " +
                "na której zaznaczyliśmy lokalizacje wszystkich zebranych przez nas wydarzeń. Dzięki " +
                "mapie łatwo znajdziesz ciekawe wydarzenie historyczne w Twojej okolicy.";
        String notExptected = "Zobacz więcej ciekawych wydarzeń historycznych w naszym kalendarzu, w którym zebraliśmy mnóstwo informacji o " +
                "terminach tegorocznych: rekonstrukcji, turniejów, pokazów, festiwali, zlotów i innych imprez historycznych. " +
                "Tam też znajduje się udostępniona mapa, na której zaznaczyliśmy lokalizacje wszystkich zebranych przez " +
                "nas wydarzeń. Dzięki mapie łatwo znajdziesz ciekawe wydarzenie historyczne w Twojej okolicy.";

        assertTrue("Zle pobrano paragrafy", result.contains(expected) && result.contains(exptected2)
                && result.contains(expected3));
        assertTrue("Pobrano za duzo paragrafów ", !result.contains(notExptected));

    }
    @Test
    public void areParagraphsNocMuzeumOk(){
        DescriptionEntity entity = muzeumScrapper.scrap();
        String expected = "Pierwsza Noc Muzeów odbyła się w Berlinie w 1997. Ze względu na sukces tej imprezy kolejne europejskie miasta zaczęły również organizować podobne przedsięwzięcia kulturalne. W Polsce pierwszą Noc Muzeów zorganizowano w 2003 r. w Muzeum Narodowym w Poznaniu. Następnego roku Noc Muzeów została zorganizowana także w Krakowie i w Warszawie. Obecnie Noc Muzeów jest corocznie organizowana przez kilkadziesiąt polskich miast.";
        String expected2 = "Serdecznie zachęcamy do udziału!";
        String notExptected = "Adres e-mail (nie zostanie opublikowany) (wymagane)";

        List<String> result = entity.getParagraphs();

        assertTrue("Pobrano za duzo paragrafów ", !result.contains(notExptected));
        assertTrue("Zle pobrano paragrafy", result.contains(expected) && result.contains(expected2));
    }
    @Test
    public void areBoldedParagraphsOk(){
        DetailsScrapper newDetailsScrapper = new DetailsScrapper("https://historia.org.pl/2020/02/23/konny-turniej-rycerski-na-zamku-w-ilzy-2020/");
        DescriptionEntity entity = newDetailsScrapper.scrap();
        String expected = "Miejsce:";
        String expected2 = "Szczegóły:";
        List<String> result = entity.getBoldedParagraphs();
        assertTrue("Zle pobrano paragrafy", result.contains(expected) && result.contains(expected2));
    }
    @Test
    public void areBoldedParagraphsFromMuzeumOk(){
        DescriptionEntity entity = muzeumScrapper.scrap();
        String expected = "Podstawowe informacje";
        String expected2 = "Komunikacja miejska podczas Nocy Muzeów w Krakowie 2020:";
        List<String> result = entity.getBoldedParagraphs();
        assertTrue("Zle pobrano paragrafy", result.contains(expected) && result.contains(expected2));
    }

    @Test
    public void areHeadingsOk(){
        String expected = "Program";
        String expected2 = "Narodowy Dzień Pamięci Żołnierzy Wyklętych";
        String notExpected = "Kalendarz i mapa wydarzeń historycznych";
        DescriptionEntity entity = detailsScrapper.scrap();
        List<String> result = entity.getHeadings();
        assertTrue("Zle pobrano nagłówki", result.contains(expected) && result.contains(expected2));
    }

    @Test
    public void wikipediaScrapper(){
        wikipediaScrapper.scrap();
    }
}

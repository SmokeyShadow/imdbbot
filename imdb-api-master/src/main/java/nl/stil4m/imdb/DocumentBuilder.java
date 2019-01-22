package nl.stil4m.imdb;

import nl.stil4m.imdb.commands.Command;
import nl.stil4m.imdb.constants.IMDBConstants;

import org.jsoup.Jsoup;

import java.io.IOException;

public class DocumentBuilder {

    public org.jsoup.nodes.Document buildDocument(Command command) throws IOException {
        String string = IMDBConstants.ROOT_URL + command.getUrlExtension();
        return Jsoup.connect(string).timeout(30000).get();
    }

}

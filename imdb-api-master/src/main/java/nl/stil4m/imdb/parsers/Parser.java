package nl.stil4m.imdb.parsers;
import org.jsoup.nodes.Element;

public interface Parser<T> {

    T parse(Element document) throws Exception;

}
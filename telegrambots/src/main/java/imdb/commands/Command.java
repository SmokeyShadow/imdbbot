package imdb.commands;

import java.io.UnsupportedEncodingException;

public interface Command {

    String getUrlExtension() throws UnsupportedEncodingException;

}
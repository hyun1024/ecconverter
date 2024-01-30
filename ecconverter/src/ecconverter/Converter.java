package ecconverter;

import java.io.IOException;

public interface Converter {

	public void convert(String type, String filename) throws IOException;
}

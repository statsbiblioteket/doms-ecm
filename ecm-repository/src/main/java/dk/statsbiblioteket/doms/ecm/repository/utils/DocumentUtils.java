package dk.statsbiblioteket.doms.ecm.repository.utils;

import org.w3c.dom.Document;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringWriter;


/**
 * Utility methods for working with w3c DOM Documents.
 */
public class DocumentUtils {


    public static DocumentBuilder getDOCUMENT_BUILDER(){
        try {
            DocumentBuilderFactory documentBuilderFactory
                    = DocumentBuilderFactory
                    .newInstance();
            documentBuilderFactory.setNamespaceAware(true);
            return documentBuilderFactory.newDocumentBuilder();
        } catch (ParserConfigurationException e) {
            throw new Error("Error initialising default document builder", e);
        }
    }

    public static Transformer getDOCUMENT_TRANSFORMER() {
        try {
            return TransformerFactory.newInstance().newTransformer();
        } catch (TransformerConfigurationException e) {
            throw new Error("Error initialising default document transformer",
                            e);
        }
    }

    /**
     *  Simple method to dump a Document object as a xml string. Be aware that the
     *  string starts with <?xml ...
     * @param doc the document to convert
     * @return The document as a xml
     * @throws TransformerException If the document could not be transformed to
     * xml
     */
    public static String documentToString(Document doc)
            throws TransformerException {

        StringWriter writer = new StringWriter();
        getDOCUMENT_TRANSFORMER().transform(new DOMSource(doc), new StreamResult(writer) );
        return writer.toString();
    }

    /**
     * Simple method to parse a string into a w3c Document
     * @param doc the string to parse
     * @return the document
     * @throws SAXException if the string did not contain valid html.
     */
    public static Document stringToDocument(String doc)
            throws SAXException {
        InputStream in = new ByteArrayInputStream(doc.getBytes());
        try {
            return getDOCUMENT_BUILDER().parse(in);
        } catch (IOException e) {
            throw new Error("Problem reading a string, should never happen",e);
        }
    }

}

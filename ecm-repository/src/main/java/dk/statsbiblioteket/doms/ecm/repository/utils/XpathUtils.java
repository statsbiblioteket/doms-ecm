package dk.statsbiblioteket.doms.ecm.repository.utils;

import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;
import javax.xml.XMLConstants;
import javax.xml.namespace.NamespaceContext;
import java.util.Map;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Collections;

/**
 * Utilities for performing Xpath operations in the ECM context. Already defines
 * all the relevant namespaces, so proper shorthands can be used.
 */
public class XpathUtils {


    private static final String[][] NAMESPACE_TABLE
            = {{XMLConstants.XML_NS_PREFIX, XMLConstants.XML_NS_URI},
            {XMLConstants.XMLNS_ATTRIBUTE, XMLConstants.XMLNS_ATTRIBUTE_NS_URI},
            {XMLConstants.DEFAULT_NS_PREFIX, XMLConstants.NULL_NS_URI},
            {"rdf", Constants.NAMESPACE_RDF},
            {"rdfs", Constants.NAMESPACE_RDFS},
            {"owl", Constants.NAMESPACE_OWL},
            {"xsi", Constants.NAMESPACE_XML_SCHEMA_INSTANCE},
            {"dc", Constants.NAMESPACE_DC},
            {"dcterms", Constants.NAMESPACE_DCTERMS},
            {"oai", Constants.NAMESPACE_OAI},
            {"oai_dc", Constants.NAMESPACE_OAIDC},
            {"view", Constants.NAMESPACE_VIEW},
            {"fedora-model", Constants.NAMESPACE_FEDORA_MODEL},
            {"foxml", Constants.NAMESPACE_FOXML},
            {"ds", Constants.NAMESPACE_DS_COMPOSITE},
            {"doms", Constants.NAMESPACE_RELATIONS},//TODO, this should be deprecated
            {"ecm", Constants.NAMESPACE_RELATIONS},//To shorthands for same namespace
            {"schema", Constants.NAMESPACE_SCHEMA},
            {"dobundle", Constants.NAMESPACE_DIGITAL_OBJECT_BUNDLE}
    };

    private static final NamespaceContext ECM_NAMESPACE_CONTEXT
            = new NamespaceContext() {
        Map<String, String> nsPrefixMap = new HashMap<String, String>(NAMESPACE_TABLE.length);
        Map<String, String> inverseNsPrefixMap = new HashMap<String, String>(NAMESPACE_TABLE.length);
        {
            for (String[] pair : NAMESPACE_TABLE) {
                nsPrefixMap.put(pair[0], pair[1]);
                inverseNsPrefixMap.put(pair[0], pair[1]);
            }
        }

        public String getNamespaceURI(String prefix) {
            if (prefix == null) {
                throw new IllegalArgumentException("Prefix is null");
            }
            String uri = nsPrefixMap.get(prefix);
            if (uri != null) {
                return uri;
            } else {
                return XMLConstants.NULL_NS_URI;
            }
        }

        public String getPrefix(String namespaceURI) {
            if (namespaceURI == null) {
                throw new IllegalArgumentException(
                        "namespaceURI is null");
            }
            return inverseNsPrefixMap.get(namespaceURI);
        }

        public Iterator getPrefixes(String namespaceURI) {
            if (namespaceURI == null) {
                throw new IllegalArgumentException(
                        "namespaceURI is null");
            }
            String prefix = getPrefix(namespaceURI);
            if (prefix == null) {
                return Collections.emptyList().iterator();
            } else {
                return Collections.singletonList(prefix)
                        .iterator();
            }
        }
    };

    /**
     * Helper method for doing an XPath query using ECM namespaces.
     *
     * @param node            The node to start XPath query on.
     * @param xpathExpression The XPath expression, using default DOMS
     *                        namespace prefixes.
     * @return The result, as a node list.
     *
     * @throws XPathExpressionException On trouble parsing or evaluating the
     *                                  expression.
     */
    public static NodeList xpathQuery(Node node, String xpathExpression)
            throws XPathExpressionException {
        XPath xPath = XPathFactory.newInstance().newXPath();
        xPath.setNamespaceContext(ECM_NAMESPACE_CONTEXT);

        return (NodeList) xPath
                .evaluate(xpathExpression, node, XPathConstants.NODESET);
    }

      /**
     * Helper method for doing an XPath query using ECM namespaces.
     *
     * @param node            The node to start XPath query on.
     * @param xpathExpression The XPath expression, using default DOMS
     *                        namespace prefixes.
     * @return The result, as a node
     *
     * @throws XPathExpressionException On trouble parsing or evaluating the
     *                                  expression.
     */
    public static Node xpathQuerySingle(Node node, String xpathExpression)
            throws XPathExpressionException {
        XPath xPath = XPathFactory.newInstance().newXPath();
        xPath.setNamespaceContext(ECM_NAMESPACE_CONTEXT);

        return (Node) xPath
                .evaluate(xpathExpression, node, XPathConstants.NODE);
    }
}

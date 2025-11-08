package ee.ut.cs.dsg.confcheck;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Simple XES reader that avoids JAXB dependencies
 */
public class SimpleXESReader {
    
    public static class SimpleTrace {
        public List<String> events = new ArrayList<>();
        public String traceId;
    }
    
    public static class SimpleLog {
        public List<SimpleTrace> traces = new ArrayList<>();
    }
    
    public static SimpleLog parseXES(String filename) throws Exception {
        File file = new File(filename);
        if (!file.exists()) {
            throw new RuntimeException("File not found: " + filename);
        }
        
        SAXParserFactory factory = SAXParserFactory.newInstance();
        SAXParser saxParser = factory.newSAXParser();
        
        SimpleXESHandler handler = new SimpleXESHandler();
        saxParser.parse(file, handler);
        
        return handler.getLog();
    }
    
    private static class SimpleXESHandler extends DefaultHandler {
        private SimpleLog log = new SimpleLog();
        private SimpleTrace currentTrace = null;
        private boolean inEvent = false;
        private String currentEventName = null;
        private boolean inTrace = false;
        
        @Override
        public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
            if (qName.equals("trace")) {
                currentTrace = new SimpleTrace();
                inTrace = true;
            } else if (qName.equals("event")) {
                inEvent = true;
                currentEventName = null;
            } else if (qName.equals("string") && attributes.getValue("key") != null) {
                String key = attributes.getValue("key");
                String value = attributes.getValue("value");
                
                if (key.equals("concept:name")) {
                    if (inEvent) {
                        currentEventName = value;
                    } else if (inTrace && currentTrace != null) {
                        currentTrace.traceId = value;
                    }
                }
            }
        }
        
        @Override
        public void endElement(String uri, String localName, String qName) throws SAXException {
            if (qName.equals("trace")) {
                if (currentTrace != null && !currentTrace.events.isEmpty()) {
                    log.traces.add(currentTrace);
                }
                currentTrace = null;
                inTrace = false;
            } else if (qName.equals("event")) {
                if (currentEventName != null && currentTrace != null) {
                    currentTrace.events.add(currentEventName);
                }
                inEvent = false;
                currentEventName = null;
            }
        }
        
        public SimpleLog getLog() {
            return log;
        }
    }
}


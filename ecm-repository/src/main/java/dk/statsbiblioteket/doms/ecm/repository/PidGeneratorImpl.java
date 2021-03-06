package dk.statsbiblioteket.doms.ecm.repository;

import dk.statsbiblioteket.doms.ecm.repository.exceptions.PIDGeneratorException;
import dk.statsbiblioteket.doms.pidgenerator.CommunicationException;
import dk.statsbiblioteket.doms.pidgenerator.PidGeneratorSoapWebservice;
import dk.statsbiblioteket.doms.pidgenerator.PidGeneratorSoapWebserviceService;
import dk.statsbiblioteket.doms.webservices.configuration.ConfigCollection;

import javax.xml.namespace.QName;
import java.net.MalformedURLException;
import java.net.URL;

/**
 */
public class PidGeneratorImpl extends PidGenerator {

    private PidGeneratorSoapWebservice pidgen;


    public PidGeneratorImpl() throws PIDGeneratorException {

        String WSDLLOCATION_STRING
                = ConfigCollection.getProperties().getProperty(
                "dk.statsbiblioteket.doms.ecm.pidgenerator.client.wsdllocation");

        URL WSDLLOCATION = null;
        try {
            WSDLLOCATION = new URL(WSDLLOCATION_STRING);
        } catch (MalformedURLException e) {
            throw new PIDGeneratorException("Failed to parse the location of the pidgenerator service",e);
        }

        PidGeneratorSoapWebserviceService service
                = new PidGeneratorSoapWebserviceService(WSDLLOCATION,
                                                        new QName(
                                                                "http://pidgenerator.doms.statsbiblioteket.dk/",
                                                                "PidGeneratorSoapWebserviceService"));
        pidgen = service.getPort(PidGeneratorSoapWebservice.class);
    }

    /**
     * Generate the next available PID.
     *
     * @param infix A string, all or part of which may be used as part of the
     * PID, but with no guarantee. May be left empty.
     * @return The next available (unique) PID, possibly including (part of) the
     * requested infix.
     */
    public String generateNextAvailablePID(String infix)
            throws PIDGeneratorException {

        try {
            return pidgen.generatePidWithInfix(infix);
        } catch (CommunicationException e) {
            throw new PIDGeneratorException("Encountered a communication problem with the pidgenerator webservice",e);
        }
    }



}

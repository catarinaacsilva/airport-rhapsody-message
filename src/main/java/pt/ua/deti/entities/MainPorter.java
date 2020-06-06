package pt.ua.deti.entities;

import java.io.IOException;
import java.util.Properties;

import pt.ua.deti.common.Utils;
import pt.ua.deti.shared.remote.ALRemote;
import pt.ua.deti.shared.remote.BCPRemote;
import pt.ua.deti.shared.remote.GRIRemote;
import pt.ua.deti.shared.remote.PHRemote;
import pt.ua.deti.shared.remote.TSARemote;
import pt.ua.deti.shared.stubs.ALInterface;
import pt.ua.deti.shared.stubs.BCPInterface;
import pt.ua.deti.shared.stubs.GRIInterface;
import pt.ua.deti.shared.stubs.PHInterface;
import pt.ua.deti.shared.stubs.TSAInterface;

/**
 * Main execution program for {@link Porter}.
 * 
 * @author Catarina Silva
 * @author Duarte Dias
 * @version 1.0
 */
public class MainPorter {

    /**
     * Main class, lets make the constructor private.
     */
    private MainPorter() {
    }

    public static void main(final String[] args) throws IOException {
        // Read the configuration file
        final Properties prop = Utils.loadProperties("config.properties");

        // AL Remote
        final String al_host = prop.getProperty("al_host");
        final int al_port = Integer.parseInt(prop.getProperty("al_port"));

        // PH Remote
        final String ph_host = prop.getProperty("ph_host");
        final int ph_port = Integer.parseInt(prop.getProperty("ph_port"));

        // BCP Remote
        final String bcp_host = prop.getProperty("bcp_host");
        final int bcp_port = Integer.parseInt(prop.getProperty("bcp_port"));

        // TSA Remote
        final String tsa_host = prop.getProperty("tsa_host");
        final int tsa_port = Integer.parseInt(prop.getProperty("tsa_port"));

        // GRI Remote
        final String gri_host = prop.getProperty("gri_host");
        final int gri_port = Integer.parseInt(prop.getProperty("gri_port"));

        // Create the Information Sharing Regions
        final ALInterface al = new ALRemote(al_host, al_port);
        final PHInterface ph = new PHRemote(ph_host, ph_port);
        final BCPInterface bcp = new BCPRemote(bcp_host, bcp_port);
        final TSAInterface tsa = new TSARemote(tsa_host, tsa_port);
        final GRIInterface gri = new GRIRemote(gri_host, gri_port);

        // Start porter
        final Porter porter = new Porter(al, ph, bcp, tsa, gri);
        final Thread tporter = new Thread(porter);
        tporter.start();

        // Wait
        try {
            tporter.join();
        } catch (final InterruptedException e) {
            e.printStackTrace();
        }
    }
}
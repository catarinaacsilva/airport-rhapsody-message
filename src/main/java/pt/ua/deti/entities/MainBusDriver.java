package pt.ua.deti.entities;

import java.io.IOException;
import java.util.Properties;

import pt.ua.deti.common.Utils;
import pt.ua.deti.shared.remote.ATERemote;
import pt.ua.deti.shared.remote.ATTQRemote;
import pt.ua.deti.shared.remote.DTERemote;
import pt.ua.deti.shared.remote.DTTQRemote;
import pt.ua.deti.shared.remote.GRIRemote;
import pt.ua.deti.shared.stubs.ATEInterface;
import pt.ua.deti.shared.stubs.ATTQInterface;
import pt.ua.deti.shared.stubs.DTEInterface;
import pt.ua.deti.shared.stubs.DTTQInterface;
import pt.ua.deti.shared.stubs.GRIInterface;

/**
 * Main execution program for {@link BusDriver}.
 * 
 * @author Catarina Silva
 * @author Duarte Dias
 * @version 1.0
 */
public class MainBusDriver {

    /**
     * Main class, lets make the constructor private.
     */
    private MainBusDriver() {
    }

    public static void main(final String[] args) throws IOException {
        // Read the configuration file
        final Properties prop = Utils.loadProperties("config.properties");

        // ATTQ Remote
        final String attq_host = prop.getProperty("attq_host");
        final int attq_port = Integer.parseInt(prop.getProperty("attq_port"));

        // DTTQ Remote
        final String dttq_host = prop.getProperty("dttq_host");
        final int dttq_port = Integer.parseInt(prop.getProperty("dttq_port"));

        // ATE Remote
        final String ate_host = prop.getProperty("ate_host");
        final int ate_port = Integer.parseInt(prop.getProperty("ate_port"));

        // DTE Remote
        final String dte_host = prop.getProperty("dte_host");
        final int dte_port = Integer.parseInt(prop.getProperty("dte_port"));

        // GRI Remote
        final String gri_host = prop.getProperty("gri_host");
        final int gri_port = Integer.parseInt(prop.getProperty("gri_port"));

        // Create the Information Sharing Regions
        final ATTQInterface attq = new ATTQRemote(attq_host, attq_port);
        final DTTQInterface dttq = new DTTQRemote(dttq_host, dttq_port);
        final ATEInterface ate = new ATERemote(ate_host, ate_port);
        final DTEInterface dte = new DTERemote(dte_host, dte_port);
        final GRIInterface gri = new GRIRemote(gri_host, gri_port);
        
        // Start bus driver life cycle
        final BusDriver busDriver = new BusDriver(attq, dttq, ate, dte, gri);
        final Thread tbusdriver = new Thread(busDriver);
        tbusdriver.start();

        // Wait
        try {
            tbusdriver.join();
        } catch (final InterruptedException e) {
            e.printStackTrace();
        }
    }
}
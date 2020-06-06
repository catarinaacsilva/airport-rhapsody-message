package pt.ua.deti.entities;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.concurrent.ThreadLocalRandom;

import pt.ua.deti.common.Bag;
import pt.ua.deti.common.Plane;
import pt.ua.deti.common.Utils;
import pt.ua.deti.shared.imp.ArrivalTerminalExit;
import pt.ua.deti.shared.imp.ArrivalTerminalTransferQuay;
import pt.ua.deti.shared.imp.BaggageReclaimOffice;
import pt.ua.deti.shared.imp.DepartureTerminalEntrance;
import pt.ua.deti.shared.remote.ALRemote;
import pt.ua.deti.shared.remote.ATERemote;
import pt.ua.deti.shared.remote.ATTQRemote;
import pt.ua.deti.shared.remote.BCPRemote;
import pt.ua.deti.shared.remote.BRORemote;
import pt.ua.deti.shared.remote.DTERemote;
import pt.ua.deti.shared.remote.DTTQRemote;
import pt.ua.deti.shared.remote.GRIRemote;
import pt.ua.deti.shared.remote.PHRemote;
import pt.ua.deti.shared.stubs.ALInterface;
import pt.ua.deti.shared.stubs.ATEInterface;
import pt.ua.deti.shared.stubs.ATTQInterface;
import pt.ua.deti.shared.stubs.BCPInterface;
import pt.ua.deti.shared.stubs.BROInterface;
import pt.ua.deti.shared.stubs.DTEInterface;
import pt.ua.deti.shared.stubs.DTTQInterface;
import pt.ua.deti.shared.stubs.GRIInterface;
import pt.ua.deti.shared.stubs.PHInterface;

/**
 * Main execution program.
 * 
 * @author Catarina Silva
 * @author Duarte Dias
 * @version 1.0
 */
public class MainPassenger {
    public static void main(final String[] args) throws IOException {
        // Read the configuration file
        final Properties prop = Utils.loadProperties("config.properties");

        // The number of Planes
        final int K = Integer.parseInt(prop.getProperty("K"));
        // The number of Passengers per Plane
        final int N = Integer.parseInt(prop.getProperty("N"));
        // Maximum number of luggage per Passenger
        final int M = Integer.parseInt(prop.getProperty("M"));
        // The probability of losing a piece of luggage
        final double P = Double.parseDouble(prop.getProperty("P"));

        // GRI Remote
        final String gri_host = prop.getProperty("gri_host");
        final int gri_port = Integer.parseInt(prop.getProperty("gri_port"));

        // PH Remote
        final String ph_host = prop.getProperty("ph_host");
        final int ph_port = Integer.parseInt(prop.getProperty("ph_port"));

        // DTTQ Remote
        final String dttq_host = prop.getProperty("dttq_host");
        final int dttq_port = Integer.parseInt(prop.getProperty("dttq_port"));

        // ATTQ Remote
        final String attq_host = prop.getProperty("attq_host");
        final int attq_port = Integer.parseInt(prop.getProperty("attq_port"));

        // AL Remote
        final String al_host = prop.getProperty("al_host");
        final int al_port = Integer.parseInt(prop.getProperty("al_port"));

        // BCP Remote
        final String bcp_host = prop.getProperty("bcp_host");
        final int bcp_port = Integer.parseInt(prop.getProperty("bcp_port"));

        // BRO Remote
        final String bro_host = prop.getProperty("bro_host");
        final int bro_port = Integer.parseInt(prop.getProperty("bro_port"));

        // ATE Remote
        final String ate_host = prop.getProperty("ate_host");
        final int ate_port = Integer.parseInt(prop.getProperty("ate_port"));

        // DTE Remote
        final String dte_host = prop.getProperty("dte_host");
        final int dte_port = Integer.parseInt(prop.getProperty("dte_port"));

        // Create the Information Sharing Regions
        // Remote
        final GRIInterface gri = new GRIRemote(gri_host, gri_port);
        final PHInterface ph = new PHRemote(ph_host, ph_port);
        
        final DTTQInterface dttq = new DTTQRemote(dttq_host, dttq_port);
        final ATTQInterface attq = new ATTQRemote(attq_host, attq_port);
        final ALInterface al = new ALRemote(al_host, al_port);
        final BCPInterface bcp = new BCPRemote(bcp_host, bcp_port);
        final BROInterface bro = new BRORemote(bro_host, bro_port);
        final ATEInterface ate = new ATERemote(ate_host, ate_port);
        final DTEInterface dte = new DTERemote(dte_host, dte_port);

        // Create the list of planes
        final List<Plane> planes = createPlanes(K, N, M, P, gri, al, bcp, bro, ate, dte, attq, dttq);

        // Get a list of planes
        for (int i = 0; i < planes.size(); i++) {
            Plane plane = planes.get(i);

            // Update the plane information
            gri.updatePlane(plane.fn(), plane.bn());
            gri.updateBags(plane.bn());
            // Reset the necessary shared location to be ready for a new set of passengers
            al.reset();
            bcp.reset();
            ate.reset((i + 1) == planes.size());
            dte.reset();
            // Load the Plane Hold
            boolean lastPlane = (i + 1) == planes.size();

            ph.loadBags(plane.getBags(), lastPlane);
            // Star the passengers of the plane
            final List<Passenger> passengers = plane.getPassengers();
            final List<Thread> tpassengers = new ArrayList<>(passengers.size());
            for (final Passenger p : passengers) {
                final Thread t = new Thread(p);
                tpassengers.add(t);
                t.start();
            }

            // Wait for the passengers to finnish them lifecycle
            try {
                for (final Thread t : tpassengers) {
                    t.join();
                }
            } catch (final InterruptedException e) {
                e.printStackTrace();
            }
        }

        try {
            ph.close();
            dte.close();
            ate.close();
            al.close();
            bcp.close();
            bro.close();
            dttq.close();
            attq.close();
            gri.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Returns a {@link List} of {@link pt.ua.deti.common.Plane}s for the
     * simulation.
     * 
     * @param K    the number of {@link pt.ua.deti.common.Plane}
     * @param N    the number of {@link pt.ua.deti.entities.Passenger} per
     *             {@link pt.ua.deti.common.Plane}
     * @param M    the maximum number of {@link pt.ua.deti.common.Bag} per
     *             {@link pt.ua.deti.entities.Passenger}
     * @param P    the probability of losing a bag in the trip
     * @param gri  {@link GeneralRepositoryInformation}
     * @param al   {@link ALInterface}
     * @param bcp  {@link BCPInterface}
     * @param bro  {@link BaggageReclaimOffice}
     * @param ate  {@link ArrivalTerminalExit}
     * @param dte  {@link DepartureTerminalEntrance}
     * @param attq {@link ArrivalTerminalTransferQuay}
     * @param dttq {@link DepartureTerminalTransferQuay}
     * @return a {@link List} of {@link pt.ua.deti.common.Plane}s for the simulation
     */
    private static List<Plane> createPlanes(final int K, final int N, final int M, final double P,
            final GRIInterface gri, final ALInterface al, final BCPInterface bcp, final BROInterface bro,
            final ATEInterface ate, final DTEInterface dte, final ATTQInterface attq, final DTTQInterface dttq) {
        final ArrayList<Plane> planes = new ArrayList<>(K);
        int globalBagId = 0;

        for (int i = 0; i < K; i++) {
            List<Passenger> passengers = new ArrayList<>();
            List<Bag> bags = new ArrayList<>();

            for (int j = 0; j < N; j++) {
                List<Integer> bagIds = new ArrayList<>();
                int numberBags = ThreadLocalRandom.current().nextInt(0, M + 1);
                boolean transit = getRandomBoolean(.5);
                for (int k = 0; k < numberBags; k++) {

                    bagIds.add(globalBagId);
                    if (getRandomBoolean(1.0 - P)) {
                        bags.add(new Bag(globalBagId, transit));
                    }
                    globalBagId++;
                }

                Passenger passenger = new Passenger((j + 1), bagIds, transit, al, bcp, bro, ate, dte, attq, dttq, gri);
                passengers.add(passenger);
            }

            Plane plane = new Plane((i + 1), passengers, bags);
            planes.add(plane);
        }

        return planes;
    }

    /**
     * Returns true if the random value is less than the probability; otherwise
     * false.
     * 
     * @param probability the probability of returning True
     * @return true if the random value is less than the probability; otherwise
     *         false
     */
    private static boolean getRandomBoolean(double probability) {
        return ThreadLocalRandom.current().nextDouble() <= probability;
    }
}
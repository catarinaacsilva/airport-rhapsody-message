package pt.ua.deti.main;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.concurrent.ThreadLocalRandom;

import pt.ua.deti.common.Bag;
import pt.ua.deti.common.Plane;
import pt.ua.deti.common.Utils;
import pt.ua.deti.entities.BusDriver;
import pt.ua.deti.entities.Passenger;
import pt.ua.deti.entities.Porter;
import pt.ua.deti.shared.imp.ArrivalLounge;
import pt.ua.deti.shared.imp.ArrivalTerminalExit;
import pt.ua.deti.shared.imp.ArrivalTerminalTransferQuay;
import pt.ua.deti.shared.imp.BaggageCollectionPoint;
import pt.ua.deti.shared.imp.BaggageReclaimOffice;
import pt.ua.deti.shared.imp.DepartureTerminalEntrance;
import pt.ua.deti.shared.imp.DepartureTerminalTransferQuay;
import pt.ua.deti.shared.imp.TemporaryStorageArea;
import pt.ua.deti.shared.remote.GRIRemote;
import pt.ua.deti.shared.remote.PHRemote;
import pt.ua.deti.shared.stubs.GRIInterface;
import pt.ua.deti.shared.stubs.PHInterface;


/**
 * Main execution program.
 * 
 * @author Catarina Silva
 * @author Duarte Dias
 * @version 1.0
 */
public class AirportConcSol {
    public static void main(final String[] args) throws IOException {
        // Read the configuration file
        final Properties prop = Utils.loadProperties("config.properties");
        
        // The number of Planes
        final int K = Integer.parseInt(prop.getProperty("K"));
        // The number of Passengers per Plane
        final int N = Integer.parseInt(prop.getProperty("N"));
        // Maximum number of luggage per Passenger
        final int M = Integer.parseInt(prop.getProperty("M"));
        // Maximum number of seats on the Bus
        final int T = Integer.parseInt(prop.getProperty("T"));
        // The duration that the Bus driver awaits for passengers (milliseconds)
        final long D = Long.parseLong(prop.getProperty("D"));
        // The probability of losing a piece of luggage
        final double P = Double.parseDouble(prop.getProperty("P"));

        // GRI Remote
        final String gri_host = prop.getProperty("gri_host");
        final int gri_port = Integer.parseInt(prop.getProperty("gri_port"));

        //PH Remote
        final String ph_host = prop.getProperty("ph_host");
        final int ph_port = Integer.parseInt(prop.getProperty("ph_port"));

        // Create the Information Sharing Regions
        // Remote
        final GRIInterface gri = new GRIRemote(gri_host, gri_port);
        final PHInterface ph = new PHRemote(ph_host, ph_port);

        // Local
        final ArrivalLounge al = new ArrivalLounge(N);
        final BaggageCollectionPoint bcp = new BaggageCollectionPoint(gri);
        final BaggageReclaimOffice bro = new BaggageReclaimOffice();
        final TemporaryStorageArea tsa = new TemporaryStorageArea(gri);
        final ArrivalTerminalExit ate = new ArrivalTerminalExit(N);
        final DepartureTerminalEntrance dte = new DepartureTerminalEntrance(N);
        final ArrivalTerminalTransferQuay attq = new ArrivalTerminalTransferQuay(N, T, D, gri);
        final DepartureTerminalTransferQuay dttq = new DepartureTerminalTransferQuay(gri);

        ate.setDTE(dte);
        dte.setATE(ate);

        // Create the list of planes
        final List<Plane> planes = createPlanes(K, N, M, P, gri, al, bcp, bro, ate, dte, attq, dttq);

        // Start porter
        final Porter porter = new Porter(al, ph, bcp, tsa, gri);
        final Thread tporter = new Thread(porter);
        tporter.start();

        // Start bus driver
        final BusDriver busDriver = new BusDriver(attq, dttq, ate, gri);
        final Thread tbusdriver = new Thread(busDriver);
        tbusdriver.start();

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

        // Wait for all entities
        try {
            tporter.join();
            tbusdriver.join();
        } catch (final InterruptedException e) {
            e.printStackTrace();
        }

        try {
            gri.close();
        } catch(Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Returns a {@link List} of {@link pt.ua.deti.common.Plane}s for the simulation.
     * 
     * @param K    the number of {@link pt.ua.deti.common.Plane}
     * @param N    the number of {@link pt.ua.deti.entities.Passenger} per {@link pt.ua.deti.common.Plane}
     * @param M    the maximum number of {@link pt.ua.deti.common.Bag} per {@link pt.ua.deti.entities.Passenger}
     * @param P    the probability of losing a bag in the trip
     * @param gri  {@link GeneralRepositoryInformation}
     * @param al   {@link ArrivalLounge}
     * @param bcp  {@link BaggageCollectionPoint}
     * @param bro  {@link BaggageReclaimOffice}
     * @param ate  {@link ArrivalTerminalExit}
     * @param dte  {@link DepartureTerminalEntrance}
     * @param attq {@link ArrivalTerminalTransferQuay}
     * @param dttq {@link DepartureTerminalTransferQuay}
     * @return a {@link List} of {@link pt.ua.deti.common.Plane}s for the simulation
     */
    private static List<Plane> createPlanes(final int K, final int N, final int M, final double P,
            final GRIInterface gri, final ArrivalLounge al, final BaggageCollectionPoint bcp,
            final BaggageReclaimOffice bro, final ArrivalTerminalExit ate, final DepartureTerminalEntrance dte,
            final ArrivalTerminalTransferQuay attq, final DepartureTerminalTransferQuay dttq) {
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
# Airport Rhapsody #2

Implementation in Java of the Airport Rhapsody #2.
This version uses message passing instead of local synchronization.

## Requirements

The project was developed in Java, and package as a Maven project.
As such the project only requires:

- Java 8
- Maven

## Message passing

As stated the project uses message passing to synchronize the processes.
The communication is based on the TCP protocol, each shared memory was converted to a server and each entity is converted to a client.
Regardless of the communication protocol, it is necessary to define a set of messages.

There are several possibilities:

1. Write a set of serializable classes, two for each method (a request and reply method)
2. Rely on a structured format such as JSON or XML
3. Use only two messages, a request and reply message respectively

We used the last option. The first option requires the coding of multiple classes, and all the classes become very similar. The second option was discarded because Java does not support natively JSON and an XML parser is computationally heavy.

## Shared Memory as Servers

Each shared memory was converted into a TCP server.
There was coded an Interface for each shared memory that define the methods necessary to interact with it.

After we also develop a remote version that implements the previous interface. This class sends a message to the shared memory server and returns the message.

Finally, it was developed a main program for each shared memory. The main program opens a server socket and creates a new thread for each client request and executes the respective method on the shared memory implementation.

```java
public static void main(final String[] args) {
    // Read the configuration file
    final Properties prop = Utils.loadProperties("config.properties");
    // Server port
    final int port = Integer.parseInt(prop.getProperty("sm_port"));
    // Create the Shared Region
    final Interface sharedMemory = new SharedMemoryImplementation();

    // Stopping criteria
    final AtomicInteger done = new AtomicInteger(0);

    try {
        // Setup the server
        final ServerSocket serverSocket = new ServerSocket(port);
        serverSocket.setSoTimeout(1000);

        // Serve all the clients (Bus driver, Porter, Passenger)
        while (done.get() < 3) {
            try {
                final Handler handler = new Handler(serverSocket.accept(), al, done);
                final Thread thread = new Thread(handler);
                thread.start();
            } catch (SocketTimeoutException e) {
                // ignore, used to check the stopping criteria
            }
        }

        // Close the server socket
        serverSocket.close();
        } catch (final Exception e) {
            e.printStackTrace();
        }
    }
}
```

It is important to mention that the entities did not suffer major alteration. Each one of the entities (Bus driver, Porter, Passenger)) runs into a separate process and uses the previously mentioned shared memory interface and remote implementation.

## Stopping criteria

Contrary to the previous implementation where a single process controlled the end of the simulation, with a distributed implementation it is necessary to develop a method for each shared memory server to end the simulation.
In this project, the stopping criteria were implemented using an atomic integer.
The server has a while loop that runs until the number of required entities did not signal the end of their life cycle.

## Configuration File

The simulation reads a configuration file (src/main/resources/config.properties) to initialize several parameters.
The configuration file is written as a properties file from Java.
In the following table describes all the properties:

|  Property |                                   Description |     Default |
|-----------|-----------------------------------------------|-------------|
|         K |                              number of planes |           5 |
|         N |                          passengers per plane |           6 |
|         M |           max number of luggage per passenger |           2 |
|         T |                max number of seats on the bus |           3 |
|         P |      probability of losing a piece of luggage |         0.1 |
|         D | milliseconds the driver awaits for passengers |         100 |
|         L |                      path to the logging file | airport.log |
|         V |           verbose (print log on the terminal) |       false |
|  gri_host |                                  GRI Hostname |   localhost |
|  gri_port |                                  GRI Port     |        2000 |
|   ph_host |                                   PH Hostname |   localhost |
|   ph_port |                                   PH Port     |        2001 |
|  tsa_host |                                  TSA Hostname |   localhost |
|  tsa_port |                                  TSA Port     |        2002 |
| dttq_host |                                 DTTQ Hostname |   localhost |
| dttq_port |                                 DTTQ Port     |        2003 |
| attq_host |                                 ATTQ Hostname |   localhost |
| attq_port |                                 ATTQ Port     |        2004 |
|   al_host |                                   AL Hostname |   localhost |
|   al_port |                                   AL Port     |        2005 |
|  bcp_host |                                  BCP Hostname |   localhost |
|  bcp_port |                                  BCP Port     |        2006 |
|  bro_host |                                  BRO Hostname |   localhost |
|  bro_port |                                  BRO Port     |        2007 |
|  ate_host |                                  ATE Hostname |   localhost |
|  ate_port |                                  ATE Port     |        2008 |
|  dte_host |                                  DTE Hostname |   localhost |
|  dte_port |                                  DTE Port     |        2009 |

## Documentation

All the code was commented with javadoc, to compile it simply run:

```bash
mvn javadoc:javadoc
firefox target/site/apidocs/index.html
```

## Run the simulation

Contrary to the first version, this project contains several main programs (each main program is identifiable as Main{Name}.java).

To help deploying the simulation just run the following command:

```bash
./run.sh
```

The script will run 10 simulation in order to stress test the code. If you want, you can pass the number of simulation as an argument, for example to run 3 simulations:

```bash
./run.sh 3
```

## Authors

- **Catarina Silva**
- **Duarte Dias**

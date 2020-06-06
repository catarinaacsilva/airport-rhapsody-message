#!/usr/bin/env bash

# Defines the number of times the simulation is repeated     
REPS=${1:-10}    

# Declare an array to hold the process ids from the programs
declare -a pids

# Define a function that kills the process ids in case of INT signal
function cleanup() {
  let pids
  # kill all pids
  for pid in ${pids[*]}; do
    kill -9 $pid
  done
  exit 1
}

# Trap the interrupt signal
trap cleanup INT

# Compile the code
mvn -q clean compile

# Run the simulation REPS times
for i in $(seq 1 ${REPS})
do
  echo -e "\nRun n. " $i
  echo -e "Launch Shared Memory Servers:"
  echo -e "\tStart GRI"
  mvn -q exec:java -Dexec.mainClass="pt.ua.deti.shared.MainGRI" &
  pids[0]=$!
  sleep 1
  
  echo -e "\tStart PH"
  mvn -q exec:java -Dexec.mainClass="pt.ua.deti.shared.MainPH" &
  pids[1]=$!
  sleep 1

  echo -e "\tStart TSA"
  mvn -q exec:java -Dexec.mainClass="pt.ua.deti.shared.MainTSA" &
  pids[2]=$!
  sleep 1

  echo -e "\tStart DTTQ"
  mvn -q exec:java -Dexec.mainClass="pt.ua.deti.shared.MainDTTQ" &
  pids[3]=$!
  sleep 1

  echo -e "\tStart ATTQ"
  mvn -q exec:java -Dexec.mainClass="pt.ua.deti.shared.MainATTQ" &
  pids[4]=$!
  sleep 1

  echo -e "\tStart AL"
  mvn -q exec:java -Dexec.mainClass="pt.ua.deti.shared.MainAL" &
  pids[5]=$!
  sleep 1

  echo -e "\tStart BCP"
  mvn -q exec:java -Dexec.mainClass="pt.ua.deti.shared.MainBCP" &
  pids[6]=$!
  sleep 1

  echo -e "\tStart BRO"
  mvn -q exec:java -Dexec.mainClass="pt.ua.deti.shared.MainBRO" &
  pids[7]=$!
  sleep 1

  echo -e "\tStart ATE"
  mvn -q exec:java -Dexec.mainClass="pt.ua.deti.shared.MainATE" &
  pids[8]=$!
  sleep 1

  echo -e "\tStart DTE"
  mvn -q exec:java -Dexec.mainClass="pt.ua.deti.shared.MainDTE" &
  pids[9]=$!
  sleep 1

  echo -e "Launch Entities:"
  echo -e "\tBus Driver"
  mvn -q exec:java -Dexec.mainClass="pt.ua.deti.entities.MainBusDriver" &
  pids[10]=$!
  sleep 1

  echo -e "\tPorter"
  mvn -q exec:java -Dexec.mainClass="pt.ua.deti.entities.MainPorter" &
  pids[10]=$!
  sleep 1

  echo -e "\tPassenger"
  mvn -q exec:java -Dexec.mainClass="pt.ua.deti.entities.MainPassenger" &
  pids[10]=$!
  sleep 1

  # wait for all pids
  for pid in ${pids[*]}; do
    wait $pid
  done
  sleep 1
done

echo -e "Done..."

#!/usr/bin/env bash

# Declare an array to hold the process ids from the programs
declare -a pids

# Define a function that kills the process ids in case of INT signal
function cleanup() {
  let pids
  # kill all pids
  for pid in ${pids[*]}; do
    echo -e "PID $pid"
    kill -9 $pid
  done
  exit 1
}

# Trap the interrupt signal
trap cleanup INT

# Compile the code
mvn -q clean compile

# Run the simulation 10 times
for i in $(seq 1 10)
do
  echo -e "\nRun n. " $i
  echo -e "Start GRI"
  mvn -q exec:java -Dexec.mainClass="pt.ua.deti.shared.MainGRI" &
  pids[0]=$!
  sleep 1
  
  echo -e "Start PH"
  mvn -q exec:java -Dexec.mainClass="pt.ua.deti.shared.MainPH" &
  pids[1]=$!
  sleep 1

  echo -e "Start TSA"
  mvn -q exec:java -Dexec.mainClass="pt.ua.deti.shared.MainTSA" &
  pids[2]=$!
  sleep 1

  echo -e "Start Old Simulation"
  mvn -q exec:java -Dexec.mainClass="pt.ua.deti.main.AirportConcSol" &
  pids[3]=$!
  sleep 1

  # wait for all pids
  for pid in ${pids[*]}; do
    wait $pid
  done
  sleep 1
done

echo -e "Done..."

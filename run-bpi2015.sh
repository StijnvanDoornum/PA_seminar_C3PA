#!/bin/bash

# BPI2015 Conformance Checking Experiment Runner
# This script runs conformance checking experiments on the BPI2015 dataset

JAVA_HOME=/opt/homebrew/Cellar/openjdk/25.0.1/libexec/openjdk.jdk/Contents/Home
JAVA=$JAVA_HOME/bin/java
PROJECT_DIR="/Users/stijnvandoornum/Documents/tu/Seminar/ConformanceCheckingUsingTries-1"
CLASSPATH="$PROJECT_DIR/target/classes:$PROJECT_DIR/lib/*"

# Number of traces to check (default: 50, change as needed)
NUM_TRACES=${1:-50}

echo "========================================"
echo " BPI2015 Conformance Checking"
echo " Checking $NUM_TRACES traces"
echo "========================================"
echo ""

# Compile if needed
if [ ! -f "$PROJECT_DIR/target/classes/ee/ut/cs/dsg/confcheck/XESRunner.class" ]; then
    echo "Compiling project..."
    cd "$PROJECT_DIR"
    mvn compile -DskipTests > /dev/null 2>&1
    echo "Compilation complete."
    echo ""
fi

# Run the experiment
cd "$PROJECT_DIR"
$JAVA -cp "$CLASSPATH" ee.ut.cs.dsg.confcheck.XESRunner

echo ""
echo "========================================"
echo " To run with more traces, use:"
echo " ./run-bpi2015.sh 100"
echo "========================================"


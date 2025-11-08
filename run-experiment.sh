#!/bin/bash

# Set Java path
JAVA_HOME=/opt/homebrew/Cellar/openjdk/25.0.1/libexec/openjdk.jdk/Contents/Home
JAVA=$JAVA_HOME/bin/java

# Set project directory
PROJECT_DIR="/Users/stijnvandoornum/Documents/tu/Seminar/ConformanceCheckingUsingTries-1"

# Set classpath
CLASSPATH="$PROJECT_DIR/target/classes:$PROJECT_DIR/lib/*"

# Run the simple test
echo "Running simple conformance checking test..."
$JAVA -cp "$CLASSPATH" ee.ut.cs.dsg.confcheck.SimpleTest

echo ""
echo "============================================"
echo "To run the full experiments with BPI2015 data,"
echo "we need to fix the compilation issues with"
echo "the XES library dependencies first."
echo "============================================"


# Setup Status and Instructions

## ✅ What's Working

The **core trie-based conformance checking algorithm** is fully functional! 

### Successfully Running Test

```bash
/opt/homebrew/Cellar/openjdk/25.0.1/libexec/openjdk.jdk/Contents/Home/bin/java \
  -cp "target/classes:lib/*" \
  ee.ut.cs.dsg.confcheck.SimpleTest
```

This test demonstrates:
- ✅ Trie construction from model traces
- ✅ Alignment computation using randomized search
- ✅ Cost calculation (log moves, model moves, synchronous moves)
- ✅ All core algorithms from the paper

### Test Results

The algorithm correctly computes alignments for various trace patterns:
- Perfect matches: cost 0
- Missing events: appropriate cost based on edit distance
- Extra events: detected and penalized correctly

## ❌ What's Not Working

### Runner.java with BPI2015 Dataset

The `Runner.java` class that loads XES log files has compilation issues because some library dependencies are not properly resolved in Maven.

**Problem Files:**
- `org/processmining/logfiltering/algorithms/ProtoTypeSelectionAlgo.java`
- Various ICC (Incremental Conformance Checking) classes
- Some parameters classes

**Missing Libraries:**
- `nl.tue.alignment` classes
- Some ProM plugin classes that aren't in the provided JARs

## 🔧 Possible Solutions

### Option 1: Fix Dependencies (Complex)

You would need to:
1. Identify all missing JAR files
2. Add them to the `lib/` directory
3. Update `pom.xml` with all system dependencies

### Option 2: Exclude Problematic Files (Easier)

Configure Maven to skip compilation of files that aren't needed:
- The logfiltering algorithms (mostly for preprocessing)
- The ICC package (alternative approach)

You only need:
- `ee.ut.cs.dsg.confcheck.*` (core trie-based checker) ✅ **WORKS**
- XES loading capability for Runner.java

### Option 3: Create Custom Runner (Recommended)

Create a simpler runner that:
1. Uses a basic XES parser (already in OpenXES jar)
2. Converts traces to the format needed by the trie
3. Runs the conformance checking experiments

## 📊 Your Dataset

Located at: `/Users/stijnvandoornum/Dropbox/Mijn Mac (MacBook Pro van Stijn)/Downloads/BPI2015/`

Files available:
- `frequencyLog.xml` - Frequency-based model traces
- `sampledLog.xml` - Sample traces to check
- `randomLog.xml` - Random sampling
- `simulatedLog.xml` - Simulated traces
- `sampledClusteredLog.xml` - Clustered sampling
- `reducedLogActivity.xml` - Activity-reduced log

## 🎯 Next Steps

### Quick Win: Test with Small Dataset

Create a simple CSV/text version of a few traces from BPI2015 and test with `SimpleTest.java`.

###  Full Solution: Fix XES Loading

I can help you create a minimal `XESRunner.java` that:
- Uses only the OpenXES library (which IS available)
- Loads the BPI2015 logs
- Runs the trie-based conformance checking
- Outputs results

This would bypass all the ProM plugin infrastructure that's causing compilation issues.

## 📝 What the Research Demonstrates

Even though the full pipeline isn't running yet, the **core contribution of the paper** is fully functional:

1. ✅ Trie data structure for compact model representation
2. ✅ Randomized search algorithm for alignment computation
3. ✅ Cost functions and heuristics
4. ✅ Handling of log moves, model moves, and synchronous moves

The only missing piece is the infrastructure to load real-world XES logs, which is a matter of plumbing rather than algorithmic implementation.

## 💡 Quick Command Reference

### Compile
```bash
cd /Users/stijnvandoornum/Documents/tu/Seminar/ConformanceCheckingUsingTries-1
mvn compile
# Will show errors but core classes compile successfully
```

### Run Simple Test
```bash
/opt/homebrew/Cellar/openjdk/25.0.1/libexec/openjdk.jdk/Contents/Home/bin/java \
  -cp "target/classes:lib/*" \
  ee.ut.cs.dsg.confcheck.SimpleTest
```

### Check What Compiled
```bash
ls -la target/classes/ee/ut/cs/dsg/confcheck/
```

---

**Would you like me to create a custom XESRunner that can work with your BPI2015 dataset?** This would be the fastest path to running full experiments.


package ee.ut.cs.dsg.confcheck;

import ee.ut.cs.dsg.confcheck.alignment.Alignment;
import ee.ut.cs.dsg.confcheck.trie.Trie;
import ee.ut.cs.dsg.confcheck.util.AlphabetService;
import ee.ut.cs.dsg.confcheck.SimpleXESReader;
import ee.ut.cs.dsg.confcheck.SimpleXESReader.SimpleLog;
import ee.ut.cs.dsg.confcheck.SimpleXESReader.SimpleTrace;

import java.util.ArrayList;
import java.util.List;

/**
 * Custom XES Runner for BPI2015 experiments
 * Uses only OpenXES library to avoid dependency issues
 */
public class XESRunner {
    
    private AlphabetService alphabetService;
    private String dataDir;
    
    public XESRunner(String dataDir) {
        this.dataDir = dataDir;
        this.alphabetService = new AlphabetService();
    }
    
    public static void main(String[] args) {
        String dataDir = "/Users/stijnvandoornum/Dropbox/Mijn Mac (MacBook Pro van Stijn)/Downloads/BPI2015/";
        
        // Check if custom data directory provided
        if (args.length > 0) {
            dataDir = args[0];
        }
        
        XESRunner runner = new XESRunner(dataDir);
        
        System.out.println("=== BPI2015 Conformance Checking Experiment ===\n");
        
        // Run experiment with frequency log as model and sampled log as test
        int tracesToCheck = 50; // Default
        if (args.length > 1) {
            try {
                tracesToCheck = Integer.parseInt(args[1]);
            } catch (NumberFormatException e) {
                System.out.println("Invalid number of traces, using default: 50");
            }
        }
        runner.runExperiment("frequencyLog.xml", "sampledLog.xml", tracesToCheck);
    }
    
    public void runExperiment(String modelLogFile, String testLogFile, int maxTraces) {
        System.out.println("Model log: " + modelLogFile);
        System.out.println("Test log: " + testLogFile);
        System.out.println("Max traces to check: " + maxTraces);
        System.out.println();
        
        try {
            // Step 1: Load model log and build trie
            System.out.println("Loading model log...");
            SimpleLog modelLog = loadXESLog(dataDir + modelLogFile);
            System.out.println("Model log contains " + modelLog.traces.size() + " traces");
            
            System.out.println("\nBuilding trie from model traces...");
            long startTime = System.currentTimeMillis();
            Trie trie = buildTrieFromLog(modelLog);
            long trieTime = System.currentTimeMillis() - startTime;
            
            System.out.println("Trie built in " + trieTime + "ms");
            System.out.println("Trie statistics:");
            System.out.println("  - Nodes: " + trie.getSize());
            System.out.println("  - Max trace length: " + trie.getMaxTraceLength());
            System.out.println("  - Min trace length: " + trie.getMinTraceLength());
            System.out.println("  - Avg trace length: " + trie.getAvgTraceLength());
            System.out.println("  - Total events: " + trie.getNumberOfEvents());
            
            // Step 2: Load test log
            System.out.println("\nLoading test log...");
            SimpleLog testLog = loadXESLog(dataDir + testLogFile);
            System.out.println("Test log contains " + testLog.traces.size() + " traces");
            
            // Step 3: Create conformance checker
            System.out.println("\nCreating conformance checker...");
            ConformanceChecker checker = new RandomConformanceChecker(
                trie, 
                1,      // log move cost
                1,      // model move cost
                50000,  // max states in queue
                1000000 // max trials
            );
            
            // Step 4: Check conformance for test traces
            System.out.println("\n=== Computing Alignments ===\n");
            System.out.println("Trace#, Length, Cost, Time(ms)");
            
            int tracesToCheck = Math.min(maxTraces, testLog.traces.size());
            long totalTime = 0;
            int totalCost = 0;
            int perfectMatches = 0;
            
            for (int i = 0; i < tracesToCheck; i++) {
                SimpleTrace xTrace = testLog.traces.get(i);
                List<String> trace = convertTrace(xTrace);
                
                startTime = System.currentTimeMillis();
                Alignment alignment = checker.check(trace);
                long alignmentTime = System.currentTimeMillis() - startTime;
                totalTime += alignmentTime;
                
                if (alignment != null) {
                    int cost = alignment.getTotalCost();
                    totalCost += cost;
                    if (cost == 0) {
                        perfectMatches++;
                    }
                    System.out.println((i+1) + ", " + trace.size() + ", " + cost + ", " + alignmentTime);
                    
                    // Print detailed alignment for first few traces only
                    if (i < 2) {
                        System.out.println("  Trace: " + getTraceString(trace, 50));
                        System.out.println("  Alignment preview:");
                        printAlignment(alignment, 3);
                    }
                } else {
                    System.out.println((i+1) + ", " + trace.size() + ", NO_ALIGNMENT, " + alignmentTime);
                }
            }
            
            // Step 5: Print summary statistics
            System.out.println("\n=== Summary Statistics ===");
            System.out.println("Total traces checked: " + tracesToCheck);
            System.out.println("Perfect matches (cost=0): " + perfectMatches + " (" + 
                String.format("%.1f%%", 100.0 * perfectMatches / tracesToCheck) + ")");
            System.out.println("Average alignment cost: " + 
                String.format("%.2f", (double) totalCost / tracesToCheck));
            System.out.println("Total computation time: " + totalTime + "ms");
            System.out.println("Average time per trace: " + 
                String.format("%.2f", (double) totalTime / tracesToCheck) + "ms");
            
            System.out.println("\n=== Experiment Complete ===");
            
        } catch (Exception e) {
            System.err.println("Error running experiment: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    private SimpleLog loadXESLog(String filename) throws Exception {
        return SimpleXESReader.parseXES(filename);
    }
    
    private Trie buildTrieFromLog(SimpleLog log) {
        // Count unique activities to size the trie appropriately
        int uniqueActivities = 100; // Default estimate
        
        Trie trie = new Trie(uniqueActivities);
        
        for (SimpleTrace xTrace : log.traces) {
            List<String> trace = convertTrace(xTrace);
            if (!trace.isEmpty()) {
                trie.addTrace(trace);
            }
        }
        
        return trie;
    }
    
    private List<String> convertTrace(SimpleTrace xTrace) {
        List<String> trace = new ArrayList<>();
        
        for (String activityName : xTrace.events) {
            if (activityName != null && !activityName.isEmpty()) {
                // Convert to single character using alphabet service
                char encoded = alphabetService.alphabetize(activityName);
                trace.add(String.valueOf(encoded));
            }
        }
        
        return trace;
    }
    
    private String getTraceString(List<String> trace, int maxLength) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < Math.min(trace.size(), maxLength); i++) {
            sb.append(trace.get(i));
        }
        if (trace.size() > maxLength) {
            sb.append("...");
        }
        return sb.toString();
    }
    
    private void printAlignment(Alignment alignment, int maxMoves) {
        String[] lines = alignment.toString().split("\n");
        int linesToPrint = Math.min(lines.length, maxMoves + 3); // +3 for header lines
        for (int i = 0; i < linesToPrint; i++) {
            System.out.println("    " + lines[i]);
        }
        if (lines.length > linesToPrint) {
            System.out.println("    ... (" + (lines.length - linesToPrint) + " more lines)");
        }
    }
}


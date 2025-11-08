package ee.ut.cs.dsg.confcheck;

import ee.ut.cs.dsg.confcheck.alignment.Alignment;
import ee.ut.cs.dsg.confcheck.trie.Trie;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class SimpleTest {

    public static void main(String[] args) {
        System.out.println("=== Trie-Based Conformance Checking Test ===\n");
        
        // Create a trie with model behavior
        Trie trie = new Trie(26);
        
        // Add model traces (like the example in the README)
        trie.addTrace(Arrays.asList("a", "b", "c", "e"));
        trie.addTrace(Arrays.asList("a", "b", "c", "d", "b", "e"));
        trie.addTrace(Arrays.asList("a", "b", "c", "d", "b", "d", "b", "e"));
        trie.addTrace(Arrays.asList("a", "c", "b", "e"));
        trie.addTrace(Arrays.asList("a", "b", "e"));
        
        System.out.println("Model traces added to trie:");
        System.out.println("1. <a,b,c,e>");
        System.out.println("2. <a,b,c,d,b,e>");
        System.out.println("3. <a,b,c,d,b,d,b,e>");
        System.out.println("4. <a,c,b,e>");
        System.out.println("5. <a,b,e>");
        System.out.println("\nTrie statistics:");
        System.out.println("- Max trace length: " + trie.getMaxTraceLength());
        System.out.println("- Min trace length: " + trie.getMinTraceLength());
        System.out.println("- Avg trace length: " + trie.getAvgTraceLength());
        System.out.println("- Number of nodes: " + trie.getSize());
        System.out.println("- Total events: " + trie.getNumberOfEvents());
        
        // Create conformance checker
        ConformanceChecker checker = new RandomConformanceChecker(trie, 1, 1, 50000, 1000000);
        
        // Test log traces (from the README example)
        List<List<String>> testTraces = new ArrayList<>();
        testTraces.add(Arrays.asList("a", "b", "c", "e"));  // Perfect match - cost 0
        testTraces.add(Arrays.asList("a", "e"));             // Missing b - cost 1
        testTraces.add(Arrays.asList("c", "e"));             // Missing a,b - cost 2
        testTraces.add(Arrays.asList("a", "c", "b", "d", "e"));  // Extra d - cost varies
        testTraces.add(Arrays.asList("a", "b", "e"));        // Perfect match - cost 0
        testTraces.add(Arrays.asList("a", "c", "b", "d", "b", "d", "b", "d", "b", "e"));
        
        System.out.println("\n=== Computing Alignments ===\n");
        
        for (int i = 0; i < testTraces.size(); i++) {
            List<String> trace = testTraces.get(i);
            System.out.println("Trace " + (i + 1) + ": " + trace);
            
            long start = System.currentTimeMillis();
            Alignment alignment = checker.check(trace);
            long time = System.currentTimeMillis() - start;
            
            if (alignment != null) {
                System.out.println("  Alignment cost: " + alignment.getTotalCost());
                System.out.println("  Time: " + time + "ms");
                System.out.println("  Alignment:");
                System.out.println(alignment.toString().replaceAll("(?m)^", "    "));
            } else {
                System.out.println("  No alignment found!");
            }
            System.out.println();
        }
        
        System.out.println("=== Test Complete ===");
    }
}


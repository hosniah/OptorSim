/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.hosniah.grid;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Multimap;
import de.unikassel.cs.kde.trias.TriasRunner;
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.Set;
import java.util.TreeSet;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author hosniah
 * @param 
 */
public class ArtMiner_bgrt { 

    private final String premise;
    private final String conclusion;
    public final String frequentTriconcept;
    public double conditionalTriadicSupport;
    public double conditionalTriadicConfidence;
    public boolean belongsToBGRT;
    public int cardinalOfSimilarTC;
    private final ArrayList<String> tasks;
    private final Multimap<Integer, String> extractionContext;
    private final String[] sites;
    private final String[] filesOFRA;

    public ArtMiner_bgrt(String premise, String conclusion, String[] FilesOfRA, String[] tasks, String[] sites) {
        super();
            this.premise                      = premise;
            this.conclusion                   = conclusion;
            this.tasks                        = new ArrayList<String>(Arrays.asList(tasks));
            this.frequentTriconcept           = "Tasks = "+Arrays.toString(tasks)+", Files = "+Arrays.toString(FilesOfRA)+", Sites ="+Arrays.toString(sites);
            this.conditionalTriadicSupport    = 0;
            this.conditionalTriadicConfidence = 0;
            this.belongsToBGRT                = false;
            this.extractionContext            = formatAndReduceExtractionContext();
            this.sites                        = sites;
            this.filesOFRA                    = FilesOfRA;
    }
    
    public Multimap<Integer, String> formatAndReduceExtractionContext() {
       // Table<String, String, Integer> table = HashBasedTable.create();
       //Table<String, String, String> table = HashBasedTable.create();
            Multimap<Integer, String> multimap = ArrayListMultimap.create();
        try {
    
            FileInputStream fstream = new FileInputStream("fixture.input");
            BufferedReader br = new BufferedReader(new InputStreamReader(fstream));
            String strLine;
            while ((strLine = br.readLine()) != null) { 
                String[] splitStr = strLine.split("\\s+");
                //Reduce extraction context to domain support, only tasks of RA are considered
                if(this.tasks.contains(splitStr[0])) {                  
                    multimap.put(Integer.parseInt(splitStr[0]), splitStr[1]+"-"+splitStr[2]);                                    
                }
            }             
            //@Debug
             //   System.out.println("Tasks Of RA: "+this.tasks.toString());
             //   System.out.println("multimap : "+ multimap.toString());            
        } catch (IOException ex) {
            Logger.getLogger(TriasRunner.class.getName()).log(Level.SEVERE, null, ex);
        }                    
     //   System.out.println(mapcolumn);
       return multimap;
    }

    public double computeConditionalTriadicSupport(int cardinalOfSimilarTC) {  
        int incidence      = 0; 
        // double support     = 0;
        int tasksGroupSize =  this.tasks.size();
        // this.extractionContext.columnKeySet();
                    // Map<String, Map<String, String>> mapcolumn = this.extractionContext.rowMap();
                     //Map<String, Map<String, String>> mapcolumn = this.extractionContext.columnMap();  
        Set keySet = this.extractionContext.keySet();
        Iterator keyIterator = keySet.iterator();
        while (keyIterator.hasNext() ) {
            Integer key = (Integer) keyIterator.next();
            Collection<String> values = this.extractionContext.get(key);
            Multimap<Integer, String> occurence_map = ArrayListMultimap.create();
            for (String occurence: values ) {
               String[] file2site = occurence.split("-");
               //use sites as keys and files as values
               occurence_map.put(Integer.parseInt(file2site[1]),file2site[0]);
            }        
        /* Context is now formatted as
           {S1=[F2, F3, F4], S2=[F2, F4], S3=[F3, F4], S4=[F1, F3, F5]}
         */
            for(Collection<String> col : occurence_map.asMap().values()) {                
             //   System.out.println(col);
                Object[] arr1 = col.toArray();
                Set <Object> set1 = new TreeSet <Object> ();
                Set <String> set2 = new TreeSet <String> (String.CASE_INSENSITIVE_ORDER);
                 set1.addAll(Arrays.asList(arr1)); 
                 set2.addAll(Arrays.asList(this.filesOFRA));                
                if(set1.containsAll(set2)) {
                   incidence++;
                   //System.out.println("Increment Support right here"); 
                }
            }            
            //iterate on Gridsites ,then on collection, if a
            //System.out.println(values.toString());
        }
        //    System.out.println("+++++++++ "+occurence_map.toString());            
            // support = (incidence/tasksGroupSize)/cardinalOfSimilarTC;
            double support = incidence/tasksGroupSize;
            double supp_c  = support/cardinalOfSimilarTC;            
            System.out.println("support count..."+incidence+" ** task count..."+tasksGroupSize+" -- Similar TC count..."+cardinalOfSimilarTC+"  => Support_c = "+supp_c);
            this.conditionalTriadicSupport = supp_c;
        return this.conditionalTriadicSupport;        
    }

    public double computeConditionalTriadicSupport4Premisse() {
        // suppc(this.premise);
        /* Find similar files for premise only to make confidence computing easy
        Premise is always one file a time:
         */
        int incidence = 0;    
        Set keySet = this.extractionContext.keySet();
        Iterator keyIterator = keySet.iterator();
        while (keyIterator.hasNext() ) {
            Integer key = (Integer) keyIterator.next();
            Collection<String> values = this.extractionContext.get(key);
            Multimap<Integer, String> occurence_map = ArrayListMultimap.create();
            for (String occurence: values ) {
               String[] file2site = occurence.split("-");
               //use sites as keys and files as values
               occurence_map.put(Integer.parseInt(file2site[1]),file2site[0]);
            }        
        /* Context is now formatted as
           {S1=[F2, F3, F4], S2=[F2, F4], S3=[F3, F4], S4=[F1, F3, F5]}
         */
            for(Collection<String> col : occurence_map.asMap().values()) {                
             //   System.out.println(col);
                Object[] arr1 = col.toArray();
                Set <Object> set1 = new TreeSet <Object> ();
                Set <String> set2 = new TreeSet <String> (String.CASE_INSENSITIVE_ORDER);
                 set1.addAll(Arrays.asList(arr1)); 
                 set2.addAll(Arrays.asList(this.premise));                
                if(set1.containsAll(set2)) {
                   incidence++;
                   //System.out.println("Increment Support right here"); 
                }
            }
        }
                   

            int tasksGroupSize =  this.tasks.size();
            formatAndReduceExtractionContext();
            double support_premise = incidence/tasksGroupSize;
            double suppc_premise  = support_premise/cardinalOfSimilarTC;
            
            
        System.out.println("+++++++++ :"+this.premise+" --- "+suppc_premise);
        return suppc_premise;
    }
 
    public double computeConditionalTriadicConfidence() {
        this.conditionalTriadicConfidence = 1; // 
        double suppc_premise = computeConditionalTriadicSupport4Premisse();
        this.conditionalTriadicConfidence = this.conditionalTriadicSupport /suppc_premise;
        return this.conditionalTriadicConfidence;        
    }

    public void addToBGRT() {
        this.belongsToBGRT  = true;
    }

}

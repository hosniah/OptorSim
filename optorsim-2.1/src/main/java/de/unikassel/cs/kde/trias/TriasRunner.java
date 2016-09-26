/**
 *  
 *  Trias Algorithm - Trias is an algorithm for computing triadic concepts which
 * 		fulfill minimal support constraints.
 *   
 *  Copyright (C) 2006 - 2009 Knowledge & Data Engineering Group, 
 *                            University of Kassel, Germany
 *                            http://www.kde.cs.uni-kassel.de/
 *  
 *  This program is free software; you can redistribute it and/or
 *  modify it under the terms of the GNU General Public License
 *  as published by the Free Software Foundation; either version 2
 *  of the License, or (at your option) any later version.
 * 
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *  
 *  You should have received a copy of the GNU General Public License
 *  along with this program; if not, write to the Free Software
 *  Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301, USA.
 */

package de.unikassel.cs.kde.trias;

import com.hosniah.grid.ArtMiner_bgrt;
import com.hosniah.grid.similarity.CosineSimilarity;
import com.hosniah.grid.similarity.LongestCommonSubsequence;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import de.unikassel.cs.kde.trias.util.ConfigurationException;
import de.unikassel.cs.kde.trias.util.TriasCommandLineArgumentsConfigurator;
import de.unikassel.cs.kde.trias.util.TriasConfigurator;
import de.unikassel.cs.kde.trias.util.TriasPropertiesConfigurator;
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map.Entry;
import java.util.Objects;
import java.util.TreeMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 
 * @authors:  rja, hosniah
 * 
 */
public class TriasRunner {

    private static final String PROPERTIES_FILE_NAME  = "trias.properties";
    private static final String TRICONCEPTS_FILE_NAME = "concepts";
    private static int gridFilesCount;

  //  public static void main(String[] args) throws IOException {
    public  void defaultTriasRunner() throws IOException {
            final Trias trias = new Trias();
            TriasConfigurator config;

            final InputStream resourceAsStream = TriasRunner.class.getClassLoader().getResourceAsStream(PROPERTIES_FILE_NAME);
            if (resourceAsStream == null) {
                    System.err.println("Could not find file '" + PROPERTIES_FILE_NAME + "' for configuration.");
                    System.exit(1);
            }

            final Properties prop = new Properties();
            prop.load(resourceAsStream);
            config = new TriasPropertiesConfigurator(prop);

          //  }
            System.err.println(config.usage());

            System.out.println(config.usage()+"****\n");
            /*
             * configure trias
             */
            try {
                    config.configureTrias(trias);
            } catch (final ConfigurationException e) {
                    System.err.println("Could not configure Trias: " + e);
                    System.err.println(config.usage());
                    System.exit(1);
            }

            trias.doWork();


        /* Triconcepts are retrieved, we create groups of tasks and their matching vectors */
    int[] gridHistoryDimensions = trias.getNumberOfItemsPerDimension();        
     TriasRunner.gridFilesCount = gridHistoryDimensions[1];
               try {
              getTriLatticeBufferedReader();
               }catch(Exception ex) {
            Logger.getLogger(TriasRunner.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public static void getTriLatticeBufferedReader() throws FileNotFoundException, Exception {
	// Open the file
FileInputStream fstream = new FileInputStream("concepts");
BufferedReader br = new BufferedReader(new InputStreamReader(fstream));
String strLine;
String[] FilesOFRA;

System.out.println ("Compute Tri-concepts similarities:");
            try {
                //Read File Line By Line
                while ((strLine = br.readLine()) != null)   {
                    // Print the content on the console
                    String result = strLine.substring(strLine.indexOf("{") + 1, strLine.indexOf("}"));
                    // @todo: write something smarter to identify group of sites
                    String files_string = strLine.substring(strLine.indexOf("C") + 2, strLine.lastIndexOf("}"));
                    String sites_string = strLine.substring(strLine.indexOf("B") + 5, strLine.indexOf("C")-4);
                    //String result = sites_string.substring(strLine.indexOf("{") + 1, strLine.indexOf("}"));
                   // System.out.println ("*************************"+strLine+"****************************");
                    
                    String[] groupOfTasks     = result.split("\\s*,\\s*"); String[] triConcept_Sites = files_string.split("\\s*,\\s*");
                    String[] triConcept_Files =  sites_string.split("\\s*,\\s*");
                    //System.out.println(Arrays.toString(triConcept_Files));
                    //System.out.println(Arrays.toString(groupOfTasks));
                   // System.out.println(Arrays.toString(triConcept_Sites));
                    //findSimilarGroupOfTasks(triconcept_tasksVectors);
                    //findSimilarGroupOfSites(triConcept_Sites);
                   
                     //gridMiner(groupOfTasks,triConcept_Files, min_supp, min_conf);
                    FilesOFRA = bgrtGetFilesOfRA(strLine);
                }      
            } catch (IOException ex) {
                Logger.getLogger(TriasRunner.class.getName()).log(Level.SEVERE, null, ex);
            }
            try {
                //Close the input stream
                br.close(); } catch (IOException ex) {
                Logger.getLogger(TriasRunner.class.getName()).log(Level.SEVERE, null, ex);
            }		
	}
    
    private static String[] bgrtGetFilesOfRA(String strLine) {
                FileInputStream fstream = null;        
                int cardinalOfSimilarTC = 0; 
                String result       = strLine.substring(strLine.indexOf("{") + 1, strLine.indexOf("}"));
                String sites_string = strLine.substring(strLine.indexOf("C") + 5, strLine.lastIndexOf("}"));
                String files_string = strLine.substring(strLine.indexOf("B") + 5, strLine.indexOf("C")-4);
                
                String[] groupOfTasks     = result.split("\\s*,\\s*");
                String[] triConcept_Sites = sites_string.split("\\s*,\\s*");
                String[] triConcept_Files = files_string.split("\\s*,\\s*");
                String[] result_FilesOfRA = triConcept_Files;
                String[] merged_FilesOfRA = null;
                String[] merged_Triconcept_tasks = groupOfTasks;
                
                try {
                    fstream           = new FileInputStream("concepts");
                    BufferedReader br = new BufferedReader(new InputStreamReader(fstream));
                    String line;              
                    try {
                        while ((line = br.readLine()) != null)   {
                            if(!Objects.equals(line, new String(strLine))) {
                                // we are walking through other Tri-concepts different from the one selected previously
                                 //System.out.println (line);
                                String triconcept               = line.substring(line.indexOf("{") + 1, line.indexOf("}"));
                                String[] tc_groupOfTasks        = triconcept.split("\\s*,\\s*");
                                String other_sites_string       = line.substring(line.indexOf("C") + 5, line.lastIndexOf("}"));
                                String[] other_triConcept_Sites = other_sites_string.split("\\s*,\\s*");
                                String other_files_string       = line.substring(line.indexOf("B") + 5, line.indexOf("C")-4);                                
                                String[] other_triConcept_Files = other_files_string.split("\\s*,\\s*");
                                try {
                                    // HashMap<String, Integer> triconcept_tasksVectors = buildGroupOfTasksVector(tc_groupOfTasks);                                
                                    boolean sim1  = areGroupOfTasksSimilar(groupOfTasks, tc_groupOfTasks);
                                    boolean sim2  = areGroupOfSitesSimilar(triConcept_Sites, other_triConcept_Sites);
                                    if(sim1==true && sim2==true) {
                                        cardinalOfSimilarTC ++;
                                        System.out.println ("TriConcept:"+strLine+"...Is Similar to..."+line);                                    
                                        //merge files ...
                                        merged_FilesOfRA = mergeFilesOfRA(triConcept_Files , other_triConcept_Files);
                                        result_FilesOfRA = mergeFilesOfRA(result_FilesOfRA, merged_FilesOfRA); 
                                        //@TODO: Update similar tri-concepts by merging tasks also.
                                        
                                    }
                                } catch (Exception ex) {
                                    Logger.getLogger(TriasRunner.class.getName()).log(Level.SEVERE, null, ex);
                                }
                            } else {
                              //  System.out.println ("No similar Triconcepts");
                            }
                        }
                  //  System.out.println("TriConcept:"+strLine+" Similar Tc count: "+cardinalOfSimilarTC);
                 //   System.out.println("Files of RA: " + Arrays.toString(result_FilesOfRA));
                    // Get Triadic  Association Rules
                    if(cardinalOfSimilarTC > 0) {
                        buildTriadicAssociationRules(result_FilesOfRA, groupOfTasks, triConcept_Sites, cardinalOfSimilarTC);
                    } else {
                        System.out.println ("**** Skipping Association rules fetch...No similar triconcepts.");
                    }
                    System.out.println ("-------------------------------------------------------------------------------------------------------");
                } catch (IOException ex) {
                    Logger.getLogger(TriasRunner.class.getName()).log(Level.SEVERE, null, ex);
                }                
   
            } catch (FileNotFoundException ex) {
                Logger.getLogger(TriasRunner.class.getName()).log(Level.SEVERE, null, ex);
            } finally {
                try {
                    fstream.close();
                } catch (IOException ex) {
                    Logger.getLogger(TriasRunner.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        return null;
    }
    
    private static String[] mergeFilesOfRA(String[] array1, String[] array2) {
        List<String> list3 = new ArrayList<String>();
        list3.addAll(Arrays.asList(array1));
        list3.addAll(Arrays.asList(array2));
        // System.out.println("Duplicates List "+list3);
            Object[] st = list3.toArray();
              for (Object s : st) {
                if (list3.indexOf(s) != list3.lastIndexOf(s)) {
                    list3.remove(list3.lastIndexOf(s));
                 }
              }
        // System.out.println("Distinct List "+list3);
        String array3[] = list3.toArray(new String[list3.size()]); 
        return array3;
    }
    
    private static boolean areGroupOfTasksSimilar(String[] src_groupOfTasks, String[] other_groupsOfTasks) {
            double sim1_threshold = 0.90;
            try {
                HashMap<String, Integer> triconcept_tasksVectors_1 = buildGroupOfTasksVector(src_groupOfTasks);
                TreeMap<String, Integer> sorted = new TreeMap<String, Integer>(triconcept_tasksVectors_1);
             /* debug vectors right below
                for (String name: sorted.keySet()){
                    String key =name.toString();
                    String value = sorted.get(name).toString();  
                    System.out.println(key + "..." + value);  
                }
                */
                
                HashMap<String, Integer> triconcept_tasksVectors_2 = buildGroupOfTasksVector(other_groupsOfTasks);
                TreeMap<String, Integer> sorted2 = new TreeMap<String, Integer>(triconcept_tasksVectors_2);
                
                double[] vector1 =  new double[triconcept_tasksVectors_1.size()];               
                double[] vector2 =  new double[triconcept_tasksVectors_2.size()]; 
                int i=0; int j=0;
                for(Entry<String, Integer> entry : sorted.entrySet()) {
                   // Integer cle = Integer.getInteger(entry.getKey());
                    double val = (double) entry.getValue();        
                    vector1[i] =  val; i++;
                }

                for(Entry<String, Integer> entry : sorted2.entrySet()) {
                   // Integer cle = Integer.getInteger(entry.getKey());
                    double val   = (double) entry.getValue();        
                    vector2[j] =  val; j++;
                }   
                
               // System.out.println(Arrays.toString(vector1));
               // System.out.println(Arrays.toString(vector2));                                                
                CosineSimilarity sim1    = new CosineSimilarity(); 
                double similarity_check  = sim1.cosineSimilarity(vector1, vector2);
                //System.out.println("v1 = "+Arrays.toString(vector1)+", v2 = "+Arrays.toString(vector2));      
               // System.out.println("sim1 = "+similarity_check);
                return similarity_check > sim1_threshold;
                                
            } catch (Exception ex) {
                Logger.getLogger(TriasRunner.class.getName()).log(Level.SEVERE, null, ex);
            }
            return false;
    }

    private static boolean areGroupOfSitesSimilar(String[] src_triConcept_Sites, String[] other_triConcept_Sites ) {
        double threshold             = 0.75;
        double others_cardinal       = 0;
        double sim2                  = 0;        
        LongestCommonSubsequence lcs = new LongestCommonSubsequence();
        String src_sites             = buildSiteString(src_triConcept_Sites);
        String other_sites           = buildSiteString(other_triConcept_Sites);               
        // System.out.println(src_sites);
        // System.out.println(other_sites);        
        //double Sim = lcs.distance(src_sites, other_sites);
          double Sim = lcs.length(src_sites, other_sites);
        // normalized 
        others_cardinal = (double) other_triConcept_Sites.length;
        sim2 = Sim/others_cardinal;  
        //System.out.println(sim2);
      //  System.out.println("sim2 = "+sim2);
        return (threshold < sim2);
    }

    private static String buildSiteString(String[] strArr) {
        StringBuilder strBuilder = new StringBuilder();
        for (int i = 0; i < strArr.length; i++) {
            strBuilder.append(strArr[i]);
        }
        String newString = strBuilder.toString();
        return newString;
    }

    private static void gridMiner(String[] groupOfTasks, String[] triConcept_Sites, double min_supp, double min_conf) {
            try {
                 HashMap<String, Integer> triconcept_tasksVectors = buildGroupOfTasksVector(groupOfTasks);
                /*
                for(Entry<String, Integer> entry : triconcept_tasksVectors.entrySet()) {
                String cle = entry.getKey();
                // System.out.println(triconcept_tasksVectors.toString());
                
                System.out.println(cle);
                Integer valeur = entry.getValue();
                // traitements
                }
            */  } catch (Exception ex) {
                Logger.getLogger(TriasRunner.class.getName()).log(Level.SEVERE, null, ex);
            }
    }

    private static HashMap<String, Integer> buildGroupOfTasksVector(String[] groupOfTasks) throws FileNotFoundException, Exception {            
        List valid = Arrays.asList(groupOfTasks);
        // init Vector              
        HashMap<String, Integer> h = new HashMap<String, Integer>(); 
        for (int i = 1; i <= TriasRunner.gridFilesCount; i++) {
            h.put("F"+i, 0);
        }              
            try {
                FileInputStream fstream = new FileInputStream("fixture.input");
                BufferedReader br = new BufferedReader(new InputStreamReader(fstream));
                String strLine;
                while ((strLine = br.readLine()) != null) {        
                    Pattern pattern       = Pattern.compile("(\\d+)\\s+"); //$NON-NLS-1$
                    Matcher matcher       = pattern.matcher(strLine);
                    List<Integer> numbers = new LinkedList<Integer>();
                    while (matcher.find()) {
                        numbers.add(Integer.valueOf(matcher.group(1)));
                    }
                    Integer[] output = numbers.toArray(new Integer[numbers.size()]);
                    //System.out.println(Arrays.toString(output));
                    // triConcepts = runner.readConceptsFromFile();
                    // if(Arrays.asList(groupOfTasks).contains(Integer.toString(output[0]))) {
                        if(valid.contains(Integer.toString(output[0]))) {
                            h.put("F"+output[1], h.get("F"+output[1]) + 1);   
                            //System.out.println(strLine);
                        }
                   //}
                }
            } catch (IOException ex) {
                Logger.getLogger(TriasRunner.class.getName()).log(Level.SEVERE, null, ex);
            }        
        //To change body of generated methods, choose Tools | Templates.        
        return h;
    }

    private static void buildTriadicAssociationRules(String[] FilesOfRA, String[] tasks, String[] sites, int cardinalOfSimilarTC) {
        double min_supp = 0.4;
        double min_conf = 0.4;
        String premise;
        String conclusion;
        for (String FilesOfRA1 : FilesOfRA) {
            java.util.List<String> list = new ArrayList<String>(Arrays.asList(FilesOfRA));
            list.remove(FilesOfRA1);
            String[] new_array      = list.toArray(new String[0]);
            premise                 = FilesOfRA1;
            String conclusionString = Arrays.toString(new_array);
            conclusion              = conclusionString.substring(conclusionString.indexOf("[")+1, conclusionString.indexOf("]"));

            ArtMiner_bgrt triadic_rule = new ArtMiner_bgrt(premise, conclusion, FilesOfRA, tasks, sites);

            System.out.println(triadic_rule.frequentTriconcept + "**** \n");
            System.out.println("TAR: " + premise + " -> " + conclusion);
            // check if it belongs to BGRT, add it if YES 
            // compute Triadic support& confidence
            double supp_c = triadic_rule.computeConditionalTriadicSupport(cardinalOfSimilarTC);
            double conf_c = triadic_rule.computeConditionalTriadicConfidence();

            if(supp_c > min_supp && conf_c > min_conf) {
                triadic_rule.addToBGRT();
                System.out.println("    BGRT: " + FilesOfRA1 + "->" + Arrays.toString(new_array)+"\n"); 
            }
            // Create association rule object with parent_TC, premise, condition, support, cnfidence and BGRT attribute
        }
    }


}
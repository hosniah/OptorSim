package org.edg.data.replication.optorsim.optor;

import java.sql.ResultSet;
import org.edg.data.replication.optorsim.reptorsim.MySQLAccess;
import java.util.*;


/**
 *
 * @author hosniah
 */
public class TriARModel {
    
    public double bestConfidence;
    public List<Integer> RTrep;
    public String[] File_To_replicate;
    public MySQLAccess dao;
    public int site_id;


    public TriARModel() {
         this.dao            = MySQLAccess.getDbCon();
         this.bestConfidence = 0;
         RTrep = new ArrayList<Integer>();
    }

    /** Query the tabe trias.bgrt with ordering on tasks_count, sites_count to prepare RAT set.
    * 
    */
    private void loadSortedBgrt() {
        try {
            ResultSet res;
            res = this.dao.query("Select * from bgrt order by tasks_count, sites_count DESC");
            while(res.next()) {
                int bgrt_id       = (int) res.getObject("id");
                String presmisse  = (String) res.getObject("premisse");
                String conclusion = (String) res.getObject("conclusion"); 
                int supp_c        = (int) res.getObject("supp_c");                                 
                int conf_c        = (int) res.getObject("conf_c");
                // @TODO add BGRT RAT to an arraylist
                
                triadicRuleWorthReplication(conf_c,  bgrt_id);

            }  
        } catch (Exception e) {
            e.printStackTrace();
        }  
    }
    
    private void triadicRuleWorthReplication(int triadicRuleConfidence, int bgrt_id) {
        if (triadicRuleConfidence > this.bestConfidence) {
            //Store the id of the id of Triadic rule worth replication
            this.RTrep.add(bgrt_id);
            this.bestConfidence = triadicRuleConfidence;
        }        
    }
}

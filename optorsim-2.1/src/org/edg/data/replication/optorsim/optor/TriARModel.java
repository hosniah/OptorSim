
import java.sql.ResultSet;
import org.edg.data.replication.optorsim.reptorsim.MySQLAccess;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author hosniah
 */
public class TriARModel {
    
    public double bestConfidence;
    public String[] RTrep;
    public String[] File_To_replicate;
    public MySQLAccess dao;


    public TriARModel() {
        
    }

    /** Query the tabe trias.bgrt with ordering on tasks_count, sites_count to prepare RAT set.
    * 
    */
    private void loadSortedBgrt() {
            ResultSet res  = this.dao.query("Select * from bgrt order by tasks_count, sites_count DESC");
            while(res.next()) {
                 bgrt_id    = (int) res.getObject("id");
                 presmisse  = (String) res.getObject("premisse");
                 conclusion = (String) res.getObject("conclusion"); 
                 supp_c     = (String) res.getObject("supp_c");                                 
                 conf_c     = (String) res.getObject("conf_c");                                 
                 
                 // @TODO add BGRT RAT to an arraylist
            }  
    }
}

package org.edg.data.replication.optorsim.optor;

import java.sql.ResultSet;
import org.edg.data.replication.optorsim.reptorsim.MySQLAccess;
import java.util.*;
import org.edg.data.replication.optorsim.infrastructure.GridSite;


/**
 *
 * @author hosniah
 */
public class TriARModel {
    
    public float bestConfidence;
    public List<Integer> RTrep;
    public String[] File_To_replicate;
   public String[] RATSITENAMES;
    public MySQLAccess dao;
    public int site_id;
    public String current_site;
    public List<Integer> all_site_ids;

    public TriARModel(GridSite site) {
        this.dao            = MySQLAccess.getDbCon();
        this.bestConfidence = 0;
        RTrep        = new ArrayList<Integer>();
        all_site_ids = new ArrayList<Integer>();
        this.site_id = 0;//getGridSiteIdByname(site.toString());
        this.current_site    = site.toString();
       // System.out.println("current optimiser site is: "+this.site_id);
    }

    private int getGridSiteIdByname(String site_name) {
        int site_id = 0;
        ResultSet res;
        try {
            res = this.dao.query("Select * from allvisitedsites where label = '"+site_name+"';");
            while(res.next()) {
                site_id       =  (int) res.getObject("id"); 
                this.all_site_ids.add(site_id);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } 
        return site_id;
    }
     
    private String getGridSiteNameById(String site_id) {
        String site_label = new String();
        ResultSet res;
        try {
            res = this.dao.query("Select * from allvisitedsites where id = '"+site_id+"';");
            while(res.next()) {
                site_label       =  (String) res.getObject("label"); 
                //this.all_site_ids.add(site_id);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } 
        return site_label;
    }
    
    private boolean isCurrentSiteAmongTriadicRuleSites(List<String> elephantList) {
       /*
        Iterator<Integer> it = all_site_ids.iterator();
        while(it.hasNext()) {
            Object obj = it.next();
            //Do something with obj
            if (elephantList.contains(this.site_id)) {
                return true;
            }
        }
        */
        return false;
    }
    
    public boolean TARSitesContainsCurrentSite_byName(List<String> elephantList) {
        if (elephantList.contains(this.current_site)) {
            return true;
        } else {
            return false;
        }        
    }
    
    public String[] getRATStiesNameasListFromIds(List<String> idsList) {
        List<String> sitesNameas = new ArrayList<String>();
        for (int i = 0; i < idsList.size(); i++)
        {
            
            String sitename = getGridSiteNameById(idsList.get(i));
            sitesNameas.add(sitename);
        }
        String[] stringArray = sitesNameas.toArray(new String[0]);
        return stringArray;
    }
    
    /** Query the tabe trias.bgrt with ordering on tasks_count, sites_count to prepare RAT set.
    * 
    */
    public void loadSortedBgrt() {
        try {
            ResultSet res;
            res = this.dao.query("SELECT * FROM `bgrt` GROUP BY `premisse`, `conclusion` Order BY `tasks_count` , `sites_count`, `conf_c` DESC");
            while(res.next()) {
                int bgrt_id         = (int) res.getObject("id");
                String presmisse    = (String) res.getObject("premisse");
                String conclusion   = (String) res.getObject("conclusion");
                float supp_c        = (float) res.getObject("supp_c");
                float conf_c        = (float) res.getObject("conf_c");
                String RT_sites     = (String) res.getObject("rt_sites");                
                // Is the current site among RT group of sites
                List<String> elephantList = Arrays.asList(RT_sites.split(","));     
               // System.out.println("************ "+this.site_id+" current RT sites are: "+RT_sites);
                // if (elephantList.contains(this.site_id)) {
                // if (isCurrentSiteAmongTriadicRuleSites(elephantList) != true) {
                
                String[] RT_sitesNames = this.getRATStiesNameasListFromIds(elephantList);
                List<String> siteNamesList = Arrays.asList(RT_sitesNames); 
                if (TARSitesContainsCurrentSite_byName(siteNamesList) != true) {
                    System.out.println("Skipping RT: "+RT_sites);
                   //  System.out.println("Skipping RT: "+RT_sites+". The site #"+this.site_id+" is not among its group of grid sites.");
                } else {
                    // Here comes the issue
                    System.out.println("current RT sites are: "+RT_sites);
                    triadicRuleWorthReplication(conf_c,  bgrt_id);
                }
            }  
        } catch (Exception e) {
            e.printStackTrace();
        }  
    }
    
    private void triadicRuleWorthReplication(float triadicRuleConfidence, int bgrt_id) {
        // add BGRT RAT to an arraylist
        if (triadicRuleConfidence > this.bestConfidence) {
            //Store the id of the id of Triadic rule worth replication
            this.RTrep.add(bgrt_id);
            this.bestConfidence = triadicRuleConfidence;
        }        
    }
}

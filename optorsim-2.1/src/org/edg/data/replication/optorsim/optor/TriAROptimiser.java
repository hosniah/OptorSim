package org.edg.data.replication.optorsim.optor;

import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import org.edg.data.replication.optorsim.infrastructure.DataFile;
import org.edg.data.replication.optorsim.infrastructure.GridSite;
import org.edg.data.replication.optorsim.infrastructure.StorageElement;
import org.edg.data.replication.optorsim.reptorsim.ReplicaManager;

/**
 * This optimiser replicates files at all times unless it is not possible
 * due to all files on the local SE being pinned or masters. To create space
 * for the replicated files it deletes the oldest files on the local SE,
 * that is, the files which were created least recently.
 * <p>
 * Copyright (c) 2016 CERN, Ahmed hosni.
 * For license conditions see LICENSE file or
 * <a href="http://www.edg.org/license.html">http://www.edg.org/license.html</a>
 * <p>
 * @since JDK1.4
 */
public class TriAROptimiser extends ReplicatingOptimiser {

    protected TriAROptimiser( GridSite site) {
        super(site);
        //Mandatory to retrieve correlated files at the begining of process
        getCorrelatedBestFiles();
    }

    /**
     * Returns the DataFile chosen by {@link LruStorageElement#filesToDelete}.
     */
    protected List chooseFilesToDelete( DataFile file, StorageElement se) {
	    return se.filesToDelete(file);
    }

    public DataFile[] getBestFile(String[] lfns, float[] fileFraction) {
    	DataFile[] files;
        /*
        TriARModel optimiserModel;        
        // please take a look to WorkerNode->run, this may be helpful for better implementtion        
                // Pack the logical file name into the expected structure:
                String lfn = "aa";
                String[] _logicalfilenames = new String[1];
            	_logicalfilenames[0] = lfn;
            	float[] fileFractions = new float[1];
            	fileFractions[0] = (float)1.0;
    	
        //System.out.println("current optimiser site is: "+_site);
        optimiserModel = new TriARModel(_site);
        // Run Datamining algorithms first
        optimiserModel.loadSortedBgrt();
     //   System.out.println("\nFiles ****: "+Arrays.toString(lfns));
  
        String[] tty = optimiserModel.triadicAssociationToBeUsedForRep();
        if(tty.length > 0) {
            
                String[] lfns2 = optimiserModel.triadicAssociationToBeUsedForRep();
                System.out.println("\nFiles ****: "+Arrays.toString(lfns2));
        }*/
        
    	files = super.getBestFile( lfns, fileFraction);

                StorageElement closeSE = _site.getCloseSE();                
		ReplicaManager rm = ReplicaManager.getInstance();		
		if( closeSE != null) {
			for( int i = 0; i < files.length; i++) {			
				StorageElement se = files[i].se();		
				// skip over any file stored on the local site
				if( se.getGridSite() == _site)
					continue;	
				
				DataFile replicatedFile;
				// Check to see if there is a possibility of replication to close SE
				if(!closeSE.isTherePotentialAvailableSpace(files[i]))
					continue;				
				// Loop trying to delete a file on closeSE to make space
				do {					
					// Attempt to replicate file to close SE.
					replicatedFile = rm.replicateFile( files[i], closeSE);					
					// If replication worked, store it and move on to next file (for loop)
					if( replicatedFile != null) {
                                            files[i].releasePin();
						files[i] = replicatedFile;
						break;
					}					
					// If replication didn't work, try finding expendable files.
					List expendable = chooseFilesToDelete( files[i], closeSE);
					// didn't find one, fall back to remoteIO
					if( expendable == null) {
						break;
					}					
					for (Iterator it = expendable.iterator(); it.hasNext() ;){
						rm.deleteFile( (DataFile) it.next());
					}
				} while( replicatedFile == null);				
			} // for
		}
		return files;
	}

    public void getCorrelatedBestFiles() {
        TriARModel optimiserModel;        
        // please take a look to WorkerNode->run, this may be helpful for better implementtion        
        // Pack the logical file name into the expected structure:
    
    	
        //System.out.println("current optimiser site is: "+_site);
        optimiserModel = new TriARModel(_site);
        // Run Datamining algorithms first
        optimiserModel.loadSortedBgrt();
   
        /* rebuild the list of files lfns
         *  Given the output of datamining process
         */
        String[] tty = optimiserModel.triadicAssociationToBeUsedForRep();
        if(tty.length > 0) {
            
                String[] lfns2 = optimiserModel.triadicAssociationToBeUsedForRep();
                //System.out.println("|==== correlated Files to be fetched : \n "+Arrays.toString(lfns2));
                System.out.println("\n |==== correlated Files to be fetched : ");
                for (String s: lfns2) {           
                    //Do your stuff here
                    System.out.println("|============ "+s); 
  
                    String[] _logicalfilenames = new String[1];
                    _logicalfilenames[0]       = s;
                    float[] fileFractions      = new float[1];
                    fileFractions[0] = (float)1.0;
                    getBestFile(_logicalfilenames, fileFractions);
                                //replicaOptimiser.getCorrelatedBestFiles();
                }
        }
    }
}

package org.edg.data.replication.optorsim.optor;

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
    }

    /**
     * Returns the DataFile chosen by {@link LruStorageElement#filesToDelete}.
     */
    protected List chooseFilesToDelete( DataFile file, StorageElement se) {
	    return se.filesToDelete(file);
    }

    public DataFile[] getBestFile(String[] lfns, float[] fileFraction) {
    	DataFile[] files;
    	
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
}

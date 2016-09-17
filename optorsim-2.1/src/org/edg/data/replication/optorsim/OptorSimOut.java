package org.edg.data.replication.optorsim;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import org.edg.data.replication.optorsim.infrastructure.OptorSimParameters;

/**
 * Prints output to screen and/or GUI as required. If the GUI is not 
 * used, it prints to stdout; if the GUI is used, it prints to stdout and
 * to the GUI terminal output window.
 * 
 * Copyright (c) 2002 CERN, ITC-irst, PPARC, on behalf of the EU DataGrid.
 * For license conditions see LICENSE file or
 * <a href="http://www.edg.org/license.html">http://www.edg.org/license.html</a>
 * <p>
 */
public abstract class OptorSimOut {
   
	/**
	 * Method to add lines of output to the terminal screens.
	 * @param newText The text which is to be displayed
	 */
	public static void println(String newText)
	{
	   /* if (useGui)
		*    output text to terminal tab and standard output
		* else
		*    output text to standard output only
		*/
	   if (OptorSimParameters.getInstance().useGui())
	   {
		  OptorSimGUI.addLine(newText);
	   }
		System.out.println(newText);
                //log traces to a text file
                try(FileWriter fw = new FileWriter("sim_traces", true);
                    BufferedWriter bw = new BufferedWriter(fw);
                    PrintWriter out = new PrintWriter(bw))
                {
                    out.println(newText);
                } catch (IOException e) {
                    //exception handling left as an exercise for the reader
                }
	}
 
}

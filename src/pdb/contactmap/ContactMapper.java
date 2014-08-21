package pdb.contactmap;

import io.resources.ResourceContentAsString;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.util.List;
import java.util.Map;

import pair.Pair;
import pdb.contacts.ClosestAtomPairContactCriteria;
import pdb.contacts.ContactCriteria;
import pdb.structures.Chain;
import pdb.structures.SimplePdbReader;
import pdb.structures.SpacePoint;
import cmdGA2.CommandLine;
import cmdGA2.NoArgumentOption;
import cmdGA2.OptionsFactory;
import cmdGA2.SingleArgumentOption;
import cmdGA2.returnvalues.OutfileValue;

public class ContactMapper {

	public static void main(String[] args) {

		////////////////////////////////////////////////////////////////////////
		// Creates command line object
		CommandLine cmd = new CommandLine();
		////////////////////////////////////////////////////////////////////////
		
		////////////////////////////////////////////////////////////////////////
		// Add command line options
		SingleArgumentOption<InputStream> inOpt= OptionsFactory.createBasicInputStreamArgument(cmd);
		SingleArgumentOption<PrintStream> outOpt= OptionsFactory.createBasicPrintStreamArgument(cmd);
		SingleArgumentOption<File> drawOpt= new SingleArgumentOption<File>(cmd, "--draw", new OutfileValue(), null);
		SingleArgumentOption<ContactCriteria> critOpt = new SingleArgumentOption<ContactCriteria>(cmd, "--criteria", new ContactCriteriaValue(), new ClosestAtomPairContactCriteria(6));
		NoArgumentOption exportAtomsOpt = new NoArgumentOption(cmd, "--withAtoms");
		NoArgumentOption exportCoordinates = new NoArgumentOption(cmd, "--withCoordinates");
		NoArgumentOption helpOpt= new NoArgumentOption(cmd, "--help");
		////////////////////////////////////////////////////////////////////////
		
		////////////////////////////////////////////////////////////////////////
		// Parse Command line
		cmd.readAndExitOnError(args);
		////////////////////////////////////////////////////////////////////////

		////////////////////////////////////////////////////////////////////////
		// Check for Help
		if (helpOpt.isPresent()) {
			String helpText = (new ResourceContentAsString()).readContents("help", ContactMapper.class);
			System.err.println(helpText);
			System.exit(0);
		}
		////////////////////////////////////////////////////////////////////////

		
		////////////////////////////////////////////////////////////////////////
		// Get values from the command line.
		BufferedReader in = new BufferedReader(new InputStreamReader(inOpt.getValue()));
		PrintStream out = outOpt.getValue();
		ContactCriteria criteria = critOpt.getValue();
		if (criteria==null) {
			System.err.println("Criteria was not parsed correctly");
			System.exit(1);
		}
		////////////////////////////////////////////////////////////////////////
		
		////////////////////////////////////////////////////////////////////////
		// Read PDB
		Map<Character, Chain> pdb = new SimplePdbReader().readPdb(in);
		////////////////////////////////////////////////////////////////////////

		////////////////////////////////////////////////////////////////////////
		// Get Contacts
		List<Pair<SpacePoint, SpacePoint>> contacts = new ContactMapListGenerator().getSpacePointsInContact(pdb, criteria);
		////////////////////////////////////////////////////////////////////////

		////////////////////////////////////////////////////////////////////////
		// Export contacts
		for (Pair<SpacePoint, SpacePoint> pair : contacts) {
			
			StringBuilder sb = new StringBuilder();
			
			sb.append(pair.getFirst().getResidueSequenceNumber());
			sb.append("\t");
			sb.append(pair.getFirst().getResidueName());
			sb.append("\t");
			sb.append(pair.getSecond().getResidueSequenceNumber());
			sb.append("\t");
			sb.append(pair.getSecond().getResidueName());

			if (exportAtomsOpt.isPresent()) {
				sb.append("\t");
				sb.append(pair.getFirst().getAtomSerialNumber());	
				sb.append("\t");
				sb.append(pair.getFirst().getAtomName());
				sb.append("\t");
				sb.append(pair.getSecond().getAtomSerialNumber());
				sb.append("\t");
				sb.append(pair.getSecond().getAtomName());
			}
			
			if (exportCoordinates.isPresent()) {
				sb.append("\t");
				sb.append(pair.getFirst().getX());
				sb.append("\t");
				sb.append(pair.getFirst().getY());
				sb.append("\t");
				sb.append(pair.getFirst().getZ());
				sb.append("\t");
				sb.append(pair.getSecond().getX());
				sb.append("\t");
				sb.append(pair.getSecond().getY());
				sb.append("\t");
				sb.append(pair.getSecond().getZ());
			}
			out.println();
			
		}
		////////////////////////////////////////////////////////////////////////

		////////////////////////////////////////////////////////////////////////
		// Draw Image
		if (drawOpt.isPresent()) {
			
			
			
		}
		////////////////////////////////////////////////////////////////////////
		
	}

}

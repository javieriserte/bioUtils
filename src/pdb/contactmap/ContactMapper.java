package pdb.contactmap;

import graphics.profile.PngWriter;
import io.resources.ResourceContentAsString;

import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import pair.Pair;
import pdb.contacts.ClosestAtomPairContactCriteria;
import pdb.contacts.ContactCriteria;
import pdb.structures.Chain;
import pdb.structures.SimplePdbReader;
import pdb.structures.SpacePoint;
import stringeditor.StringEditor;
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
		NoArgumentOption exportChainOpt = new NoArgumentOption(cmd, "--withChains");
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
			
			List<String> dataToExport = new ArrayList<String>();
			
			dataToExport.add(String.valueOf(pair.getFirst().getResidueSequenceNumber()));
			dataToExport.add(String.valueOf(pair.getFirst().getResidueName()));
			dataToExport.add(String.valueOf(pair.getSecond().getResidueSequenceNumber()));
			dataToExport.add(String.valueOf(pair.getSecond().getResidueName()));
			
			if (exportChainOpt.isPresent()) {
				dataToExport.add(String.valueOf(pair.getFirst().getChainIdentifier()));
				dataToExport.add(String.valueOf(pair.getSecond().getChainIdentifier()));
			}

			if (exportAtomsOpt.isPresent()) {
				dataToExport.add(String.valueOf(pair.getFirst().getAtomSerialNumber()));
				dataToExport.add(String.valueOf(pair.getFirst().getAtomName()));
				dataToExport.add(String.valueOf(pair.getSecond().getAtomSerialNumber()));
				dataToExport.add(String.valueOf(pair.getSecond().getAtomName()));
			}
			
			if (exportCoordinates.isPresent()) {
				dataToExport.add(String.valueOf(pair.getFirst().getX()));
				dataToExport.add(String.valueOf(pair.getFirst().getY()));
				dataToExport.add(String.valueOf(pair.getFirst().getZ()));
				dataToExport.add(String.valueOf(pair.getSecond().getX()));
				dataToExport.add(String.valueOf(pair.getSecond().getY()));
				dataToExport.add(String.valueOf(pair.getSecond().getZ()));
			}
			
			out.println(StringEditor.join(dataToExport, "\t"));
			
		}
		out.flush();
		out.close();
		////////////////////////////////////////////////////////////////////////

		////////////////////////////////////////////////////////////////////////
		// Draw Image
		if (drawOpt.isPresent()) {
			
			ContactMapDrawer drawer = new ContactMapDrawer();
			
			BufferedImage image = drawer.drawMap(pdb, contacts);
			
			PngWriter writer = new PngWriter();
			
			try {
				writer.write(drawOpt.getValue(), image);
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
			
		}
		////////////////////////////////////////////////////////////////////////
		
	}

}

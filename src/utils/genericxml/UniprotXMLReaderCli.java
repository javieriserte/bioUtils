package utils.genericxml;

import java.io.File;
import java.io.IOException;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import cmdGA2.CommandLine;
import cmdGA2.OptionsFactory;
import cmdGA2.SingleArgumentOption;
import cmdGA2.returnvalues.InfileValue;

/**
 * Reads a XML file from uniprot in a generic way.
 * Currently contains an example implementation for a specific task.
 * Use the code as template for other tasks.
 * 
 * @author javier iserte
 *
 */
public class UniprotXMLReaderCli {

	public static void main(String[] args) {
		
		////////////////////////////////////////////////////////////////////////
		// Command line
		CommandLine cmd = new CommandLine();
		////////////////////////////////////////////////////////////////////////

		////////////////////////////////////////////////////////////////////////
		// Add Command line arguments
		SingleArgumentOption<File> inOpt = new SingleArgumentOption<File>(cmd, "--xml", new InfileValue(), null);
		SingleArgumentOption<PrintStream> outOpt = OptionsFactory.createBasicPrintStreamArgument(cmd);
		////////////////////////////////////////////////////////////////////////
		
		cmd.readAndExitOnError(args);
		
	    DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
	    factory.setValidating(true);
	    factory.setIgnoringElementContentWhitespace(true);
	    try {
	        DocumentBuilder builder = factory.newDocumentBuilder();
	        builder.setErrorHandler(null);
			File file = inOpt.getValue();
			PrintStream out = outOpt.getValue();
	        Document doc = builder.parse(file);

	        Node entry = getRelevantEntryForUniprot(doc);
	 
	        if (entry==null) {
	        	System.err.println("No Swiss Prot entry for "+file.getName());
	        	System.exit(1);
	        }
	        
	        String entryName = getEntryName(entry);
	        
	        String entryProteinNames = getProteinNames(entry);
	        
	        String entryGeneNames = getGeneNames(entry);
	        
	        List<String> entryComments = getComments(entry);
	        
	        out.println("\\section{Resultados para: "+ file.getName()+"}");
	        out.println("\\subsection{Nombre de registro Uniprot:" + entryName+"}");
	        out.println("\\subsection{Nombre de los genes:"+"}");
	        out.println(entryGeneNames);
	        out.println("\\subsection{Nombre de las proteinas:"+"}");
	        out.println(entryProteinNames);
	        out.println("\\subsection{Comentarios"+"}");
	        for (String string : entryComments) {
	        	System.out.println(string);	
			}
	        out.println("\\newpage");
	        
	        
	        
	        
	        

	     
	        
	    } catch (ParserConfigurationException e) {
	    	System.out.println("Error");
	    } catch (SAXException e) {
	    } catch (IOException e) { 
	    }
	    

	}

	private static List<String> getComments(Node entry) {
		// <comment type="function"><text evidence="6 7">Cation channel with high affinity for sodium, which is gated by extracellular protons and inhibited by the diuretic amiloride. Generates a biphasic current with a fast inactivating and a slow sustained phase. In sensory neurons is proposed to mediate the pain induced by acidosis that occurs in ischemic, damaged or inflamed tissue. May be involved in hyperalgesia. May play a role in mechanoreception. Heteromeric channel assembly seems to modulate channel properties.</text>
		
		List<String> result = new ArrayList<>();

		List<Node> commentNodes = getNamedChildNodes(entry, "comment");
		
		for (Node node : commentNodes) {
			StringBuilder sb = new StringBuilder();
			Node typeNode = node.getAttributes().getNamedItem("type");
			
			if (typeNode !=null) {
				sb.append("\\subsubsection{"+typeNode.getNodeValue()+"}");
			}
			
			List<Node> textNodes = getNamedChildNodes(node, "text");
			
			boolean emptyTextNode=true;
			for (Node node2 : textNodes) {
			
				emptyTextNode = emptyTextNode & node2.getTextContent().trim()=="";
				sb.append(System.getProperty("line.separator")+ node2.getTextContent() );
				
			}
			if (!emptyTextNode) {
				result.add(sb.toString());
			}
			
		}
	
		return result;
		
	}

	private static String getGeneNames(Node entry) {
		
		//<gene><name type="primary">ASIC3</name><name type="synonym">ACCN3</name><name type="synonym">SLNAC1</name><name type="synonym">TNAC1</name>
		
		StringBuilder result = new StringBuilder();
		List<Node> geneNodes = getNamedChildNodes(entry, "gene");
		
		if (geneNodes.size()==0) { return ""; }
			
		Node geneNode = geneNodes.get(0);
			
		List<Node> names = getNamedChildNodes(geneNode, "name");
			
		if (names.size()==0) {return "";}
		
		boolean firstname =true;
		for (Node node : names) {
			Node a = node.getAttributes().getNamedItem("type");
			if (!firstname) {
				result.append(", ");
			}
			firstname=false;
			if (a!=null) {result.append(a.getNodeValue()+": ");}
			result.append(node.getTextContent());
		}
		return result.toString();
	}

	private static String getProteinNames(Node entry) {
		//   <protein><recommendedName><fullName>Acid-sensing ion channel 3</fullName><shortName>ASIC3</shortName><shortName>hASIC3</shortName>
		StringBuilder result = new StringBuilder();
		List<Node> proteinNodes = getNamedChildNodes(entry, "protein");
		
		if (proteinNodes.size()==0) { return ""; }
			
		Node proteinNode = proteinNodes.get(0);
			
		List<Node> recomendedNames = getNamedChildNodes(proteinNode, "recommendedName");
			
		if (recomendedNames.size()==0) {return "";}
		
		List<Node> fullnames = getNamedChildNodes(recomendedNames.get(0), "fullName");
		
		if (fullnames.size()==0) {return "";}
		result.append(fullnames.get(0).getTextContent());
		
		List<Node> shortNames = getNamedChildNodes(recomendedNames.get(0), "shortName");
		
		for (Node node : shortNames) {
			result.append(", ");
			result.append(node.getTextContent());
		}
		return result.toString();

	}
	
	private static List<Node> getNamedChildNodes(Node entry, String needleName) {
		List<Node> result = new ArrayList<Node>();
		
		for (int i = 0; i < entry.getChildNodes().getLength() ; i++) {
			Node currentItem = entry.getChildNodes().item(i);
			if (currentItem.getNodeName().equals(needleName)) {
				
				result.add(currentItem);
				
			}
			
		}
		
		return result;
		
	}

	private static String getEntryName(Node entry) {
		
		if (entry== null || entry.getChildNodes()==null) {
			return null;
		}
		
		for (int i = 0; i < entry.getChildNodes().getLength() ; i++) {
			
			if (entry.getChildNodes().item(i).getNodeName().equals("name")) {
				return entry.getChildNodes().item(i).getTextContent();
			}
			
		}
		
		return "";
	}

	private static Node getRelevantEntryForUniprot(Document doc) {
        Node rootUniprot = doc.getChildNodes().item(0);
        rootUniprot.getNodeName();
        NodeList uniprotElements = rootUniprot.getChildNodes();
        for (int i =0; i< uniprotElements.getLength(); i++) {
	        
        	
        	Node node = uniprotElements.item(i);
        	
        	if (node.getNodeName().equals("entry") && node.getAttributes().getNamedItem("dataset").getNodeValue().equals("Swiss-Prot")) {
        		return node;
        	}
        	
        }
        return null;
	}

}

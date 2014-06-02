package utils.mutualinformation.misticmod.tocytoscape;

import io.onelinelister.OneLineListReader;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.util.List;
import java.util.TreeSet;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

import utils.mutualinformation.misticmod.MI_Position;
import utils.mutualinformation.misticmod.MI_PositionLineParser;
import cmdGA2.CommandLine;
import cmdGA2.OptionsFactory;
import cmdGA2.SingleArgumentOption;


/**
 * MIDataToCytoscapeConverter reads a MI data file and creates and
 * XML output compatible to Cytoscape. 
 * 
 * @author javier
 *
 */
public class MIDataToCytoscapeConverter {

	public static void main(String[] args) {
		
		/////////////////////////////////////////////
		// Create command line
		CommandLine cmd = new CommandLine();
		/////////////////////////////////////////////
		
		/////////////////////////////////////////////
		// Add commmand line options
		SingleArgumentOption<InputStream> inOpt = OptionsFactory.createBasicInputStreamArgument(cmd);
		SingleArgumentOption<PrintStream> outOpt = OptionsFactory.createBasicPrintStreamArgument(cmd);
		/////////////////////////////////////////////
		
		/////////////////////////////////////////////
		// Parse command line
		cmd.readAndExitOnError(args);		
		/////////////////////////////////////////////
		
		/////////////////////////////////////////////
		// Get Command line arguments
		BufferedReader in = new BufferedReader(new InputStreamReader(inOpt.getValue()));
		PrintStream out = outOpt.getValue();
		/////////////////////////////////////////////
		
		OneLineListReader<MI_Position> reader = new OneLineListReader<MI_Position>(new MI_PositionLineParser());
		
		try {
			List<MI_Position> positions = reader.read(in);

			DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
	 
			//////////////////////////////////////////
			// Create the Header root element
			Document doc = docBuilder.newDocument();
			doc.setXmlStandalone(true);
			Node rootGraph = createRootElement(doc);
			doc.appendChild(rootGraph);
			///////////////////////////////////////////
			
			////////////////////////////////////////////
			// Append All Nodes
			TreeSet<Integer> allColumns = new TreeSet<>();
			
			for (MI_Position mi_Position : positions) {
				allColumns.add(mi_Position.getPos1());
				allColumns.add(mi_Position.getPos2());
			}
			
			for (Integer columnNumber : allColumns) {
				
				rootGraph.appendChild(createNewNode(doc, columnNumber));	
				
			}
			////////////////////////////////////////////
			
			///////////////////////////////////////////
			// Append All Edges
			for (MI_Position mi_Position : positions) {
				
				rootGraph.appendChild(createNewEdge(doc, mi_Position));
				
				
			}
			////////////////////////////////////////////
			
			////////////////////////////////////////////
			// Print Out The XML File
			TransformerFactory transformerFactory = TransformerFactory.newInstance();
			Transformer transformer = transformerFactory.newTransformer();
			DOMSource source = new DOMSource(doc);
			StreamResult result = new StreamResult(out);
			transformer.transform(source, result);
			////////////////////////////////////////////
			
			
			
		} catch (IOException | ParserConfigurationException e) {
			e.printStackTrace();
		} catch (TransformerConfigurationException e) {
			e.printStackTrace();
		} catch (TransformerException e) {
			e.printStackTrace();
		}
		
		
	}

	private static Node createNewEdge(Document doc, MI_Position mi_Position) {
		String edgeLabel = mi_Position.getPos1() + " (MI) " +  mi_Position.getPos2();
		Node newNode = createElementWithAttributes(doc, "edge", "label",edgeLabel, "source","-" + String.valueOf(mi_Position.getPos1()), "target","-" + String.valueOf(mi_Position.getPos2()));
		
		newNode.appendChild(createElementWithAttributes(doc, "att", "type","real","name","MutualInfo","value",String.valueOf(mi_Position.getMi())));
		newNode.appendChild(createElementWithAttributes(doc, "att", "type","string","name","canonicalName","value",edgeLabel));
		newNode.appendChild(createElementWithAttributes(doc, "att", "type","string","name","interaction","value","MI","cy:editable","false"));
		newNode.appendChild(createElementWithAttributes(doc, "graphics", "width","1","fill","#000000", "cy:sourceArrow","0","cy:targetArrow","0", "cy:sourceArrowColor","#000000", "cy:targetArrowColor","#000000","cy:edgeLabelFont","SanSerif-0-10","cy:edgeLabel","","cy:edgeLineType","SOLID" ,"cy:curved","STRAIGHT_LINES")); 
		return newNode;
	}

	private static Node createNewNode(Document doc, Integer columnNumber) {
		Node newNode = createElementWithAttributes(doc, "node", "label",String.valueOf(columnNumber),"id","-" + String.valueOf(columnNumber)); 
		newNode.appendChild(createElementWithAttributes(doc, "att", "type","string","name","canonicalName","value",String.valueOf(columnNumber)));
		newNode.appendChild(createElementWithAttributes(doc, "graphics", "type","ELLIPSE","h","35.0", "w","35.0", "fill","#62c8e0", "width","1", "outline","#000000","cy:nodeTransparency","0.72", "cy:nodeLabelFont","Default-0-12","cy:nodeLabel",String.valueOf(columnNumber), "cy:borderLineType","solid"));
		return newNode;
	}

	private static Node createRootElement(Document doc) {
		
		Element rootElement = doc.createElement("graph");
		rootElement.setAttribute("label", "Default NetWork (" + (Math.random() * 1000 + 1) +")");
		rootElement.setAttribute("xmlns:dc","http://purl.org/dc/elements/1.1/");
        rootElement.setAttribute("xmlns:xlink","http://www.w3.org/1999/xlink" );
        rootElement.setAttribute("xmlns:rdf","http://www.w3.org/1999/02/22-rdf-syntax-ns#" );
        rootElement.setAttribute("xmlns:cy","http://www.cytoscape.org" );
        rootElement.setAttribute("xmlns","http://www.cs.rpi.edu/XGMML"  );
        rootElement.setAttribute("directed","1");
        
        Node element;
        rootElement.appendChild(createElementWithAttributes(doc,"att","name","documentVersion", "value","1.1"));
        
        element = createElementWithAttributes(doc, "att","name","networkMetadata");
        element.appendChild(createRdfElement(doc));
        rootElement.appendChild(element);
        
        rootElement.appendChild(createElementWithAttributes(doc,"att","type","string","name","backgroundColor", "value","#ffffff"));
        rootElement.appendChild(createElementWithAttributes(doc,"att","type","real","name","GRAPH_VIEW_ZOOM", "value","0.05"));
        rootElement.appendChild(createElementWithAttributes(doc,"att","type","real","name","GRAPH_VIEW_CENTER_X", "value","0"));
        rootElement.appendChild(createElementWithAttributes(doc,"att","type","real","name","GRAPH_VIEW_CENTER_Y", "value","0"));
        rootElement.appendChild(createElementWithAttributes(doc,"att","type","boolean","name","NODE_SIZE_LOCKED", "value","true"));
        rootElement.appendChild(createElementWithAttributes(doc,"att","type","string","name","__layoutAlgorithm", "value","kamada-kawai-noweight","cy:hidden","true"));

        return rootElement;
	}

	private static Node createElementWithAttributes(Document doc, String name ,String ... attr) {
		Element elem = doc.createElement(name);
		
		for (int i = 0; i < Math.ceil(attr.length / 2d)*2; i=i+2) {
			
				elem.setAttribute(attr[i], attr[i+1]);
			
		}
		
		return elem;
	}

	private static Node createRdfElement(Document doc) {
		Element rdfRoot = doc.createElement("rdf:RDF");
		Element rdfDesc = doc.createElement("rdf:Description");
		rdfDesc.setAttribute("rdf:about", "http://www.cytoscape.org/");
		rdfRoot.appendChild(rdfDesc);
		rdfDesc.appendChild(createNewSimpleElement("dc:type","Protein-Protein Interaction",doc));
		rdfDesc.appendChild(createNewSimpleElement("dc:description","N/A",doc));
		rdfDesc.appendChild(createNewSimpleElement("dc:identifier","N/A",doc));
		rdfDesc.appendChild(createNewSimpleElement("dc:source","http://www.cytoscape.org/",doc));
		rdfDesc.appendChild(createNewSimpleElement("dc:format","Cytoscape-XGMML",doc));
		return rdfRoot;
	}

	private static Node createNewSimpleElement(String name, String textContent,
			Document doc) {
		Element newElement = doc.createElement(name);
		newElement.setTextContent(textContent);
		return newElement ;
	}
	
}

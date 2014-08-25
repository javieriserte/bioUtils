package pdb.contactmap;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.Shape;
import java.awt.geom.AffineTransform;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.TreeSet;

import pair.Pair;
import pdb.structures.Chain;
import pdb.structures.Residue;
import pdb.structures.SpacePoint;

public class ContactMapDrawer {

	public BufferedImage drawMap(Map<Character,Chain> pdb, List<Pair<SpacePoint,SpacePoint>> contactMap) {
		
		////////////////////////////////////////////////////////////////////////
		// Sort residues in pdb by chain id as first key, and residue number as
		// Second key.
		List<Residue> allResidues = getSortedResidues(pdb);
		////////////////////////////////////////////////////////////////////////
		
		////////////////////////////////////////////////////////////////////////
		// Creates a map with residues as Keys and the order number as value. 
		Map<Residue, Integer> orderIndex = getResidueToOrderMap(allResidues);
		////////////////////////////////////////////////////////////////////////
		
		int cellWidth= 10;
		int cellSpacerWidth = 1;
		int extraChainSpaceWidth = 1;
		
		////////////////////////////////////////////////////////////////////////
		// Creates a map for the chain order
		Map<Character,Integer> chainOrder = new TreeMap<Character, Integer>();
		TreeSet<Character> chainsIdSet = new TreeSet<>(pdb.keySet());
		int counter = 0;
		for (Character chainId : chainsIdSet) {
			chainOrder.put(chainId, counter);
			counter++;
		}
		////////////////////////////////////////////////////////////////////////
		
		////////////////////////////////////////////////////////////////////////
		// An array with the size of each chain
		int[] chainLengths = new int[pdb.size()];
		counter = 0;
		int total = 0;
		for (Character chainId : chainsIdSet) {
			total = total + pdb.get(chainId).getResidues().size();			
			chainLengths[counter] = total;
			counter++;
		}
		////////////////////////////////////////////////////////////////////////
		
		////////////////////////////////////////////////////////////////////////
		// Creates a new enpty image for the contact map drawing.
		int width = cellWidth * (allResidues.size()) + cellSpacerWidth * (allResidues.size() -1 + extraChainSpaceWidth * (pdb.size()-1)) ;
		BufferedImage image = new BufferedImage(width, width, BufferedImage.TYPE_INT_RGB);
		////////////////////////////////////////////////////////////////////////

		////////////////////////////////////////////////////////////////////////
		// Draw Background grid
		drawGrid(image,cellWidth,cellSpacerWidth,allResidues.size(),extraChainSpaceWidth,chainLengths);
		////////////////////////////////////////////////////////////////////////
		
		////////////////////////////////////////////////////////////////////////
		// Draw contact points
		addContactPoints(contactMap, pdb, orderIndex, cellWidth, cellSpacerWidth, image, chainOrder);
		////////////////////////////////////////////////////////////////////////
		
		return image;
		
	}
	
	////////////////////////////////////////////////////////////////////////////
	// Private methods
	private Map<Residue, Integer> getResidueToOrderMap(List<Residue> allResidues) {
		Map<Residue,Integer> orderIndex = new HashMap<>();
		int counter = 0;
		for (Residue residue : allResidues) {
			orderIndex.put(residue, counter);
			counter++;
		}
		return orderIndex;
	}

	private List<Residue> getSortedResidues(Map<Character, Chain> pdb) {
		List<Residue> allResidues = new ArrayList<Residue>();
		
		for (Chain chain : pdb.values()) {
			allResidues.addAll(chain.getResiduesCollection());
		}

		Collections.sort(allResidues, getResidueComparator());
		return allResidues;
	}

	private void addContactPoints(
			
			List<Pair<SpacePoint, SpacePoint>> contactMap,
			Map<Character,Chain> pdb,
			Map<Residue, Integer> orderIndex, int cellWidth,
			int cellSpacerWidth, BufferedImage image,
			Map<Character,Integer> chainOrder) {
		Graphics2D g = (Graphics2D) image.getGraphics();
		
		int shapeWidth = 8;
		
		Shape shape = new Ellipse2D.Double(0,0,10,10);
		
		Rectangle2D shapeBounds = shape.getBounds2D();
		
		Point2D shapeCenter = new Point2D.Double(shapeBounds.getCenterX(), shapeBounds.getCenterY());
		
		final AffineTransform translateToCenterTransform = AffineTransform.getTranslateInstance(-shapeCenter.getX(), -shapeCenter.getY());
		double scaleFactor = shapeWidth/Math.min(shapeBounds.getWidth(), shapeBounds.getHeight());
		final AffineTransform scaleTransform = AffineTransform.getScaleInstance(scaleFactor, scaleFactor);
		
		scaleTransform.concatenate(translateToCenterTransform);
		
		Shape centered = scaleTransform.createTransformedShape(shape);
		
		g.setColor(new Color(255, 170, 190));
		
		g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		g.setRenderingHint(RenderingHints.KEY_ALPHA_INTERPOLATION, RenderingHints.VALUE_ALPHA_INTERPOLATION_QUALITY);
		g.setRenderingHint(RenderingHints.KEY_COLOR_RENDERING , RenderingHints.VALUE_COLOR_RENDER_QUALITY);
		g.setRenderingHint(RenderingHints.KEY_FRACTIONALMETRICS, RenderingHints.VALUE_FRACTIONALMETRICS_ON);
		g.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BICUBIC);

		
		for ( Pair<SpacePoint,SpacePoint> contact :contactMap ) {
			
			Residue firstResidue = pdb.get(contact.getFirst().getChainIdentifier()).getResidues().get(contact.getFirst().getResidueSequenceNumber());
			Residue secondResidue = pdb.get(contact.getSecond().getChainIdentifier()).getResidues().get(contact.getSecond().getResidueSequenceNumber());
			
			int p1 = orderIndex.get(firstResidue);
			int p2 = orderIndex.get(secondResidue);
			
			double x1 = p1 * (cellWidth + cellSpacerWidth) + (cellWidth)/2 + chainOrder.get(contact.getFirst().getChainIdentifier());
			double x2 = p2 * (cellWidth + cellSpacerWidth) + (cellWidth)/2 + chainOrder.get(contact.getSecond().getChainIdentifier());

			
			AffineTransform translateToNewCoordinatesTransform = AffineTransform.getTranslateInstance(x1, x2);
			
			Shape finalShape = translateToNewCoordinatesTransform.createTransformedShape(centered);

			g.fill(finalShape);
			
			translateToNewCoordinatesTransform = AffineTransform.getTranslateInstance(x2, x1);
			
			finalShape = translateToNewCoordinatesTransform.createTransformedShape(centered);
			
			g.fill(finalShape);


			
		}
	}

	private void drawGrid(BufferedImage image, int cellWidth, int cellSpacerWidth, int size, int extraChainSpacerWidth, int[] chainEndings) {
		
		Graphics2D g = (Graphics2D)image.getGraphics();
		
		g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		g.setRenderingHint(RenderingHints.KEY_ALPHA_INTERPOLATION, RenderingHints.VALUE_ALPHA_INTERPOLATION_QUALITY);
		g.setRenderingHint(RenderingHints.KEY_COLOR_RENDERING , RenderingHints.VALUE_COLOR_RENDER_QUALITY);
		g.setRenderingHint(RenderingHints.KEY_FRACTIONALMETRICS, RenderingHints.VALUE_FRACTIONALMETRICS_ON);
		g.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BICUBIC);
		
		int width = image.getWidth();
		
		//////////////////////////////////////////////////////
		// Draw white background
		g.setColor(Color.white);
		g.fillRect(0, 0, width, width);
		//////////////////////////////////////////////////////

		//////////////////////////////////////////////////////
		// Draw black rectangles in the diagonal
		g.setColor(Color.black);
		g.setStroke(new BasicStroke(cellSpacerWidth));
		for (int i = 1; i<=size; i++) {
			int chainOrder = this.getChainOrder(chainEndings, i);
			int x1 = (i-1) * (cellWidth + cellSpacerWidth)  + extraChainSpacerWidth * chainOrder;
			g.fillRect(x1, x1, cellWidth, cellWidth);
			
		}
		//////////////////////////////////////////////////////

		//////////////////////////////////////////////////////
		// Draw grid lines in gray
		g.setColor(Color.gray);
		g.setStroke(new BasicStroke(cellSpacerWidth));
		for (int i = 1; i<size; i++) {
			int chainOrder = this.getChainOrder(chainEndings, i);
			int p = i * (cellWidth + cellSpacerWidth) - cellSpacerWidth + extraChainSpacerWidth * chainOrder;
			g.drawLine(0, p, width, p);
			g.drawLine(p, 0, p, width);
		}
		//////////////////////////////////////////////////////		
	}

	private int getChainOrder(int[] chainEndings, int i) {
		
		for (int j=0;j<chainEndings.length ;j++) {
			
			if (i<chainEndings[j]) {
				return j;
			}
			
		}
		
		return 0;
		
	}

	private Comparator<Residue> getResidueComparator() {
		
		return new Comparator<Residue>() {

			@Override
			public int compare(Residue o1, Residue o2) {
				
				int diff = o1.getResNumber() - o2.getResNumber();
				if (diff!=0) {
					return diff;
				} else {
					return o1.getChain().compareTo(o2.getChain());
				}
			}
		};
	}
	// End of private method
	////////////////////////////////////////////////////////////////////////////

	
}

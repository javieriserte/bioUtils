package pdb.contactmap;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import pair.Pair;
import pdb.structures.Chain;
import pdb.structures.Residue;
import pdb.structures.SpacePoint;

public class ContactMapDrawer {

	public BufferedImage drawMap(Map<Character,Chain> pdb, List<Pair<SpacePoint,SpacePoint>> contactMap) {
		
		List<Residue> allResidues = new ArrayList<Residue>();
		
		for (Chain chain : pdb.values()) {
			allResidues.addAll(chain.getResiduesCollection());
		}

		Collections.sort(allResidues, getResidueComparator());
		
		Map<Residue,Integer> orderIndex = new HashMap<>();
		
		int counter = 0;
		for (Residue residue : allResidues) {
			orderIndex.put(residue, counter);
			counter++;
		}
		
		int cellWidth= 10;
		int cellSpacerWidth = 1;
		
		int width = cellWidth * (allResidues.size()) + cellSpacerWidth * (allResidues.size() -1);
		BufferedImage image = new BufferedImage(width, width, BufferedImage.TYPE_INT_RGB);
		
		drawGrid(image,cellWidth,cellSpacerWidth,allResidues.size());
		
		return null;
		
	}

	private void drawGrid(BufferedImage image, int cellWidth, int cellSpacerWidth, int size) {
		
		Graphics2D g = (Graphics2D)image.getGraphics();
		
		int width = image.getWidth();
		
		g.setColor(Color.white);
		
		g.fillRect(0, 0, width, width);

		g.setColor(Color.gray);
		g.setStroke(new BasicStroke(cellSpacerWidth));
		for (int i = 1; i<size; i++) {
			int p = i * (cellWidth + cellSpacerWidth); 
			g.fillRect(0, p, width, p);
		}

		
		g.setColor(Color.gray);
		g.setStroke(new BasicStroke(cellSpacerWidth));
		for (int i = 1; i<size; i++) {
			int p = i * (cellWidth + cellSpacerWidth); 
			g.drawLine(0, p, width, p);
			g.drawLine(p, 0, p, width);
		}
		
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
	
}

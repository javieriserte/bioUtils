//package utils.oneshotscripts;
//
//import java.awt.BasicStroke;
//import java.awt.Color;
//import java.awt.Font;
//import java.awt.Graphics;
//import java.awt.Graphics2D;
//import java.awt.Polygon;
//import java.awt.RenderingHints;
//import java.awt.Shape;
//import java.awt.geom.AffineTransform;
//import java.awt.image.BufferedImage;
//import java.io.File;
//import java.io.FileNotFoundException;
//import java.io.FileOutputStream;
//import java.io.IOException;
//import java.util.ArrayList;
//import java.util.Arrays;
//import java.util.Collections;
//import java.util.List;
//
//import com.sun.image.codec.jpeg.JPEGCodec;
//import com.sun.image.codec.jpeg.JPEGEncodeParam;
//import com.sun.image.codec.jpeg.JPEGImageEncoder;
//
//@SuppressWarnings("restriction")
//public class DrawGraphForBet {
//	Integer[] vac_N_mut = new Integer[]{3194, 3158, 2862, 2530, 2369};
//	Integer[] vac_N_mutLabel = new Integer[]{47, 59, 158, 268, 322 };
//	Integer[] vac_G_mut= new Integer[]{192, 591, 646, 705, 1368, 1415};
//	Integer[] vac_G_mutLabel = new Integer[]{35, 168, 186, 206, 427, 446 };
//	Integer[] nonvac_mut = new Integer[]{192, 330, 340, 408, 415, 421, 436, 450, 462, 558, 639, 714, 1533, 1539, 3163, 3092, 3062, 3044, 3038, 3020, 3014, 3010, 3002, 2995, 2982, 2912, 2862, 2759, 2567, 2530, 2510, 2472, 2368, 2138, 2136, 1907, 1856, 1839, 1817, 1801, 1794, 1696, 1678};
//	String[] vac_N_chars = new String[]{"VVVVVVE", "RKKKRKR", "IIIIVIV", "EEEEDED", "TTTTITI"};
//	String[] vac_G_chars = new String[]{"IIIIVIV", "TAAAAAA", "EEEEEEG", "SSSSSSP", "FFFFFFI", "TTSSTSS"};
//	Integer segment_length = 3414;
//	Integer strain_names_band =200;
//	Integer g_orf_start=90;
//	Integer g_orf_stop=1544;
//	Integer n_orf_start=1642;
//	Integer n_orf_stop=3333;
//	
//
//	RenderingHints renderingHints;	
//
//	public DrawGraphForBet() {
//		super();
//		
//		renderingHints = new RenderingHints(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BICUBIC);
//		renderingHints.add(new RenderingHints(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON));
//		renderingHints.add(new RenderingHints(RenderingHints.KEY_COLOR_RENDERING, RenderingHints.VALUE_COLOR_RENDER_QUALITY ));
//		renderingHints.add(new RenderingHints(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON ));
//		renderingHints.add(new RenderingHints(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY));
//	}
//
//	public static void main(String[] args) {
//		
//		DrawGraphForBet d = new DrawGraphForBet();
//		
//		try {
//			d.exportJPG(new File("B:\\drawforbet.jpg"),d.render());
//		} catch (FileNotFoundException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		} catch (IOException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//		
//	}
//	
//	public BufferedImage render() {
//		
//
//		BufferedImage bi = new BufferedImage(strain_names_band + segment_length, 2000, BufferedImage.TYPE_INT_RGB);
//
//		Graphics2D g = bi.createGraphics();
//		
//
//		
//		g.addRenderingHints(renderingHints);
//		
//		BufferedImage genome = this.drawGenome();
//		
//		BufferedImage features = this.drawFeatures();
//		
//		BufferedImage mutations = this.drawMutations();
//		
//		BufferedImage plots = this.drawPlots();
//		
//		BufferedImage horizontal = this.drawHorizontal();
//		
//		BufferedImage vertical = this.drawVertical();
//
//		BufferedImage names = this.drawnames();
//		
//		
//		g.setColor(Color.white);
//		
//		g.fillRect(0, 0, bi.getWidth(), bi.getHeight());
//		
//		g.drawImage(genome,null, strain_names_band, 700);
//		
//		g.drawImage(features,null, strain_names_band, 1000);
//		
//		g.drawImage(mutations,null, strain_names_band, 0);
//
//		g.drawImage(plots,null, strain_names_band, 1250);
//		
//		g.drawImage(vertical,null,0,1200);
//		
//		g.drawImage(horizontal,null,strain_names_band-50,1800);
//		
//		g.setXORMode(Color.white);
//		g.drawImage(names,null,0,0);
//
//		
//		return bi;
//		
//	}
//	
//	private BufferedImage drawnames() {
//		
//		
//		BufferedImage bi = new BufferedImage(420,  700, BufferedImage.TYPE_INT_RGB);
//		
//		Graphics2D g = bi.createGraphics();
//		g.addRenderingHints(renderingHints);
//		// Background white
//		
//		g.setColor(Color.white);
//
//		g.fillRect(0, 0, bi.getWidth(), bi.getHeight());
//
//		
//		String[] names = new String[]{"XJ#13","XJ#17","XJ#34","XJ#39","XJ#44","XJ#48","Candid#1"};
//		
//		Polygon a = new Polygon(new int[]{10,20,20,30,20,20,10},new int[]{5,5,15,0,-15,-5,-5}, 7);
//		
//		AffineTransform af = new AffineTransform();
//		
//		AffineTransform af2 = new AffineTransform();
//		
//		af.rotate(-Math.PI/2);
//		
//		af2.translate(180, 520);
//		
//		Font f = new Font("Calibri", 1, 35);
//		g.setFont(f);
//		
//		g.setColor(Color.black);
//		
//		for (int i = 0; i<names.length;i++) {
//			
//			if (i>0) {
//
//				
//				AffineTransform af3 = new AffineTransform();
//				
//				af3 = (AffineTransform) af2.clone();
//				
//				af3.concatenate(af);
//				
//				Shape b = af3.createTransformedShape(a);
//
//				g.fill(b);
//				
//				af2.translate(0, -60);
//
//
//			}
//			
//			byte[] bytes = names[i].getBytes();
//			
//			int ad = g.getFontMetrics().bytesWidth(bytes, 0,bytes.length)/2;
//
//			g.drawString(names[i],180-ad,540-420/7*i);
//			
//			
//		}
//		
//		
//		return bi;
//		
//		
//	}
//
//	private BufferedImage drawVertical() {
//
//		int[] bigmarks = new int[]{0,1,2,3,4,5};
//		
//		BufferedImage bi = new BufferedImage(200, 600, BufferedImage.TYPE_INT_RGB);
//
//		Graphics2D g = bi.createGraphics();
//
//		g.addRenderingHints(renderingHints);
//		
//		g.setColor(Color.white);
//		
//		g.fillRect(0, 0, bi.getWidth(), bi.getHeight());
//		
//		g.setStroke(new BasicStroke(5));
//		
//		g.setColor(Color.black);
//		
//		
//		g.drawLine( 180, 50 , 180, 550);
//		
//		Font f = new Font("Calibri", 1, 75);
//		
//		g.setFont(f);
//		
//		for (int i : bigmarks) {
//			
//			g.drawLine( 90, 550-i*100 , 150, 550-i*100);
//			
//			String textnumber = String.valueOf(i);
//			
//			g.drawString(textnumber, 20 ,570-i*100 );
//			
//		}
//		
//		
//		
//		return bi;
//	}
//	
//	private BufferedImage drawHorizontal() {
//
//		int[] bigmarks = new int[]{0,1000,2000,3000};
//		
//		int[] smallmarks = new int[34];
//		
//		for (int i=0;i<34;i++) {
//			
//			smallmarks[i] = (i+1)*100;
//			
//		}
//		
//		BufferedImage bi = new BufferedImage(segment_length+50, 200, BufferedImage.TYPE_INT_RGB);
//
//		Graphics2D g = bi.createGraphics();
//
//		g.addRenderingHints(renderingHints);
//		
//		g.setColor(Color.white);
//		
//		g.fillRect(0, 0, bi.getWidth(), bi.getHeight());
//		
//		
//		g.setStroke(new BasicStroke(5));
//		
//		g.setColor(Color.black);
//		
//		g.drawLine(50, 0, segment_length+50, 0);
//		
//		for (int i : smallmarks) {
//			
//			g.drawLine(50+i , 20,50+ i, 50);
//			
//		}
//		
//		Font f = new Font("Calibri", 1, 75);
//		
//		g.setFont(f);
//		
//		for (int i : bigmarks) {
//			
//			g.drawLine(50+i , 20, 50+i, 80);
//			
//			String textnumber = String.valueOf(i);
//			
//			int ad = g.getFontMetrics().bytesWidth(textnumber.getBytes(), 0, textnumber.length())/2;
//			
//			g.drawString(textnumber, i+50-ad, 140);
//			
//		}
//		
//		return bi;
//	}
//
//	private BufferedImage drawPlots() {
//		BufferedImage bi = new BufferedImage(segment_length, 600, BufferedImage.TYPE_INT_RGB);
//		
//		Graphics2D g = bi.createGraphics();
//		g.addRenderingHints(renderingHints);
//
//		// Background white
//		g.setColor(Color.white);
//		g.fillRect(0, 0, bi.getWidth(), bi.getHeight());
//		
//		List<Integer> allvac = new ArrayList<Integer>();
//		allvac.addAll(Arrays.asList(vac_N_mut));
//		allvac.addAll(Arrays.asList(vac_G_mut));
//		
//		List<Integer> allnon = new ArrayList<Integer>();
//		
//		allnon.addAll(Arrays.asList(nonvac_mut));
//		
//		Collections.sort(allvac);
//		Collections.sort(allnon);
//		
//		int window = 31;
//		int delta = window/2;
//		
//		int[] nvacmut = new int[3414];
//		int[] nnonvacmut = new int[3414];
//		
//		for (int i : allvac) {
//			
//			for (int j=-delta;j<delta;j++) {
//				
//				nvacmut[i+j]++;
//				
//			}
//			
//		}
//		
//		for (int i : allnon) {
//			
//			for (int j=-delta;j<delta;j++) {
//				
//				nnonvacmut[i+j]++;
//				
//			}
//			
//		}
//
//		g.setColor(Color.black);
//		
//		g.setStroke(new BasicStroke(3));
//		
//		for (int i =0; i<nnonvacmut.length; i++) {
//			
//			g.drawLine(i, 500, i,100*(5-nnonvacmut[i]));
//			
//		}
//		
//
//		g.setColor(new Color(240,100,100));
//		
//		g.setStroke(new BasicStroke(6));
//		
//		for (int i =1; i<nvacmut.length; i++) {
//			
//			g.drawLine(i-1, 100*(5-nvacmut[i-1]), i, 100*(5-nvacmut[i]));
//			
//		}
//		
//		return bi;
//	}
//
//	private BufferedImage drawMutations() {
//		BufferedImage bi = new BufferedImage(segment_length, 700, BufferedImage.TYPE_INT_RGB);
//		
//		Graphics2D g = bi.createGraphics();
//		g.addRenderingHints(renderingHints);
//
//		// Background white
//		g.setColor(Color.white);
//		g.fillRect(0, 0, bi.getWidth(), bi.getHeight());
//		
//		
//		List<Integer> labels = new ArrayList<Integer>();
//		
//		List<Integer> pos = new ArrayList<Integer>();
//		
//		List<String> letters = new ArrayList<String>();
//		
//		;
//		
//		for (Integer integer : vac_G_mutLabel) { labels.add(integer); }
//		for (Integer integer : (new reverseArray<Integer>()).reverse(vac_N_mutLabel)) { labels.add(integer); }
//		for (Integer integer : vac_G_mut) { pos.add(integer); }
//		for (Integer integer :(new reverseArray<Integer>()).reverse(vac_N_mut)) { pos.add(integer); }
//		for (String integer : vac_G_chars) { letters.add(integer); }
//		for (String integer : (new reverseArray<String>()).reverse(vac_N_chars)) { letters.add(integer); }
//		
//		BufferedImage drawOneMutation;
//		
//		
////		int sp = (segment_length - labels.size()*50)/(labels.size()-1);
//
//		AffineTransform aft = new AffineTransform();
//
//        aft.translate(0, 550);
//		
//		aft.rotate(-Math.PI/2);
//		
//		int last = 0;
//		
//		for (int i=0; i<labels.size(); i++) {
//			
//			drawOneMutation = this.drawOneMutation(letters.get(i));
//			
//			int current_x = pos.get(i)-25;
//			
//	        int delta = Math.max(current_x-last, 70);
//	        
//	        last = last + delta;
//	        
//			aft.translate(0, delta);
//	        
//			g.drawImage(drawOneMutation, aft,null);
//
//			BufferedImage tx = new BufferedImage(100, 50, BufferedImage.TYPE_INT_RGB);
//			
//			Graphics g2 = tx.getGraphics();
//      	    g2.setColor(Color.white);
//			g2.fillRect(0, 0, tx.getWidth(), tx.getHeight());
//			g2.setFont(new Font("Calibri",1,45));
//			g2.setColor(Color.black);			
//			g2.drawString(String.valueOf(labels.get(i)), 00, 40);
//			
//			g.setXORMode(Color.white);
//			
//			AffineTransform af2 = (AffineTransform)aft.clone();
//			af2.translate(430,0);
//			AffineTransform af3 = new AffineTransform();
//		    af3.rotate(+Math.PI/4);
//			af2.concatenate(af3);
//			//af3.concatenate(af2);
//			
//			g.drawImage(tx,af2,null);
//			
//			// Draw line
//			g.setColor(Color.black);
//			g.setStroke(new BasicStroke(3));
//			
//			
//			g.drawLine(current_x+25, 700, last+25, 550);
//			
//
//		}
//		
//		
//
//		//g.drawImage(drawOneMutation, null, null);
//		
//
//		
//		
//
//		
//		
//		return bi;
//	}
//	
//	private BufferedImage drawOneMutation(String text) {
//
//		BufferedImage bi = new BufferedImage(400,  50, BufferedImage.TYPE_INT_RGB);
//		
//		Graphics2D g = bi.createGraphics();
//		g.addRenderingHints(renderingHints);
//		// Background white
//		g.setColor(new Color(220,220,220));
//		g.fillRect(0, 0, bi.getWidth(), bi.getHeight());
//
//		
//		Polygon a = new Polygon(new int[]{0,20,20,30,20,20,0},new int[]{5,5,15,0,-15,-5,-5}, 7);
//		
//		a.translate(35, 25);
//		
//		Font f = new Font("Calibri", 1, 35);
//		
//		g.setFont(f);
//		
//		g.setColor(Color.black);
//		for (int i = 0; i<text.length();i++) {
//			
//			if (i>0) {
//
//				g.fillPolygon(a);
//
//				a.translate(60, 0);
//				
//
//			}
//			
//			g.drawString(String.valueOf(text.charAt(i)),10+i*60,35);
//			
//		}
//		
//		
//		return bi;
//		
//		
//	}
//	
//
//	
//	public BufferedImage drawFeatures() {
//		
//		String[] names = new String[]{"Signal Peptide", "G1", "G2"};
//		int[] start = new int[]{90,267, 837};
//		int[] ends = new int[]{174,567, 699};
//	
//		BufferedImage bi = new BufferedImage(segment_length, 400, BufferedImage.TYPE_INT_RGB);
//		
//		Graphics2D g = bi.createGraphics();
//		g.addRenderingHints(renderingHints);
//		// Background white
//		g.setColor(Color.white);
//		g.fillRect(0, 0, bi.getWidth(), bi.getHeight());
//		
//		
//		// draw features lines
//		g.setColor(Color.black);
//		g.setStroke(new BasicStroke(6));
//		for(int i=0;i<3;i++) {
//			g.drawLine(start[i], 20 + 20 * (i%2==0?0:1), start[i]+ends[i], 20+ 20 * (i%2==0?0:1));
//		}
//		
//		// draw names of features
//		
//		Font a = new Font("Calibri", 1, 40);
//
//		g.setFont(a);
//		for(int i=0;i<3;i++) {
//			
//			int ad = g.getFontMetrics().charsWidth(names[i].toCharArray(), 0, names[i].length())/2;
//			
//			g.drawString(names[i], start[i]+ends[i]/2-ad, 80);
//			
//		}
//		
//		// draw names of features
//		
//		a = new Font("Calibri", 1, 80);
//
//		g.setFont(a);
//		
//		int adg = g.getFontMetrics().charsWidth("GPC".toCharArray(), 0, 3)/2;
//		int adn = g.getFontMetrics().charsWidth("N".toCharArray(), 0, 1)/2;
//		
//		g.drawString("GPC", g_orf_start + (g_orf_stop-g_orf_start)/2 - adg, 150);
//		g.drawString("N", n_orf_start + (n_orf_stop-n_orf_start)/2 - adn, 150);
//		
//		return bi;
//		
//	}
//	
//	public BufferedImage drawGenome() {
//		
//		BufferedImage bi = new BufferedImage(segment_length, 300, BufferedImage.TYPE_INT_RGB);
//		
//		Graphics2D g = bi.createGraphics();
//		g.addRenderingHints(renderingHints);
//
//		// Background white
//		g.setColor(Color.white);
//		g.fillRect(0, 0, bi.getWidth(), bi.getHeight());
//		
//		g.setColor(new Color(240,127,128));
//		
//		// Arrow for G ORF
//		g.fillRect(g_orf_start, 150-20, g_orf_stop-g_orf_start-50, 40);
//		Polygon arrow = new Polygon(new int[]{g_orf_stop+0,g_orf_stop-50,g_orf_stop-50},new int[]{150,150+50,150-50},3);
//		g.fillPolygon(arrow);
//		
//		// Arrow for N ORF
//		g.fillRect(this.n_orf_start+50, 150-20, n_orf_stop-n_orf_start, 40);
//		arrow = new Polygon(new int[]{n_orf_start+0,n_orf_start+50,n_orf_start+50},new int[]{150,150+50,150-50},3);
//		g.fillPolygon(arrow);
//		
//		// Draw Type II mutations
//		g.setStroke(new BasicStroke(10));
//		g.setColor(new Color(180,80,80));
//		for (int i : vac_N_mut) { g.drawLine(i, 150, i, 50); }
//		for (int i : vac_G_mut) { g.drawLine(i, 150, i, 50); }
//		// Draw Type I mutations
//		g.setStroke(new BasicStroke(4));
//		g.setColor(new Color(80,80,180));
//		for (int i : nonvac_mut) { g.drawLine(i, 150, i, 250); }
//		
//		// Draw line for complete genome
//		g.setColor(new Color(80,80,80,150));
//		g.fillRect(0, 150-10, segment_length, 20);
//		
//		return bi; 
//	}
//	
//	public void 				exportJPG							(File outfile, BufferedImage bi) throws FileNotFoundException, IOException {
//		FileOutputStream out = new FileOutputStream(outfile);
//		JPEGImageEncoder encoder = JPEGCodec.createJPEGEncoder(out);
//		JPEGEncodeParam param = encoder.getDefaultJPEGEncodeParam(bi);
//		param.setQuality(1.0f, false);
//		encoder.setJPEGEncodeParam(param);
//		encoder.encode(bi);
//	}
//
//	
//	public class reverseArray<T> {
//		
//		public T[] reverse(T[] array) {
//			
//			T[] newarray = array.clone();
//			
//			for (int i = 0; i<array.length;i++) {
//			
//				newarray[array.length-i-1] = array[i];
//				
//			}
//			
//			return newarray;
//		}
//	}
//}

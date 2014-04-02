package utils.ConservationImage;

import java.io.File;
import utils.ConservationImage.renderer.ColoredLinesRenderer;
import utils.ConservationImage.renderer.Renderer;
import utils.ConservationImage.renderer.RendererReader;
import utils.ConservationImage.renderer.XYPlotRenderer;
import cmdGA2.returnvalues.InfileValue;
import cmdGA2.returnvalues.ReturnValueParser;

public class RendererValue extends ReturnValueParser<Renderer> {
	///////////////////////////////////////
	// Class constants
	public static final String XY_PLOT_RENDERER = "XY";
	public static final String VERTICAL_LINES_RENDERER = "LINES";
	///////////////////////////////////////
	
	@Override
	public Renderer parse(String token) {
		Renderer renderer = null;
		if (token.equals(RendererValue.XY_PLOT_RENDERER)) {
			renderer = new XYPlotRenderer();
			renderer.setLayout(renderer.getDefaultLayout());
		} else 
		if (token.equals(RendererValue.VERTICAL_LINES_RENDERER)){
			renderer = new ColoredLinesRenderer();
			renderer.setLayout(renderer.getDefaultLayout());
		} else {
			File infile = (new InfileValue()).parse(token);
			if ( infile.exists() ) {
				
				renderer = (new RendererReader()).parse_file(infile);
				
			}
		
		}
		return renderer;

	}
	
	
	

}

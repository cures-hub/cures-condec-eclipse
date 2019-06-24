package de.uhd.ifi.se.decision.management.eclipse.view;

import org.gephi.graph.api.GraphModel;
import org.gephi.layout.plugin.force.StepDisplacement;
import org.gephi.layout.plugin.force.yifanHu.YifanHuLayout;
import org.gephi.layout.plugin.fruchterman.FruchtermanReingold;
import org.gephi.layout.plugin.labelAdjust.LabelAdjust;
import org.gephi.layout.plugin.noverlap.NoverlapLayout;

public enum LayoutType {
	FORCE_ATLAS, FRUCHTERMAN_REINGOLD, LABEL_ADJUST, YIFAN_HU, NOVERLAP, EVENTGRAPH;

	public void generateLayout(GraphModel graphModel, int size) {
		switch (this) {
		case FORCE_ATLAS:
			// ForceAtlas atlas = new ForceAtlas();
			// @issue How to pass the graph to the atlas algorithm?
			break;
		case FRUCHTERMAN_REINGOLD:
			LayoutType.setFruchtermanReingold(graphModel, size);
			break;
		case LABEL_ADJUST:
			LayoutType.setLabelAdjust(graphModel, size);
			break;
		case YIFAN_HU:
			LayoutType.setYifanHuLayout(graphModel, size);
			break;
		case NOVERLAP:
			LayoutType.setNoverlapLayout(graphModel, size);
			break;
		default:
			break;
		}
	}

	public static void setFruchtermanReingold(GraphModel graphModel, int size) {
		FruchtermanReingold fruchtermanReingold = new FruchtermanReingold(null);
		fruchtermanReingold.setGraphModel(graphModel);
		fruchtermanReingold.resetPropertiesValues();
		fruchtermanReingold.setSpeed(100d);
		fruchtermanReingold.setGravity(40d);
		fruchtermanReingold.initAlgo();
		for (int i = 0; i < 10 * Math.sqrt(size) && fruchtermanReingold.canAlgo(); i++) {
			fruchtermanReingold.goAlgo();
		}
		fruchtermanReingold.endAlgo();
	}

	public static void setLabelAdjust(GraphModel graphModel, int size) {
		LabelAdjust labelAdjust = new LabelAdjust(null);
		labelAdjust.setGraphModel(graphModel);
		labelAdjust.resetPropertiesValues();
		labelAdjust.setSpeed(25d);
		labelAdjust.initAlgo();
		for (int i = 0; i < 10 * Math.sqrt(size) && labelAdjust.canAlgo(); i++) {
			labelAdjust.goAlgo();
		}
		labelAdjust.endAlgo();
	}

	public static void setYifanHuLayout(GraphModel graphModel, int size) {
		YifanHuLayout yifanHu = new YifanHuLayout(null, new StepDisplacement(5f));
		yifanHu.setGraphModel(graphModel);
		yifanHu.resetPropertiesValues();
		yifanHu.setOptimalDistance(200f);
		yifanHu.initAlgo();
		for (int i = 0; i < 10 * Math.sqrt(size) && yifanHu.canAlgo(); i++) {
			yifanHu.goAlgo();
		}
		yifanHu.endAlgo();
	}

	public static void setNoverlapLayout(GraphModel graphModel, int size) {
		NoverlapLayout noverlap = new NoverlapLayout(null);
		noverlap.setGraphModel(graphModel);
		noverlap.resetPropertiesValues();
		noverlap.initAlgo();
		for (int i = 0; i < 10 * Math.sqrt(size) && noverlap.canAlgo(); i++) {
			noverlap.goAlgo();
		}
		noverlap.endAlgo();
	}
}

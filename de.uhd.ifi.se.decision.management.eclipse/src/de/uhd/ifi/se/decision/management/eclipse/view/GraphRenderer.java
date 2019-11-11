package de.uhd.ifi.se.decision.management.eclipse.view;

import org.gephi.preview.plugin.renderers.NodeRenderer;
import org.gephi.preview.spi.MouseResponsiveRenderer;
import org.gephi.preview.spi.PreviewMouseListener;
import org.gephi.preview.spi.Renderer;
import org.openide.util.lookup.ServiceProvider;

/**
 * Implementation of the Gephi-Renderer
 *
 */
@ServiceProvider(service = Renderer.class)
public class GraphRenderer extends NodeRenderer implements MouseResponsiveRenderer {

	@Override
    public boolean needsPreviewMouseListener(PreviewMouseListener listener) {
        return listener instanceof GraphMouseListener;
    }

}

package de.uhd.ifi.se.decision.management.eclipse.view;

import java.awt.Component;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.atomic.AtomicBoolean;

import javax.swing.JPanel;

import org.gephi.graph.api.GraphController;
import org.gephi.graph.api.Node;
import org.gephi.preview.api.G2DTarget;
import org.gephi.preview.api.PreviewController;
import org.gephi.preview.api.PreviewMouseEvent;
import org.gephi.preview.api.Vector;
import org.gephi.project.api.Workspace;
import org.openide.util.Lookup;

import de.uhd.ifi.se.decision.management.eclipse.event.NodeUtils;
import de.uhd.ifi.se.decision.management.eclipse.persistence.KnowledgePersistenceManager;

/**
 * @author markus
 *
 */
public class PreviewSketch extends JPanel implements MouseListener, MouseWheelListener, MouseMotionListener {

	private static final long serialVersionUID = 1L;
	private static final int WHEEL_TIMER = 500;
	// Data
	private final PreviewController previewController;
	private final G2DTarget target;
	private Workspace workspace;
	// Geometry
	private final Vector ref = new Vector();
	private final Vector lastMove = new Vector();
	// Utils
	private final RefreshLoop refreshLoop = new RefreshLoop();
	private Timer wheelTimer;
	private boolean inited;
	private final boolean isRetina;
	// MouseListener
	public static boolean createLink = false;
	private static Node sourceNode = null;
	private static Node targetNode = null;

	public PreviewSketch(G2DTarget target, Workspace workspace) {
		this.target = target;
		this.workspace = workspace;
		previewController = Lookup.getDefault().lookup(PreviewController.class);
		isRetina = false;
	}

	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);

		if (!inited) {
			// Listeners
			addMouseListener(this);
			addMouseMotionListener(this);
			addMouseWheelListener(this);
			inited = true;
		}

		int width = (int) (getWidth() * (isRetina ? 2.0 : 1.0));
		int height = (int) (getHeight() * (isRetina ? 2.0 : 1.0));

		if (target.getWidth() != width || target.getHeight() != height) {
			target.resize(width, height);
		}

		g.drawImage(target.getImage(), 0, 0, getWidth(), getHeight(), this);
	}

	public void setMoving(boolean moving) {
		target.setMoving(moving);
	}
	
	public void refresh() {
		this.target.refresh();
		refreshLoop.refreshSketch();
	}

	public void zoomPlus() {
		target.setScaling(target.getScaling() * 2f);
		refreshLoop.refreshSketch();
	}

	public void zoomMinus() {
		target.setScaling(target.getScaling() / 2f);
		refreshLoop.refreshSketch();
	}

	public void resetZoom() {
		target.reset();
		refreshLoop.refreshSketch();
	}

	private Vector screenPositionToModelPosition(Vector screenPos) {
		Vector center = new Vector(getWidth() / 2f, getHeight() / 2f);
		Vector scaledCenter = Vector.mult(center, target.getScaling());
		Vector scaledTrans = Vector.sub(center, scaledCenter);

		Vector modelPos = new Vector(screenPos.x, screenPos.y);
		modelPos.sub(scaledTrans);
		modelPos.div(target.getScaling());
		modelPos.sub(target.getTranslate());
		return modelPos;
	}

	private PreviewMouseEvent buildPreviewMouseEvent(MouseEvent evt, PreviewMouseEvent.Type type) {
		int mouseX = evt.getX();
		int mouseY = evt.getY();
		
		PreviewMouseEvent.Button button = PreviewMouseEvent.Button.LEFT;
		if (evt.isPopupTrigger()) {
			button = PreviewMouseEvent.Button.RIGHT;
		}

		Vector pos = screenPositionToModelPosition(new Vector(mouseX, mouseY));
		
		KeyEvent keyEvent = new KeyEvent(evt.getComponent(), 0, 0, 0, 0, ' ');

		return new PreviewMouseEvent((int) pos.x, (int) pos.y, type, button, keyEvent);
	}

	private class RefreshLoop {
		private final long DELAY = 100;
		private final AtomicBoolean running = new AtomicBoolean();
		private final AtomicBoolean refresh = new AtomicBoolean();
		// Timer
		private long timeout = DELAY * 10;
		private Timer timer;

		public void refreshSketch() {
			refresh.set(true);
			if (!running.getAndSet(true)) {
				startTimer();
			}
		}

		private void startTimer() {
			timer = new Timer("PreviewRefreshLoop", true);
			timer.schedule(new TimerTask() {
				@Override
				public void run() {
					if (refresh.getAndSet(false)) {
						target.refresh();
						repaint();
					} else if (timeout == 0) {
						timeout = DELAY * 10;
						stopTimer();
					} else {
						timeout -= DELAY;
					}
				}
			}, 0, DELAY);
		}

		private void stopTimer() {
			timer.cancel();
			running.set(false);
		}
	}
	
	public void refreshWorkspace(Workspace workspace) {
		this.workspace = workspace;
	}
	
	@Override
	public void mouseClicked(MouseEvent e) {
		PreviewMouseEvent previewEvent = buildPreviewMouseEvent(e, PreviewMouseEvent.Type.CLICKED);
		
		mouseEvent(previewEvent, e.isPopupTrigger());
		
		if (previewController.sendMouseEvent(previewEvent)) {
			refreshLoop.refreshSketch();
		}
	}

	@Override
	public void mousePressed(MouseEvent e) {
		PreviewMouseEvent previewEvent = buildPreviewMouseEvent(e, PreviewMouseEvent.Type.PRESSED);
		
		previewController.sendMouseEvent(previewEvent);
		ref.set(e.getX(), e.getY());
		lastMove.set(target.getTranslate());
		
		mouseEvent(previewEvent, e.isPopupTrigger());

		refreshLoop.refreshSketch();
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		PreviewMouseEvent previewEvent = buildPreviewMouseEvent(e, PreviewMouseEvent.Type.RELEASED);
		
		mouseEvent(previewEvent, e.isPopupTrigger());
		
		if (!previewController.sendMouseEvent(previewEvent)) {
			setMoving(false);
		}

		refreshLoop.refreshSketch();
	}

	@Override
	public void mouseEntered(MouseEvent e) {
		// Empty
	}

	@Override
	public void mouseExited(MouseEvent e) {
		// Empty
	}

	@Override
	public void mouseWheelMoved(MouseWheelEvent e) {
		if (e.getUnitsToScroll() == 0) {
			return;
		}
		float way = -e.getUnitsToScroll() / Math.abs(e.getUnitsToScroll());
		target.setScaling(target.getScaling() * (way > 0 ? 2f : 0.5f));
		setMoving(true);
		if (wheelTimer != null) {
			wheelTimer.cancel();
			wheelTimer = null;
		}
		wheelTimer = new Timer();
		wheelTimer.schedule(new TimerTask() {
			@Override
			public void run() {
				setMoving(false);
				refreshLoop.refreshSketch();
				wheelTimer = null;
			}
		}, WHEEL_TIMER);
		refreshLoop.refreshSketch();
	}

	@Override
	public void mouseDragged(MouseEvent e) {
		if (!previewController.sendMouseEvent(buildPreviewMouseEvent(e, PreviewMouseEvent.Type.DRAGGED))) {
			setMoving(true);
			Vector trans = target.getTranslate();
			trans.set(e.getX(), e.getY());
			trans.sub(ref);
			trans.mult(isRetina ? 2f : 1f);
			trans.div(target.getScaling()); // ensure const. moving speed whatever the zoom is
			trans.add(lastMove);

			refreshLoop.refreshSketch();
		}
	}

	@Override
	public void mouseMoved(MouseEvent e) {
		// Empty
	}
	
	/**
     * Handles the interaction with the graph with mouse clicks
     * @param event
     * 		the mouse event containing the mouse click
     * @param popupTrigger
     * 		true if the event is a popup-trigger, false if not
     * @return
     * 		true, if the graph was interacted with
     */
	private boolean mouseEvent(PreviewMouseEvent event, boolean popupTrigger) {
		
		if (!createLink) {
			if (popupTrigger) {
				createPopupMenu(event);
				sourceNode = getClickedNode(event);
			}
		}
		else if (createLink) {
			targetNode = getClickedNode(event);
			
			KnowledgePersistenceManager.insertLink(sourceNode, targetNode);
			
			createLink = false;
			sourceNode = null;
			targetNode = null;
		}
		
		return true;
	}
	
	/**
     * Checks if a node was clicked and returns it
     * @param event
     * 		the mouse event containing the mouse click
     * @return
     * 		if a node was clicked, the node that was clicked; else null
     */
    private Node getClickedNode(PreviewMouseEvent event) {
    	for (Node node : Lookup.getDefault().lookup(GraphController.class).getGraphModel(workspace).getGraph().getNodes()) {
			if (NodeUtils.clickInNode(node, event.x, event.y)) {
    			return node;
    		}
        }

    	return null;
    }
    
    /**
     * Creates a popup menu if a node was right clicked
     * @param event
     * 		the mouse event containing the mouse click
     * @return
     * 		true, if a popup-menu was created
     */
    private boolean createPopupMenu(PreviewMouseEvent event) {
    	Node selectedNode = getClickedNode(event);
		PopupMenu popup = new PopupMenu(selectedNode);
		Component component = event.keyEvent.getComponent();
		Point point = component.getMousePosition();
		popup.show(component, point.x, point.y);
		
        return true;
    }
	
}

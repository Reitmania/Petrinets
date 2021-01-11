package mypackage.controller;

import org.graphstream.ui.view.ViewerListener;

public class ClickListener implements ViewerListener {
	/** Controller der Anwendung */
	private Controller controller;

	/**
	 * Im Konstruktor wird der Controller gesetzt.
	 * 
	 * @param controller
	 *            Controller der Anwendung
	 */
	public ClickListener(Controller controller) {
		this.controller = controller;
	}

	@Override
	public void viewClosed(String viewName) {
		System.out.println("ClickListener - viewClosed: " + viewName);
		// wird nicht verwendet
	}

	@Override
	public void buttonPushed(String id) {
		System.out.println("ClickListener - buttonPushed: " + id);

		if (controller != null) {
			controller.clickNodeInGraph(id);
		}
	}

	@Override
	public void buttonReleased(String id) {
		System.out.println("ClickListener - buttonReleased: " + id);
		// wird nicht verwendet
	}
}

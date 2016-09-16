package com.rambler.tasklipse.provider;

import org.eclipse.jface.viewers.CellLabelProvider;
import org.eclipse.jface.viewers.ColumnViewer;
import org.eclipse.jface.viewers.StyledCellLabelProvider;
import org.eclipse.jface.viewers.ViewerCell;
import org.eclipse.jface.viewers.ViewerColumn;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.StyleRange;
import org.eclipse.swt.events.MouseAdapter;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.Event;

class HyperLinkLabelProvider extends StyledCellLabelProvider {

	private final CellLabelProvider cellLabelProvider;
	private int columnIndex = -1;
	private int charWidth;

	public HyperLinkLabelProvider(CellLabelProvider cellLabelProvider) {
		this.cellLabelProvider = cellLabelProvider;
	}

	@Override
	public void initialize(ColumnViewer viewer, ViewerColumn column) {
		super.initialize(viewer, column);
		LinkMouseListener mouseListener = new LinkMouseListener(viewer);
		viewer.getControl().addMouseListener(mouseListener);	
	}

	@Override
	protected void paint(Event event, Object element) {
		super.paint(event, element);
		charWidth = event.gc.getFontMetrics().getAverageCharWidth() ;
	}

	@Override
	public void update(ViewerCell cell) {
		cellLabelProvider.update(cell);
		StyleRange s = new StyleRange();
		s.foreground = cell.getItem().getDisplay().getSystemColor(SWT.COLOR_BLUE);
		s.underline = true;
		s.start = 0;
		s.length = cell.getText().length();
		cell.setStyleRanges(new StyleRange[] {s});
		columnIndex = cell.getColumnIndex();
	}

	private final class LinkMouseListener extends MouseAdapter {
		private final ColumnViewer column;
		public LinkMouseListener(ColumnViewer viewer) {column  =  viewer;}
		@Override public void mouseDown(MouseEvent e) {
			Point point = new Point(e.x,e.y);
			ViewerCell cell = column.getCell(point);
			if (cell != null && cell.getColumnIndex() == columnIndex) {
				Rectangle rect = cell.getTextBounds();
				rect.width = cell.getText().length() * charWidth;
				if (rect.contains(point))
					System.out.println("Clicked at :"+cell.getElement().toString());
			}
		}
	}
}

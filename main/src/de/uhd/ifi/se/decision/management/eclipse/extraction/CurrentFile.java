package de.uhd.ifi.se.decision.management.eclipse.extraction;

import org.eclipse.core.runtime.IPath;
import org.eclipse.jface.text.BadLocationException;
import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.ITextSelection;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.ISelectionProvider;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.part.FileEditorInput;
import org.eclipse.ui.texteditor.ITextEditor;

/**
 * Handles the current file and its content in the eclipse environment
 */
public class CurrentFile {

	/**
	 * Retrieves the current line of cursor position
	 * 
	 * @return current line of cursor position (workbench starts with line 0)
	 */
	public static int getLineOfCursorPosition() {

		int lineNumber = 0;

		IWorkbench workbench = PlatformUI.getWorkbench();
		IWorkbenchWindow window = workbench.getActiveWorkbenchWindow();
		IWorkbenchPage page = window.getActivePage();
		IEditorPart editor = page.getActiveEditor();

		if (editor instanceof ITextEditor) {
			ISelectionProvider selectionProvider = ((ITextEditor) editor).getSelectionProvider();
			ISelection selection = selectionProvider.getSelection();
			IDocument document = ((ITextEditor) editor).getDocumentProvider().getDocument(editor.getEditorInput());
			if (selection instanceof ITextSelection) {
				ITextSelection textSelection = (ITextSelection) selection;
				int offsetted = textSelection.getOffset();
				try {
					lineNumber = document.getLineOfOffset(offsetted);
				} catch (BadLocationException e) {
					System.out.println("Current Line Number cannot be determined");
					e.printStackTrace();
				}
			}
		} else {
			System.out.println("Current Line Number cannot be determined for this editor");
		}

		return lineNumber;
	}

	/**
	 * Retrieves the editor content of the currently opened file
	 * 
	 * @return content as String
	 */
	public static String getCurrentEditorContent() {

		final IEditorPart editor = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage()
				.getActiveEditor();

		if (!(editor instanceof ITextEditor)) {
			return null;
		}

		ITextEditor textEditor = (ITextEditor) editor;
		IDocument document = textEditor.getDocumentProvider().getDocument(textEditor.getEditorInput());

		return document.get();
	}

	/**
	 * Retrieves the path of the currently opened file in the editor
	 * 
	 * @return path of the currently opened file
	 */
	public static IPath getOpenFilePath() {
		final IEditorPart editor = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage()
				.getActiveEditor();
		if (!(editor instanceof ITextEditor)) {
			return null;
		} else {
			IEditorInput input = editor.getEditorInput();
			IPath path = input instanceof FileEditorInput ? ((FileEditorInput) input).getPath() : null;
			if (path != null) {
				return path;
			}
		}
		return null;
	}
}

package de.uhd.ifi.se.decision.management.eclipse.event;

import org.eclipse.core.runtime.IPath;
import org.eclipse.jface.text.BadLocationException;
import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.ITextSelection;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.ISelectionProvider;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorPart;
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
		ITextEditor editor = getTextEditor();
		if (editor == null) {
			System.out.println("Current line number cannot be determined for this editor.");
			return 0;
		}
		IDocument document = ((ITextEditor) editor).getDocumentProvider().getDocument(editor.getEditorInput());

		ITextSelection textSelection = getTextSelection(editor);
		if (textSelection == null) {
			return 0;
		}

		int lineNumber = 0;
		try {
			lineNumber = document.getLineOfOffset(textSelection.getOffset());
		} catch (BadLocationException e) {
			System.out.println("Current line number cannot be determined.");
			e.printStackTrace();
		}
		return lineNumber;
	}

	private static ITextEditor getTextEditor() {
		IEditorPart editor = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage().getActiveEditor();
		if (editor instanceof ITextEditor) {
			return (ITextEditor) editor;
		}
		return null;
	}

	private static ITextSelection getTextSelection(ITextEditor editor) {
		ISelectionProvider selectionProvider = editor.getSelectionProvider();
		ISelection selection = selectionProvider.getSelection();
		if (selection instanceof ITextSelection) {
			return (ITextSelection) selection;
		}
		return null;
	}

	/**
	 * Retrieves the editor content of the currently opened file.
	 * 
	 * @return editor content as String.
	 */
	public static String getCurrentEditorContent() {
		ITextEditor editor = getTextEditor();
		if (editor == null) {
			return null;
		}
		IDocument document = editor.getDocumentProvider().getDocument(editor.getEditorInput());
		return document.get();
	}

	/**
	 * Retrieves the path of the currently opened file in the editor.
	 * 
	 * @return path of the currently opened file.
	 */
	public static IPath getOpenFilePath() {
		ITextEditor editor = getTextEditor();
		if (editor == null) {
			return null;
		}
		IEditorInput input = editor.getEditorInput();
		IPath path = input instanceof FileEditorInput ? ((FileEditorInput) input).getPath() : null;
		return path;
	}
}

/* Glazed Lists                                                 (c) 2003-2005 */
/* http://publicobject.com/glazedlists/                      publicobject.com,*/
/*                                                     O'Dell Engineering Ltd.*/
package ca.odell.glazedlists.swing;

import ca.odell.glazedlists.TextFilterator;
import ca.odell.glazedlists.TextMatcherEditor;

import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.BadLocationException;
import javax.swing.text.Document;
import javax.swing.text.JTextComponent;
import javax.swing.*;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

/**
 * A MatcherEditor that matches Objects that contain the filter text located
 * within a {@link Document}. This {@link TextMatcherEditor} is directly
 * coupled with a Document and fires MatcherEditor changes in response to
 * Document changes. This matcher is fully concrete and is expected to be used
 * by Swing applications.
 *
 * <p>The {@link TextComponentMatcherEditor} constructors require that either a
 * {@link Document} or a {@link JTextComponent} (from which a {@link Document}
 * is extracted) be specified.
 *
 * <p>The MatcherEditor registers itself as a {@link DocumentListener} on the
 * given Document, or {@link ActionListener} for non-live filtering. If this
 * MatcherEditor must be garbage collected before the underlying Document,
 * or JTextField, the listener can be unregistered by calling {@link #dispose()}.
 *
 * @author James Lemieux
 */
public class TextComponentMatcherEditor extends TextMatcherEditor {

    /** The Document that provides the filter values. */
    private Document document;

    /** the JTextField being observed for actions */
    private JTextField textField;

    /** The listener attached to the given {@link #document}. */
    private FilterHandler filterHandler = new FilterHandler();

    /**
     * Creates a TextMatcherEditor bound to the {@link Document} backing the
     * given <code>textComponent</code> with the given
     * <code>textFilterator</code>.
     *
     * @param textComponent the text component backed by the {@link Document}
     *      that is the source of text filter values
     * @param textFilterator an object capable of producing Strings from the
     *      objects being filtered. If <code>textFilterator</code> is
     *      <code>null</code> then all filtered objects are expected to
     *      implement {@link ca.odell.glazedlists.TextFilterable}.
     */
    public TextComponentMatcherEditor(JTextComponent textComponent, TextFilterator textFilterator) {
        this(textComponent, textFilterator, true);
    }

    /**
     * Creates a TextMatcherEditor bound to the {@link Document} backing the
     * given <code>textComponent</code> with the given
     * <code>textFilterator</code>.
     *
     * @param textComponent the text component backed by the {@link Document}
     *      that is the source of text filter values
     * @param textFilterator an object capable of producing Strings from the
     *      objects being filtered. If <code>textFilterator</code> is
     *      <code>null</code> then all filtered objects are expected to
     *      implement {@link ca.odell.glazedlists.TextFilterable}.
     * @param live <code>true</code> to filter by the keystroke or <code>false</code>
     *      to filter only when {@link java.awt.event.KeyEvent#VK_ENTER Enter} is pressed
     *      within the {@link JTextField}. Note that non-live filtering is only
     *      supported if <code>textComponent</code> is a {@link JTextField}.
     * @throws IllegalArgumentException if the <code>textComponent</code> does
     *      is not a {@link JTextField} and non-live filtering is specified.
     */
    public TextComponentMatcherEditor(JTextComponent textComponent, TextFilterator textFilterator, boolean live) {
        super(textFilterator);

        if(live) {
            this.document = textComponent.getDocument();
            this.document.addDocumentListener(this.filterHandler);
        } else {
            if(!(textComponent instanceof JTextField)) throw new IllegalArgumentException("Non-live filtering supported only for JTextField (argument class " + textComponent.getClass().getName() + ")");
            this.textField = (JTextField)textComponent;
            this.document = this.textField.getDocument();
            textField.addActionListener(this.filterHandler);
        }

        // if the document is non-empty to begin with!
        refilter();
    }

    /**
     * Creates a TextMatcherEditor bound to the given <code>document</code>
     * with the given <code>textFilterator</code>.
     *
     * @param document the {@link Document} that is the source of text filter
     *      values
     * @param textFilterator an object capable of producing Strings from the
     *      objects being filtered. If <code>textFilterator</code> is
     *      <code>null</code> then all filtered objects are expected to
     *      implement {@link ca.odell.glazedlists.TextFilterable}.
     */
    public TextComponentMatcherEditor(Document document, TextFilterator textFilterator) {
        super(textFilterator);
        this.document = document;
        this.document.addDocumentListener(this.filterHandler);

        // if the document is non-empty to begin with!
        refilter();
    }

    /**
     * A cleanup method which stops this MatcherEditor from listening to
     * changes on the underlying {@link Document}, thus freeing the
     * MatcherEditor or Document to be garbage collected.
     */
    public void dispose() {
        if(textField != null) textField.removeActionListener(this.filterHandler);
        else document.removeDocumentListener(this.filterHandler);
    }

    /**
     * Update the filter text from the contents of the Document.
     */
    private void refilter() {
        try {
            setFilterText(document.getText(0, document.getLength()).split("[ \t]"));
        } catch (BadLocationException ble) {
            // this shouldn't ever, ever happen
            throw new RuntimeException(ble);
        }
    }


    /**
     * This class responds to any change in the Document by setting the filter
     * text of this TextMatcherEditor to the contents of the Document.
     */
    private class FilterHandler implements DocumentListener, ActionListener {
        public void insertUpdate(DocumentEvent e) {
            refilter();
        }

        public void removeUpdate(DocumentEvent e) {
            refilter();
        }

        public void changedUpdate(DocumentEvent e) {
            refilter();
        }

        public void actionPerformed(ActionEvent e) {
            refilter();
        }
    }
}
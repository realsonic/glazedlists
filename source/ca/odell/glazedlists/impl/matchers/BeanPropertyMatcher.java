/* Glazed Lists                                                 (c) 2003-2006 */
/* http://publicobject.com/glazedlists/                      publicobject.com,*/
/*                                                     O'Dell Engineering Ltd.*/
package ca.odell.glazedlists.impl.matchers;

import ca.odell.glazedlists.impl.GlazedListsImpl;
import ca.odell.glazedlists.impl.beans.BeanProperty;
import ca.odell.glazedlists.matchers.Matcher;

/**
 * A {@link Matcher} which uses a {@link BeanProperty} to read a bean property
 * from a given bean and check it for equality with a given value.
 * <code>null</code> property values are allowed.
 *
 * @author James Lemieux
 */
public final class BeanPropertyMatcher<E> implements Matcher<E> {

    /** The BeanProperty containing logic for extracting the property value from an item. */
    private final BeanProperty<E> beanProperty;

    /** The value with which to compare the bean property. */
    private final Object value;

    /**
     * Create a new {@link Matcher} that matches whenever the given property
     * equals the given <code>value</code>.
     */
    public BeanPropertyMatcher(BeanProperty<E> beanProperty, Object value) {
        if (beanProperty == null)
            throw new IllegalArgumentException("beanProperty may not be null");
        
        this.beanProperty = beanProperty;
        this.value = value;
    }

    /** {@inheritDoc} */
    public boolean matches(E item) {
        return GlazedListsImpl.equal(this.beanProperty.get(item), this.value);
    }
}
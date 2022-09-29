package com.company;

/**
 * Focused is the interface for all the architectures
 * to add them the functionality to be focused on.
 *
 * @see Architecture
 */
public interface Focused {

    /**
     * Focus method returns the unique property
     * of this object in <code>String</code> format for
     * printing.
     * @return  Unique property of this object
     *          in <code>String</code> format for
     *          printing.
     */
    String focus();
}

////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
// Copyright (c) 2018-2020 Saxonica Limited
// This Source Code Form is subject to the terms of the Mozilla Public License, v. 2.0.
// If a copy of the MPL was not distributed with this file, You can obtain one at http://mozilla.org/MPL/2.0/.
// This Source Code Form is "Incompatible With Secondary Licenses", as defined by the Mozilla Public License, v. 2.0.
////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

package net.sf.saxon.pull;

import net.sf.saxon.event.PipelineConfiguration;
import net.sf.saxon.om.*;
import net.sf.saxon.s9api.Location;
import net.sf.saxon.trans.XPathException;
import net.sf.saxon.type.SchemaType;
import net.sf.saxon.value.AtomicValue;

import java.util.List;

/**
 * PullFilter is a pass-through filter class that links one PullProvider to another PullProvider
 * in a pipeline. This class passes all events through completely unchanged. The class is designed
 * so that subclasses can modify this behavior by altering some of the events.
 */

public class PullFilter implements PullProvider {

    private PullProvider base;
    private PipelineConfiguration pipe;
    protected Event currentEvent;

    /**
     * Create a PullFilter
     *
     * @param base the PullProvider to which requests are to be passed
     */

    public PullFilter(/*@NotNull*/ PullProvider base) {
        this.base = base;
        if (base.getPipelineConfiguration() != null) {
            setPipelineConfiguration(base.getPipelineConfiguration());
        }
        currentEvent = base.current();
    }

    /**
     * Set configuration information. This must only be called before any events
     * have been read.
     */

    @Override
    public void setPipelineConfiguration(PipelineConfiguration pipe) {
        this.pipe = pipe;
        base.setPipelineConfiguration(pipe);
    }

    /**
     * Get configuration information.
     */

    @Override
    public PipelineConfiguration getPipelineConfiguration() {
        return pipe;
    }

    /**
     * Helper method to get the current namePool
     *
     * @return the NamePool
     */

    public final NamePool getNamePool() {
        return getPipelineConfiguration().getConfiguration().getNamePool();
    }

    /**
     * Get the underlying PullProvider
     *
     * @return the underlying PullProvider
     */

    public PullProvider getUnderlyingProvider() {
        return base;
    }

    /**
     * Get the next event.
     * <p>Note that a subclass that overrides this method is responsible for ensuring
     * that current() works properly. This can be achieved by setting the field
     * currentEvent to the event returned by any call on next().</p>
     *
     * @return an integer code indicating the type of event. The code
     *         {@link Event#END_OF_INPUT} is returned at the end of the sequence.
     */

    @Override
    public Event next() throws XPathException {
        return base.next();
    }

    /**
     * Get the event most recently returned by next(), or by other calls that change
     * the position, for example getStringValue() and skipToMatchingEnd(). This
     * method does not change the position of the PullProvider.
     *
     * @return the current event
     */

    @Override
    public Event current() {
        return currentEvent;
    }

    /**
     * Get the attributes associated with the current element. This method must
     * be called only after a START_ELEMENT event has been notified. The contents
     * of the returned AttributeMap are immutable.
     * <p>Attributes may be read before or after reading the namespaces of an element,
     * but must not be read after the first child node has been read, or after calling
     * one of the methods skipToEnd(), getStringValue(), or getTypedValue().</p>
     *
     * @return an AttributeMap representing the attributes of the element
     *         that has just been notified.
     */

    @Override
    public AttributeMap getAttributes() throws XPathException {
        return base.getAttributes();
    }

    /**
     * Get the namespace declarations associated with the current element. This method must
     * be called only after a START_ELEMENT event has been notified. In the case of a top-level
     * START_ELEMENT event (that is, an element that either has no parent node, or whose parent
     * is not included in the sequence being read), the NamespaceDeclarations object returned
     * will contain a namespace declaration for each namespace that is in-scope for this element
     * node. In the case of a non-top-level element, the NamespaceDeclarations will contain
     * a set of namespace declarations and undeclarations, representing the differences between
     * this element and its parent.
     * <p>It is permissible for this method to return namespace declarations that are redundant.</p>
     * <p>The NamespaceDeclarations object is guaranteed to remain unchanged until the next START_ELEMENT
     * event, but may then be overwritten. The object should not be modified by the client.</p>
     * <p>Namespaces may be read before or after reading the attributes of an element,
     * but must not be read after the first child node has been read, or after calling
     * one of the methods skipToEnd(), getStringValue(), or getTypedValue().</p>
     */

    @Override
    public NamespaceBinding[] getNamespaceDeclarations() throws XPathException {
        return base.getNamespaceDeclarations();
    }

    /**
     * Skip the current subtree. This method may be called only immediately after
     * a START_DOCUMENT or START_ELEMENT event. This call returns the matching
     * END_DOCUMENT or END_ELEMENT event; the next call on next() will return
     * the event following the END_DOCUMENT or END_ELEMENT.
     */

    @Override
    public Event skipToMatchingEnd() throws XPathException {
        return base.skipToMatchingEnd();
    }

    /**
     * Close the event reader. This indicates that no further events are required.
     * It is not necessary to close an event reader after {@link Event#END_OF_INPUT} has
     * been reported, but it is recommended to close it if reading terminates
     * prematurely. Once an event reader has been closed, the effect of further
     * calls on next() is undefined.
     */

    @Override
    public void close() {
        base.close();
    }

    @Override
    public NodeName getNodeName() {
        return base.getNodeName();
    }

    /**
     * Get the string value of the current element, text node, processing-instruction,
     * or top-level attribute or namespace node, or atomic value.
     * <p>In other situations the result is undefined and may result in an IllegalStateException.</p>
     * <p>If the most recent event was a {@link Event#START_ELEMENT}, this method causes the content
     * of the element to be read. The next event notified will be the corresponding {@link Event#END_ELEMENT}.</p>
     *
     * @return the String Value of the node in question, defined according to the rules in the
     *         XPath data model.
     */

    @Override
    public CharSequence getStringValue() throws XPathException {
        return base.getStringValue();
    }

    /**
     * Get an atomic value. This call may be used only when the last event reported was
     * ATOMIC_VALUE. This indicates that the PullProvider is reading a sequence that contains
     * a free-standing atomic value; it is never used when reading the content of a node.
     */

    @Override
    public AtomicValue getAtomicValue() {
        return base.getAtomicValue();
    }

    /**
     * Get the type annotation of the current attribute or element node, or atomic value.
     * The result of this method is undefined unless the most recent event was START_ELEMENT,
     * ATTRIBUTE, or ATOMIC_VALUE.
     *
     * @return the type annotation.
     */

    @Override
    public SchemaType getSchemaType() {
        return base.getSchemaType();
    }

    /**
     * Get the location of the current event.
     * For an event stream representing a real document, the location information
     * should identify the location in the lexical XML source. For a constructed document, it should
     * identify the location in the query or stylesheet that caused the node to be created.
     * A value of null can be returned if no location information is available.
     */

    @Override
    public Location getSourceLocator() {
        return base.getSourceLocator();
    }

    /**
     * Get a list of unparsed entities.
     *
     * @return a list of unparsed entities, or null if the information is not available, or
     *         an empty list if there are no unparsed entities.
     */

    @Override
    public List<UnparsedEntity> getUnparsedEntities() {
        return base.getUnparsedEntities();
    }
}


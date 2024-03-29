////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
// Copyright (c) 2018-2020 Saxonica Limited
// This Source Code Form is subject to the terms of the Mozilla Public License, v. 2.0.
// If a copy of the MPL was not distributed with this file, You can obtain one at http://mozilla.org/MPL/2.0/.
// This Source Code Form is "Incompatible With Secondary Licenses", as defined by the Mozilla Public License, v. 2.0.
////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

package net.sf.saxon.value;

import net.sf.saxon.expr.sort.AtomicMatchKey;
import net.sf.saxon.expr.sort.ComparisonException;
import net.sf.saxon.lib.StringCollator;
import net.sf.saxon.om.SequenceTool;
import net.sf.saxon.trans.Err;
import net.sf.saxon.trans.XPathException;
import net.sf.saxon.type.AtomicType;
import net.sf.saxon.type.BuiltInAtomicType;
import net.sf.saxon.type.ConversionResult;
import net.sf.saxon.type.ValidationFailure;

/**
 * A boolean XPath value
 */

public final class BooleanValue extends AtomicValue implements Comparable, AtomicMatchKey {
    private boolean value;

    /**
     * The boolean value TRUE
     */
    public static final BooleanValue TRUE = new BooleanValue(true);
    /**
     * The boolean value FALSE
     */
    public static final BooleanValue FALSE = new BooleanValue(false);

    /**
     * Private Constructor: create a boolean value. Only two instances of this class are
     * ever created, one to represent true and one to represent false.
     *
     * @param value the initial value, true or false
     */

    private BooleanValue(boolean value) {
        this.value = value;
        typeLabel = BuiltInAtomicType.BOOLEAN;
    }

    /**
     * Factory method: get a BooleanValue
     *
     * @param value true or false, to determine which boolean value is
     *              required
     * @return the BooleanValue requested
     */

    public static BooleanValue get(boolean value) {
        return value ? TRUE : FALSE;
    }

    /**
     * Create a new Boolean value with a user-supplied type label.
     * It is the caller's responsibility to ensure that the value is valid for the subtype
     *
     * @param value     the boolean value
     * @param typeLabel the type label, xs:boolean or a subtype
     */

    public BooleanValue(boolean value, AtomicType typeLabel) {
        this.value = value;
        this.typeLabel = typeLabel;
    }

    /**
     * Create a copy of this atomic value (usually so that the type label can be changed).
     * The type label of the copy will be reset to the primitive type.
     *
     * @param typeLabel the atomic type label to be added to the copied value
     */

    @Override
    public AtomicValue copyAsSubType(AtomicType typeLabel) {
        BooleanValue v = new BooleanValue(value);
        v.typeLabel = typeLabel;
        return v;
    }

    /**
     * Convert a string to a boolean value, using the XML Schema rules (including
     * whitespace trimming)
     *
     * @param s the input string
     * @return the relevant BooleanValue if validation succeeds; or a ValidationFailure if not.
     */

    public static ConversionResult fromString(CharSequence s) {
        // implementation designed to avoid creating new objects
        s = Whitespace.trimWhitespace(s);
        int len = s.length();
        if (len == 1) {
            char c = s.charAt(0);
            if (c == '1') {
                return TRUE;
            } else if (c == '0') {
                return FALSE;
            }
        } else if (len == 4) {
            if (s.charAt(0) == 't' && s.charAt(1) == 'r' && s.charAt(2) == 'u' && s.charAt(3) == 'e') {
                return TRUE;
            }
        } else if (len == 5) {
            if (s.charAt(0) == 'f' && s.charAt(1) == 'a' && s.charAt(2) == 'l' && s.charAt(3) == 's' && s.charAt(4) == 'e') {
                return FALSE;
            }
        }
        ValidationFailure err = new ValidationFailure(
                "The string " + Err.wrap(s, Err.VALUE) + " cannot be cast to a boolean");
        err.setErrorCode("FORG0001");
        return err;
    }

    /**
     * Get the value
     *
     * @return true or false, the actual boolean value of this BooleanValue
     */

    public boolean getBooleanValue() {
        return value;
    }

    /**
     * Get the effective boolean value of this expression
     *
     * @return the boolean value
     */
    @Override
    public boolean effectiveBooleanValue() {
        return value;
    }

    /**
     * Determine the primitive type of the value. This delivers the same answer as
     * getItemType().getPrimitiveItemType(). The primitive types are
     * the 19 primitive types of XML Schema, plus xs:integer, xs:dayTimeDuration and xs:yearMonthDuration,
     * and xs:untypedAtomic. For external objects, the result is AnyAtomicType.
     */

    @Override
    public BuiltInAtomicType getPrimitiveType() {
        return BuiltInAtomicType.BOOLEAN;
    }

    /**
     * Convert to string
     *
     * @return "true" or "false"
     */

    @Override
    public String getPrimitiveStringValue() {
        return value ? "true" : "false";
    }

    /**
     * Get a Comparable value that implements the XML Schema ordering comparison semantics for this value.
     * The default implementation returns "this". This is overridden for particular atomic types.
     * <p>In the case of data types that are partially ordered, the returned Comparable extends the standard
     * semantics of the compareTo() method by returning the value {@link SequenceTool#INDETERMINATE_ORDERING} when there
     * is no defined order relationship between two given values.</p>
     *
     * @return a Comparable that follows XML Schema comparison rules
     */

    @Override
    public Comparable getSchemaComparable() {
        return new BooleanComparable();
    }

    private class BooleanComparable implements Comparable {

        public boolean asBoolean() {
            return BooleanValue.this.getBooleanValue();
        }

        @Override
        public int compareTo(/*@NotNull*/ Object o) {
            return equals(o) ? 0 : SequenceTool.INDETERMINATE_ORDERING;
        }

        public boolean equals(Object o) {
            return o instanceof BooleanComparable && asBoolean() == ((BooleanComparable) o).asBoolean();
        }

        public int hashCode() {
            return asBoolean() ? 9999999 : 8888888;
        }

    }

    /**
     * Get a Comparable value that implements the XPath ordering comparison semantics for this value.
     * Returns null if the value is not comparable according to XPath rules. The default implementation
     * returns null. This is overridden for types that allow ordered comparisons in XPath: numeric, boolean,
     * string, date, time, dateTime, yearMonthDuration, dayTimeDuration, and anyURI.
     *
     *
     * @param ordered  true if an ordered comparison is required. In this case the result is null if the
     *                 type is unordered; in other cases the returned value will be a Comparable.
     * @param collator the collation to be used when comparing strings
     * @param implicitTimezone  the XPath dynamic evaluation context, used in cases where the comparison is context
     *                 sensitive
     * @return an Object whose equals() and hashCode() methods implement the XPath comparison semantics
     *         with respect to this atomic value. If ordered is specified, the result will either be null if
     *         no ordering is defined, or will be a Comparable
     */

    @Override
    public AtomicMatchKey getXPathComparable(boolean ordered, StringCollator collator, int implicitTimezone) {
        return this;
    }

    /**
     * Compare the value to another boolean value
     *
     * @param other The other boolean value
     * @return -1 if this one is the lower, 0 if they are equal, +1 if this
     *         one is the higher. False is considered to be less than true.
     * @throws ClassCastException if the other value is not a BooleanValue
     *                            (the parameter is declared as Object to satisfy the Comparable
     *                            interface)
     */

    @Override
    public int compareTo(Object other) {
        if (!(other instanceof BooleanValue)) {
            XPathException e = new XPathException("Boolean values are not comparable to " + other.getClass(), "XPTY0004");
            throw new ComparisonException(e);
        }
        if (value == ((BooleanValue) other).value) {
            return 0;
        }
        if (value) {
            return +1;
        }
        return -1;
    }

    /**
     * Determine whether two boolean values are equal
     *
     * @param other the value to be compared to this value
     * @return true if the other value is a boolean value and is equal to this
     *         value
     * @throws ClassCastException if other value is not xs:boolean or derived therefrom
     */
    public boolean equals(Object other) {
        return other instanceof BooleanValue && value == ((BooleanValue) other).value;
    }

    /**
     * Get a hash code for comparing two BooleanValues
     *
     * @return the hash code
     */
    public int hashCode() {
        return value ? 0 : 1;
    }

    /**
     * Diagnostic display of this value as a string
     *
     * @return a string representation of this value: "true()" or "false()"
     */
    public String toString() {
        return getStringValue() + "()";
    }
}


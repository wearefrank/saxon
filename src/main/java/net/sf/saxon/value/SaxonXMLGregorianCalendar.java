////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
// Copyright (c) 2018-2020 Saxonica Limited
// This Source Code Form is subject to the terms of the Mozilla Public License, v. 2.0.
// If a copy of the MPL was not distributed with this file, You can obtain one at http://mozilla.org/MPL/2.0/.
// This Source Code Form is "Incompatible With Secondary Licenses", as defined by the Mozilla Public License, v. 2.0.
////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

package net.sf.saxon.value;

import net.sf.saxon.functions.AccessorFn;
import net.sf.saxon.trans.XPathException;

import javax.xml.datatype.DatatypeConstants;
import javax.xml.datatype.Duration;
import javax.xml.datatype.XMLGregorianCalendar;
import javax.xml.namespace.QName;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.*;

/**
 * Saxon implementation of the JAXP class javax.xml.datatype.XMLGregorianCalendar.
 * This is currently used only by the XQJ interface for XQuery: the normal representation of a
 * date, time, or dateTime value in Saxon is with a subclass of {@link CalendarValue}
 * <p>The JAXP specification for this class defines it in terms of XML Schema 1.0 semantics.
 * This implementation is more aligned to the XPath 2.0 semantics of the data types.</p>
 * <p>Note that this class, unlike the representations of all other data types, is mutable.</p>
 */
public class SaxonXMLGregorianCalendar extends XMLGregorianCalendar {

    /*@Nullable*/ private CalendarValue calendarValue;
    /*@Nullable*/ private BigInteger year;
    private int month = DatatypeConstants.FIELD_UNDEFINED;
    private int day = DatatypeConstants.FIELD_UNDEFINED;
    private int hour = DatatypeConstants.FIELD_UNDEFINED;
    private int minute = DatatypeConstants.FIELD_UNDEFINED;
    private int second = DatatypeConstants.FIELD_UNDEFINED;
    private int microsecond = DatatypeConstants.FIELD_UNDEFINED;
    private int tzOffset = DatatypeConstants.FIELD_UNDEFINED;

    /**
     * Create a SaxonXMLGregorianCalendar from a Saxon CalendarValue object
     *
     * @param value the CalendarValue
     */

    public SaxonXMLGregorianCalendar(/*@NotNull*/ CalendarValue value) {
        clear();
        setCalendarValue(value);
    }

    private SaxonXMLGregorianCalendar() {
    }

    /**
     * Set the calendar value of this object
     *
     * @param value the calendar value
     */

    public void setCalendarValue(/*@NotNull*/ CalendarValue value) {
        calendarValue = value;
        try {
            if (value instanceof GYearValue) {
                year = BigInteger.valueOf(((Int64Value) value.getComponent(AccessorFn.Component.YEAR)).longValue());
            } else if (value instanceof GYearMonthValue) {
                year = BigInteger.valueOf(((Int64Value) value.getComponent(AccessorFn.Component.YEAR)).longValue());
                month = (int) ((Int64Value) value.getComponent(AccessorFn.Component.MONTH)).longValue();
            } else if (value instanceof GMonthValue) {
                month = (int) ((Int64Value) value.getComponent(AccessorFn.Component.MONTH)).longValue();
            } else if (value instanceof GMonthDayValue) {
                month = (int) ((Int64Value) value.getComponent(AccessorFn.Component.MONTH)).longValue();
                day = (int) ((Int64Value) value.getComponent(AccessorFn.Component.DAY)).longValue();
            } else if (value instanceof GDayValue) {
                day = (int) ((Int64Value) value.getComponent(AccessorFn.Component.DAY)).longValue();
            } else if (value instanceof DateValue) {
                year = BigInteger.valueOf(((Int64Value) value.getComponent(AccessorFn.Component.YEAR)).longValue());
                month = (int) ((Int64Value) value.getComponent(AccessorFn.Component.MONTH)).longValue();
                day = (int) ((Int64Value) value.getComponent(AccessorFn.Component.DAY)).longValue();
            } else if (value instanceof TimeValue) {
                hour = (int) ((Int64Value) value.getComponent(AccessorFn.Component.HOURS)).longValue();
                minute = (int) ((Int64Value) value.getComponent(AccessorFn.Component.MINUTES)).longValue();
                second = (int) ((Int64Value) value.getComponent(AccessorFn.Component.WHOLE_SECONDS)).longValue();
                microsecond = (int) ((Int64Value) value.getComponent(AccessorFn.Component.MICROSECONDS)).longValue();
            } else {
                year = BigInteger.valueOf(((Int64Value) value.getComponent(AccessorFn.Component.YEAR)).longValue());
                month = (int) ((Int64Value) value.getComponent(AccessorFn.Component.MONTH)).longValue();
                day = (int) ((Int64Value) value.getComponent(AccessorFn.Component.DAY)).longValue();
                hour = (int) ((Int64Value) value.getComponent(AccessorFn.Component.HOURS)).longValue();
                minute = (int) ((Int64Value) value.getComponent(AccessorFn.Component.MINUTES)).longValue();
                second = (int) ((Int64Value) value.getComponent(AccessorFn.Component.WHOLE_SECONDS)).longValue();
                microsecond = (int) ((Int64Value) value.getComponent(AccessorFn.Component.MICROSECONDS)).longValue();
            }
        } catch (XPathException e) {
            throw new IllegalArgumentException(e.getMessage());
        }
    }

    /**
     * <p>Unset all fields to undefined.</p>
     * <p>Set all int fields to {@link DatatypeConstants#FIELD_UNDEFINED} and reference fields
     * to null.</p>
     */
    @Override
    public void clear() {
        year = null;
        month = DatatypeConstants.FIELD_UNDEFINED;
        day = DatatypeConstants.FIELD_UNDEFINED;
        hour = DatatypeConstants.FIELD_UNDEFINED;
        minute = DatatypeConstants.FIELD_UNDEFINED;
        second = DatatypeConstants.FIELD_UNDEFINED;
        microsecond = DatatypeConstants.FIELD_UNDEFINED;
        tzOffset = DatatypeConstants.FIELD_UNDEFINED;
    }

    /**
     * <p>Reset this <code>XMLGregorianCalendar</code> to its original values.</p>
     * <p>Saxon does not attempt to reset to the initial value as defined in the specification of
     * the superclass, because it cannot distinguish the initial setting from subsequent changes.
     * This method is therefore synonymous with {@link #clear()}</p>
     */
    @Override
    public void reset() {
        clear();
    }

    /**
     * <p>Set low and high order component of XSD <code>dateTime</code> year field.</p>
     * <p>Unset this field by invoking the setter with a parameter value of <code>null</code>.</p>
     *
     * @param year value constraints summarized in <a href="#datetimefield-year">year field of date/time field mapping table</a>.
     * @throws IllegalArgumentException if <code>year</code> parameter is
     *                                  outside value constraints for the field as specified in
     *                                  <a href="#datetimefieldmapping">date/time field mapping table</a>.
     */
    @Override
    public void setYear(BigInteger year) {
        calendarValue = null;
        this.year = year;
    }

    /**
     * <p>Set year of XSD <code>dateTime</code> year field.</p>
     * <p>Unset this field by invoking the setter with a parameter value of
     * {@link DatatypeConstants#FIELD_UNDEFINED}.</p>
     * <p>Note: if the absolute value of the <code>year</code> parameter
     * is less than 10^9, the eon component of the XSD year field is set to
     * <code>null</code> by this method.</p>
     *
     * @param year value constraints are summarized in <a href="#datetimefield-year">year field of date/time field mapping table</a>.
     *             If year is {@link DatatypeConstants#FIELD_UNDEFINED}, then eon is set to <code>null</code>.
     */
    @Override
    public void setYear(int year) {
        calendarValue = null;
        this.year = BigInteger.valueOf(year);
    }

    /**
     * <p>Set month.</p>
     * <p>Unset this field by invoking the setter with a parameter value of {@link DatatypeConstants#FIELD_UNDEFINED}.</p>
     *
     * @param month value constraints summarized in <a href="#datetimefield-month">month field of date/time field mapping table</a>.
     * @throws IllegalArgumentException if <code>month</code> parameter is
     *                                  outside value constraints for the field as specified in
     *                                  <a href="#datetimefieldmapping">date/time field mapping table</a>.
     */
    @Override
    public void setMonth(int month) {
        calendarValue = null;
        this.month = month;
    }

    /**
     * <p>Set days in month.</p>
     * <p>Unset this field by invoking the setter with a parameter value of {@link DatatypeConstants#FIELD_UNDEFINED}.</p>
     *
     * @param day value constraints summarized in <a href="#datetimefield-day">day field of date/time field mapping table</a>.
     * @throws IllegalArgumentException if <code>day</code> parameter is
     *                                  outside value constraints for the field as specified in
     *                                  <a href="#datetimefieldmapping">date/time field mapping table</a>.
     */
    @Override
    public void setDay(int day) {
        calendarValue = null;
        this.day = day;
    }

    /**
     * <p>Set the number of minutes in the timezone offset.</p>
     * <p>Unset this field by invoking the setter with a parameter value of {@link DatatypeConstants#FIELD_UNDEFINED}.</p>
     *
     * @param offset value constraints summarized in <a href="#datetimefield-timezone">
     *               timezone field of date/time field mapping table</a>.
     * @throws IllegalArgumentException if <code>offset</code> parameter is
     *                                  outside value constraints for the field as specified in
     *                                  <a href="#datetimefieldmapping">date/time field mapping table</a>.
     */
    @Override
    public void setTimezone(int offset) {
        calendarValue = null;
        tzOffset = offset;
    }

    /**
     * <p>Set hours.</p>
     * <p>Unset this field by invoking the setter with a parameter value of {@link DatatypeConstants#FIELD_UNDEFINED}.</p>
     *
     * @param hour value constraints summarized in <a href="#datetimefield-hour">hour field of date/time field mapping table</a>.
     * @throws IllegalArgumentException if <code>hour</code> parameter is outside value constraints for the field as specified in
     *                                  <a href="#datetimefieldmapping">date/time field mapping table</a>.
     */
    @Override
    public void setHour(int hour) {
        calendarValue = null;
        this.hour = hour;
    }

    /**
     * <p>Set minutes.</p>
     * <p>Unset this field by invoking the setter with a parameter value of {@link DatatypeConstants#FIELD_UNDEFINED}.</p>
     *
     * @param minute value constraints summarized in <a href="#datetimefield-minute">minute field of date/time field mapping table</a>.
     * @throws IllegalArgumentException if <code>minute</code> parameter is outside value constraints for the field as specified in
     *                                  <a href="#datetimefieldmapping">date/time field mapping table</a>.
     */
    @Override
    public void setMinute(int minute) {
        calendarValue = null;
        this.minute = minute;
    }

    /**
     * <p>Set seconds.</p>
     * <p>Unset this field by invoking the setter with a parameter value of {@link DatatypeConstants#FIELD_UNDEFINED}.</p>
     *
     * @param second value constraints summarized in <a href="#datetimefield-second">second field of date/time field mapping table</a>.
     * @throws IllegalArgumentException if <code>second</code> parameter is outside value constraints for the field as specified in
     *                                  <a href="#datetimefieldmapping">date/time field mapping table</a>.
     */
    @Override
    public void setSecond(int second) {
        calendarValue = null;
        this.second = second;
    }

    /**
     * <p>Set milliseconds.</p>
     * <p>Unset this field by invoking the setter with a parameter value of {@link DatatypeConstants#FIELD_UNDEFINED}.</p>
     *
     * @param millisecond value constraints summarized in
     *                    <a href="#datetimefield-millisecond">millisecond field of date/time field mapping table</a>.
     * @throws IllegalArgumentException if <code>millisecond</code> parameter is outside value constraints for the field as specified
     *                                  in <a href="#datetimefieldmapping">date/time field mapping table</a>.
     */
    @Override
    public void setMillisecond(int millisecond) {
        calendarValue = null;
        microsecond = millisecond * 1000;
    }

    /**
     * <p>Set fractional seconds.</p>
     * <p>Unset this field by invoking the setter with a parameter value of <code>null</code>.</p>
     *
     * @param fractional value constraints summarized in
     *                   <a href="#datetimefield-fractional">fractional field of date/time field mapping table</a>.
     * @throws IllegalArgumentException if <code>fractional</code> parameter is outside value constraints for the field as specified
     *                                  in <a href="#datetimefieldmapping">date/time field mapping table</a>.
     */
    @Override
    public void setFractionalSecond(/*@NotNull*/ BigDecimal fractional) {
        calendarValue = null;
        second = fractional.intValue();
        BigInteger micros = fractional.movePointRight(6).toBigInteger();
        micros = micros.remainder(BigInteger.valueOf(1000000));
        microsecond = micros.intValue();
    }

    /**
     * <p>Return high order component for XML Schema 1.0 dateTime datatype field for
     * <code>year</code>.
     * <code>null</code> if this optional part of the year field is not defined.</p>
     * <p>Value constraints for this value are summarized in
     * <a href="#datetimefield-year">year field of date/time field mapping table</a>.</p>
     *
     * @return eon of this <code>XMLGregorianCalendar</code>. The value
     *         returned is an integer multiple of 10^9.
     * @see #getYear()
     * @see #getEonAndYear()
     */
    @Override
    public BigInteger getEon() {
        return year.divide(BigInteger.valueOf(1000000000));
    }

    /**
     * <p>Return low order component for XML Schema 1.0 dateTime datatype field for
     * <code>year</code> or {@link DatatypeConstants#FIELD_UNDEFINED}.</p>
     * <p>Value constraints for this value are summarized in
     * <a href="#datetimefield-year">year field of date/time field mapping table</a>.</p>
     *
     * @return year  of this <code>XMLGregorianCalendar</code>.
     * @see #getEon()
     * @see #getEonAndYear()
     */
    @Override
    public int getYear() {
        return year.intValue();
    }

    /**
     * <p>Return XML Schema 1.0 dateTime datatype field for
     * <code>year</code>.</p>
     * <p>Value constraints for this value are summarized in
     * <a href="#datetimefield-year">year field of date/time field mapping table</a>.</p>
     *
     * @return sum of <code>eon</code> and <code>BigInteger.valueOf(year)</code>
     *         when both fields are defined. When only <code>year</code> is defined,
     *         return it. When both <code>eon</code> and <code>year</code> are not
     *         defined, return <code>null</code>.
     * @see #getEon()
     * @see #getYear()
     */
    /*@Nullable*/
    @Override
    public BigInteger getEonAndYear() {
        return year;
    }

    /**
     * <p>Return number of month or {@link DatatypeConstants#FIELD_UNDEFINED}.</p>
     * <p>Value constraints for this value are summarized in
     * <a href="#datetimefield-month">month field of date/time field mapping table</a>.</p>
     *
     * @return year  of this <code>XMLGregorianCalendar</code>.
     */
    @Override
    public int getMonth() {
        return month;
    }

    /**
     * Return day in month or {@link DatatypeConstants#FIELD_UNDEFINED}.
     * <p>Value constraints for this value are summarized in
     * <a href="#datetimefield-day">day field of date/time field mapping table</a>.</p>
     *
     * @see #setDay(int)
     */
    @Override
    public int getDay() {
        return day;
    }

    /**
     * Return timezone offset in minutes or
     * {@link DatatypeConstants#FIELD_UNDEFINED} if this optional field is not defined.
     * <p>Value constraints for this value are summarized in
     * <a href="#datetimefield-timezone">timezone field of date/time field mapping table</a>.</p>
     *
     * @see #setTimezone(int)
     */
    @Override
    public int getTimezone() {
        return tzOffset;
    }

    /**
     * Return hours or {@link DatatypeConstants#FIELD_UNDEFINED}.
     * Returns {@link DatatypeConstants#FIELD_UNDEFINED} if this field is not defined.
     * <p>Value constraints for this value are summarized in
     * <a href="#datetimefield-hour">hour field of date/time field mapping table</a>.</p>
     *
     * @see #setTime(int, int, int)
     */
    @Override
    public int getHour() {
        return hour;
    }

    /**
     * Return minutes or {@link DatatypeConstants#FIELD_UNDEFINED}.
     * <p>Returns {@link DatatypeConstants#FIELD_UNDEFINED} if this field is not defined.</p>
     * <p>Value constraints for this value are summarized in
     * <a href="#datetimefield-minute">minute field of date/time field mapping table</a>.</p>
     *
     * @see #setTime(int, int, int)
     */
    @Override
    public int getMinute() {
        return minute;
    }

    /**
     * <p>Return seconds or {@link DatatypeConstants#FIELD_UNDEFINED}.</p>
     * <p>Returns {@link DatatypeConstants#FIELD_UNDEFINED} if this field is not defined.
     * When this field is not defined, the optional xs:dateTime
     * fractional seconds field, represented by
     * {@link #getFractionalSecond()} and {@link #getMillisecond()},
     * must not be defined.</p>
     * <p>Value constraints for this value are summarized in
     * <a href="#datetimefield-second">second field of date/time field mapping table</a>.</p>
     *
     * @return Second  of this <code>XMLGregorianCalendar</code>.
     * @see #getFractionalSecond()
     * @see #getMillisecond()
     * @see #setTime(int, int, int)
     */
    @Override
    public int getSecond() {
        return second;
    }

    /**
     * <p>Return microsecond precision of {@link #getFractionalSecond()}.</p>
     * <p>This method represents a convenience accessor to infinite
     * precision fractional second value returned by
     * {@link #getFractionalSecond()}. The returned value is the rounded
     * down to microseconds value of
     * {@link #getFractionalSecond()}. When {@link #getFractionalSecond()}
     * returns <code>null</code>, this method must return
     * {@link DatatypeConstants#FIELD_UNDEFINED}.</p>
     * <p>Value constraints for this value are summarized in
     * <a href="#datetimefield-second">second field of date/time field mapping table</a>.</p>
     *
     * @return Millisecond  of this <code>XMLGregorianCalendar</code>.
     * @see #getFractionalSecond()
     * @see #setTime(int, int, int)
     */
    public int getMicrosecond() {

        BigDecimal fractionalSeconds = getFractionalSecond();

        // is field undefined?
        if (fractionalSeconds == null) {
            return DatatypeConstants.FIELD_UNDEFINED;
        }

        return getFractionalSecond().movePointRight(6).intValue();
    }


    /**
     * <p>Return fractional seconds.</p>
     * <p><code>null</code> is returned when this optional field is not defined.</p>
     * <p>Value constraints are detailed in
     * <a href="#datetimefield-second">second field of date/time field mapping table</a>.</p>
     * <p>This optional field can only have a defined value when the
     * xs:dateTime second field, represented by {@link #getSecond()},
     * does not return {@link DatatypeConstants#FIELD_UNDEFINED}.</p>
     *
     * @return fractional seconds  of this <code>XMLGregorianCalendar</code>.
     * @see #getSecond()
     * @see #setTime(int, int, int, BigDecimal)
     */
    /*@Nullable*/
    @Override
    public BigDecimal getFractionalSecond() {
        if (second == DatatypeConstants.FIELD_UNDEFINED) {
            return null;
        }
        return BigDecimal.valueOf(microsecond).movePointLeft(6);
    }

    /**
     * <p>Compare two instances of W3C XML Schema 1.0 date/time datatypes
     * according to partial order relation defined in
     * <a href="http://www.w3.org/TR/xmlschema-2/#dateTime-order">W3C XML Schema 1.0 Part 2, Section 3.2.7.3,
     * <i>Order relation on dateTime</i></a>.</p>
     * <p><code>xsd:dateTime</code> datatype field mapping to accessors of
     * this class are defined in
     * <a href="#datetimefieldmapping">date/time field mapping table</a>.</p>
     *
     * @param xmlGregorianCalendar Instance of <code>XMLGregorianCalendar</code> to compare
     * @return The relationship between <code>this</code> <code>XMLGregorianCalendar</code> and
     *         the specified <code>xmlGregorianCalendar</code> as
     *         {@link DatatypeConstants#LESSER},
     *         {@link DatatypeConstants#EQUAL},
     *         {@link DatatypeConstants#GREATER} or
     *         {@link DatatypeConstants#INDETERMINATE}.
     * @throws NullPointerException if <code>xmlGregorianCalendar</code> is null.
     */
    @Override
    public int compare(/*@NotNull*/ XMLGregorianCalendar xmlGregorianCalendar) {
        return toCalendarValue().getSchemaComparable().compareTo(
                ((SaxonXMLGregorianCalendar) xmlGregorianCalendar).toCalendarValue().getSchemaComparable());
    }

    /**
     * <p>Normalize this instance to UTC.</p>
     * <p>2000-03-04T23:00:00+03:00 normalizes to 2000-03-04T20:00:00Z</p>
     * <p>Implements W3C XML Schema Part 2, Section 3.2.7.3 (A).</p>
     *
     * @return a copy of this <code>XMLGregorianCalendar</code> normalized to UTC.
     */
    /*@NotNull*/
    @Override
    public XMLGregorianCalendar normalize() {
        return new SaxonXMLGregorianCalendar(toCalendarValue().adjustTimezone(0));
    }

    /**
     * <p>Return the lexical representation of <code>this</code> instance.
     * The format is specified in
     * <a href="http://www.w3.org/TR/xmlschema-2/#dateTime-order">XML Schema 1.0 Part 2, Section 3.2.[7-14].1,
     * <i>Lexical Representation</i>".</a></p>
     * <p>Specific target lexical representation format is determined by
     * {@link #getXMLSchemaType()}.</p>
     *
     * @return XML, as <code>String</code>, representation of this <code>XMLGregorianCalendar</code>
     * @throws IllegalStateException if the combination of set fields
     *                               does not match one of the eight defined XML Schema builtin date/time datatypes.
     */
    @Override
    public String toXMLFormat() {
        return toCalendarValue().getStringValue();
    }

    /**
     * <p>Return the name of the XML Schema date/time type that this instance
     * maps to. Type is computed based on fields that are set.</p>
     *
     * @return One of the following class constants:
     *         {@link DatatypeConstants#DATETIME},
     *         {@link DatatypeConstants#TIME},
     *         {@link DatatypeConstants#DATE},
     *         {@link DatatypeConstants#GYEARMONTH},
     *         {@link DatatypeConstants#GMONTHDAY},
     *         {@link DatatypeConstants#GYEAR},
     *         {@link DatatypeConstants#GMONTH} or
     *         {@link DatatypeConstants#GDAY}.
     * @throws IllegalStateException if the combination of set fields
     *                               does not match one of the eight defined XML Schema builtin
     *                               date/time datatypes.
     */
    @Override
    public QName getXMLSchemaType() {
        if (second == DatatypeConstants.FIELD_UNDEFINED) {
            if (year == null) {
                if (month == DatatypeConstants.FIELD_UNDEFINED) {
                    return DatatypeConstants.GDAY;
                } else if (day == DatatypeConstants.FIELD_UNDEFINED) {
                    return DatatypeConstants.GMONTH;
                } else {
                    return DatatypeConstants.GMONTHDAY;
                }
            } else if (day == DatatypeConstants.FIELD_UNDEFINED) {
                if (month == DatatypeConstants.FIELD_UNDEFINED) {
                    return DatatypeConstants.GYEAR;
                } else {
                    return DatatypeConstants.GYEARMONTH;
                }
            }
            return DatatypeConstants.DATE;
        } else if (year == null) {
            return DatatypeConstants.TIME;
        } else {
            return DatatypeConstants.DATETIME;
        }
    }

    /**
     * Validate instance by <code>getXMLSchemaType()</code> constraints.
     *
     * @return true if data values are valid.
     */
    @Override
    public boolean isValid() {
        return true;
    }

    /**
     * <p>Add <code>duration</code> to this instance.</p>
     * <p>The computation is specified in
     * <a href="http://www.w3.org/TR/xmlschema-2/#adding-durations-to-dateTimes">XML Schema 1.0 Part 2, Appendix E,
     * <i>Adding durations to dateTimes</i></a>.
     * <a href="#datetimefieldsmapping">date/time field mapping table</a>
     * defines the mapping from XML Schema 1.0 <code>dateTime</code> fields
     * to this class' representation of those fields.</p>
     *
     * @param duration Duration to add to this <code>XMLGregorianCalendar</code>.
     * @throws NullPointerException when <code>duration</code> parameter is <code>null</code>.
     */
    @Override
    public void add(/*@NotNull*/ Duration duration) {
        try {
            CalendarValue cv = toCalendarValue().add(((SaxonDuration) duration).getDurationValue());
            setCalendarValue(cv);
        } catch (XPathException err) {
            throw new IllegalArgumentException(err.getMessage());
        }
    }

    /**
     * <p>Convert this <code>XMLGregorianCalendar</code> to a {@link GregorianCalendar}.</p>
     * <p>When <code>this</code> instance has an undefined field, this
     * conversion relies on the <code>java.util.GregorianCalendar</code> default
     * for its corresponding field. A notable difference between
     * XML Schema 1.0 date/time datatypes and <code>java.util.GregorianCalendar</code>
     * is that Timezone value is optional for date/time datatypes and it is
     * a required field for <code>java.util.GregorianCalendar</code>. See javadoc
     * for <code>java.util.TimeZone.getDefault()</code> on how the default
     * is determined. To explicitly specify the <code>TimeZone</code>
     * instance, see
     * {@link #toGregorianCalendar(TimeZone, Locale, XMLGregorianCalendar)}.</p>
     *
     * @see #toGregorianCalendar(TimeZone, Locale, XMLGregorianCalendar)
     */
    @Override
    public GregorianCalendar toGregorianCalendar() {
        return toCalendarValue().getCalendar();
    }

    /**
     * <p>Convert this <code>XMLGregorianCalendar</code> along with provided parameters
     * to a {@link GregorianCalendar} instance.</p>
     * <p> Since XML Schema 1.0 date/time datetypes has no concept of
     * timezone ids or daylight savings timezone ids, this conversion operation
     * allows the user to explicitly specify one with
     * <code>timezone</code> parameter.</p>
     * <p>To compute the return value's <code>TimeZone</code> field:</p>
     * <ul>
     * <li>when parameter <code>timeZone</code> is non-null,
     * it is the timezone field.</li>
     * <li>else when <code>this.getTimezone() != FIELD_UNDEFINED</code>,
     * create a <code>java.util.TimeZone</code> with a custom timezone id
     * using the <code>this.getTimezone()</code>.</li>
     * <li>else when <code>defaults.getTimezone() != FIELD_UNDEFINED</code>,
     * create a <code>java.util.TimeZone</code> with a custom timezone id
     * using <code>defaults.getTimezone()</code>.</li>
     * <li>else use the <code>GregorianCalendar</code> default timezone value
     * for the host is defined as specified by
     * <code>java.util.TimeZone.getDefault()</code>.</li></ul>
     * <p>To ensure consistency in conversion implementations, the new
     * <code>GregorianCalendar</code> should be instantiated in the following
     * manner:</p>
     * <ul>
     * <li>Create a new <code>java.util.GregorianCalendar(TimeZone,
     * Locale)</code> with TimeZone set as specified above and the
     * <code>Locale</code> parameter.</li>
     * <li>Initialize all GregorianCalendar fields by calling {@link GregorianCalendar#clear()}</li>
     * <li>Obtain a pure Gregorian Calendar by invoking
     * <code>GregorianCalendar.setGregorianChange(
     * new Date(Long.MIN_VALUE))</code>.</li>
     * <li>Its fields ERA, YEAR, MONTH, DAY_OF_MONTH, HOUR_OF_DAY,
     * MINUTE, SECOND and MILLISECOND are set using the method
     * <code>Calendar.set(int,int)</code></li>
     * </ul>
     *
     * @param timezone provide Timezone. <code>null</code> is a legal value.
     * @param aLocale  provide explicit Locale. Use default GregorianCalendar locale if
     *                 value is <code>null</code>.
     * @param defaults provide default field values to use when corresponding
     *                 field for this instance is FIELD_UNDEFINED or null.
     *                 If <code>defaults</code>is <code>null</code> or a field
     *                 within the specified <code>defaults</code> is undefined,
     *                 just use <code>java.util.GregorianCalendar</code> defaults.
     * @return a java.util.GregorianCalendar conversion of this instance.
     */
    /*@NotNull*/
    @Override
    public GregorianCalendar toGregorianCalendar(TimeZone timezone, Locale aLocale, /*@NotNull*/ XMLGregorianCalendar defaults) {
        GregorianCalendar gc = new GregorianCalendar(timezone, aLocale);
        gc.setGregorianChange(new Date(Long.MIN_VALUE));
        gc.set(Calendar.ERA, year == null ? defaults.getYear() > 0 ? +1 : -1 : year.signum());
        gc.set(Calendar.YEAR, year == null ? defaults.getYear() : year.abs().intValue());
        gc.set(Calendar.MONTH, month == DatatypeConstants.FIELD_UNDEFINED ? defaults.getMonth() : month);
        gc.set(Calendar.DAY_OF_MONTH, day == DatatypeConstants.FIELD_UNDEFINED ? defaults.getDay() : day);
        gc.set(Calendar.HOUR, hour == DatatypeConstants.FIELD_UNDEFINED ? defaults.getHour() : hour);
        gc.set(Calendar.MINUTE, minute == DatatypeConstants.FIELD_UNDEFINED ? defaults.getMinute() : minute);
        gc.set(Calendar.SECOND, second == DatatypeConstants.FIELD_UNDEFINED ? defaults.getSecond() : second);
        gc.set(Calendar.MILLISECOND, microsecond == DatatypeConstants.FIELD_UNDEFINED
                ? defaults.getMillisecond() : microsecond / 1000);
        return gc;
    }

    /**
     * <p>Returns a <code>java.util.TimeZone</code> for this class.</p>
     * <p>If timezone field is defined for this instance,
     * returns TimeZone initialized with custom timezone id
     * of zoneoffset. If timezone field is undefined,
     * try the defaultZoneoffset that was passed in.
     * If defaultZoneoffset is FIELD_UNDEFINED, return
     * default timezone for this host.
     * (Same default as java.util.GregorianCalendar).</p>
     *
     * @param defaultZoneoffset default zoneoffset if this zoneoffset is
     *                          {@link DatatypeConstants#FIELD_UNDEFINED}.
     * @return TimeZone for this.
     */
    @Override
    public TimeZone getTimeZone(int defaultZoneoffset) {
        if (tzOffset == DatatypeConstants.FIELD_UNDEFINED) {
            if (defaultZoneoffset == DatatypeConstants.FIELD_UNDEFINED) {
                return new GregorianCalendar().getTimeZone();
            } else {
                return new SimpleTimeZone(defaultZoneoffset * 60000, "XXX");
            }
        } else {
            return new SimpleTimeZone(tzOffset * 60000, "XXX");
        }
    }

    /**
     * <p>Creates and returns a copy of this object.</p>
     *
     * @return copy of this <code>Object</code>
     */
    /*@NotNull*/
    @Override
    @SuppressWarnings({"CloneDoesntCallSuperClone"})
    public Object clone() {
        SaxonXMLGregorianCalendar s = new SaxonXMLGregorianCalendar();
        s.setYear(year);
        s.setMonth(month);
        s.setDay(day);
        s.setHour(hour);
        s.setMinute(minute);
        s.setSecond(second);
        s.setMillisecond(microsecond / 1000);
        s.setTimezone(tzOffset);
        return s;
    }

    /**
     * Convert this SaxonXMLGregorianCalendar to a Saxon CalendarValue object
     *
     * @return the corresponding CalendarValue
     */

    /*@Nullable*/
    public CalendarValue toCalendarValue() {
        if (calendarValue != null) {
            return calendarValue;
        }
        if (second == DatatypeConstants.FIELD_UNDEFINED) {
            if (year == null) {
                if (month == DatatypeConstants.FIELD_UNDEFINED) {
                    return new GDayValue((byte) day, tzOffset);
                } else if (day == DatatypeConstants.FIELD_UNDEFINED) {
                    return new GMonthValue((byte) month, tzOffset);
                } else {
                    return new GMonthDayValue((byte) month, (byte) day, tzOffset);
                }
            } else if (day == DatatypeConstants.FIELD_UNDEFINED) {
                if (month == DatatypeConstants.FIELD_UNDEFINED) {
                    return new GYearValue(year.intValue(), tzOffset, true);
                } else {
                    return new GYearMonthValue(year.intValue(), (byte) month, tzOffset, true);
                }
            }
            return new DateValue(year.intValue(), (byte) month, (byte) day, tzOffset, true);
        } else if (year == null) {
            return new TimeValue((byte) hour, (byte) minute, (byte) second, getMicrosecond()*1000, tzOffset, "");
        } else {
            return new DateTimeValue(year.intValue(), (byte) month, (byte) day,
                    (byte) hour, (byte) minute, (byte) second, getMicrosecond(), tzOffset, true);
        }
    }
}

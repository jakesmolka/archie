package com.nedap.archie.rm.datavalues.quantity;

import com.nedap.archie.rm.datavalues.quantity.datetime.DvDateTime;

import javax.annotation.Nullable;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElements;
import javax.xml.bind.annotation.XmlTransient;
import javax.xml.bind.annotation.XmlType;

/**
 * Created by pieter.bos on 04/11/15.
 */
@XmlAccessorType(XmlAccessType.PROPERTY)
@XmlType(name = "DV_QUANTIFIED", propOrder = {
        "magnitudeStatus",
        "accuracy",
        "magnitude"
})
public abstract class DvQuantified<AccuracyType, MagnitudeType extends Comparable> extends DvOrdered<MagnitudeType> {

    @Nullable
    private String magnitudeStatus;
    @Nullable
    private AccuracyType accuracy;
    private MagnitudeType magnitude;

    @Nullable
    @XmlElement(name = "magnitude_status")
    public String getMagnitudeStatus() {
        return magnitudeStatus;
    }

    public void setMagnitudeStatus(@Nullable String magnitudeStatus) {
        this.magnitudeStatus = magnitudeStatus;
    }

    @XmlElements({
            @XmlElement(type = Long.class),
            @XmlElement(type = Double.class)

    })
    public AccuracyType getAccuracy() {
        return accuracy;
    }

    public void setAccuracy(AccuracyType accuracy) {
        this.accuracy = accuracy;
    }

    @XmlElements({
            @XmlElement(type = Long.class),
            @XmlElement(type = Double.class)

    })
    public MagnitudeType getMagnitude() {
        return magnitude;
    }

    public void setMagnitude(MagnitudeType magnitude) {
        this.magnitude = magnitude;
    }

    @Override
    public int compareTo(MagnitudeType other) {
        return magnitude.compareTo(other);
    }
}
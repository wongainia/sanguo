// Generated by http://code.google.com/p/protostuff/ ... DO NOT EDIT!
// Generated from common.proto

package com.vikings.sanguo.protos;

import java.io.Externalizable;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;

import com.dyuproject.protostuff.GraphIOUtil;
import com.dyuproject.protostuff.Input;
import com.dyuproject.protostuff.Message;
import com.dyuproject.protostuff.Output;
import com.dyuproject.protostuff.Schema;

public final class RoundRegion implements Externalizable, Message<RoundRegion>, Schema<RoundRegion>
{

    public static Schema<RoundRegion> getSchema()
    {
        return DEFAULT_INSTANCE;
    }

    public static RoundRegion getDefaultInstance()
    {
        return DEFAULT_INSTANCE;
    }

    static final RoundRegion DEFAULT_INSTANCE = new RoundRegion();

    
    private Long center;
    private Integer radius;

    public RoundRegion()
    {
        
    }

    // getters and setters

    // center

    public boolean hasCenter(){
        return center != null;
    }


    public Long getCenter()
    {
        return center == null ? 0L : center;
    }

    public RoundRegion setCenter(Long center)
    {
        this.center = center;
        return this;
    }

    // radius

    public boolean hasRadius(){
        return radius != null;
    }


    public Integer getRadius()
    {
        return radius == null ? 0 : radius;
    }

    public RoundRegion setRadius(Integer radius)
    {
        this.radius = radius;
        return this;
    }

    // java serialization

    public void readExternal(ObjectInput in) throws IOException
    {
        GraphIOUtil.mergeDelimitedFrom(in, this, this);
    }

    public void writeExternal(ObjectOutput out) throws IOException
    {
        GraphIOUtil.writeDelimitedTo(out, this, this);
    }

    // message method

    public Schema<RoundRegion> cachedSchema()
    {
        return DEFAULT_INSTANCE;
    }

    // schema methods

    public RoundRegion newMessage()
    {
        return new RoundRegion();
    }

    public Class<RoundRegion> typeClass()
    {
        return RoundRegion.class;
    }

    public String messageName()
    {
        return RoundRegion.class.getSimpleName();
    }

    public String messageFullName()
    {
        return RoundRegion.class.getName();
    }

    public boolean isInitialized(RoundRegion message)
    {
        return true;
    }

    public void mergeFrom(Input input, RoundRegion message) throws IOException
    {
        for(int number = input.readFieldNumber(this);; number = input.readFieldNumber(this))
        {
            switch(number)
            {
                case 0:
                    return;
                case 10:
                    message.center = input.readUInt64();
                    break;
                case 20:
                    message.radius = input.readUInt32();
                    break;
                default:
                    input.handleUnknownField(number, this);
            }   
        }
    }


    public void writeTo(Output output, RoundRegion message) throws IOException
    {
        if(message.center != null)
            output.writeUInt64(10, message.center, false);

        if(message.radius != null)
            output.writeUInt32(20, message.radius, false);
    }

    public String getFieldName(int number)
    {
        return Integer.toString(number);
    }

    public int getFieldNumber(String name)
    {
        return Integer.parseInt(name);
    }
    
}

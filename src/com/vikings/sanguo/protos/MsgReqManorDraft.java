// Generated by http://code.google.com/p/protostuff/ ... DO NOT EDIT!
// Generated from client.proto

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

public final class MsgReqManorDraft implements Externalizable, Message<MsgReqManorDraft>, Schema<MsgReqManorDraft>
{

    public static Schema<MsgReqManorDraft> getSchema()
    {
        return DEFAULT_INSTANCE;
    }

    public static MsgReqManorDraft getDefaultInstance()
    {
        return DEFAULT_INSTANCE;
    }

    static final MsgReqManorDraft DEFAULT_INSTANCE = new MsgReqManorDraft();

    
    private Integer buildingid;
    private Integer propid;
    private Integer type;
    private Integer count;

    public MsgReqManorDraft()
    {
        
    }

    // getters and setters

    // buildingid

    public boolean hasBuildingid(){
        return buildingid != null;
    }


    public Integer getBuildingid()
    {
        return buildingid == null ? 0 : buildingid;
    }

    public MsgReqManorDraft setBuildingid(Integer buildingid)
    {
        this.buildingid = buildingid;
        return this;
    }

    // propid

    public boolean hasPropid(){
        return propid != null;
    }


    public Integer getPropid()
    {
        return propid == null ? 0 : propid;
    }

    public MsgReqManorDraft setPropid(Integer propid)
    {
        this.propid = propid;
        return this;
    }

    // type

    public boolean hasType(){
        return type != null;
    }


    public Integer getType()
    {
        return type == null ? 0 : type;
    }

    public MsgReqManorDraft setType(Integer type)
    {
        this.type = type;
        return this;
    }

    // count

    public boolean hasCount(){
        return count != null;
    }


    public Integer getCount()
    {
        return count == null ? 0 : count;
    }

    public MsgReqManorDraft setCount(Integer count)
    {
        this.count = count;
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

    public Schema<MsgReqManorDraft> cachedSchema()
    {
        return DEFAULT_INSTANCE;
    }

    // schema methods

    public MsgReqManorDraft newMessage()
    {
        return new MsgReqManorDraft();
    }

    public Class<MsgReqManorDraft> typeClass()
    {
        return MsgReqManorDraft.class;
    }

    public String messageName()
    {
        return MsgReqManorDraft.class.getSimpleName();
    }

    public String messageFullName()
    {
        return MsgReqManorDraft.class.getName();
    }

    public boolean isInitialized(MsgReqManorDraft message)
    {
        return true;
    }

    public void mergeFrom(Input input, MsgReqManorDraft message) throws IOException
    {
        for(int number = input.readFieldNumber(this);; number = input.readFieldNumber(this))
        {
            switch(number)
            {
                case 0:
                    return;
                case 10:
                    message.buildingid = input.readUInt32();
                    break;
                case 20:
                    message.propid = input.readUInt32();
                    break;
                case 30:
                    message.type = input.readUInt32();
                    break;
                case 40:
                    message.count = input.readUInt32();
                    break;
                default:
                    input.handleUnknownField(number, this);
            }   
        }
    }


    public void writeTo(Output output, MsgReqManorDraft message) throws IOException
    {
        if(message.buildingid != null)
            output.writeUInt32(10, message.buildingid, false);

        if(message.propid != null)
            output.writeUInt32(20, message.propid, false);

        if(message.type != null)
            output.writeUInt32(30, message.type, false);

        if(message.count != null)
            output.writeUInt32(40, message.count, false);
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

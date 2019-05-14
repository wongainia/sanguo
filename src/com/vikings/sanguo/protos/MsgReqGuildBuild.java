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

public final class MsgReqGuildBuild implements Externalizable, Message<MsgReqGuildBuild>, Schema<MsgReqGuildBuild>
{

    public static Schema<MsgReqGuildBuild> getSchema()
    {
        return DEFAULT_INSTANCE;
    }

    public static MsgReqGuildBuild getDefaultInstance()
    {
        return DEFAULT_INSTANCE;
    }

    static final MsgReqGuildBuild DEFAULT_INSTANCE = new MsgReqGuildBuild();

    
    private String name;
    private Integer type;

    public MsgReqGuildBuild()
    {
        
    }

    // getters and setters

    // name

    public boolean hasName(){
        return name != null;
    }


    public String getName()
    {
        return name == null ? "" : name;
    }

    public MsgReqGuildBuild setName(String name)
    {
        this.name = name;
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

    public MsgReqGuildBuild setType(Integer type)
    {
        this.type = type;
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

    public Schema<MsgReqGuildBuild> cachedSchema()
    {
        return DEFAULT_INSTANCE;
    }

    // schema methods

    public MsgReqGuildBuild newMessage()
    {
        return new MsgReqGuildBuild();
    }

    public Class<MsgReqGuildBuild> typeClass()
    {
        return MsgReqGuildBuild.class;
    }

    public String messageName()
    {
        return MsgReqGuildBuild.class.getSimpleName();
    }

    public String messageFullName()
    {
        return MsgReqGuildBuild.class.getName();
    }

    public boolean isInitialized(MsgReqGuildBuild message)
    {
        return true;
    }

    public void mergeFrom(Input input, MsgReqGuildBuild message) throws IOException
    {
        for(int number = input.readFieldNumber(this);; number = input.readFieldNumber(this))
        {
            switch(number)
            {
                case 0:
                    return;
                case 10:
                    message.name = input.readString();
                    break;
                case 20:
                    message.type = input.readUInt32();
                    break;
                default:
                    input.handleUnknownField(number, this);
            }   
        }
    }


    public void writeTo(Output output, MsgReqGuildBuild message) throws IOException
    {
        if(message.name != null)
            output.writeString(10, message.name, false);

        if(message.type != null)
            output.writeUInt32(20, message.type, false);
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

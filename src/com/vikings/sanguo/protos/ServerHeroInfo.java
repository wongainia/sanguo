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

public final class ServerHeroInfo implements Externalizable, Message<ServerHeroInfo>, Schema<ServerHeroInfo>
{

    public static Schema<ServerHeroInfo> getSchema()
    {
        return DEFAULT_INSTANCE;
    }

    public static ServerHeroInfo getDefaultInstance()
    {
        return DEFAULT_INSTANCE;
    }

    static final ServerHeroInfo DEFAULT_INSTANCE = new ServerHeroInfo();

    
    private Integer initScheme;

    public ServerHeroInfo()
    {
        
    }

    // getters and setters

    // initScheme

    public boolean hasInitScheme(){
        return initScheme != null;
    }


    public Integer getInitScheme()
    {
        return initScheme == null ? 0 : initScheme;
    }

    public ServerHeroInfo setInitScheme(Integer initScheme)
    {
        this.initScheme = initScheme;
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

    public Schema<ServerHeroInfo> cachedSchema()
    {
        return DEFAULT_INSTANCE;
    }

    // schema methods

    public ServerHeroInfo newMessage()
    {
        return new ServerHeroInfo();
    }

    public Class<ServerHeroInfo> typeClass()
    {
        return ServerHeroInfo.class;
    }

    public String messageName()
    {
        return ServerHeroInfo.class.getSimpleName();
    }

    public String messageFullName()
    {
        return ServerHeroInfo.class.getName();
    }

    public boolean isInitialized(ServerHeroInfo message)
    {
        return true;
    }

    public void mergeFrom(Input input, ServerHeroInfo message) throws IOException
    {
        for(int number = input.readFieldNumber(this);; number = input.readFieldNumber(this))
        {
            switch(number)
            {
                case 0:
                    return;
                case 10:
                    message.initScheme = input.readUInt32();
                    break;
                default:
                    input.handleUnknownField(number, this);
            }   
        }
    }


    public void writeTo(Output output, ServerHeroInfo message) throws IOException
    {
        if(message.initScheme != null)
            output.writeUInt32(10, message.initScheme, false);
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

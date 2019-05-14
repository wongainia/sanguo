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

public final class BlacklistInfo implements Externalizable, Message<BlacklistInfo>, Schema<BlacklistInfo>
{

    public static Schema<BlacklistInfo> getSchema()
    {
        return DEFAULT_INSTANCE;
    }

    public static BlacklistInfo getDefaultInstance()
    {
        return DEFAULT_INSTANCE;
    }

    static final BlacklistInfo DEFAULT_INSTANCE = new BlacklistInfo();

    
    private BaseBlacklistInfo bi;
    private ServerBlacklistInfo si;

    public BlacklistInfo()
    {
        
    }

    // getters and setters

    // bi

    public boolean hasBi(){
        return bi != null;
    }


    public BaseBlacklistInfo getBi()
    {
        return bi == null ? new BaseBlacklistInfo() : bi;
    }

    public BlacklistInfo setBi(BaseBlacklistInfo bi)
    {
        this.bi = bi;
        return this;
    }

    // si

    public boolean hasSi(){
        return si != null;
    }


    public ServerBlacklistInfo getSi()
    {
        return si == null ? new ServerBlacklistInfo() : si;
    }

    public BlacklistInfo setSi(ServerBlacklistInfo si)
    {
        this.si = si;
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

    public Schema<BlacklistInfo> cachedSchema()
    {
        return DEFAULT_INSTANCE;
    }

    // schema methods

    public BlacklistInfo newMessage()
    {
        return new BlacklistInfo();
    }

    public Class<BlacklistInfo> typeClass()
    {
        return BlacklistInfo.class;
    }

    public String messageName()
    {
        return BlacklistInfo.class.getSimpleName();
    }

    public String messageFullName()
    {
        return BlacklistInfo.class.getName();
    }

    public boolean isInitialized(BlacklistInfo message)
    {
        return true;
    }

    public void mergeFrom(Input input, BlacklistInfo message) throws IOException
    {
        for(int number = input.readFieldNumber(this);; number = input.readFieldNumber(this))
        {
            switch(number)
            {
                case 0:
                    return;
                case 10:
                    message.bi = input.mergeObject(message.bi, BaseBlacklistInfo.getSchema());
                    break;

                case 20:
                    message.si = input.mergeObject(message.si, ServerBlacklistInfo.getSchema());
                    break;

                default:
                    input.handleUnknownField(number, this);
            }   
        }
    }


    public void writeTo(Output output, BlacklistInfo message) throws IOException
    {
        if(message.bi != null)
             output.writeObject(10, message.bi, BaseBlacklistInfo.getSchema(), false);


        if(message.si != null)
             output.writeObject(20, message.si, ServerBlacklistInfo.getSchema(), false);

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
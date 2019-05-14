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

public final class ExGuildInfo implements Externalizable, Message<ExGuildInfo>, Schema<ExGuildInfo>
{

    public static Schema<ExGuildInfo> getSchema()
    {
        return DEFAULT_INSTANCE;
    }

    public static ExGuildInfo getDefaultInstance()
    {
        return DEFAULT_INSTANCE;
    }

    static final ExGuildInfo DEFAULT_INSTANCE = new ExGuildInfo();

    
    private DataCtrl ctrl;
    private GuildInfo info;

    public ExGuildInfo()
    {
        
    }

    // getters and setters

    // ctrl

    public boolean hasCtrl(){
        return ctrl != null;
    }


    public DataCtrl getCtrl()
    {
        return ctrl == null ? new DataCtrl() : ctrl;
    }

    public ExGuildInfo setCtrl(DataCtrl ctrl)
    {
        this.ctrl = ctrl;
        return this;
    }

    // info

    public boolean hasInfo(){
        return info != null;
    }


    public GuildInfo getInfo()
    {
        return info == null ? new GuildInfo() : info;
    }

    public ExGuildInfo setInfo(GuildInfo info)
    {
        this.info = info;
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

    public Schema<ExGuildInfo> cachedSchema()
    {
        return DEFAULT_INSTANCE;
    }

    // schema methods

    public ExGuildInfo newMessage()
    {
        return new ExGuildInfo();
    }

    public Class<ExGuildInfo> typeClass()
    {
        return ExGuildInfo.class;
    }

    public String messageName()
    {
        return ExGuildInfo.class.getSimpleName();
    }

    public String messageFullName()
    {
        return ExGuildInfo.class.getName();
    }

    public boolean isInitialized(ExGuildInfo message)
    {
        return true;
    }

    public void mergeFrom(Input input, ExGuildInfo message) throws IOException
    {
        for(int number = input.readFieldNumber(this);; number = input.readFieldNumber(this))
        {
            switch(number)
            {
                case 0:
                    return;
                case 10:
                    message.ctrl = input.mergeObject(message.ctrl, DataCtrl.getSchema());
                    break;

                case 20:
                    message.info = input.mergeObject(message.info, GuildInfo.getSchema());
                    break;

                default:
                    input.handleUnknownField(number, this);
            }   
        }
    }


    public void writeTo(Output output, ExGuildInfo message) throws IOException
    {
        if(message.ctrl != null)
             output.writeObject(10, message.ctrl, DataCtrl.getSchema(), false);


        if(message.info != null)
             output.writeObject(20, message.info, GuildInfo.getSchema(), false);

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

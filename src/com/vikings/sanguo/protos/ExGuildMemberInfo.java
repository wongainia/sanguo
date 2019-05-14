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

public final class ExGuildMemberInfo implements Externalizable, Message<ExGuildMemberInfo>, Schema<ExGuildMemberInfo>
{

    public static Schema<ExGuildMemberInfo> getSchema()
    {
        return DEFAULT_INSTANCE;
    }

    public static ExGuildMemberInfo getDefaultInstance()
    {
        return DEFAULT_INSTANCE;
    }

    static final ExGuildMemberInfo DEFAULT_INSTANCE = new ExGuildMemberInfo();

    
    private DataCtrl ctrl;
    private GuildMemberInfo info;

    public ExGuildMemberInfo()
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

    public ExGuildMemberInfo setCtrl(DataCtrl ctrl)
    {
        this.ctrl = ctrl;
        return this;
    }

    // info

    public boolean hasInfo(){
        return info != null;
    }


    public GuildMemberInfo getInfo()
    {
        return info == null ? new GuildMemberInfo() : info;
    }

    public ExGuildMemberInfo setInfo(GuildMemberInfo info)
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

    public Schema<ExGuildMemberInfo> cachedSchema()
    {
        return DEFAULT_INSTANCE;
    }

    // schema methods

    public ExGuildMemberInfo newMessage()
    {
        return new ExGuildMemberInfo();
    }

    public Class<ExGuildMemberInfo> typeClass()
    {
        return ExGuildMemberInfo.class;
    }

    public String messageName()
    {
        return ExGuildMemberInfo.class.getSimpleName();
    }

    public String messageFullName()
    {
        return ExGuildMemberInfo.class.getName();
    }

    public boolean isInitialized(ExGuildMemberInfo message)
    {
        return true;
    }

    public void mergeFrom(Input input, ExGuildMemberInfo message) throws IOException
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
                    message.info = input.mergeObject(message.info, GuildMemberInfo.getSchema());
                    break;

                default:
                    input.handleUnknownField(number, this);
            }   
        }
    }


    public void writeTo(Output output, ExGuildMemberInfo message) throws IOException
    {
        if(message.ctrl != null)
             output.writeObject(10, message.ctrl, DataCtrl.getSchema(), false);


        if(message.info != null)
             output.writeObject(20, message.info, GuildMemberInfo.getSchema(), false);

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

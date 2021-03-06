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

public final class ExRoleInfo implements Externalizable, Message<ExRoleInfo>, Schema<ExRoleInfo>
{

    public static Schema<ExRoleInfo> getSchema()
    {
        return DEFAULT_INSTANCE;
    }

    public static ExRoleInfo getDefaultInstance()
    {
        return DEFAULT_INSTANCE;
    }

    static final ExRoleInfo DEFAULT_INSTANCE = new ExRoleInfo();

    
    private DataCtrl ctrl;
    private RoleInfo info;

    public ExRoleInfo()
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

    public ExRoleInfo setCtrl(DataCtrl ctrl)
    {
        this.ctrl = ctrl;
        return this;
    }

    // info

    public boolean hasInfo(){
        return info != null;
    }


    public RoleInfo getInfo()
    {
        return info == null ? new RoleInfo() : info;
    }

    public ExRoleInfo setInfo(RoleInfo info)
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

    public Schema<ExRoleInfo> cachedSchema()
    {
        return DEFAULT_INSTANCE;
    }

    // schema methods

    public ExRoleInfo newMessage()
    {
        return new ExRoleInfo();
    }

    public Class<ExRoleInfo> typeClass()
    {
        return ExRoleInfo.class;
    }

    public String messageName()
    {
        return ExRoleInfo.class.getSimpleName();
    }

    public String messageFullName()
    {
        return ExRoleInfo.class.getName();
    }

    public boolean isInitialized(ExRoleInfo message)
    {
        return true;
    }

    public void mergeFrom(Input input, ExRoleInfo message) throws IOException
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
                    message.info = input.mergeObject(message.info, RoleInfo.getSchema());
                    break;

                default:
                    input.handleUnknownField(number, this);
            }   
        }
    }


    public void writeTo(Output output, ExRoleInfo message) throws IOException
    {
        if(message.ctrl != null)
             output.writeObject(10, message.ctrl, DataCtrl.getSchema(), false);


        if(message.info != null)
             output.writeObject(20, message.info, RoleInfo.getSchema(), false);

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

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

public final class ExEquipmentInfo implements Externalizable, Message<ExEquipmentInfo>, Schema<ExEquipmentInfo>
{

    public static Schema<ExEquipmentInfo> getSchema()
    {
        return DEFAULT_INSTANCE;
    }

    public static ExEquipmentInfo getDefaultInstance()
    {
        return DEFAULT_INSTANCE;
    }

    static final ExEquipmentInfo DEFAULT_INSTANCE = new ExEquipmentInfo();

    
    private DataCtrl ctrl;
    private EquipmentInfo info;

    public ExEquipmentInfo()
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

    public ExEquipmentInfo setCtrl(DataCtrl ctrl)
    {
        this.ctrl = ctrl;
        return this;
    }

    // info

    public boolean hasInfo(){
        return info != null;
    }


    public EquipmentInfo getInfo()
    {
        return info == null ? new EquipmentInfo() : info;
    }

    public ExEquipmentInfo setInfo(EquipmentInfo info)
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

    public Schema<ExEquipmentInfo> cachedSchema()
    {
        return DEFAULT_INSTANCE;
    }

    // schema methods

    public ExEquipmentInfo newMessage()
    {
        return new ExEquipmentInfo();
    }

    public Class<ExEquipmentInfo> typeClass()
    {
        return ExEquipmentInfo.class;
    }

    public String messageName()
    {
        return ExEquipmentInfo.class.getSimpleName();
    }

    public String messageFullName()
    {
        return ExEquipmentInfo.class.getName();
    }

    public boolean isInitialized(ExEquipmentInfo message)
    {
        return true;
    }

    public void mergeFrom(Input input, ExEquipmentInfo message) throws IOException
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
                    message.info = input.mergeObject(message.info, EquipmentInfo.getSchema());
                    break;

                default:
                    input.handleUnknownField(number, this);
            }   
        }
    }


    public void writeTo(Output output, ExEquipmentInfo message) throws IOException
    {
        if(message.ctrl != null)
             output.writeObject(10, message.ctrl, DataCtrl.getSchema(), false);


        if(message.info != null)
             output.writeObject(20, message.info, EquipmentInfo.getSchema(), false);

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
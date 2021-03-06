// Generated by http://code.google.com/p/protostuff/ ... DO NOT EDIT!
// Generated from common.proto

package com.vikings.sanguo.protos;

import java.io.Externalizable;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import java.util.ArrayList;
import java.util.List;

import com.dyuproject.protostuff.GraphIOUtil;
import com.dyuproject.protostuff.Input;
import com.dyuproject.protostuff.Message;
import com.dyuproject.protostuff.Output;
import com.dyuproject.protostuff.Schema;

public final class ArmPropInfo implements Externalizable, Message<ArmPropInfo>, Schema<ArmPropInfo>
{

    public static Schema<ArmPropInfo> getSchema()
    {
        return DEFAULT_INSTANCE;
    }

    public static ArmPropInfo getDefaultInstance()
    {
        return DEFAULT_INSTANCE;
    }

    static final ArmPropInfo DEFAULT_INSTANCE = new ArmPropInfo();

    
    private Integer armid;
    private List<ArmEffectInfo> infos;

    public ArmPropInfo()
    {
        
    }

    // getters and setters

    // armid

    public boolean hasArmid(){
        return armid != null;
    }


    public Integer getArmid()
    {
        return armid == null ? 0 : armid;
    }

    public ArmPropInfo setArmid(Integer armid)
    {
        this.armid = armid;
        return this;
    }

    // infos

    public boolean hasInfos(){
        return infos != null;
    }


    public List<ArmEffectInfo> getInfosList()
    {
        return infos == null?  new ArrayList<ArmEffectInfo>():infos;
    }

    public int getInfosCount()
    {
        return infos == null?0:infos.size();
    }

    public ArmEffectInfo getInfos(int i)
    {
        return infos == null?null:infos.get(i);
    }


    public ArmPropInfo setInfosList(List<ArmEffectInfo> infos)
    {
        this.infos = infos;
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

    public Schema<ArmPropInfo> cachedSchema()
    {
        return DEFAULT_INSTANCE;
    }

    // schema methods

    public ArmPropInfo newMessage()
    {
        return new ArmPropInfo();
    }

    public Class<ArmPropInfo> typeClass()
    {
        return ArmPropInfo.class;
    }

    public String messageName()
    {
        return ArmPropInfo.class.getSimpleName();
    }

    public String messageFullName()
    {
        return ArmPropInfo.class.getName();
    }

    public boolean isInitialized(ArmPropInfo message)
    {
        return true;
    }

    public void mergeFrom(Input input, ArmPropInfo message) throws IOException
    {
        for(int number = input.readFieldNumber(this);; number = input.readFieldNumber(this))
        {
            switch(number)
            {
                case 0:
                    return;
                case 10:
                    message.armid = input.readUInt32();
                    break;
                case 20:
                    if(message.infos == null)
                        message.infos = new ArrayList<ArmEffectInfo>();
                    message.infos.add(input.mergeObject(null, ArmEffectInfo.getSchema()));
                    break;

                default:
                    input.handleUnknownField(number, this);
            }   
        }
    }


    public void writeTo(Output output, ArmPropInfo message) throws IOException
    {
        if(message.armid != null)
            output.writeUInt32(10, message.armid, false);

        if(message.infos != null)
        {
            for(ArmEffectInfo infos : message.infos)
            {
                if(infos != null)
                    output.writeObject(20, infos, ArmEffectInfo.getSchema(), true);
            }
        }

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

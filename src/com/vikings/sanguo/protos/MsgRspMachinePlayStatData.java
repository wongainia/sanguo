// Generated by http://code.google.com/p/protostuff/ ... DO NOT EDIT!
// Generated from client.proto

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

public final class MsgRspMachinePlayStatData implements Externalizable, Message<MsgRspMachinePlayStatData>, Schema<MsgRspMachinePlayStatData>
{

    public static Schema<MsgRspMachinePlayStatData> getSchema()
    {
        return DEFAULT_INSTANCE;
    }

    public static MsgRspMachinePlayStatData getDefaultInstance()
    {
        return DEFAULT_INSTANCE;
    }

    static final MsgRspMachinePlayStatData DEFAULT_INSTANCE = new MsgRspMachinePlayStatData();

    
    private List<MachinePlayStatInfo> machinePlayStatInfos;

    public MsgRspMachinePlayStatData()
    {
        
    }

    // getters and setters

    // machinePlayStatInfos

    public boolean hasMachinePlayStatInfos(){
        return machinePlayStatInfos != null;
    }


    public List<MachinePlayStatInfo> getMachinePlayStatInfosList()
    {
        return machinePlayStatInfos == null?  new ArrayList<MachinePlayStatInfo>():machinePlayStatInfos;
    }

    public int getMachinePlayStatInfosCount()
    {
        return machinePlayStatInfos == null?0:machinePlayStatInfos.size();
    }

    public MachinePlayStatInfo getMachinePlayStatInfos(int i)
    {
        return machinePlayStatInfos == null?null:machinePlayStatInfos.get(i);
    }


    public MsgRspMachinePlayStatData setMachinePlayStatInfosList(List<MachinePlayStatInfo> machinePlayStatInfos)
    {
        this.machinePlayStatInfos = machinePlayStatInfos;
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

    public Schema<MsgRspMachinePlayStatData> cachedSchema()
    {
        return DEFAULT_INSTANCE;
    }

    // schema methods

    public MsgRspMachinePlayStatData newMessage()
    {
        return new MsgRspMachinePlayStatData();
    }

    public Class<MsgRspMachinePlayStatData> typeClass()
    {
        return MsgRspMachinePlayStatData.class;
    }

    public String messageName()
    {
        return MsgRspMachinePlayStatData.class.getSimpleName();
    }

    public String messageFullName()
    {
        return MsgRspMachinePlayStatData.class.getName();
    }

    public boolean isInitialized(MsgRspMachinePlayStatData message)
    {
        return true;
    }

    public void mergeFrom(Input input, MsgRspMachinePlayStatData message) throws IOException
    {
        for(int number = input.readFieldNumber(this);; number = input.readFieldNumber(this))
        {
            switch(number)
            {
                case 0:
                    return;
                case 10:
                    if(message.machinePlayStatInfos == null)
                        message.machinePlayStatInfos = new ArrayList<MachinePlayStatInfo>();
                    message.machinePlayStatInfos.add(input.mergeObject(null, MachinePlayStatInfo.getSchema()));
                    break;

                default:
                    input.handleUnknownField(number, this);
            }   
        }
    }


    public void writeTo(Output output, MsgRspMachinePlayStatData message) throws IOException
    {
        if(message.machinePlayStatInfos != null)
        {
            for(MachinePlayStatInfo machinePlayStatInfos : message.machinePlayStatInfos)
            {
                if(machinePlayStatInfos != null)
                    output.writeObject(10, machinePlayStatInfos, MachinePlayStatInfo.getSchema(), true);
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

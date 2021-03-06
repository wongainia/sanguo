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

public final class MsgRspHeroSkillStudy implements Externalizable, Message<MsgRspHeroSkillStudy>, Schema<MsgRspHeroSkillStudy>
{

    public static Schema<MsgRspHeroSkillStudy> getSchema()
    {
        return DEFAULT_INSTANCE;
    }

    public static MsgRspHeroSkillStudy getDefaultInstance()
    {
        return DEFAULT_INSTANCE;
    }

    static final MsgRspHeroSkillStudy DEFAULT_INSTANCE = new MsgRspHeroSkillStudy();

    
    private Integer studyResult;
    private ReturnInfo ri;

    public MsgRspHeroSkillStudy()
    {
        
    }

    // getters and setters

    // studyResult

    public boolean hasStudyResult(){
        return studyResult != null;
    }


    public Integer getStudyResult()
    {
        return studyResult == null ? 0 : studyResult;
    }

    public MsgRspHeroSkillStudy setStudyResult(Integer studyResult)
    {
        this.studyResult = studyResult;
        return this;
    }

    // ri

    public boolean hasRi(){
        return ri != null;
    }


    public ReturnInfo getRi()
    {
        return ri == null ? new ReturnInfo() : ri;
    }

    public MsgRspHeroSkillStudy setRi(ReturnInfo ri)
    {
        this.ri = ri;
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

    public Schema<MsgRspHeroSkillStudy> cachedSchema()
    {
        return DEFAULT_INSTANCE;
    }

    // schema methods

    public MsgRspHeroSkillStudy newMessage()
    {
        return new MsgRspHeroSkillStudy();
    }

    public Class<MsgRspHeroSkillStudy> typeClass()
    {
        return MsgRspHeroSkillStudy.class;
    }

    public String messageName()
    {
        return MsgRspHeroSkillStudy.class.getSimpleName();
    }

    public String messageFullName()
    {
        return MsgRspHeroSkillStudy.class.getName();
    }

    public boolean isInitialized(MsgRspHeroSkillStudy message)
    {
        return true;
    }

    public void mergeFrom(Input input, MsgRspHeroSkillStudy message) throws IOException
    {
        for(int number = input.readFieldNumber(this);; number = input.readFieldNumber(this))
        {
            switch(number)
            {
                case 0:
                    return;
                case 10:
                    message.studyResult = input.readUInt32();
                    break;
                case 20:
                    message.ri = input.mergeObject(message.ri, ReturnInfo.getSchema());
                    break;

                default:
                    input.handleUnknownField(number, this);
            }   
        }
    }


    public void writeTo(Output output, MsgRspHeroSkillStudy message) throws IOException
    {
        if(message.studyResult != null)
            output.writeUInt32(10, message.studyResult, false);

        if(message.ri != null)
             output.writeObject(20, message.ri, ReturnInfo.getSchema(), false);

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

// Generated by http://code.google.com/p/protostuff/ ... DO NOT EDIT!
// Generated from common.proto

package com.vikings.sanguo.protos;

public enum GuildSearchFieldType implements com.dyuproject.protostuff.EnumLite<GuildSearchFieldType>
{
    GUILD_SEARCH_GUILD(1);
    
    public final int number;
    
    private GuildSearchFieldType (int number)
    {
        this.number = number;
    }
    
    public int getNumber()
    {
        return number;
    }
    
    public static GuildSearchFieldType valueOf(int number)
    {
        switch(number) 
        {
            case 1: return GUILD_SEARCH_GUILD;
            default: return null;
        }
    }
}
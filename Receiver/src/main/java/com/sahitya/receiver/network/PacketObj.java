package com.sahitya.receiver.network;

public class PacketObj<T> {
    Packet<T> packet;

    public  Packet getPacket() { return  packet; }
    public  void setPacket(Packet packet) { this.packet = packet; }
}

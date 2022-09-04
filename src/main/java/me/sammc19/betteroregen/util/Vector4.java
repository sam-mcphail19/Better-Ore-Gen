package me.sammc19.betteroregen.util;

public class Vector4 {
    public double x;
    public double y;
    public double z;
    public double w;

    public Vector4(double x, double y, double z, double w) {
        this.x = x;
        this.y = y;
        this.z = z;
        this.w = w;
    }

    public Vector4 subtract(Vector4 otherVec) {
        return new Vector4(this.x - otherVec.x, this.y - otherVec.y, this.z - otherVec.z, this.w - otherVec.w);
    }
}

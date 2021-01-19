package com.ecarto.cartoapp.utils;

import java.io.InputStream;
import java.io.OutputStream;
import java.sql.Blob;
import java.sql.SQLException;

public class MyBlob implements Blob {
    public byte[] mBytes;

    public MyBlob(byte[] bytes){
        this.mBytes = bytes;
    }

    @Override
    public long length() throws SQLException {
        return mBytes.length;
    }

    @Override
    public byte[] getBytes(long pos, int length) throws SQLException {
        return mBytes;
    }

    @Override
    public InputStream getBinaryStream() throws SQLException {
        return null;
    }

    @Override
    public long position(byte[] pattern, long start) throws SQLException {
        return 0;
    }

    @Override
    public long position(Blob pattern, long start) throws SQLException {
        return 0;
    }

    @Override
    public int setBytes(long pos, byte[] bytes) throws SQLException {
        return 0;
    }

    @Override
    public int setBytes(long pos, byte[] bytes, int offset, int len) throws SQLException {
        return 0;
    }

    @Override
    public OutputStream setBinaryStream(long pos) throws SQLException {
        return null;
    }

    @Override
    public void truncate(long len) throws SQLException {

    }

    @Override
    public void free() throws SQLException {

    }

    @Override
    public InputStream getBinaryStream(long pos, long length) throws SQLException {
        return null;
    }
}

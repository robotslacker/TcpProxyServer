package com.robotslacker.tcpproxy.tcpserver;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.ClosedChannelException;
import java.nio.channels.SocketChannel;

public class TcpServerSocketBuffer {

    private final static int BUFFER_SIZE = 1000;
    private enum BufferState {READY_TO_WRITE, READY_TO_READ}
    private ByteBuffer buffer;

    public TcpServerSocketBuffer()
    {
        this.buffer = ByteBuffer.allocateDirect(BUFFER_SIZE);
    }

    public TcpServerSocketBuffer(boolean directBuffer)
    {
        if (!directBuffer)
        {
            this.buffer = ByteBuffer.allocate(BUFFER_SIZE);
        }
    }
    private BufferState state = BufferState.READY_TO_WRITE;

    public boolean isReadyToRead() {
        return state == BufferState.READY_TO_READ;
    }
    public boolean isReadyToWrite() {
        return state == BufferState.READY_TO_WRITE;
    }

    public void writeFrom(SocketChannel channel) throws IOException {
        int read = channel.read(buffer);
        if (read == -1) throw new ClosedChannelException();
        if (read > 0) {
            buffer.flip();
            state = BufferState.READY_TO_READ;
        }
    }

    /**
     * This method try to write data from buffer to channel.
     * Buffer changes state to READY_TO_READ only if all data were written to channel,
     * in other case you should call this method again
     *
     * @param channel - channel
     */
    public void writeTo(SocketChannel channel) throws IOException {
        channel.write(buffer);

        // only if buffer is empty
        if (buffer.remaining() == 0) {
            buffer.clear();
            state = BufferState.READY_TO_WRITE;
        }
    }
}

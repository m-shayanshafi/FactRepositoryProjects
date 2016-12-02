/**
 *   Stigma - Multiplayer online RPG - http://stigma.sourceforge.net
 *   Copyright (C) 2005-2009 Minions Studio
 *
 *   This program is free software: you can redistribute it and/or modify
 *   it under the terms of the GNU General Public License as published by
 *   the Free Software Foundation, either version 3 of the License, or
 *   (at your option) any later version.
 *
 *   This program is distributed in the hope that it will be useful,
 *   but WITHOUT ANY WARRANTY; without even the implied warranty of
 *   MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *   GNU General Public License for more details.
 *
 *   You should have received a copy of the GNU General Public License
 *   along with this program.  If not, see <http://www.gnu.org/licenses/>.
 *   
 */
package pl.org.minions.stigma.network;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.ClosedChannelException;
import java.nio.channels.SelectableChannel;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.util.Arrays;
import java.util.Iterator;
import java.util.TimerTask;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.atomic.AtomicBoolean;

import pl.org.minions.stigma.globals.GlobalConfig;
import pl.org.minions.stigma.globals.GlobalTimer;
import pl.org.minions.stigma.network.connection.Connection;
import pl.org.minions.stigma.network.connection.GenericConnection;
import pl.org.minions.stigma.network.messaging.NetworkMessage;
import pl.org.minions.stigma.network.messaging.NetworkMessageType;
import pl.org.minions.stigma.network.messaging.system.PingMessage;
import pl.org.minions.utils.logger.Log;

/**
 * Static-singleton class responsible for network connection
 * with multiple users. Uses {@link SocketChannel
 * SocketChannels} and two threads: one for receiving and
 * one for sending data to optimize connection processes. It
 * is NOT responsible for sustaining connection - they may
 * time out. Closed connections are automatically removed.
 * Every connection is represented by different
 * {@link GenericConnection} object. This class may be
 * initialized only by children classes which will implement
 * application specific {@code init} functions.
 * @see GenericConnection
 */
public abstract class GlobalConnector
{
    private class ReceiveClient
    {
        private ByteBuffer buffer;
        private boolean inReceiving;
        private Connection connection;
        private int receivedCount;
        private TimerTask timeoutTask;
        private TimerTask pingTask;

        public ReceiveClient(Connection connection2)
        {
            this.connection = connection2;
            this.receivedCount = 0;

            safeBuildTimeoutTask();
        }

        private void buildTimeoutTask()
        {
            timeoutTask = new TimerTask()
            {
                @Override
                public void run()
                {
                    checkTimeout();
                }
            };

            int timeout = GlobalConfig.globalInstance().getNetworkTimeout();
            GlobalTimer.getTimer().schedule(timeoutTask, timeout, timeout);

            if (autoPing)
            {
                pingTask = new TimerTask()
                {
                    @Override
                    public void run()
                    {
                        connection.networkMessage(new PingMessage(System.currentTimeMillis()));
                    }
                };

                timeout /= 2;
                GlobalTimer.getTimer().schedule(pingTask, 0, timeout);
            }
        }

        private void checkTimeout()
        {
            if (receivedCount <= 0)
            {
                // if not connected - just discard this timer task, no logging needed
                if (connection.isConnected() && Log.isDebugEnabled())
                    Log.logger.debug("Connection " + connection.toString()
                        + " timed out");
                closeConnection();
            }
            else
                receivedCount = 0;
        }

        public void closeConnection()
        {
            if (timeoutTask != null)
                timeoutTask.cancel();
            if (pingTask != null)
                pingTask.cancel();
            connection.closeConnection();
        }

        public void receive(NetworkMessage message)
        {
            ++receivedCount;
            if (spammerCount != 0 && receivedCount > spammerCount)
            {
                Log.logger.info("Spammer detected: " + connection);
                closeConnection();
            }

            connection.receive(message);
        }

        private void safeBuildTimeoutTask()
        {
            // this for sure will work on server
            if (GlobalConfig.globalInstance() != null)
            {
                buildTimeoutTask();
            }
            else
            {
                // and this will add timeout check on client when it is ready
                timeoutTask = new TimerTask()
                {
                    @Override
                    public void run()
                    {
                        safeBuildTimeoutTask();
                    }
                };

                final int timeout = 10;
                GlobalTimer.getTimer().schedule(timeoutTask, timeout);
            }
        }
    }

    private class ReceivingThread implements Runnable
    {
        private static final int RECEIVE_SLEEP_MILIS = 10;

        public void run()
        {
            while (!Thread.interrupted())
            {
                try
                {
                    if (canSelect.get())
                        selector.select();
                    else
                        Thread.sleep(RECEIVE_SLEEP_MILIS);
                }
                catch (IOException e)
                {
                    Log.logger.warn("Selector.select IOException: " + e);
                    if (Thread.interrupted())
                        break;
                }
                catch (InterruptedException e)
                {
                    break;
                }

                if (Thread.interrupted())
                    break;

                Iterator<SelectionKey> it = selector.selectedKeys().iterator();
                while (it.hasNext())
                {
                    SelectionKey key = it.next();
                    it.remove();
                    ReceiveClient client = (ReceiveClient) key.attachment();
                    if (!key.isValid())
                    {
                        client.closeConnection();
                        continue;
                    }

                    if (key.isAcceptable())
                    {
                        if (!performAccept(key))
                        {
                            Log.logger.warn("Accept failed");
                            continue;
                        }
                    }

                    if (key.isReadable())
                    {
                        readInput(key, client);
                    }
                }
            }
            Log.logger.debug("ReceivingThread stopped");
        }
    }

    private class SendingThread implements Runnable
    {
        /** {@inheritDoc} */
        @Override
        public void run()
        {
            while (!Thread.interrupted())
            {
                SendTask task;
                try
                {
                    task = sendQueue.take();
                }
                catch (InterruptedException e)
                {
                    break;
                }
                task.run();
            }
            Log.logger.debug("Sending thread interrupted");
        }
    }

    private class SendTask implements Runnable
    {
        private NetworkMessage message;
        private Connection sender;

        public SendTask(Connection connection, NetworkMessage message)
        {
            this.sender = connection;
            this.message = message;
        }

        @Override
        public void run()
        {
            if (!sender.isConnected())
                return;

            Buffer buf;
            try
            {
                buf = codec.encode(message);
            }
            // CHECKSTYLE:OFF
            catch (Throwable t)
            // CHECKSTYLE:ON
            {
                Log.logger.warn("Send task caught", t);
                throw new RuntimeException(t);
            }

            try
            {
                sender.send(buf.getByteBuffer());
            }
            catch (IOException e)
            {
                Log.logger.info("Send IOException: " + e);
                sender.closeConnection();
            }
        }

    }

    private static GlobalConnector instance;

    private Selector selector;
    private AtomicBoolean canSelect = new AtomicBoolean(true);
    private Codec codec = new Codec();
    private Thread receiveThread;
    private Thread sendThread;
    private boolean autoPing;
    private BlockingQueue<SendTask> sendQueue;

    private final int spammerCount;
    private final short maxPacketSize;

    /**
     * Constructor for descendant classes.
     * @param autoPing
     *            when {@code true} pings will be sent to
     *            all connections
     */
    protected GlobalConnector(boolean autoPing)
    {
        this.autoPing = autoPing;

        if (GlobalConfig.globalInstance() == null
            || GlobalConfig.globalInstance().getSpammerInterval() == 0)
            spammerCount = 0;
        else
            spammerCount =
                    GlobalConfig.globalInstance().getNetworkTimeout()
                        / GlobalConfig.globalInstance().getSpammerInterval();

        if (GlobalConfig.globalInstance() != null)
            maxPacketSize = GlobalConfig.globalInstance().getMaxPacketSize();
        else
            maxPacketSize = Short.MAX_VALUE;
    }

    /**
     * Stops all threads and clears resources.
     */
    public static void destroyGlobalInstance()
    {
        if (instance != null)
        {
            instance.stop();
            instance = null;
        }
    }

    /**
     * Singleton instance access method.
     * @return global instance
     */
    public static GlobalConnector globalInstance()
    {
        assert instance != null;
        return instance;
    }

    /**
     * Changes global instance.
     * @param instance
     *            new global instance
     */
    protected static void setGlobalInstance(GlobalConnector instance)
    {
        GlobalConnector.instance = instance;
    }

    /**
     * Should be used only by {@link Connection} class. Adds
     * Connection with assigned {@link SocketChannel} to
     * list of connections operated by this GlobalConnector.
     * @param connection
     *            object representing new connection -
     *            generally "this" when called from
     *            GenericConnection on it's creation.
     * @param chan
     *            socket channel assigned to connection
     * @return key representing GenericConnection in
     *         GlobalConnector list
     */
    public SelectionKey addReceiver(Connection connection,
                                    SelectableChannel chan)
    {
        SelectionKey selKey = null;
        try
        {
            canSelect.set(false);
            selector.wakeup();
            selKey = chan.register(selector, SelectionKey.OP_READ);
            canSelect.set(true);
        }
        catch (ClosedChannelException e)
        {
            Log.logger.error("SelectableChannel register failed: " + e);
            return null;
        }

        selKey.attach(new ReceiveClient(connection));
        return selKey;
    }

    /**
     * Basic initialization. Need to be called before
     * receiving any data.
     */
    protected void endInit()
    {
        sendQueue = new LinkedBlockingQueue<SendTask>();

        receiveThread = new Thread(new ReceivingThread(), "receivingThread");
        receiveThread.setDaemon(true);
        receiveThread.start();

        sendThread = new Thread(new SendingThread(), "sendingThread");
        sendThread.setDaemon(true);
        sendThread.start();
    }

    /**
     * Returns selector.
     * @return selector
     */
    protected Selector getSelector()
    {
        return selector;
    }

    /**
     * Called after incoming connection received.
     * @param key
     *            key which received new connection
     * @return {@code true} when connection accepted
     *         properly
     */
    protected abstract boolean performAccept(SelectionKey key);

    /**
     * Called after receiving {@link PingMessage}.
     * @param msg
     *            received message
     * @param connection
     *            connection which received this client
     * @return when {@code true} message will be forwarded
     *         to client
     */
    protected abstract boolean processPing(NetworkMessage msg,
                                           Connection connection);

    private void readInput(SelectionKey key, ReceiveClient client)
    {
        if (client == null)
            return;
        if (client.inReceiving)
        {
            try
            {
                int i = ((SocketChannel) key.channel()).read(client.buffer);
                if (i == -1)
                {
                    Log.logger.info("GenericConnection closed (1): "
                        + key.channel());
                    client.closeConnection();
                    return;
                }
            }
            catch (IOException e)
            {
                Log.logger.info("read IOException: " + e);
                client.closeConnection();
                return;
            }
            if (client.buffer.remaining() == 0) // received all
            {
                client.buffer.flip();
                if (Log.isTraceEnabled())
                {
                    int size = client.buffer.limit();
                    Log.logger.trace("Received msg length: " + size);
                    byte[] tmp = new byte[size];
                    client.buffer.get(tmp);
                    Log.logger.trace("Message in: " + Arrays.toString(tmp));
                    client.buffer.flip();
                }
                client.inReceiving = false;
                NetworkMessage message =
                        codec.decode(new Buffer(client.buffer));

                if (message == null)
                {
                    Log.logger.error("Decoding failed");
                    client.closeConnection();
                    return;
                }

                if (message.getType() != NetworkMessageType.PING
                    || processPing(message, client.connection))
                    client.receive(message);
                else
                    ++client.receivedCount;
            }
        }
        else
        {
            ByteBuffer initBuf = ByteBuffer.allocateDirect(Buffer.HEADER_SIZE);
            try
            {
                int i = ((SocketChannel) key.channel()).read(initBuf);
                if (i == -1)
                {
                    Log.logger.info("GenericConnection closed (2): "
                        + key.channel());
                    client.closeConnection();
                    return;
                }
            }
            catch (IOException e)
            {
                Log.logger.info("Channel: " + key.channel()
                    + " - IOException on read: " + e);
                client.closeConnection();
                return;
            }

            if (initBuf.remaining() == 0)
            {
                initBuf.flip();
                short len = initBuf.getShort();

                if (len >= maxPacketSize)
                {
                    Log.logger.info("Spammer detected: maximum packet size exceeded: "
                        + len + " channel: " + key.channel());
                    client.closeConnection();
                    return;
                }

                if (Log.isTraceEnabled())
                    Log.logger.trace("Read msg length: " + len);

                client.inReceiving = true;
                client.buffer = ByteBuffer.allocateDirect(len);
            }
            else
            {
                client.closeConnection();
                // didn't read even beginning of frame
            }
        }
    }

    /**
     * Puts message {@code message} in sending queue - to be
     * send by sending thread using connection represented
     * by {@code sender}.
     * @param connection
     *            GenericConnection representing connection
     *            to be used for sending; this connection
     *            will be closed if sending fails.
     * @param message
     *            message to send.
     */
    public void send(Connection connection, NetworkMessage message)
    {
        try
        {
            sendQueue.put(new SendTask(connection, message));
        }
        catch (InterruptedException e)
        {
            Log.logger.info("Put interrupted");
        }
    }

    /**
     * Need to be called on begin of all other
     * initialization functions.
     */
    protected void startInit()
    {
        if (selector != null)
            return;

        try
        {
            selector = Selector.open();
        }
        catch (IOException e)
        {
            Log.logger.fatal("Selector.open() failed: " + e);
            System.exit(1);
        }
    }

    /**
     * Clears GlobalConnector. Closes any opened
     * connections. Stops threads.
     */
    public void stop()
    {
        if (receiveThread != null)
            receiveThread.interrupt();
        if (sendThread != null)
            sendThread.interrupt();

        try
        {
            if (selector != null)
            {
                for (SelectionKey key : selector.keys())
                {
                    try
                    {
                        if (key.channel() != null)
                            key.channel().close();
                    }
                    catch (IOException e)
                    {
                        Log.logger.debug("Some error disconnecting: " + e);
                    }
                }
                selector.close();
            }
        }
        catch (IOException e)
        {
            Log.logger.error("Selector::close() failed: " + e);
        }
        selector = null;
    }
}

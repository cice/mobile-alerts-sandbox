package digital.theisen;

import digital.theisen.messages.*;
import org.apache.commons.codec.binary.Hex;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.util.concurrent.Callable;
import java.util.concurrent.TimeoutException;

public class ConfigureGatewayProcedure implements Callable<GatewayInformation> {
    private static final int PORT = 8003;
    private static final String BROADCAST_ADDRESS = "255.255.255.255";
    private static final int MAX_MESSAGE_LENGTH = 256;
    private static final long TIMEOUT = 10000000L;

    private final Inet4Address ipToUse;
    private final short portToUse;
    private final String name;

    public ConfigureGatewayProcedure(Inet4Address ipToUse, short portToUse, String name) {
        this.ipToUse = ipToUse;
        this.portToUse = portToUse;
        this.name = name;
    }

    private IMessage receiveMessage(DatagramSocket socket) throws IOException {
        byte[] buf = new byte[MAX_MESSAGE_LENGTH];
        DatagramPacket packet = new DatagramPacket(buf, buf.length);
        socket.receive(packet);
        System.out.println("Received: " + Hex.encodeHexString(buf));
        return ReceivedMessage.parse(packet.getData(), packet.getLength());
    }

    private <T extends IMessage> T waitForMessage(DatagramSocket socket, CommandType commandType) throws IOException, TimeoutException {
        long start = System.currentTimeMillis();
        long now = start;

        while (!Thread.currentThread().isInterrupted() && now < start + TIMEOUT) {
            IMessage message = receiveMessage(socket);
            if (message != null && commandType == message.getCommandType()) {
                return (T) message;
            }
            now = System.currentTimeMillis();
        }

        throw new TimeoutException();
    }

    private void sendMessage(DatagramSocket socket, IMessage message) throws IOException {
        byte[] bytes = message.getBytes();
        System.out.println(Hex.encodeHexString(bytes));
        InetAddress address = InetAddress.getByName(BROADCAST_ADDRESS);
        DatagramPacket packet = new DatagramPacket(bytes, bytes.length, address, PORT);
        System.out.println("Sending: " + Hex.encodeHexString(bytes));
        socket.send(packet);
    }

    @Override
    public GatewayInformation call() throws Exception {
        DatagramSocket socket = new DatagramSocket(PORT);

        try {
            // Trigger discovery & get config
            sendMessage(socket, new FindGatewaysMessage());
            GetConfigMessage getConfig = waitForMessage(socket, CommandType.GET_CONFIG);
            SetConfigMessage setConfig = reconfigure(getConfig);

            if (isConfigured(getConfig, setConfig)) {
                return new GatewayInformation(getConfig.DHCPIp, getConfig.getGatewayId());
            }
            sendMessage(socket, setConfig);

            // Validate
            sendMessage(socket, new FindGatewaysMessage(getConfig.getGatewayId()));
            getConfig = waitForMessage(socket, CommandType.GET_CONFIG);

            if (isConfigured(getConfig, setConfig)) {
                return new GatewayInformation(getConfig.DHCPIp, getConfig.getGatewayId());
            }

            throw new RuntimeException("Configuration failed");
        } finally {
            socket.close();
        }
    }

    private boolean isConfigured(GetConfigMessage getConfig, SetConfigMessage setConfig) {
        return getConfig.DeviceName.equals(setConfig.DeviceName) && getConfig.UseProxy == setConfig.UseProxy && getConfig.ProxyServerName.equals(setConfig.ProxyServerName) && getConfig.ProxyPort == setConfig.ProxyPort;
    }

    private SetConfigMessage reconfigure(GetConfigMessage getConfig) {
        SetConfigMessage config = getConfig.setConfigMessage();

        config.UseProxy = true;
        config.DeviceName = this.name != null ? this.name : "MobileAlerts";
        config.ProxyServerName = ipToUse.getHostAddress();
        config.ProxyPort = portToUse;

        return config;
    }
}

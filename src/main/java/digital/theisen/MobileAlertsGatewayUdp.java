package digital.theisen;

import digital.theisen.messages.IMessage;
import digital.theisen.messages.IMessageHandler;
import digital.theisen.messages.ReceivedMessage;
import org.apache.commons.codec.binary.Hex;
import org.eclipse.jdt.annotation.NonNullByDefault;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.text.SimpleDateFormat;
import java.util.Date;

@NonNullByDefault
public class MobileAlertsGatewayUdp {
    private static int PORT = 8003;
    private static String BROADCAST_ADDRESS = "255.255.255.255";
    private static int MAX_MESSAGE_LENGTH = 256;

    private DatagramSocket socket;
    private IMessageHandler Handler;

    public MobileAlertsGatewayUdp() throws SocketException {
        socket = new DatagramSocket(PORT);
    }

    public void setHandler(IMessageHandler handler) {
        this.Handler = handler;
    }

    public Runnable listen() {
        return new Runnable() {
            public void run() {
                byte[] buf = new byte[MAX_MESSAGE_LENGTH];
                boolean stopped = false;

                while (!Thread.currentThread().isInterrupted() && !stopped) {
                    DatagramPacket packet = new DatagramPacket(buf, buf.length);
                    try {
                        socket.receive(packet);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    stopped = handleMessage(packet.getData(), packet.getLength(), packet.getAddress(), packet.getPort());
                }
            }
        };
    }

    private boolean handleMessage(byte[] buf, int length, InetAddress address, int port) {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd 'at' HH:mm:ss z");
        Date date = new Date(System.currentTimeMillis());
        System.out.println(formatter.format(date));
        System.out.println("Got message from " + address);
        System.out.println(Hex.encodeHexString(buf));

        IMessage message = ReceivedMessage.parse(buf, length);
        if (this.Handler != null) {
            return this.Handler.handler(message);
        }

        return false;
    }

    public void sendMessage(IMessage message) throws IOException {
        byte[] bytes = message.getBytes();
        System.out.println(Hex.encodeHexString(bytes));
        InetAddress address = InetAddress.getByName(BROADCAST_ADDRESS);
        DatagramPacket packet = new DatagramPacket(bytes, bytes.length, address, PORT);
        socket.send(packet);
    }
}

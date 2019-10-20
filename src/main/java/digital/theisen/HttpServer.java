package digital.theisen;

import digital.theisen.sensors.ISensorPackage;
import digital.theisen.sensors.SensorPackage;
import org.apache.commons.codec.DecoderException;
import org.apache.commons.codec.binary.Hex;
import org.eclipse.jetty.server.Request;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.handler.AbstractHandler;
import sun.misc.IOUtils;

import javax.servlet.ServletException;
import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;


public class HttpServer extends AbstractHandler {
    public static final byte SENSOR_DATA_TYPE = (byte) 0xC0;
    private Server server;

    public HttpServer() {
        this.server = new Server(8080);
        this.server.setHandler(this);
    }

    @Override
    public void handle(String target, Request request, HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) throws IOException, ServletException {
        System.out.println(target);
        ServletInputStream inputStream = request.getInputStream();
        byte[] input = IOUtils.readFully(inputStream, -1, false);
        String identifier = request.getHeader("http_identify");
        System.out.println(identifier);
        System.out.println(Hex.encodeHex(input));

        if (identifier == null) {
            httpServletResponse.setStatus(400);
            return;
        }

        String[] ids = identifier.split(":");
        if (ids.length < 3) {
            httpServletResponse.setStatus(400);
            return;
        }
        String sn = ids[0];
        String mac = ids[1];
        byte type;
        try {
            type = Hex.decodeHex(ids[2])[0];
        } catch (DecoderException e) {
            httpServletResponse.setStatus(400);
            return;
        }
        if (type == SENSOR_DATA_TYPE) {
            try {
                ISensorPackage[] sensorPackages = SensorPackage.parseAll(input);
                System.out.println("Received " + sensorPackages.length + " packages");
            } catch (Exception e) {
                httpServletResponse.setStatus(400);
                return;
            }
        }

        httpServletResponse.setHeader("content-type", "application/octet-stream");
        httpServletResponse.setStatus(200);
        OutputStream output = httpServletResponse.getOutputStream();
        output.write(getStandardResponse());
        output.close();
    }

    public byte[] getStandardResponse() {
        ByteBuffer buffer = ByteBuffer.allocate(24);
        buffer.order(ByteOrder.BIG_ENDIAN);
        buffer.putInt(0, 420);
        buffer.putInt(4, 0);
        long seconds = (System.currentTimeMillis() / 1000);
        buffer.putInt(8, Math.toIntExact(seconds));
        buffer.putInt(12, 0);
        buffer.putInt(16, 0x1761D480);
        buffer.putInt(20, 15);
        byte[] bytes = buffer.array();
        System.out.println("Sending: " + Hex.encodeHexString(bytes));
        return bytes;
    }

    public void startServer() throws Exception {
        this.server.start();
    }
}

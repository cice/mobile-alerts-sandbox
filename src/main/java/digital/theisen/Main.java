package digital.theisen;

import digital.theisen.messages.FindGatewaysMessage;
import digital.theisen.messages.GetConfigMessage;
import digital.theisen.messages.IMessage;
import digital.theisen.messages.SetConfigMessage;

import java.io.IOException;
import java.net.SocketException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ThreadPoolExecutor;

public class Main {

    public static void main(String[] args) throws IOException, InterruptedException {
        final MobileAlertsGatewayUdp udp = new MobileAlertsGatewayUdp();

        Runnable listener = udp.listen();
        ExecutorService executor = Executors.newSingleThreadExecutor();
        executor.submit(listener);
        udp.setHandler((IMessage msg) -> {
            if (msg instanceof GetConfigMessage) {
                GetConfigMessage getConfig = (GetConfigMessage) msg;
                System.out.println("UseProxy: " + getConfig.UseProxy);

                SetConfigMessage setConfig = getConfig.setConfigMessage();
                setConfig.UseProxy = false;
                setConfig.DeviceName = "Sensor-Dingens";

                try {
                    System.out.println("Sending new config");
                    udp.sendMessage(setConfig);
                    System.out.println("Setting handler");
                    udp.setHandler((IMessage nextMsg) -> {
                        if (nextMsg instanceof GetConfigMessage) {
                            return true;
                        }
                        return false;
                    });
                    System.out.println("Checking config");
                    udp.sendMessage(new FindGatewaysMessage());
                    return false;
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            return false;
        });
        udp.sendMessage(new FindGatewaysMessage());

        System.out.println("Taking a nap");
        Thread.sleep(30000);
        executor.shutdownNow();
    }
}

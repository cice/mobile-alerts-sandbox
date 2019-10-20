package digital.theisen;

import java.io.IOException;
import java.net.Inet4Address;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class Main {

    public static void main(String[] args) throws Exception {
        ExecutorService executor = Executors.newSingleThreadExecutor();

        ConfigureGatewayProcedure procedure = new ConfigureGatewayProcedure(
                (Inet4Address) Inet4Address.getByName("192.168.178.20"),
                (short) 8080,
                "SensorBums"
        );

        Future<GatewayInformation> result = executor.submit(procedure);

        GatewayInformation gatewayInformation = result.get();

        System.out.println(gatewayInformation);

        executor.shutdownNow();
        HttpServer server = new HttpServer();
        server.startServer();
    }
}

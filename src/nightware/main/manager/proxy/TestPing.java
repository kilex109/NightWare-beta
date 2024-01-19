package nightware.main.manager.proxy;

import com.google.common.util.concurrent.ThreadFactoryBuilder;
import com.mojang.realmsclient.gui.ChatFormatting;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelException;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.proxy.Socks4ProxyHandler;
import io.netty.handler.proxy.Socks5ProxyHandler;
import io.netty.handler.timeout.ReadTimeoutHandler;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.UnknownHostException;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.ThreadPoolExecutor;
import net.minecraft.network.EnumConnectionState;
import net.minecraft.network.EnumPacketDirection;
import net.minecraft.network.NettyPacketDecoder;
import net.minecraft.network.NettyPacketEncoder;
import net.minecraft.network.NettyVarint21FrameDecoder;
import net.minecraft.network.NettyVarint21FrameEncoder;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.handshake.client.C00Handshake;
import net.minecraft.network.status.INetHandlerStatusClient;
import net.minecraft.network.status.client.CPacketPing;
import net.minecraft.network.status.client.CPacketServerQuery;
import net.minecraft.network.status.server.SPacketPong;
import net.minecraft.network.status.server.SPacketServerInfo;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentString;

public class TestPing {
   public String state = "";
   private long pingSentAt;
   private NetworkManager pingDestination = null;
   private Proxy proxy;
   private static final ThreadPoolExecutor EXECUTOR = new ScheduledThreadPoolExecutor(5, (new ThreadFactoryBuilder()).setNameFormat("Server Pinger #%d").setDaemon(true).build());

   public void run(String ip, int port, Proxy proxy) {
      this.proxy = proxy;
      EXECUTOR.submit(() -> {
         this.ping(ip, port);
      });
   }

   private void ping(final String ip, int port) {
      this.state = "Соединяемся с " + ip + "...";

      final NetworkManager networkManager;
      try {
         networkManager = this.createTestClientConnection(InetAddress.getByName(ip), port);
      } catch (UnknownHostException var6) {
         this.state = ChatFormatting.RED + "Не удалось подключиться к прокси";
         return;
      } catch (Exception var7) {
         this.state = ChatFormatting.RED + "Не удалось установить соединение с прокси-сервером";
         return;
      }

      this.pingDestination = networkManager;
      networkManager.setNetHandler(new INetHandlerStatusClient() {
         private boolean successful;

         public void onDisconnect(ITextComponent reason) {
            TestPing.this.pingDestination = null;
            if (!this.successful) {
               TestPing.this.state = ChatFormatting.RED + "Не удалось установить соединение с " + ip;
            }

         }

         public void handleServerInfo(SPacketServerInfo packetIn) {
            TestPing.this.pingSentAt = System.currentTimeMillis();
            networkManager.sendPacket(new CPacketPing(TestPing.this.pingSentAt));
         }

         public void handlePong(SPacketPong packetIn) {
            this.successful = true;
            TestPing.this.pingDestination = null;
            long pingToServer = System.currentTimeMillis() - TestPing.this.pingSentAt;
            TestPing.this.state = "Пинг: " + pingToServer;
            networkManager.closeChannel(new TextComponentString("Finished"));
         }
      });

      try {
         networkManager.sendPacket(new C00Handshake(ip, port, EnumConnectionState.STATUS));
         networkManager.sendPacket(new CPacketServerQuery());
      } catch (Throwable var5) {
         this.state = ChatFormatting.RED + "Не удалось установить соединение с " + ip;
      }

   }

   private NetworkManager createTestClientConnection(InetAddress address, int port) {
      final NetworkManager clientConnection = new NetworkManager(EnumPacketDirection.CLIENTBOUND);
      ((Bootstrap)((Bootstrap)((Bootstrap)(new Bootstrap()).group((EventLoopGroup)NetworkManager.CLIENT_NIO_EVENTLOOP.getValue())).handler(new ChannelInitializer<Channel>() {
         protected void initChannel(Channel channel) {
            try {
               channel.config().setOption(ChannelOption.TCP_NODELAY, true);
            } catch (ChannelException var3) {
            }

            channel.pipeline().addLast("timeout", new ReadTimeoutHandler(30)).addLast("splitter", new NettyVarint21FrameDecoder()).addLast("decoder", new NettyPacketDecoder(EnumPacketDirection.CLIENTBOUND)).addLast("prepender", new NettyVarint21FrameEncoder()).addLast("encoder", new NettyPacketEncoder(EnumPacketDirection.SERVERBOUND)).addLast("packet_handler", clientConnection);
            if (TestPing.this.proxy.type == Proxy.ProxyType.SOCKS5) {
               channel.pipeline().addFirst(new ChannelHandler[]{new Socks5ProxyHandler(new InetSocketAddress(TestPing.this.proxy.getIp(), TestPing.this.proxy.getPort()), TestPing.this.proxy.username.isEmpty() ? null : TestPing.this.proxy.username, TestPing.this.proxy.password.isEmpty() ? null : TestPing.this.proxy.password)});
            } else {
               channel.pipeline().addFirst(new ChannelHandler[]{new Socks4ProxyHandler(new InetSocketAddress(TestPing.this.proxy.getIp(), TestPing.this.proxy.getPort()), TestPing.this.proxy.username.isEmpty() ? null : TestPing.this.proxy.username)});
            }

         }
      })).channel(NioSocketChannel.class)).connect(address, port).syncUninterruptibly();
      return clientConnection;
   }

   public void pingPendingNetworks() {
      if (this.pingDestination != null) {
         if (this.pingDestination.isChannelOpen()) {
            this.pingDestination.processReceivedPackets();
         } else {
            this.pingDestination.checkDisconnected();
         }
      }

   }
}

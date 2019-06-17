package com.cloud.mina.springboot;

import com.cloud.mina.component.filter.ComponentIOFilter;
import com.cloud.mina.component.filter.MHRootComponent;
import com.cloud.mina.component.filter.UnitASportComponent;
import com.cloud.mina.component.unit_a.sport.*;
import com.cloud.mina.component.unit_a.strategy.StrategyFactroyHandler;
import com.cloud.mina.util.Logger;
import org.apache.mina.core.filterchain.IoFilter;
import org.apache.mina.core.service.IoAcceptor;
import org.apache.mina.core.session.IdleStatus;
import org.apache.mina.core.session.IoSessionConfig;
import org.apache.mina.filter.executor.ExecutorFilter;
import org.apache.mina.filter.logging.LoggingFilter;
import org.apache.mina.transport.socket.nio.NioSocketAcceptor;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

import javax.annotation.Resource;
import java.io.IOException;
import java.net.InetSocketAddress;

@SuppressWarnings(value = "all")
@Configuration
@ConfigurationProperties(prefix ="mina")
public class MinaConfig {
    private String ip;
    private int port;
    private int readerIdleTime;
    private int minReadBufferSize;
    private int maxReadBufferSize;
    //    智能终端运动的底层解析包
    @Resource(name = "sportLoginParser")
    private SportLoginParser sportLoginParser;
    @Resource(name = "no8OneWayParser")
    private No8OneWayParser no8OneWayParser;
    @Resource(name = "no8TwoWayParser")
    private No8TwoWayParser no8TwoWayParser;
    @Resource(name = "no8ThreeWayParser")
    private No8ThreeWayParser no8ThreeWayParser;
    @Resource(name = "sportLogoutParser")
    private SportLogoutParser sportLogoutParser;

    /**
     * 设置mina 的ioHandler 自定义处理类
     *
     * @return
     */
    @Bean
    public StrategyFactroyHandler getIoHandler() {
        StrategyFactroyHandler strategyFactroyHandler = new StrategyFactroyHandler();
        strategyFactroyHandler.setRestTemplate(restTemplate());
        return strategyFactroyHandler;
    }

    /**
     * Mina 的IoAccptor 设置
     *
     * @return
     * @throws IOException
     */
    @Bean
    public IoAcceptor getIoAccptor() throws IOException {
        IoAcceptor acceptor = new NioSocketAcceptor();
        IoSessionConfig sessionConfig = acceptor.getSessionConfig();
        sessionConfig.setIdleTime(IdleStatus.READER_IDLE, readerIdleTime);
        sessionConfig.setMinReadBufferSize(minReadBufferSize);
        sessionConfig.setMaxReadBufferSize(maxReadBufferSize);
        acceptor.setDefaultLocalAddress(getinetAddress());
        acceptor.getFilterChain().addLast("codec", getIOFilter());
        acceptor.getFilterChain().addLast("logger", getLogFilter());
        acceptor.getFilterChain().addLast("executors", getExecutorFilter());
        acceptor.setHandler(getIoHandler());
        acceptor.bind();
        Logger.writeLog("监听端口" + port + "....");
        return acceptor;
    }

    /**
     * 业务树解码器的根类（注册到spring 容器）
     *
     * @return
     */
    @Bean
    public MHRootComponent getMHRootComponent() {
        MHRootComponent mHRootComponent = new MHRootComponent();
        mHRootComponent.add(getUnitASportComponent());
        return mHRootComponent;
    }

    /**
     * unitA 智能终端运动解码器组装
     *
     * @return
     */
    @Bean
    public UnitASportComponent getUnitASportComponent() {
        UnitASportComponent unitASportComponent = new UnitASportComponent();
        unitASportComponent.add(sportLoginParser);
        unitASportComponent.add(no8OneWayParser);
        unitASportComponent.add(no8TwoWayParser);
        unitASportComponent.add(no8ThreeWayParser);
        unitASportComponent.add(sportLogoutParser);
        return unitASportComponent;
    }

    /**
     * springCloud 消息交互模板
     *
     * @return
     */
    @Bean
    @LoadBalanced
    RestTemplate restTemplate() {
        return new RestTemplate();
    }

    @Bean
    public InetSocketAddress getinetAddress() {
        return new InetSocketAddress(port);
    }

    /**
     * Mina 的日志过滤器
     *
     * @return
     */
    @Bean
    public LoggingFilter getLogFilter() {
        return new LoggingFilter();
    }

    /**
     * Mina 的业务多线程处理过滤器
     *
     * @return
     */
    @Bean
    public ExecutorFilter getExecutorFilter() {
        return new ExecutorFilter();
    }

    /**
     * Mina 的ioFileter 设置
     *
     * @return
     */
    @Bean
    public IoFilter getIOFilter() {
        ComponentIOFilter componentIOFilter = new ComponentIOFilter(getMHRootComponent());
        return componentIOFilter;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public int getReaderIdleTime() {
        return readerIdleTime;
    }

    public void setReaderIdleTime(int readerIdleTime) {
        this.readerIdleTime = readerIdleTime;
    }

    public int getMinReadBufferSize() {
        return minReadBufferSize;
    }

    public void setMinReadBufferSize(int minReadBufferSize) {
        this.minReadBufferSize = minReadBufferSize;
    }

    public int getMaxReadBufferSize() {
        return maxReadBufferSize;
    }

    public void setMaxReadBufferSize(int maxReadBufferSize) {
        this.maxReadBufferSize = maxReadBufferSize;
    }
}

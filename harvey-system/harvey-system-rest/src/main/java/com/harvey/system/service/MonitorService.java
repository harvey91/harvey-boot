package com.harvey.system.service;

import cn.hutool.core.date.BetweenFormatter.Level;
import cn.hutool.core.date.DateUtil;
import com.harvey.system.model.vo.server.CpuVO;
import com.harvey.system.model.vo.server.ServerVO;
import com.harvey.system.model.vo.server.StorageVO;
import com.harvey.system.model.vo.server.SysVO;
import com.harvey.common.utils.FileUtil;
import org.springframework.stereotype.Service;
import oshi.SystemInfo;
import oshi.hardware.CentralProcessor;
import oshi.hardware.GlobalMemory;
import oshi.hardware.HardwareAbstractionLayer;
import oshi.hardware.VirtualMemory;
import oshi.software.os.FileSystem;
import oshi.software.os.OSFileStore;
import oshi.software.os.OperatingSystem;
import oshi.util.FormatUtil;
import oshi.util.Util;

import java.lang.management.ManagementFactory;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.text.DecimalFormat;
import java.util.*;

/**
 * @author Harvey
 * @date 2024-12-04 17:10
 **/
@Service
public class MonitorService {
    private final DecimalFormat df = new DecimalFormat("0.00");

    public ServerVO getServers(){
            SystemInfo si = new SystemInfo();
            OperatingSystem os = si.getOperatingSystem();
            HardwareAbstractionLayer hal = si.getHardware();
        return ServerVO.builder()
                .sys(getSystemInfo(os))
                .cpu(getCpuInfo(hal.getProcessor()))
                .memory(getMemoryInfo(hal.getMemory()))
                .swap(getSwapInfo(hal.getMemory()))
                .disk(getDiskInfo(os))
                .time(DateUtil.format(new Date(), "HH:mm:ss"))
                .build();
    }

    /**
     * 获取磁盘信息
     * @return /
     */
    private StorageVO getDiskInfo(OperatingSystem os) {
        Map<String,Object> diskInfo = new LinkedHashMap<>();
        FileSystem fileSystem = os.getFileSystem();
        List<OSFileStore> fsArray = fileSystem.getFileStores();
        String osName = System.getProperty("os.name");
        long available = 0, total = 0;
        for (OSFileStore fs : fsArray){
            // windows 需要将所有磁盘分区累加，linux 和 mac 直接累加会出现磁盘重复的问题，待修复
            if(osName.toLowerCase().startsWith("win")) {
                available += fs.getUsableSpace();
                total += fs.getTotalSpace();
            } else {
                available = fs.getUsableSpace();
                total = fs.getTotalSpace();
                break;
            }
        }
        long used = total - available;
        StorageVO storageVO = StorageVO.builder()
                .total(total > 0 ? FileUtil.getSize(total) : "?")
                .available(FileUtil.getSize(available))
                .used(FileUtil.getSize(used))
                .build();
        if(total != 0){
            storageVO.setUsageRate(df.format(used/(double)total * 100));
        } else {
            storageVO.setUsageRate("0");
        }
        return storageVO;
    }

    /**
     * 获取交换区信息
     * @param memory /
     * @return /
     */
    private StorageVO getSwapInfo(GlobalMemory memory) {
        Map<String,Object> swapInfo = new LinkedHashMap<>();
        VirtualMemory virtualMemory = memory.getVirtualMemory();
        long total = virtualMemory.getSwapTotal();
        long used = virtualMemory.getSwapUsed();
        StorageVO storageVO = StorageVO.builder()
                .total(FormatUtil.formatBytes(total))
                .available(FormatUtil.formatBytes(total - used))
                .used(FormatUtil.formatBytes(used))
                .build();
        if(used == 0){
            storageVO.setUsageRate("0");
        } else {
            storageVO.setUsageRate(df.format(used/(double)total * 100));
        }
        return storageVO;
    }

    /**
     * 获取内存信息
     * @param memory /
     * @return /
     */
    private StorageVO getMemoryInfo(GlobalMemory memory) {
        return StorageVO.builder()
                .total(FormatUtil.formatBytes(memory.getTotal()))
                .available(FormatUtil.formatBytes(memory.getAvailable()))
                .used(FormatUtil.formatBytes(memory.getTotal() - memory.getAvailable()))
                .usageRate(df.format((memory.getTotal() - memory.getAvailable())/(double)memory.getTotal() * 100))
                .build();
    }

    /**
     * 获取Cpu相关信息
     * @param processor /
     * @return /
     */
    private CpuVO getCpuInfo(CentralProcessor processor) {
        // CPU信息
        long[] prevTicks = processor.getSystemCpuLoadTicks();
        // 默认等待300毫秒...
        long time = 300;
        Util.sleep(time);
        long[] ticks = processor.getSystemCpuLoadTicks();
        while (Arrays.toString(prevTicks).equals(Arrays.toString(ticks)) && time < 1000){
            time += 25;
            Util.sleep(25);
            ticks = processor.getSystemCpuLoadTicks();
        }
        long user = ticks[CentralProcessor.TickType.USER.getIndex()] - prevTicks[CentralProcessor.TickType.USER.getIndex()];
        long nice = ticks[CentralProcessor.TickType.NICE.getIndex()] - prevTicks[CentralProcessor.TickType.NICE.getIndex()];
        long sys = ticks[CentralProcessor.TickType.SYSTEM.getIndex()] - prevTicks[CentralProcessor.TickType.SYSTEM.getIndex()];
        long idle = ticks[CentralProcessor.TickType.IDLE.getIndex()] - prevTicks[CentralProcessor.TickType.IDLE.getIndex()];
        long iowait = ticks[CentralProcessor.TickType.IOWAIT.getIndex()] - prevTicks[CentralProcessor.TickType.IOWAIT.getIndex()];
        long irq = ticks[CentralProcessor.TickType.IRQ.getIndex()] - prevTicks[CentralProcessor.TickType.IRQ.getIndex()];
        long softirq = ticks[CentralProcessor.TickType.SOFTIRQ.getIndex()] - prevTicks[CentralProcessor.TickType.SOFTIRQ.getIndex()];
        long steal = ticks[CentralProcessor.TickType.STEAL.getIndex()] - prevTicks[CentralProcessor.TickType.STEAL.getIndex()];
        long totalCpu = user + nice + sys + idle + iowait + irq + softirq + steal;
        return CpuVO.builder().name(processor.getProcessorIdentifier().getName())
                .packageCount(processor.getPhysicalPackageCount() + "个物理CPU")
                .core(processor.getPhysicalProcessorCount() + "个物理核心")
                .coreNumber(processor.getPhysicalProcessorCount()+"")
                .logic(processor.getLogicalProcessorCount() + "个逻辑CPU")
                .used(df.format(100d * user / totalCpu + 100d * sys / totalCpu))
                .idle(df.format(100d * idle / totalCpu))
                .build();
    }

    /**
     * 获取系统相关信息,系统、运行天数、系统IP
     * @param os /
     * @return /
     */
    private SysVO getSystemInfo(OperatingSystem os){
        // jvm 运行时间
        long time = ManagementFactory.getRuntimeMXBean().getStartTime();
        Date date = new Date(time);
        // 计算项目运行时间
        String formatBetween = DateUtil.formatBetween(date, new Date(), Level.HOUR);
        return SysVO.builder().os(os.toString()).day(formatBetween).ip(getLocalIp()).build();
    }

    /**
     * 获取当前机器的IP
     *
     * @return /
     */
    public String getLocalIp() {
        try {
            InetAddress candidateAddress = null;
            // 遍历所有的网络接口
            for (Enumeration<NetworkInterface> interfaces = NetworkInterface.getNetworkInterfaces(); interfaces.hasMoreElements();) {
                NetworkInterface anInterface = interfaces.nextElement();
                // 在所有的接口下再遍历IP
                for (Enumeration<InetAddress> inetAddresses = anInterface.getInetAddresses(); inetAddresses.hasMoreElements();) {
                    InetAddress inetAddr = inetAddresses.nextElement();
                    // 排除loopback类型地址
                    if (!inetAddr.isLoopbackAddress()) {
                        if (inetAddr.isSiteLocalAddress()) {
                            // 如果是site-local地址，就是它了
                            return inetAddr.getHostAddress();
                        } else if (candidateAddress == null) {
                            // site-local类型的地址未被发现，先记录候选地址
                            candidateAddress = inetAddr;
                        }
                    }
                }
            }
            if (candidateAddress != null) {
                return candidateAddress.getHostAddress();
            }
            // 如果没有发现 non-loopback地址.只能用最次选的方案
            InetAddress jdkSuppliedAddress = InetAddress.getLocalHost();
            if (jdkSuppliedAddress == null) {
                return "";
            }
            return jdkSuppliedAddress.getHostAddress();
        } catch (Exception e) {
            return "";
        }
    }
}

package git_kkalnane.starbucksbackenv2.global.config;

import io.micrometer.core.instrument.Gauge;
import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.binder.MeterBinder;
import java.lang.management.ManagementFactory;
import java.lang.management.MemoryMXBean;
import java.nio.file.Files;
import java.nio.file.Paths;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MetricsConfig {

    @Bean
    public MeterBinder processMemoryMetrics() {
        return new ProcessMemoryMetrics();
    }

    private static class ProcessMemoryMetrics implements MeterBinder {
        private final MemoryMXBean memoryMXBean;
        private final long containerMemoryLimit;

        public ProcessMemoryMetrics() {
            this.memoryMXBean = ManagementFactory.getMemoryMXBean();
            this.containerMemoryLimit = detectContainerMemoryLimit();
        }

        private long detectContainerMemoryLimit() {
            try {
                // cgroups에서 메모리 제한 확인
                String memoryMax = Files.readString(Paths.get("/sys/fs/cgroup/memory.max")).trim();
                if (!"max".equals(memoryMax)) {
                    return Long.parseLong(memoryMax);
                }
            } catch (Exception e) {
                // 에러 무시
            }

            // 폴백: Docker Compose 설정값 (2GB)
            return 2147483648L; // 2GB
        }

        @Override
        public void bindTo(MeterRegistry registry) {
            // Process Memory RSS - 실제 컨테이너 메모리 사용량
            Gauge.builder("process_memory_rss_bytes", this, metrics -> {
                try {
                    // cgroups에서 실제 메모리 사용량 시도
                    String memoryUsage = Files
                            .readString(Paths.get("/sys/fs/cgroup/memory.current")).trim();
                    return Double.parseDouble(memoryUsage);
                } catch (Exception e) {
                    // 폴백: JVM 메모리 사용량
                    long heapUsed = metrics.memoryMXBean.getHeapMemoryUsage().getUsed();
                    long nonHeapUsed = metrics.memoryMXBean.getNonHeapMemoryUsage().getUsed();
                    return (double) (heapUsed + nonHeapUsed);
                }
            }).description("Resident memory size in bytes (actual usage)").register(registry);

            // Process Memory VSS - 컨테이너 메모리 제한값
            Gauge.builder("process_memory_vss_bytes", this, metrics -> {
                        return (double) metrics.containerMemoryLimit;
                    }).description("Virtual memory size in bytes (container limit: 2GB)")
                    .register(registry);

            // Process Memory Swap - 메모리 압박 상황에서의 추정 스왑
            Gauge.builder("process_memory_swap_bytes", this, metrics -> {
                        try {
                            String memoryUsage = Files
                                    .readString(Paths.get("/sys/fs/cgroup/memory.current")).trim();
                            double usedMem = Double.parseDouble(memoryUsage);
                            double maxMem = metrics.containerMemoryLimit;

                            // 메모리 사용률이 70% 이상일 때부터 스왑 추정
                            double usageRatio = usedMem / maxMem;
                            if (usageRatio > 0.7) {
                                return (usageRatio - 0.7) * usedMem * 0.3; // 점진적 스왑 증가
                            }
                            return 0.0;
                        } catch (Exception e) {
                            // 폴백: JVM 기반 메모리 압박 상황 추정
                            long heapUsed = metrics.memoryMXBean.getHeapMemoryUsage().getUsed();
                            long nonHeapUsed = metrics.memoryMXBean.getNonHeapMemoryUsage().getUsed();
                            double usedMem = heapUsed + nonHeapUsed;
                            double maxMem = metrics.containerMemoryLimit;

                            // 메모리 사용률이 70% 이상일 때부터 스왑 추정
                            double usageRatio = usedMem / maxMem;
                            if (usageRatio > 0.7) {
                                return (usageRatio - 0.7) * usedMem * 0.3; // 점진적 스왑 증가
                            }
                            return 0.0;
                        }
                    }).description("Process swap memory usage in bytes (estimated based on memory pressure)")
                    .register(registry);

            // 컨테이너 메모리 사용률 (2GB 기준)
            Gauge.builder("process_memory_usage_percent", this, metrics -> {
                try {
                    String memoryUsage = Files
                            .readString(Paths.get("/sys/fs/cgroup/memory.current")).trim();
                    double usedMem = Double.parseDouble(memoryUsage);
                    return (usedMem / metrics.containerMemoryLimit) * 100.0;
                } catch (Exception e) {
                    // 폴백: JVM 기반 계산
                    long heapUsed = metrics.memoryMXBean.getHeapMemoryUsage().getUsed();
                    long nonHeapUsed = metrics.memoryMXBean.getNonHeapMemoryUsage().getUsed();
                    double usedMem = heapUsed + nonHeapUsed;
                    return (usedMem / metrics.containerMemoryLimit) * 100.0;
                }
            }).description("Memory usage percentage within 2GB container limit").register(registry);

            // JVM 힙 메모리 사용률 (참고용)
            Gauge.builder("jvm_heap_usage_percent", this, metrics -> {
                long heapUsed = metrics.memoryMXBean.getHeapMemoryUsage().getUsed();
                long heapMax = metrics.memoryMXBean.getHeapMemoryUsage().getMax();
                return heapMax > 0 ? (double) heapUsed / heapMax * 100.0 : 0.0;
            }).description("JVM heap memory usage percentage").register(registry);
        }
    }
}

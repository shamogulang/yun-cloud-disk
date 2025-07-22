package cn.yun.oddworld.cache;

import cn.yun.oddworld.entity.StorageBucket;
import cn.yun.oddworld.mapper.StorageBucketMapper;
import lombok.Getter;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import javax.annotation.PostConstruct;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

@Component
public class StorageBucketCache {
    private final StorageBucketMapper storageBucketMapper;
    @Getter
    private final List<StorageBucket> activeBuckets = new CopyOnWriteArrayList<>();
    @Getter
    private final Map<Long,StorageBucket> storageBucketMap = new ConcurrentHashMap<>();

    public StorageBucketCache(StorageBucketMapper storageBucketMapper) {
        this.storageBucketMapper = storageBucketMapper;
    }

    @PostConstruct
    public void init() {
        activeBuckets.clear();
        List<StorageBucket> storageBuckets = storageBucketMapper.selectActiveBuckets();
        if (CollectionUtils.isEmpty(storageBuckets)){
            throw new RuntimeException("s3 storage can not be empty!");
        }
        for (StorageBucket storageBucket : storageBuckets) {
            storageBucketMap.put(storageBucket.getId(), storageBucket);
        }
        activeBuckets.addAll(storageBuckets);
    }

    public StorageBucket getDefaultBucket() {
        return activeBuckets.stream()
                .filter(StorageBucket::getIsDefault)
                .findFirst()
                .orElse(null);
    }
} 
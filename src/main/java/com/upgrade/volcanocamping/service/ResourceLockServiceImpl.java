package com.upgrade.volcanocamping.service;

import com.upgrade.volcanocamping.model.ResourceLock;
import com.upgrade.volcanocamping.repository.ResourceLockRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
public class ResourceLockServiceImpl implements ResourceLockService  {

    private static final Long INTERVAL = 1000L;

    private final ResourceLockRepository resourceLockRepository;

    @Autowired
    public ResourceLockServiceImpl(ResourceLockRepository resourceLockRepository) {
        this.resourceLockRepository = resourceLockRepository;
    }

    @Override
    public Optional<ResourceLock> acquire(String resourceId) {
        if(this.isResourceLocked(resourceId)) {
            return Optional.empty();
        }
        ResourceLock lock = ResourceLock.builder()
                .lockingTime(LocalDateTime.now())
                .resourceId(resourceId).build();
        return Optional.of(this.resourceLockRepository.save(lock));
    }

    @Override
    public void release(String resourceId) {
        Optional<ResourceLock> lock = this.resourceLockRepository.findByResourceId(resourceId);
        lock.ifPresent(entity -> {
            entity.setReleaseTime(LocalDateTime.now());
            this.resourceLockRepository.save(entity);
        });
    }

    @Override
    public Boolean isResourceLocked(String resourceId) {
        return this.resourceLockRepository.findByResourceId(resourceId).isPresent();
    }
}

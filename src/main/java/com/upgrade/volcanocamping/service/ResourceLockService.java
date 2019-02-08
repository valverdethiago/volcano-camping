package com.upgrade.volcanocamping.service;

import com.upgrade.volcanocamping.model.ResourceLock;

import java.util.Optional;

public interface ResourceLockService {

    Optional<ResourceLock> acquire(String resourceId);

    void release(String resourceId);

    Boolean isResourceLocked(String resourceId);
}

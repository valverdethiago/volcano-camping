package com.upgrade.volcanocamping.model;

import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "resource_lock")
@Data
@Builder
@EqualsAndHashCode(of={"id"})
@NoArgsConstructor
@AllArgsConstructor
public class ResourceLock {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name="resource_id")
    private String resourceId;
    @Column(name="locking_ts")
    private LocalDateTime lockingTime;
    @Column(name="release_ts")
    private LocalDateTime releaseTime;
}

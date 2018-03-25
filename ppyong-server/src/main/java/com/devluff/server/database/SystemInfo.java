package com.devluff.server.database;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

@Entity(name="system_info")
public class SystemInfo {
    @Id
    @Column(name="num")
    @GeneratedValue(strategy=GenerationType.AUTO)
    private long lNum;
    
    @ManyToOne(targetEntity=AgentInfo.class, fetch=FetchType.LAZY)
    @JoinColumn(name="agent_id")
    private AgentInfo oAgentInfo;

    @Column(name="insert_time")
    private long lInsertTime;
    
    @Column(name="cpu_usage")
    private double dCPUUsae;
    
    @Column(name="memory_usage")
    private double dMemoryUsage;
    
    @Column(name="disk_usage")
    private double dDiskUsage;
}

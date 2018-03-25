package com.devluff.server.database;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

@Entity(name="process_info")
public class ProcessInfo {
    @Id
    @Column(name="num")
    @GeneratedValue(strategy=GenerationType.AUTO)
    private long lNum;
    
    @ManyToOne(targetEntity=AgentInfo.class, fetch=FetchType.LAZY)
    @JoinColumn(name="agent_info_id")
    private AgentInfo oAgentInfo;
    
    @ManyToOne(targetEntity=SystemInfo.class, fetch=FetchType.LAZY)
    @JoinColumn(name="system_info_id")
    private SystemInfo oSystemInfo;

    @Column(name="process_pid")
    private int nProcessPID;
    
    @Column(name="process_ppid")
    private int nProcessPPID;
    
    @Column(name="command_line")
    private String strCommandLine;
    
    @Column(name="used_cpu_percent")
    private double lUsedCPUPercent;
    
    @Column(name="used_memory_size_mb")
    private long lUsedMemorySizeMB;
    
    @Column(name="used_disk_size_mb")
    private long lUsedDiskSizeMB;
}

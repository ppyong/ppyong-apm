package com.devluff.server.database;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;

@IdClass(AgentInfoPK.class)
@Entity(name="agent_info")
public class AgentInfo {
    @Id
    @Column(name="agent_ip")
    private String strAgentIP;
    
    @Id
    @Column(name="mac_address")
    private String strMacAddress;
    
    @Column(name="last_access_time")
    private long lLastAccessTime;
    
    @Column(name="agent_status")
    private int nAgentStatus;
    
    @Column(name="cpu_name")
    private String strCPUName;
    
    @Column(name="os_name")
    private String strOSName;
    
    @Column(name="total_memory_size_mb")
    private long lTotalMemorySizeMB;
    
    @Column(name="used_memory_size_mb")
    private long lUsedMemorySizeMB;
    
    @Column(name="total_disk_size_mb")
    private long lTotalDiskSizeMB;
    
    @Column(name="used_disk_size_mb")
    private long lUsedDiskSizeMB;
}

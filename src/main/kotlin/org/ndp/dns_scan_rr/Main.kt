package org.ndp.dns_scan_rr

import org.ndp.dns_scan_rr.bean.DNSServer
import org.ndp.dns_scan_rr.bean.Task
import org.ndp.dns_scan_rr.utils.DatabaseHandler
import org.ndp.dns_scan_rr.utils.Logger.logger
import org.ndp.dns_scan_rr.utils.OtherTools
import org.ndp.dns_scan_rr.utils.RedisHandler

object Main {
    @JvmStatic
    fun main(args: Array<String>) {
        logger.info("start result recycling...")
        val results = RedisHandler.consumeResult(RedisHandler.generateNonce(5))
        RedisHandler.returnACK()
        val updateTasks = ArrayList<Task>()
        val insertServers = ArrayList<DNSServer>()
        for (r in results) {
            // task status update
            if (r.status == 1) {
                updateTasks.add(Task(r.taskID, 21000, r.desc))
                continue
            }
            updateTasks.add(Task(r.taskID, 20030, ""))
            for (s in r.result) {
                val id = OtherTools.iNetString2Number(s)
                if (!DatabaseHandler.selectDNSID(id)) {
                    insertServers.add(DNSServer(id, s))
                }
            }
        }
        DatabaseHandler.batchInsertDNSServer(insertServers)
        DatabaseHandler.batchUpdateTaskStatus(updateTasks)
    }
}
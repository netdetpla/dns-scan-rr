package org.ndp.dns_scan_rr.utils

import me.liuwj.ktorm.database.Database
import me.liuwj.ktorm.dsl.*
import org.ndp.dns_scan_rr.bean.DNSServer
import org.ndp.dns_scan_rr.bean.Task
import org.ndp.dns_scan_rr.table.DNS
import org.ndp.dns_scan_rr.utils.Logger.logger
import org.ndp.dns_scan_rr.table.Task as TableTask

object DatabaseHandler {
    private val dbUrl = Settings.setting["dbUrl"] as String
    private val dbDriver = Settings.setting["dbDriver"] as String
    private val dbUser = Settings.setting["dbUser"] as String
    private val dbPassword = Settings.setting["dbPassword"] as String
    private val database: Database


    init {
        database = Database.Companion.connect(
            dbUrl,
            dbDriver,
            dbUser,
            dbPassword
        )
    }

    fun batchUpdateTaskStatus(updateTasks: List<Task>) {
        logger.debug("task size: ${updateTasks.size}")
        TableTask.batchUpdate {
            for (task in updateTasks) {
                item {
                    it.taskStatus to task.status
                    it.desc to task.desc
                    where {
                        TableTask.id eq task.id
                    }
                }
            }
        }
    }

    fun batchInsertDNSServer(insertServers: List<DNSServer>) {
        logger.debug("server size: ${insertServers.size}")
        DNS.batchInsert {
            for (i in insertServers) {
                item {
                    it.id to i.id
                    it.dnsIP to i.dnsIP
                }
            }
        }
    }

    fun selectDNSID(id: Long): Boolean {
        val result = DNS.select(DNS.id)
            .where { DNS.id eq id }
            .map { it[DNS.id]!! }
            .toList()
            .size
        return result != 0
    }
}
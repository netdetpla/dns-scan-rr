package org.ndp.dns_scan_rr.table

import me.liuwj.ktorm.schema.Table
import me.liuwj.ktorm.schema.long
import me.liuwj.ktorm.schema.varchar

object DNS : Table<Nothing>("dns") {
    val id by long("id").primaryKey()
    val dnsIP by varchar("dns_ip")
}
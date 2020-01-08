package com.example.grpcserviceclientdemo.app.tables

import org.jetbrains.exposed.dao.IntIdTable

object Workflows : IntIdTable() {
    val name = varchar("name", 255)
}

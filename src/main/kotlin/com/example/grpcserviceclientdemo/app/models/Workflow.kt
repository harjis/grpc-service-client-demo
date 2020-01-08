package com.example.grpcserviceclientdemo.app.models

import com.example.grpcserviceclientdemo.app.tables.Workflows
import org.jetbrains.exposed.dao.EntityID
import org.jetbrains.exposed.dao.IntEntity
import org.jetbrains.exposed.dao.IntEntityClass

class Workflow(id: EntityID<Int>) : IntEntity(id) {
    companion object : IntEntityClass<Workflow>(Workflows)

    var name by Workflows.name
}

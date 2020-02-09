databaseChangeLog() {
    changeSet(id: 'create-table', author: 'marchenko-ea') {
        comment "Initial tables create"
        createTable(schemaName: 'test', tableName: 'messages', remarks: 'Messages table') {
            column(name: 'id', type: 'bigint', autoIncrement: true, remarks: 'identifier') {
                constraints(nullable: false, primaryKey: true)
            }
            column(name: 'payload', type: 'varchar(1000)') {
                constraints(nullable: false)
            }
        }
        rollback {
            dropTable(tableName: 'messages')
        }
    }
}
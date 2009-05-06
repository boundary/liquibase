package liquibase.statement.generator;

import liquibase.database.*;
import liquibase.statement.DropColumnStatement;
import liquibase.statement.syntax.Sql;
import liquibase.statement.syntax.UnparsedSql;
import liquibase.exception.ValidationErrors;
import liquibase.exception.ValidationErrors;

public class DropColumnGenerator implements SqlGenerator<DropColumnStatement> {

    public int getSpecializationLevel() {
        return SPECIALIZATION_LEVEL_DEFAULT;
    }

    public boolean isValidGenerator(DropColumnStatement statement, Database database) {
        return true;
    }

    public ValidationErrors validate(DropColumnStatement dropColumnStatement, Database database) {
        return new ValidationErrors();
    }

    public Sql[] generateSql(DropColumnStatement statement, Database database) {
        if (database instanceof DB2Database) {
            return new Sql[] { new UnparsedSql("ALTER TABLE " + database.escapeTableName(statement.getSchemaName(), statement.getTableName()) + " DROP COLUMN " + database.escapeColumnName(statement.getSchemaName(), statement.getTableName(), statement.getColumnName())) };
        } else if (database instanceof SybaseDatabase || database instanceof SybaseASADatabase || database instanceof FirebirdDatabase || database instanceof InformixDatabase) {
            return new Sql[] { new UnparsedSql("ALTER TABLE " + database.escapeTableName(statement.getSchemaName(), statement.getTableName()) + " DROP " + database.escapeColumnName(statement.getSchemaName(), statement.getTableName(), statement.getColumnName())) };
        }
        return new Sql[] { new UnparsedSql("ALTER TABLE " + database.escapeTableName(statement.getSchemaName(), statement.getTableName()) + " DROP COLUMN " + database.escapeColumnName(statement.getSchemaName(), statement.getTableName(), statement.getColumnName())) };
    }
}
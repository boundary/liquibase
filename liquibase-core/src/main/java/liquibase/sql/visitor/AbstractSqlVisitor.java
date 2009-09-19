package liquibase.sql.visitor;

import liquibase.database.Database;

import java.util.Collection;

public abstract class AbstractSqlVisitor implements SqlVisitor {
    private Collection applicableDbms;
    private boolean appliedOnRollback;

    public Collection getApplicableDbms() {
        return applicableDbms;
    }

    public void setApplicableDbms(Collection applicableDbms) {
        this.applicableDbms = applicableDbms;
    }

    public boolean isApplicable(Database database) {
        return applicableDbms == null || applicableDbms.contains(database.getTypeName());

    }

    public boolean isAppliedOnRollback() {
        return appliedOnRollback;
    }

    public void setAppliedOnRollback(boolean appliedOnRollback) {
        this.appliedOnRollback = appliedOnRollback;
    }
}
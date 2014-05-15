package liquibase.snapshot;

import liquibase.exception.DatabaseException;
import liquibase.structure.DatabaseObject;
import liquibase.structure.core.Catalog;
import liquibase.structure.core.Schema;

import java.util.*;

public class SnapshotGeneratorChain {
    private Iterator<SnapshotGenerator> snapshotGenerators;

    private Set<Class<? extends SnapshotGenerator>> replacedGenerators = new HashSet<Class<? extends SnapshotGenerator>>();

    public SnapshotGeneratorChain(SortedSet<SnapshotGenerator> snapshotGenerators) {
        if (snapshotGenerators != null) {
            this.snapshotGenerators = snapshotGenerators.iterator();
        }

        for (SnapshotGenerator generator : snapshotGenerators) {
            Class<? extends SnapshotGenerator>[] replaces = generator.replaces();
            if (replaces != null && replaces.length > 0) {
                replacedGenerators.addAll(Arrays.asList(replaces));
            }
        }
    }

    public <T extends DatabaseObject> T snapshot(T example, DatabaseSnapshot snapshot) throws DatabaseException, InvalidExampleException {
        if (example == null) {
            return null;
        }

        if (snapshot.getDatabase().isSystemObject(example)) {
            return null;
        }

        if (!snapshot.getSnapshotControl().shouldInclude(example.getClass())) {
            return null;
        }

        SnapshotGenerator next = getNextValidGenerator();

        if (next == null) {
            return null;
        }

        T obj = next.snapshot(example, snapshot, this);
        if (obj != null && obj.getSnapshotId() == null) {
            obj.setSnapshotId(UUID.randomUUID());
        }
        return obj;
    }

    public SnapshotGenerator getNextValidGenerator() {
        if (snapshotGenerators == null) {
            return null;
        }

        if (!snapshotGenerators.hasNext()) {
            return null;
        }

        SnapshotGenerator next = snapshotGenerators.next();
        for (Class<? extends SnapshotGenerator> removedGenerator : replacedGenerators) {
            if (removedGenerator.isAssignableFrom(next.getClass())) {
                return getNextValidGenerator();
            }
        }
        return next;
    }
}
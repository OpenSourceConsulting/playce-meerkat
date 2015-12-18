package com.athena.dolly.controller.web.machine;

import static com.mysema.query.types.PathMetadataFactory.*;

import com.mysema.query.types.path.*;

import com.mysema.query.types.PathMetadata;
import javax.annotation.Generated;
import com.mysema.query.types.Path;
import com.mysema.query.types.path.PathInits;


/**
 * QDisk is a Querydsl query type for Disk
 */
@Generated("com.mysema.query.codegen.EntitySerializer")
public class QDisk extends EntityPathBase<Disk> {

    private static final long serialVersionUID = 738530514L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QDisk disk = new QDisk("disk");

    public final NumberPath<Double> capacity = createNumber("capacity", Double.class);

    public final StringPath fileSystem = createString("fileSystem");

    public final NumberPath<Double> freeCapacity = createNumber("freeCapacity", Double.class);

    public final NumberPath<Integer> Id = createNumber("Id", Integer.class);

    public final QMachine machine;

    public final StringPath name = createString("name");

    public final StringPath unit = createString("unit");

    public QDisk(String variable) {
        this(Disk.class, forVariable(variable), INITS);
    }

    public QDisk(Path<? extends Disk> path) {
        this(path.getType(), path.getMetadata(), path.getMetadata().isRoot() ? INITS : PathInits.DEFAULT);
    }

    public QDisk(PathMetadata<?> metadata) {
        this(metadata, metadata.isRoot() ? INITS : PathInits.DEFAULT);
    }

    public QDisk(PathMetadata<?> metadata, PathInits inits) {
        this(Disk.class, metadata, inits);
    }

    public QDisk(Class<? extends Disk> type, PathMetadata<?> metadata, PathInits inits) {
        super(type, metadata, inits);
        this.machine = inits.isInitialized("machine") ? new QMachine(forProperty("machine"), inits.get("machine")) : null;
    }

}


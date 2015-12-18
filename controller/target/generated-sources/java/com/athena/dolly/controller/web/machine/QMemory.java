package com.athena.dolly.controller.web.machine;

import static com.mysema.query.types.PathMetadataFactory.*;

import com.mysema.query.types.path.*;

import com.mysema.query.types.PathMetadata;
import javax.annotation.Generated;
import com.mysema.query.types.Path;
import com.mysema.query.types.path.PathInits;


/**
 * QMemory is a Querydsl query type for Memory
 */
@Generated("com.mysema.query.codegen.EntitySerializer")
public class QMemory extends EntityPathBase<Memory> {

    private static final long serialVersionUID = 1312017142L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QMemory memory = new QMemory("memory");

    public final NumberPath<Double> capacity = createNumber("capacity", Double.class);

    public final NumberPath<Integer> Id = createNumber("Id", Integer.class);

    public final QMachine machine;

    public final StringPath name = createString("name");

    public final StringPath type = createString("type");

    public final StringPath unit = createString("unit");

    public QMemory(String variable) {
        this(Memory.class, forVariable(variable), INITS);
    }

    public QMemory(Path<? extends Memory> path) {
        this(path.getType(), path.getMetadata(), path.getMetadata().isRoot() ? INITS : PathInits.DEFAULT);
    }

    public QMemory(PathMetadata<?> metadata) {
        this(metadata, metadata.isRoot() ? INITS : PathInits.DEFAULT);
    }

    public QMemory(PathMetadata<?> metadata, PathInits inits) {
        this(Memory.class, metadata, inits);
    }

    public QMemory(Class<? extends Memory> type, PathMetadata<?> metadata, PathInits inits) {
        super(type, metadata, inits);
        this.machine = inits.isInitialized("machine") ? new QMachine(forProperty("machine"), inits.get("machine")) : null;
    }

}


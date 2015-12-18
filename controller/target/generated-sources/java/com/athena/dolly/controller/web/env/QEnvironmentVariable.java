package com.athena.dolly.controller.web.env;

import static com.mysema.query.types.PathMetadataFactory.*;

import com.mysema.query.types.path.*;

import com.mysema.query.types.PathMetadata;
import javax.annotation.Generated;
import com.mysema.query.types.Path;
import com.mysema.query.types.path.PathInits;


/**
 * QEnvironmentVariable is a Querydsl query type for EnvironmentVariable
 */
@Generated("com.mysema.query.codegen.EntitySerializer")
public class QEnvironmentVariable extends EntityPathBase<EnvironmentVariable> {

    private static final long serialVersionUID = -1728304000L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QEnvironmentVariable environmentVariable = new QEnvironmentVariable("environmentVariable");

    public final NumberPath<Integer> Id = createNumber("Id", Integer.class);

    public final BooleanPath isNested = createBoolean("isNested");

    public final com.athena.dolly.controller.web.machine.QMachine machine;

    public final StringPath name = createString("name");

    public final NumberPath<Integer> revision = createNumber("revision", Integer.class);

    public final StringPath value = createString("value");

    public QEnvironmentVariable(String variable) {
        this(EnvironmentVariable.class, forVariable(variable), INITS);
    }

    public QEnvironmentVariable(Path<? extends EnvironmentVariable> path) {
        this(path.getType(), path.getMetadata(), path.getMetadata().isRoot() ? INITS : PathInits.DEFAULT);
    }

    public QEnvironmentVariable(PathMetadata<?> metadata) {
        this(metadata, metadata.isRoot() ? INITS : PathInits.DEFAULT);
    }

    public QEnvironmentVariable(PathMetadata<?> metadata, PathInits inits) {
        this(EnvironmentVariable.class, metadata, inits);
    }

    public QEnvironmentVariable(Class<? extends EnvironmentVariable> type, PathMetadata<?> metadata, PathInits inits) {
        super(type, metadata, inits);
        this.machine = inits.isInitialized("machine") ? new com.athena.dolly.controller.web.machine.QMachine(forProperty("machine"), inits.get("machine")) : null;
    }

}


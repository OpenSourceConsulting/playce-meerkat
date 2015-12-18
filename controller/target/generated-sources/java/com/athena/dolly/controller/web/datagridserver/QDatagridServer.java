package com.athena.dolly.controller.web.datagridserver;

import static com.mysema.query.types.PathMetadataFactory.*;

import com.mysema.query.types.path.*;

import com.mysema.query.types.PathMetadata;
import javax.annotation.Generated;
import com.mysema.query.types.Path;
import com.mysema.query.types.path.PathInits;


/**
 * QDatagridServer is a Querydsl query type for DatagridServer
 */
@Generated("com.mysema.query.codegen.EntitySerializer")
public class QDatagridServer extends EntityPathBase<DatagridServer> {

    private static final long serialVersionUID = 1378873216L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QDatagridServer datagridServer = new QDatagridServer("datagridServer");

    public final QDatagridServerGroup datagridServerGroup;

    public final NumberPath<Integer> Id = createNumber("Id", Integer.class);

    public final com.athena.dolly.controller.web.machine.QMachine machine;

    public final StringPath type = createString("type");

    public QDatagridServer(String variable) {
        this(DatagridServer.class, forVariable(variable), INITS);
    }

    public QDatagridServer(Path<? extends DatagridServer> path) {
        this(path.getType(), path.getMetadata(), path.getMetadata().isRoot() ? INITS : PathInits.DEFAULT);
    }

    public QDatagridServer(PathMetadata<?> metadata) {
        this(metadata, metadata.isRoot() ? INITS : PathInits.DEFAULT);
    }

    public QDatagridServer(PathMetadata<?> metadata, PathInits inits) {
        this(DatagridServer.class, metadata, inits);
    }

    public QDatagridServer(Class<? extends DatagridServer> type, PathMetadata<?> metadata, PathInits inits) {
        super(type, metadata, inits);
        this.datagridServerGroup = inits.isInitialized("datagridServerGroup") ? new QDatagridServerGroup(forProperty("datagridServerGroup"), inits.get("datagridServerGroup")) : null;
        this.machine = inits.isInitialized("machine") ? new com.athena.dolly.controller.web.machine.QMachine(forProperty("machine"), inits.get("machine")) : null;
    }

}


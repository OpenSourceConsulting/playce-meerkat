package com.athena.dolly.controller.web.datagridserver;

import static com.mysema.query.types.PathMetadataFactory.*;

import com.mysema.query.types.path.*;

import com.mysema.query.types.PathMetadata;
import javax.annotation.Generated;
import com.mysema.query.types.Path;
import com.mysema.query.types.path.PathInits;


/**
 * QDatagridServerGroup is a Querydsl query type for DatagridServerGroup
 */
@Generated("com.mysema.query.codegen.EntitySerializer")
public class QDatagridServerGroup extends EntityPathBase<DatagridServerGroup> {

    private static final long serialVersionUID = 1744291551L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QDatagridServerGroup datagridServerGroup = new QDatagridServerGroup("datagridServerGroup");

    public final CollectionPath<DatagridServer, QDatagridServer> datagridServers = this.<DatagridServer, QDatagridServer>createCollection("datagridServers", DatagridServer.class, QDatagridServer.class, PathInits.DIRECT2);

    public final com.athena.dolly.controller.web.domain.QDomain domain;

    public final NumberPath<Integer> Id = createNumber("Id", Integer.class);

    public final StringPath name = createString("name");

    public QDatagridServerGroup(String variable) {
        this(DatagridServerGroup.class, forVariable(variable), INITS);
    }

    public QDatagridServerGroup(Path<? extends DatagridServerGroup> path) {
        this(path.getType(), path.getMetadata(), path.getMetadata().isRoot() ? INITS : PathInits.DEFAULT);
    }

    public QDatagridServerGroup(PathMetadata<?> metadata) {
        this(metadata, metadata.isRoot() ? INITS : PathInits.DEFAULT);
    }

    public QDatagridServerGroup(PathMetadata<?> metadata, PathInits inits) {
        this(DatagridServerGroup.class, metadata, inits);
    }

    public QDatagridServerGroup(Class<? extends DatagridServerGroup> type, PathMetadata<?> metadata, PathInits inits) {
        super(type, metadata, inits);
        this.domain = inits.isInitialized("domain") ? new com.athena.dolly.controller.web.domain.QDomain(forProperty("domain"), inits.get("domain")) : null;
    }

}


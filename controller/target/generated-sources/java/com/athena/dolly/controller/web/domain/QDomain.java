package com.athena.dolly.controller.web.domain;

import static com.mysema.query.types.PathMetadataFactory.*;

import com.mysema.query.types.path.*;

import com.mysema.query.types.PathMetadata;
import javax.annotation.Generated;
import com.mysema.query.types.Path;
import com.mysema.query.types.path.PathInits;


/**
 * QDomain is a Querydsl query type for Domain
 */
@Generated("com.mysema.query.codegen.EntitySerializer")
public class QDomain extends EntityPathBase<Domain> {

    private static final long serialVersionUID = 1259969856L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QDomain domain = new QDomain("domain");

    public final NumberPath<Integer> id = createNumber("id", Integer.class);

    public final BooleanPath isClustering = createBoolean("isClustering");

    public final StringPath name = createString("name");

    public final com.athena.dolly.controller.web.datagridserver.QDatagridServerGroup serverGroup;

    public final CollectionPath<com.athena.dolly.controller.web.tomcat.instance.TomcatInstance, com.athena.dolly.controller.web.tomcat.instance.QTomcatInstance> tomcats = this.<com.athena.dolly.controller.web.tomcat.instance.TomcatInstance, com.athena.dolly.controller.web.tomcat.instance.QTomcatInstance>createCollection("tomcats", com.athena.dolly.controller.web.tomcat.instance.TomcatInstance.class, com.athena.dolly.controller.web.tomcat.instance.QTomcatInstance.class, PathInits.DIRECT2);

    public QDomain(String variable) {
        this(Domain.class, forVariable(variable), INITS);
    }

    public QDomain(Path<? extends Domain> path) {
        this(path.getType(), path.getMetadata(), path.getMetadata().isRoot() ? INITS : PathInits.DEFAULT);
    }

    public QDomain(PathMetadata<?> metadata) {
        this(metadata, metadata.isRoot() ? INITS : PathInits.DEFAULT);
    }

    public QDomain(PathMetadata<?> metadata, PathInits inits) {
        this(Domain.class, metadata, inits);
    }

    public QDomain(Class<? extends Domain> type, PathMetadata<?> metadata, PathInits inits) {
        super(type, metadata, inits);
        this.serverGroup = inits.isInitialized("serverGroup") ? new com.athena.dolly.controller.web.datagridserver.QDatagridServerGroup(forProperty("serverGroup"), inits.get("serverGroup")) : null;
    }

}


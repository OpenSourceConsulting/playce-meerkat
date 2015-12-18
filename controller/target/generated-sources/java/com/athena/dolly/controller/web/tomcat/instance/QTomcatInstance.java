package com.athena.dolly.controller.web.tomcat.instance;

import static com.mysema.query.types.PathMetadataFactory.*;

import com.mysema.query.types.path.*;

import com.mysema.query.types.PathMetadata;
import javax.annotation.Generated;
import com.mysema.query.types.Path;
import com.mysema.query.types.path.PathInits;


/**
 * QTomcatInstance is a Querydsl query type for TomcatInstance
 */
@Generated("com.mysema.query.codegen.EntitySerializer")
public class QTomcatInstance extends EntityPathBase<TomcatInstance> {

    private static final long serialVersionUID = 737657910L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QTomcatInstance tomcatInstance = new QTomcatInstance("tomcatInstance");

    public final NumberPath<Integer> ajpPort = createNumber("ajpPort", Integer.class);

    public final CollectionPath<com.athena.dolly.controller.web.application.Application, com.athena.dolly.controller.web.application.QApplication> applications = this.<com.athena.dolly.controller.web.application.Application, com.athena.dolly.controller.web.application.QApplication>createCollection("applications", com.athena.dolly.controller.web.application.Application.class, com.athena.dolly.controller.web.application.QApplication.class, PathInits.DIRECT2);

    public final CollectionPath<com.athena.dolly.controller.web.datasource.Datasource, com.athena.dolly.controller.web.datasource.QDatasource> datasources = this.<com.athena.dolly.controller.web.datasource.Datasource, com.athena.dolly.controller.web.datasource.QDatasource>createCollection("datasources", com.athena.dolly.controller.web.datasource.Datasource.class, com.athena.dolly.controller.web.datasource.QDatasource.class, PathInits.DIRECT2);

    public final com.athena.dolly.controller.web.domain.QDomain domain;

    public final NumberPath<Integer> httpPort = createNumber("httpPort", Integer.class);

    public final NumberPath<Integer> Id = createNumber("Id", Integer.class);

    public final com.athena.dolly.controller.web.machine.QMachine machine;

    public final StringPath name = createString("name");

    public final NumberPath<Integer> redirectPort = createNumber("redirectPort", Integer.class);

    public final NumberPath<Integer> state = createNumber("state", Integer.class);

    public QTomcatInstance(String variable) {
        this(TomcatInstance.class, forVariable(variable), INITS);
    }

    public QTomcatInstance(Path<? extends TomcatInstance> path) {
        this(path.getType(), path.getMetadata(), path.getMetadata().isRoot() ? INITS : PathInits.DEFAULT);
    }

    public QTomcatInstance(PathMetadata<?> metadata) {
        this(metadata, metadata.isRoot() ? INITS : PathInits.DEFAULT);
    }

    public QTomcatInstance(PathMetadata<?> metadata, PathInits inits) {
        this(TomcatInstance.class, metadata, inits);
    }

    public QTomcatInstance(Class<? extends TomcatInstance> type, PathMetadata<?> metadata, PathInits inits) {
        super(type, metadata, inits);
        this.domain = inits.isInitialized("domain") ? new com.athena.dolly.controller.web.domain.QDomain(forProperty("domain"), inits.get("domain")) : null;
        this.machine = inits.isInitialized("machine") ? new com.athena.dolly.controller.web.machine.QMachine(forProperty("machine"), inits.get("machine")) : null;
    }

}


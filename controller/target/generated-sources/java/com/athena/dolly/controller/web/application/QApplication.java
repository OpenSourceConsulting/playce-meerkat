package com.athena.dolly.controller.web.application;

import static com.mysema.query.types.PathMetadataFactory.*;

import com.mysema.query.types.path.*;

import com.mysema.query.types.PathMetadata;
import javax.annotation.Generated;
import com.mysema.query.types.Path;
import com.mysema.query.types.path.PathInits;


/**
 * QApplication is a Querydsl query type for Application
 */
@Generated("com.mysema.query.codegen.EntitySerializer")
public class QApplication extends EntityPathBase<Application> {

    private static final long serialVersionUID = -329753852L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QApplication application = new QApplication("application");

    public final StringPath contextPath = createString("contextPath");

    public final DateTimePath<java.util.Date> deployedDate = createDateTime("deployedDate", java.util.Date.class);

    public final StringPath displayName = createString("displayName");

    public final NumberPath<Integer> Id = createNumber("Id", Integer.class);

    public final DateTimePath<java.util.Date> lastModifiedDate = createDateTime("lastModifiedDate", java.util.Date.class);

    public final DateTimePath<java.util.Date> lastReloaddedDate = createDateTime("lastReloaddedDate", java.util.Date.class);

    public final DateTimePath<java.util.Date> lastStartedDate = createDateTime("lastStartedDate", java.util.Date.class);

    public final DateTimePath<java.util.Date> lastStoppedDate = createDateTime("lastStoppedDate", java.util.Date.class);

    public final CollectionPath<com.athena.dolly.controller.web.session.Session, com.athena.dolly.controller.web.session.QSession> sessions = this.<com.athena.dolly.controller.web.session.Session, com.athena.dolly.controller.web.session.QSession>createCollection("sessions", com.athena.dolly.controller.web.session.Session.class, com.athena.dolly.controller.web.session.QSession.class, PathInits.DIRECT2);

    public final NumberPath<Integer> state = createNumber("state", Integer.class);

    public final com.athena.dolly.controller.web.tomcat.instance.QTomcatInstance tomcat;

    public final StringPath version = createString("version");

    public final StringPath warPath = createString("warPath");

    public QApplication(String variable) {
        this(Application.class, forVariable(variable), INITS);
    }

    public QApplication(Path<? extends Application> path) {
        this(path.getType(), path.getMetadata(), path.getMetadata().isRoot() ? INITS : PathInits.DEFAULT);
    }

    public QApplication(PathMetadata<?> metadata) {
        this(metadata, metadata.isRoot() ? INITS : PathInits.DEFAULT);
    }

    public QApplication(PathMetadata<?> metadata, PathInits inits) {
        this(Application.class, metadata, inits);
    }

    public QApplication(Class<? extends Application> type, PathMetadata<?> metadata, PathInits inits) {
        super(type, metadata, inits);
        this.tomcat = inits.isInitialized("tomcat") ? new com.athena.dolly.controller.web.tomcat.instance.QTomcatInstance(forProperty("tomcat"), inits.get("tomcat")) : null;
    }

}


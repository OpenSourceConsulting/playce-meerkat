package com.athena.dolly.controller.web.datasource;

import static com.mysema.query.types.PathMetadataFactory.*;

import com.mysema.query.types.path.*;

import com.mysema.query.types.PathMetadata;
import javax.annotation.Generated;
import com.mysema.query.types.Path;
import com.mysema.query.types.path.PathInits;


/**
 * QDatasource is a Querydsl query type for Datasource
 */
@Generated("com.mysema.query.codegen.EntitySerializer")
public class QDatasource extends EntityPathBase<Datasource> {

    private static final long serialVersionUID = -1501713504L;

    public static final QDatasource datasource = new QDatasource("datasource");

    public final StringPath dbType = createString("dbType");

    public final NumberPath<Integer> Id = createNumber("Id", Integer.class);

    public final StringPath jdbcUrl = createString("jdbcUrl");

    public final NumberPath<Integer> maxConnection = createNumber("maxConnection", Integer.class);

    public final NumberPath<Integer> maxConnectionPool = createNumber("maxConnectionPool", Integer.class);

    public final NumberPath<Integer> minConnectionPool = createNumber("minConnectionPool", Integer.class);

    public final StringPath name = createString("name");

    public final StringPath password = createString("password");

    public final NumberPath<Integer> timeout = createNumber("timeout", Integer.class);

    public final CollectionPath<com.athena.dolly.controller.web.tomcat.instance.TomcatInstance, com.athena.dolly.controller.web.tomcat.instance.QTomcatInstance> tomcatInstances = this.<com.athena.dolly.controller.web.tomcat.instance.TomcatInstance, com.athena.dolly.controller.web.tomcat.instance.QTomcatInstance>createCollection("tomcatInstances", com.athena.dolly.controller.web.tomcat.instance.TomcatInstance.class, com.athena.dolly.controller.web.tomcat.instance.QTomcatInstance.class, PathInits.DIRECT2);

    public final StringPath userName = createString("userName");

    public QDatasource(String variable) {
        super(Datasource.class, forVariable(variable));
    }

    public QDatasource(Path<? extends Datasource> path) {
        super(path.getType(), path.getMetadata());
    }

    public QDatasource(PathMetadata<?> metadata) {
        super(Datasource.class, metadata);
    }

}


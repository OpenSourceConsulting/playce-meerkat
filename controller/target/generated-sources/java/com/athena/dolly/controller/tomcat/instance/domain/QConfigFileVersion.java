package com.athena.dolly.controller.tomcat.instance.domain;

import static com.mysema.query.types.PathMetadataFactory.*;

import com.mysema.query.types.path.*;

import com.mysema.query.types.PathMetadata;
import javax.annotation.Generated;
import com.mysema.query.types.Path;


/**
 * QConfigFileVersion is a Querydsl query type for ConfigFileVersion
 */
@Generated("com.mysema.query.codegen.EntitySerializer")
public class QConfigFileVersion extends EntityPathBase<ConfigFileVersion> {

    private static final long serialVersionUID = -1686484269L;

    public static final QConfigFileVersion configFileVersion = new QConfigFileVersion("configFileVersion");

    public final StringPath contents = createString("contents");

    public final StringPath fileName = createString("fileName");

    public final NumberPath<Integer> fileType = createNumber("fileType", Integer.class);

    public final DateTimePath<java.util.Date> regDate = createDateTime("regDate", java.util.Date.class);

    public final NumberPath<Integer> revision = createNumber("revision", Integer.class);

    public final NumberPath<Long> tomcatInstanceId = createNumber("tomcatInstanceId", Long.class);

    public QConfigFileVersion(String variable) {
        super(ConfigFileVersion.class, forVariable(variable));
    }

    public QConfigFileVersion(Path<? extends ConfigFileVersion> path) {
        super(path.getType(), path.getMetadata());
    }

    public QConfigFileVersion(PathMetadata<?> metadata) {
        super(ConfigFileVersion.class, metadata);
    }

}


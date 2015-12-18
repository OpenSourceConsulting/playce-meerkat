package com.athena.dolly.controller.web.machine;

import static com.mysema.query.types.PathMetadataFactory.*;

import com.mysema.query.types.path.*;

import com.mysema.query.types.PathMetadata;
import javax.annotation.Generated;
import com.mysema.query.types.Path;
import com.mysema.query.types.path.PathInits;


/**
 * QNetworkInterface is a Querydsl query type for NetworkInterface
 */
@Generated("com.mysema.query.codegen.EntitySerializer")
public class QNetworkInterface extends EntityPathBase<NetworkInterface> {

    private static final long serialVersionUID = -54417824L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QNetworkInterface networkInterface = new QNetworkInterface("networkInterface");

    public final StringPath default_gateway = createString("default_gateway");

    public final NumberPath<Integer> Id = createNumber("Id", Integer.class);

    public final StringPath ipv4 = createString("ipv4");

    public final StringPath ipv6 = createString("ipv6");

    public final QMachine machine;

    public final StringPath name = createString("name");

    public final StringPath netmask = createString("netmask");

    public QNetworkInterface(String variable) {
        this(NetworkInterface.class, forVariable(variable), INITS);
    }

    public QNetworkInterface(Path<? extends NetworkInterface> path) {
        this(path.getType(), path.getMetadata(), path.getMetadata().isRoot() ? INITS : PathInits.DEFAULT);
    }

    public QNetworkInterface(PathMetadata<?> metadata) {
        this(metadata, metadata.isRoot() ? INITS : PathInits.DEFAULT);
    }

    public QNetworkInterface(PathMetadata<?> metadata, PathInits inits) {
        this(NetworkInterface.class, metadata, inits);
    }

    public QNetworkInterface(Class<? extends NetworkInterface> type, PathMetadata<?> metadata, PathInits inits) {
        super(type, metadata, inits);
        this.machine = inits.isInitialized("machine") ? new QMachine(forProperty("machine"), inits.get("machine")) : null;
    }

}


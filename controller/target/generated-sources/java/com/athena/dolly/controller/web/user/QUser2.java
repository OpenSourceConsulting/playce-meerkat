package com.athena.dolly.controller.web.user;

import static com.mysema.query.types.PathMetadataFactory.*;

import com.mysema.query.types.path.*;

import com.mysema.query.types.PathMetadata;
import javax.annotation.Generated;
import com.mysema.query.types.Path;
import com.mysema.query.types.path.PathInits;


/**
 * QUser2 is a Querydsl query type for User2
 */
@Generated("com.mysema.query.codegen.EntitySerializer")
public class QUser2 extends EntityPathBase<User2> {

    private static final long serialVersionUID = 1048401682L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QUser2 user2 = new QUser2("user2");

    public final DateTimePath<java.util.Date> createdDate = createDateTime("createdDate", java.util.Date.class);

    public final StringPath email = createString("email");

    public final StringPath fullName = createString("fullName");

    public final NumberPath<Integer> Id = createNumber("Id", Integer.class);

    public final DateTimePath<java.util.Date> lastLoginDate = createDateTime("lastLoginDate", java.util.Date.class);

    public final StringPath password = createString("password");

    public final StringPath userName = createString("userName");

    public final QUserRole2 userRole;

    public QUser2(String variable) {
        this(User2.class, forVariable(variable), INITS);
    }

    public QUser2(Path<? extends User2> path) {
        this(path.getType(), path.getMetadata(), path.getMetadata().isRoot() ? INITS : PathInits.DEFAULT);
    }

    public QUser2(PathMetadata<?> metadata) {
        this(metadata, metadata.isRoot() ? INITS : PathInits.DEFAULT);
    }

    public QUser2(PathMetadata<?> metadata, PathInits inits) {
        this(User2.class, metadata, inits);
    }

    public QUser2(Class<? extends User2> type, PathMetadata<?> metadata, PathInits inits) {
        super(type, metadata, inits);
        this.userRole = inits.isInitialized("userRole") ? new QUserRole2(forProperty("userRole")) : null;
    }

}

